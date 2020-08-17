package kz.danke.test.project.repository;

import kz.danke.test.project.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"courses"})
    @Query("select s from User s")
    List<User> findAllWithCourse();

    @EntityGraph(attributePaths = {"courses"})
    @Query("select s from User s where s.id=?1")
    Optional<User> findByIdWithCourses(Long id);

    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findByUsername(String username);
}
