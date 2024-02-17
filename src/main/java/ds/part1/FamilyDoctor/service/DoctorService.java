package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.*;
import ds.part1.FamilyDoctor.repository.DoctorRepository;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional
    public Doctor getDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId).get();
    }

    @Transactional
    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    @Transactional
    public void saveDoctor(Doctor doctor){
        doctor.setAppointmentsCompleted(0);
        doctor.setRating(0F);
        doctorRepository.save(doctor);
    }

    @Transactional
    public void updateDoctor(Doctor doctor){
        doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    @Transactional
    public List<Appointment> getDoctorAppointments(Long doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getAppointments();
    }

    @Transactional
    public List<Citizen> getDoctorCitizens(Long doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getCitizens();
    }

    @Transactional
    public List<Request> getDoctorRequests(Long doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).get();
        return doctor.getRequests();
    }
}
