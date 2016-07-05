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

import com.lks.PPTManagementProperties;
import com.lks.core.PortfolioModel;
import com.lks.models.User;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SMSNotification {

    @Autowired
    private PPTManagementProperties pptManagementProperties;

    private String templateHeaderText = "Dear Customer," +
            "\n" +
            "Your current networth is Rs ${overallWorth} (+Rs ${overallGain})" +
            "\n" +
            "Regards - LKSMurthy";

    public String sendSms(List<PortfolioModel> portfolioModelList, User userModel) {
        try {

            NotificationModel notificationModel = createNotificationModel(portfolioModelList, userModel);
            // Construct data
            String user = "username=" + pptManagementProperties.getSmsUsername();
            String hash = "&hash=" + pptManagementProperties.getSmsHash();
            String message = "&message=" + generateSMSMessage(notificationModel);
            String sender = "&sender=" + pptManagementProperties.getSmsSender();
            String numbers = "&numbers=" + userModel.getPhoneNumber();

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
            String data = user + hash + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }

    private NotificationModel createNotificationModel(List<PortfolioModel> portfolioModelList, User user) {

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setUserName(user.getFirstName() + " " + user.getLastName());
        double overallWorth = 0.0;
        double overallGain = 0.0;



        for(PortfolioModel portfolioModel : portfolioModelList) {
            overallWorth += portfolioModel.getTotalValue();
            overallGain += portfolioModel.getOverallGainValue();
        }

        notificationModel.setOverallWorth(String.valueOf(overallWorth));
        notificationModel.setOverallGain(String.valueOf(overallGain));
        return notificationModel;
    }

    private String generateSMSMessage(NotificationModel notificationModel) {
        Map<String, String> valuesMap = new HashMap<String, String>();
        String resolvedMessageString = null;
        valuesMap.put("overallWorth", notificationModel.getOverallWorth());
        valuesMap.put("overallGain", notificationModel.getOverallGain());
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        resolvedMessageString = sub.replace(templateHeaderText);


        return resolvedMessageString;
    }
}
