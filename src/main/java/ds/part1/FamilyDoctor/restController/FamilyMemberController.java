package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.service.AppointmentService;
import ds.part1.FamilyDoctor.entity.Appointment;
import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.FamilyMember;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import ds.part1.FamilyDoctor.service.CitizenService;
import ds.part1.FamilyDoctor.service.DoctorService;
import ds.part1.FamilyDoctor.service.FamilyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/familyMember")
public class FamilyMemberController {

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/new/forCitizen:{citizen_id}")
    public ResponseEntity<?> saveFamilyMember(@RequestBody FamilyMember familyMember,@PathVariable Long citizen_id){

        Citizen citizen = citizenService.getCitizen(citizen_id);
        if(citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }
        familyMember.setCitizen(citizen);

        List<FamilyMember> allFamilyMembers = familyMemberService.getAllFamilyMembers();
        for(FamilyMember currFamMem : allFamilyMembers){
            if (currFamMem.getAMKA().equals(familyMember.getAMKA())){
                return ResponseEntity.badRequest().body(new MessageResponse("Family Member already exists!"));
            }
        }

        familyMemberService.saveFamilyMember(familyMember);
        return ResponseEntity.ok(new MessageResponse("Family Member saved!"));
    }

    @GetMapping("/all/forCitizen:{citizen_id}")
    public List<FamilyMember> showFamily(@PathVariable Long citizen_id){
        Citizen citizen = citizenService.getCitizen(citizen_id);
        List<FamilyMember> family = null;
        if(citizen==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }else{
             family = citizenService.getCitizenFamilyMembers(citizen_id);
        }
        return family;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/update/forCitizen:{citizen_id}/Member:{familyMember_id}")
    public List<FamilyMember> editFamilyMember(@RequestBody FamilyMember familyMember,@PathVariable Long citizen_id,@PathVariable Long familyMember_id){

        FamilyMember updatedFamilyMember = familyMemberService.getFamilyMember(familyMember_id);
        Citizen citizen = citizenService.getCitizen(citizen_id);

        if(updatedFamilyMember==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member doesn't exists!"));
        }else if(citizen == null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }else{
            updatedFamilyMember.setFullName(familyMember.getFullName());
            updatedFamilyMember.setAMKA(familyMember.getAMKA());
            updatedFamilyMember.setMemberRelationship(familyMember.getMemberRelationship());
            familyMemberService.updateFamilyMember(updatedFamilyMember);
            ResponseEntity.ok(new MessageResponse("Family Member updated!"));
        }

        return citizen.getFamilyMembers();

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/delete/{familyMember_id}")
    public ResponseEntity<?> deleteFamilyMember(@PathVariable Long familyMember_id){

        FamilyMember familyMember = familyMemberService.getFamilyMember(familyMember_id);
        if(familyMember==null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member doesn't exists!"));
        }

        if(familyMember.getAppointment()!=null){

            //Find member's appointment
            Long appointment_id = (familyMember.getAppointment()).getId();
            Appointment appointment = appointmentService.getAppointment(appointment_id);

            //Find appointment's doctor and removing the appointment from his/her list
            Long doctor_id = (appointment.getDoctor()).getId();
            Doctor doctor = doctorService.getDoctor(doctor_id);
            doctor.getAppointments().remove(appointment);

            //Deleting member's appointment because we don't need it
            appointmentService.deleteAppointment(appointment_id);

        }

        //Change FullName with citizen
        if( familyMember.getCitizen().getFullName().equals(familyMember.getFullName()) ){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You cannot delete the main Family Member!"));
        }

        //Deleting the member
        familyMemberService.deleteFamilyMember(familyMember_id);

        return ResponseEntity.ok(new MessageResponse("Family Member deleted!"));
    }

}