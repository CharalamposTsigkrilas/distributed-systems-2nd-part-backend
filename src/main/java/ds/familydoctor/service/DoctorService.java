package ds.familydoctor.service;

import ds.familydoctor.entity.Doctor;
import ds.familydoctor.repository.DoctorRepository;
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
        return docRepo.findById(doctorId).get();
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return docRepo.findAll();
    }

    @Transactional
    public void saveDoctor(Doctor doc) {
        docRepo.save(doc);
    }

    @Transactional
    public void updateDoctor(Doctor doc) {
        docRepo.save(doc);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        docRepo.deleteById(doctorId);
    }

}
