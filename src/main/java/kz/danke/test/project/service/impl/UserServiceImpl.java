package kz.danke.test.project.service.impl;

import kz.danke.test.project.configuration.security.CustomUserDetails;
import kz.danke.test.project.exception.NotFoundException;
import kz.danke.test.project.model.Course;
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
    private final CrudOperations<Course> courseCrudOperations;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CrudOperations<Course> courseCrudOperations) {
        this.userRepository = userRepository;
        this.courseCrudOperations = courseCrudOperations;
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
                .orElseThrow(() -> new NotFoundException(String.format("User with ID %s not found", id)));
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCourseToUser(String userId, String courseId) {
        Course courseById = courseCrudOperations.findById(courseId);

        userRepository.insertUserIdAndCourseId(Long.parseLong(userId), courseById.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteExistingCourse(String userId, String courseId) {
        User userInSession = findById(userId);

        long idCourseToDelete = Long.parseLong(courseId);

        List<Course> courses = userInSession.getCourses();

        boolean isCourseExists = courses
                .stream()
                .anyMatch(course -> course.getId().equals(idCourseToDelete));

        if (isCourseExists) {
            userRepository.deleteCourseFromUser(
                    Long.parseLong(userId),
                    Long.parseLong(courseId)
            );
        } else {
            throw new NotFoundException("User course not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        return new CustomUserDetails(user);
    }
}
