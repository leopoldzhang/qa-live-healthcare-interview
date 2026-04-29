package com.leansofx.qaserviceuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leansofx.qaserviceuser.dto.ScheduleRequest;
import com.leansofx.qaserviceuser.entity.DoctorSchedule;
import com.leansofx.qaserviceuser.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ScheduleController 集成测试
 */
@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // 支持 Java 8 时间类型
    }

    @Test
    @DisplayName("POST /api/schedule - 创建排班成功")
    void createScheduleSuccess() throws Exception {
        // Arrange
        ScheduleRequest request = new ScheduleRequest();
        request.setDoctorId("doctor001");
        request.setDoctorName("张伟医生");
        request.setScheduleDate(LocalDate.now().plusDays(1));
        request.setTimeSlot("09:30-10:00");
        request.setLocation("门诊楼301");
        request.setMaxAppointments(10);

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId("schedule001");
        schedule.setDoctorId("doctor001");
        schedule.setDoctorName("张伟医生");
        schedule.setScheduleDate(LocalDate.now().plusDays(1));
        schedule.setTimeSlot("09:30-10:00");
        schedule.setLocation("门诊楼301");
        schedule.setMaxAppointments(10);
        schedule.setCurrentAppointments(0);
        schedule.setStatus("AVAILABLE");

        when(scheduleService.createSchedule(any(ScheduleRequest.class)))
                .thenReturn(schedule);

        // Act & Assert
        mockMvc.perform(post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("排班创建成功"))
                .andExpect(jsonPath("$.data.id").value("schedule001"))
                .andExpect(jsonPath("$.data.doctorId").value("doctor001"))
                .andExpect(jsonPath("$.data.timeSlot").value("09:30-10:00"));

        verify(scheduleService, times(1)).createSchedule(any(ScheduleRequest.class));
    }

    @Test
    @DisplayName("POST /api/schedule - 创建排班失败（排班已存在）")
    void createScheduleFailureAlreadyExists() throws Exception {
        // Arrange
        ScheduleRequest request = new ScheduleRequest();
        request.setDoctorId("doctor001");
        request.setScheduleDate(LocalDate.now().plusDays(1));
        request.setTimeSlot("09:30-10:00");

        when(scheduleService.createSchedule(any(ScheduleRequest.class)))
                .thenThrow(new RuntimeException("该时间段的排班已存在"));

        // Act & Assert
        mockMvc.perform(post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1004))
                .andExpect(jsonPath("$.message").value("该时间段的排班已存在"));
    }

    @Test
    @DisplayName("GET /api/schedule/{scheduleId} - 获取排班详情成功")
    void getScheduleByIdSuccess() throws Exception {
        // Arrange
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId("schedule001");
        schedule.setDoctorId("doctor001");
        schedule.setDoctorName("张伟医生");
        schedule.setScheduleDate(LocalDate.now().plusDays(1));
        schedule.setTimeSlot("09:30-10:00");
        schedule.setStatus("AVAILABLE");

        when(scheduleService.getScheduleById("schedule001"))
                .thenReturn(schedule);

        // Act & Assert
        mockMvc.perform(get("/api/schedule/schedule001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("查询成功"))
                .andExpect(jsonPath("$.data.id").value("schedule001"))
                .andExpect(jsonPath("$.data.doctorName").value("张伟医生"));
    }

    @Test
    @DisplayName("GET /api/schedule/{scheduleId} - 获取排班详情失败（排班不存在）")
    void getScheduleByIdFailureNotFound() throws Exception {
        // Arrange
        when(scheduleService.getScheduleById("schedule999"))
                .thenThrow(new RuntimeException("排班不存在"));

        // Act & Assert
        mockMvc.perform(get("/api/schedule/schedule999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1003))
                .andExpect(jsonPath("$.message").value("排班不存在"));
    }

    @Test
    @DisplayName("GET /api/schedule/doctor/{doctorId} - 获取医生排班列表")
    void getSchedulesByDoctorIdSuccess() throws Exception {
        // Arrange
        DoctorSchedule schedule1 = new DoctorSchedule();
        schedule1.setId("schedule001");
        schedule1.setDoctorId("doctor001");
        schedule1.setScheduleDate(LocalDate.now().plusDays(1));
        schedule1.setTimeSlot("09:30-10:00");

        DoctorSchedule schedule2 = new DoctorSchedule();
        schedule2.setId("schedule002");
        schedule2.setDoctorId("doctor001");
        schedule2.setScheduleDate(LocalDate.now().plusDays(2));
        schedule2.setTimeSlot("10:00-10:30");

        List<DoctorSchedule> schedules = Arrays.asList(schedule1, schedule2);

        when(scheduleService.getSchedulesByDoctorId(eq("doctor001"), isNull(), isNull()))
                .thenReturn(schedules);

        // Act & Assert
        mockMvc.perform(get("/api/schedule/doctor/doctor001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("查询成功"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value("schedule001"))
                .andExpect(jsonPath("$.data[1].id").value("schedule002"));
    }

    @Test
    @DisplayName("GET /api/schedule/available - 查询可用时间段")
    void getAvailableSchedulesSuccess() throws Exception {
        // Arrange
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId("schedule001");
        schedule.setDoctorId("doctor001");
        schedule.setScheduleDate(LocalDate.now().plusDays(1));
        schedule.setTimeSlot("09:30-10:00");
        schedule.setMaxAppointments(10);
        schedule.setCurrentAppointments(3);
        schedule.setStatus("AVAILABLE");

        when(scheduleService.getAvailableSchedules(eq("doctor001"), isNull(), isNull()))
                .thenReturn(Arrays.asList(schedule));

        // Act & Assert
        mockMvc.perform(get("/api/schedule/available")
                .param("doctorId", "doctor001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("查询成功"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].remainingSlots").value(7));
    }

    @Test
    @DisplayName("PUT /api/schedule/{scheduleId} - 更新排班成功")
    void updateScheduleSuccess() throws Exception {
        // Arrange
        ScheduleRequest request = new ScheduleRequest();
        request.setLocation("门诊楼302");
        request.setMaxAppointments(15);

        DoctorSchedule updatedSchedule = new DoctorSchedule();
        updatedSchedule.setId("schedule001");
        updatedSchedule.setLocation("门诊楼302");
        updatedSchedule.setMaxAppointments(15);

        when(scheduleService.updateSchedule(eq("schedule001"), any(ScheduleRequest.class)))
                .thenReturn(updatedSchedule);

        // Act & Assert
        mockMvc.perform(put("/api/schedule/schedule001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("排班更新成功"))
                .andExpect(jsonPath("$.data.location").value("门诊楼302"))
                .andExpect(jsonPath("$.data.maxAppointments").value(15));
    }

    @Test
    @DisplayName("DELETE /api/schedule/{scheduleId} - 删除排班成功")
    void deleteScheduleSuccess() throws Exception {
        // Arrange
        doNothing().when(scheduleService).deleteSchedule("schedule001");

        // Act & Assert
        mockMvc.perform(delete("/api/schedule/schedule001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("排班删除成功"));

        verify(scheduleService, times(1)).deleteSchedule("schedule001");
    }

    @Test
    @DisplayName("DELETE /api/schedule/{scheduleId} - 删除排班失败（已有关联预约）")
    void deleteScheduleFailureHasAppointments() throws Exception {
        // Arrange
        doThrow(new RuntimeException("排班已有关联的预约，无法删除"))
                .when(scheduleService).deleteSchedule("schedule001");

        // Act & Assert
        mockMvc.perform(delete("/api/schedule/schedule001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1004))
                .andExpect(jsonPath("$.message").value("排班已有关联的预约，无法删除"));
    }
}
