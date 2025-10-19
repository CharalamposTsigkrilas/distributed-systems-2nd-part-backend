package ds.familydoctor.service;

import ds.familydoctor.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitialDataService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleService roleService;

    @PostConstruct
    public void setup() {
        if (userRepo.count() == 0) {
            createRoles();
        }
    }

    public void createRoles() {
        roleService.findOrCreate("ADMIN");
        roleService.findOrCreate("DOCTOR");
        roleService.findOrCreate("CITIZEN");
    }
}
