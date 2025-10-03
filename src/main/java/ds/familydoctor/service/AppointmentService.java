package ds.familydoctor.service;

import ds.familydoctor.entity.Appointment;
import ds.familydoctor.entity.FamilyMember;
import ds.familydoctor.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
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
        return appoRepo.findById(appointmentId).get();
    }

    @Transactional
    public List<Appointment> getAppointments() {
        return appoRepo.findAll();
    }

    @Transactional
    public void saveAppointment(Appointment appo) {
        appo.setStatus(Appointment.AppointmentStatus.PENDING);
        appoRepo.save(appo);
    }

    @Transactional
    public void updateAppointment(Appointment appo) {
        appoRepo.save(appo);
    }

    @Transactional
    public void deleteAppointment(Long appointmentId) {
        appoRepo.deleteById(appointmentId);
    }

}
