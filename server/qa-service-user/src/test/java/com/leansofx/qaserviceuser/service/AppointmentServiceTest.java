package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.entity.Appointment;
import com.leansofx.qaserviceuser.entity.Doctor;
import com.leansofx.qaserviceuser.entity.DoctorSchedule;
import com.leansofx.qaserviceuser.entity.Patient;
import com.leansofx.qaserviceuser.repository.AppointmentRepository;
import com.leansofx.qaserviceuser.repository.DoctorRepository;
import com.leansofx.qaserviceuser.repository.DoctorScheduleRepository;
import com.leansofx.qaserviceuser.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AppointmentService 单元测试
 */
@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient testPatient;
    private Doctor testDoctor;
    private DoctorSchedule testSchedule;
    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        testPatient = new Patient();
        testPatient.setId("patient001");
        testPatient.setName("张三");

        testDoctor = new Doctor();
        testDoctor.setId("doctor001");
        testDoctor.setName("张伟医生");

        testSchedule = new DoctorSchedule();
        testSchedule.setId("schedule001");
        testSchedule.setDoctorId("doctor001");
        testSchedule.setScheduleDate(LocalDate.now().plusDays(1));
        testSchedule.setTimeSlot("09:00-09:30");
        testSchedule.setStatus("AVAILABLE");
        testSchedule.setMaxAppointments(10);
        testSchedule.setCurrentAppointments(0);

        testAppointment = new Appointment();
        testAppointment.setId("apt001");
        testAppointment.setAppointmentNo("APT123456789");
        testAppointment.setPatientId("patient001");
        testAppointment.setDoctorId("doctor001");
        testAppointment.setAppointmentDate(LocalDate.now().plusDays(1));
        testAppointment.setTimeSlot("09:00-09:30");
        testAppointment.setStatus("PENDING");
    }

    @Test
    @DisplayName("创建预约成功")
    void createAppointmentSuccess() {
        // Arrange
        when(patientRepository.findById("patient001")).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById("doctor001")).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                eq("doctor001"), any(LocalDate.class), eq("09:00-09:30")))
                .thenReturn(Optional.of(testSchedule));
        when(appointmentRepository.findByPatientIdAndAppointmentDateAndTimeSlotAndStatusNot(
                anyString(), any(LocalDate.class), anyString(), anyString()))
                .thenReturn(new ArrayList<>());
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(testSchedule);
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment apt = invocation.getArgument(0);
            apt.setId("apt001");
            apt.setAppointmentNo("APT123456789");
            return apt;
        });

        // Act
        Map<String, Object> result = appointmentService.createAppointment(
                "patient001", "doctor001",
                LocalDate.now().plusDays(1), "09:00-09:30",
                "门诊楼301", "感冒咳嗽");

        // Assert
        assertNotNull(result);
        assertEquals("patient001", result.get("patientId"));
        assertEquals("doctor001", result.get("doctorId"));
        assertEquals("PENDING", result.get("status"));
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
        verify(doctorScheduleRepository, times(1)).save(any(DoctorSchedule.class));
    }

    @Test
    @DisplayName("创建预约失败 - 患者不存在")
    void createAppointmentFailurePatientNotFound() {
        // Arrange
        when(patientRepository.findById("patient999")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(
                    "patient999", "doctor001",
                    LocalDate.now().plusDays(1), "09:00-09:30",
                    null, null);
        });

        assertTrue(exception.getMessage().contains("Patient not found"));
    }

    @Test
    @DisplayName("创建预约失败 - 医生不存在")
    void createAppointmentFailureDoctorNotFound() {
        // Arrange
        when(patientRepository.findById("patient001")).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById("doctor999")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(
                    "patient001", "doctor999",
                    LocalDate.now().plusDays(1), "09:00-09:30",
                    null, null);
        });

        assertTrue(exception.getMessage().contains("Doctor not found"));
    }

    @Test
    @DisplayName("创建预约失败 - 排班不可用")
    void createAppointmentFailureScheduleNotAvailable() {
        // Arrange
        when(patientRepository.findById("patient001")).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById("doctor001")).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                anyString(), any(LocalDate.class), anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(
                    "patient001", "doctor001",
                    LocalDate.now().plusDays(1), "09:00-09:30",
                    null, null);
        });

        assertTrue(exception.getMessage().contains("Doctor schedule not available"));
    }

    @Test
    @DisplayName("创建预约失败 - 排班已满")
    void createAppointmentFailureScheduleFull() {
        // Arrange
        testSchedule.setCurrentAppointments(10);
        testSchedule.setMaxAppointments(10);

        when(patientRepository.findById("patient001")).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById("doctor001")).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                anyString(), any(LocalDate.class), anyString()))
                .thenReturn(Optional.of(testSchedule));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(
                    "patient001", "doctor001",
                    LocalDate.now().plusDays(1), "09:00-09:30",
                    null, null);
        });

        assertTrue(exception.getMessage().contains("Doctor schedule is full"));
    }

    @Test
    @DisplayName("创建预约失败 - 预约冲突")
    void createAppointmentFailureConflict() {
        // Arrange
        List<Appointment> conflictAppointments = new ArrayList<>();
        conflictAppointments.add(testAppointment);

        when(patientRepository.findById("patient001")).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById("doctor001")).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                anyString(), any(LocalDate.class), anyString()))
                .thenReturn(Optional.of(testSchedule));
        when(appointmentRepository.findByPatientIdAndAppointmentDateAndTimeSlotAndStatusNot(
                anyString(), any(LocalDate.class), anyString(), anyString()))
                .thenReturn(conflictAppointments);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(
                    "patient001", "doctor001",
                    LocalDate.now().plusDays(1), "09:00-09:30",
                    null, null);
        });

        assertTrue(exception.getMessage().contains("Appointment conflict"));
    }

    @Test
    @DisplayName("根据预约单号查询预约成功")
    void getAppointmentByNoSuccess() {
        // Arrange
        when(appointmentRepository.findByAppointmentNo("APT123456789"))
                .thenReturn(Optional.of(testAppointment));

        // Act
        Map<String, Object> result = appointmentService.getAppointmentByNo("APT123456789");

        // Assert
        assertNotNull(result);
        assertEquals("apt001", result.get("id"));
        assertEquals("APT123456789", result.get("appointmentNo"));
    }

    @Test
    @DisplayName("根据预约单号查询预约失败 - 预约不存在")
    void getAppointmentByNoFailureNotFound() {
        // Arrange
        when(appointmentRepository.findByAppointmentNo("APT999"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.getAppointmentByNo("APT999");
        });

        assertTrue(exception.getMessage().contains("Appointment not found"));
    }

    @Test
    @DisplayName("根据患者ID查询预约记录")
    void getAppointmentsByPatientIdSuccess() {
        // Arrange
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(testAppointment);
        when(appointmentRepository.findByPatientIdOrderByAppointmentDateDesc("patient001"))
                .thenReturn(appointments);

        // Act
        List<Map<String, Object>> result = appointmentService.getAppointmentsByPatientId("patient001", null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("apt001", result.get(0).get("id"));
    }

    @Test
    @DisplayName("取消预约成功")
    void cancelAppointmentSuccess() {
        // Arrange
        testAppointment.setStatus("PENDING");
        testSchedule.setCurrentAppointments(1); // 确保会调用 save
        when(appointmentRepository.findByAppointmentNo("APT123456789"))
                .thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                anyString(), any(LocalDate.class), anyString()))
                .thenReturn(Optional.of(testSchedule));
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(testSchedule);

        // Act
        Map<String, Object> result = appointmentService.cancelAppointment("APT123456789");

        // Assert
        assertNotNull(result);
        assertEquals("CANCELLED", result.get("status"));
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("取消预约失败 - 预约状态不正确")
    void cancelAppointmentFailureWrongStatus() {
        // Arrange
        testAppointment.setStatus("COMPLETED");
        when(appointmentRepository.findByAppointmentNo("APT123456789"))
                .thenReturn(Optional.of(testAppointment));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.cancelAppointment("APT123456789");
        });

        assertTrue(exception.getMessage().contains("Cannot cancel appointment"));
    }

    @Test
    @DisplayName("确认预约成功")
    void confirmAppointmentSuccess() {
        // Arrange
        testAppointment.setStatus("PENDING");
        when(appointmentRepository.findByAppointmentNo("APT123456789"))
                .thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Map<String, Object> result = appointmentService.confirmAppointment("APT123456789");

        // Assert
        assertNotNull(result);
        assertEquals("CONFIRMED", result.get("status"));
    }

    @Test
    @DisplayName("完成预约成功")
    void completeAppointmentSuccess() {
        // Arrange
        testAppointment.setStatus("CONFIRMED");
        when(appointmentRepository.findByAppointmentNo("APT123456789"))
                .thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Map<String, Object> result = appointmentService.completeAppointment("APT123456789");

        // Assert
        assertNotNull(result);
        assertEquals("COMPLETED", result.get("status"));
    }
}
