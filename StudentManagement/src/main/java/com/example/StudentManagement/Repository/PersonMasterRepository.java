package com.example.StudentManagement.Repository;

import com.example.StudentManagement.Entity.PersonMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PersonMasterRepository extends JpaRepository<PersonMaster, Long> {

    Optional<PersonMaster> findByAadhaarNo(String aadhaarNo);
}