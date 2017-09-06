/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.googlemaps;

import java.io.IOException;
import java.time.*;
/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = "/home/renansantos/Área de Trabalho/Estimativa_GoogleMaps_USAs.xls";
        String directionsApiKey = "AIzaSyCgaZr9fRAUs3_8lftkt026_MfZ3yZVN4E";
        GoogleMapsTime gmt = new GoogleMapsTime(filePath, directionsApiKey);
        gmt.getEstimatedDurationUsingAPI(0, 20);
        
    }
    
}
