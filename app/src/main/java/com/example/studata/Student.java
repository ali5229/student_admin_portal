
package com.example.studata;

public class Student {
    private String email;
    private String name;
    private String cgpa;
    private String roll;
    private String semester;
    private String imageUrl;

    public Student() {
    }

    public Student(String email, String name, String cgpa, String roll, String semester, String imageUrl) {
        this.email = email;
        this.name = name;
        this.cgpa = cgpa;
        this.roll = roll;
        this.semester = semester;
        this.imageUrl = imageUrl;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getCgpa() { return cgpa; }
    public String getRoll() { return roll; }
    public String getSemester() { return semester; }
    public String getImageUrl() { return imageUrl; }

    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setCgpa(String cgpa) { this.cgpa = cgpa; }
    public void setRoll(String roll) { this.roll = roll; }
    public void setSemester(String semester) { this.semester = semester; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
