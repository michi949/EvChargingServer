package at.fhooe.mc.server.Controller;


import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import at.fhooe.mc.server.Repository.CarRepository;
import at.fhooe.mc.server.Repository.SessionRepository;
import at.fhooe.mc.server.Repository.UserRepository;
import at.fhooe.mc.server.Services.Optimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class SessionController {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    CarRepository carRepository;

    @GetMapping(value = "/getSession", produces = "application/json")
    public Session getSession(@RequestParam(name="id") int id) {
        return sessionRepository.findOne(id);
    }


    @GetMapping(value = "/getAllSessionFromCar", produces = "application/json")
    public Session getAllSession(@RequestParam(name="id") int id){

        Car car = carRepository.findOne(id);

        if(car == null) {
            return null;
        }

        return car.getSession();
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
