package com.karbaros.transvisionassignment;

/**
 * Created by shanu on 26-Apr-17.
 */

public class Employee {
    private int id;
    private String name;
    private String address;
    private int age;
    private long phone;
    private String gender;
    private String grade;
    private String place;
    private String dob;


    public Employee() {
    }

    public Employee(int id, String name, String address, int age, long phone, String gender, String grade, String place, String dob) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.phone = phone;
        this.gender = gender;
        this.grade = grade;
        this.place = place;
        this.dob = dob;
    }

    public Employee(String name, String address, int age, long phone, String gender, String grade, String place, String dob) {
        super();
        this.name = name;
        this.address = address;
        this.age = age;
        this.phone = phone;
        this.gender = gender;
        this.grade = grade;
        this.place = place;
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
