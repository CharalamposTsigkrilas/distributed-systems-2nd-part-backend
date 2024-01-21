package ds.part1.FamilyDoctor.implementation;

import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.FamilyMember;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class CitizenDetailsImpl extends UserDetailsImpl{

    private String AMKA;

    private String apartmentAddress;
    private List<FamilyMember> familyMembers;
    private Doctor doctor;

    public CitizenDetailsImpl(Long id, String fullName, String username, String password, String email,
                              String phoneNumber, String department, String prefecture,
                              Collection<? extends GrantedAuthority> authorities, String AMKA, String apartmentAddress,
                              List<FamilyMember> familyMembers, Doctor doctor) {

        super(id, fullName, username, password, email, phoneNumber, department, prefecture, authorities);

        this.AMKA = AMKA;
        this.apartmentAddress = apartmentAddress;
        this.familyMembers = familyMembers;
        this.doctor = doctor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getFullName() {
        return super.getFullName();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    public String getDepartment() {
        return super.getDepartment();
    }

    @Override
    public String getPrefecture() {
        return super.getPrefecture();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public String getAMKA() {
        return AMKA;
    }

    public String getApartmentAddress() {
        return apartmentAddress;
    }

    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

}