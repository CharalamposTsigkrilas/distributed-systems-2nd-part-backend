package ds.familydoctor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "family_members")
public class FamilyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Relationship {
        SELF, CHILD, PARENT, SPOUSE
    }

    @NotBlank
    private String name;

    @Pattern(regexp = "\\d{11}")
    private String amka;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Relationship memberRelationship;

    @ManyToOne
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @OneToMany(mappedBy = "familyMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    public FamilyMember(Long id, String name, String amka, Relationship memberRelationship, Citizen citizen, List<Appointment> appointments) {
        this.id = id;
        this.name = name;
        this.amka = amka;
        this.memberRelationship = memberRelationship;
        this.citizen = citizen;
        this.appointments = appointments;
    }

    public FamilyMember() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public Relationship getMemberRelationship() {
        return memberRelationship;
    }

    public void setMemberRelationship(Relationship memberRelationship) {
        this.memberRelationship = memberRelationship;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    // helper - count pending
//    public long pendingAppointmentsCount() {
//        return appointments.stream().filter(a -> a.getStatus()==AppointmentStatus.PENDING).count();
//    }
}
