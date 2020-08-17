package kz.danke.test.project.mapper;

import kz.danke.test.project.dto.UserDTO;
import kz.danke.test.project.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CourseMapper.class}, imports = {Collectors.class, ArrayList.class})
public abstract class UserMapper {

    @Autowired
    protected CourseMapper courseMapper;

    public abstract UserDTO toStudentDTO(User user);

    @Mappings({
            @Mapping(target = "courseStudentResponseList", expression = "java(user.getCourses() != null ? user.getCourses().stream().map(courseMapper::toCourseStudentResponse).collect(Collectors.toList()) : new ArrayList())")
    })
    public abstract UserDTO toFullStudentDTO(User user);
}
