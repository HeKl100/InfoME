package model;

public class Employee
{
    private int id;
    private String name;
    private String surname;
    private String email;
    private String department;
    private String category;
    private String state;

    public Employee(int id, String name, String surname, String email, String department, String category, String state) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.department = department;
        this.category = category;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
