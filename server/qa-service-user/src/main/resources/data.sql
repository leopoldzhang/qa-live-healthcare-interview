-- Initialize H2 Database with Doctor Data

INSERT INTO doctors (id, username, password, name, title, department, avatar, experience, is_active) VALUES
('doc001', 'dr-zhang-wei', '123456', '张伟医生', '主任医师', '心内科', 'https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg?auto=compress&cs=tinysrgb&w=400', '15年临床经验', TRUE),
('doc002', 'dr-li-na', '123456', '李娜医生', '副主任医师', '儿科', 'https://images.pexels.com/photos/5327585/pexels-photo-5327585.jpeg?auto=compress&cs=tinysrgb&w=400', '10年临床经验', TRUE),
('doc003', 'dr-wang-qiang', '123456', '王强医生', '主治医师', '骨科', 'https://images.pexels.com/photos/5452293/pexels-photo-5452293.jpeg?auto=compress&cs=tinysrgb&w=400', '8年临床经验', TRUE),
('doc004', 'dr-liu-min', '123456', '刘敏医生', '主任医师', '妇产科', 'https://images.pexels.com/photos/5452201/pexels-photo-5452201.jpeg?auto=compress&cs=tinysrgb&w=400', '18年临床经验', FALSE),
('doc005', 'dr-chen-jie', '123456', '陈杰医生', '副主任医师', '消化内科', 'https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg?auto=compress&cs=tinysrgb&w=400', '12年临床经验', TRUE);

-- Initialize H2 Database with Patient Data
-- Passwords are stored in plain text for development (should be BCrypt hashed in production)

INSERT INTO patients (id, username, password, name, birthday, phone, gender, created_at, updated_at) VALUES
('patient001', 'zhaoming', '123456', '赵明', '1985-03-15', '13812341234', '男', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO patients (id, username, password, name, birthday, phone, gender, created_at, updated_at) VALUES
('patient002', 'sunli', '123456', '孙丽', '1990-07-22', '13956785678', '女', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO patients (id, username, password, name, birthday, phone, gender, created_at, updated_at) VALUES
('patient003', 'zhoujie', '123456', '周杰', '1978-11-08', '13790129012', '男', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO patients (id, username, password, name, birthday, phone, gender, created_at, updated_at) VALUES
('patient004', 'wufang', '123456', '吴芳', '1995-05-20', '13634563456', '女', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO patients (id, username, password, name, birthday, phone, gender, created_at, updated_at) VALUES
('patient005', 'zhenghao', '123456', '郑浩', '1988-09-12', '13578907890', '男', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- 医生排班数据 (未来14天)
-- ========================================

-- 张伟医生 (doc001) - 心内科 - 周一至周五
INSERT INTO doctor_schedules (id, doctor_id, doctor_name, schedule_date, time_slot, location, max_appointments, current_appointments, status, create_time, update_time) VALUES
('sch001', 'doc001', '张伟医生', CURRENT_DATE + 1, '09:00-09:30', '心内科诊室A01', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch002', 'doc001', '张伟医生', CURRENT_DATE + 1, '09:30-10:00', '心内科诊室A01', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch003', 'doc001', '张伟医生', CURRENT_DATE + 1, '10:00-10:30', '心内科诊室A01', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch004', 'doc001', '张伟医生', CURRENT_DATE + 2, '09:00-09:30', '心内科诊室A01', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch005', 'doc001', '张伟医生', CURRENT_DATE + 2, '09:30-10:00', '心内科诊室A01', 2, 1, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch006', 'doc001', '张伟医生', CURRENT_DATE + 3, '14:00-14:30', '心内科诊室A01', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch007', 'doc001', '张伟医生', CURRENT_DATE + 3, '14:30-15:00', '心内科诊室A01', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch008', 'doc001', '张伟医生', CURRENT_DATE + 5, '09:00-09:30', '心内科诊室A01', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch009', 'doc001', '张伟医生', CURRENT_DATE + 5, '09:30-10:00', '心内科诊室A01', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 李娜医生 (doc002) - 儿科 - 周一至周六
INSERT INTO doctor_schedules (id, doctor_id, doctor_name, schedule_date, time_slot, location, max_appointments, current_appointments, status, create_time, update_time) VALUES
('sch010', 'doc002', '李娜医生', CURRENT_DATE + 1, '08:30-09:00', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch011', 'doc002', '李娜医生', CURRENT_DATE + 1, '09:00-09:30', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch012', 'doc002', '李娜医生', CURRENT_DATE + 1, '09:30-10:00', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch013', 'doc002', '李娜医生', CURRENT_DATE + 2, '08:30-09:00', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch014', 'doc002', '李娜医生', CURRENT_DATE + 2, '09:00-09:30', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch015', 'doc002', '李娜医生', CURRENT_DATE + 4, '14:00-14:30', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch016', 'doc002', '李娜医生', CURRENT_DATE + 4, '14:30-15:00', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch017', 'doc002', '李娜医生', CURRENT_DATE + 6, '08:30-09:00', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch018', 'doc002', '李娜医生', CURRENT_DATE + 6, '09:00-09:30', '儿科诊室B02', 3, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 王强医生 (doc003) - 骨科 - 周二至周六
INSERT INTO doctor_schedules (id, doctor_id, doctor_name, schedule_date, time_slot, location, max_appointments, current_appointments, status, create_time, update_time) VALUES
('sch019', 'doc003', '王强医生', CURRENT_DATE + 2, '10:00-10:30', '骨科诊室C03', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch020', 'doc003', '王强医生', CURRENT_DATE + 2, '10:30-11:00', '骨科诊室C03', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch021', 'doc003', '王强医生', CURRENT_DATE + 3, '10:00-10:30', '骨科诊室C03', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch022', 'doc003', '王强医生', CURRENT_DATE + 3, '10:30-11:00', '骨科诊室C03', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch023', 'doc003', '王强医生', CURRENT_DATE + 5, '14:00-14:30', '骨科诊室C03', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch024', 'doc003', '王强医生', CURRENT_DATE + 5, '14:30-15:00', '骨科诊室C03', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch025', 'doc003', '王强医生', CURRENT_DATE + 6, '10:00-10:30', '骨科诊室C03', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch026', 'doc003', '王强医生', CURRENT_DATE + 6, '10:30-11:00', '骨科诊室C03', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 陈杰医生 (doc005) - 消化内科 - 周一至周五
INSERT INTO doctor_schedules (id, doctor_id, doctor_name, schedule_date, time_slot, location, max_appointments, current_appointments, status, create_time, update_time) VALUES
('sch027', 'doc005', '陈杰医生', CURRENT_DATE + 1, '08:00-08:30', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch028', 'doc005', '陈杰医生', CURRENT_DATE + 1, '08:30-09:00', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch029', 'doc005', '陈杰医生', CURRENT_DATE + 3, '08:00-08:30', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch030', 'doc005', '陈杰医生', CURRENT_DATE + 3, '08:30-09:00', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch031', 'doc005', '陈杰医生', CURRENT_DATE + 4, '14:00-14:30', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch032', 'doc005', '陈杰医生', CURRENT_DATE + 4, '14:30-15:00', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch033', 'doc005', '陈杰医生', CURRENT_DATE + 5, '08:00-08:30', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch034', 'doc005', '陈杰医生', CURRENT_DATE + 5, '08:30-09:00', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch035', 'doc005', '陈杰医生', CURRENT_DATE + 7, '08:00-08:30', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sch036', 'doc005', '陈杰医生', CURRENT_DATE + 7, '08:30-09:00', '消化内科诊室D04', 2, 0, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- 示例预约数据 (让医生可以查看预约情况)
-- ========================================

-- 张伟医生的预约 (PENDING 和 CONFIRMED 状态)
INSERT INTO appointments (id, patient_id, patient_name, doctor_id, doctor_name, appointment_date, time_slot, location, status, description, appointment_no, create_time, update_time) VALUES
('apt001', 'patient001', '赵明', 'doc001', '张伟医生', CURRENT_DATE + 1, '09:00-09:30', '心内科诊室A01', 'CONFIRMED', '胸闷、气短，活动后加重', 'APT001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('apt002', 'patient002', '孙丽', 'doc001', '张伟医生', CURRENT_DATE + 2, '09:30-10:00', '心内科诊室A01', 'PENDING', '孕期心悸，需要咨询', 'APT002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 李娜医生的预约
INSERT INTO appointments (id, patient_id, patient_name, doctor_id, doctor_name, appointment_date, time_slot, location, status, description, appointment_no, create_time, update_time) VALUES
('apt003', 'patient003', '周杰', 'doc002', '李娜医生', CURRENT_DATE + 1, '08:30-09:00', '儿科诊室B02', 'CONFIRMED', '孩子反复发烧3天', 'APT003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('apt004', 'patient004', '吴芳', 'doc002', '李娜医生', CURRENT_DATE + 1, '09:00-09:30', '儿科诊室B02', 'PENDING', '婴儿湿疹严重', 'APT004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('apt005', 'patient001', '赵明', 'doc002', '李娜医生', CURRENT_DATE + 4, '14:00-14:30', '儿科诊室B02', 'PENDING', '成人问诊：胸闷', 'APT005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 王强医生的预约
INSERT INTO appointments (id, patient_id, patient_name, doctor_id, doctor_name, appointment_date, time_slot, location, status, description, appointment_no, create_time, update_time) VALUES
('apt006', 'patient005', '郑浩', 'doc003', '王强医生', CURRENT_DATE + 2, '10:00-10:30', '骨科诊室C03', 'CONFIRMED', '腰椎间盘突出复查', 'APT006', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 陈杰医生的预约
INSERT INTO appointments (id, patient_id, patient_name, doctor_id, doctor_name, appointment_date, time_slot, location, status, description, appointment_no, create_time, update_time) VALUES
('apt007', 'patient002', '孙丽', 'doc005', '陈杰医生', CURRENT_DATE + 1, '08:00-08:30', '消化内科诊室D04', 'COMPLETED', '胃痛、反酸', 'APT007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('apt008', 'patient003', '周杰', 'doc005', '陈杰医生', CURRENT_DATE + 3, '08:00-08:30', '消化内科诊室D04', 'CONFIRMED', '腹胀、消化不良', 'APT008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
