package com.mycompany.googlemaps;

import java.time.*;
/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args){
        String directionsApiKey = "AIzaSyB6QWiZU8P2KZDlTeW2_r1QVrnkoHglUuk";// AIzaSyCgaZr9fRAUs3_8lftkt026_MfZ3yZVN4E
        String origin = "rua estér augusta ribeiro, camargos, belo horizonte";
        String destination = "praça sete de setembro, centro, belo horizonte";
        LocalDateTime ldt = LocalDateTime.of(2017, 10, 20, 12, 0);
        LocalDateTime ldt2 = LocalDateTime.of(2017, 10, 22, 12, 0);

        GoogleMapsTime google = new GoogleMapsTime(directionsApiKey);
        google.getNormalTimeBetween(origin, destination);
        long normalTime = google.getNormalTimeBetween(origin, destination);
        System.out.println("normal time = " + normalTime/60 + " minutes");
        
        google.getTrafficTimeBetween(origin, destination, ldt);
        long trafficTime = google.getTrafficTimeBetween(origin, destination, ldt);
        System.out.println("traffic time = " + trafficTime/60 + " minutes");
        
        google.getTrafficTimeBetween(origin, destination, ldt2);
        long trafficTime2 = google.getTrafficTimeBetween(origin, destination, ldt2);
        System.out.println("traffic time 2 = " + trafficTime2/60 + " minutes");
    }

}
