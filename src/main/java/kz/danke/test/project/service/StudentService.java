package kz.danke.test.project.service;

import kz.danke.test.project.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> findAllWithCourses();
}
