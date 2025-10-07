package ds.familydoctor.service;

import ds.familydoctor.entity.*;
import ds.familydoctor.repository.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository reqRepo;

    @Autowired
    private CitizenService citiService;

    @Autowired
    private DoctorService docService;

    @Transactional
    public Request getRequest(Long requestId) {
        return reqRepo.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found with this id: " + requestId));
    }

    @Transactional
    public List<Request> getAllRequests() {
        return reqRepo.findAll();
    }

    @Transactional
    public Request save(Request req) {
        req.setStatus(Request.RequestStatus.PENDING);
        return reqRepo.save(req);
    }

    @Transactional
    public List<Request> updateRequestList(List<Request> toUpdate) {
        return reqRepo.saveAll(toUpdate);
    }

    @Transactional
    public Request update(Request req) {
        return reqRepo.save(req);
    }

    @Transactional
    public void deleteRequest(Long requestId) {
        if (!reqRepo.existsById(requestId)) {
            throw new EntityNotFoundException("Request not found with this id: " + requestId);
        }
        reqRepo.deleteById(requestId);
    }

    @Transactional
    public boolean alreadyDoneRequest(Citizen citizen, Doctor doctor) {
        return reqRepo.existsByCitizenAndDoctor(citizen, doctor);
    }

    @Transactional
    public List<Request> findCitizenRequests(Citizen citi) {
        return reqRepo.findByCitizen(citi);
    }

    @Transactional
    public Request createRequest(Long citizenId, Long doctorId){
        Citizen citizen = citiService.getCitizen(citizenId);
        Doctor doctor = docService.getDoctor(doctorId);

        // unique per citizen-doctor
        if (alreadyDoneRequest(citizen, doctor)){
            throw new IllegalStateException("Request already exists from the same citizen to that doctor.");
        }

        Request req = new Request();
        req.setCitizen(citizen);
        req.setDoctor(doctor);
        req.setStatus(Request.RequestStatus.PENDING);
        return reqRepo.save(req);
    }

    @Transactional
    public void acceptRequest(Long requestId) {
        Request req = getRequest(requestId);
        if (req.getStatus()!= Request.RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be accepted.");
        }

        req.setStatus(Request.RequestStatus.ACCEPTED);
        save(req);

        // give citizen this doctor
        Citizen citi = req.getCitizen();
        citi.setFamilyDoctor(req.getDoctor());
        citiService.update(citi);

        // Reject all other requests by this citizen (for other doctors) automatically
        List<Request> allRequests = findCitizenRequests(citi);
        List<Request> toReject = new ArrayList<>();

        for (Request currReq : allRequests) {
            if (!currReq.getId().equals(req.getId()) && currReq.getStatus().equals(Request.RequestStatus.PENDING)) {
                currReq.setStatus(Request.RequestStatus.REJECTED);
                toReject.add(currReq);
            }
        }

        updateRequestList(toReject);
    }

    @Transactional
    public void rejectRequest(Long requestId) {
        Request req = getRequest(requestId);
        if (req.getStatus()!= Request.RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be rejected.");
        }

        req.setStatus(Request.RequestStatus.REJECTED);
        save(req);
    }

    @Transactional
    public void cancelRequestByCitizen(Long requestId) {
        Request req = getRequest(requestId);
        if (req.getStatus()!= Request.RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be canceled.");
        }

        req.setStatus(Request.RequestStatus.CANCELED);
        save(req);
    }
}
