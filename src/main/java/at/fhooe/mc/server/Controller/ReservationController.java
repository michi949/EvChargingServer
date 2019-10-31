package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.Reservation;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Repository.ReservationRepository;
import at.fhooe.mc.server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ReservationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @GetMapping(value = "/getReservation", produces = "application/json")
    public Reservation getReservation(@RequestParam(name="id") int id) {
        return reservationRepository.findOne(id);
    }

    @GetMapping(value = "/getAllReservationForUser", produces = "application/json")
    public ArrayList<Reservation> getAllReservation(@RequestParam(name="id") int id) {

        User user = userRepository.findOne(id);

        if(user == null) {
            return null;
        }


        return new ArrayList<Reservation>(user.getReservation());
    }


    @PutMapping(value = "/setReservation", produces = "application/json")
    public String setReservation(){

        return "success";
    }

    @DeleteMapping(value = "/deleteReservation", produces = "application/json")
    public String deleteReservation(@RequestParam(name="id") int id) {
        reservationRepository.delete(id);
        return "200";
    }
}
