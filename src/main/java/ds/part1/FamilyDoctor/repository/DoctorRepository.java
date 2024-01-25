package ds.part1.FamilyDoctor.repository;

import ds.part1.FamilyDoctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository//RestResource(path= "doctor")
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}