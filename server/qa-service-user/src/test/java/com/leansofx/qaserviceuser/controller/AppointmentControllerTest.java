package com.leansofx.qaserviceuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leansofx.qaserviceuser.dto.AppointmentRequest;
import com.leansofx.qaserviceuser.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AppointmentController 单元测试
 * 使用 @WebMvcTest 只加载 web 层，@MockBean 模拟依赖
 */
@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("POST /api/appointment - 创建预约成功")
    void createAppointmentSuccess() throws Exception {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId("patient001");
        request.setDoctorId("doctor001");
        request.setAppointmentDate("2026-05-01");
        request.setTimeSlot("09:00-09:30");
        request.setLocation("门诊楼301");
        request.setDescription("感冒咳嗽");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", "apt001");
        responseData.put("patientId", "patient001");
        responseData.put("status", "PENDING");
        responseData.put("appointmentNo", "APT123456789");

        when(appointmentService.createAppointment(
                eq("patient001"), eq("doctor001"), any(LocalDate.class),
                eq("09:00-09:30"), eq("门诊楼301"), eq("感冒咳嗽")))
                .thenReturn(responseData);

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value("apt001"));
    }

    @Test
    @DisplayName("POST /api/appointment - 创建预约失败（患者不存在）")
    void createAppointmentFailurePatientNotFound() throws Exception {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId("patient999");
        request.setDoctorId("doctor001");
        request.setAppointmentDate("2026-05-01");
        request.setTimeSlot("09:00-09:30");

        // 设置 mock 抛出异常（使用 any() 以匹配 null 值）
        when(appointmentService.createAppointment(anyString(), anyString(), any(LocalDate.class),
                anyString(), any(), any()))
                .thenThrow(new RuntimeException("Patient not found"));

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1003))
                .andExpect(jsonPath("$.message").value("患者不存在"));
    }

    @Test
    @DisplayName("GET /api/appointment/{appointmentNo} - 查询预约成功")
    void getAppointmentByNoSuccess() throws Exception {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", "apt001");
        responseData.put("appointmentNo", "APT123456789");
        responseData.put("status", "PENDING");

        when(appointmentService.getAppointmentByNo("APT123456789"))
                .thenReturn(responseData);

        mockMvc.perform(get("/api/appointment/APT123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.appointmentNo").value("APT123456789"));
    }

    @Test
    @DisplayName("PUT /api/appointment/{appointmentNo}/cancel - 取消预约成功")
    void cancelAppointmentSuccess() throws Exception {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", "apt001");
        responseData.put("status", "CANCELLED");

        when(appointmentService.cancelAppointment("APT123456789"))
                .thenReturn(responseData);

        mockMvc.perform(put("/api/appointment/APT123456789/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));
    }

    @Test
    @DisplayName("PUT /api/appointment/{appointmentNo}/confirm - 确认预约成功")
    void confirmAppointmentSuccess() throws Exception {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", "apt001");
        responseData.put("status", "CONFIRMED");

        when(appointmentService.confirmAppointment("APT123456789"))
                .thenReturn(responseData);

        mockMvc.perform(put("/api/appointment/APT123456789/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("CONFIRMED"));
    }
}
