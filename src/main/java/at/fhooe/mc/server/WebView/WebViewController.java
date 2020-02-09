package at.fhooe.mc.server.WebView;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebViewController {

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String indexView(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

}
