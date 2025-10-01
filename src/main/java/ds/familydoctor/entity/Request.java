package ds.familydoctor.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "requests",
        uniqueConstraints = @UniqueConstraint(columnNames = {"citizen_id", "doctor_id"}))
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum RequestStatus {
        PENDING, ACCEPTED, REJECTED
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public Request(Long id, RequestStatus status, Citizen citizen, Doctor doctor) {
        this.id = id;
        this.status = status;
        this.citizen = citizen;
        this.doctor = doctor;
    }

    public Request() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
