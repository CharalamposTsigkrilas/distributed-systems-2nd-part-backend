package ds.familydoctor.service;

import ds.familydoctor.entity.User;
import ds.familydoctor.repository.UserRepository;
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
        return userRepo.findById(userId).get();
    }

    @Transactional
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Transactional
    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepo.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

}
