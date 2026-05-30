package com.example.StudentManagement.Repository;


import com.example.StudentManagement.Entity.StudentDocument;
import com.example.StudentManagement.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentDocumentRepository extends JpaRepository<StudentDocument, Long> {
    List<StudentDocument>
    findByStudentStudentId(String studentId);
    Optional<StudentDocument>
    findByStudentStudentIdAndDocumentType(
            String studentId,
            DocumentType documentType
    );
}