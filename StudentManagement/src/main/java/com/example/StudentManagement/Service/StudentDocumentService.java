package com.example.StudentManagement.Service;


import com.example.StudentManagement.DTO.DocumentResponseDTO;

import com.example.StudentManagement.enums.DocumentType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface StudentDocumentService {

    public DocumentResponseDTO uploadDocument(
            String studentId,
            MultipartFile file,
            DocumentType documentType,
            String documentName
    );
    Resource viewDocument(Long documentId);

    Resource downloadDocument(Long documentId);

    DocumentResponseDTO updateDocument(
            Long documentId,
            MultipartFile file,
            DocumentType documentType,
            String documentName
    );
    void deleteDocument(Long documentId);

    }
