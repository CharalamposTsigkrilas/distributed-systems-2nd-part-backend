package ds.part1.FamilyDoctor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

@Entity
public class Citizen extends User{

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    private String AMKA;

    @NotBlank
    @Size(max = 50)
    private String apartmentAddress;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "citizen_family_members")
    private List<FamilyMember> familyMembers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="request_id")
    private Request request;

    public Citizen(String fullName, String username, String password, String email, String phoneNumber,
                   String department, String prefecture, String AMKA, String apartmentAddress) {

        super(fullName, username, password, email, phoneNumber, department, prefecture);

        this.AMKA = AMKA;
        this.apartmentAddress = apartmentAddress;
    }

    public Citizen() {

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

    public String getAMKA() {
        return AMKA;
    }

    public void setAMKA(String AMKA) {
        this.AMKA = AMKA;
    }

    public String getApartmentAddress() {
        return apartmentAddress;
    }

    public void setApartmentAddress(String apartmentAddress) {
        this.apartmentAddress = apartmentAddress;
    }

    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}