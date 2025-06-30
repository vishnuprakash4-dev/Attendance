package com.example.attendance;

public class EmployeeDirectory {

    private String name;
    private String position;
    private String image;

    public String getEmp_ip() {
        return emp_ip;
    }

    public void setEmp_ip(String emp_ip) {
        this.emp_ip = emp_ip;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    private String emp_ip;
    private String email_id;

    private String emp_department;
    private String emp_phnno;
    private String emp_doj;

    public String getEmp_department() {
        return emp_department;
    }

    public void setEmp_department(String emp_department) {
        this.emp_department = emp_department;
    }

    public String getEmp_phnno() {
        return emp_phnno;
    }

    public void setEmp_phnno(String emp_phnno) {
        this.emp_phnno = emp_phnno;
    }

    public String getEmp_doj() {
        return emp_doj;
    }

    public void setEmp_doj(String emp_doj) {
        this.emp_doj = emp_doj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public EmployeeDirectory(String name, String position, String image, String emp_ip, String email_id, String emp_doj, String emp_department, String emp_phnno){
        this.name = name;
        this.image = image;
        this.position = position;
        this.emp_ip = emp_ip;
        this.email_id = email_id;
        this.emp_doj = emp_doj;
        this.emp_department = emp_department;
        this.emp_phnno = emp_phnno;
    }
}
