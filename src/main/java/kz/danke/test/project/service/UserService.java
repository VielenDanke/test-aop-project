package kz.danke.test.project.service;

import kz.danke.test.project.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllWithCourses();
}
