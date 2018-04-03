package com.kaishengit.mapper;

import com.kaishengit.entity.Student;

import java.util.List;

public interface StudentMapperBak {

    Student findById(int id);
    List<Student> findAll();
    Student findByIdWithTagAndSchool(int id);

}
