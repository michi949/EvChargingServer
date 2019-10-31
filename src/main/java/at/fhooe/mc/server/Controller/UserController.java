package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;


    @GetMapping(value = "/getUserFromID", produces = "application/json")
    public User getUserID(@RequestParam(name="id") int id) {
        return userRepository.findOne(id);
    }

    @GetMapping(value = "/getUserFromCard", produces = "application/json")
    public User getUserFromCard(@RequestParam(name="card") int card) {
        return userRepository.findUserByCard(card);
    }

    @PostMapping(value = "/setUser", produces = "application/json")
    public String setUser() {

        return "success";
    }


    @PutMapping(value = "/updateUser", produces = "application/json")
    public String updateUser() {

        return "success";
    }


    @DeleteMapping(value = "/deleteUser", produces = "application/json")
    public String deleteUser(@RequestParam(name="id") int id) {
        userRepository.delete(id);
        return "success";
    }
}
