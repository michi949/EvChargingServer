package at.fhooe.mc.server.WebView;

import at.fhooe.mc.server.Data.HourlyWeatherForecast;
import at.fhooe.mc.server.Services.Optimizer.Optimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class WebViewController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    Optimizer optimizer;

    @GetMapping("/")
    public String indexView(Model model) {
        ArrayList<HourlyWeatherForecast> weatherForecast = optimizer.getWeatherForecasts();
        model.addAttribute("weatherForecast", weatherForecast);
        return "overview";
    }

}
