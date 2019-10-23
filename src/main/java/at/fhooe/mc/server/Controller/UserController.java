package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping(value = "/getUserID", produces = "application/json")
    public User getUserID(@RequestParam(name="id") int id) {

        return new User();
    }

    @GetMapping(value = "/getUserName", produces = "application/json")
    public User getUserName(@RequestParam(name="id") int id){


        return new User();
    }

    @PostMapping(value = "/setUser", produces = "application/json")
    public String setUser() {

        return "success";
    }


    @PutMapping(value = "/updateUser", produces = "application/json")
    public String updateUser() {

        return "success";
    }
}
