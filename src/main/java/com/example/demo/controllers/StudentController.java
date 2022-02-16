package com.example.demo.controllers;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Response;
import com.example.demo.domain.Student;
import com.example.demo.services.StudentService;

@RestController
@RequestMapping("students")
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("insert")
    public void insertar(){
        System.err.println("en insertar");
        studentService.addStudent(new Student(7,"Manolito"));
        System.out.println(studentService.getStudents());

    }

    @GetMapping("list")
    public ResponseEntity<Response> getStudents(){
    	Response response = new Response();
    	response.setTimeStamp(LocalDateTime.now());
        response.setStatusCode(1);
        response.setStatus(OK);
        response.setReason("");
        response.setMessage(" students recuperados");
        response.setDeveloperMessage("");
        response.setData(Map.of("students",studentService.getStudents()));
        ResponseEntity ok= ResponseEntity.ok(response);                
        return ok;
    }

    @GetMapping("student/{id}")
    public Student getStudentById(@PathVariable("id") int id){
        return  this.studentService.findStudentById(id);
    }

    @PostMapping("add")
    public void addStudent(@RequestBody Student student){
        System.err.println("estoy aqui para poner nuevos students");
        this.studentService.addStudent(student);
    }
}
