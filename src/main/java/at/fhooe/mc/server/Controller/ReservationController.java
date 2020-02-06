package at.fhooe.mc.server.Controller;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.LoadingPort;
import at.fhooe.mc.server.Data.Reservation;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Repository.LoadingPortRepository;
import at.fhooe.mc.server.Repository.ReservationRepository;
import at.fhooe.mc.server.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@RestController
public class ReservationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    LoadingPortRepository loadingPortRepository;

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


    @RequestMapping(value = "/createReservation", method = RequestMethod.POST, consumes = "application/json")
    public String createReservation(@RequestBody String payload) throws Exception{
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode userNode = rootNode.get("user");
            JsonNode reservationNode = rootNode.get("reservation");
            JsonNode carNode = rootNode.get("vehicle");

            int portID = reservationNode.get("port").asInt();
            LoadingPort port = loadingPortRepository.findLoadingPortById(portID);

            if(port == null){
                return "{success: false}";
            }

            Date startDate = new Date(reservationNode.get("startTime").asLong());
            Date endDate = new Date(reservationNode.get("endTime").asLong());

            if(checkIfPortAlreadyHasReservation(port, startDate, endDate)){
                return "{success: false}";
            }

            int userId = userNode.get("user").asInt();
            User user = userRepository.findUserById(userId);

            if(user == null){
                return "{success: false}";
            }

            Reservation reservation = new Reservation();
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setLoadingport(port);
            reservation.setUser(user);

            reservationRepository.save(reservation);

            return "{success: true}";

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }
    }

    @RequestMapping(value = "/createReservation", method = RequestMethod.DELETE, consumes = "application/json")
    public String deleteReservation(@RequestBody String payload) throws Exception{
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode reservationNode = rootNode.get("reservation");

            int reservationID = reservationNode.get("id").asInt();
            Reservation reservation = reservationRepository.findReservationById(reservationID);

            if(reservation == null){
                return "{success: false}";
            }

            reservationRepository.delete(reservation);

            return "{success: true}";

        } catch (IOException e) {
            e.printStackTrace();
            return "{success: false}";
        }
    }

    private boolean checkIfPortAlreadyHasReservation(LoadingPort port, Date startDate, Date endDate){
        for(Reservation reservation : port.getReservations()){
            if(startDate.getTime() <= reservation.getEndDate().getTime() && reservation.getStartDate().getTime() <= endDate.getTime()){
                return false;
            }
        }
        return true;
    }
}
