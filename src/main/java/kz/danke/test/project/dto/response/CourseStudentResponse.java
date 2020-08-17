package kz.danke.test.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudentResponse {

    private Long id;
    private String name;
    private String description;
}
