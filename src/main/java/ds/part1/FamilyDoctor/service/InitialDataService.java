//package ds.part1.FamilyDoctor.service;
//
//import ds.part1.FamilyDoctor.entity.*;
//import ds.part1.FamilyDoctor.repository.RoleRepository;
//import ds.part1.FamilyDoctor.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class InitialDataService {
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private DoctorService doctorService;
//
//    @Autowired
//    private CitizenService citizenService;
//
//    @Autowired
//    private FamilyMemberService familyMemberService;
//
//    @Autowired
//    private AppointmentService appointmentService;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    private void creatRoles(){
//        roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
//            roleRepository.save(new Role("ROLE_ADMIN"));
//            return null;
//        });
//
//        roleRepository.findByName("ROLE_USER").orElseGet(() -> {
//            roleRepository.save(new Role("ROLE_USER"));
//            return null;
//        });
//
//        roleRepository.findByName("ROLE_DOCTOR").orElseGet(() -> {
//            roleRepository.save(new Role("ROLE_DOCTOR"));
//            return null;
//        });
//
//        roleRepository.findByName("ROLE_CITIZEN").orElseGet(() -> {
//            roleRepository.save(new Role("ROLE_CITIZEN"));
//            return null;
//        });
//    }
//
//    private void createAdmin(){
//        User admin = new User("My Name Is Admin","admin",
//                passwordEncoder.encode("1234"), "admin@hua.gr",
//                "2101234567","Sterea Ellada", "Attiki");
//
//        Set<Role> roles = new HashSet<>();
//
//        //Save admin with role admin and role user
//        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(adminRole);
//
//        Role userRole = roleRepository.findByName("ROLE_USER")
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(userRole);
//
//        admin.setRoles(roles);
//        userRepository.save(admin);
//    }
//
//    private void createDoctors(){
//        Doctor doctor1 = new Doctor("Iwannis Athanasiadis","GiannisAtha",passwordEncoder.encode("1234"),"gatha@gmail.com","2100000123","Sterea Ellada", "Attiki","Kardiologos","Davaki 11, Kallithea");
//        Doctor doctor2 = new Doctor("Giorgos Alexopoulos","GiorgosAlex",passwordEncoder.encode("1234"),"galexop@gmail.com","2100000321","Sterea Ellada", "Evia","Kardiologos","Davaki 11, Kallithea");
//
//        Set<Role> roles = new HashSet<>();
//
//        Role userRole = roleRepository.findByName("ROLE_USER")
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(userRole);
//
//        Role doctorRole = roleRepository.findByName("ROLE_DOCTOR")
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(doctorRole);
//
//        doctor1.setRoles(roles);
//        doctor1.setMaxNumberOfCitizens(10);
//        doctorService.saveDoctor(doctor1);
//
//        doctor2.setRoles(roles);
//        doctor2.setMaxNumberOfCitizens(5);
//        doctorService.saveDoctor(doctor2);
//    }
//
//    private void createCitizens(){
//        Citizen citizen1 = new Citizen("Kyriakos Antoniadis", "KoulisAnto",passwordEncoder.encode("1234"),"kanto@gmail.com","2101230000","Sterea Ellada", "Attiki","20050301234","Tompra 3");
//        Citizen citizen2 = new Citizen("Nikolas Zafeiriadis", "NikcZafei",passwordEncoder.encode("1234"),"nzafeiriadis@gmail.com","2103210000","Sterea Ellada", "Viotia","20059001234","Kosmitorou 23");
//
//        Set<Role> roles = new HashSet<>();
//
//        Role userRole = roleRepository.findByName("ROLE_USER")
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(userRole);
//
//        Role citizenRole = roleRepository.findByName("ROLE_CITIZEN")
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(citizenRole);
//
//        FamilyMember famMem1 = new FamilyMember("Mirto Vasileiou","01010100001","Wife");
//        famMem1.setCitizen(citizen1);
//
//        List<FamilyMember> fms1 = new ArrayList<>();
//        fms1.add(famMem1);
//        citizen1.setFamilyMembers(fms1);
//
//        familyMemberService.saveFamilyMember(famMem1);
//
//        FamilyMember famMem2 = new FamilyMember("Konstantina Katsiri","01010100002","Wife");
//        famMem2.setCitizen(citizen2);
//
//        List<FamilyMember> fms2 = new ArrayList<>();
//        fms2.add(famMem2);
//        citizen2.setFamilyMembers(fms2);
//
//        familyMemberService.saveFamilyMember(famMem2);
//
//        citizen1.setRoles(roles);
//        citizenService.saveCitizen(citizen1);
//
//        citizen2.setRoles(roles);
//        citizenService.saveCitizen(citizen2);
//    }
//
////    private void createFamilyMembers(){
////        FamilyMember famMem1 = new FamilyMember("Mirto Vasileiou","01010100001","Wife");
////        //Citizen citizen1 = citizenService.getCitizen(4L);
////        famMem1.setCitizen(citizen1);
////
////        List<FamilyMember> fm = citizen1.getFamilyMembers();
////        fm.add(famMem1);
////        citizen1.setFamilyMembers(fm);
////
////        familyMemberService.saveFamilyMember(famMem1);
////
////        FamilyMember famMem2 = new FamilyMember("Konstantina Katsiri","01010100002","Wife");
////        //Citizen citizen2 = citizenService.getCitizen(5L);
////        famMem2.setCitizen(citizen2);
////
////        List<FamilyMember> fm2 = citizen2.getFamilyMembers();
////        fm2.add(famMem2);
////        citizen2.setFamilyMembers(fm2);
////
////        familyMemberService.saveFamilyMember(famMem2);
////    }
//
//    @PostConstruct
//    public void setup() {
//        if (userRepository.count()!=0){
//            return;
//        }
//        this.creatRoles();
//        this.createAdmin();
//        this.createDoctors();
//        this.createCitizens();
//        //this.createFamilyMembers();
//    }
//
//}
