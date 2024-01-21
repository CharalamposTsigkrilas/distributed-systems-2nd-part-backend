package ds.part1.FamilyDoctor.payload.request;

import jakarta.validation.constraints.*;

import java.util.Set;

public class SignupRequest {

    @NotBlank
    @Size(max = 50)
    private String fullName;

    @NotBlank
    @Size(min = 8, max = 40)
    private String username;

    @NotBlank
    @Size(min = 8, max = 30)
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Pattern(regexp = "\\d{10}")
    private String phoneNumber;

    @NotBlank
    @Size(max = 50)
    private String department;

    @NotBlank
    @Size(max = 50)
    private String prefecture;

    private Set<String> role;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

}