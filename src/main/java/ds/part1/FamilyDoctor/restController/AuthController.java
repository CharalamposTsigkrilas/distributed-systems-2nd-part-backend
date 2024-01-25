package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.entity.Citizen;
import ds.part1.FamilyDoctor.entity.Doctor;
import ds.part1.FamilyDoctor.repository.RoleRepository;
import ds.part1.FamilyDoctor.repository.UserRepository;
import ds.part1.FamilyDoctor.config.JwtUtils;
import ds.part1.FamilyDoctor.payload.request.LoginRequest;
import ds.part1.FamilyDoctor.payload.response.JwtResponseForCitizens;
import ds.part1.FamilyDoctor.payload.response.JwtResponseForDoctors;
import ds.part1.FamilyDoctor.payload.response.JwtResponseForUsers;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import ds.part1.FamilyDoctor.implementation.UserDetailsImpl;
import ds.part1.FamilyDoctor.service.CitizenService;
import ds.part1.FamilyDoctor.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    CitizenService citizenService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Long userId = userDetails.getId();

        for(String userRoles:roles){
            switch (userRoles) {
                case "ROLE_CITIZEN":

                    Citizen citizen = citizenService.getCitizen(userId);
                    return ResponseEntity.ok(new JwtResponseForCitizens(jwt,
                            citizen.getId(),
                            citizen.getFullName(),
                            citizen.getUsername(),
                            citizen.getEmail(),
                            citizen.getPhoneNumber(),
                            citizen.getDepartment(),
                            citizen.getPrefecture(),
                            roles,
                            citizen.getAMKA(),
                            citizen.getApartmentAddress(),
                            citizen.getFamilyMembers(),
                            citizen.getDoctor()));

                case "ROLE_DOCTOR":

                    Doctor doctor = doctorService.getDoctor(userId);

                    return ResponseEntity.ok(new JwtResponseForDoctors(jwt,
                            doctor.getId(),
                            doctor.getFullName(),
                            doctor.getUsername(),
                            doctor.getEmail(),
                            doctor.getPhoneNumber(),
                            doctor.getDepartment(),
                            doctor.getPrefecture(),
                            roles,
                            doctor.getSpecialty(),
                            doctor.getDoctorOfficeAddress(),
                            doctor.getRating(),
                            doctor.getAppointmentsCompleted(),
                            doctor.getMaxNumberOfCitizens(),
                            doctor.getCitizens(),
                            doctor.getAppointments()));

                case "ROLE_ADMIN":

                    return ResponseEntity.ok(new JwtResponseForUsers(jwt,
                            userDetails.getId(),
                            userDetails.getFullName(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            userDetails.getPhoneNumber(),
                            userDetails.getDepartment(),
                            userDetails.getPrefecture(),
                            roles));
            }
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
    }

}