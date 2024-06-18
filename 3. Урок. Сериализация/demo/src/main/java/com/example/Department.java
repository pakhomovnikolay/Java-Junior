package com.example;

public class Department {

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        String result = "Идентификатор - " + id
                + "; Наименвоание - " + name;
        return result;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) + name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Department department) {
            return this.id == department.id && this.name == department.name;
        }
        return false;
    }
}
