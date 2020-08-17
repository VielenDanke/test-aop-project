package kz.danke.test.project.controller;

import com.fasterxml.jackson.annotation.JsonView;
import kz.danke.test.project.dto.StudentDTO;
import kz.danke.test.project.dto.request.StudentSaveRequest;
import kz.danke.test.project.json.view.StudentView;
import kz.danke.test.project.mapper.StudentMapper;
import kz.danke.test.project.model.Student;
import kz.danke.test.project.service.CrudOperations;
import kz.danke.test.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final CrudOperations<Student> studentCrudOperations;
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentController(CrudOperations<Student> studentCrudOperations,
                             StudentService studentService,
                             StudentMapper studentMapper) {
        this.studentCrudOperations = studentCrudOperations;
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    @JsonView(StudentView.StudentInfo.class)
    public ResponseEntity<List<StudentDTO>> findAllStudents() {
        List<StudentDTO> studentDTOList = studentCrudOperations
                .findAll()
                .parallelStream()
                .map(studentMapper::toStudentDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(studentDTOList);
    }

    @GetMapping(value = "/courses")
    @JsonView(StudentView.FullInfo.class)
    public ResponseEntity<List<StudentDTO>> findAllStudentsWithCourses() {
        List<StudentDTO> studentDTOList = studentService
                .findAllWithCourses()
                .parallelStream()
                .map(studentMapper::toFullStudentDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(studentDTOList);
    }

    @GetMapping(value = "/{id}")
    @JsonView(StudentView.FullInfo.class)
    public ResponseEntity<StudentDTO> findStudentById(@PathVariable(name = "id") String id) {
        Student student = studentCrudOperations.findById(id);

        StudentDTO studentDTO = studentMapper.toFullStudentDTO(student);

        return ResponseEntity.ok(studentDTO);
    }

    @PostMapping
    @JsonView(StudentView.StudentInfo.class)
    public ResponseEntity<StudentDTO> saveStudent(@Valid @RequestBody StudentSaveRequest saveRequest) {
        Student student = new Student(
                saveRequest.getUsername(), saveRequest.getPassword(), saveRequest.getFullName()
        );
        Student savedStudent = studentCrudOperations.save(student);

        StudentDTO studentDTO = studentMapper.toStudentDTO(savedStudent);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable(name = "id") String id) {
        studentCrudOperations.delete(id);

        return ResponseEntity.ok().build();
    }
}
