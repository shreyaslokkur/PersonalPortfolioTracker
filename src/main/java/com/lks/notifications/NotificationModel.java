/*************************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 *  Copyright 2014 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by all applicable intellectual property
 * laws, including trade secret and copyright laws.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/

package com.lks.notifications;

public class NotificationModel {

    private String userName;
    private String phoneNumber;
    private String overallWorth;
    private String overallGain;
    private String overallGainPercentage;
    private String maxGainerShareName;
    private String maxGainerSharePrice;
    private String maxGainerDayGain;
    private String maxGainerDayGainPercentage;
    private String maxLoserShareName;
    private String maxLoserSharePrice;
    private String maxLoserDayLoss;
    private String maxLoserDayLossPercentage;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOverallWorth() {
        return overallWorth;
    }

    public void setOverallWorth(String overallWorth) {
        this.overallWorth = overallWorth;
    }

    public String getOverallGain() {
        return overallGain;
    }

    public void setOverallGain(String overallGain) {
        this.overallGain = overallGain;
    }

    public String getOverallGainPercentage() {
        return overallGainPercentage;
    }

    public void setOverallGainPercentage(String overallGainPercentage) {
        this.overallGainPercentage = overallGainPercentage;
    }

    public String getMaxGainerShareName() {
        return maxGainerShareName;
    }

    public void setMaxGainerShareName(String maxGainerShareName) {
        this.maxGainerShareName = maxGainerShareName;
    }

    public String getMaxGainerSharePrice() {
        return maxGainerSharePrice;
    }

    public void setMaxGainerSharePrice(String maxGainerSharePrice) {
        this.maxGainerSharePrice = maxGainerSharePrice;
    }

    public String getMaxGainerDayGain() {
        return maxGainerDayGain;
    }

    public void setMaxGainerDayGain(String maxGainerDayGain) {
        this.maxGainerDayGain = maxGainerDayGain;
    }

    public String getMaxGainerDayGainPercentage() {
        return maxGainerDayGainPercentage;
    }

    public void setMaxGainerDayGainPercentage(String maxGainerDayGainPercentage) {
        this.maxGainerDayGainPercentage = maxGainerDayGainPercentage;
    }

    public String getMaxLoserShareName() {
        return maxLoserShareName;
    }

    public void setMaxLoserShareName(String maxLoserShareName) {
        this.maxLoserShareName = maxLoserShareName;
    }

    public String getMaxLoserSharePrice() {
        return maxLoserSharePrice;
    }

    public void setMaxLoserSharePrice(String maxLoserSharePrice) {
        this.maxLoserSharePrice = maxLoserSharePrice;
    }

    public String getMaxLoserDayLoss() {
        return maxLoserDayLoss;
    }

    public void setMaxLoserDayLoss(String maxLoserDayLoss) {
        this.maxLoserDayLoss = maxLoserDayLoss;
    }

    public String getMaxLoserDayLossPercentage() {
        return maxLoserDayLossPercentage;
    }

    public void setMaxLoserDayLossPercentage(String maxLoserDayLossPercentage) {
        this.maxLoserDayLossPercentage = maxLoserDayLossPercentage;
    }
}
