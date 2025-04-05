package com.flights.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class to access weather details from <a href="https://open-meteo.com">Open Meteo</a> for a specific Flight
 */
public class Weather {
    private final LocalTime[] hours = new LocalTime[96];
    private final float[] temperatures = new float[96];
    private final float[] precipitations = new float[96];
    private final float[] windSpeeds = new float[96];
    private final float[] windGusts = new float[96];
    private final int[] weatherCodes = new int[96];
    private static final String[][] LOCATIONS = {
        {"Dublin", "53.4256", "-6.2573"},
        {"Barcelona", "41.2983", "2.0800"},
        {"Paris", "49.0078", "2.5507"},
        {"London", "51.4679", "-0.4550"},
        {"Rome", "41.8034", "12.2519"},
        {"Warsaw", "52.1648", "20.9691"},
        {"Berlin", "52.3650", "13.5010"}
    };

    /**
     * Construct a new weather object
     * @param d the date to collect weather data from
     * @param location the location to use (not coordinates)
     * @throws IllegalArgumentException if location is not one of the 7 preset locations
     */
    public Weather(LocalDate d, String location) {
        try {
            String date = d.toString();
            String latitude = "";
            String longitude = "";
            for (String[] entry: LOCATIONS) {
                if (location.equalsIgnoreCase(entry[0])) {
                    latitude = entry[1];
                    longitude = entry[2];
                    break;
                }
            }
            if (latitude.isEmpty() || longitude.isEmpty()) {
                throw new IllegalArgumentException("Entered location is invalid");
            }
            URI uri = new URI("https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&minutely_15=temperature_2m,precipitation,weather_code,wind_speed_10m,wind_gusts_10m&timezone=GMT&start_date="+date+"&end_date="+date);
            URL apiURL = uri.toURL();
            URLConnection uc = apiURL.openConnection();

            Scanner s = new Scanner(new InputStreamReader(uc.getInputStream()));
            JSONObject obj = new JSONObject(s.nextLine());
            s.close();

            JSONObject timeObject = obj.getJSONObject("minutely_15");
            JSONArray times = timeObject.getJSONArray("time");
            JSONArray temps = timeObject.getJSONArray("temperature_2m");
            JSONArray rains = timeObject.getJSONArray("precipitation");
            JSONArray codes = timeObject.getJSONArray("weather_code");
            JSONArray winds = timeObject.getJSONArray("wind_speed_10m");
            JSONArray gusts = timeObject.getJSONArray("wind_gusts_10m");            
            
            for (int i = 0; i < times.length(); i++) {
                this.hours[i] = LocalDateTime.parse(times.getString(i)).toLocalTime();
                this.temperatures[i] = temps.getFloat(i);
                this.precipitations[i] = rains.getFloat(i);
                this.weatherCodes[i] = codes.getInt(i);
                this.windSpeeds[i] = winds.getFloat(i);
                this.windGusts[i] = gusts.getFloat(i);
            }
        } catch (MalformedURLException e) {
            JErrorDialog.showError("Malformed URL or no valid protocol found", e);
        } catch (URISyntaxException e) {
            JErrorDialog.showError("Invalid URL", e);
        } catch (IOException e) {
            JErrorDialog.showError("An I/O error has occurred", e);
        }
    }

    /**
     * Gets the weather data
     * @param l the time to get the weather data from
     * @return a float[] containing the following in this exact order:
     * <br><code>weatherCode, windSpeed, windGust, precipitation, temperature</code>
     * <br>Returns empty float[5] if invalid time
     */
    public float[] getWeatherData(LocalTime l) {
        int minutes = l.toSecondOfDay() / 60;
        int rounded = (minutes / 15) * 15;
        l = LocalTime.ofSecondOfDay(rounded * 60);
        for (int i = 0; i < hours.length; i++) {
            if (hours[i].equals(l)) {
                return new float[] {weatherCodes[i], windSpeeds[i], windGusts[i], precipitations[i], temperatures[i]};
            } 
        }
        return new float[5];
    }
}
