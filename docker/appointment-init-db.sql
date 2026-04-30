-- Appointment and Doctor Schedule Tables for QA Healthcare
-- Created for FEAT-001-Appointment-Registration
-- Date: 2026-04-29

USE qa_healthcare;

-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id VARCHAR(50) PRIMARY KEY COMMENT '预约ID',
    patient_id VARCHAR(50) NOT NULL COMMENT '患者ID',
    patient_name VARCHAR(100) NOT NULL COMMENT '患者姓名（冗余字段，减少关联查询）',
    doctor_id VARCHAR(50) NOT NULL COMMENT '医生ID',
    doctor_name VARCHAR(100) NOT NULL COMMENT '医生姓名（冗余字段）',
    appointment_date DATE NOT NULL COMMENT '预约日期',
    time_slot VARCHAR(20) NOT NULL COMMENT '时间段（如：09:00-09:30）',
    location VARCHAR(200) COMMENT '就诊地点',
    status ENUM('PENDING','CONFIRMED','COMPLETED','CANCELLED','MISSED') DEFAULT 'PENDING' COMMENT '预约状态',
    description TEXT COMMENT '症状描述（可选）',
    appointment_no VARCHAR(50) UNIQUE COMMENT '预约单号（唯一）',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_patient_id (patient_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_appointment_date (appointment_date),
    INDEX idx_status (status),
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约记录表';

-- Create doctor_schedules table
CREATE TABLE IF NOT EXISTS doctor_schedules (
    id VARCHAR(50) PRIMARY KEY COMMENT '排班ID',
    doctor_id VARCHAR(50) NOT NULL COMMENT '医生ID',
    doctor_name VARCHAR(100) NOT NULL COMMENT '医生姓名（冗余字段）',
    schedule_date DATE NOT NULL COMMENT '排班日期',
    time_slot VARCHAR(20) NOT NULL COMMENT '时间段（如：09:00-09:30）',
    location VARCHAR(200) COMMENT '就诊地点',
    max_appointments INT DEFAULT 1 COMMENT '最大预约数',
    current_appointments INT DEFAULT 0 COMMENT '当前预约数',
    status ENUM('AVAILABLE','UNAVAILABLE') DEFAULT 'AVAILABLE' COMMENT '状态',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_schedule_date (schedule_date),
    INDEX idx_doctor_date (doctor_id, schedule_date),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医生排班表';
