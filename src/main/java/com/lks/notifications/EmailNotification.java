package com.lks.notifications;

import com.lks.PPTManagementProperties;
import com.lks.core.PortfolioModel;
import com.lks.models.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 24/6/16
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailNotification {

    @Autowired
    private PPTManagementProperties pptManagementProperties;

    private String greetingsText = "<p>Hi <strong>${userName}</strong>,</p>";

    private String templateHeaderText = " <p><span style=\"color:#646464;font-size:20px;font-family:arial;font-style:normal\">Your current networth </span><img src=\"${overallGainImage}\" width=\"24\" height=\"24\"> </p>" +
            "<p><span style=\"color:##444444;font-size:35px;font-weight:bold;font-style:normal\"> Rs ${overallWorth}</span> " +
            "<span style=\"color:${overallGainColor};font-size:25px;font-weight:bold;font-style:normal\">Rs ${overallGain} (${overallGainPercentage}%)</span></p>"+
            "<hr>";

    private String templateGainerText = "<p><span style=\"color:#646464;font-size:20px;font-family:arial;font-style:normal\">Max gainer <span></p>" +
            "<p><span style=\"color:#1e5695;font-size:20px;font-family:arial;font-style:normal; font-weight:bold;\"> ${maxGainerShareName} </span><img src=\"https://s32.postimg.org/4u5opm0ol/1469482066_go_up.png\" width=\"24\" height=\"24\"> </p>" +
            "<p><span style=\"color:##444444;font-size:16px;font-weight:bold;font-style:normal\"> Rs ${maxGainerSharePrice} </span></p>" +
            "<p><span style=\"color:#008000;font-size:14px;font-weight:bold;font-style:normal\">Rs ${maxGainerDayGain} (${maxGainerDayGainPercentage}%)</span></p>"+
            "<hr>";


    private String templateLoserText = "<p><span style=\"color:#646464;font-size:24px;font-family:arial;font-style:normal\">Max Loser </span></p>" +
            "<p><span style=\"color:#1e5695;font-size:20px;font-family:arial;font-style:normal; font-weight:bold;\"> ${maxLoserShareName} </span><img src=\"https://s31.postimg.org/o82gn80jf/1469483895_down_arrow.png\" width=\"24\" height=\"24\"> </p>" +
            "<p><span style=\"color:##444444;font-size:16px;font-weight:bold;font-style:normal\"> Rs ${maxLoserSharePrice}</span></p> " +
            "<p><span style=\"color:#de0000;font-size:14px;font-weight:bold;font-style:normal\">Rs ${maxLoserDayLoss} (${maxLoserDayLossPercentage}%)</span></p>";

    public void generateAndSendEmail(String fileName, List<PortfolioModel> portfolioModelList, User user) {

        NotificationModel notificationModel = createNotificationModel(portfolioModelList, user);

        String resolvedMailBody = generateEmailBody(notificationModel);


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(pptManagementProperties.getEmailUsername(), pptManagementProperties.getEmailPassword());
                    }
                });

        try {

            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(pptManagementProperties.getEmailPassword()));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));

            // Set Subject: header field
            message.setSubject("Portfolio Tracker");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(resolvedMailBody);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

    private NotificationModel createNotificationModel(List<PortfolioModel> portfolioModelList, User user) {

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setUserName(user.getFirstName() + " " + user.getLastName());
        double overallInvestedPrice = 0.0;
        double overallGainPercentage = 0.0;
        double overallWorth = 0.0;
        double overallGain = 0.0;
        double maxSharePrice = 0.0;
        double maxShareDayGain = 0.0;
        double maxShareGainPercentage = 0.0;
        double minSharePrice = 0.0;
        double minShareDayLoss = 0.0;
        double minShareLossPercentage = 0.0;
        String maxShareName = null;
        String minShareName = null;


        for(PortfolioModel portfolioModel : portfolioModelList) {
            overallInvestedPrice += (portfolioModel.getInvestedPrice()* portfolioModel.getQuantity());
            overallWorth += portfolioModel.getTotalValue();
            overallGain += portfolioModel.getOverallGainValue();
            if(maxShareGainPercentage < portfolioModel.getDaysGainPercentage()) {
                maxSharePrice = portfolioModel.getClosingPrice();
                maxShareName = portfolioModel.getStockName();
                maxShareDayGain = portfolioModel.getDaysGainValue();
                maxShareGainPercentage = portfolioModel.getDaysGainPercentage();
            }
            if(minShareLossPercentage > portfolioModel.getDaysGainPercentage()) {
                minSharePrice = portfolioModel.getClosingPrice();
                minShareName = portfolioModel.getStockName();
                minShareDayLoss = portfolioModel.getDaysGainValue();
                minShareLossPercentage = portfolioModel.getDaysGainPercentage();
            }

        }

        overallGainPercentage = round(((overallGain / overallInvestedPrice) * 100),2);

        notificationModel.setMaxGainerShareName(maxShareName);
        notificationModel.setMaxGainerDayGain(String.valueOf(maxShareDayGain));
        notificationModel.setMaxGainerSharePrice(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(maxSharePrice)));
        notificationModel.setMaxGainerDayGainPercentage(String.valueOf(maxShareGainPercentage));

        notificationModel.setMaxLoserShareName(minShareName);
        notificationModel.setMaxLoserDayLoss(String.valueOf(minShareDayLoss));
        notificationModel.setMaxLoserSharePrice(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(minSharePrice)));
        notificationModel.setMaxLoserDayLossPercentage(String.valueOf(minShareLossPercentage));

        notificationModel.setOverallWorth(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(overallWorth)));
        notificationModel.setOverallGain(String.valueOf(overallGain));
        notificationModel.setOverallGainPercentage(String.valueOf(overallGainPercentage));
        return notificationModel;
    }

    private String generateEmailBody(NotificationModel notificationModel) {
        Map<String, String> valuesMap = new HashMap<String, String>();
        String resolvedGreetingString = null;
        String resolvedHeaderString = null;
        String resolvedGainerString = null;
        String resolvedLoserString = null;
        valuesMap.put("userName", notificationModel.getUserName());
        valuesMap.put("overallWorth", notificationModel.getOverallWorth());
        valuesMap.put("overallGain", notificationModel.getOverallGain());
        valuesMap.put("overallGainPercentage", notificationModel.getOverallGainPercentage());
        if(Double.parseDouble(notificationModel.getOverallGain()) > 0 ) {
            valuesMap.put("overallGainImage","https://s32.postimg.org/4u5opm0ol/1469482066_go_up.png");
            valuesMap.put("overallGainColor", "#008000");
        } else {
            valuesMap.put("overallGainImage","https://s31.postimg.org/o82gn80jf/1469483895_down_arrow.png");
            valuesMap.put("overallGainColor", "#de0000");
        }
        valuesMap.put("maxGainerShareName", notificationModel.getMaxGainerShareName());
        valuesMap.put("maxGainerSharePrice", notificationModel.getMaxGainerSharePrice());
        valuesMap.put("maxGainerDayGain", notificationModel.getMaxGainerDayGain());
        valuesMap.put("maxGainerDayGainPercentage", notificationModel.getMaxGainerDayGainPercentage());
        valuesMap.put("maxLoserShareName", notificationModel.getMaxLoserShareName());
        valuesMap.put("maxLoserSharePrice", notificationModel.getMaxLoserSharePrice());
        valuesMap.put("maxLoserDayLoss", notificationModel.getMaxLoserDayLoss());
        valuesMap.put("maxLoserDayLossPercentage", notificationModel.getMaxLoserDayLossPercentage());
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        resolvedGreetingString = sub.replace(greetingsText);
        resolvedHeaderString = sub.replace(templateHeaderText);

        if(StringUtils.isBlank(notificationModel.getMaxGainerShareName())) {
            resolvedGainerString = "<span style=\"color:#646464;font-size:20px;font-family:arial;font-style:normal\">No gains today</span>";
        } else {
            resolvedGainerString = sub.replace(templateGainerText);
        }

        if(StringUtils.isBlank(notificationModel.getMaxLoserShareName())) {
            resolvedLoserString = "<span style=\"color:#646464;font-size:20px;font-family:arial;font-style:normal\">No loses today</span>";
        } else {
            resolvedLoserString = sub.replace(templateLoserText);
        }
        return resolvedGreetingString + resolvedHeaderString + resolvedGainerString + resolvedLoserString;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
