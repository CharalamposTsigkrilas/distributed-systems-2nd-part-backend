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
    private boolean is_citizen = false;

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

}
