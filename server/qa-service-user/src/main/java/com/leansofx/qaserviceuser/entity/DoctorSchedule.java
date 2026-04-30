package com.leansofx.qaserviceuser.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_schedules")
public class DoctorSchedule {

    @Id
    private String id;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @Column(name = "time_slot", nullable = false)
    private String timeSlot;

    private String location;

    @Column(name = "max_appointments")
    private Integer maxAppointments = 1;

    @Column(name = "current_appointments")
    private Integer currentAppointments = 0;

    @Column(nullable = false)
    private String status = "AVAILABLE";

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    // Constructors
    public DoctorSchedule() {}

    public DoctorSchedule(String id, String doctorId, String doctorName,
                          LocalDate scheduleDate, String timeSlot,
                          String location, Integer maxAppointments,
                          Integer currentAppointments, String status) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.scheduleDate = scheduleDate;
        this.timeSlot = timeSlot;
        this.location = location;
        this.maxAppointments = maxAppointments;
        this.currentAppointments = currentAppointments;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getCurrentAppointments() {
        return currentAppointments;
    }

    public void setCurrentAppointments(Integer currentAppointments) {
        this.currentAppointments = currentAppointments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (maxAppointments == null) {
            maxAppointments = 1;
        }
        if (currentAppointments == null) {
            currentAppointments = 0;
        }
        if (status == null) {
            status = "AVAILABLE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
