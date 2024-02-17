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
    private DoctorService doctorService;

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