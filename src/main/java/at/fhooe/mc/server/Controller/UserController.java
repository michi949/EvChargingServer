package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/getUserID")
    public User getUserID(@RequestParam(name="id") int id) {

        return new User();
    }

    @GetMapping("/getUserName")
    public User getUserName(@RequestParam(name="id") int id){


        return new User();
    }

    @PostMapping("/setUser")
    public String setUser() {

        return "success";
    }


    @PutMapping("/updateUser")
    public String updateUser() {

        return "success";
    }
}
