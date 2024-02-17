package ds.part1.FamilyDoctor.service;

import ds.part1.FamilyDoctor.entity.*;
import ds.part1.FamilyDoctor.repository.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private DoctorService doctorService;

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

    @Transactional
    public Citizen getRequestCitizen(Long requestId){
        List<Citizen> citizens = citizenService.getCitizens();

        for (Citizen currentCitizen : citizens) {
            Request currentCitizenRequest = currentCitizen.getRequest();
            Long currentCitizenRequestId = currentCitizenRequest.getId();

            if (currentCitizenRequestId.equals(requestId)) {
                return currentCitizen;
            }
        }
        return null;
    }

    @Transactional
    public Doctor getRequestDoctor(Long requestId){
        List<Doctor> doctors = doctorService.getDoctors();

        for (Doctor currentDoctor : doctors) {
            Long currentDoctorId = currentDoctor.getId();
            List<Request> doctorRequests = doctorService.getDoctorRequests(currentDoctorId);

            for (Request currentDoctorRequest : doctorRequests) {
                Long currentDoctorRequestId = currentDoctorRequest.getId();

                if (currentDoctorRequestId.equals(requestId)){
                    return currentDoctor;
                }
            }
        }
        return null;
    }

}