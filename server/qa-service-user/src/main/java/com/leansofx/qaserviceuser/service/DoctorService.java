package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.entity.Doctor;
import com.leansofx.qaserviceuser.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        // 添加 specialties 数据（从 JSON 数据映射）
        doctors.forEach(this::enrichWithSpecialties);
        return doctors;
    }

    public List<Doctor> getActiveDoctors() {
        List<Doctor> doctors = doctorRepository.findByIsActive(true);
        doctors.forEach(this::enrichWithSpecialties);
        return doctors;
    }

    public Optional<Doctor> getDoctorById(String id) {
        return doctorRepository.findById(id).map(this::enrichWithSpecialties);
    }

    public Optional<Doctor> getDoctorByUsername(String username) {
        return doctorRepository.findByUsername(username).map(this::enrichWithSpecialties);
    }

    private Doctor enrichWithSpecialties(Doctor doctor) {
        // 根据 ID 设置 specialties（临时硬编码，实际可以从另一个表获取）
        switch (doctor.getId()) {
            case "doc001":
                doctor.setSpecialties(List.of("高血压", "冠心病", "心律失常"));
                break;
            case "doc002":
                doctor.setSpecialties(List.of("儿童感冒", "儿童发育", "疫苗接种"));
                break;
            case "doc003":
                doctor.setSpecialties(List.of("骨折", "关节炎", "运动损伤"));
                break;
            case "doc004":
                doctor.setSpecialties(List.of("孕期保健", "妇科炎症", "产后恢复"));
                break;
            case "doc005":
                doctor.setSpecialties(List.of("胃炎", "肠道疾病", "肝病"));
                break;
            default:
                doctor.setSpecialties(List.of());
        }
        return doctor;
    }
}
