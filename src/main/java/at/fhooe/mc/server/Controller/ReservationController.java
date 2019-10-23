package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.Reservation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ReservationController {

    @GetMapping(value = "/getReservation", produces = "application/json")
    public Reservation getReservation(@RequestParam(name="id") int id, @RequestParam(name="name", required = false) String name) {
        System.out.println(id + " " + name);
        return new Reservation();
    }

    @GetMapping(value = "/getAllReservation", produces = "application/json")
    public ArrayList<Reservation> getAllReservation(@RequestParam(name="id") int id, @RequestParam(name="name") String name) {

        return  new ArrayList<Reservation>();
    }


    @PutMapping(value = "/setReservation", produces = "application/json")
    public String setReservation(){

        return "success";
    }

    @DeleteMapping(value = "/deleteReservation", produces = "application/json")
    public String deleteReservation() {

        return "success";
    }
}
