package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.entity.Appointment;
import com.leansofx.qaserviceuser.entity.Doctor;
import com.leansofx.qaserviceuser.entity.DoctorSchedule;
import com.leansofx.qaserviceuser.entity.Patient;
import com.leansofx.qaserviceuser.repository.AppointmentRepository;
import com.leansofx.qaserviceuser.repository.DoctorRepository;
import com.leansofx.qaserviceuser.repository.DoctorScheduleRepository;
import com.leansofx.qaserviceuser.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * 创建预约
     */
    @Transactional
    public Map<String, Object> createAppointment(String patientId, String doctorId,
                                                 LocalDate appointmentDate, String timeSlot,
                                                 String location, String description) {
        Map<String, Object> result = new HashMap<>();

        // 1. 验证患者是否存在
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found");
        }
        Patient patient = patientOpt.get();

        // 2. 验证医生是否存在
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            throw new RuntimeException("Doctor not found");
        }
        Doctor doctor = doctorOpt.get();

        // 3. 检查医生排班是否存在且可用
        Optional<DoctorSchedule> scheduleOpt = doctorScheduleRepository
                .findByDoctorIdAndScheduleDateAndTimeSlot(doctorId, appointmentDate, timeSlot);
        if (scheduleOpt.isEmpty()) {
            throw new RuntimeException("Doctor schedule not available");
        }
        DoctorSchedule schedule = scheduleOpt.get();

        // 4. 检查排班状态
        if (!"AVAILABLE".equals(schedule.getStatus())) {
            throw new RuntimeException("Doctor schedule not available");
        }

        // 5. 检查是否已满
        if (schedule.getCurrentAppointments() >= schedule.getMaxAppointments()) {
            throw new RuntimeException("Doctor schedule is full");
        }

        // 6. 检查预约冲突（同一患者同一时间段不能预约多个医生）
        List<Appointment> conflictAppointments = appointmentRepository
                .findByPatientIdAndAppointmentDateAndTimeSlotAndStatusNot(
                        patientId, appointmentDate, timeSlot, "CANCELLED");
        if (!conflictAppointments.isEmpty()) {
            throw new RuntimeException("Appointment conflict: you already have an appointment at this time");
        }

        // 7. 创建预约记录
        Appointment appointment = new Appointment();
        appointment.setId(java.util.UUID.randomUUID().toString());
        appointment.setPatientId(patientId);
        appointment.setPatientName(patient.getName());
        appointment.setDoctorId(doctorId);
        appointment.setDoctorName(doctor.getName());
        appointment.setAppointmentDate(appointmentDate);
        appointment.setTimeSlot(timeSlot);
        appointment.setLocation(location != null ? location : schedule.getLocation());
        appointment.setStatus("PENDING");
        appointment.setDescription(description);
        appointment.setAppointmentNo("APT" + System.currentTimeMillis());

        // 8. 更新医生排班的当前预约数
        schedule.setCurrentAppointments(schedule.getCurrentAppointments() + 1);
        doctorScheduleRepository.save(schedule);

        // 9. 保存预约
        appointment = appointmentRepository.save(appointment);

        // 10. 返回预约详情
        result.put("id", appointment.getId());
        result.put("patientId", appointment.getPatientId());
        result.put("patientName", appointment.getPatientName());
        result.put("doctorId", appointment.getDoctorId());
        result.put("doctorName", appointment.getDoctorName());
        result.put("appointmentDate", appointment.getAppointmentDate());
        result.put("timeSlot", appointment.getTimeSlot());
        result.put("location", appointment.getLocation());
        result.put("status", appointment.getStatus());
        result.put("description", appointment.getDescription());
        result.put("appointmentNo", appointment.getAppointmentNo());
        result.put("createTime", appointment.getCreateTime());

        return result;
    }

    /**
     * 根据预约单号查询预约
     */
    public Map<String, Object> getAppointmentByNo(String appointmentNo) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findByAppointmentNo(appointmentNo);
        if (appointmentOpt.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }

        Appointment appointment = appointmentOpt.get();
        return appointmentToMap(appointment);
    }

    /**
     * 根据患者ID查询预约记录
     */
    public List<Map<String, Object>> getAppointmentsByPatientId(String patientId, String status) {
        List<Appointment> appointments;

        if (status != null && !status.isEmpty()) {
            appointments = appointmentRepository.findByPatientIdAndStatusOrderByAppointmentDateDesc(patientId, status);
        } else {
            appointments = appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patientId);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            result.add(appointmentToMap(appointment));
        }

        return result;
    }

    /**
     * 取消预约
     */
    @Transactional
    public Map<String, Object> cancelAppointment(String appointmentNo) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findByAppointmentNo(appointmentNo);
        if (appointmentOpt.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }

        Appointment appointment = appointmentOpt.get();

        // 检查预约状态（只有 PENDING 和 CONFIRMED 可以取消）
        if (!"PENDING".equals(appointment.getStatus()) && !"CONFIRMED".equals(appointment.getStatus())) {
            throw new RuntimeException("Cannot cancel appointment with status: " + appointment.getStatus());
        }

        // 更新预约状态
        appointment.setStatus("CANCELLED");
        appointment = appointmentRepository.save(appointment);

        // 减少医生排班的当前预约数
        Optional<DoctorSchedule> scheduleOpt = doctorScheduleRepository
                .findByDoctorIdAndScheduleDateAndTimeSlot(
                        appointment.getDoctorId(),
                        appointment.getAppointmentDate(),
                        appointment.getTimeSlot());
        if (scheduleOpt.isPresent()) {
            DoctorSchedule schedule = scheduleOpt.get();
            if (schedule.getCurrentAppointments() > 0) {
                schedule.setCurrentAppointments(schedule.getCurrentAppointments() - 1);
                doctorScheduleRepository.save(schedule);
            }
        }

        return appointmentToMap(appointment);
    }

    /**
     * 确认预约（医生）
     */
    @Transactional
    public Map<String, Object> confirmAppointment(String appointmentNo) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findByAppointmentNo(appointmentNo);
        if (appointmentOpt.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }

        Appointment appointment = appointmentOpt.get();

        // 检查预约状态
        if (!"PENDING".equals(appointment.getStatus())) {
            throw new RuntimeException("Cannot confirm appointment with status: " + appointment.getStatus());
        }

        // 更新预约状态
        appointment.setStatus("CONFIRMED");
        appointment = appointmentRepository.save(appointment);

        return appointmentToMap(appointment);
    }

    /**
     * 完成预约（医生）
     */
    @Transactional
    public Map<String, Object> completeAppointment(String appointmentNo) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findByAppointmentNo(appointmentNo);
        if (appointmentOpt.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }

        Appointment appointment = appointmentOpt.get();

        // 检查预约状态
        if (!"CONFIRMED".equals(appointment.getStatus())) {
            throw new RuntimeException("Cannot complete appointment with status: " + appointment.getStatus());
        }

        // 更新预约状态
        appointment.setStatus("COMPLETED");
        appointment = appointmentRepository.save(appointment);

        return appointmentToMap(appointment);
    }

    /**
     * 将 Appointment 实体转换为 Map
     */
    private Map<String, Object> appointmentToMap(Appointment appointment) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", appointment.getId());
        map.put("patientId", appointment.getPatientId());
        map.put("patientName", appointment.getPatientName());
        map.put("doctorId", appointment.getDoctorId());
        map.put("doctorName", appointment.getDoctorName());
        map.put("appointmentDate", appointment.getAppointmentDate());
        map.put("timeSlot", appointment.getTimeSlot());
        map.put("location", appointment.getLocation());
        map.put("status", appointment.getStatus());
        map.put("description", appointment.getDescription());
        map.put("appointmentNo", appointment.getAppointmentNo());
        map.put("createTime", appointment.getCreateTime());
        map.put("updateTime", appointment.getUpdateTime());
        return map;
    }
}
