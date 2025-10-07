package ds.familydoctor.service;

import ds.familydoctor.entity.FamilyMember;
import ds.familydoctor.repository.FamilyMemberRepository;
import jakarta.persistence.EntityNotFoundException;
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
        return fmRepo.findById(familyMemberId)
                .orElseThrow(() -> new EntityNotFoundException("Family Member not found with this id: " + familyMemberId));
    }

    @Transactional
    public List<FamilyMember> getAllFamilyMembers() {
        return fmRepo.findAll();
    }

    @Transactional
    public FamilyMember save(FamilyMember fm) {
        return fmRepo.save(fm);
    }

    @Transactional
    public FamilyMember update(FamilyMember fm) {
        return fmRepo.save(fm);
    }

    @Transactional
    public void delete(Long familyMemberId) {
        if (!fmRepo.existsById(familyMemberId)) {
            throw new EntityNotFoundException("Family Member not found with this id: " + familyMemberId);
        }
        fmRepo.deleteById(familyMemberId);
    }

//    @Transactional
//    public FamilyMember createMember() {
//
//    }

}
