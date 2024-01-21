package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.config.JwtUtils;
import ds.part1.FamilyDoctor.entity.*;
import ds.part1.FamilyDoctor.extraClasses.ExtraMethods;
import ds.part1.FamilyDoctor.implementation.DoctorDetailsImpl;
import ds.part1.FamilyDoctor.payload.request.LoginRequest;
import ds.part1.FamilyDoctor.payload.response.JwtResponseForDoctors;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import ds.part1.FamilyDoctor.repository.UserRepository;
import ds.part1.FamilyDoctor.service.AppointmentService;
import ds.part1.FamilyDoctor.service.CitizenService;
import ds.part1.FamilyDoctor.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Secured("ROLE_ADMIN")
    @PostMapping("/signup")
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
        doctor.setAppointmentsCompleted(0);
        doctor.setRating(0);
        doctorService.saveDoctor(doctor);
        return ResponseEntity.ok(new MessageResponse("Doctor saved!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> autheticateDoctor(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        DoctorDetailsImpl doctorDetails = (DoctorDetailsImpl) authentication.getPrincipal();
        List<String> roles = doctorDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseForDoctors(jwt,
                doctorDetails.getId(),
                doctorDetails.getFullName(),
                doctorDetails.getUsername(),
                doctorDetails.getEmail(),
                doctorDetails.getPhoneNumber(),
                doctorDetails.getDepartment(),
                doctorDetails.getPrefecture(),
                roles,
                doctorDetails.getSpecialty(),
                doctorDetails.getDoctorOfficeAddress(),
                doctorDetails.getRating(),
                doctorDetails.getAppointmentsCompleted(),
                doctorDetails.getMaxNumberOfCitizens(),
                doctorDetails.getCitizens(),
                doctorDetails.getAppointments()));

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/update/{doctor_id}")
    public Doctor editDoctor(@RequestBody Doctor doctor, @PathVariable Long doctor_id){
        Doctor updatedDoctor = doctorService.getDoctor(doctor_id);

        if(updatedDoctor==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
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
        //ResponseEntity.ok(new MessageResponse("Doctor saved!"));
        return updatedDoctor;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/delete/{doctor_id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long doctor_id){
        Doctor doctor = doctorService.getDoctor(doctor_id);

        if (doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }

        Set<Role> userRoles = doctor.getRoles();
        for (Role role : userRoles){
            if (role.getName().equals("ROLE_ADMIN") && !ExtraMethods.adminCheck()){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: This user can't be deleted because is the last admin in the system!"));
            }
        }

        //Remove doctor from all the citizens before deleting doctor
        List<Citizen> customers = doctorService.getDoctorCitizens(doctor_id);
        for(Citizen cit : customers){

            //Remove doctor from each citizen
            cit.setDoctor(null);
//            customers.remove(cit);
        }

        //Remove appointments from fam members
        List<Appointment> doctorAppointments = doctor.getAppointments();
        for(Appointment appo : doctorAppointments){

            FamilyMember famMembersOfAppo = appo.getFamilyMember();
            famMembersOfAppo.setAppointment(null);

            //Delete appointment
//            appo.setDoctor(null);
//            appo.setFamilyMember(null);
//            doctorAppointments.remove(appo);
            appointmentService.deleteAppointment(appo.getId());
        }

        //Finally delete doctor
        doctorService.deleteDoctor(doctor_id);
        return ResponseEntity.ok(new MessageResponse("Doctor has been successfully deleted!"));

    }

    @GetMapping("/citizens/forDoctor:{doctor_id}")
    public List<Citizen> showCustomers(@PathVariable Long doctor_id){
        Doctor doctor = doctorService.getDoctor(doctor_id);

        List<Citizen> customers = null;
        if(doctor==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }else{
            customers = doctorService.getDoctorCitizens(doctor_id);
        }
        return customers;
    }

    @GetMapping("/appointments/forDoctor:{doctor_id}")
    public List<Appointment> showDoctorAppointments(@PathVariable Long doctor_id){
        Doctor doctor = doctorService.getDoctor(doctor_id);

        List<Appointment> appointments = null;
        if(doctor==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }else{
            appointments = doctorService.getDoctorAppointments(doctor_id);
        }
        return appointments;
    }

    @PostMapping("/remove/fromDoctor:{doctor_id}/citizen:{citizen_id}")
    public ResponseEntity<?> removeCitizen(@PathVariable Long citizen_id, @PathVariable Long doctor_id){

        Doctor doctor = doctorService.getDoctor(doctor_id);
        Citizen citizen = citizenService.getCitizen(citizen_id);

        if(doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }else if(citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        if(!doctor.getCitizens().contains(citizen)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't have this Citizen on his list!"));
        }

        //First remove the citizen from Doctor's Citizen list
        citizen.getDoctor().getCitizens().remove(citizen);

        //Then removing the doctor from Citizen
        citizen.setDoctor(null);

        return ResponseEntity.ok(new MessageResponse("Citizen \"+citizen.getFullName()+\" has been removed!"));
    }

    @PostMapping("/accept/request/fromDoctor:{doctor_id}/forCitizen:{citizen_id}")
    public ResponseEntity<?> setFamilyDoctor(@PathVariable Long citizen_id, @PathVariable Long doctor_id){
        Doctor doctor = doctorService.getDoctor(doctor_id);
        Citizen citizen = citizenService.getCitizen(citizen_id);

        if(doctor==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor doesn't exists!"));
        }else if(citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        if (citizen.getDoctor()!=null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen already has a family doctor!"));
        }

        citizen.setDoctor(doctor);
        doctor.getCitizens().add(citizen);

        return ResponseEntity.ok(new MessageResponse("Doctor \"+ doctor.getFullName()+\" has been set as a family doctor" +
                " for citizen \"+citizen.getFullName()+\" !"));
    }

}