package ds.part1.FamilyDoctor.repository;

import ds.part1.FamilyDoctor.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {

}