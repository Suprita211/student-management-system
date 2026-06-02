package com.example.StudentManagement.Repository;
import com.example.StudentManagement.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    boolean existsByPersonPersonIdAndCourseName(
            Long personId,
            String courseName
    );

    @Query("""
       SELECT COUNT(DISTINCT s.courseName)
       FROM Student s
       """)
    long countDistinctCourses();
}