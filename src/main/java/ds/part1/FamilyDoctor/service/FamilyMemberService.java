package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Citizen;
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

            //famMem1.setCitizen(citizen1);
            //famMem2.setCitizen(citizen2);

            List<FamilyMember> fms1 = citizen1.getFamilyMembers();
            List<FamilyMember> fms2 = citizen2.getFamilyMembers();

            fms1.add(famMem1);
            fms2.add(famMem2);

            citizen1.setFamilyMembers(fms1);
            citizen2.setFamilyMembers(fms2);

            citizenService.updateCitizen(citizen1);
            citizenService.updateCitizen(citizen2);

            familyMemberRepository.save(famMem1);
            familyMemberRepository.save(famMem2);
        }

    }

    @Transactional
    public void saveFamilyMember(FamilyMember familyMember){
        familyMemberRepository.save(familyMember);
    }

    @Transactional
    public FamilyMember getFamilyMember(Long familyMemberId) {
        return familyMemberRepository.findById(familyMemberId).get();
    }

    @Transactional
    public List<FamilyMember> getAllFamilyMembers() {
        return familyMemberRepository.findAll();
    }

    @Transactional
    public void updateFamilyMember(FamilyMember familyMember){
        familyMemberRepository.save(familyMember);
    }

    @Transactional
    public void deleteFamilyMember(Long familyMemberId){
        familyMemberRepository.deleteById(familyMemberId);
    }
}