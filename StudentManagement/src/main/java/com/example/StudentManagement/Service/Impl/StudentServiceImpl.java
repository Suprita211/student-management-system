package com.example.StudentManagement.Service.Impl;


import com.example.StudentManagement.DTO.*;
import com.example.StudentManagement.Entity.PersonMaster;
import com.example.StudentManagement.Entity.Student;
import com.example.StudentManagement.Entity.StudentDocument;
import com.example.StudentManagement.Repository.PersonMasterRepository;
import com.example.StudentManagement.Repository.StudentDocumentRepository;
import com.example.StudentManagement.Repository.StudentRepository;
import com.example.StudentManagement.Service.StudentService;
import com.example.StudentManagement.enums.DocumentType;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class StudentServiceImpl implements StudentService {

    private final PersonMasterRepository personMasterRepository;
    private final StudentRepository studentRepository;
    private final StudentDocumentRepository studentDocumentRepository;

    // CREATE STUDENT



    @Transactional
    @Override
    public StudentResponseDTO createStudentAdmission(
            StudentCreateRequestDTO request
    ) {

        // CHECK STUDENT ID ALREADY EXISTS

        if (studentRepository.existsById(
                request.getStudentId())) {

            throw new RuntimeException(
                    "Student ID already exists"
            );
        }

        // CHECK AADHAAR EXISTS OR NOT

        Optional<PersonMaster> existingPerson =
                personMasterRepository.findByAadhaarNo(
                        request.getAadhaarNo()
                );

        PersonMaster person;

        if (existingPerson.isPresent()) {

            person = existingPerson.get();

            // PREVENT SAME COURSE ADMISSION

            if (studentRepository
                    .existsByPersonPersonIdAndCourseName(
                            person.getPersonId(),
                            request.getCourseName()
                    )) {

                throw new RuntimeException(
                        "Student already enrolled in this course"
                );
            }

        } else {

            // CREATE NEW PERSON

            person = PersonMaster.builder()
                    .aadhaarNo(request.getAadhaarNo())
                    .fullName(request.getFullName())
                    .fatherName(request.getFatherName())
                    .motherName(request.getMotherName())
                    .dateOfBirth(request.getDateOfBirth())
                    .primaryContact(request.getPrimaryContact())
                    .secondaryContact(request.getSecondaryContact())
                    .email(request.getEmail())
                    .presentAddress(request.getPresentAddress())
                    .permanentAddress(request.getPermanentAddress())
                    .createdAt(LocalDateTime.now())
                    .build();

            person = personMasterRepository.save(person);
        }

        // CREATE STUDENT ADMISSION

        Student student = Student.builder()
                .studentId(request.getStudentId())
                .person(person)
                .courseName(request.getCourseName())
                .courseType(request.getCourseType())
                .universityRegistrationNo(
                        request.getUniversityRegistrationNo()
                )
                .dateOfAdmission(request.getDateOfAdmission())
                .counsellorName(request.getCounsellorName())
                .session(request.getSession())
                .duration(request.getDuration())
                .createdAt(LocalDateTime.now())
                .build();

        student = studentRepository.save(student);

        return mapToResponse(student);
    }

    // UPDATE STUDENT

    @Override
    public StudentResponseDTO updateStudentAdmission(
            String studentId,
            StudentUpdateRequestDTO request
    ) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        PersonMaster person = student.getPerson();

        // UPDATE PERSON_MASTER

        if (request.getFullName() != null) {
            person.setFullName(request.getFullName());
        }

        if (request.getFatherName() != null) {
            person.setFatherName(request.getFatherName());
        }

        if (request.getMotherName() != null) {
            person.setMotherName(request.getMotherName());
        }

        if (request.getDateOfBirth() != null) {
            person.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getPrimaryContact() != null) {
            person.setPrimaryContact(request.getPrimaryContact());
        }

        if (request.getSecondaryContact() != null) {
            person.setSecondaryContact(request.getSecondaryContact());
        }

        if (request.getEmail() != null) {
            person.setEmail(request.getEmail());
        }

        if (request.getPresentAddress() != null) {
            person.setPresentAddress(request.getPresentAddress());
        }

        if (request.getPermanentAddress() != null) {
            person.setPermanentAddress(request.getPermanentAddress());
        }

        personMasterRepository.save(person);

        // UPDATE STUDENT

        if (request.getCourseName() != null) {
            student.setCourseName(request.getCourseName());
        }

        if (request.getCourseType() != null) {
            student.setCourseType(request.getCourseType());
        }

        if (request.getUniversityRegistrationNo() != null) {
            student.setUniversityRegistrationNo(
                    request.getUniversityRegistrationNo()
            );
        }

        if (request.getDateOfAdmission() != null) {
            student.setDateOfAdmission(
                    request.getDateOfAdmission()
            );
        }

        if (request.getCounsellorName() != null) {
            student.setCounsellorName(
                    request.getCounsellorName()
            );
        }

        if (request.getSession() != null) {
            student.setSession(request.getSession());
        }

        if (request.getDuration() != null) {
            student.setDuration(request.getDuration());
        }

        student = studentRepository.save(student);

        return mapToResponse(student);
    }

    // GET SINGLE STUDENT

    @Override
    public StudentResponseDTO getStudentById(
            String studentId
    ) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return mapToResponse(student);
    }

    // GET ALL STUDENTS WITH PAGINATION

    @Override
    public Page<StudentResponseDTO> getAllStudents(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Student> studentPage =
                studentRepository.findAll(pageable);

        return studentPage.map(this::mapToResponse);
    }

    // DTO RESPONSE MAPPER

    private StudentResponseDTO mapToResponse(
            Student student
    ) {

        PersonMaster person = student.getPerson();

        return StudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .personId(person.getPersonId())
                .aadhaarNo(person.getAadhaarNo())
                .fullName(person.getFullName())
                .fatherName(person.getFatherName())
                .motherName(person.getMotherName())
                .dateOfBirth(person.getDateOfBirth())
                .primaryContact(person.getPrimaryContact())
                .secondaryContact(person.getSecondaryContact())
                .email(person.getEmail())
                .presentAddress(person.getPresentAddress())
                .permanentAddress(person.getPermanentAddress())
                .courseName(student.getCourseName())
                .courseType(student.getCourseType())
                .universityRegistrationNo(
                        student.getUniversityRegistrationNo()
                )
                .dateOfAdmission(student.getDateOfAdmission())
                .counsellorName(student.getCounsellorName())
                .session(student.getSession())
                .duration(student.getDuration())
                .build();
    }

    @Override
    public byte[] generateStudentPdf(String studentId) {

        try {

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found"));

            PersonMaster person = student.getPerson();

            List<StudentDocument> documents =
                    studentDocumentRepository
                            .findByStudentStudentId(studentId);

            Optional<StudentDocument> photoDocument =
                    studentDocumentRepository
                            .findByStudentStudentIdAndDocumentType(
                                    studentId,
                                    DocumentType.PHOTO
                            );

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            Document pdf = new Document(PageSize.A4);

            PdfWriter.getInstance(pdf, outputStream);

            pdf.open();

            // LOGO
            try {

                InputStream logoStream =
                        getClass()
                                .getResourceAsStream(
                                        "/static/images/NimttLogo.jpg"
                                );

                if (logoStream != null) {

                    Image logo =
                            Image.getInstance(
                                    logoStream.readAllBytes()
                            );

                    logo.scaleToFit(80, 80);
                    logo.setAlignment(Element.ALIGN_CENTER);

                    pdf.add(logo);
                }

            } catch (Exception ignored) {
            }

            Paragraph heading =
                    new Paragraph(
                            "NIMTT STUDENT ADMISSION REPORT",
                            FontFactory.getFont(
                                    FontFactory.HELVETICA_BOLD,
                                    16
                            )
                    );

            heading.setAlignment(Element.ALIGN_CENTER);

            pdf.add(heading);

            pdf.add(new Paragraph(" "));

            // PHOTO
            if (photoDocument.isPresent()) {

                try {

                    Image studentPhoto =
                            Image.getInstance(
                                    photoDocument.get().getFilePath()
                            );

                    studentPhoto.scaleToFit(120, 120);
                    studentPhoto.setAlignment(
                            Element.ALIGN_CENTER
                    );

                    pdf.add(studentPhoto);

                    pdf.add(new Paragraph(" "));

                } catch (Exception ignored) {
                }
            }

            // PERSONAL DETAILS TABLE

            PdfPTable personalTable =
                    new PdfPTable(2);

            personalTable.setWidthPercentage(100);

            personalTable.addCell("Student ID");
            personalTable.addCell(student.getStudentId());

            personalTable.addCell("Full Name");
            personalTable.addCell(person.getFullName());

            personalTable.addCell("Father Name");
            personalTable.addCell(person.getFatherName());

            personalTable.addCell("Mother Name");
            personalTable.addCell(person.getMotherName());

            personalTable.addCell("Aadhaar");
            personalTable.addCell(person.getAadhaarNo());

            personalTable.addCell("Primary Contact");
            personalTable.addCell(person.getPrimaryContact());

            personalTable.addCell("Email");
            personalTable.addCell(person.getEmail());

            pdf.add(personalTable);

            pdf.add(new Paragraph(" "));

            // COURSE DETAILS

            PdfPTable courseTable =
                    new PdfPTable(2);

            courseTable.setWidthPercentage(100);

            courseTable.addCell("Course Name");
            courseTable.addCell(student.getCourseName());

            courseTable.addCell("Course Type");
            courseTable.addCell(student.getCourseType());

            courseTable.addCell("University Reg No");
            courseTable.addCell(
                    student.getUniversityRegistrationNo()
            );

            courseTable.addCell("Admission Date");
            courseTable.addCell(
                    String.valueOf(
                            student.getDateOfAdmission()
                    )
            );

            courseTable.addCell("Counsellor");
            courseTable.addCell(
                    student.getCounsellorName()
            );

            courseTable.addCell("Session");
            courseTable.addCell(student.getSession());

            courseTable.addCell("Duration");
            courseTable.addCell(student.getDuration());

            pdf.add(courseTable);

            pdf.add(new Paragraph(" "));

            // DOCUMENTS TABLE

            PdfPTable documentTable =
                    new PdfPTable(3);

            documentTable.setWidthPercentage(100);

            documentTable.addCell("Document Type");
            documentTable.addCell("Document Name");
            documentTable.addCell("Uploaded At");

            for (StudentDocument document : documents) {

                documentTable.addCell(
                        document.getDocumentType().name()
                );

                documentTable.addCell(
                        document.getDocumentName()
                );

                documentTable.addCell(
                        String.valueOf(
                                document.getUploadedAt()
                        )
                );
            }

            pdf.add(documentTable);

            pdf.add(new Paragraph(" "));

            pdf.add(
                    new Paragraph(
                            "Generated On : "
                                    + LocalDateTime.now()
                    )
            );

            pdf.add(new Paragraph(" "));
            pdf.add(new Paragraph("Admin Signature"));

            pdf.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "PDF generation failed : "
                            + e.getMessage()
            );
        }
    }

    @Override
    public StudentSearchResponseDTO searchByStudentId(
            String studentId
    ) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        List<DocumentResponseDTO> documents =
                student.getDocuments()
                        .stream()
                        .map(doc -> DocumentResponseDTO.builder()
                                .documentId(doc.getDocumentId())
                                .studentId(student.getStudentId())
                                .documentName(doc.getDocumentName())
                                .documentType(doc.getDocumentType())
                                .originalFileName(doc.getOriginalFileName())
                                .storedFileName(doc.getStoredFileName())
                                .filePath(doc.getFilePath())
                                .fileType(doc.getFileType())
                                .fileSize(doc.getFileSize())
                                .uploadedAt(doc.getUploadedAt())
                                .build())
                        .toList();

        return StudentSearchResponseDTO.builder()
                .studentId(student.getStudentId())
                .fullName(student.getPerson().getFullName())
                .aadhaarNo(student.getPerson().getAadhaarNo())
                .courseName(student.getCourseName())
                .courseType(student.getCourseType())
                .session(student.getSession())
                .duration(student.getDuration())
                .documents(documents)
                .build();
    }
}