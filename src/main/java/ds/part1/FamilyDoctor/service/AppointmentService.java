package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Appointment;
import ds.part1.FamilyDoctor.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional
    public void saveAppointment(Appointment appointment){
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void updateAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment getAppointment(Long appointment_id) {
        return appointmentRepository.findById(appointment_id).get();
    }

    @Transactional
    public void deleteAppointment(Long appointment_id){
        appointmentRepository.deleteById(appointment_id);
    }

}