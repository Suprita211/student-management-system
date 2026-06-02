
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

            boolean documentExists =
                    studentDocumentRepository
                            .findByStudentStudentIdAndDocumentType(
                                    studentId,
                                    documentType
                            )
                            .isPresent();

            if (documentExists) {

                throw new RuntimeException(
                        documentType +
                                " already uploaded. Use update API."
                );
            }

            // VALIDATE FILE
            String contentType = file.getContentType();
            if (contentType == null) {
                throw new RuntimeException(
                        "Invalid file type"
                );
            }
            // EMPTY FILE CHECK
            if (file.isEmpty()) {
                throw new RuntimeException(
                        "File cannot be empty"
                );
            }

// SIZE VALIDATION
            if (documentType == DocumentType.PHOTO
                    && file.getSize() > 2 * 1024 * 1024) {

                throw new RuntimeException(
                        "Photo size cannot exceed 2 MB"
                );
            }

            if (documentType != DocumentType.PHOTO
                    && file.getSize() > 10 * 1024 * 1024) {

                throw new RuntimeException(
                        "Document size cannot exceed 10 MB"
                );
            }

            if (documentType == DocumentType.PHOTO) {

                if (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")) {

                    throw new RuntimeException(
                            "PHOTO must be JPG or PNG"
                    );
                }

            } else {

                if (!contentType.equals("application/pdf")
                        && !contentType.equals(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                )) {

                    throw new RuntimeException(
                            "Only PDF or DOCX allowed"
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
            if (originalFileName == null
                    || !originalFileName.contains(".")) {

                throw new RuntimeException(
                        "Invalid file name"
                );
            }

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


    @Override
    public DocumentResponseDTO updateDocument(
            Long documentId,
            MultipartFile file,
            DocumentType documentType,
            String documentName
    ) {

        try {

            StudentDocument existingDocument =
                    studentDocumentRepository.findById(documentId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Document not found"
                                    ));

            // DOCUMENT TYPE CANNOT BE CHANGED
            if (existingDocument.getDocumentType() != documentType) {

                throw new RuntimeException(
                        "Document type cannot be changed. Use upload API for a new document."
                );
            }

            String contentType = file.getContentType();

            if (contentType == null) {
                throw new RuntimeException(
                        "Invalid file type"
                );
            }

            if (file.isEmpty()) {
                throw new RuntimeException(
                        "File cannot be empty"
                );
            }

            // SIZE VALIDATION
            if (documentType == DocumentType.PHOTO
                    && file.getSize() > 2 * 1024 * 1024) {

                throw new RuntimeException(
                        "Photo size cannot exceed 2 MB"
                );
            }

            if (documentType != DocumentType.PHOTO
                    && file.getSize() > 10 * 1024 * 1024) {

                throw new RuntimeException(
                        "Document size cannot exceed 10 MB"
                );
            }

            // FILE TYPE VALIDATION
            if (documentType == DocumentType.PHOTO) {

                if (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")) {

                    throw new RuntimeException(
                            "PHOTO must be JPG or PNG"
                    );
                }

            } else {

                if (!contentType.equals("application/pdf")
                        && !contentType.equals(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                )) {

                    throw new RuntimeException(
                            "Only PDF or DOCX allowed"
                    );
                }
            }

            // DELETE OLD FILE
            Path oldFilePath =
                    Paths.get(existingDocument.getFilePath());

            Files.deleteIfExists(oldFilePath);

            String folderName =
                    documentType == DocumentType.PHOTO
                            ? "photos"
                            : "documents";

            String studentId =
                    existingDocument.getStudent()
                            .getStudentId();

            Path uploadPath = Paths.get(
                    uploadDir,
                    "students",
                    studentId,
                    folderName
            );

            Files.createDirectories(uploadPath);

            String originalFileName =
                    file.getOriginalFilename();

            if (originalFileName == null
                    || !originalFileName.contains(".")) {

                throw new RuntimeException(
                        "Invalid file name"
                );
            }

            String extension =
                    originalFileName.substring(
                            originalFileName.lastIndexOf(".")
                    );

            String storedFileName =
                    UUID.randomUUID() + extension;

            Path filePath =
                    uploadPath.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            existingDocument.setDocumentName(documentName);
            existingDocument.setOriginalFileName(originalFileName);
            existingDocument.setStoredFileName(storedFileName);
            existingDocument.setFilePath(filePath.toString());
            existingDocument.setFileType(contentType);
            existingDocument.setFileSize(file.getSize());

            StudentDocument savedDocument =
                    studentDocumentRepository.save(existingDocument);

            return DocumentResponseDTO.builder()
                    .documentId(savedDocument.getDocumentId())
                    .studentId(savedDocument.getStudent().getStudentId())
                    .documentName(savedDocument.getDocumentName())
                    .documentType(savedDocument.getDocumentType())
                    .originalFileName(savedDocument.getOriginalFileName())
                    .storedFileName(savedDocument.getStoredFileName())
                    .filePath(savedDocument.getFilePath())
                    .fileType(savedDocument.getFileType())
                    .fileSize(savedDocument.getFileSize())
                    .uploadedAt(savedDocument.getUploadedAt())
                    .build();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Document update failed: "
                            + e.getMessage()
            );
        }
    }
    @Override
    public void deleteDocument(Long documentId) {

        try {

            StudentDocument document =
                    studentDocumentRepository.findById(documentId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Document not found"
                                    ));

            Path filePath =
                    Paths.get(document.getFilePath());

            Files.deleteIfExists(filePath);

            studentDocumentRepository.delete(document);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Document delete failed: "
                            + e.getMessage()
            );
        }
    }

}

