package com.lks;

import com.lks.core.BhavModel;
import com.lks.generator.PortfolioGenerator;
import com.lks.core.PortfolioModel;
import com.lks.generator.ExcelGenerator;
import com.lks.models.User;
import com.lks.notifications.EmailNotification;
import com.lks.parser.CSVParser;
import com.lks.scraper.NSEBhavScraper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersonalPortfolioTrackerApplication.class)
@WebAppConfiguration
public class PersonalPortfolioTrackerApplicationTests {

    @Autowired
    CSVParser csvParser;

    @Autowired
    NSEBhavScraper nseBhavScraper;

    @Autowired
    EmailNotification emailNotification;

    @Autowired
    ExcelGenerator excelGenerator;

    @Autowired
    PortfolioGenerator portfolioGenerator;

    @Test
    public void testCSVParsing(){
        Map<String,BhavModel> bhavModelMap = csvParser.parseCSV();
        Assert.assertNotNull(bhavModelMap);
        Assert.assertNotNull(bhavModelMap.get("INE021A01026"));
    }

    @Test
    public void testCSVScraping(){
        nseBhavScraper.scrapeBhavFromNSE();
        //Assert if file exists
        Assert.assertEquals(new File("static/bhav.csv").exists(), true);
    }

    @Test
    public void testSendEmail() {
        Map<String, BhavModel> bhavModelMap = csvParser.parseCSV();
        Map<User, List<PortfolioModel>> userListMap = portfolioGenerator.generatePortfolioForAllUsers(bhavModelMap);
        for(User user: userListMap.keySet()) {
            String fileName = excelGenerator.generateExcel(userListMap.get(user), user);
            emailNotification.generateAndSendEmail(fileName, userListMap.get(user), user);
        }
    }

    @Test
    public void textExcelGeneration() {
        User user = new User();
        user.setEmail("shreyaslokkur@gmail.com");
        user.setFirstName("Shreyas");
        user.setLastName("Lokkur");
        user.setId(1);
        user.setPhoneNumber("9986896450");

        List<PortfolioModel> portfolioModelList = new ArrayList<PortfolioModel>();

        PortfolioModel portfolioModel = new PortfolioModel();
        portfolioModel.setQuantity(10);
        portfolioModel.setChange(-10);
        portfolioModel.setDaysGainPercentage(-1.2);
        portfolioModel.setDaysGainValue(-0.96);
        portfolioModel.setInvestedPrice(100.1);
        portfolioModel.setStockName("Asian Paints");
        portfolioModel.setTotalValue(9960.3);
        portfolioModel.setClosingPrice(99.63);

        portfolioModelList.add(portfolioModel);

        excelGenerator.generateExcel(portfolioModelList, user);
    }

    @Test
    public void testPortfolioGeneration() {
        Map<String, BhavModel> bhavModelMap = csvParser.parseCSV();
        Map<User, List<PortfolioModel>> userListMap = portfolioGenerator.generatePortfolioForAllUsers(bhavModelMap);
        for(User user : userListMap.keySet()) {
            excelGenerator.generateExcel(userListMap.get(user), user);
        }

        Assert.assertNotNull(userListMap);
    }

}
