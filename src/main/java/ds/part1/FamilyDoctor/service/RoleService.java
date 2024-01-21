package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Role;
import ds.part1.FamilyDoctor.repository.FamilyMemberRepository;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    }

    @Transactional
    public Role getRole(Long roleId) {
        return roleRepository.findById(roleId).get();
    }
}