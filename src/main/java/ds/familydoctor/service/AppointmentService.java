package ds.familydoctor.service;

import ds.familydoctor.entity.*;
import ds.familydoctor.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appoRepo;

    @Autowired
    private FamilyMemberService fmService;

    @Transactional
    public Appointment getAppointment(Long appointmentId) {
        return appoRepo.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with this id: " + appointmentId));
    }

    @Transactional
    public List<Appointment> getAllAppointments() {
        return appoRepo.findAll();
    }

    @Transactional
    public Appointment save(Appointment appo) {
        appo.setStatus(Appointment.AppointmentStatus.PENDING);
        return appoRepo.save(appo);
    }

    @Transactional
    public Appointment update(Appointment appo) {
        return appoRepo.save(appo);
    }

    @Transactional
    public void delete(Long appointmentId) {
        if (!appoRepo.existsById(appointmentId)) {
            throw new EntityNotFoundException("Appointment not found with this id: " + appointmentId);
        }
        appoRepo.deleteById(appointmentId);
    }

    @Transactional
    public boolean doctorHasAppointmentAtThisTime(Doctor doc, OffsetDateTime appointmentDateTime) {
        return appoRepo.existsByDoctorAndAppointmentDateTime(doc, appointmentDateTime);
    }

    @Transactional
    public boolean doctorHasAppointmentWithThisFamilyMember(Doctor doc, FamilyMember fm, Appointment.AppointmentStatus status) {
        return appoRepo.existsByDoctorAndFamilyMemberAndStatus(doc, fm, status);
    }

    @Transactional
    public Appointment bookAppointment(Long familyMemberId, OffsetDateTime appointmentDateTime) {
        FamilyMember fm = fmService.getFamilyMember(familyMemberId);
        Citizen citizen = fm.getCitizen();
        Doctor doc = citizen.getFamilyDoctor();

        if (doc == null) {
            throw new IllegalStateException("Citizen has no assigned family doctor.");
        }

        if (!appointmentDateTime.isAfter(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Appointment must be in the future");
        }

        // family member mustn't have pending appointments
        if (fm.hasActiveAppointment()) {
            throw new IllegalStateException("FamilyMember already has a pending appointment");
        }

        // no conflicting appointment for doctor at same datetime
        if (doctorHasAppointmentAtThisTime(doc, appointmentDateTime)) {
            throw new IllegalStateException("Doctor is busy at that time");
        }

        // also ensure no existing pending appointment between same doctor & familyMember
        if (doctorHasAppointmentWithThisFamilyMember(doc, fm, Appointment.AppointmentStatus.PENDING)) {
            throw new IllegalStateException("Pending appointment already exists with this doctor for this family member");
        }

        Appointment appo = new Appointment();
        appo.setFamilyMember(fm);
        appo.setDoctor(doc);
        appo.setAppointmentDateTime(appointmentDateTime);
        appo.setStatus(Appointment.AppointmentStatus.PENDING);

        return save(appo);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appo = getAppointment(appointmentId);
        if (appo.getStatus()!= Appointment.AppointmentStatus.PENDING) {
            throw new IllegalStateException("Only pending appointments can be canceled.");
        }

        appo.setStatus(Appointment.AppointmentStatus.CANCELED);
        save(appo);
    }

    @Transactional
    public void completeAppointment(Long appointmentId) {
        Appointment appo = getAppointment(appointmentId);
        if (appo.getStatus()!= Appointment.AppointmentStatus.PENDING) {
            throw new IllegalStateException("Only pending appointments can be completed.");
        }

        appo.setStatus(Appointment.AppointmentStatus.COMPLETED);
        save(appo);
    }
}
