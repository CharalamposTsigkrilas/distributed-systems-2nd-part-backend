package ds.familydoctor.service;

import ds.familydoctor.entity.Appointment;
import ds.familydoctor.entity.FamilyMember;
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

}
