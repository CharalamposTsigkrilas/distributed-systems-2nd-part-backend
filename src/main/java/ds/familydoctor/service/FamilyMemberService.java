package ds.familydoctor.service;

import ds.familydoctor.entity.FamilyMember;
import ds.familydoctor.repository.FamilyMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyMemberService {

    @Autowired
    private FamilyMemberRepository fmRepo;

    @Transactional
    public FamilyMember getFamilyMember(Long familyMemberId) {
        return fmRepo.findById(familyMemberId).get();
    }

    @Transactional
    public List<FamilyMember> getFamilyMembers() {
        return fmRepo.findAll();
    }

    @Transactional
    public void saveFamilyMember(FamilyMember fm) {
        fmRepo.save(fm);
    }

    @Transactional
    public void updateFamilyMember(FamilyMember fm) {
        fmRepo.save(fm);
    }

    @Transactional
    public void deleteFamilyMember(Long familyMemberId) {
        fmRepo.deleteById(familyMemberId);
    }

}
