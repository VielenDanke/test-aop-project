package kz.danke.test.project.dto;

import com.fasterxml.jackson.annotation.JsonView;
import kz.danke.test.project.dto.response.CourseStudentResponse;
import kz.danke.test.project.json.view.StudentView;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    @JsonView(StudentView.StudentInfo.class)
    private Long id;

    @JsonView(StudentView.StudentInfo.class)
    private String username;

    @JsonView(StudentView.StudentInfo.class)
    private String fullName;

    @JsonView(StudentView.FullInfo.class)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CourseStudentResponse> courseStudentResponseList;
}
