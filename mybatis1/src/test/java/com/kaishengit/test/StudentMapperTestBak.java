package com.kaishengit.test;

import com.kaishengit.entity.Student;
import com.kaishengit.entity.Tag;
import com.kaishengit.mapper.StudentMapper;
import com.kaishengit.mapper.StudentMapperBak;
import com.kaishengit.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class StudentMapperTestBak {

    private SqlSession sqlSession;
    private StudentMapperBak studentMapperBak;
    @Before
    public void init() {
        // 自动提交事务
        sqlSession = SqlSessionFactoryUtil.getSqlSession(true);
        // 动态代理 动态生成
        studentMapperBak = sqlSession.getMapper(StudentMapperBak.class);
    }

    @After
    public void destroy() {
        // 5.关闭sqlSession
        sqlSession.close();
    }

    @Test
    public void testFindById() {
        Student student = studentMapperBak.findById(1);
        System.out.println(student);
    }

    @Test
    public void testFindByIdWithTagAndSchool() {

        Student student = studentMapperBak.findByIdWithTagAndSchool(1);
        System.out.println(student);
        List<Tag> tagList = student.getTagList();
        for(Tag tag : tagList) {
            System.out.println(tag);
        }


    }

    @Test
    public void testFindAll() {
        List<Student> studentList = studentMapperBak.findAll();
        for(Student stu : studentList) {
            System.out.println(stu);
        }
    }
}
