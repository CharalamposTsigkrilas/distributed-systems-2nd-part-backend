package ds.familydoctor.dto.citizen;

import jakarta.validation.constraints.*;

public class CreateCitizenDto {

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    private String amka;

    public CreateCitizenDto(String amka) {
        this.amka = amka;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }
}
