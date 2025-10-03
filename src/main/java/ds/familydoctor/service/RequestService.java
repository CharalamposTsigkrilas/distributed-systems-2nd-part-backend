package ds.familydoctor.service;

import ds.familydoctor.entity.Request;
import ds.familydoctor.repository.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository reqRepo;

    @Transactional
    public Request getRequest(Long requestId) {
        return reqRepo.findById(requestId).get();
    }

    @Transactional
    public List<Request> getRequests() {
        return reqRepo.findAll();
    }

    @Transactional
    public void saveRequest(Request req) {
        req.setStatus(Request.RequestStatus.PENDING);
        reqRepo.save(req);
    }

    @Transactional
    public void updateRequest(Request req) {
        reqRepo.save(req);
    }

    @Transactional
    public void deleteRequest(Long requestId) {
        reqRepo.deleteById(requestId);
    }

}
