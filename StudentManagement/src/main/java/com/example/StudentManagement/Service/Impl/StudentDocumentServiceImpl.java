package com.example.StudentManagement.Service.Impl;





import com.example.StudentManagement.Entity.StudentDocument;
import com.example.StudentManagement.Repository.StudentDocumentRepository;
import com.example.StudentManagement.Service.StudentDocumentService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentDocumentServiceImpl implements StudentDocumentService {

    private final StudentDocumentRepository studentDocumentRepository;

    @Override
    public StudentDocument saveDocument(StudentDocument document) {

        document.setUploadedAt(LocalDateTime.now());

        return studentDocumentRepository.save(document);
    }
}