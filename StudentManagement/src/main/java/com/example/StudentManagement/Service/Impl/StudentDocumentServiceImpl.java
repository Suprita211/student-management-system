
        package com.example.StudentManagement.Service.Impl;

import com.example.StudentManagement.DTO.DocumentResponseDTO;
import com.example.StudentManagement.Entity.Student;
import com.example.StudentManagement.Entity.StudentDocument;
import com.example.StudentManagement.Repository.StudentDocumentRepository;
import com.example.StudentManagement.Repository.StudentRepository;
import com.example.StudentManagement.Service.StudentDocumentService;
import com.example.StudentManagement.enums.DocumentType;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentDocumentServiceImpl
        implements StudentDocumentService {

    private final StudentRepository studentRepository;
    private final StudentDocumentRepository studentDocumentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public DocumentResponseDTO uploadDocument(
            String studentId,
            MultipartFile file,
            DocumentType documentType,
            String documentName
    ) {

        try {

            // FIND STUDENT
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found"));

            // VALIDATE FILE
            String contentType = file.getContentType();

            if (documentType == DocumentType.PHOTO) {

                if (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")) {

                    throw new RuntimeException(
                            "PHOTO must be JPG or PNG"
                    );
                }

            } else {

                if (!contentType.equals("application/pdf")) {

                    throw new RuntimeException(
                            "Only PDF allowed for documents"
                    );
                }
            }

            // CREATE FOLDER
            String folderName = documentType == DocumentType.PHOTO
                    ? "photos"
                    : "documents";

            Path uploadPath = Paths.get(
                    uploadDir,
                    "students",
                    studentId,
                    folderName
            );

            Files.createDirectories(uploadPath);

            // GENERATE UNIQUE FILE NAME
            String originalFileName = file.getOriginalFilename();

            String extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            );

            String storedFileName =
                    UUID.randomUUID() + extension;

            // FINAL FILE PATH
            Path filePath = uploadPath.resolve(storedFileName);

            // SAVE FILE
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // SAVE DB
            StudentDocument document = StudentDocument.builder()
                    .student(student)
                    .documentName(documentName)
                    .documentType(documentType)
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .filePath(filePath.toString())
                    .fileType(contentType)
                    .fileSize(file.getSize())
                    .uploadedAt(LocalDateTime.now())
                    .build();

            StudentDocument savedDocument =
                    studentDocumentRepository.save(document);

            // RESPONSE DTO
            return DocumentResponseDTO.builder()
                    .documentId(savedDocument.getDocumentId())
                    .studentId(student.getStudentId())
                    .documentName(savedDocument.getDocumentName())
                    .documentType(savedDocument.getDocumentType())
                    .originalFileName(savedDocument.getOriginalFileName())
                    .storedFileName(savedDocument.getStoredFileName())
                    .filePath(savedDocument.getFilePath())
                    .fileType(savedDocument.getFileType())
                    .fileSize(savedDocument.getFileSize())
                    .uploadedAt(savedDocument.getUploadedAt())
                    .build();
        }

        catch (IOException e) {

            throw new RuntimeException(
                    "File upload failed: " + e.getMessage()
            );
        }
    }

    @Override
    public Resource viewDocument(Long documentId) {

        try {

            StudentDocument document =
                    studentDocumentRepository.findById(documentId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Document not found"
                                    ));

            // PHOTO RESTRICTION
            if (document.getDocumentType() == DocumentType.PHOTO) {

                throw new RuntimeException(
                        "Photos cannot be viewed"
                );
            }

            Path path = Paths.get(document.getFilePath());

            Resource resource = new UrlResource(
                    path.toUri()
            );

            if (!resource.exists()) {

                throw new RuntimeException(
                        "File not found"
                );
            }

            return resource;

        } catch (MalformedURLException e) {

            throw new RuntimeException(
                    "Error loading file"
            );
        }
    }

    @Override
    public Resource downloadDocument(Long documentId) {

        try {

            StudentDocument document =
                    studentDocumentRepository.findById(documentId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Document not found"
                                    ));

            // PHOTO RESTRICTION
            if (document.getDocumentType() == DocumentType.PHOTO) {

                throw new RuntimeException(
                        "Photos cannot be downloaded"
                );
            }

            Path path = Paths.get(document.getFilePath());

            Resource resource = new UrlResource(
                    path.toUri()
            );

            if (!resource.exists()) {

                throw new RuntimeException(
                        "File not found"
                );
            }

            return resource;

        } catch (MalformedURLException e) {

            throw new RuntimeException(
                    "Error downloading file"
            );
        }
    }
}

