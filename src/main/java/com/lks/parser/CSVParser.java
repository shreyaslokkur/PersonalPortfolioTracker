package com.lks.parser;

import com.lks.core.BhavModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 24/6/16
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSVParser {


    private final static int SYMBOL_POS = 0;
    private final static int SERIES_POS = 1;
    private final static int OPEN_POS = 2;
    private final static int HIGH_POS = 3;
    private final static int LOW_POS = 4;
    private final static int CLOSE_POS = 5;
    private final static int LAST_POS = 6;
    private final static int PREVCLOSE_POS = 7;
    private final static int TOTTRDQTY_POS = 8;
    private final static int TOTTRDVAL_POS = 9;
    private final static int TIMESTAMP_POS = 10;
    private final static int TOTALTRADES_POS = 11;
    private final static int ISIN_POS = 12;

    public Map<String, BhavModel> parseCSV() {

        BufferedReader crunchifyBuffer = null;
        String line = "";
        String splitBy = ",";
        Map<String,BhavModel> bhavModelMap = new HashMap<String, BhavModel>();

        try {
            String crunchifyLine;

            File csvFile = new File("static/bhav.csv");
            crunchifyBuffer = new BufferedReader(new FileReader(csvFile));

            //skipLine
            crunchifyBuffer.readLine();

            BhavModel bhavModel = null;
            while ((crunchifyLine = crunchifyBuffer.readLine()) != null) {
                String[] bhav = crunchifyLine.split(splitBy);
                bhavModel = new BhavModel();
                bhavModel.setSymbol(bhav[SYMBOL_POS]);
                bhavModel.setSeries(bhav[SERIES_POS]);
                bhavModel.setOpen(Double.parseDouble(bhav[OPEN_POS]));
                bhavModel.setHigh(Double.parseDouble(bhav[HIGH_POS]));
                bhavModel.setLow(Double.parseDouble(bhav[LOW_POS]));
                bhavModel.setClose(Double.parseDouble(bhav[CLOSE_POS]));
                bhavModel.setLast(Double.parseDouble(bhav[LAST_POS]));
                bhavModel.setPrevClose(Double.parseDouble(bhav[PREVCLOSE_POS]));
                bhavModel.setTotalTradedQuantity(Integer.parseInt(bhav[TOTTRDQTY_POS]));
                bhavModel.setTotalTradedValue(Double.parseDouble(bhav[TOTTRDVAL_POS]));
                bhavModel.setTimeStamp(bhav[TIMESTAMP_POS]);
                bhavModel.setTotalTrades(bhav[TOTALTRADES_POS]);
                bhavModel.setIsin(bhav[ISIN_POS]);
                bhavModelMap.put(bhav[ISIN_POS], bhavModel);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (crunchifyBuffer != null) crunchifyBuffer.close();
            } catch (IOException crunchifyException) {
                crunchifyException.printStackTrace();
            }
        }
        return bhavModelMap;
    }


}
