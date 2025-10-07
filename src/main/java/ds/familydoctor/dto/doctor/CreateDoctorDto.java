package ds.familydoctor.dto.doctor;

import jakarta.validation.constraints.*;

public class CreateDoctorDto {

    @NotBlank
    @Pattern(regexp = "\\d{9}")
    private String afm;

    @NotBlank
    private String specialty;

    @NotBlank
    private String officeAddress;

    public CreateDoctorDto(String afm, String specialty, String officeAddress) {
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
}
