package ds.familydoctor.service;

import ds.familydoctor.dto.doctor.CreateDoctorDto;
import ds.familydoctor.entity.*;
import ds.familydoctor.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Doctor getDoctor(Long doctorId) {
        return docRepo.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with this id: " + doctorId + "."));
    }

    @Transactional
    public List<Doctor> getAllDoctors() {
        return docRepo.findAll();
    }

    @Transactional
    public Doctor save(Doctor doc) {
        return docRepo.save(doc);
    }

    @Transactional
    public Doctor updateDoctor(Doctor doc) {
        return docRepo.save(doc);
    }

    @Transactional
    public void delete(Long doctorId) {
        if (!docRepo.existsById(doctorId)) {
            throw new EntityNotFoundException("Doctor not found with this id: " + doctorId + ".");
        }
        docRepo.deleteById(doctorId);
    }

    @Transactional
    public boolean alreadyExists(String afm) {
        return docRepo.existsByAfm(afm);
    }

    @Transactional
    public Doctor create(CreateDoctorDto docDto){

        User user = userService.createUserWithRole(docDto, "DOCTOR");

        if (alreadyExists(docDto.getAfm())) {
            throw new IllegalStateException("AFM already in use.");
        }

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setAfm(docDto.getAfm());
        doctor.setSpecialty(docDto.getSpecialty());
        doctor.setOfficeAddress(docDto.getOfficeAddress());
        return save(doctor);
    }
}
