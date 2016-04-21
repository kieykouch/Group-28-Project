package com.group28.cs160.babybump;

/**
 * Created by eviltwin on 4/20/16.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PlacesService {

    private static final String API_KEY = "AIzaSyAl6SIsImULYPreEIzwczDHaS3IiciNIcg";

    public static List<Place> findPlaces(double latitude, double longitude, String placeSpacification)
    {

        String urlString = makeUrl(latitude, longitude, placeSpacification);
        try {
            URLTask loc_task = new URLTask();
            loc_task.execute(urlString);
            Log.d("Places Service", "Fetching Nearby Places.");
            // Get Locations.
            JSONArray array = loc_task.get();
            ArrayList<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                Place place = Place.jsonToPontoReferencia((JSONObject) array.get(i));
                arrayList.add(place);
            }
            return arrayList;
        } catch (Exception e) {
            Log.d("Places Service", "Exception with fetching locations. " + e.toString());
            return null;
        }
    }

    //https://maps.googleapis.com/maps/api/place/search/json?location=28.632808,77.218276&radius=500&types=atm&sensor=false&key=apikey
    private static String makeUrl(double latitude, double longitude,String place) {
        StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");

        if (place.equals("")) {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=1000");
            //   urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
        } else {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=1000");
            urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
        }
        return urlString.toString();
    }
}