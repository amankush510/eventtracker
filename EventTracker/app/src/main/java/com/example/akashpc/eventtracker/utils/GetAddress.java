package com.example.akashpc.eventtracker.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;
import java.util.Locale;

/**
 * Created by Akash PC on 11-01-2017.
 */

public class GetAddress {
    private Double latitude, longitude;
    private Context context;

    public GetAddress(Double latitude, Double longitude, Context context){
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }

    public String getAddressFromCoordinates(){
        String address = "";
        Geocoder geocoder;
        List<Address> addresses;
        try {
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String mainAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            if(mainAddress != null){
                address += mainAddress;
            }
            if(city != null){
                address = " ," + city;
            }
            if(state != null){
                address = " ," + state;
            }
            if(country != null){
                address = " ," + country;
            }
            if(postalCode != null){
                address = " ," + postalCode;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return address;
    }
}
