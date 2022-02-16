package com.example.demo.domain;

public class Student {
    private final Integer id;
    private  String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void update(Student student) {
        this.name=student.getName();
    }
}
