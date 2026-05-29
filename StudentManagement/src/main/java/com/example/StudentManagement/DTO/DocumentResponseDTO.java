package com.example.StudentManagement.DTO;



import com.example.StudentManagement.enums.DocumentType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponseDTO {

    private Long documentId;

    private String studentId;

    private String documentName;

    private DocumentType documentType;

    private String originalFileName;

    private String storedFileName;

    private String fileType;

    private Long fileSize;

    private String filePath;

    private LocalDateTime uploadedAt;
}