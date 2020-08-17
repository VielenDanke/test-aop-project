package kz.danke.test.project;

import kz.danke.test.project.model.Course;
import kz.danke.test.project.model.Student;
import kz.danke.test.project.repository.CourseRepository;
import kz.danke.test.project.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy
public class TestProjectApplication {

	private final CourseRepository courseRepository;
	private final StudentRepository studentRepository;

	@Autowired
	public TestProjectApplication(CourseRepository courseRepository,
								  StudentRepository studentRepository) {
		this.courseRepository = courseRepository;
		this.studentRepository = studentRepository;
	}

	@Bean
	public CommandLineRunner runner() {
		return args -> {
			List<Course> courses = Arrays.asList(
					new Course("first course", "first course"),
					new Course("second course", "second course"),
					new Course("third course", "third course")
			);
			List<Student> students = Arrays.asList(
					new Student("first student", "first student"),
					new Student("second student", "second student"),
					new Student("third student", "third student")
			);
			courseRepository.saveAll(courses);
			studentRepository.saveAll(students);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(TestProjectApplication.class, args);
	}

}
