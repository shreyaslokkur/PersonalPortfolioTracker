package com.lks.generator;

import com.lks.core.BhavModel;
import com.lks.core.PortfolioModel;
import com.lks.core.StockAveragedInvestmentModel;
import com.lks.generator.ExcelGenerator;
import com.lks.models.Equities;
import com.lks.models.Investments;
import com.lks.models.User;
import com.lks.models.dao.EquitiesDao;
import com.lks.models.dao.InvestmentsDao;
import com.lks.models.dao.UserDao;
import com.lks.notifications.EmailNotification;
import com.lks.parser.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 28/6/16
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioGenerator {

    @Autowired
    UserDao userDao;

    @Autowired
    InvestmentsDao investmentsDao;

    @Autowired
    EquitiesDao equitiesDao;

    @Autowired
    CSVParser csvParser;

    @Autowired
    ExcelGenerator excelGenerator;

    @Autowired
    EmailNotification emailNotification;

    private static Logger logger = Logger.getLogger(PortfolioGenerator.class.getName());

    public Map<User, List<PortfolioModel>> generatePortfolioForAllUsers(Map<String,BhavModel> bhavModelMap) {

        //Get all users from the user database
        Iterable<User> users = userDao.findAll();

        logger.info("Retrieved all the users from the database");

        Map<User, List<PortfolioModel>> userInvestmentsMap = new HashMap<User, List<PortfolioModel>>();

        for(User user : users) {
            logger.info("Retrieving investments for the user : "+ user.getEmail());
            List<Investments> investmentsList = investmentsDao.findByUserId(user.getId());

            logger.info("Number of investments retrieved for the user: "+ user.getEmail() + " is :" + investmentsList.size());
            //createAverageOfAllInvestments
            Map<String, StockAveragedInvestmentModel> averageOfAllInvestments = createAverageOfAllInvestments(investmentsList);
            List<PortfolioModel> portfolioModelList = new ArrayList<PortfolioModel>();
            userInvestmentsMap.put(user, portfolioModelList);
            PortfolioModel portfolioModel = null;
            for(String isinNumber: averageOfAllInvestments.keySet()) {
                logger.info("Creating portfolio model for the isin number :" + isinNumber+" for the user: "+ user.getEmail());
                Equities equitiesDaoByIsinNumber = equitiesDao.findByIsinNumber(isinNumber);
                StockAveragedInvestmentModel stockAveragedInvestmentModel = averageOfAllInvestments.get(isinNumber);
                BhavModel bhavModel = bhavModelMap.get(isinNumber);
                portfolioModel = new PortfolioModel();
                //calculate change
                portfolioModel.setStockName(equitiesDaoByIsinNumber.getNameOfCompany());
                portfolioModel.setChange(calculateChange(bhavModel.getOpen(), bhavModel.getClose()));
                portfolioModel.setTotalValue(calculateTotal(stockAveragedInvestmentModel.getQuantity(), stockAveragedInvestmentModel.getAveragePrice()));
                portfolioModel.setClosingPrice(bhavModel.getClose());
                portfolioModel.setDaysGainValue(calculateGainValue(bhavModel.getOpen(), bhavModel.getClose()));
                portfolioModel.setDaysGainPercentage(calculateGainPercentage(bhavModel.getOpen(), bhavModel.getClose()));
                portfolioModel.setInvestedPrice(stockAveragedInvestmentModel.getAveragePrice());
                portfolioModel.setOverallGainValue(calculateGainValue(stockAveragedInvestmentModel.getAveragePrice(), bhavModel.getClose()));
                portfolioModel.setOverallGainPercentage(calculateGainPercentage(stockAveragedInvestmentModel.getAveragePrice(), bhavModel.getClose()));
                portfolioModel.setQuantity(stockAveragedInvestmentModel.getQuantity());
                portfolioModelList.add(portfolioModel);
            }

        }

        return userInvestmentsMap;


    }

    private Map<String, StockAveragedInvestmentModel> createAverageOfAllInvestments(List<Investments> investmentsList) {
        Map<String, StockAveragedInvestmentModel> averagedInvestmentModelMap = new HashMap<String, StockAveragedInvestmentModel>();
        for(Investments investments : investmentsList) {
            StockAveragedInvestmentModel stockAveragedInvestmentModel = null;
            if(!averagedInvestmentModelMap.containsKey(investments.getIsinNumber())) {
                stockAveragedInvestmentModel = new StockAveragedInvestmentModel();
                stockAveragedInvestmentModel.setQuantity(investments.getQuantity());
                stockAveragedInvestmentModel.setAveragePrice(investments.getInvestmentPrice());
                stockAveragedInvestmentModel.setIsinNumber(investments.getIsinNumber());
                stockAveragedInvestmentModel.setStockName(investments.getStockName());
                stockAveragedInvestmentModel.setStockSymbol(investments.getStockSymbol());
                averagedInvestmentModelMap.put(investments.getIsinNumber(), stockAveragedInvestmentModel);
            } else {
                stockAveragedInvestmentModel = averagedInvestmentModelMap.get(investments.getIsinNumber());
                int quantity = stockAveragedInvestmentModel.getQuantity() + investments.getQuantity();
                double averagePrice = ((stockAveragedInvestmentModel.getAveragePrice() * stockAveragedInvestmentModel.getQuantity()) + (investments.getInvestmentPrice() * investments.getQuantity()))/ quantity;
                stockAveragedInvestmentModel.setQuantity(quantity);
                stockAveragedInvestmentModel.setAveragePrice(round(averagePrice,2));

            }

        }
        return averagedInvestmentModelMap;
    }

    private double calculateGainPercentage(double open, double close) {
        return round((((close - open) / open) * 100), 2);
    }

    private double calculateGainValue(double open, double close) {
        return round((close - open),2);
    }

    private double calculateChange(double open, double close) {
        return round((close - open), 2);
    }

    private double calculateTotal(int quantity, double averagePrice) {
        return round((quantity * averagePrice), 2);
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
