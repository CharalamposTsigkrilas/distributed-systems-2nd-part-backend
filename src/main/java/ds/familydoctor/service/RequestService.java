package ds.familydoctor.service;

import ds.familydoctor.entity.Request;
import ds.familydoctor.repository.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository reqRepo;

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

}
