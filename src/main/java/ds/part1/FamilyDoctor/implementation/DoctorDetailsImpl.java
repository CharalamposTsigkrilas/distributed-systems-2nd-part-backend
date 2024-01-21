package ds.part1.FamilyDoctor.implementation;

import ds.part1.FamilyDoctor.entity.Appointment;
import ds.part1.FamilyDoctor.entity.Citizen;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class DoctorDetailsImpl extends UserDetailsImpl{

    private String specialty;
    private String doctorOfficeAddress;
    private float rating;
    private int appointmentsCompleted;
    private int maxNumberOfCitizens;
    private List<Citizen> citizens;
    private List<Appointment> appointments;

    public DoctorDetailsImpl(Long id, String fullName, String username, String password, String email,
                             String phoneNumber, String department, String prefecture,
                             Collection<? extends GrantedAuthority> authorities, String specialty,
                             String doctorOfficeAddress, float rating, int appointmentsCompleted,
                             int maxNumberOfCitizens, List<Citizen> citizens, List<Appointment> appointments) {

        super(id, fullName, username, password, email, phoneNumber, department, prefecture, authorities);

        this.specialty = specialty;
        this.doctorOfficeAddress = doctorOfficeAddress;
        this.rating = rating;
        this.appointmentsCompleted = appointmentsCompleted;
        this.maxNumberOfCitizens = maxNumberOfCitizens;
        this.citizens = citizens;
        this.appointments = appointments;
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

    public String getSpecialty() {
        return specialty;
    }

    public String getDoctorOfficeAddress() {
        return doctorOfficeAddress;
    }

    public float getRating() {
        return rating;
    }

    public int getAppointmentsCompleted() {
        return appointmentsCompleted;
    }

    public int getMaxNumberOfCitizens() {
        return maxNumberOfCitizens;
    }

    public List<Citizen> getCitizens() {
        return citizens;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

}