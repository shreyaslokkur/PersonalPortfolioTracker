package com.lks.core;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 28/6/16
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockAveragedInvestmentModel {

    private String isinNumber;

    private String stockName;

    private String stockSymbol;

    private double averagePrice;

    private int quantity;

    public String getIsinNumber() {
        return isinNumber;
    }

    public void setIsinNumber(String isinNumber) {
        this.isinNumber = isinNumber;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
