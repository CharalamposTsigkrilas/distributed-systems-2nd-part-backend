package ds.familydoctor.service;

import ds.familydoctor.entity.*;
import ds.familydoctor.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public Doctor getDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId).get();
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public void saveDoctor(Doctor doctor) {
        doctor.setAppointmentsCompleted(0);
        doctor.setRating(0F);
        doctorRepository.save(doctor);
    }

    @Transactional
    public void updateDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    @Transactional
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getAppointments();
    }

    @Transactional
    public List<Citizen> getDoctorCitizens(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getCitizens();
    }

    @Transactional
    public List<Request> getDoctorRequests(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getRequests();
    }
}
