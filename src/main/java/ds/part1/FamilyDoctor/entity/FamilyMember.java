package ds.part1.FamilyDoctor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(	name = "family_members")
public class FamilyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String fullName;

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    private String AMKA;

    @Size(max = 11)
    private String memberRelationship;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="citizen_id")
    private Citizen citizen;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="appointment_id")
    private Appointment appointment;

    public FamilyMember(String fullName, String AMKA, String memberRelationship) {
        this.fullName = fullName;
        this.AMKA = AMKA;
        this.memberRelationship = memberRelationship;
    }

    public FamilyMember() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAMKA() {
        return AMKA;
    }

    public void setAMKA(String AMKA) {
        this.AMKA = AMKA;
    }

    public String getMemberRelationship() {
        return memberRelationship;
    }

    public void setMemberRelationship(String memberRelationship) {
        this.memberRelationship = memberRelationship;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}