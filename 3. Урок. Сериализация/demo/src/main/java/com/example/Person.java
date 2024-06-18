package com.example;

public class Person {

    private Department department;
    private long id;
    private int age;
    private String name;
    private boolean active;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Person(long id, int age, String name, boolean active, Department department) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.active = active;
        this.department = department;
    }

    @Override
    public String toString() {
        String result = "\n["
                + " Идентификатор - " + id
                + "; Возраст - " + age
                + "; Имя - " + name
                + "; Работает - " + (active ? "Да" : "Нет")
                + " ]\n";
        return result;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) + Integer.hashCode(age) + name.hashCode() + Boolean.hashCode(active)
                + department.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person person) {
            return this.id == person.id && this.age == person.age && this.name == person.name
                    && this.active == person.active;
        }
        return false;
    }
}
