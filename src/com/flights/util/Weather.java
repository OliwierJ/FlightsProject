package com.flights.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {
    private LocalTime[] hours = new LocalTime[96];
    private float[] temperatures = new float[96];
    private float[] precipitations = new float[96];
    private float[] windSpeeds = new float[96];
    private float[] windGusts = new float[96];
    private int[] weatherCodes = new int[96];
    private static String[][] locations = {
        {"Dublin", "53.3331", "-6.2489"},
        {"Barcelona", "41.2983", "2.0800"}
    };

    public Weather(LocalDate d, String location) {
        try {
            String date = d.toString();
            String latitude = "";
            String longitude = "";
            for (String[] entry: locations) {
                if (location.equalsIgnoreCase(entry[0])) {
                    latitude = entry[1];
                    longitude = entry[2];
                    break;
                }
            }
            if (latitude.isEmpty() || longitude.isEmpty()) {
                throw new IllegalArgumentException("Error while retrieving location data");
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

    // Testing purposes
    public String printData(String time) {
        for (int i = 0; i < hours.length; i++) {
            if (hours[i].equals(time)) {
                return "Weather code: "+weatherCodes[i]+", wind speed: "+windSpeeds[i]+", gusts: "+windGusts[i]+", precipitation: "+precipitations[i]+", temperature degrees C: "+temperatures[i];
            }
        }
        return null;
    }

    public void printData(LocalTime l) {
        l = roundTime(l);
        for (int i = 0; i < hours.length; i++) {
            if (hours[i].equals(l)) {
                System.out.println(hours[i].toString());
                System.out.println("Weather code: "+weatherCodes[i]+", wind speed: "+windSpeeds[i]+", gusts: "+windGusts[i]+", precipitation: "+precipitations[i]+", temperature degrees C: "+temperatures[i]);
            }
        }
    }

    public float[] getFlightData(LocalTime l) {
        l = roundTime(l);
        for (int i = 0; i < hours.length; i++) {
            if (hours[i].equals(l)) {
                return new float[] {weatherCodes[i], windSpeeds[i], windGusts[i], precipitations[i], temperatures[i]};
            } 
        }
        return null;
    }

    private LocalTime roundTime(LocalTime l) {
        int minutes = l.toSecondOfDay() / 60;
        int rounded = (minutes / 15) * 15;
        return LocalTime.ofSecondOfDay(rounded * 60);
    }

    public void getWeatherStatus() {
        
    }

    // TODO finish off class testing purposes
    public static void main(String[] args) {
        Weather w = new Weather(LocalDate.of(2025, 03, 19), "Dublin");
        w.printData(LocalTime.of(11, 29));
        // weather.printData("2025-01-24T10:15");
    }
}
