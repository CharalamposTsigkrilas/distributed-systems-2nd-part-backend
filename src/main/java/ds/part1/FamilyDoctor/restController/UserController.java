package ds.part1.FamilyDoctor.restController;

import ds.part1.FamilyDoctor.entity.User;
import ds.part1.FamilyDoctor.entity.Role;
import ds.part1.FamilyDoctor.extraClasses.ExtraMethods;
import ds.part1.FamilyDoctor.payload.response.MessageResponse;
import ds.part1.FamilyDoctor.repository.UserRepository;
import ds.part1.FamilyDoctor.service.RoleService;
import ds.part1.FamilyDoctor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    //Sign-in and Sign-up are in AuthController

    @GetMapping("/users")
    public List<User> showUsers(){
        List<User> users = userService.getUsers();
        return users;
    }

    @GetMapping("/{user_id}")
    public User showUser(@PathVariable Long user_id){
        User user = userService.getUser(user_id);
        if(user==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }
        return user;
    }

//    @GetMapping("/role/add/role:{role_id}/toUser:{user_id}")
//    public User addRoletoUser(@PathVariable Long user_id, @PathVariable Long role_id){
//        User user = userService.getUser(user_id);
//        Role role = roleService.getRole(role_id);
//
//        if(user==null){
//            ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
//        }else if(role==null){
//            ResponseEntity.badRequest().body(new MessageResponse("Error: Role not found!"));
//        }else{
//            user.getRoles().add(role);
//            System.out.println("User's '"+user.getUsername()+"' roles after adding the role '"+role.getName()+"' : "+user.getRoles());
//            userService.updateUser(user);
//            ResponseEntity.ok(new MessageResponse("Role added to user!"));
//        }
//
//        return userService.getUser(user_id);
//
//    }
//
//    @GetMapping("/role/delete/role:{role_id}/fromUser:{user_id}")
//    public User deleteRolefromUser(@PathVariable Long user_id, @PathVariable Long role_id){
//        User user = userService.getUser(user_id);
//        Role role = roleService.getRole(role_id);
//
//        if(user==null){
//            ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
//        }else if(role==null){
//            ResponseEntity.badRequest().body(new MessageResponse("Error: Role not found!"));
//        }else{
//            if (role.getName().equals("ROLE_ADMIN") && !ExtraMethods.adminCheck()){
//                ResponseEntity.badRequest().body(new MessageResponse("Error: At least one admin must be in the system!"));
//            }else{
//                user.getRoles().remove(role);
//                System.out.println("User '"+user.getUsername()+"' roles after deleting the role '"+role.getName()+"' : "+user.getRoles());
//                userService.updateUser(user);
//                ResponseEntity.ok(new MessageResponse("Role deleted from user!"));
//            }
//        }
//
//        return userService.getUser(user_id);
//
//    }

    @GetMapping("/change/infos/{user_id}")
    public User editUser(@RequestBody User user, @PathVariable Long user_id){
        User updatedUser = userService.getUser(user_id);

        if(updatedUser==null){
            ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }else{
            updatedUser.setFullName(user.getFullName());
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPhoneNumber(user.getPhoneNumber());

            userService.updateUser(updatedUser);
            ResponseEntity.ok(new MessageResponse("User has been updated!"));
        }

        return userService.getUser(user_id);
    }

    @PostMapping("/delete/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long user_id){
        User user = userService.getUser(user_id);
        if (user==null){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User doesn't exists!"));
        }

        Set<Role> userRoles = user.getRoles();
        for (Role role : userRoles){
            String roleName = role.getName();
            switch (roleName){
                case "ROLE_CITIZEN" :
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Citizen can't be deleted from here!"));
                case "ROLE_DOCTOR" :
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Doctor can't be deleted from here!"));
                case "ROLE_ADMIN" :
                    if(!ExtraMethods.adminCheck()) {
                        return ResponseEntity.badRequest().body(new MessageResponse("Error: This user can't be deleted because is the last admin in the system!"));
                    }
            }
        }

        userService.deleteUser(user_id);
        return ResponseEntity.ok(new MessageResponse("User has been successfully deleted!"));
    }

}