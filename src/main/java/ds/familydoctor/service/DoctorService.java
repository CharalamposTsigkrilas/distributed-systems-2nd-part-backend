package ds.familydoctor.service;

import ds.familydoctor.entity.Doctor;
import ds.familydoctor.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository docRepo;

    @Transactional
    public Doctor getDoctor(Long doctorId) {
        return docRepo.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with this id: " + doctorId));
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
            throw new EntityNotFoundException("Doctor not found with this id: " + doctorId);
        }
        docRepo.deleteById(doctorId);
    }

}
