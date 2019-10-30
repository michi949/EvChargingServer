package at.fhooe.mc.server.Controller;


import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import at.fhooe.mc.server.Services.Optimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class SessionController {


    @GetMapping(value = "/getSession", produces = "application/json")
    public Session getSession(@RequestParam(name="user") String user, @RequestParam(name="id") int id) {

        return new Session();
    }


    @GetMapping(value = "/getAllSession", produces = "application/json")
    public ArrayList<Session> getAllSession(){

        return new ArrayList<Session>();
    }


    @PostMapping(value = "/setSession", produces = "application/json")
    public String setSession(){

        return "success";
    }

    @PutMapping(value = "/updateSession", produces = "application/json")
    public String updateSession() {

        return "success";
    }

    @PutMapping(value = "/stopSession", produces = "application/json")
    public String stopSession() {

        return "success";
    }
}
