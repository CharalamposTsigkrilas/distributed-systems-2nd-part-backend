package ds.familydoctor.service;

import ds.familydoctor.entity.Citizen;
import ds.familydoctor.repository.CitizenRepository;
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
        return citiRepo.findById(citizenId).get();
    }

    @Transactional
    public List<Citizen> getCitizens() {
        return citiRepo.findAll();
    }

    @Transactional
    public void saveCitizen(Citizen citi) {
        citi.ensureSelfFamilyMember();
        citiRepo.save(citi);
    }

    @Transactional
    public void updateCitizen(Citizen citi) {
        citiRepo.save(citi);
    }

    @Transactional
    public void deleteCitizen(Long citizenId) {
        citiRepo.deleteById(citizenId);
    }

}
