package kz.danke.test.project;

import kz.danke.test.project.model.Course;
import kz.danke.test.project.model.Role;
import kz.danke.test.project.model.User;
import kz.danke.test.project.repository.CourseRepository;
import kz.danke.test.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy
public class TestProjectApplication {

	private final CourseRepository courseRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public TestProjectApplication(CourseRepository courseRepository,
								  UserRepository userRepository,
								  PasswordEncoder passwordEncoder) {
		this.courseRepository = courseRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public CommandLineRunner runner() {
		return args -> {
			List<Course> courses = Arrays.asList(
					new Course("first course", "first course"),
					new Course("second course", "second course"),
					new Course("third course", "third course")
			);
			User first = new User("first", passwordEncoder.encode("first"), "first student");
			User second = new User("second", passwordEncoder.encode("second"), "second student");
			User third = new User("third", passwordEncoder.encode("third"), "third student");

			first.setAuthorities(Collections.singleton(Role.ROLE_USER));
			second.setAuthorities(Collections.singleton(Role.ROLE_USER));
			third.setAuthorities(Collections.singleton(Role.ROLE_ADMIN));

			List<User> users = Arrays.asList(
					first,
					second,
					third
			);
			courseRepository.saveAll(courses);
			userRepository.saveAll(users);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(TestProjectApplication.class, args);
	}

}
