package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.entity.Appointment;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.entity.FamilyMember;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import ds.part1.FamilyDoctor.service.AppointmentService;
import ds.part1.FamilyDoctor.service.DoctorService;
import ds.part1.FamilyDoctor.service.FamilyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @PostMapping("/new/forFamilyMember:{familyMember_id}")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment, @PathVariable Long familyMember_id ){

        //Find doctor and family member for ids given for the appointment
        FamilyMember familyMember = familyMemberService.getFamilyMember(familyMember_id);

        if(familyMember==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member doesn't exists!"));

        }

        if( (familyMember.getAppointment() != null) &&
                (appointment.getCurrentStatus().equals(Appointment.status.Set.toString()) ||
                 appointment.getCurrentStatus().equals(Appointment.status.Changed.toString())) ){

            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family Member has already an active Appointment!"));
        }

        Doctor doctor = familyMember.getCitizen().getDoctor();
        if(doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Family of this Member doesn't have a family doctor!"));
        }

        //Save doctor for appointment
        appointment.setDoctor(doctor);
        //Save family member for appointment
        appointment.setFamilyMember(familyMember);

        //Save other infos about the appointment
        appointment.setAMKA(familyMember.getAMKA());
        appointment.setDoctorName(doctor.getFullName());
        appointment.setCurrentStatus( Appointment.status.Set.toString() );
        appointment.setCustomerName( ( familyMember.getCitizen() ).getFullName() );

        appointmentService.saveAppointment(appointment);

        return ResponseEntity.ok(new MessageResponse("Appointment has been saved!"));
    }

    @GetMapping("/delete/{appointment_id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long appointment_id){

        Appointment appointment = appointmentService.getAppointment(appointment_id);
        if (appointment==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doesn't exists!"));
        }

        //Removing appointment from family member before deleting it
        Long famMember_id = (appointment.getFamilyMember()).getId();
        FamilyMember famMember = familyMemberService.getFamilyMember(famMember_id);
        famMember.setAppointment(null);

        //Removing appointment from doctor before, deleting it
        Long doctor_id = (appointment.getDoctor()).getId();
        Doctor doctor = doctorService.getDoctor(doctor_id);
        doctor.getAppointments().remove(appointment);

        appointmentService.deleteAppointment(appointment_id);

        return ResponseEntity.ok(new MessageResponse("Appointment has been successfully deleted!"));
    }

    @GetMapping("/update/{appointment_id}")
    public Appointment editAppointment(@RequestBody Appointment appointment, @PathVariable Long appointment_id){
        Appointment updatedAppointment = appointmentService.getAppointment(appointment_id);

        if (updatedAppointment==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doesn't exists!"));
        }

        updatedAppointment.setPlace( appointment.getPlace() );
        updatedAppointment.setDate( appointment.getDate() );
        updatedAppointment.setTime( appointment.getTime() );
        updatedAppointment.setCurrentStatus( Appointment.status.Changed.toString() );

        appointmentService.updateAppointment(updatedAppointment);

        //ResponseEntity.ok(new MessageResponse("Appointment has been updated!"));
        return updatedAppointment;
    }

    @GetMapping("/{appointment_id}")
    public Appointment showAppointment(@PathVariable Long appointment_id) {
        return appointmentService.getAppointment(appointment_id);
    }

    @PostMapping("/complete/{appointment_id}")
    public ResponseEntity<?> completedAppointment(@RequestBody int appointmentGrade, @PathVariable Long appointment_id){
        Appointment appointment = appointmentService.getAppointment(appointment_id);

        if (appointment==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doesn't exists!"));
        }
        appointment.setCurrentStatus(Appointment.status.Completed.toString());

        //After the appointment is done we set a grade for it.
        appointment.setEvaluationGrade(appointmentGrade);

        //Also we add it to doctor's rating
        Doctor doctor = appointment.getDoctor();

        float rating = doctor.getRating();
        int totalAppointmentsNow = doctor.getAppointmentsCompleted()+1;
        doctor.setAppointmentsCompleted(totalAppointmentsNow);

        rating = (rating+appointmentGrade)/(totalAppointmentsNow);

        doctor.setRating(rating);

        return ResponseEntity.ok(new MessageResponse("Appointment has been completed successfully!"));
    }

    @PostMapping("/cancel/{appointment_id}")
    public ResponseEntity<?> canceledAppointment(@PathVariable Long appointment_id){
        Appointment appointment = appointmentService.getAppointment(appointment_id);

        if (appointment==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Appointment doesn't exists!"));
        }
        appointment.setCurrentStatus(Appointment.status.Canceled.toString());

        return ResponseEntity.ok(new MessageResponse("Appointment has been canceled successfully!"));
    }
}