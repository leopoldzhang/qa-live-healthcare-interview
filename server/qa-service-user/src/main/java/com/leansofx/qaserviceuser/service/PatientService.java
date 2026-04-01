package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.entity.Patient;
import com.leansofx.qaserviceuser.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * 患者注册
     */
    public Patient register(String username, String password, String name, String birthday, String phone, String gender) {
        // 检查用户名是否已存在
        if (patientRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        // 创建患者记录
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID().toString().replace("-", ""));
        patient.setUsername(username);
        patient.setPassword(password); // 在生产环境中应该使用 BCrypt 加密
        patient.setName(name);
        patient.setBirthday(birthday);
        patient.setPhone(phone);
        patient.setGender(gender);

        return patientRepository.save(patient);
    }

    /**
     * 患者登录
     */
    public Patient login(String username, String password) {
        Optional<Patient> patientOpt = patientRepository.findByUsername(username);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found");
        }

        Patient patient = patientOpt.get();
        // 在生产环境中应该使用 BCrypt 验证密码
        if (!patient.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return patient;
    }

    /**
     * 根据 ID 获取患者
     */
    public Patient getPatientById(String id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    /**
     * 根据用户名获取患者
     */
    public Patient getPatientByUsername(String username) {
        return patientRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    /**
     * 获取所有患者
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * 检查用户名是否存在
     */
    public boolean checkUsernameExists(String username) {
        return patientRepository.existsByUsername(username);
    }
}
