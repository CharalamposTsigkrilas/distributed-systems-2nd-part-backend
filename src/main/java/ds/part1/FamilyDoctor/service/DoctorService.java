package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Appointment;
import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.Role;
import ds.part1.FamilyDoctor.repository.DoctorRepository;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void createDefaultDoctors(){
        if (doctorRepository.count() == 0) {
            Doctor doctor1 = new Doctor("Iwannis Athanasiadis","GiannisAtha",passwordEncoder.encode("1234"),"gatha@gmail.com","2100000123","Sterea Ellada", "Attiki","Kardiologos","Davaki 11, Kallithea");
            Doctor doctor2 = new Doctor("Giorgos Alexopoulos","GiorgosAlex",passwordEncoder.encode("1234"),"galexop@gmail.com","2100000321","Sterea Ellada", "Evia","Kardiologos","Davaki 11, Kallithea");

            Set<Role> roles = new HashSet<>();

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            Role doctorRole = roleRepository.findByName("ROLE_DOCTOR")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(doctorRole);

            doctor1.setRoles(roles);
            doctor1.setAppointmentsCompleted(0);
            doctor1.setRating(0F);
            doctor1.setMaxNumberOfCitizens(10);
            doctorRepository.save(doctor1);


            doctor2.setRoles(roles);
            doctor2.setAppointmentsCompleted(0);
            doctor2.setRating(0F);
            doctor2.setMaxNumberOfCitizens(5);
            doctorRepository.save(doctor2);

        }
    }

    @Transactional
    public Doctor getDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId).get();
    }

    @Transactional
    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    @Transactional
    public void saveDoctor(Doctor doctor){
        doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    @Transactional
    public void updateDoctor(Doctor doctor){
        doctorRepository.save(doctor);
    }

    @Transactional
    public List<Appointment> getDoctorAppointments(Long doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getAppointments();
    }

    @Transactional
    public List<Citizen> getDoctorCitizens(Long doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getCitizens();
    }
}
