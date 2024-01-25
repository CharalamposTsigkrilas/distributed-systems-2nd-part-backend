package ds.part1.FamilyDoctor.repository;

import ds.part1.FamilyDoctor.entity.Citizen;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path= "citizen")
@Hidden
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

}