package ds.familydoctor.dto.citizen;

import ds.familydoctor.dto.user.CreateUserDto;
import jakarta.validation.constraints.*;

public class CreateCitizenDto extends CreateUserDto {

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    private String amka;

    public CreateCitizenDto(String username, String password, String email, String fullName, String phoneNumber, String homeAddress, String country, String continent, String prefecture, String city, String amka) {
        super(username, password, email, fullName, phoneNumber, homeAddress, country, continent, prefecture, city);
        this.amka = amka;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public CreateCitizenDto(String username, String password, String email, String fullName, String phoneNumber, String homeAddress, String country, String continent, String prefecture, String city) {
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
