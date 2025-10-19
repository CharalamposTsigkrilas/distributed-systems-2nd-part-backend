package ds.familydoctor.repository;

import ds.familydoctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByAfm(String afm);

    boolean existsByAfm(String afm);
}
