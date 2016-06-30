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
@Table(name = "equities")
public class Equities {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int paidUpValue;

    @NotNull
    private String isinNumber;

    @NotNull
    private String symbol;

    @NotNull
    private String nameOfCompany;

    private String series;

    private int marketLot;

    private int faceValue;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaidUpValue() {
        return paidUpValue;
    }

    public void setPaidUpValue(int paidUpValue) {
        this.paidUpValue = paidUpValue;
    }

    public String getIsinNumber() {
        return isinNumber;
    }

    public void setIsinNumber(String isinNumber) {
        this.isinNumber = isinNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNameOfCompany() {
        return nameOfCompany;
    }

    public void setNameOfCompany(String nameOfCompany) {
        this.nameOfCompany = nameOfCompany;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getMarketLot() {
        return marketLot;
    }

    public void setMarketLot(int marketLot) {
        this.marketLot = marketLot;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(int faceValue) {
        this.faceValue = faceValue;
    }
}
