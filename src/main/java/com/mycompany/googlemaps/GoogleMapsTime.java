package com.mycompany.googlemaps;

import com.google.maps.*;
import com.google.maps.model.*;
import java.time.*;
import java.util.*;
import org.joda.time.DateTime;

/**
 *
 * @author renansantos
 */
public class GoogleMapsTime {

    private LocalDateTime occurrenceTime;
    private String directionsApiKey;

    public GoogleMapsTime(String directionsApiKey) {
        this.directionsApiKey = directionsApiKey;
    }

    public long getNormalTimeBetween(String origin, String destination) {
        
        GeoApiContext geoApiContextSAMU = new GeoApiContext().setApiKey(directionsApiKey);
        DirectionsApiRequest directionsApiRequestSAMU = DirectionsApi.getDirections(geoApiContextSAMU, origin, destination);

        directionsApiRequestSAMU.alternatives(true);
        directionsApiRequestSAMU.mode(TravelMode.DRIVING);

        DirectionsResult routes = null;
        routes = makeConnectionInServer(routes, directionsApiRequestSAMU);

        DirectionsRoute[] route = routes.routes;
        List<DirectionsLeg[]> directionsLegs = new ArrayList<>();
        DirectionsLeg[] leg = getMinimumTimeRoute(route, directionsLegs);
        
//        System.out.println("Normal time = " + leg[0].duration);
        return leg[0].duration.inSeconds;
    }

    private DirectionsResult makeConnectionInServer(DirectionsResult routes, DirectionsApiRequest directionsApiRequestSAMU) {
        try {
            routes = directionsApiRequestSAMU.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    private DirectionsLeg[] getMinimumTimeRoute(DirectionsRoute[] route, List<DirectionsLeg[]> directionsLegs) {
        for (int j = 0; j < route.length; j++) {
            directionsLegs.add(route[j].legs);
            DirectionsLeg[] directionsLeg2 = route[j].legs;
        }
        directionsLegs.sort(Comparator.comparing(u -> u[0].duration.inSeconds));
        DirectionsLeg[] leg = directionsLegs.get(0);
        return leg;
    }

    public long getTrafficTimeBetween(String origin, String destination, LocalDateTime occurrenceTime) {

        GeoApiContext geoApiContextSAMU = new GeoApiContext().setApiKey(directionsApiKey);
        DirectionsApiRequest directionsApiRequestSAMU = DirectionsApi.getDirections(geoApiContextSAMU, origin, destination);

        DateTime dt = returnsCorrectedDateTime(occurrenceTime);
        LocalTime localTime = occurrenceTime.toLocalTime();
        LocalDate localDate = occurrenceTime.toLocalDate();

        directionsApiRequestSAMU.alternatives(true);
        directionsApiRequestSAMU.mode(TravelMode.DRIVING).departureTime(dt);//TRANSIT é para ônibus

        DirectionsResult routes = null;
        routes = makeConnectionInServer(routes, directionsApiRequestSAMU);

        if (routes != null) {
            DirectionsRoute[] route = routes.routes;
            List<DirectionsLeg[]> directionsLegs = new ArrayList<>();
            DirectionsLeg[] leg = getMinimumTimeRoute(route, directionsLegs);

//            System.out.println("Time With Traffic = " + leg[0].durationInTraffic);
            return leg[0].durationInTraffic.inSeconds;
        } else {
            return -1;
        }
    }

    public DateTime returnsCorrectedDateTime(LocalDateTime occurrenceTime) {
//        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime currentTime = LocalDateTime.of(2017,10,10,0,0);
        int WEEK_DAYS = 7;
        DateTime dateTimeForGoogleMaps;

        LocalDateTime intermediate = currentTime.plusDays(WEEK_DAYS);
        LocalDateTime corrected = null;

        int daysBetween = occurrenceTime.getDayOfWeek().getValue() - currentTime.getDayOfWeek().getValue();
        corrected = LocalDateTime.of(intermediate.getYear(), intermediate.getMonth(), intermediate.plusDays(daysBetween).getDayOfMonth(),
                occurrenceTime.getHour(), occurrenceTime.getMinute());

        return new DateTime(corrected.getYear(), corrected.getMonthValue(), corrected.getDayOfMonth(),
                corrected.getHour(), corrected.getMinute());
    }
}
