package kz.danke.test.project.controller;

import com.fasterxml.jackson.annotation.JsonView;
import kz.danke.test.project.dto.UserDTO;
import kz.danke.test.project.dto.request.StudentSaveRequest;
import kz.danke.test.project.json.view.UserView;
import kz.danke.test.project.mapper.UserMapper;
import kz.danke.test.project.model.User;
import kz.danke.test.project.service.CrudOperations;
import kz.danke.test.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CrudOperations<User> studentCrudOperations;
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(CrudOperations<User> studentCrudOperations,
                          UserService userService,
                          UserMapper userMapper) {
        this.studentCrudOperations = studentCrudOperations;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    @JsonView(UserView.UserInfo.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> findAllStudents() {
        List<UserDTO> userDTOList = studentCrudOperations
                .findAll()
                .parallelStream()
                .map(userMapper::toStudentDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping(value = "/courses")
    @JsonView(UserView.FullInfo.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> findAllStudentsWithCourses() {
        List<UserDTO> userDTOList = userService
                .findAllWithCourses()
                .parallelStream()
                .map(userMapper::toFullStudentDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping(value = "/{id}")
    @JsonView(UserView.FullInfo.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> findStudentById(@PathVariable(name = "id") String id) {
        User user = studentCrudOperations.findById(id);

        UserDTO userDTO = userMapper.toFullStudentDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    @JsonView(UserView.UserInfo.class)
    public ResponseEntity<UserDTO> saveStudent(@Valid @RequestBody StudentSaveRequest saveRequest) {
        User user = new User(
                saveRequest.getUsername(), saveRequest.getPassword(), saveRequest.getFullName()
        );
        User savedUser = studentCrudOperations.save(user);

        UserDTO userDTO = userMapper.toStudentDTO(savedUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable(name = "id") String id) {
        studentCrudOperations.delete(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/cabinet")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @JsonView(UserView.FullInfo.class)
    public ResponseEntity<UserDTO> getPersonalCabinet(@AuthenticationPrincipal String id) {
        User user = studentCrudOperations.findById(id);

        UserDTO userDTO = userMapper.toFullStudentDTO(user);

        return ResponseEntity.ok(userDTO);
    }
}
