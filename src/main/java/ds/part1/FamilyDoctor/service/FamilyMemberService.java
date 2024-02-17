package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Appointment;
import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.FamilyMember;
import ds.part1.FamilyDoctor.repository.CitizenRepository;
import ds.part1.FamilyDoctor.repository.FamilyMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyMemberService {

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private CitizenService citizenService;

    @Transactional
    public void createDefaultFamilyMembers(){
        if (familyMemberRepository.count() <= citizenRepository.count()) {

            FamilyMember famMem1 = new FamilyMember("Mirto Vasileiou","01010100001","Wife");
            FamilyMember famMem2 = new FamilyMember("Konstantina Katsiri","01010100002","Wife");

            List<Citizen> citizens = citizenService.getCitizens();

            if(citizens.isEmpty()){
                return;
            }

            Citizen citizen1 = citizens.get(0);
            Citizen citizen2 = citizens.get(1);

            citizen1.getFamilyMembers().add(famMem1);
            citizen2.getFamilyMembers().add(famMem2);


            citizen1.setFamilyMembers(citizen1.getFamilyMembers());
            citizen2.setFamilyMembers(citizen2.getFamilyMembers());

            citizenService.updateCitizen(citizen1);
            citizenService.updateCitizen(citizen2);

            familyMemberRepository.save(famMem1);
            familyMemberRepository.save(famMem2);
        }

    }

    @Transactional
    public FamilyMember getFamilyMember(Long familyMemberId) {
        return familyMemberRepository.findById(familyMemberId).get();
    }

    @Transactional
    public List<FamilyMember> getFamilyMembers() {
        return familyMemberRepository.findAll();
    }

    @Transactional
    public void saveFamilyMember(FamilyMember familyMember){
        familyMemberRepository.save(familyMember);
    }

    @Transactional
    public void updateFamilyMember(FamilyMember familyMember){
        familyMemberRepository.save(familyMember);
    }

    @Transactional
    public void deleteFamilyMember(Long familyMemberId){
        familyMemberRepository.deleteById(familyMemberId);
    }

    @Transactional
    public Citizen getFamilyMemberCitizen(Long familyMemberId){

        List<Citizen> citizens = citizenService.getCitizens();

        for (Citizen currentCitizen : citizens) {
            Long currentCitizenId = currentCitizen.getId();
            List<FamilyMember> citizenFamilyMembers = citizenService.getCitizenFamilyMembers(currentCitizenId);

            for (FamilyMember currentFamilyMember : citizenFamilyMembers){
                Long currentFamilyMemberId = currentFamilyMember.getId();

                if (currentFamilyMemberId.equals(familyMemberId)) {
                    return currentCitizen;
                }
            }
        }
        return null;
    }

    @Transactional
    public Doctor getFamilyMemberDoctor(Long familyMemberId){
        Citizen citizen = getFamilyMemberCitizen(familyMemberId);
        Long citizenId = citizen.getId();
        return citizenService.getCitizenDoctor(citizenId);
    }

    @Transactional
    public Appointment getFamilyMemberAppointment(Long familyMemberId){
        FamilyMember familyMember = familyMemberRepository.findById(familyMemberId).get();
        return familyMember.getAppointment();
    }
}