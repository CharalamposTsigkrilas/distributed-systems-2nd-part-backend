package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.Request;
import ds.part1.FamilyDoctor.repository.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Transactional
    public Request getRequest(Long requestId){
        return requestRepository.findById(requestId).get();
    }

    @Transactional
    public List<Request> getRequests(){
        return requestRepository.findAll();
    }

    @Transactional
    public void saveRequest(Request request){
        request.setCurrentStatus(Request.status.unseen.toString());
        requestRepository.save(request);
    }

    @Transactional
    public void updateRequest(Request request){
        requestRepository.save(request);
    }

    @Transactional
    public void deleteRequest(Long requestId){
        requestRepository.deleteById(requestId);
    }
}
