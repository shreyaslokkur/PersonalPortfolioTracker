package com.lks.generator;

import com.lks.core.PortfolioModel;
import com.lks.models.User;
import org.apache.poi.ss.usermodel.Cell;
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

    public String generateExcel(List<PortfolioModel> portfolioModelList, User user) {
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet(
                " Investment Info ");
        createHeader(spreadsheet);
        //Create row object
        XSSFRow row;
        //Iterate over data and write to sheet
        int rowid = 1;
        Map<String, Object[]> mapOfInvestments = createMapOfInvestments(portfolioModelList);
        for (String key : mapOfInvestments.keySet())
        {
            row = spreadsheet.createRow(rowid++);
            Object [] objArr = mapOfInvestments.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Double)
                    cell.setCellValue((Double)obj);
                else if(obj instanceof Integer) {
                    cell.setCellValue((Integer)obj);
                }
            }

        }
        //Write the workbook in file system

        String fileName = createFile(workbook, user);
        return fileName;


    }

    private String createFile(XSSFWorkbook workbook, User user) {
        String fileName = "static/portfolios/" + user.getId() + ".xls";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(
                    new File(fileName));
            workbook.write(out);
            out.close();
            System.out.println(
                    "Writesheet.xlsx written successfully" );
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return fileName;

    }

    private void createHeader(XSSFSheet spreadSheet){

        XSSFRow row = spreadSheet.createRow(0);
        String[] headers = {"Stock Name", "Closing Price", "Change","Quantity","Invested Price","Day's Gain Value","Day's Gain %","Overall Gain Value","Overall Gain %","Total Value"};
        int cellId = 0;
        for(String headerName : headers) {
            Cell cell = row.createCell(cellId++);
            cell.setCellValue(headerName);
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




}
