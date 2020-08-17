package kz.danke.test.project.mapper;

import kz.danke.test.project.dto.response.CourseStudentResponse;
import kz.danke.test.project.model.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {

    public abstract CourseStudentResponse toCourseStudentResponse(Course course);
}
