package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.domain.Student;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class StudentService {
    private List<Student> students = new ArrayList<Student>();


    //no debemos tocar el constructor
    @PostConstruct
    public void populateStudents() {
        students.add(new Student(1, "Felipe"));
        students.add(new Student(2, "Arturo"));
    }

    public List<Student> getStudents() {
        return students;
    }

    public boolean addStudent(Student student) {
        return students.add(student);
    }

    public Student findStudentById(int id) {
        return students.stream()
                .filter((student) -> student.getId() == id)
                .findFirst()
                .get();
    }

    public boolean deleteStudentById(int id){
        AtomicBoolean resultado = new AtomicBoolean(false);
         students.stream()
                .filter((student) -> student.getId() == id)
                .findFirst().ifPresent((student)->{
                    resultado.set(students.remove(student));
                 });
         return resultado.get();
    }

    public boolean updateStudent(Student student){
        Student studentById = findStudentById(student.getId());
        if(studentById!=null) {
            studentById.update(student);
            return true;
        }
        return false;
    }
}
