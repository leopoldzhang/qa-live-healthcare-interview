package com.leansofx.qaserviceuser.repository;

import com.leansofx.qaserviceuser.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(String patientId);

    List<Appointment> findByPatientIdAndAppointmentDateAndTimeSlotAndStatusNot(String patientId,
                                                                             LocalDate appointmentDate,
                                                                             String timeSlot,
                                                                             String status);

    List<Appointment> findByPatientIdAndStatusOrderByAppointmentDateDesc(String patientId, String status);

    List<Appointment> findByDoctorIdAndAppointmentDateOrderByTimeSlot(String doctorId, LocalDate appointmentDate);

    Optional<Appointment> findByAppointmentNo(String appointmentNo);

    List<Appointment> findByStatus(String status);

    long countByDoctorIdAndAppointmentDateAndTimeSlotAndStatusNot(String doctorId,
                                                                  LocalDate appointmentDate,
                                                                  String timeSlot,
                                                                  String status);
}
