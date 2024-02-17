package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.*;
import ds.part1.FamilyDoctor.repository.CitizenRepository;
import ds.part1.FamilyDoctor.repository.FamilyMemberRepository;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CitizenService {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private DoctorService doctorService;

    @Transactional
    public void createDefaultCitizens(){
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

            saveCitizen(citizen1);
            saveCitizen(citizen2);
            saveCitizen(citizen3);
            saveCitizen(citizen4);
            saveCitizen(citizen5);
        }
    }

    @Transactional
    public Citizen getCitizen(Long citizenId) {
        return citizenRepository.findById(citizenId).get();
    }

    @Transactional
    public List<Citizen> getCitizens(){
        return citizenRepository.findAll();
    }

    @Transactional
    public void saveCitizen(Citizen citizen){

        //Save citizen as a family member
        FamilyMember familyMember = new FamilyMember();
        familyMember.setFullName(citizen.getFullName());
        familyMember.setAMKA(citizen.getAMKA());
        familyMember.setMemberRelationship("-");

        List<FamilyMember> fm = new ArrayList<>();
        fm.add(familyMember);
        citizen.setFamilyMembers(fm);

        familyMemberRepository.save(familyMember);
        citizenRepository.save(citizen);

    }

    @Transactional
    public void updateCitizen(Citizen citizen){
        citizenRepository.save(citizen);
    }

    @Transactional
    public void deleteCitizen(Long citizenId) {
        citizenRepository.deleteById(citizenId);
    }

    @Transactional
    public Doctor getCitizenDoctor(Long citizenId){

        List<Doctor> doctors = doctorService.getDoctors();

        for (Doctor currentDoctor : doctors){
            Long currentDoctorId = currentDoctor.getId();
            List<Citizen> doctorCitizens = doctorService.getDoctorCitizens(currentDoctorId);

            for (Citizen currentCitizen : doctorCitizens) {
                Long currentCitizenId = currentCitizen.getId();

                if (currentCitizenId.equals(citizenId)){
                    return currentDoctor;
                }
            }
        }
        return null;
    }

    @Transactional
    public Request getCitizenRequest(Long citizenId){
        Citizen citizen = citizenRepository.findById(citizenId).get();
        return citizen.getRequest();
    }

    @Transactional
    public List<FamilyMember> getCitizenFamilyMembers(Long citizenId){
        Citizen citizen = citizenRepository.findById(citizenId).get();
        return citizen.getFamilyMembers();
    }

}