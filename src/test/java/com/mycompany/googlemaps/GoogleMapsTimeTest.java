/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.googlemaps;

import org.junit.Test;
import java.time.*;
import static org.junit.Assert.*;

/**
 *
 * @author renansantos
 */
public class GoogleMapsTimeTest {

    public GoogleMapsTimeTest() {

    }

    @Test
    public void testReturnsCorrectedDateTime() {
        String filePath = "/home/renansantos/Área de Trabalho/Estimativa_GoogleMaps_USAs.xls";
        String directionsApiKey = "AIzaSyCgaZr9fRAUs3_8lftkt026_MfZ3yZVN4E";
        GoogleMapsTime gmt = new GoogleMapsTime(filePath, directionsApiKey);

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime occurrence = LocalDateTime.of(2016, 5, 2, 15, 42);

        System.out.println(current);
        System.out.println("old = " + occurrence);
        System.out.println("old = " + occurrence.getDayOfWeek());
       
        System.out.println(occurrence);

        LocalDateTime corrected = null;
        int daysBetween = occurrence.getDayOfWeek().getValue() - current.getDayOfWeek().getValue();
        if (daysBetween < 0) {
            //corrected = occurrence.plusDays(7);
            LocalDateTime intermediate = current.plusDays(7);
            System.out.println("Intermediate = " + intermediate);
            corrected = LocalDateTime.of(current.getYear(), current.getMonth(),intermediate.plusDays(daysBetween).getDayOfMonth(), occurrence.getHour(), occurrence.getMinute());
        } else if(daysBetween == 0){
            
        } else {
            
        }
        System.out.println("new = " + corrected.getDayOfWeek());
        System.out.println("new = " + corrected);
        System.out.println("Days between = " + daysBetween);
        assertEquals(true, occurrence.getDayOfWeek().getValue() < current.getDayOfWeek().getValue());
    }

}
