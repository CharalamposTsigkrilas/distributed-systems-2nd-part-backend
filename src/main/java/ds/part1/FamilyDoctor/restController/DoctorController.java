package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.entity.*;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import ds.part1.FamilyDoctor.repository.UserRepository;
import ds.part1.FamilyDoctor.service.*;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/{doctor_id}")
    public Doctor getDoctor(@PathVariable Long doctor_id){
        return doctorService.getDoctor(doctor_id);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("")
    public List<Doctor> getDoctors(){
        return doctorService.getDoctors();
    }

    @GetMapping("/{doctor_id}/requests")
    public List<Request> getDoctorRequests(@PathVariable Long doctor_id){
        List<Request> requests = doctorService.getDoctorRequests(doctor_id);
        if(requests.isEmpty()){
            return null;
        }
        return requests;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/new")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody Doctor doctor){

        if (userRepository.existsByUsername(doctor.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(doctor.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        doctor.setPassword(encoder.encode(doctor.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        Role doctorRole = roleRepository.findByName("ROLE_DOCTOR")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(doctorRole);

        doctor.setRoles(roles);
        doctorService.saveDoctor(doctor);
        return ResponseEntity.ok(new MessageResponse("Doctor saved!"));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{doctor_id}/update")
    public ResponseEntity<?> editDoctor(@Valid @RequestBody Doctor doctor, @PathVariable Long doctor_id){

        Doctor updatedDoctor = doctorService.getDoctor(doctor_id);
        if(updatedDoctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }

        updatedDoctor.setFullName(doctor.getFullName());
        updatedDoctor.setUsername(doctor.getUsername());
        updatedDoctor.setEmail(doctor.getEmail());
        updatedDoctor.setPhoneNumber(doctor.getPhoneNumber());
        updatedDoctor.setDepartment(doctor.getDepartment());
        updatedDoctor.setPrefecture(doctor.getPrefecture());

        updatedDoctor.setSpecialty(doctor.getSpecialty());
        updatedDoctor.setDoctorOfficeAddress(doctor.getDoctorOfficeAddress());
        updatedDoctor.setRating(doctor.getRating());
        updatedDoctor.setAppointmentsCompleted(doctor.getAppointmentsCompleted());
        updatedDoctor.setMaxNumberOfCitizens(doctor.getMaxNumberOfCitizens());

        doctorService.updateDoctor(updatedDoctor);
        return ResponseEntity.ok(new MessageResponse("Doctor updated!"));

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{doctor_id}/delete")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long doctor_id){

        Doctor doctor = doctorService.getDoctor(doctor_id);
        if (doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }

        //Remove appointments from fam members
        List<Appointment> doctorAppointments = doctorService.getDoctorAppointments(doctor_id);
        for(Appointment currentAppointment : doctorAppointments){
            Long appointmentId = currentAppointment.getId();
            FamilyMember appointmentFamilyMember = appointmentService.getAppointmentFamilyMember(appointmentId);

            //Remove appointment from family member
            appointmentFamilyMember.setAppointment(null);
            familyMemberService.updateFamilyMember(appointmentFamilyMember);

            //Delete doctor appointment
            doctorAppointments.remove(currentAppointment);
            doctorService.updateDoctor(doctor);

            appointmentService.deleteAppointment(appointmentId);
        }

        List<Request> doctorRequests = doctorService.getDoctorRequests(doctor_id);
        for (Request currentRequest : doctorRequests) {
            Long currentRequestId = currentRequest.getId();
            Citizen requestCitizen = requestService.getRequestCitizen(currentRequestId);

            requestCitizen.setRequest(null);
            citizenService.updateCitizen(requestCitizen);

            doctorRequests.remove(currentRequest);
            doctorService.updateDoctor(doctor);

            requestService.deleteRequest(currentRequestId);
        }

        //Finally delete doctor
        doctorService.deleteDoctor(doctor_id);
        return ResponseEntity.ok(new MessageResponse("Doctor has been successfully deleted!"));

    }

    @GetMapping("/{doctor_id}/citizens")
    public List<Citizen> showCustomers(@PathVariable Long doctor_id){
        List<Citizen> citizens = doctorService.getDoctorCitizens(doctor_id);
        if(citizens.isEmpty()){
            return null;
        }
        return citizens;
    }

    @GetMapping("/{doctor_id}/appointments")
    public List<Appointment> showDoctorAppointments(@PathVariable Long doctor_id){
        List<Appointment> appointments = doctorService.getDoctorAppointments(doctor_id);
        if (appointments.isEmpty()) {
            return null;
        }
        return appointments;
    }

    @PostMapping("/{doctor_id}/remove/citizen/{citizen_id}")
    public ResponseEntity<?> removeCitizen(@PathVariable Long citizen_id, @PathVariable Long doctor_id){

        Doctor doctor = doctorService.getDoctor(doctor_id);
        Citizen citizen = citizenService.getCitizen(citizen_id);

        if(doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }
        if(citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        List<Citizen> doctorCitizens = doctorService.getDoctorCitizens(doctor_id);
        if(!doctorCitizens.contains(citizen)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't have this Citizen on his list!"));
        }

        //Remove the citizen from Doctor's Citizen list
        doctorCitizens.remove(citizen);
        doctorService.updateDoctor(doctor);

        List<FamilyMember> citizenFamilyMembersFamilyMember = citizenService.getCitizenFamilyMembers(citizen_id);
        for(FamilyMember familyMember : citizenFamilyMembersFamilyMember){

            if (familyMember.getAppointment() != null) {
                Appointment familyMemberAppointment = familyMember.getAppointment();

                Long familyMemberAppointmentId = familyMemberAppointment.getId();

                //Remove Appointment of each member from doctors
                doctor.getAppointments().remove(familyMemberAppointment);
                doctorService.updateDoctor(doctor);

                familyMember.setAppointment(null);

                familyMemberService.updateFamilyMember(familyMember);
                appointmentService.deleteAppointment(familyMemberAppointmentId);
            }

        }

        return ResponseEntity.ok(new MessageResponse("Citizen "+citizen.getFullName()+" has been removed!"));
    }

}