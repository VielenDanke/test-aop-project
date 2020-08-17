package kz.danke.test.project.service.impl;

import kz.danke.test.project.configuration.security.CustomUserDetails;
import kz.danke.test.project.model.User;
import kz.danke.test.project.repository.UserRepository;
import kz.danke.test.project.service.CrudOperations;
import kz.danke.test.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements CrudOperations<User>, UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllWithCourses() {
        return userRepository.findAllWithCourse();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(String id) {
        long studentId = Long.parseLong(id);

        return userRepository.findByIdWithCourses(studentId)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        long studentId = Long.parseLong(id);

        userRepository.deleteById(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        return new CustomUserDetails(user);
    }
}
