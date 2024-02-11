package ds.part1.FamilyDoctor.payload.response;

import ds.part1.FamilyDoctor.entity.Appointment;
import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Request;

import java.util.List;

public class JwtResponseForDoctors extends JwtResponseForUsers{

    private String specialty;
    private String doctorOfficeAddress;
    private float rating;
    private int appointmentsCompleted;
    private int maxNumberOfCitizens;
    private List<Citizen> citizens;
    private List<Appointment> appointments;

    private List<Request> requests;

    public JwtResponseForDoctors(String token, Long id, String fullName, String username, String email,
                                 String phoneNumber, String department, String prefecture, List<String> roles,
                                 String specialty, String doctorOfficeAddress, float rating, int appointmentsCompleted,
                                 int maxNumberOfCitizens, List<Citizen> citizens, List<Appointment> appointments,
                                 List<Request> requests) {

        super(token, id, fullName, username, email, phoneNumber, department, prefecture, roles);

        this.specialty = specialty;
        this.doctorOfficeAddress = doctorOfficeAddress;
        this.rating = rating;
        this.appointmentsCompleted = appointmentsCompleted;
        this.maxNumberOfCitizens = maxNumberOfCitizens;
        this.citizens = citizens;
        this.appointments = appointments;
        this.requests = requests;
    }

    @Override
    public String getAccessToken() {
        return super.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        super.setAccessToken(accessToken);
    }

    @Override
    public String getTokenType() {
        return super.getTokenType();
    }

    @Override
    public void setTokenType(String tokenType) {
        super.setTokenType(tokenType);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public String getFullName() {
        return super.getFullName();
    }

    @Override
    public void setFullName(String fullName) {
        super.setFullName(fullName);
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        super.setPhoneNumber(phoneNumber);
    }

    @Override
    public List<String> getRoles() {
        return super.getRoles();
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDoctorOfficeAddress() {
        return doctorOfficeAddress;
    }

    public void setDoctorOfficeAddress(String doctorOfficeAddress) {
        this.doctorOfficeAddress = doctorOfficeAddress;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getAppointmentsCompleted() {
        return appointmentsCompleted;
    }

    public void setAppointmentsCompleted(int appointmentsCompleted) {
        this.appointmentsCompleted = appointmentsCompleted;
    }

    public int getMaxNumberOfCitizens() {
        return maxNumberOfCitizens;
    }

    public void setMaxNumberOfCitizens(int maxNumberOfCitizens) {
        this.maxNumberOfCitizens = maxNumberOfCitizens;
    }

    public List<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<Citizen> citizens) {
        this.citizens = citizens;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
