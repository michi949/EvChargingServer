package at.fhooe.mc.server.WebView;

import at.fhooe.mc.server.Data.*;
import at.fhooe.mc.server.Repository.LoadingPortRepository;
import at.fhooe.mc.server.Repository.ReservationRepository;
import at.fhooe.mc.server.Repository.SystemReportRepository;
import at.fhooe.mc.server.Services.Optimizer.Optimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Controller
public class WebViewController {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    LoadingPortRepository loadingPortRepository;

    @Autowired
    SystemReportRepository systemReportRepository;

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

        double aviableSolarPower = optimizer.getAvailableSolarPower();
        model.addAttribute("aviableSolarPower", aviableSolarPower);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date startDate = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, 23);
        Date endDate = c.getTime();
        ArrayList<SystemReport> systemReports = new ArrayList<>(systemReportRepository.findAllReportsInAllRange(startDate));
        model.addAttribute("systemReports", systemReports);

        c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        ArrayList<Reservation> reservations = new ArrayList<>(reservationRepository.findReservationByDateRange(new Date(), c.getTime()));
        model.addAttribute("reservations", reservations);
        return "overview";
    }

    @GetMapping("/WeatherForecast")
    public String WeatherForecastView(Model model) {
        ArrayList<HourlyWeatherForecast> weatherForecast = optimizer.getWeatherForecasts();
        weatherForecast.sort((HourlyWeatherForecast p1, HourlyWeatherForecast p2) -> p1.getTime().compareTo(p2.getTime()));
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
    public String SessionsDetailView(@RequestParam(value = "id", required = true) Integer id, Model model) {
        ArrayList<Session> sessions = optimizer.getSessions();

        for(Session session : sessions) {
            if(session.getId() == id){
                model.addAttribute("sessionItem", session);
                 return "sessionsDetailView";
            }
        }

        return "sessionsDetailView";
    }

    @GetMapping("/Settings")
    public String SettingsView(Model model) {

        return "settings";
    }

}
