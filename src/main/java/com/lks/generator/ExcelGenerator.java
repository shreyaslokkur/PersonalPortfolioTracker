package com.lks.generator;

import com.lks.core.PortfolioModel;
import com.lks.models.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 24/6/16
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExcelGenerator {

    private static final String portfolioFolder = "static/portfolios/";

    private static final List<Integer> columnsWithGainOrLoss = new ArrayList<Integer>(){{
        add(new Integer(2));
        add(new Integer(5));
        add(new Integer(6));
        add(new Integer(7));
        add(new Integer(8));
    }};

    private static final List<Integer> columnsWithPercentage = new ArrayList<Integer>() {{
        add(new Integer(6));
        add(new Integer(8));
    }};

    public String generateExcel(List<PortfolioModel> portfolioModelList, User user) {
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet(
                " Investment Info ");
        createHeader(spreadsheet);
        //Create row object
        XSSFRow row;


        XSSFFont defaultFont= workbook.createFont();
        defaultFont.setFontHeightInPoints((short)10);
        defaultFont.setFontName("Arial");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        defaultFont.setBold(false);
        defaultFont.setItalic(false);




        //Iterate over data and write to sheet
        int rowid = 1;
        Map<String, Object[]> mapOfInvestments = createMapOfInvestments(portfolioModelList);

        Cell cell = null;
        for (String key : mapOfInvestments.keySet())
        {
            row = spreadsheet.createRow(rowid++);
            Object [] objArr = mapOfInvestments.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                cell = row.createCell(cellnum);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Double) {
                    Double value = (Double) obj;
                    if(value > 0 && columnsWithGainOrLoss.contains(new Integer(cellnum))) {
                        cell.setCellStyle(getGainCellStyle(workbook));
                    } else if(value < 0 && columnsWithGainOrLoss.contains(new Integer(cellnum))) {
                        cell.setCellStyle(getLossCellStyle(workbook));
                    }

                    if(columnsWithPercentage.contains(new Integer(cellnum))) {
                        cell.setCellValue((Double)obj + "%");


                    } else {
                        cell.setCellValue((Double)obj);
                    }

                }

                else if(obj instanceof Integer) {
                    Integer value = (Integer) obj;
                    if(value > 0 && columnsWithGainOrLoss.contains(new Integer(cellnum))) {
                        cell.setCellStyle(getGainCellStyle(workbook));
                    } else if(value < 0 && columnsWithGainOrLoss.contains(new Integer(cellnum))) {
                        cell.setCellStyle(getLossCellStyle(workbook));
                    }
                    cell.setCellValue((Integer)obj);
                }
                spreadsheet.autoSizeColumn(cellnum);

                cellnum++;

            }

        }


        //Write the workbook in file system

        String fileName = createFile(workbook, user);
        return fileName;


    }

    private String createFile(XSSFWorkbook workbook, User user) {
        File folder = new File(portfolioFolder);
        if(!folder.exists()) {
            folder.mkdir();
        }
        String fileName = portfolioFolder + user.getId() + ".xls";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(
                    new File(fileName));
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return fileName;

    }

    private void createHeader(XSSFSheet spreadSheet){

        XSSFRow row = spreadSheet.createRow(0);
        CellStyle style=spreadSheet.getWorkbook().createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(getFont(spreadSheet.getWorkbook()));
        String[] headers = {"Stock Name", "Closing Price", "Change","Quantity","Invested Price","Day's Gain Value","Day's Gain %","Overall Gain Value","Overall Gain %","Total Value"};
        int cellId = 0;
        for(String headerName : headers) {
            Cell cell = row.createCell(cellId++);
            cell.setCellValue(headerName);
            cell.setCellStyle(style);
        }




    }

    private Map<String, Object[]> createMapOfInvestments(List<PortfolioModel> portfolioModelList) {
        Map<String, Object[]> data = new HashMap<String, Object[]>();
        for(PortfolioModel portfolioModel : portfolioModelList) {
            data.put(portfolioModel.getStockName(),
                     new Object[]{portfolioModel.getStockName(),
                                  portfolioModel.getClosingPrice(),
                                  portfolioModel. getChange(),
                                  portfolioModel.getQuantity(),
                                  portfolioModel.getInvestedPrice(),
                                  portfolioModel.getDaysGainValue(),
                                  portfolioModel.getDaysGainPercentage(),
                                  portfolioModel.getOverallGainValue(),
                                  portfolioModel.getOverallGainPercentage(),
                                  portfolioModel.getTotalValue()});

        }
        return data;
    }

    private XSSFFont getFont(XSSFWorkbook workbook){
        XSSFFont font= workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        font.setItalic(false);
        return font;
    }

    private CellStyle getGainCellStyle(XSSFWorkbook workbook){
        CellStyle style=workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        XSSFFont font= workbook.createFont();
        font.setColor(IndexedColors.GREEN.getIndex());
        style.setFont(font);
        return style;
    }

    private CellStyle getLossCellStyle(XSSFWorkbook workbook){
        CellStyle style=workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        XSSFFont font= workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        return style;
    }




}
