package ds.part1.FamilyDoctor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String date;

    @NotBlank
    private String time;

    @NotBlank
    @Size(max = 50)
    private String place;

    @NotBlank
    private String customerName;

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    private String AMKA;

    @NotBlank
    private String doctorName;

    //1 to 5
    @NotBlank
    @Size(max = 1)
    private int evaluationGrade;

    public enum status {Set, Completed, Canceled, Changed};

    @NotBlank
    private String currentStatus;


    public Appointment() {

    }

    public Appointment(String date, String time, String place) {
        this.date = date;
        this.time = time;
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAMKA() {
        return AMKA;
    }

    public void setAMKA(String AMKA) {
        this.AMKA = AMKA;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getEvaluationGrade() {
        return evaluationGrade;
    }

    public void setEvaluationGrade(int evaluationGrade) {
        this.evaluationGrade = evaluationGrade;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

}