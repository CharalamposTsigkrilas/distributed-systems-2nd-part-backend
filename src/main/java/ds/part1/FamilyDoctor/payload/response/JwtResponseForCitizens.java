package ds.part1.FamilyDoctor.payload.response;

import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.FamilyMember;

import java.util.List;

public class JwtResponseForCitizens extends JwtResponseForUsers{

    private String AMKA;

    private String apartmentAddress;
    private List<FamilyMember> familyMembers;
    private Doctor doctor;

    public JwtResponseForCitizens(String token, Long id, String fullName, String username, String email,
                                  String phoneNumber, String department, String prefecture, List<String> roles,
                                  String AMKA, String apartmentAddress, List<FamilyMember> familyMembers,
                                  Doctor doctor) {

        super(token, id, fullName, username, email, phoneNumber, department, prefecture, roles);

        this.AMKA = AMKA;
        this.apartmentAddress = apartmentAddress;
        this.familyMembers = familyMembers;
        this.doctor = doctor;
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String getAccessToken() {
        return super.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        super.setAccessToken(accessToken);
    }

    @Override
    public String getTokenType() {
        return super.getTokenType();
    }

    @Override
    public void setTokenType(String tokenType) {
        super.setTokenType(tokenType);
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
    public List<String> getRoles() {
        return super.getRoles();
    }
}
