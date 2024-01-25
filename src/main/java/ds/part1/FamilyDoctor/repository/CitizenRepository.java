package ds.part1.FamilyDoctor.repository;

import ds.part1.FamilyDoctor.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository//RestResource(path= "citizen")
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

}