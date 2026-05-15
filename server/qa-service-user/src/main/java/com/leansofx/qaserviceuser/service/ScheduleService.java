package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.dto.ScheduleRequest;
import com.leansofx.qaserviceuser.entity.DoctorSchedule;
import com.leansofx.qaserviceuser.repository.DoctorScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 排班服务类
 */
@Service
@Transactional
public class ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    /**
     * 创建排班
     */
    public DoctorSchedule createSchedule(ScheduleRequest request) {
        logger.info("创建排班: doctorId={}, date={}, timeSlot={}",
                    request.getDoctorId(), request.getScheduleDate(), request.getTimeSlot());

        // 检查是否已存在相同医生和时间的排班
        Optional<DoctorSchedule> existingSchedule =
            doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                request.getDoctorId(),
                request.getScheduleDate(),
                request.getTimeSlot()
            );

        if (existingSchedule.isPresent()) {
            logger.warn("排班已存在: doctorId={}, date={}, timeSlot={}",
                        request.getDoctorId(), request.getScheduleDate(), request.getTimeSlot());
            throw new RuntimeException("该时间段的排班已存在");
        }

        // 创建排班记录
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId(generateScheduleId());
        schedule.setDoctorId(request.getDoctorId());
        schedule.setDoctorName(request.getDoctorName());
        schedule.setScheduleDate(request.getScheduleDate());
        schedule.setTimeSlot(request.getTimeSlot());
        schedule.setLocation(request.getLocation());
        schedule.setMaxAppointments(request.getMaxAppointments());
        schedule.setCurrentAppointments(0);
        schedule.setStatus("AVAILABLE");

        DoctorSchedule savedSchedule = doctorScheduleRepository.save(schedule);
        logger.info("排班创建成功: id={}", savedSchedule.getId());

        return savedSchedule;
    }

    /**
     * 根据ID获取排班详情
     */
    @Transactional(readOnly = true)
    public DoctorSchedule getScheduleById(String scheduleId) {
        logger.info("查询排班详情: scheduleId={}", scheduleId);

        Optional<DoctorSchedule> scheduleOpt = doctorScheduleRepository.findById(scheduleId);
        if (scheduleOpt.isEmpty()) {
            logger.warn("排班不存在: scheduleId={}", scheduleId);
            throw new RuntimeException("排班不存在");
        }

        return scheduleOpt.get();
    }

    /**
     * 获取医生的排班列表
     */
    @Transactional(readOnly = true)
    public List<DoctorSchedule> getSchedulesByDoctorId(String doctorId, LocalDate startDate, LocalDate endDate) {
        logger.info("查询医生排班列表: doctorId={}, startDate={}, endDate={}",
                    doctorId, startDate, endDate);

        // 设置默认日期范围
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(7);
        }

        return doctorScheduleRepository.findByDoctorIdAndScheduleDateBetweenOrderByScheduleDateAscTimeSlotAsc(
            doctorId, startDate, endDate);
    }

    /**
     * 查询可用时间段（患者预约时使用）
     */
    @Transactional(readOnly = true)
    public List<DoctorSchedule> getAvailableSchedules(String doctorId, LocalDate startDate, LocalDate endDate) {
        logger.info("查询可用时间段: doctorId={}, startDate={}, endDate={}",
                    doctorId, startDate, endDate);

        // 设置默认日期范围
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(7);
        }

        // 查询可用排班
        List<DoctorSchedule> schedules =
            doctorScheduleRepository.findByDoctorIdAndScheduleDateBetweenOrderByScheduleDateAscTimeSlotAsc(
                doctorId, startDate, endDate);

        // 过滤状态为 AVAILABLE 且还有剩余名额的排班
        return schedules.stream()
            .filter(s -> "AVAILABLE".equals(s.getStatus()))
            .filter(s -> s.getCurrentAppointments() < s.getMaxAppointments())
            .toList();
    }

    /**
     * 更新排班
     */
    public DoctorSchedule updateSchedule(String scheduleId, ScheduleRequest request) {
        logger.info("更新排班: scheduleId={}", scheduleId);

        DoctorSchedule schedule = getScheduleById(scheduleId);

        // 更新排班信息
        if (request.getLocation() != null) {
            schedule.setLocation(request.getLocation());
        }
        if (request.getMaxAppointments() != null) {
            schedule.setMaxAppointments(request.getMaxAppointments());
        }
        schedule.setUpdateTime(LocalDateTime.now());

        DoctorSchedule updatedSchedule = doctorScheduleRepository.save(schedule);
        logger.info("排班更新成功: id={}", updatedSchedule.getId());

        return updatedSchedule;
    }

    /**
     * 删除排班
     */
    public void deleteSchedule(String scheduleId) {
        logger.info("删除排班: scheduleId={}", scheduleId);

        DoctorSchedule schedule = getScheduleById(scheduleId);

        // 检查是否已有关联的预约
        if (schedule.getCurrentAppointments() > 0) {
            logger.warn("排班已有关联的预约，无法删除: scheduleId={}, currentAppointments={}",
                        scheduleId, schedule.getCurrentAppointments());
            throw new RuntimeException("排班已有关联的预约，无法删除");
        }

        doctorScheduleRepository.delete(schedule);
        logger.info("排班删除成功: id={}", scheduleId);
    }

    /**
     * 生成排班ID
     */
    private String generateScheduleId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "SCH" + timestamp;
    }
}
