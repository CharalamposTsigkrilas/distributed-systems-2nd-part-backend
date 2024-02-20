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
import jakarta.validation.Valid;
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

    @GetMapping("/{familyMember_id}/appointment")
    public Appointment getFamilyMemberAppointment(@PathVariable Long familyMember_id){
        return familyMemberService.getFamilyMemberAppointment(familyMember_id);
    }

    @PostMapping("/new/for/citizen/{citizen_id}")
    public ResponseEntity<?> saveFamilyMember(@Valid @RequestBody FamilyMember familyMember, @PathVariable Long citizen_id){

        Citizen citizen = citizenService.getCitizen(citizen_id);
        if(citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        List<FamilyMember> allFamilyMembers = familyMemberService.getFamilyMembers();
        for(FamilyMember currFamMem : allFamilyMembers){
            if (currFamMem.getAMKA().equals(familyMember.getAMKA())){
                return ResponseEntity.badRequest().body(new MessageResponse("Family Member already exists!"));
            }
        }

        citizen.getFamilyMembers().add(familyMember);

        familyMemberService.saveFamilyMember(familyMember);
        citizenService.updateCitizen(citizen);
        return ResponseEntity.ok(new MessageResponse("Family Member saved!"));
    }

    @GetMapping("/all/forCitizen/{citizen_id}")
    public List<FamilyMember> showFamily(@PathVariable Long citizen_id){
        List<FamilyMember> family = citizenService.getCitizenFamilyMembers(citizen_id);
        if(family.isEmpty()){
            return null;
        }
        return family;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{familyMember_id}/update")
    public ResponseEntity<?> editFamilyMember(@RequestBody FamilyMember updatedFamilyMember, @PathVariable Long familyMember_id){

        FamilyMember familyMember = familyMemberService.getFamilyMember(familyMember_id);
        if(familyMember==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member doesn't exists!"));
        }

        Citizen familyMemberCitizen = familyMemberService.getFamilyMemberCitizen(familyMember_id);
        if(familyMemberCitizen == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen of this family member not found!"));
        }

        List<FamilyMember> citizenFamilyMembers = familyMemberCitizen.getFamilyMembers();
        citizenFamilyMembers.remove(familyMember);

        familyMember.setFullName( updatedFamilyMember.getFullName() );
        familyMember.setAMKA( updatedFamilyMember.getAMKA() );
        familyMember.setMemberRelationship( updatedFamilyMember.getMemberRelationship() );

        citizenFamilyMembers.add(familyMember);
        citizenService.saveCitizen(familyMemberCitizen);

        familyMemberService.updateFamilyMember(familyMember);
        return ResponseEntity.ok(new MessageResponse("Family Member updated!"));

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/delete/{familyMember_id}")
    public ResponseEntity<?> deleteFamilyMember(@PathVariable Long familyMember_id){

        FamilyMember familyMember = familyMemberService.getFamilyMember(familyMember_id);
        if(familyMember==null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member doesn't exists!"));
        }

        Citizen familyMemberCitizen = familyMemberService.getFamilyMemberCitizen(familyMember_id);
        if (familyMemberCitizen==null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member citizen not found!"));
        }

        String citizenAMKA = familyMemberCitizen.getAMKA();
        String fmAMKA = familyMember.getAMKA();
        if(citizenAMKA.equals(fmAMKA)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You cannot delete the main Family Member!"));
        }

        List<FamilyMember> citizenFamilyMembers = familyMemberCitizen.getFamilyMembers();
        citizenFamilyMembers.remove(familyMember);
        citizenService.updateCitizen(familyMemberCitizen);

        Appointment familyMemberAppointment = familyMember.getAppointment();
        if(familyMemberAppointment!=null){

            //Find member's appointment
            Long appointment_id = familyMemberAppointment.getId();

            //Find appointment's doctor and removing the appointment from his/her list
            Doctor doctor = appointmentService.getAppointmentDoctor(appointment_id);
            doctor.getAppointments().remove(familyMemberAppointment);
            doctorService.updateDoctor(doctor);

            //Deleting member's appointment because we don't need it
            appointmentService.deleteAppointment(appointment_id);
        }

        //Deleting the member
        familyMemberService.deleteFamilyMember(familyMember_id);

        return ResponseEntity.ok(new MessageResponse("Family Member deleted!"));
    }

}