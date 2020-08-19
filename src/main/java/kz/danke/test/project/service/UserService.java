package kz.danke.test.project.service;

import kz.danke.test.project.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllWithCourses();

    void addCourseToUser(String userId, String courseId);

    void deleteExistingCourse(String userId, String courseId);
}
