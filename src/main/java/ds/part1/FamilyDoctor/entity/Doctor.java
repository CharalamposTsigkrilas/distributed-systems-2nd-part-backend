package ds.part1.FamilyDoctor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

@Entity
public class Doctor extends User{

    @NotBlank
    @Size(max = 50)
    private String specialty;

    @NotBlank
    @Size(max = 50)
    private String doctorOfficeAddress;

    // 0.0-5.0
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private float rating;

    private int appointmentsCompleted;

    private int maxNumberOfCitizens;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_citizens")
    private List<Citizen> citizens;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_appointments")
    private List<Appointment> appointments;

    public Doctor(String fullName, String username, String password, String email, String phoneNumber,
                  String department, String prefecture, String specialty, String doctorOfficeAddress) {
        super(fullName, username, password, email, phoneNumber, department, prefecture);

        this.specialty = specialty;
        this.doctorOfficeAddress = doctorOfficeAddress;
    }

    public Doctor() {

    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public String getFullName() {
        return super.getFullName();
    }

    @Override
    public void setFullName(String fullName) {
        super.setFullName(fullName);
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        super.setPhoneNumber(phoneNumber);
    }

    @Override
    public Set<Role> getRoles() {
        return super.getRoles();
    }

    @Override
    public void setRoles(Set<Role> roles) {
        super.setRoles(roles);
    }

    @Override
    public String getDepartment() {
        return super.getDepartment();
    }

    @Override
    public void setDepartment(String department) {
        super.setDepartment(department);
    }

    @Override
    public String getPrefecture() {
        return super.getPrefecture();
    }

    @Override
    public void setPrefecture(String prefecture) {
        super.setPrefecture(prefecture);
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDoctorOfficeAddress() {
        return doctorOfficeAddress;
    }

    public void setDoctorOfficeAddress(String doctorOfficeAddress) {
        this.doctorOfficeAddress = doctorOfficeAddress;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getAppointmentsCompleted() {
        return appointmentsCompleted;
    }

    public void setAppointmentsCompleted(int appointmentsCompleted) {
        this.appointmentsCompleted = appointmentsCompleted;
    }

    public int getMaxNumberOfCitizens() {
        return maxNumberOfCitizens;
    }

    public void setMaxNumberOfCitizens(int maxNumberOfCitizens) {
        this.maxNumberOfCitizens = maxNumberOfCitizens;
    }

    public List<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<Citizen> citizens) {
        this.citizens = citizens;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}