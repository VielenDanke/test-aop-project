package kz.danke.test.project.service.impl;

import kz.danke.test.project.exception.NotFoundException;
import kz.danke.test.project.model.Course;
import kz.danke.test.project.repository.CourseRepository;
import kz.danke.test.project.service.CrudOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl implements CrudOperations<Course> {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> findAll() {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Course findById(String id) {
        long courseId = Long.parseLong(id);

        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(String.format("Course with ID %s not found", id)));
    }

    @Override
    public Course save(Course course) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
