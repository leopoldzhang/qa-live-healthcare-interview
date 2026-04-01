package com.leansofx.qaserviceuser.controller;

import com.leansofx.qaserviceuser.entity.Doctor;
import com.leansofx.qaserviceuser.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", doctors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to get doctors: " + e.getMessage());
            errorResponse.put("data", null);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveDoctors() {
        try {
            List<Doctor> doctors = doctorService.getActiveDoctors();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", doctors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to get active doctors: " + e.getMessage());
            errorResponse.put("data", null);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getDoctorByUsername(@PathVariable String username) {
        try {
            Optional<Doctor> doctor = doctorService.getDoctorByUsername(username);
            if (doctor.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 200);
                response.put("message", "success");
                response.put("data", doctor.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 404);
                errorResponse.put("message", "Doctor not found");
                errorResponse.put("data", null);
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to get doctor: " + e.getMessage());
            errorResponse.put("data", null);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
