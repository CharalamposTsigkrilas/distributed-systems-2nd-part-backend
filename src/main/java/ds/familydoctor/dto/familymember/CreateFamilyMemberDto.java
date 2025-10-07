package ds.familydoctor.dto.familymember;

import ds.familydoctor.entity.FamilyMember;
import jakarta.validation.constraints.*;

public class CreateFamilyMemberDto {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    private String amka;

    @NotNull
    private FamilyMember.Relationship memberRelationship;

    public CreateFamilyMemberDto(String name, String amka, FamilyMember.Relationship memberRelationship) {
        this.name = name;
        this.amka = amka;
        this.memberRelationship = memberRelationship;
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

    public FamilyMember.Relationship getMemberRelationship() {
        return memberRelationship;
    }

    public void setMemberRelationship(FamilyMember.Relationship memberRelationship) {
        this.memberRelationship = memberRelationship;
    }
}
