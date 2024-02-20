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

    @GetMapping("")
    public List<Request> getRequests(){
        return requestService.getRequests();
    }

    @GetMapping("/{request_id}/citizen")
    public Citizen getCitizenFromRequest(@PathVariable Long request_id){
        return requestService.getRequestCitizen(request_id);
    }

    @GetMapping("/{request_id}/doctor")
    public Doctor getDoctorFromRequest(@PathVariable Long request_id){
        return requestService.getRequestDoctor(request_id);
    }

    @PostMapping("/new/from/citizen/{citizen_id}/to/doctor/{doctor_id}")
    public ResponseEntity<?> createRequest(@PathVariable Long citizen_id, @PathVariable Long doctor_id){

        Request request = new Request();

        Citizen citizen = citizenService.getCitizen(citizen_id);
        Doctor doctor = doctorService.getDoctor(doctor_id);

        if (citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request Citizen not found!"));
        }

        if (doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request Doctor not found!"));
        }

        Request citizenRequest = citizen.getRequest();
        if (citizenRequest != null ) {
            String requestStatus = citizenRequest.getCurrentStatus();
            if (requestStatus.equals(Request.status.accepted.toString())){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen's previous Request has already been accepted by a Doctor!"));
            }
            if (requestStatus.equals(Request.status.unseen.toString())){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen has already sent a Request to a Doctor!"));
            }
        }

        requestService.saveRequest(request);

        citizen.setRequest(request);
        citizenService.updateCitizen(citizen);

        doctor.getRequests().add(request);
        doctorService.updateDoctor(doctor);

        return ResponseEntity.ok(new MessageResponse("Request created!"));
    }

    @PostMapping("/{request_id}/answer/{answer}")
    public ResponseEntity<?> DoctorAnswer(@PathVariable Long request_id, @PathVariable String answer){
        Request request = requestService.getRequest(request_id);

        String currStatus=Request.status.unseen.toString();

        if(answer.equals("accept")){
            currStatus = Request.status.accepted.toString();
        }else if(answer.equals("reject")){
            currStatus = Request.status.rejected.toString();
        }

        Doctor requestDoctor = requestService.getRequestDoctor(request_id);
        if (requestDoctor == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request Doctor not found!"));
        }
        List<Request> doctorRequests = requestDoctor.getRequests();
        List<Citizen> doctorCitizens = requestDoctor.getCitizens();

        Citizen requestCitizen = requestService.getRequestCitizen(request_id);
        if (requestCitizen == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request Citizen not found!"));
        }

        Request citizenRequest = requestCitizen.getRequest();
        if (citizenRequest == null ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen Request not found!"));
        }

        String requestStatus = citizenRequest.getCurrentStatus();
        if (!requestStatus.equals(Request.status.unseen.toString())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen's previous Request has already been answered by a Doctor!"));
        }

        doctorRequests.remove(request);

        request.setCurrentStatus(currStatus);

        requestCitizen.setRequest(request);
        citizenService.updateCitizen(requestCitizen);

        doctorRequests.add(request);
        if (currStatus.equals(Request.status.accepted.toString())) {
            doctorCitizens.add(requestCitizen);
        }
        doctorService.updateDoctor(requestDoctor);

        requestService.updateRequest(request);

        if (currStatus.equals(Request.status.accepted.toString())) {
            return ResponseEntity.ok(new MessageResponse("Request accepted!"));
        }
        return ResponseEntity.ok(new MessageResponse("Request rejected!"));
    }

    @PostMapping("/{request_id}/delete")
    public ResponseEntity<?> cancelRequest(@PathVariable Long request_id){

        Request request = requestService.getRequest(request_id);
        if (request == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request not found!"));
        }
        String status = request.getCurrentStatus();

        if(status.equals(Request.status.accepted.toString()) || status.equals(Request.status.rejected.toString())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen's Request cannot be canceled! It has already been answered by a Doctor!"));
        }

        Doctor requestDoctor = requestService.getRequestDoctor(request_id);
        if (requestDoctor == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request Doctor not found!"));
        }
        List<Request> doctorRequests = requestDoctor.getRequests();

        Citizen requestCitizen = requestService.getRequestCitizen(request_id);
        if (requestCitizen == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request Citizen not found!"));
        }

        Request citizenRequest = requestCitizen.getRequest();
        if (citizenRequest == null ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen Request not found!"));
        }

        doctorRequests.remove(request);

        requestCitizen.setRequest(null);
        citizenService.updateCitizen(requestCitizen);

        doctorService.updateDoctor(requestDoctor);

        requestService.deleteRequest(request_id);

        return ResponseEntity.ok(new MessageResponse("Request deleted!"));
    }
}
