package com.lks.scraper;

import com.lks.core.PPTException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 24/6/16
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class NSEBhavScraper {

    private static final String bhavURL = "https://www.nseindia.com/content/historical/EQUITIES/";
    private static final String csvFolderPath = "static/";
    private static final String zipFile = "bhav.csv.zip";

    private static Logger logger = Logger.getLogger(NSEBhavScraper.class.getName());

    public void scrapeBhavFromNSE(){

        logger.info("Entered the method to scrapeBhavFromNSE");

        //Create bhav download url
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String month = getMonthForInt(calendar.get(Calendar.MONTH)).toUpperCase();
        String date = getDate();
        String fileNameInUrl = "cm"+date+month+year+zipFile;
        String downloadURl = bhavURL+year+"/"+month+"/"+fileNameInUrl;

        logger.info("Initiating download of Bhav from url: "+ downloadURl);

        downloadBhav(downloadURl);
        File file = unZipIt();
        deleteOldCSVBhavFile();
        renameUnzippedFile(file.getName());
        deleteZipFile();
    }

    private void deleteZipFile() {
        File zipBhavFile = new File(zipFile);
        if(zipBhavFile.exists()) {
            zipBhavFile.delete();
        }
    }

    private void renameUnzippedFile(String currentFileName) {
        File oldFileName = new File(csvFolderPath + currentFileName);
        File newFileName = new File(csvFolderPath + "bhav.csv");
        if(!oldFileName.renameTo(newFileName)) {
            throw new PPTException("RENAME_UNZIPPED_BHAV_FAILED: "+ oldFileName + " - "+ newFileName);
        }

    }

    private void deleteOldCSVBhavFile() {

        File file = new File(csvFolderPath + "bhav.csv");
        if(file.exists()) {
            if(!file.delete()){
                throw new PPTException("DELETE_OLD_BHAV_FAILED");
            }
        }


    }

    private void downloadBhav(String href) {
        //Open a URL Stream
        String downloadUrl = href;

        URL url = null;
        try {
            url = new URL(downloadUrl);
            InputStream in = url.openStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(zipFile));
            for (int b; (b = in.read()) != -1;) {
                out.write(b);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            throw new PPTException("BHAV_FILE_NOT_AVAILABLE : "+ downloadUrl, e);
        } catch (FileNotFoundException e) {
            throw new PPTException("BHAV_FILE_NOT_AVAILABLE : "+ downloadUrl, e);
        } catch (IOException e) {
            throw new PPTException("BHAV_FILE_NOT_AVAILABLE : "+ downloadUrl, e);
        }
    }

    /**
     * Unzip it
     *
     */
    private File unZipIt(){

        logger.info("Entered method to unzip the downloaded bhav file");

        byte[] buffer = new byte[1024];
        File unzippedFile = null;

        try{

            //create output directory is not exists
            File folder = new File(zipFile);
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                unzippedFile = new File(csvFolderPath + File.separator + fileName);

                logger.info("file unzip : "+ unzippedFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(unzippedFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(unzippedFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
            logger.info("Finished unzipping of the file: "+ unzippedFile);


        }catch(IOException ex){
            throw new PPTException("FAILED_TO_UNZIP_BHAV_FILE: "+ unzippedFile, ex);
        }
        return unzippedFile;
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String dateInSimpleFormat = dateFormat.format(cal.getTime());
        String date = dateInSimpleFormat.substring(8,10);
        return date;
    }
}
