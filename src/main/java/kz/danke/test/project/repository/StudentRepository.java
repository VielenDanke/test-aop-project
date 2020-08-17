package kz.danke.test.project.repository;

import kz.danke.test.project.model.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @EntityGraph(attributePaths = {"courses"})
    @Query("select s from Student s")
    List<Student> findAllWithCourse();

    @EntityGraph(attributePaths = {"courses"})
    @Query("select s from Student s where s.id=?1")
    Optional<Student> findByIdWithCourses(Long id);
}
