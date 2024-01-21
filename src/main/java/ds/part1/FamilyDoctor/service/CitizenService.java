package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.FamilyMember;
import ds.part1.FamilyDoctor.entity.Role;
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

    @Transactional
    public void createDefaultCitizens(){
        if (citizenRepository.count() == 0) {
            Citizen citizen1 = new Citizen("Kyriakos Antoniadis", "KoulisAnto",passwordEncoder.encode("11223344"),"kanto@gmail.com","2101230000","Sterea Ellada", "Attiki","20050301234","Tompra 3");
            Citizen citizen2 = new Citizen("Nikolas Zafeiriadis", "NikcZafei",passwordEncoder.encode("44332211"),"nzafeiriadis@gmail.com","2103210000","Sterea Ellada", "Viotia","20059001234","Kosmitorou 23");

            Set<Role> roles = new HashSet<>();

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            Role citizenRole = roleRepository.findByName("ROLE_CITIZEN")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(citizenRole);

            citizen1.setRoles(roles);
            saveCitizen(citizen1);

            citizen2.setRoles(roles);
            saveCitizen(citizen2);
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
        familyMember.setCitizen(citizen);

        List<FamilyMember> fm = new ArrayList<>();
        fm.add(familyMember);
        citizen.setFamilyMembers(fm);

        familyMemberRepository.save(familyMember);
        citizenRepository.save(citizen);

    }

    @Transactional
    public void deleteCitizen(Long citizenId) {
        citizenRepository.deleteById(citizenId);
    }

    @Transactional
    public void updateCitizen(Citizen citizen){
        citizenRepository.save(citizen);
    }

    @Transactional
    public Doctor getCitizenDoctor(Long citizenId){
        Citizen citizen = citizenRepository.findById(citizenId).get();
        return citizen.getDoctor();
    }

    @Transactional
    public List<FamilyMember> getCitizenFamilyMembers(Long citizenId){
        Citizen citizen = citizenRepository.findById(citizenId).get();
        return citizen.getFamilyMembers();
    }

}