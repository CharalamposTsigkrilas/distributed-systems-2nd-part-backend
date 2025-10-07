package ds.familydoctor.repository;

import ds.familydoctor.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    boolean existsByCitizenAndDoctor(Citizen citi, Doctor doc);

    List<Request> findByCitizen(Citizen citi);

}
