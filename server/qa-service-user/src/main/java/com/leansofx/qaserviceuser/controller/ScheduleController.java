package com.leansofx.qaserviceuser.controller;

import com.leansofx.qaserviceuser.dto.ScheduleRequest;
import com.leansofx.qaserviceuser.entity.DoctorSchedule;
import com.leansofx.qaserviceuser.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排班管理控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 创建排班
     * POST /api/schedule
     */
    @PostMapping
    public Map<String, Object> createSchedule(@RequestBody ScheduleRequest request) {
        Map<String, Object> response = new HashMap<>();
        logger.info("收到创建排班请求: doctorId={}, date={}, timeSlot={}",
                    request.getDoctorId(), request.getScheduleDate(), request.getTimeSlot());

        try {
            DoctorSchedule schedule = scheduleService.createSchedule(request);

            response.put("code", 200);
            response.put("data", scheduleToMap(schedule));
            response.put("message", "排班创建成功");

            logger.info("排班创建成功: id={}", schedule.getId());
            return response;

        } catch (RuntimeException e) {
            logger.error("排班创建失败: {}", e.getMessage());
            response.put("code", 1004);
            response.put("message", e.getMessage());
            return response;
        }
    }

    /**
     * 获取排班详情
     * GET /api/schedule/{scheduleId}
     */
    @GetMapping("/{scheduleId}")
    public Map<String, Object> getScheduleById(@PathVariable String scheduleId) {
        Map<String, Object> response = new HashMap<>();
        logger.info("收到查询排班详情请求: scheduleId={}", scheduleId);

        try {
            DoctorSchedule schedule = scheduleService.getScheduleById(scheduleId);

            response.put("code", 200);
            response.put("data", scheduleToMap(schedule));
            response.put("message", "查询成功");

            return response;

        } catch (RuntimeException e) {
            logger.error("查询排班详情失败: {}", e.getMessage());
            response.put("code", 1003);
            response.put("message", e.getMessage());
            return response;
        }
    }

    /**
     * 获取医生的排班列表
     * GET /api/schedule/doctor/{doctorId}
     */
    @GetMapping("/doctor/{doctorId}")
    public Map<String, Object> getSchedulesByDoctorId(
            @PathVariable String doctorId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Map<String, Object> response = new HashMap<>();
        logger.info("收到查询医生排班列表请求: doctorId={}, startDate={}, endDate={}",
                    doctorId, startDate, endDate);

        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

            List<DoctorSchedule> schedules = scheduleService.getSchedulesByDoctorId(doctorId, start, end);

            List<Map<String, Object>> dataList = schedules.stream()
                .map(this::scheduleToMap)
                .toList();

            response.put("code", 200);
            response.put("data", dataList);
            response.put("message", "查询成功");

            return response;

        } catch (Exception e) {
            logger.error("查询医生排班列表失败: {}", e.getMessage());
            response.put("code", 1005);
            response.put("message", "系统错误");
            return response;
        }
    }

    /**
     * 查询可用时间段（患者预约时使用）
     * GET /api/schedule/available
     */
    @GetMapping("/available")
    public Map<String, Object> getAvailableSchedules(
            @RequestParam String doctorId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Map<String, Object> response = new HashMap<>();
        logger.info("收到查询可用时间段请求: doctorId={}, startDate={}, endDate={}",
                    doctorId, startDate, endDate);

        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

            List<DoctorSchedule> schedules = scheduleService.getAvailableSchedules(doctorId, start, end);

            List<Map<String, Object>> dataList = schedules.stream()
                .map(s -> {
                    Map<String, Object> map = scheduleToMap(s);
                    // 添加剩余名额信息
                    int remaining = s.getMaxAppointments() - s.getCurrentAppointments();
                    map.put("remainingSlots", remaining);
                    return map;
                })
                .toList();

            response.put("code", 200);
            response.put("data", dataList);
            response.put("message", "查询成功");

            return response;

        } catch (Exception e) {
            logger.error("查询可用时间段失败: {}", e.getMessage());
            response.put("code", 1005);
            response.put("message", "系统错误");
            return response;
        }
    }

    /**
     * 更新排班
     * PUT /api/schedule/{scheduleId}
     */
    @PutMapping("/{scheduleId}")
    public Map<String, Object> updateSchedule(
            @PathVariable String scheduleId,
            @RequestBody ScheduleRequest request) {
        Map<String, Object> response = new HashMap<>();
        logger.info("收到更新排班请求: scheduleId={}", scheduleId);

        try {
            DoctorSchedule schedule = scheduleService.updateSchedule(scheduleId, request);

            response.put("code", 200);
            response.put("data", scheduleToMap(schedule));
            response.put("message", "排班更新成功");

            logger.info("排班更新成功: id={}", schedule.getId());
            return response;

        } catch (RuntimeException e) {
            logger.error("排班更新失败: {}", e.getMessage());
            response.put("code", 1003);
            response.put("message", e.getMessage());
            return response;
        }
    }

    /**
     * 删除排班
     * DELETE /api/schedule/{scheduleId}
     */
    @DeleteMapping("/{scheduleId}")
    public Map<String, Object> deleteSchedule(@PathVariable String scheduleId) {
        Map<String, Object> response = new HashMap<>();
        logger.info("收到删除排班请求: scheduleId={}", scheduleId);

        try {
            scheduleService.deleteSchedule(scheduleId);

            response.put("code", 200);
            response.put("message", "排班删除成功");

            logger.info("排班删除成功: id={}", scheduleId);
            return response;

        } catch (RuntimeException e) {
            logger.error("排班删除失败: {}", e.getMessage());
            response.put("code", 1004);
            response.put("message", e.getMessage());
            return response;
        }
    }

    /**
     * 将 DoctorSchedule 实体转换为 Map
     */
    private Map<String, Object> scheduleToMap(DoctorSchedule schedule) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", schedule.getId());
        map.put("doctorId", schedule.getDoctorId());
        map.put("doctorName", schedule.getDoctorName());
        map.put("scheduleDate", schedule.getScheduleDate());
        map.put("timeSlot", schedule.getTimeSlot());
        map.put("location", schedule.getLocation());
        map.put("maxAppointments", schedule.getMaxAppointments());
        map.put("currentAppointments", schedule.getCurrentAppointments());
        map.put("status", schedule.getStatus());
        map.put("createTime", schedule.getCreateTime());
        map.put("updateTime", schedule.getUpdateTime());
        return map;
    }
}
