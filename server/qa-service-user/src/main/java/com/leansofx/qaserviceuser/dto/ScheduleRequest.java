package com.leansofx.qaserviceuser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 排班请求 DTO
 */
public class ScheduleRequest {

    @NotBlank(message = "医生ID不能为空")
    private String doctorId;

    @NotBlank(message = "医生姓名不能为空")
    private String doctorName;

    @NotNull(message = "排班日期不能为空")
    private LocalDate scheduleDate;

    @NotBlank(message = "时间段不能为空")
    private String timeSlot;

    private String location;

    private Integer maxAppointments = 1;

    // Constructors
    public ScheduleRequest() {}

    public ScheduleRequest(String doctorId, String doctorName, LocalDate scheduleDate,
                          String timeSlot, String location, Integer maxAppointments) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.scheduleDate = scheduleDate;
        this.timeSlot = timeSlot;
        this.location = location;
        this.maxAppointments = maxAppointments;
    }

    // Getters and Setters
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaxAppointments() {
        return maxAppointments;
    }

    public void setMaxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
    }
}
