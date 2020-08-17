package kz.danke.test.project.dto;

import com.fasterxml.jackson.annotation.JsonView;
import kz.danke.test.project.dto.response.CourseStudentResponse;
import kz.danke.test.project.json.view.UserView;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonView(UserView.UserInfo.class)
    private Long id;

    @JsonView(UserView.UserInfo.class)
    private String username;

    @JsonView(UserView.UserInfo.class)
    private String fullName;

    @JsonView(UserView.FullInfo.class)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CourseStudentResponse> courseStudentResponseList;
}
