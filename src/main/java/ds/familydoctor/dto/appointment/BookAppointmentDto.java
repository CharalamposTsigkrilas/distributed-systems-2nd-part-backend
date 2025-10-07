package ds.familydoctor.dto.appointment;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public class BookAppointmentDto {

    @NotNull
    private Long familyMemberId;

    @NotNull
    @Future
    private OffsetDateTime appointmentDateTime;

    public BookAppointmentDto(Long familyMemberId, OffsetDateTime appointmentDateTime) {
        this.familyMemberId = familyMemberId;
        this.appointmentDateTime = appointmentDateTime;
    }

    public Long getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(Long familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public OffsetDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(OffsetDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
}
