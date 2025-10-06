package ds.familydoctor.repository;

import ds.familydoctor.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorAndAppointmentDateTime(Doctor doc, OffsetDateTime appoDateTime);

    boolean existsByDoctorAndFamilyMemberAndStatus(Doctor doc, FamilyMember fm, Appointment.AppointmentStatus status);

}
