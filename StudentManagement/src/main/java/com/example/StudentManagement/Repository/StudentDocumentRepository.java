package com.example.StudentManagement.Repository;


import com.example.StudentManagement.Entity.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDocumentRepository extends JpaRepository<StudentDocument, Long> {
}