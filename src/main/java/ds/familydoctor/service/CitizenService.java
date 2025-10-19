package ds.familydoctor.service;

import ds.familydoctor.dto.citizen.CreateCitizenDto;
import ds.familydoctor.entity.*;
import ds.familydoctor.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitizenService {

    @Autowired
    private CitizenRepository citiRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public Citizen getCitizen(Long citizenId) {
        return citiRepo.findById(citizenId)
                .orElseThrow(() -> new EntityNotFoundException("Citizen not found with this id: " + citizenId + "."));
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
    public void delete(Long citizenId) {
        if (!citiRepo.existsById(citizenId)){
            throw new EntityNotFoundException("Citizen not found with this id: " + citizenId + ".");
        }
        citiRepo.deleteById(citizenId);
    }

    @Transactional
    public boolean alreadyExists(String amka) {
        return citiRepo.existsByAmka(amka);
    }

    @Transactional
    public Citizen create(CreateCitizenDto citiDto){

        User user = userService.createUserWithRole(citiDto, "CITIZEN");

        if (alreadyExists(citiDto.getAmka())) {
            throw new IllegalStateException("AMKA already in use.");
        }

        Citizen citizen = new Citizen();
        citizen.setUser(user);
        citizen.setAmka(citiDto.getAmka());
        citizen.ensureSelfFamilyMember();
        return save(citizen);
    }
}
