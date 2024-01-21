package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Role;
import ds.part1.FamilyDoctor.entity.User;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import ds.part1.FamilyDoctor.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //It is called from Role service post construct. We need roles, before users.
    @Transactional
    public void createDefaultUsers() {

        if (userRepository.count() == 0) {

            User admin = new User("My Name Is Admin","IAmAdmin",
                    passwordEncoder.encode("pass1234"), "admin@hua.com",
                    "2101234567","Sterea Ellada", "Attiki");

            User user = new User("My Name Is User 0","IAmUser0",
                    passwordEncoder.encode("pass0000"), "userzero@hua.com",
                    "2101001012","Sterea Ellada", "Attiki");


            Set<Role> roles = new HashSet<>();

            //Save user with only role user
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            user.setRoles(roles);
            userRepository.save(user);

            //Save admin with role admin and role user
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            admin.setRoles(roles);
            userRepository.save(admin);

        }
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User getUser(Long user_id) {
        return userRepository.findById(user_id).get();
    }

    @Transactional
    public void deleteUser(Long user_id){
        userRepository.deleteById(user_id);
    }
}