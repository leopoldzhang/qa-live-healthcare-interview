package com.leansofx.qaserviceuser.repository;

import com.leansofx.qaserviceuser.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, String> {

    List<DoctorSchedule> findByDoctorIdAndScheduleDateBetweenOrderByScheduleDateAscTimeSlotAsc(
        String doctorId, LocalDate startDate, LocalDate endDate);

    List<DoctorSchedule> findByDoctorIdAndScheduleDateAndStatus(
        String doctorId, LocalDate scheduleDate, String status);

    Optional<DoctorSchedule> findByDoctorIdAndScheduleDateAndTimeSlot(
        String doctorId, LocalDate scheduleDate, String timeSlot);

    List<DoctorSchedule> findByScheduleDateBetweenAndStatusOrderByScheduleDateAscTimeSlotAsc(
        LocalDate startDate, LocalDate endDate, String status);
}
