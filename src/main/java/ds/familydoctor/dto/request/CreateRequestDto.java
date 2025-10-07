package ds.familydoctor.dto.request;

import jakarta.validation.constraints.*;

public class CreateRequestDto {

    @NotNull
    private Long citizenId;

    @NotNull
    private Long doctorId;

    public CreateRequestDto(Long citizenId, Long doctorId) {
        this.citizenId = citizenId;
        this.doctorId = doctorId;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}
