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
