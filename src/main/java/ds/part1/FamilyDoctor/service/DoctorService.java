package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.*;
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
            Doctor doctor1 = new Doctor("Iwannis Athanasiadis","doctor1",passwordEncoder.encode("1234"),"gatha@gmail.com","2100000123","Sterea Ellada", "Attiki","Kardiologos","Davaki 11, Kallithea");
            Doctor doctor2 = new Doctor("Giorgos Alexopoulos","doctor2",passwordEncoder.encode("1234"),"galexop@gmail.com","2100000321","Sterea Ellada", "Evia","Pathologos","Makri Ippokratis 35, Nea Artaki Evia");
            Doctor doctor3 = new Doctor("Hrw Alexandrou","doctor3",passwordEncoder.encode("1234"),"halexandrou@gmail.com","2100000333","Kriti", "Xania","Kardiologos","Ethnikis Antistaseos, Neapoli Lasithiou");
            Doctor doctor4 = new Doctor("Mhltos Anastasiou","doctor4",passwordEncoder.encode("1234"),"manastasiou@gmail.com","2100000222","Makedonia", "Thessaloniki","Orila","Loutron 35, Lagadas Thessaloniki");
            Doctor doctor5 = new Doctor("Nikoleta Ioannou","doctor5",passwordEncoder.encode("1234"),"nioanou@gmail.com","2100000111","Makedonia", "Kavala","Odontiatros","Papachristidis Fr. 119, Eleftheroupolis Kavala");

            Set<Role> roles = new HashSet<>();

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            Role doctorRole = roleRepository.findByName("ROLE_DOCTOR")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(doctorRole);

            doctor1.setRoles(roles);
            doctor2.setRoles(roles);
            doctor3.setRoles(roles);
            doctor4.setRoles(roles);
            doctor5.setRoles(roles);

            doctor1.setMaxNumberOfCitizens(10);
            doctor2.setMaxNumberOfCitizens(5);
            doctor3.setMaxNumberOfCitizens(8);
            doctor4.setMaxNumberOfCitizens(34);
            doctor5.setMaxNumberOfCitizens(21);

            saveDoctor(doctor1);
            saveDoctor(doctor2);
            saveDoctor(doctor3);
            saveDoctor(doctor4);
            saveDoctor(doctor5);
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
        doctor.setAppointmentsCompleted(0);
        doctor.setRating(0F);
        doctorRepository.save(doctor);
    }

    @Transactional
    public void updateDoctor(Doctor doctor){
        doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
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

    @Transactional
    public List<Request> getDoctorRequests(Long doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getRequests();
    }
}
