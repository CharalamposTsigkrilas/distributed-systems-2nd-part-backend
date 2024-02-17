package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.FamilyMember;
import ds.part1.FamilyDoctor.repository.AppointmentRepository;
import ds.part1.FamilyDoctor.entity.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private DoctorService doctorService;

    @Transactional
    public Appointment getAppointment(Long appointment_id) {
        return appointmentRepository.findById(appointment_id).get();
    }

    @Transactional
    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    @Transactional
    public void saveAppointment(Appointment appointment){
        appointment.setCurrentStatus(Appointment.status.Set.toString());
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void updateAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void deleteAppointment(Long appointment_id){
        appointmentRepository.deleteById(appointment_id);
    }

    @Transactional
    public FamilyMember getAppointmentFamilyMember(Long appointment_id){
        List<FamilyMember> familyMembers = familyMemberService.getFamilyMembers();

        for (FamilyMember currentFamilyMember : familyMembers) {
            Appointment currentFamilyMemberAppointment = currentFamilyMember.getAppointment();
            Long currentFamilyMemberAppointmentId = currentFamilyMemberAppointment.getId();

            if (currentFamilyMemberAppointmentId.equals(appointment_id)) {
                return currentFamilyMember;
            }
        }
        return null;
    }

    @Transactional
    public Doctor getAppointmentDoctor(Long appointment_id){
        List<Doctor> doctors = doctorService.getDoctors();

        for (Doctor currentDoctor : doctors) {
            Long currentDoctorId = currentDoctor.getId();
            List<Appointment> doctorAppointments = doctorService.getDoctorAppointments(currentDoctorId);

            for (Appointment currentDoctorAppointment : doctorAppointments) {
                Long currentDoctorAppointmentId = currentDoctorAppointment.getId();

                if (currentDoctorAppointmentId.equals(appointment_id)){
                    return currentDoctor;
                }
            }
        }
        return null;
    }

}