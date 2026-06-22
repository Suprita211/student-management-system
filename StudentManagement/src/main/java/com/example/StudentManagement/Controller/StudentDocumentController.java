package com.example.StudentManagement.Controller;

import com.example.StudentManagement.DTO.DocumentResponseDTO;
import com.example.StudentManagement.Entity.StudentDocument;
import com.example.StudentManagement.Service.StudentDocumentService;
import com.example.StudentManagement.enums.DocumentType;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/students/documents")
@RequiredArgsConstructor
public class StudentDocumentController {

    private final StudentDocumentService studentDocumentService;

    // UPLOAD DOCUMENT
    @PostMapping("/{studentId}")
    public DocumentResponseDTO uploadDocument(

            @PathVariable String studentId,

            @RequestParam("file")
            MultipartFile file,

            @RequestParam("documentType")
            DocumentType documentType,

            @RequestParam("documentName")
            String documentName
    ) {

        return studentDocumentService.uploadDocument(
                studentId,
                file,
                documentType,
                documentName
        );
    }

    // VIEW PDF
    @GetMapping("/{documentId}/view")
    public ResponseEntity<Resource> viewDocument(
            @PathVariable Long documentId
    ) {

        StudentDocument document =
                studentDocumentService.getDocument(documentId);

        Resource resource =
                studentDocumentService.viewDocument(documentId);

        MediaType mediaType;

        if (document.getFileType().equals("image/png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (document.getFileType().equals("image/jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else {
            mediaType = MediaType.APPLICATION_PDF;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                document.getOriginalFileName() + "\""
                )
                .body(resource);
    }
    // DOWNLOAD PDF
    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long documentId
    ) {

        StudentDocument document =
                studentDocumentService.getDocument(documentId);

        Resource resource =
                studentDocumentService.downloadDocument(documentId);

        MediaType mediaType;

        if (document.getFileType().equals("image/png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (document.getFileType().equals("image/jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else {
            mediaType = MediaType.APPLICATION_PDF;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                document.getOriginalFileName() + "\""
                )
                .body(resource);
    }
    @PutMapping("/{documentId}")
    public DocumentResponseDTO updateDocument(

            @PathVariable Long documentId,

            @RequestParam("file")
            MultipartFile file,

            @RequestParam("documentType")
            DocumentType documentType,

            @RequestParam("documentName")
            String documentName
    ) {

        return studentDocumentService.updateDocument(
                documentId,
                file,
                documentType,
                documentName
        );
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<String> deleteDocument(
            @PathVariable Long documentId
    ) {

        studentDocumentService.deleteDocument(
                documentId
        );

        return ResponseEntity.ok(
                "Document deleted successfully"
        );
    }
}