package ds.familydoctor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 40)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String full_name;

    @NotBlank
    @Pattern(regexp = "\\d{10}")
    private String phone_number;

    @NotBlank
    private String home_address;

    @NotBlank
    private String country;

    @NotBlank
    private String continent;

    @NotBlank
    private String prefecture;

    @NotBlank
    private String city;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Citizen citizenProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Doctor doctorProfile;

    public User(Long id, String username, String password, String email, String full_name, String phone_number, String home_address, String country, String continent, String prefecture, String city, Set<Role> roles, Citizen citizenProfile, Doctor doctorProfile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.full_name = full_name;
        this.phone_number = phone_number;
        this.home_address = home_address;
        this.country = country;
        this.continent = continent;
        this.prefecture = prefecture;
        this.city = city;
        this.roles = roles;
        this.citizenProfile = citizenProfile;
        this.doctorProfile = doctorProfile;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Citizen getCitizenProfile() {
        return citizenProfile;
    }

    public void setCitizenProfile(Citizen citizenProfile) {
        this.citizenProfile = citizenProfile;
    }

    public Doctor getDoctorProfile() {
        return doctorProfile;
    }

    public void setDoctorProfile(Doctor doctorProfile) {
        this.doctorProfile = doctorProfile;
    }

    // helper to add role
    public void addRole(Role r){ roles.add(r); }
}
