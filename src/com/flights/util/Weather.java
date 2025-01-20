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
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {
    private static String defaultURL = "https://api.open-meteo.com/v1/forecast?latitude=53.3331&longitude=-6.2489&minutely_15=temperature_2m,precipitation,weather_code,wind_speed_10m,wind_gusts_10m&timezone=GMT&start_date=2025-01-16&end_date=2025-01-16";
    
    private String[] hours = new String[96];
    private LocalDateTime[] hours2 = new LocalDateTime[96];
    private float[] temperatures = new float[96];
    private float[] precipitations = new float[96];
    private float[] windSpeeds = new float[96];
    private float[] windGusts = new float[96];
    private int[] weatherCodes = new int[96];

    public Weather(LocalDate date, String location) {
        try {
            String latitude = "53.3331";
            String longitude = "-6.2489";
            URI uri = new URI("https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+"&minutely_15=temperature_2m,precipitation,weather_code,wind_speed_10m,wind_gusts_10m&timezone=GMT&start_date=2025-01-16&end_date=2025-01-16");
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
                this.hours[i] = times.getString(i);
                this.temperatures[i] = temps.getFloat(i);
                this.precipitations[i] = rains.getFloat(i);
                this.weatherCodes[i] = codes.getInt(i);
                this.windSpeeds[i] = winds.getFloat(i);
                this.windGusts[i] = gusts.getFloat(i);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Testing purposes
    public void printData(String time) {
        for (int i = 0; i < hours.length; i++) {
            if (hours[i].equals(time)) {
                System.out.println("Weather code: "+weatherCodes[i]+", wind speed: "+windSpeeds[i]+", gusts: "+windGusts[i]+", precipitation: "+precipitations[i]+", temperature degrees C: "+temperatures[i]);
            }
        }
    }

    // testing purposes
    public static void main(String[] args) {
        Weather weather = new Weather(null, "Dublin");
        weather.printData("2025-01-16T19:00");
    }
}
