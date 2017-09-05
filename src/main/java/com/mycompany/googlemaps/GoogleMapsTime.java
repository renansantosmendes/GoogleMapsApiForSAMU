/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.googlemaps;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class GoogleMapsTime {

    private List<String> origins = new ArrayList<>();
    private List<String> destinations = new ArrayList<>();
    private List<LocalDateTime> occurrencesTime = new ArrayList<>();
    private String filePath;

    public GoogleMapsTime(String filePath) {
        this.filePath = filePath;
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

    private void readData() throws IOException, BiffException {
        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath), conf);
        Sheet sheet = workbook.getSheet(0);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        System.out.println("n de linhas = " + rows);
        System.out.println("n de colunas = " + columns);

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
                //System.out.println(occurrenteDateTime);
            }
            
            
            //LocalDateTime occurrenteDateTime = LocalDateTime.of(year, month, day, hour, minute);
            //System.out.println(occurrenteDateTime);
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
    
    
}
