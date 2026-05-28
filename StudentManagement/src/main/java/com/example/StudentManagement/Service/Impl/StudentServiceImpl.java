package com.example.StudentManagement.Service.Impl;


import com.example.StudentManagement.DTO.StudentCreateRequestDTO;
import com.example.StudentManagement.DTO.StudentResponseDTO;
import com.example.StudentManagement.DTO.StudentUpdateRequestDTO;
import com.example.StudentManagement.Entity.PersonMaster;
import com.example.StudentManagement.Entity.Student;
import com.example.StudentManagement.Repository.PersonMasterRepository;
import com.example.StudentManagement.Repository.StudentRepository;
import com.example.StudentManagement.Service.StudentService;


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

    // CREATE STUDENT

    @Override
    public StudentResponseDTO createStudentAdmission(
            StudentCreateRequestDTO request
    ) {

        // CHECK AADHAAR EXISTS OR NOT

        Optional<PersonMaster> existingPerson =
                personMasterRepository.findByAadhaarNo(
                        request.getAadhaarNo()
                );

        PersonMaster person;

        // IF PERSON EXISTS

        if (existingPerson.isPresent()) {

            person = existingPerson.get();

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
}