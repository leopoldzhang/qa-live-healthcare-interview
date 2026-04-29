package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.dto.ScheduleRequest;
import com.leansofx.qaserviceuser.entity.DoctorSchedule;
import com.leansofx.qaserviceuser.repository.DoctorScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ScheduleService 单元测试
 */
@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private ScheduleRequest testRequest;
    private DoctorSchedule testSchedule;

    @BeforeEach
    void setUp() {
        testRequest = new ScheduleRequest();
        testRequest.setDoctorId("doctor001");
        testRequest.setDoctorName("张伟医生");
        testRequest.setScheduleDate(LocalDate.now().plusDays(1));
        testRequest.setTimeSlot("09:00-09:30");
        testRequest.setLocation("门诊楼301");
        testRequest.setMaxAppointments(10);

        testSchedule = new DoctorSchedule();
        testSchedule.setId("schedule001");
        testSchedule.setDoctorId("doctor001");
        testSchedule.setDoctorName("张伟医生");
        testSchedule.setScheduleDate(LocalDate.now().plusDays(1));
        testSchedule.setTimeSlot("09:00-09:30");
        testSchedule.setLocation("门诊楼301");
        testSchedule.setMaxAppointments(10);
        testSchedule.setCurrentAppointments(0);
        testSchedule.setStatus("AVAILABLE");
    }

    @Test
    @DisplayName("创建排班成功")
    void createScheduleSuccess() {
        // Arrange
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                eq("doctor001"), any(LocalDate.class), eq("09:00-09:30")))
                .thenReturn(Optional.empty());
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenAnswer(invocation -> {
            DoctorSchedule schedule = invocation.getArgument(0);
            schedule.setId("schedule001");
            return schedule;
        });

        // Act
        DoctorSchedule result = scheduleService.createSchedule(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals("doctor001", result.getDoctorId());
        assertEquals("09:00-09:30", result.getTimeSlot());
        assertEquals("AVAILABLE", result.getStatus());
        verify(doctorScheduleRepository, times(1)).save(any(DoctorSchedule.class));
    }

    @Test
    @DisplayName("创建排班失败 - 排班已存在")
    void createScheduleFailureAlreadyExists() {
        // Arrange
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                anyString(), any(LocalDate.class), anyString()))
                .thenReturn(Optional.of(testSchedule));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            scheduleService.createSchedule(testRequest);
        });

        assertTrue(exception.getMessage().contains("排班已存在"));
    }

    @Test
    @DisplayName("根据ID获取排班详情成功")
    void getScheduleByIdSuccess() {
        // Arrange
        when(doctorScheduleRepository.findById("schedule001"))
                .thenReturn(Optional.of(testSchedule));

        // Act
        DoctorSchedule result = scheduleService.getScheduleById("schedule001");

        // Assert
        assertNotNull(result);
        assertEquals("schedule001", result.getId());
        assertEquals("doctor001", result.getDoctorId());
    }

    @Test
    @DisplayName("根据ID获取排班详情失败 - 排班不存在")
    void getScheduleByIdFailureNotFound() {
        // Arrange
        when(doctorScheduleRepository.findById("schedule999"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            scheduleService.getScheduleById("schedule999");
        });

        assertTrue(exception.getMessage().contains("排班不存在"));
    }

    @Test
    @DisplayName("获取医生的排班列表")
    void getSchedulesByDoctorIdSuccess() {
        // Arrange
        List<DoctorSchedule> schedules = Arrays.asList(testSchedule);
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateBetweenOrderByScheduleDateAscTimeSlotAsc(
                eq("doctor001"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(schedules);

        // Act
        List<DoctorSchedule> result = scheduleService.getSchedulesByDoctorId(
                "doctor001", null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("schedule001", result.get(0).getId());
    }

    @Test
    @DisplayName("查询可用时间段")
    void getAvailableSchedulesSuccess() {
        // Arrange
        DoctorSchedule availableSchedule = new DoctorSchedule();
        availableSchedule.setId("schedule001");
        availableSchedule.setDoctorId("doctor001");
        availableSchedule.setScheduleDate(LocalDate.now().plusDays(1));
        availableSchedule.setTimeSlot("09:00-09:30");
        availableSchedule.setStatus("AVAILABLE");
        availableSchedule.setMaxAppointments(10);
        availableSchedule.setCurrentAppointments(3);

        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateBetweenOrderByScheduleDateAscTimeSlotAsc(
                eq("doctor001"), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(availableSchedule));

        // Act
        List<DoctorSchedule> result = scheduleService.getAvailableSchedules(
                "doctor001", null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("schedule001", result.get(0).getId());
    }

    @Test
    @DisplayName("查询可用时间段 - 过滤已满的排班")
    void getAvailableSchedulesFilterFullSchedule() {
        // Arrange
        DoctorSchedule fullSchedule = new DoctorSchedule();
        fullSchedule.setId("schedule001");
        fullSchedule.setDoctorId("doctor001");
        fullSchedule.setStatus("AVAILABLE");
        fullSchedule.setMaxAppointments(10);
        fullSchedule.setCurrentAppointments(10); // 已满

        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateBetweenOrderByScheduleDateAscTimeSlotAsc(
                anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(fullSchedule));

        // Act
        List<DoctorSchedule> result = scheduleService.getAvailableSchedules(
                "doctor001", null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // 已满的排班应该被过滤掉
    }

    @Test
    @DisplayName("更新排班成功")
    void updateScheduleSuccess() {
        // Arrange
        when(doctorScheduleRepository.findById("schedule001"))
                .thenReturn(Optional.of(testSchedule));
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenAnswer(invocation -> {
            DoctorSchedule schedule = invocation.getArgument(0);
            schedule.setUpdateTime(LocalDateTime.now());
            return schedule;
        });

        ScheduleRequest updateRequest = new ScheduleRequest();
        updateRequest.setLocation("门诊楼302");
        updateRequest.setMaxAppointments(15);

        // Act
        DoctorSchedule result = scheduleService.updateSchedule("schedule001", updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("门诊楼302", result.getLocation());
        assertEquals(15, result.getMaxAppointments());
        assertNotNull(result.getUpdateTime());
    }

    @Test
    @DisplayName("删除排班成功")
    void deleteScheduleSuccess() {
        // Arrange
        testSchedule.setCurrentAppointments(0);
        when(doctorScheduleRepository.findById("schedule001"))
                .thenReturn(Optional.of(testSchedule));
        doNothing().when(doctorScheduleRepository).delete(any(DoctorSchedule.class));

        // Act & Assert
        assertDoesNotThrow(() -> {
            scheduleService.deleteSchedule("schedule001");
        });

        verify(doctorScheduleRepository, times(1)).delete(any(DoctorSchedule.class));
    }

    @Test
    @DisplayName("删除排班失败 - 已有关联的预约")
    void deleteScheduleFailureHasAppointments() {
        // Arrange
        testSchedule.setCurrentAppointments(3);
        when(doctorScheduleRepository.findById("schedule001"))
                .thenReturn(Optional.of(testSchedule));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            scheduleService.deleteSchedule("schedule001");
        });

        assertTrue(exception.getMessage().contains("排班已有关联的预约"));
    }
}
