package kz.danke.test.project.mapper;

import kz.danke.test.project.dto.StudentDTO;
import kz.danke.test.project.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CourseMapper.class}, imports = {Collectors.class, ArrayList.class})
public abstract class StudentMapper {

    @Autowired
    protected CourseMapper courseMapper;

    public abstract StudentDTO toStudentDTO(Student student);

    @Mappings({
            @Mapping(target = "courseStudentResponseList", expression = "java(student.getCourses() != null ? student.getCourses().stream().map(courseMapper::toCourseStudentResponse).collect(Collectors.toList()) : new ArrayList())")
    })
    public abstract StudentDTO toFullStudentDTO(Student student);
}
