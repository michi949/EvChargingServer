package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.Reservation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ReservationController {

    @GetMapping(value = "/getReservation")
    public ArrayList<Reservation> getReservation(@RequestParam(name="user", required=true) String user) {

        return  new ArrayList<Reservation>();
    }


    @PutMapping(value = "/setReservation")
    public String setReservation(){

        return "success";
    }

    @DeleteMapping(value = "/deleteReservation")
    public String deleteReservation() {

        return "success";
    }
}
