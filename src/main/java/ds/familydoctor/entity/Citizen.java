package ds.familydoctor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "citizens", uniqueConstraints = {
        @UniqueConstraint(columnNames = "amka")
})
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    private String amka;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor familyDoctor;

    @OneToMany(mappedBy = "citizen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FamilyMember> familyMembers;

    @OneToMany(mappedBy = "citizen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> requests;

    public Citizen(Long id, String amka, User user, Doctor familyDoctor, List<FamilyMember> familyMembers, List<Request> requests) {
        this.id = id;
        this.amka = amka;
        this.user = user;
        this.familyDoctor = familyDoctor;
        this.familyMembers = familyMembers;
        this.requests = requests;
    }

    public Citizen() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Doctor getFamilyDoctor() {
        return familyDoctor;
    }

    public void setFamilyDoctor(Doctor familyDoctor) {
        this.familyDoctor = familyDoctor;
    }

    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    // constructor / helper για αυτόματη δημιουργία FamilyMember για τον εαυτό
    @PrePersist
    public void ensureSelfFamilyMember(){
        if (familyMembers == null) {
            familyMembers = new ArrayList<>();
        }

        boolean hasSelf = familyMembers.stream()
                .anyMatch(fm -> fm.getMemberRelationship() == FamilyMember.Relationship.SELF);

        if (!hasSelf) {
            FamilyMember self = new FamilyMember();
            self.setName(user != null ? user.getFullName() : "Unknown");
            self.setMemberRelationship(FamilyMember.Relationship.SELF);
            self.setCitizen(this);
            familyMembers.add(self);
        }
    }

}
