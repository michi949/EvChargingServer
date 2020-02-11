package at.fhooe.mc.server.WebView;

import at.fhooe.mc.server.Data.HourlyWeatherForecast;
import at.fhooe.mc.server.Data.Reservation;
import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Repository.ReservationRepository;
import at.fhooe.mc.server.Services.Optimizer.Optimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Controller
public class WebViewController {

    @Autowired
    ReservationRepository reservationRepository;

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    Optimizer optimizer;

    @GetMapping("/")
    public String indexView(Model model) {
        ArrayList<HourlyWeatherForecast> weatherForecast = optimizer.getWeatherForecasts();
        model.addAttribute("weatherForecast", weatherForecast);

        ArrayList<Session> sessions = optimizer.getSessions();
        model.addAttribute("sessions", sessions);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        ArrayList<Reservation> reservations = new ArrayList<>(reservationRepository.findReservationByDateRange(new Date(), c.getTime()));
        model.addAttribute("reservations", reservations);
        return "overview";
    }

    @GetMapping("/WeatherForecast")
    public String WeatherForecastView(Model model) {
        ArrayList<HourlyWeatherForecast> weatherForecast = optimizer.getWeatherForecasts();
        model.addAttribute("weatherForecast", weatherForecast);

        return "weatherForecast";
    }

    @GetMapping("/Reservations")
    public String ReservationsView(Model model) {
        ArrayList<Reservation> reservations = new ArrayList<>(reservationRepository.findAllReservationsFromNowOn(new Date()));
        model.addAttribute("reservations", reservations);

        return "reservations";
    }

    @GetMapping("/Sessions")
    public String SessionsView(Model model) {
        ArrayList<Session> sessions = optimizer.getSessions();
        model.addAttribute("sessions", sessions);

        return "sessions";
    }

    @GetMapping("/SessionsID")
    public String SessionsDetailView(Model model) {
        ArrayList<Session> sessions = optimizer.getSessions();
        model.addAttribute("sessions", sessions);

        return "sessionsDetailView";
    }

    @GetMapping("/Settings")
    public String SettingsView(Model model) {

        return "settings";
    }

}
