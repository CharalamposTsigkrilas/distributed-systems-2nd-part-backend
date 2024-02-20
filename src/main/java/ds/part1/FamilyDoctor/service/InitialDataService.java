package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.*;
import ds.part1.FamilyDoctor.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InitialDataService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private void creatRoles(){
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
    }

    private void createAdmin(){

        if (userRepository.count() == 0) {

            User admin = new User("My Name Is Admin", "admin",
                    passwordEncoder.encode("1234"), "admin@hua.gr",
                    "2101234567", "Sterea Ellada", "Attiki");

            Set<Role> roles = new HashSet<>();

            //Save admin with role admin and role user
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            admin.setRoles(roles);
            userRepository.save(admin);
        }

    }

    private void createDoctors(){

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

            doctorService.saveDoctor(doctor1);
            doctorService.saveDoctor(doctor2);
            doctorService.saveDoctor(doctor3);
            doctorService.saveDoctor(doctor4);
            doctorService.saveDoctor(doctor5);
        }

    }

    private void createCitizens(){

        if (citizenRepository.count() == 0) {

            Citizen citizen1 = new Citizen("Kyriakos Antoniadis", "citizen1",passwordEncoder.encode("1234"),"kanto@gmail.com","2101230000","Sterea Ellada", "Attiki","20050301234","Tompra 3");
            Citizen citizen2 = new Citizen("Nikolas Zafeiriadis", "citizen2",passwordEncoder.encode("1234"),"nzafeiriadis@gmail.com","2103210000","Sterea Ellada", "Viotia","20059001234","Kosmitorou 23");
            Citizen citizen3 = new Citizen("Kwnstantinos Pappadopoulos", "citizen3",passwordEncoder.encode("1234"),"kwstaspap@gmail.com","2103212000","Kriti", "Xania","21089901234","25hs Martiou 34");
            Citizen citizen4 = new Citizen("Mery Politou", "citizen4",passwordEncoder.encode("1234"),"merypolitou@gmail.com","2103213000","Makedonia", "Thessaloniki","21079501234","Skoufa 5");
            Citizen citizen5 = new Citizen("Gwgw Georgiou", "citizen5",passwordEncoder.encode("1234"),"gwgwgeorgiou@gmail.com","2103214000","Makedonia", "Thessaloniki","20050101234","Kallirois 4");

            Set<Role> roles = new HashSet<>();

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            Role citizenRole = roleRepository.findByName("ROLE_CITIZEN")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(citizenRole);

            citizen1.setRoles(roles);
            citizen2.setRoles(roles);
            citizen3.setRoles(roles);
            citizen4.setRoles(roles);
            citizen5.setRoles(roles);

            citizenService.saveCitizen(citizen1);
            citizenService.saveCitizen(citizen2);
            citizenService.saveCitizen(citizen3);
            citizenService.saveCitizen(citizen4);
            citizenService.saveCitizen(citizen5);
        }

    }

    private void createFamilyMembers(){
        if (familyMemberRepository.count() <= citizenRepository.count()) {

            FamilyMember famMem1 = new FamilyMember("Mirto Vasileiou","01010100001","Wife");
            FamilyMember famMem2 = new FamilyMember("Konstantina Katsiri","01010100002","Wife");

            List<Citizen> citizens = citizenService.getCitizens();

            if(citizens.isEmpty()){
                return;
            }

            Citizen citizen1 = citizens.get(0);
            Citizen citizen2 = citizens.get(1);

            List<FamilyMember> citizen1FamilyMembers = citizen1.getFamilyMembers();
            List<FamilyMember> citizen2FamilyMembers = citizen2.getFamilyMembers();

            citizen1FamilyMembers.add(famMem1);
            citizen2FamilyMembers.add(famMem2);

            citizen1.setFamilyMembers(citizen1FamilyMembers);
            citizen2.setFamilyMembers(citizen2FamilyMembers);

            citizenService.updateCitizen(citizen1);
            citizenService.updateCitizen(citizen2);

            familyMemberService.saveFamilyMember(famMem1);
            familyMemberService.saveFamilyMember(famMem2);
        }
    }

    @PostConstruct
    public void setup() {
        if (userRepository.count()!=0){
            return;
        }
        this.creatRoles();
        this.createAdmin();
        this.createDoctors();
        this.createCitizens();
        this.createFamilyMembers();
    }

}
