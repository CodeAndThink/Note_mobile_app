package com.example.noteapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
    public static String getCurrentTimestamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy"); // must be lowercase for api 23
            String currentDateTime = dateFormat.format(new Date());
            return currentDateTime;
        }catch (Exception e) {
            return null;
        }
    }
    public  static String getMonthFromNumber(String monthNumber){
        switch (monthNumber){
            case "01":{
                return "Jan";
            }
            case "02":{
                return "Jan";
            }
            case "03":{
                return "Jan";
            }
            case "04":{
                return "Jan";
            }
            case "05":{
                return "Jan";
            }
            case "06":{
                return "Jan";
            }
            case "07":{
                return "Jan";
            }
            case "08":{
                return "Jan";
            }
            case "09":{
                return "Jan";
            }
            case "10":{
                return "Jan";
            }
            case "11":{
                return "Jan";
            }
            case "12":{
                return "Jan";
            }
            default:{
                return "Error";
            }
        }
    }
}
