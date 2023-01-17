package chatApi.service;

import chatApi.DTO.UserDto;
import chatApi.model.Status;
import chatApi.model.User;
import chatApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void saveNewUser(UserDto userDto) {
        User user = new User(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("user.already.exist");
        }
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("user not found"));
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("user not found"));
        if (user.getStatus().equals(Status.ACTIVE)) {
            user.setStatus(Status.Banned);
        } else {
            user.setStatus(Status.ACTIVE);
        }
        userRepository.save(user);
    }
}
