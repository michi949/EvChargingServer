package at.fhooe.mc.server.Controller;


import at.fhooe.mc.server.Data.Session;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class SessionController {

    @GetMapping("/getSession")
    public Session getSession(@RequestParam(name="user") String user, @RequestParam(name="id") int id) {

        return new Session();
    }


    @GetMapping("/getAllSession")
    public ArrayList<Session> getAllSession(){

        return new ArrayList<Session>();
    }


    @PostMapping("/setSession")
    public String setSession(){

        return "success";
    }

    @PutMapping("/updateSession")
    public String updateSession() {

        return "success";
    }

    @PutMapping("/stopSession")
    public String stopSession() {

        return "success";
    }
}
