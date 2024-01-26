package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.entity.*;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import ds.part1.FamilyDoctor.repository.UserRepository;
import ds.part1.FamilyDoctor.service.AppointmentService;
import ds.part1.FamilyDoctor.config.JwtUtils;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import ds.part1.FamilyDoctor.service.CitizenService;
import ds.part1.FamilyDoctor.service.DoctorService;
import ds.part1.FamilyDoctor.service.FamilyMemberService;
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
        String birthDatesString = citizen.getAMKA().substring(0, 6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        LocalDate birthDate = LocalDate.parse(birthDatesString, formatter);

        LocalDate currentDate = LocalDate.now();
        long age = ChronoUnit.YEARS.between(birthDate, currentDate);

        //Due to this date format 'ddMMyy', we can't calculate users that born before 2000.
        //So we add a 19 in years so format can be completed: 'ddMMyyyy' (ddMM19yy)
        if(age<0){
            birthDatesString=birthDatesString.substring(0, 4)+"19"+birthDatesString.substring(4);
            formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            birthDate = LocalDate.parse(birthDatesString, formatter);

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
    @GetMapping("/update/{citizen_id}")
    public Citizen editCitizen(@RequestBody Citizen citizen, @PathVariable Long citizen_id){
        Citizen updatedCitizen = citizenService.getCitizen(citizen_id);

        if(updatedCitizen==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        updatedCitizen.setFullName(citizen.getFullName());
        updatedCitizen.setUsername(citizen.getUsername());
        updatedCitizen.setEmail(citizen.getEmail());
        updatedCitizen.setPhoneNumber(citizen.getPhoneNumber());
        updatedCitizen.setDepartment(citizen.getDepartment());
        updatedCitizen.setPrefecture(citizen.getPrefecture());

        updatedCitizen.setAMKA(citizen.getAMKA());
        updatedCitizen.setApartmentAddress(citizen.getApartmentAddress());

        citizenService.updateCitizen(updatedCitizen);
        //ResponseEntity.ok(new MessageResponse("Citizen saved!"));
        return updatedCitizen;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/delete/{citizen_id}")
    public ResponseEntity<?> deleteCitizen(@PathVariable Long citizen_id){
        Citizen citizen = citizenService.getCitizen(citizen_id);

        if (citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        //Delete all the family members before deleting citizen
        List<FamilyMember> family = citizenService.getCitizenFamilyMembers(citizen_id);
        for(FamilyMember member : family){

            if(member.getAppointment()!=null){
                Appointment memberAppointment = member.getAppointment();
                Doctor doctorOfAppointment = memberAppointment.getDoctor();
                List<Appointment> appointmentsOfDoctor = doctorOfAppointment.getAppointments();

                //Remove Appointment of each member from doctors
                appointmentsOfDoctor.remove(memberAppointment);

                //Delete members' appointments
//                memberAppointment.setDoctor(null);
//                memberAppointment.setFamilyMember(null);
                appointmentService.deleteAppointment(memberAppointment.getId());
            }

            //Delete family member
//            member.setCitizen(null);
//            member.setAppointment(null);
            familyMemberService.deleteFamilyMember(member.getId());
        }

        //Remove citizen from doctor
        Doctor familyDoctor = citizen.getDoctor();
        List<Citizen> doctorCitizens = familyDoctor.getCitizens();
        doctorCitizens.remove(citizen);

        //Finally delete citizen
//        citizen.setDoctor(null);
        citizenService.deleteCitizen(citizen_id);
        return ResponseEntity.ok(new MessageResponse("Citizen has been successfully deleted! " +
                "Family members of them and their appointments also got deleted!"));

    }

    @GetMapping("/family/forCitizen:{citizen_id}")
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

    @GetMapping("/family/doctor/forCitizen:{citizen_id}")
    public Doctor showDoctor(@PathVariable Long citizen_id){
        Citizen citizen = citizenService.getCitizen(citizen_id);

        if(citizen==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        Doctor doctor = citizenService.getCitizenDoctor(citizen_id);

        if(doctor==null){
            ResponseEntity.badRequest().body(new MessageResponse("Citizen doesn't have a family doctor!"));
        }
        return doctor;
    }

    @GetMapping("/nearby/doctors/forCitizen:{citizen_id}")
    public List<Doctor> showNearbyDoctors(@PathVariable Long citizen_id){

        Citizen citizen = citizenService.getCitizen(citizen_id);

        if(citizen==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        //Checking all the doctors in the same prefecture as Citizen and add them in list that we return
        List<Doctor> allDoctors = doctorService.getDoctors();
        List<Doctor> nearbyDoctors = null;
        for (Doctor currDoc: allDoctors){
            if(citizen.getPrefecture().equals(currDoc.getPrefecture())){
                nearbyDoctors.add(currDoc);
            }
        }

        //If there are no doctors in the same prefecture we search doctors in the same department
        if (nearbyDoctors==null){
            for (Doctor currDoc: allDoctors){
                if(citizen.getDepartment().equals(currDoc.getDepartment())){
                    nearbyDoctors.add(currDoc);
                }
            }
        }

        return nearbyDoctors;
    }

    @PostMapping("/request/fromCitizen:{citizen_id}/toDoctor:{doctor_id}")
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

        return ResponseEntity.ok(new MessageResponse("Doctor "+ doctor.getFullName()+" has been set as a family doctor" +
                " for citizen "+citizen.getFullName()+" !"));
    }

    @PostMapping("/remove/family/doctor/fromCitizen:{citizen_id}")
    public ResponseEntity<?> removeDoctor(@PathVariable Long citizen_id){
        Citizen citizen = citizenService.getCitizen(citizen_id);

        if(citizen==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't exists!"));
        }

        if (citizen.getDoctor()==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen doesn't have a family doctor!"));
        }

        //First remove the citizen from Doctor's Citizen list
        citizen.getDoctor().getCitizens().remove(citizen);

        //Then removing the doctor from Citizen
        citizen.setDoctor(null);

        return ResponseEntity.ok(new MessageResponse("Doctor has been removed from citizen "+citizen.getFullName()+" !"));
    }

}