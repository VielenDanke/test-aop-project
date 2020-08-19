package kz.danke.test.project.repository;

import kz.danke.test.project.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"courses"})
    @Query("select s from User s")
    List<User> findAllWithCourse();

    @EntityGraph(attributePaths = {"courses"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select s from User s where s.id=?1")
    Optional<User> findByIdWithCourses(Long id);

    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findByUsername(String username);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM student_course WHERE student_id=?1 AND course_id=?2")
    void deleteCourseFromUser(Long studentId, Long courseId);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO student_course(student_id, course_id) values (?1, ?2)")
    void insertUserIdAndCourseId(Long userId, Long courseId);
}
