package com.example.user.bustimings.webService;

import android.util.Log;

/**
 * Created by user on 3/2/2018.
 */

public class Links {
    //links for the bus API
    private String busArrival = "http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2";
    private String busStops = "http://datamall2.mytransport.sg/ltaodataservice/BusStops";

    public Links(){

    }
    //Arrival link for
    public String getBusArrival(String busCode, String busNumber){
        StringBuilder link = new StringBuilder();
        //main link
        link.append(this.busArrival);
        //additional information
        link.append("?BusStopCode=").append(busCode);
        link.append("&ServiceNo=").append(busNumber);
        Log.i("full link: ",link.toString());
        return link.toString();
    }
    public String getBusArrival(String busCode){
        StringBuilder link = new StringBuilder();
        //main link
        link.append(this.busArrival);
        //additional information
        link.append("?BusStopCode=").append(busCode);
        Log.i("full link: ",link.toString());

        return link.toString();
    }

    public String getBusStops(){
        return this.busStops;
    }
}
