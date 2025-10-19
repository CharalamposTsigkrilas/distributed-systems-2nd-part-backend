package ds.familydoctor.dto.doctor;

import ds.familydoctor.dto.user.CreateUserDto;
import jakarta.validation.constraints.*;

public class CreateDoctorDto extends CreateUserDto {

    @NotBlank
    @Pattern(regexp = "\\d{9}")
    private String afm;

    @NotBlank
    private String specialty;

    @NotBlank
    private String officeAddress;

    public CreateDoctorDto(String username, String password, String email, String fullName, String phoneNumber, String homeAddress, String country, String continent, String prefecture, String city, String afm, String specialty, String officeAddress) {
        super(username, password, email, fullName, phoneNumber, homeAddress, country, continent, prefecture, city);
        this.afm = afm;
        this.specialty = specialty;
        this.officeAddress = officeAddress;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public CreateDoctorDto(String username, String password, String email, String fullName, String phoneNumber, String homeAddress, String country, String continent, String prefecture, String city) {
        super(username, password, email, fullName, phoneNumber, homeAddress, country, continent, prefecture, city);
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
    public String getFullName() {
        return super.getFullName();
    }

    @Override
    public void setFullName(String fullName) {
        super.setFullName(fullName);
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
    public String getHomeAddress() {
        return super.getHomeAddress();
    }

    @Override
    public void setHomeAddress(String homeAddress) {
        super.setHomeAddress(homeAddress);
    }

    @Override
    public String getCountry() {
        return super.getCountry();
    }

    @Override
    public void setCountry(String country) {
        super.setCountry(country);
    }

    @Override
    public String getContinent() {
        return super.getContinent();
    }

    @Override
    public void setContinent(String continent) {
        super.setContinent(continent);
    }

    @Override
    public String getPrefecture() {
        return super.getPrefecture();
    }

    @Override
    public void setPrefecture(String prefecture) {
        super.setPrefecture(prefecture);
    }

    @Override
    public String getCity() {
        return super.getCity();
    }

    @Override
    public void setCity(String city) {
        super.setCity(city);
    }
}
