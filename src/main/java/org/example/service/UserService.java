package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepository.delete(user);
    }

    public Optional<User> update(User user, Long id) {
        return userRepository.findById(id).map(
                exist ->
        {
            exist.setFullName(user.getFullName());
            exist.setPassword(user.getPassword());
            exist.setAge(user.getAge());
            exist.setEmail(user.getEmail());
            exist.setPets(user.getPets());
            return userRepository.save(exist);
        });
    }
}
