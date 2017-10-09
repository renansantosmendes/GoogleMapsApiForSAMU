package com.mycompany.googlemaps;

import java.time.*;
/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args){
        String directionsApiKey = "AIzaSyCgaZr9fRAUs3_8lftkt026_MfZ3yZVN4E";
        String origin = "rua estér augusta ribeiro, camargos, belo horizonte";
        String destination = "praça sete de setembro, centro, belo horizonte";
        LocalDateTime ldt = LocalDateTime.of(2017, 10, 6, 12, 0);

        GoogleMapsTime google = new GoogleMapsTime(directionsApiKey);
        google.getNormalTimeBetween(origin, destination);
        google.getTrafficTimeBetween(origin, destination, ldt);
    }

}
