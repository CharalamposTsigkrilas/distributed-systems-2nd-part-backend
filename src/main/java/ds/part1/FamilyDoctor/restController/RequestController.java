package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.Request;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import ds.part1.FamilyDoctor.service.CitizenService;
import ds.part1.FamilyDoctor.service.DoctorService;
import ds.part1.FamilyDoctor.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{request_id}")
    public Request getRequest(@PathVariable Long request_id){
        return requestService.getRequest(request_id);
    }

    @GetMapping("/{request_id}/citizen")
    public Citizen getCitizenFromRequest(@PathVariable Long request_id){
        List<Citizen> citizens = citizenService.getCitizens();
        for(Citizen currCitizen: citizens){
            if(currCitizen.getRequest()==null){
                continue;
            }
            Long citizenRequestId = currCitizen.getRequest().getId();
            if (citizenRequestId.equals(request_id)){
                return currCitizen;
            }
        }
        return null;
    }

    @GetMapping("")
    public List<Request> getRequests(){
        return requestService.getRequests();
    }

    @PostMapping("/new/from/citizen/{citizen_id}/to/doctor/{doctor_id}")
    public ResponseEntity<?> createRequest(@PathVariable Long citizen_id, @PathVariable Long doctor_id){

        Request request = new Request();

        Citizen citizen = citizenService.getCitizen(citizen_id);
        Doctor doctor = doctorService.getDoctor(doctor_id);

        if (citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        if (doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }

        requestService.saveRequest(request);

        citizen.setRequest(request);
        citizenService.updateCitizen(citizen);

        doctor.getRequests().add(request);
        doctorService.updateDoctor(doctor);

        return ResponseEntity.ok(new MessageResponse("Request created!"));
    }

    @PostMapping("/{request_id}/answer/{answer}")
    public void DoctorAnswer(@PathVariable Long request_id, @PathVariable String answer){
        Request request = requestService.getRequest(request_id);

        String currStatus=Request.status.unseen.toString();

        if(answer.equals("accept")){
            currStatus = Request.status.accepted.toString();
        }else if(answer.equals("reject")){
            currStatus = Request.status.rejected.toString();
        }

        request.setCurrentStatus(currStatus);

        requestService.updateRequest(request);

        boolean doctorFound = false;
        List<Doctor> allDoctors = doctorService.getDoctors();
        Doctor doctor=null;
        for (Doctor currDoctor: allDoctors){
            List<Request> doctorRequests = currDoctor.getRequests();
            if (doctorRequests.isEmpty()){
                continue;
            }
            for (Request docReq: doctorRequests){
                Long currReqId = docReq.getId();
                if(currReqId.equals(request_id)){
                    doctor = currDoctor;
                    doctor.getRequests().remove(docReq);
                    doctor.getRequests().add(request);
                    doctorFound = true;
                    break;
                }
            }
            if (doctorFound){
                break;
            }
        }

        if(!doctorFound){
            return;
        }

        boolean citizenFound = false;
        List<Citizen> allCitizens = citizenService.getCitizens();
        Citizen citizen=null;
        for(Citizen currCitizen: allCitizens){
            if(currCitizen.getRequest()==null){
                continue;
            }
            Long citizenRequestId = currCitizen.getRequest().getId();
            if (citizenRequestId.equals(request_id)){
                citizen=currCitizen;
                citizen.setRequest(request);
                citizenFound = true;
                break;
            }
        }

        if(!citizenFound){
            return;
        }

        doctor.getCitizens().add(citizen);
        //doctorService.updateDoctor(doctor);

        citizen.setDoctor(doctor);
        //citizenService.updateCitizen(citizen);
    }

    @PostMapping("/change/status")
    public ResponseEntity<?> updateRequest (){
        return ResponseEntity.ok(new MessageResponse("Request updated!"));
    }
}
