package at.fhooe.mc.server.Parser;

import at.fhooe.mc.server.Data.Weather;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.Date;
import java.util.Map;

/**
 * Parse the Data from JSON String.
 * Usin Jackson Parser, implementet into spring.
 */
public class WeatherParser {

    public static Weather parseWeather(String jsonWeather) {

        Weather weather = new Weather();
        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonNode rootNode = mapper.readTree(jsonWeather);
            weather = readMainValues(weather,rootNode.get("main"));
            weather = readSysValues(weather, rootNode.get("sys"));
            weather = readCloudValues(weather, rootNode.get("clouds"));
            weather = readWeatherValues(weather, rootNode.get("weather"));

        } catch (IOException e) {
            e.printStackTrace();
            return new Weather();
        }

        return weather;
    }

    /**
     * Takes main sub tree vom Json and parse it.
     * @param weather weathe data
     * @param subNode sub tree
     * @return updated weather data
     */
    private static Weather readMainValues(Weather weather, JsonNode subNode) {

       weather.setTemperature(subNode.path("temp").asDouble());
       weather.setTemperature_max(subNode.path("temp_max").asDouble());
       weather.setTemperature_min(subNode.path("temp_min").asDouble());
       weather.setPressure(subNode.path("pressure").asInt());

        return weather;
    }

    /**
     * Takes sys sub tree vom Json and parse it.
     * @param weather weathe data
     * @param subNode sub tree
     * @return updated weather data
     */
    private static Weather readSysValues(Weather weather, JsonNode subNode) {

        weather.setSunrise(new Date(subNode.path("sunrise").asInt()));
        weather.setSunset(new Date(subNode.path("sunset").asInt()));

        return weather;
    }

    /**
     * Takes cloud sub tree vom Json and parse it.
     * @param weather weathe data
     * @param subNode sub tree
     * @return updated weather data
     */
    private static Weather readCloudValues(Weather weather, JsonNode subNode) {

        weather.setClouds(subNode.path("all").asInt());

        return weather;
    }

    /**
     * Takes weather sub tree vom Json and parse it.
     * Loop over an array of data.
     * @param weather weather data
     * @param subNode sub tree
     * @return updated weather data
     */
    private static Weather readWeatherValues(Weather weather, JsonNode subNode) {

       for(int i = 0; i < subNode.size(); i++) {
           JsonNode arrayNode = subNode.get(i);
           weather.setLight(arrayNode.path("description").asText());
       }

        return weather;
    }


    /**
     * Convert Fahrenheit to Celsius!
     * @param fahreheit
     * @return
     */
    private static Double FarenheitToCelsius(double fahreheit) {
        return ((fahreheit - 32)*5)/9;
    }
}
