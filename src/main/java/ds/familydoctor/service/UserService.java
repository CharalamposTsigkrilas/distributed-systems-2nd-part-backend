package ds.familydoctor.service;

import ds.familydoctor.entity.User;
import ds.familydoctor.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public User getUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with this id: " + userId));
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
            throw new EntityNotFoundException("User not found with this id: " + userId);
        }
        userRepo.deleteById(userId);
    }

}
