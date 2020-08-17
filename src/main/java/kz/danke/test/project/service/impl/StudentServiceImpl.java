package kz.danke.test.project.service.impl;

import kz.danke.test.project.model.Student;
import kz.danke.test.project.repository.StudentRepository;
import kz.danke.test.project.service.CrudOperations;
import kz.danke.test.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements CrudOperations<Student>, StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAllWithCourses() {
        return studentRepository.findAllWithCourse();
    }

    @Override
    @Transactional(readOnly = true)
    public Student findById(String id) {
        long studentId = Long.parseLong(id);

        return studentRepository.findByIdWithCourses(studentId)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        long studentId = Long.parseLong(id);

        studentRepository.deleteById(studentId);
    }
}
