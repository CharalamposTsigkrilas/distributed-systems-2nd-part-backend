package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.Role;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService  doctorService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Transactional
    @PostConstruct
    public void setup() {

        roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_ADMIN"));
            return null;
        });

        roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_USER"));
            return null;
        });

        roleRepository.findByName("ROLE_DOCTOR").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_DOCTOR"));
            return null;
        });

        roleRepository.findByName("ROLE_CITIZEN").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_CITIZEN"));
            return null;
        });

        //After roles creation we create all default objects
        userService.createDefaultUsers();
        doctorService.createDefaultDoctors();
        citizenService.createDefaultCitizens();
        familyMemberService.createDefaultFamilyMembers();

//        Citizen citizen = citizenService.getCitizen(4L);
//        Doctor doctor = doctorService.getDoctor(2L);

//        citizen.setDoctor(doctor);
//        citizenService.updateCitizen(citizen);
//        doctor.getCitizens().add(citizen);
//        doctorService.saveDoctor(doctor);
    }


}