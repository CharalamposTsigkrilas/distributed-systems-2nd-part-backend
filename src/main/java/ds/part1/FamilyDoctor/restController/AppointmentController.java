package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.service.AppointmentService;
import ds.part1.FamilyDoctor.entity.Appointment;
import ds.part1.FamilyDoctor.entity.FamilyMember;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import ds.part1.FamilyDoctor.service.CitizenService;
import ds.part1.FamilyDoctor.service.DoctorService;
import ds.part1.FamilyDoctor.service.FamilyMemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @GetMapping("/{appointment_id}")
    public Appointment getAppointment(@PathVariable Long appointment_id) {
        return appointmentService.getAppointment(appointment_id);
    }

    @GetMapping("")
    public List<Appointment> getAppointments(){
        return appointmentService.getAppointments();
    }

    @PostMapping("/new/for/FamilyMember/{familyMember_id}")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment, @PathVariable Long familyMember_id ){

        //Find doctor and family member for ids given for the appointment
        FamilyMember familyMember = familyMemberService.getFamilyMember(familyMember_id);
        if( familyMember==null ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member doesn't exists!"));
        }

        if( familyMemberService.getFamilyMemberAppointment(familyMember_id) != null ){
            if ((appointment.getCurrentStatus().equals(Appointment.status.Set.toString()) ||
                 appointment.getCurrentStatus().equals(Appointment.status.Changed.toString()))) {

                return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member has already an active Appointment!"));
            }
        }

        Citizen citizen = familyMemberService.getFamilyMemberCitizen(familyMember_id);

        if (citizen==null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member Citizen not found!"));
        }

        Doctor doctor = familyMemberService.getFamilyMemberDoctor(familyMember_id);

        if(doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family of this Member doesn't have a family doctor!"));
        }

        //Save other infos about the appointment
        appointment.setAMKA(familyMember.getAMKA());
        appointment.setDoctorName(doctor.getFullName());
        appointment.setCustomerName(citizen.getFullName());

        appointmentService.saveAppointment(appointment);

        familyMember.setAppointment(appointment);
        familyMemberService.updateFamilyMember(familyMember);

        doctor.getAppointments().add(appointment);
        doctorService.updateDoctor(doctor);

        return ResponseEntity.ok(new MessageResponse("Appointment has been saved!"));
    }

    @PostMapping("/{appointment_id}/delete")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long appointment_id){

        Appointment appointment = appointmentService.getAppointment(appointment_id);
        if (appointment == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doesn't exists!"));
        }

        //Removing appointment from family member before deleting it
        FamilyMember appointmentFamilyMember = appointmentService.getAppointmentFamilyMember(appointment_id);
        if (appointmentFamilyMember == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment family member not found!"));
        }

        appointmentFamilyMember.setAppointment(null);
        familyMemberService.updateFamilyMember(appointmentFamilyMember);

        //Removing appointment from doctor before, deleting it
        Doctor appointmentDoctor = appointmentService.getAppointmentDoctor(appointment_id);
        if (appointmentDoctor == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doctor not found!"));
        }

        appointmentDoctor.getAppointments().remove(appointment);
        doctorService.updateDoctor(appointmentDoctor);

        appointmentService.deleteAppointment(appointment_id);

        return ResponseEntity.ok(new MessageResponse("Appointment has been successfully deleted!"));
    }

    @PostMapping("/{appointment_id}/update")
    public ResponseEntity<?> editAppointment(@Valid @RequestBody Appointment appointment, @PathVariable Long appointment_id){

        Appointment updatedAppointment = appointmentService.getAppointment(appointment_id);
        if (updatedAppointment == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doesn't exists!"));
        }
        if ( appointment.getCurrentStatus().equals( Appointment.status.Completed.toString() ) ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Completed appointment cannot be changed!"));
        }
        if ( appointment.getCurrentStatus().equals( Appointment.status.Canceled.toString() ) ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Canceled appointment cannot be changed!"));
        }

        Doctor appointmentDoctor = appointmentService.getAppointmentDoctor(appointment_id);
        if (appointmentDoctor == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doctor not found!"));
        }

        FamilyMember appointmentFamilyMember = appointmentService.getAppointmentFamilyMember(appointment_id);
        if (appointmentFamilyMember == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment family Member not found!"));
        }

        appointmentDoctor.getAppointments().remove(appointment);

        updatedAppointment.setPlace( appointment.getPlace() );
        updatedAppointment.setDate( appointment.getDate() );
        updatedAppointment.setTime( appointment.getTime() );
        updatedAppointment.setCurrentStatus( Appointment.status.Changed.toString() );

        appointmentDoctor.getAppointments().add(updatedAppointment);
        doctorService.updateDoctor(appointmentDoctor);

        familyMemberService.updateFamilyMember(appointmentFamilyMember);

        appointmentService.updateAppointment(updatedAppointment);

        return ResponseEntity.ok(new MessageResponse("Appointment has been updated!"));
    }

    @PostMapping("/{appointment_id}/complete")
    public ResponseEntity<?> completedAppointment(@RequestBody int appointmentGrade, @PathVariable Long appointment_id){

        Appointment appointment = appointmentService.getAppointment(appointment_id);
        if (appointment == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doesn't exists!"));
        }
        if ( appointment.getCurrentStatus().equals( Appointment.status.Completed.toString() ) ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment has already been completed!"));
        }
        if ( appointment.getCurrentStatus().equals( Appointment.status.Canceled.toString() ) ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Canceled appointment cannot be completed!"));
        }

        Doctor appointmentDoctor = appointmentService.getAppointmentDoctor(appointment_id);
        if (appointmentDoctor == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doctor not found!"));
        }

        FamilyMember appointmentFamilyMember = appointmentService.getAppointmentFamilyMember(appointment_id);
        if (appointmentFamilyMember == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment family Member not found!"));
        }

        List<Appointment> doctorAppointments = appointmentDoctor.getAppointments();
        doctorAppointments.remove(appointment);

        appointment.setCurrentStatus(Appointment.status.Completed.toString());

        //After the appointment is done we set a grade for it.
        appointment.setEvaluationGrade(appointmentGrade);

        float rating = appointmentDoctor.getRating();

        int totalAppointmentsNow = appointmentDoctor.getAppointmentsCompleted() + 1;
        appointmentDoctor.setAppointmentsCompleted(totalAppointmentsNow);

        rating = (rating+appointmentGrade)/(totalAppointmentsNow);

        appointmentDoctor.setRating(rating);

        doctorAppointments.add(appointment);
        doctorService.updateDoctor(appointmentDoctor);

        familyMemberService.updateFamilyMember(appointmentFamilyMember);

        appointmentService.updateAppointment(appointment);

        return ResponseEntity.ok(new MessageResponse("Appointment has been completed successfully!"));
    }

    @PostMapping("/{appointment_id}/cancel")
    public ResponseEntity<?> canceledAppointment(@PathVariable Long appointment_id){
        Appointment appointment = appointmentService.getAppointment(appointment_id);
        if (appointment == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doesn't exists!"));
        }
        if ( appointment.getCurrentStatus().equals( Appointment.status.Completed.toString() ) ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Completed Appointment cannot be canceled!"));
        }
        if ( appointment.getCurrentStatus().equals( Appointment.status.Canceled.toString() ) ) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment has already been canceled!"));
        }

        Doctor appointmentDoctor = appointmentService.getAppointmentDoctor(appointment_id);
        if (appointmentDoctor == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doctor not found!"));
        }

        FamilyMember appointmentFamilyMember = appointmentService.getAppointmentFamilyMember(appointment_id);
        if (appointmentFamilyMember == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment family Member not found!"));
        }

        appointmentDoctor.getAppointments().remove(appointment);

        appointment.setCurrentStatus(Appointment.status.Canceled.toString());

        appointmentDoctor.getAppointments().add(appointment);
        doctorService.updateDoctor(appointmentDoctor);

        familyMemberService.updateFamilyMember(appointmentFamilyMember);

        appointmentService.updateAppointment(appointment);


        return ResponseEntity.ok(new MessageResponse("Appointment has been canceled successfully!"));
    }
}