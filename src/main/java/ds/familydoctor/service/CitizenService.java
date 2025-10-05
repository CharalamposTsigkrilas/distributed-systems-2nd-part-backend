package ds.familydoctor.service;

import ds.familydoctor.entity.Citizen;
import ds.familydoctor.repository.CitizenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitizenService {

    @Autowired
    private CitizenRepository citiRepo;

    @Transactional
    public Citizen getCitizen(Long citizenId) {
        return citiRepo.findById(citizenId)
                .orElseThrow(() -> new EntityNotFoundException("Citizen not found with this id: " + citizenId));
    }

    @Transactional
    public List<Citizen> getAllCitizens() {
        return citiRepo.findAll();
    }

    @Transactional
    public Citizen save(Citizen citi) {
        citi.ensureSelfFamilyMember();
        return citiRepo.save(citi);
    }

    @Transactional
    public Citizen update(Citizen citi) {
        return citiRepo.save(citi);
    }

    @Transactional
    public void deleteCitizen(Long citizenId) {
        if (!citiRepo.existsById(citizenId)){
            throw new EntityNotFoundException("Citizen not found with this id: " + citizenId);
        }
        citiRepo.deleteById(citizenId);
    }

}
