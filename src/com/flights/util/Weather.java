package com.flights.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {
    private String[] hours = new String[96];
    private float[] temperatures = new float[96];
    private float[] precipitations = new float[96];
    private float[] windSpeeds = new float[96];
    private float[] windGusts = new float[96];
    private int[] weatherCodes = new int[96];
    private static String[][] locations = {
        {"Dublin", "53.3331", "-6.2489"},
        {"Barcelona", "41.2983", "2.0800"}
    };

    public Weather(String date, String location) {
        try {
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
                this.hours[i] = times.getString(i);
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
    public void printData(String time) {
        for (int i = 0; i < hours.length; i++) {
            if (hours[i].equals(time)) {
                System.out.println("Weather code: "+weatherCodes[i]+", wind speed: "+windSpeeds[i]+", gusts: "+windGusts[i]+", precipitation: "+precipitations[i]+", temperature degrees C: "+temperatures[i]);
            }
        }
    }

    public float[] getFlightData(String time) {
        for (int i = 0; i < hours.length; i++) {
            if (hours[i].equals(time)) {
                return new float[] {weatherCodes[i], windSpeeds[i], windGusts[i], precipitations[i], temperatures[i]};
            }
        }
        return null;
    }

    public void getWeatherStatus() {
        
    }

    // TODO finish off class testing purposes
    public static void main(String[] args) {
        Weather weather = new Weather("2025-01-24", "Dublin");
        weather.printData("2025-01-24T10:15");
    }
}
