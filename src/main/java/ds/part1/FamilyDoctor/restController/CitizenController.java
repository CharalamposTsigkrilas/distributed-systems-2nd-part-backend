package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.entity.*;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import ds.part1.FamilyDoctor.repository.UserRepository;
import ds.part1.FamilyDoctor.service.*;
import ds.part1.FamilyDoctor.config.JwtUtils;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/citizen")
public class CitizenController {

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/{citizen_id}")
    public Citizen getCitizen(@PathVariable Long citizen_id){
        return citizenService.getCitizen(citizen_id);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("")
    public List<Citizen> getCitizens(){
        return citizenService.getCitizens();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/new")
    public ResponseEntity<?> registerCitizen(@Valid @RequestBody Citizen citizen){

        if (userRepository.existsByUsername(citizen.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(citizen.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        citizen.setPassword(encoder.encode(citizen.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        Role citizenRole = roleRepository.findByName("ROLE_CITIZEN")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(citizenRole);

        List<Citizen> allCitizens = citizenService.getCitizens();
        for(Citizen currCiti : allCitizens){
            if (currCiti.getAMKA().equals(citizen.getAMKA())){
                return ResponseEntity.badRequest().body(new MessageResponse("Citizen with this AMKA already exists!"));
            }
        }

        //Checking if the user is an adult by taking the 6 first numbers of AMKA and compare them with system's Date
        String birthDateString = citizen.getAMKA().substring(0, 6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);

        LocalDate currentDate = LocalDate.now();
        long age = ChronoUnit.YEARS.between(birthDate, currentDate);

        //Due to this date format 'ddMMyy', we can't calculate users that born before 2000.
        //So we add a 19 in years so format can be completed: 'ddMMyyyy' (ddMM19yy)
        if(age<0){
            birthDateString=birthDateString.substring(0, 4)+"19"+birthDateString.substring(4);
            formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            birthDate = LocalDate.parse(birthDateString, formatter);

            currentDate = LocalDate.now();
            age = ChronoUnit.YEARS.between(birthDate, currentDate);
        }

        if(age<18){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen must be an adult to register!"));
        }

        citizen.setRoles(roles);
        citizenService.saveCitizen(citizen);
        return ResponseEntity.ok(new MessageResponse("Citizen saved!"));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{citizen_id}/update")
    public ResponseEntity<?> editCitizen(@Valid @RequestBody Citizen citizen, @PathVariable Long citizen_id){
        Citizen updatedCitizen = citizenService.getCitizen(citizen_id);

        if (updatedCitizen == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        Doctor citizenDoctor = citizenService.getCitizenDoctor(citizen_id);


        updatedCitizen.setFullName(citizen.getFullName());
        updatedCitizen.setUsername(citizen.getUsername());
        updatedCitizen.setEmail(citizen.getEmail());
        updatedCitizen.setPhoneNumber(citizen.getPhoneNumber());
        updatedCitizen.setDepartment(citizen.getDepartment());
        updatedCitizen.setPrefecture(citizen.getPrefecture());

        updatedCitizen.setAMKA(citizen.getAMKA());
        updatedCitizen.setApartmentAddress(citizen.getApartmentAddress());

        if( citizenDoctor != null ) {
            citizenDoctor.getCitizens().remove(citizen);
            citizenDoctor.getCitizens().add(updatedCitizen);
            doctorService.updateDoctor(citizenDoctor);
        }

        citizenService.updateCitizen(updatedCitizen);
        return ResponseEntity.ok(new MessageResponse("Citizen updated!"));

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{citizen_id}/delete")
    public ResponseEntity<?> deleteCitizen(@PathVariable Long citizen_id){
        Citizen citizen = citizenService.getCitizen(citizen_id);

        if (citizen == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        //Delete all the family members before deleting citizen
        List<FamilyMember> family = citizenService.getCitizenFamilyMembers(citizen_id);
        for(FamilyMember familyMember : family){

            if (familyMember.getAppointment() != null) {
                Appointment familyMemberAppointment = familyMember.getAppointment();

                Long familyMemberAppointmentId = familyMemberAppointment.getId();
                Doctor appointmentDoctor = appointmentService.getAppointmentDoctor(familyMemberAppointmentId);

                //Remove Appointment of each member from doctors
                appointmentDoctor.getAppointments().remove(familyMemberAppointment);
                doctorService.updateDoctor(appointmentDoctor);

                familyMember.setAppointment(null);
                appointmentService.deleteAppointment(familyMemberAppointmentId);
            }

            //Remove family member from citizen
            citizen.getFamilyMembers().remove(familyMember);
            citizenService.updateCitizen(citizen);

            //Delete family member
            familyMemberService.deleteFamilyMember(familyMember.getId());
        }

        //Remove citizen from doctor
        Doctor citizenDoctor = citizenService.getCitizenDoctor(citizen_id);
        if (citizenDoctor != null) {
            citizenDoctor.getCitizens().remove(citizen);
            doctorService.updateDoctor(citizenDoctor);
        }

        Request citizenRequest = citizenService.getCitizenRequest(citizen_id);
        if (citizenRequest != null) {
            citizen.setRequest(null);
            citizenService.updateCitizen(citizen);

            Long citizenRequestId = citizenRequest.getId();
            Doctor requestDoctor = requestService.getRequestDoctor(citizenRequestId);
            requestDoctor.getRequests().remove(citizenRequest);
            doctorService.updateDoctor(requestDoctor);

            requestService.deleteRequest(citizenRequestId);
        }

        //Finally delete citizen
        citizenService.deleteCitizen(citizen_id);
        return ResponseEntity.ok(new MessageResponse("Citizen has been successfully deleted! " +
                "Family members of them and their appointments also got deleted!"));

    }

    @GetMapping("/{citizen_id}/family")
    public List<FamilyMember> showFamily(@PathVariable Long citizen_id){
        return citizenService.getCitizenFamilyMembers(citizen_id);
    }

    @GetMapping("/{citizen_id}/doctor")
    public Doctor showDoctor(@PathVariable Long citizen_id){
        return citizenService.getCitizenDoctor(citizen_id);
    }

    @GetMapping("/{citizen_id}/nearby/doctors")
    public List<Doctor> showNearbyDoctors(@PathVariable Long citizen_id){

        Citizen citizen = citizenService.getCitizen(citizen_id);

        //Checking all the doctors in the same prefecture as Citizen and add them in list that we return
        List<Doctor> allDoctors = doctorService.getDoctors();
        List<Doctor> nearbyDoctors = new ArrayList<>();
        for (Doctor currDoc: allDoctors){
            if(citizen.getPrefecture().equals(currDoc.getPrefecture())){
                nearbyDoctors.add(currDoc);
            }
        }

        //If there are no doctors in the same prefecture we search doctors in the same department
        if (nearbyDoctors.isEmpty()){
            for (Doctor currDoc: allDoctors){
                if(citizen.getDepartment().equals(currDoc.getDepartment())){
                    nearbyDoctors.add(currDoc);
                }
            }
        }

        return nearbyDoctors;
    }

    @PostMapping("/{citizen_id}/remove/family/doctor")
    public ResponseEntity<?> removeDoctor(@PathVariable Long citizen_id){

        Citizen citizen = citizenService.getCitizen(citizen_id);
        if (citizen == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        Doctor citizenDoctor = citizenService.getCitizenDoctor(citizen_id);
        if (citizenDoctor == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't have a family doctor!"));
        }

        //Remove the citizen from Doctor's Citizen list
        citizenDoctor.getCitizens().remove(citizen);
        doctorService.saveDoctor(citizenDoctor);

        List<FamilyMember> family = citizenService.getCitizenFamilyMembers(citizen_id);
        for(FamilyMember familyMember : family){

            if (familyMember.getAppointment() != null) {
                Appointment familyMemberAppointment = familyMember.getAppointment();

                Long familyMemberAppointmentId = familyMemberAppointment.getId();
                Doctor appointmentDoctor = appointmentService.getAppointmentDoctor(familyMemberAppointmentId);

                //Remove Appointment of each member from doctors
                appointmentDoctor.getAppointments().remove(familyMemberAppointment);
                doctorService.updateDoctor(appointmentDoctor);

                familyMember.setAppointment(null);

                familyMemberService.updateFamilyMember(familyMember);
                appointmentService.deleteAppointment(familyMemberAppointmentId);
            }

        }

        return ResponseEntity.ok(new MessageResponse("Doctor has been removed from citizen "+citizen.getFullName()+" !"));
    }

}