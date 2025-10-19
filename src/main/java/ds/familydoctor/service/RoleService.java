package ds.familydoctor.service;

import ds.familydoctor.entity.Role;
import ds.familydoctor.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepo;

    @Transactional
    public Role findByName(String roleName){
        return roleRepo.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException(roleName + " role not found."));
    }

    @Transactional
    public Role findOrCreate(String roleName) {
        return roleRepo.findByName(roleName)
                .orElseGet(() -> roleRepo.save(new Role(roleName)));

    }
}
