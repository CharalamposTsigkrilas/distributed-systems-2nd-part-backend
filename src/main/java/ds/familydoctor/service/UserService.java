package ds.familydoctor.service;

import ds.familydoctor.dto.user.CreateUserDto;
import ds.familydoctor.entity.*;
import ds.familydoctor.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User getUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id: " + userId + "."));
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Transactional
    public User save(User user) {
        return userRepo.save(user);
    }

    @Transactional
    public User update(User user) {
        return userRepo.save(user);
    }

    @Transactional
    public void delete(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("User not found with this id: " + userId + ".");
        }
        userRepo.deleteById(userId);
    }

    @Transactional
    public boolean alreadyExists(String username, String email) {
        return userRepo.existsByUsername(username) || userRepo.existsByEmail(email);
    }

    @Transactional
    public User createUserWithRole(CreateUserDto userDto, String roleName) {
        if (alreadyExists(userDto.getUsername(), userDto.getEmail())) {
            throw new IllegalStateException("Username or Email already exists.");
        }

        Role role = roleService.findByName(roleName);

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setHomeAddress(userDto.getHomeAddress());
        user.setCountry(userDto.getCountry());
        user.setContinent(userDto.getContinent());
        user.setPrefecture(userDto.getPrefecture());
        user.setCity(userDto.getCity());
        user.addRole(role);
        return save(user);
    }

}
