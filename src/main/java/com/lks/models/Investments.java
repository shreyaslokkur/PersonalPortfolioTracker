package com.lks.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 24/6/16
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "investments")
public class Investments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private int userId;

    @NotNull
    private String isinNumber;

    private String stockName;

    private String stockSymbol;

    @NotNull
    private double investmentPrice;

    @NotNull
    private Date purchaseDate;

    @NotNull
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public double getInvestmentPrice() {
        return investmentPrice;
    }

    public void setInvestmentPrice(double investmentPrice) {
        this.investmentPrice = investmentPrice;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
