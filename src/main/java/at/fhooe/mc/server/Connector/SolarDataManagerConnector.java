package at.fhooe.mc.server.Connector;

import at.fhooe.mc.server.Data.HourlyWeatherForecast;
import at.fhooe.mc.server.Parser.SolarDataManagerParser;
import at.fhooe.mc.server.Parser.WeatherForecastParser;
import at.fhooe.mc.server.Utilitys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SolarDataManagerConnector {
    private String path;
    private RestTemplate restTemplate;

    @Autowired
    SolarDataManagerParser solarDataManagerParser;

    public SolarDataManagerConnector() {
        this.restTemplate = new RestTemplate();
        this.path = "https://193.170.124.83/solar_api/v1/GetInverterRealtimeData.cgi";
    }

    public double performRequest() {

        String url = null;
        try {
            url = path + getParamsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0.0;
        }

        String jsonDataManager =  this.restTemplate.getForObject(url, String.class);

        return 0.0;
    }

    /**
     * Build the request String.
     * @return GET Request String for WeatherAPI
     * @throws UnsupportedEncodingException
     */
    private String getParamsString() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        Map<String, String> params = setupParameters();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
    }


    private Map<String, String> setupParameters(){
        Map<String, String> params = new HashMap<>();

        //params.put("APPID", Utilitys.WEATHERAPI);

        return params;
    }
}
