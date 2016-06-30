package com.lks.core;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 24/6/16
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioModel {
    private String stockName;
    private double closingPrice;
    private double change;
    private int quantity;
    private double investedPrice;
    private double daysGainValue;
    private double daysGainPercentage;
    private double overallGainValue;
    private double overallGainPercentage;
    private double totalValue;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(double closingPrice) {
        this.closingPrice = closingPrice;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getInvestedPrice() {
        return investedPrice;
    }

    public void setInvestedPrice(double investedPrice) {
        this.investedPrice = investedPrice;
    }

    public double getDaysGainValue() {
        return daysGainValue;
    }

    public void setDaysGainValue(double daysGainValue) {
        this.daysGainValue = daysGainValue;
    }

    public double getDaysGainPercentage() {
        return daysGainPercentage;
    }

    public void setDaysGainPercentage(double daysGainPercentage) {
        this.daysGainPercentage = daysGainPercentage;
    }

    public double getOverallGainValue() {
        return overallGainValue;
    }

    public void setOverallGainValue(double overallGainValue) {
        this.overallGainValue = overallGainValue;
    }

    public double getOverallGainPercentage() {
        return overallGainPercentage;
    }

    public void setOverallGainPercentage(double overallGainPercentage) {
        this.overallGainPercentage = overallGainPercentage;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
}
