/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.googlemaps;

import com.google.maps.*;
import com.google.maps.model.*;
import java.io.*;
import java.time.*;
import java.util.*;
import jxl.*;
import jxl.read.biff.BiffException;
import org.joda.time.DateTime;

/**
 *
 * @author renansantos
 */
public class GoogleMapsTime {

    private List<String> origins = new ArrayList<>();
    private List<String> destinations = new ArrayList<>();
    private List<LocalDateTime> occurrencesTime = new ArrayList<>();
    private String filePath;
    private String directionsApiKey;

    public GoogleMapsTime(String filePath) {
        this.filePath = filePath;
        tryToReadData();
    }

    public GoogleMapsTime(String filePath, String directionsApiKey) {
        this.filePath = filePath;
        this.directionsApiKey = directionsApiKey;
        tryToReadData();
    }

    private void tryToReadData() {
        try {
            this.readData();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BiffException ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getOrigins() {
        return origins;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public List<LocalDateTime> getOccurrencesTime() {
        return occurrencesTime;
    }

    private void readData() throws IOException, BiffException {
        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath), conf);
        Sheet sheet = workbook.getSheet(0);

        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        for (int i = 1; i < rows; i++) {
            Cell originCell = sheet.getCell(6, i);
            Cell destinationCell = sheet.getCell(7, i);
            Cell occurrenceTimeCell = sheet.getCell(1, i);
            Cell occurrenceDateCell = sheet.getCell(12, i);

            int day = 0, month = 0, year = 0, hour = 0, minute = 0;

            if (occurrenceDateCell.getContents() != "") {
                String[] transmitionTimeString = occurrenceDateCell.getContents().split("/");
                String[] occurrenceTimeString = occurrenceTimeCell.getContents().split(":");

                day = Integer.parseInt(transmitionTimeString[0]);
                month = Integer.parseInt(transmitionTimeString[1]);
                year = 2000 + Integer.parseInt(transmitionTimeString[2]);
                hour = Integer.parseInt(occurrenceTimeString[0]);
                minute = Integer.parseInt(occurrenceTimeString[1]);

                LocalDateTime occurrenteDateTime = LocalDateTime.of(year, month, day, hour, minute);
                this.origins.add(originCell.getContents());
                this.destinations.add(destinationCell.getContents());
                this.occurrencesTime.add(occurrenteDateTime);
            }
        }
    }

    public void getCalculatedDurationUsingAPI(int start, int end) {
        GeoApiContext geoApiContextSAMU = new GeoApiContext().setApiKey(directionsApiKey);

        for (int i = start; i < end; i++) {
            DirectionsApiRequest directionsApiRequestSAMU = DirectionsApi
                    .getDirections(geoApiContextSAMU, origins.get(i), destinations.get(i));

            directionsApiRequestSAMU.alternatives(true);
            directionsApiRequestSAMU.mode(TravelMode.DRIVING);//TRANSIT é para ônibus

            DirectionsResult routes = null;
            try {
                routes = directionsApiRequestSAMU.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            DirectionsRoute[] route = routes.routes;

            List<DirectionsLeg[]> directionsLegs = new ArrayList<>();
            for (int j = 0; j < route.length; j++) {
                directionsLegs.add(route[j].legs);
                DirectionsLeg[] directionsLeg2 = route[j].legs;
            }

            directionsLegs.sort(Comparator.comparing(u -> u[0].duration.inSeconds));
            DirectionsLeg[] leg = directionsLegs.get(0);
            //deve ser retornado o valor de leg[0]
            System.out.println("Best time = " + leg[0].duration);
            //directionsLegs.forEach(u -> System.out.println(u[0].duration));
        }
    }

    public void getEstimatedDurationUsingAPI(int start, int end) {
        GeoApiContext geoApiContextSAMU = new GeoApiContext().setApiKey(directionsApiKey);

        for (int i = start; i < end; i++) {
            DirectionsApiRequest directionsApiRequestSAMU = DirectionsApi
                    .getDirections(geoApiContextSAMU, origins.get(i), destinations.get(i));

            DateTime dt = returnsCorrectedDateTime(i);
            //System.out.println(dt);
            LocalTime localTime = occurrencesTime.get(i).toLocalTime();
            LocalDate localDate = occurrencesTime.get(i).toLocalDate();

            directionsApiRequestSAMU.alternatives(true);
            directionsApiRequestSAMU.mode(TravelMode.DRIVING).departureTime(dt);//TRANSIT é para ônibus

            DirectionsResult routes = null;
            try {
                routes = directionsApiRequestSAMU.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            DirectionsRoute[] route = routes.routes;

            List<DirectionsLeg[]> directionsLegs = new ArrayList<>();
            for (int j = 0; j < route.length; j++) {
                directionsLegs.add(route[j].legs);
                DirectionsLeg[] directionsLeg2 = route[j].legs;
            }

            directionsLegs.sort(Comparator.comparing(u -> u[0].durationInTraffic.inSeconds));
            DirectionsLeg[] leg = directionsLegs.get(0);
            //deve ser retornado o valor de leg[0]
            System.out.println(leg[0].durationInTraffic);
//            directionsLegs.forEach(u -> System.out.println(u[0].duration));
//            directionsLegs.forEach(u -> System.out.println(u[0].durationInTraffic));
        }
    }

    public DateTime returnsCorrectedDateTime(int index) {
        LocalDateTime occurrence = this.occurrencesTime.get(index);
        LocalDateTime currentTime = LocalDateTime.now();
        int WEEK_DAYS = 7;
        DateTime dateTimeForGoogleMaps;

        LocalDateTime intermediate = currentTime.plusDays(WEEK_DAYS);
        LocalDateTime corrected = null;

        int daysBetween = occurrence.getDayOfWeek().getValue() - currentTime.getDayOfWeek().getValue();
        corrected = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(), intermediate.plusDays(daysBetween).getDayOfMonth(),
                occurrence.getHour(), occurrence.getMinute());

        return new DateTime(corrected.getYear(), corrected.getMonthValue(), corrected.getDayOfMonth(),
                corrected.getHour(), corrected.getMinute());
    }

    private static boolean isTheSameWeekDay(LocalDateTime occurrenceTime, LocalDateTime currentTime) {
        return occurrenceTime.getDayOfWeek() == currentTime.getDayOfWeek();
    }
}
