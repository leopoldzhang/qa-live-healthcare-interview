package com.leansofx.qaserviceuser.controller;

import com.leansofx.qaserviceuser.entity.Patient;
import com.leansofx.qaserviceuser.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/patient")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AuthController {

    @Autowired
    private PatientService patientService;

    /**
     * 患者注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证参数
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "用户名不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "密码不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getName() == null || request.getName().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "姓名不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            // 注册患者
            Patient patient = patientService.register(
                    request.getUsername(),
                    request.getPassword(),
                    request.getName(),
                    request.getBirthday(),
                    request.getPhone(),
                    request.getGender()
            );

            response.put("code", 200);
            response.put("data", patientToMap(patient));
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Username already exists")) {
                response.put("code", 1001);
                response.put("message", "该用户名已被注册,请更换");
            } else {
                response.put("code", 1005);
                response.put("message", "系统繁忙,请稍后重试");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 患者登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证参数
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "用户名不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                response.put("code", 1004);
                response.put("message", "密码不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            // 登录
            Patient patient = patientService.login(request.getUsername(), request.getPassword());

            response.put("code", 200);
            response.put("data", patientToMap(patient));
            response.put("message", "登录成功");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Patient not found")) {
                response.put("code", 1003);
                response.put("message", "用户不存在,请先注册");
            } else if (e.getMessage().contains("Invalid password")) {
                response.put("code", 1002);
                response.put("message", "用户名或密码错误");
            } else {
                response.put("code", 1005);
                response.put("message", "系统繁忙,请稍后重试");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 患者登出
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "登出成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        boolean exists = patientService.checkUsernameExists(username);
        data.put("exists", exists);

        response.put("code", 200);
        response.put("data", data);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取所有患者
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPatients() {
        Map<String, Object> response = new HashMap<>();
        List<Patient> patients = patientService.getAllPatients();

        response.put("code", 200);
        response.put("data", patients);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    /**
     * 根据 ID 获取患者
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPatientById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Patient patient = patientService.getPatientById(id);
            response.put("code", 200);
            response.put("data", patientToMap(patient));
            response.put("message", "success");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("code", 1003);
            response.put("message", "患者不存在");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 将 Patient 实体转换为 Map (排除密码)
     */
    private Map<String, Object> patientToMap(Patient patient) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", patient.getId());
        map.put("username", patient.getUsername());
        map.put("name", patient.getName());
        map.put("birthday", patient.getBirthday());
        map.put("phone", patient.getPhone());
        map.put("gender", patient.getGender());
        map.put("createdAt", patient.getCreatedAt());
        return map;
    }

    // 请求 DTO
    public static class RegisterRequest {
        private String username;
        private String password;
        private String name;
        private String birthday;
        private String phone;
        private String gender;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
