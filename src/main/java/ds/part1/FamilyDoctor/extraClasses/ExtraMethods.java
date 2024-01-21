package ds.part1.FamilyDoctor.extraClasses;

import ds.part1.FamilyDoctor.entity.Role;
import ds.part1.FamilyDoctor.entity.User;
import ds.part1.FamilyDoctor.service.UserService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
public abstract class ExtraMethods {

    static UserService userService;

    //Check if at least 2 admins exists, so we can delete one, or remove role from one
    public static boolean adminCheck(){
        int numberOfAdmins = 0;

        List<User> allUsers = userService.getUsers();
        for(User currentUser : allUsers){
            Set<Role> userRoles = currentUser.getRoles();
            for(Role currRoles : userRoles){
                if(currRoles.getName().equals("ROLE_ADMIN")){
                    numberOfAdmins++;
                }
            }
        }
        return numberOfAdmins > 1;
    }
}
