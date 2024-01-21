package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.FamilyMember;
import ds.part1.FamilyDoctor.repository.CitizenRepository;
import ds.part1.FamilyDoctor.repository.FamilyMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            Citizen citizen1 = citizenService.getCitizen(5L);
            famMem1.setCitizen(citizen1);

            List<FamilyMember> fm = citizen1.getFamilyMembers();
            fm.add(famMem1);
            citizen1.setFamilyMembers(fm);

            familyMemberRepository.save(famMem1);

            FamilyMember famMem2 = new FamilyMember("Konstantina Katsiri","01010100002","Wife");
            Citizen citizen2 = citizenService.getCitizen(6L);
            famMem2.setCitizen(citizen2);

            List<FamilyMember> fm2 = citizen2.getFamilyMembers();
            fm2.add(famMem2);
            citizen2.setFamilyMembers(fm2);

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