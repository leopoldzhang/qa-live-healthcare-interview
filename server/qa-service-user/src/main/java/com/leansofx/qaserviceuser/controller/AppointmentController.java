package com.leansofx.qaserviceuser.controller;

import com.leansofx.qaserviceuser.dto.AppointmentRequest;
import com.leansofx.qaserviceuser.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 创建预约
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAppointment(@RequestBody AppointmentRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证参数
            if (request.getPatientId() == null || request.getPatientId().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "患者ID不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getDoctorId() == null || request.getDoctorId().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "医生ID不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getAppointmentDate() == null || request.getAppointmentDate().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "预约日期不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getTimeSlot() == null || request.getTimeSlot().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "时间段不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            // 创建预约
            Map<String, Object> data = appointmentService.createAppointment(
                    request.getPatientId(),
                    request.getDoctorId(),
                    java.time.LocalDate.parse(request.getAppointmentDate()),
                    request.getTimeSlot(),
                    request.getLocation(),
                    request.getDescription()
            );

            response.put("code", 200);
            response.put("data", data);
            response.put("message", "预约成功");
            return ResponseEntity.status(201).body(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Patient not found")) {
                response.put("code", 1003);
                response.put("message", "患者不存在");
            } else if (e.getMessage().contains("Doctor not found")) {
                response.put("code", 1003);
                response.put("message", "医生不存在");
            } else if (e.getMessage().contains("Doctor schedule not available")) {
                response.put("code", 1004);
                response.put("message", "医生排班不可用");
            } else if (e.getMessage().contains("Doctor schedule is full")) {
                response.put("code", 1004);
                response.put("message", "医生该时间段已约满");
            } else if (e.getMessage().contains("Appointment conflict")) {
                response.put("code", 1004);
                response.put("message", "您在该时间段已有预约，不能重复预约");
            } else {
                response.put("code", 1005);
                response.put("message", "系统繁忙，请稍后重试");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据预约单号查询预约
     */
    @GetMapping("/{appointmentNo}")
    public ResponseEntity<Map<String, Object>> getAppointmentByNo(@PathVariable String appointmentNo) {
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> data = appointmentService.getAppointmentByNo(appointmentNo);

            response.put("code", 200);
            response.put("data", data);
            response.put("message", "success");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Appointment not found")) {
                response.put("code", 1003);
                response.put("message", "预约不存在");
            } else {
                response.put("code", 1005);
                response.put("message", "系统繁忙，请稍后重试");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据患者ID查询预约记录
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getAppointmentsByPatientId(
            @PathVariable String patientId,
            @RequestParam(required = false) String status) {
        System.out.println("=== 获取预约列表: patientId=" + patientId + ", status=" + status);
        Map<String, Object> response = new HashMap<>();

        try {
            List<Map<String, Object>> data = appointmentService.getAppointmentsByPatientId(patientId, status);

            response.put("code", 200);
            response.put("data", data);
            response.put("message", "success");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("code", 1005);
            response.put("message", "系统繁忙，请稍后重试");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 取消预约
     */
    @PutMapping("/{appointmentNo}/cancel")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@PathVariable String appointmentNo) {
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> data = appointmentService.cancelAppointment(appointmentNo);

            response.put("code", 200);
            response.put("data", data);
            response.put("message", "预约已取消");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Appointment not found")) {
                response.put("code", 1003);
                response.put("message", "预约不存在");
            } else if (e.getMessage().contains("Cannot cancel appointment")) {
                response.put("code", 1004);
                response.put("message", e.getMessage());
            } else {
                response.put("code", 1005);
                response.put("message", "系统繁忙，请稍后重试");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 确认预约（医生）
     */
    @PutMapping("/{appointmentNo}/confirm")
    public ResponseEntity<Map<String, Object>> confirmAppointment(@PathVariable String appointmentNo) {
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> data = appointmentService.confirmAppointment(appointmentNo);

            response.put("code", 200);
            response.put("data", data);
            response.put("message", "预约已确认");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Appointment not found")) {
                response.put("code", 1003);
                response.put("message", "预约不存在");
            } else if (e.getMessage().contains("Cannot confirm appointment")) {
                response.put("code", 1004);
                response.put("message", e.getMessage());
            } else {
                response.put("code", 1005);
                response.put("message", "系统繁忙，请稍后重试");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 完成预约（医生）
     */
    @PutMapping("/{appointmentNo}/complete")
    public ResponseEntity<Map<String, Object>> completeAppointment(@PathVariable String appointmentNo) {
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> data = appointmentService.completeAppointment(appointmentNo);

            response.put("code", 200);
            response.put("data", data);
            response.put("message", "预约已完成");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Appointment not found")) {
                response.put("code", 1003);
                response.put("message", "预约不存在");
            } else if (e.getMessage().contains("Cannot complete appointment")) {
                response.put("code", 1004);
                response.put("message", e.getMessage());
            } else {
                response.put("code", 1005);
                response.put("message", "系统繁忙，请稍后重试");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }
}
