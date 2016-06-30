package com.lks.notifications;

import com.lks.core.PortfolioModel;
import com.lks.models.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.HashMap;
import java.util.List;
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

    String templateHeaderText = "Hi ${userName},\n" +
            "\n" +
            "Your current networth is Rs ${overallWorth} (+Rs ${overallGain})\n" +
            "\n";
    String templateGainerText = "Max gainer today is: ${maxGainerShareName} - Rs ${maxGainerSharePrice} (+Rs ${maxGainerDayGain})\n" +
            "\n";

    String templateLoserText =
            "Max loser today is : ${maxLoserShareName} - Rs ${maxLoserSharePrice} (+Rs ${maxLoserDayLoss})";

    private final String fromUsername = "lksportfoliotracker@gmail.com";
    private final String password = "mydestiny";

    public void generateAndSendEmail(String fileName, List<PortfolioModel> portfolioModelList, User user) {

        MailBodyModel mailBodyModel = createMailBodyModel(portfolioModelList, user);

        String resolvedMailBody = generateEmailBody(mailBodyModel);


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromUsername, password);
                    }
                });

        try {

            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromUsername));

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

    private MailBodyModel createMailBodyModel(List<PortfolioModel> portfolioModelList, User user) {

        MailBodyModel mailBodyModel = new MailBodyModel();
        mailBodyModel.setUserName(user.getFirstName() + " " + user.getLastName());
        double overallWorth = 0.0;
        double overallGain = 0.0;
        double maxSharePrice = 0.0;
        double maxShareDayGain = 0.0;
        double minSharePrice = 0.0;
        double minShareDayLoss = 0.0;
        String maxShareName = null;
        String minShareName = null;


        for(PortfolioModel portfolioModel : portfolioModelList) {
            overallWorth += portfolioModel.getTotalValue();
            overallGain += portfolioModel.getOverallGainValue();
            if(maxSharePrice < portfolioModel.getClosingPrice()) {
                maxSharePrice = portfolioModel.getClosingPrice();
                maxShareName = portfolioModel.getStockName();
                maxShareDayGain = portfolioModel.getDaysGainValue();
            }
            if(minSharePrice > portfolioModel.getClosingPrice() && (portfolioModel.getChange() < 0)) {
                minSharePrice = portfolioModel.getOverallGainValue();
                minShareName = portfolioModel.getStockName();
                minShareDayLoss = portfolioModel.getDaysGainValue();
            }

        }

        mailBodyModel.setMaxGainerShareName(maxShareName);
        mailBodyModel.setMaxGainerDayGain(String.valueOf(maxShareDayGain));
        mailBodyModel.setMaxGainerSharePrice(String.valueOf(maxSharePrice));

        mailBodyModel.setMaxLoserShareName(minShareName);
        mailBodyModel.setMaxLoserDayLoss(String.valueOf(minShareDayLoss));
        mailBodyModel.setMaxLoserSharePrice(String.valueOf(minSharePrice));

        mailBodyModel.setOverallWorth(String.valueOf(overallWorth));
        mailBodyModel.setOverallGain(String.valueOf(overallGain));
        return mailBodyModel;
    }

    public String generateEmailBody(MailBodyModel mailBodyModel) {
        Map<String, String> valuesMap = new HashMap<String, String>();
        String resolvedHeaderString = null;
        String resolvedGainerString = null;
        String resolvedLoserString = null;
        valuesMap.put("userName", mailBodyModel.getUserName());
        valuesMap.put("overallWorth", mailBodyModel.getOverallWorth());
        valuesMap.put("overallGain", mailBodyModel.getOverallGain());
        valuesMap.put("maxGainerShareName", mailBodyModel.getMaxGainerShareName());
        valuesMap.put("maxGainerSharePrice", mailBodyModel.getMaxGainerSharePrice());
        valuesMap.put("maxGainerDayGain", mailBodyModel.getMaxGainerDayGain());
        valuesMap.put("maxLoserShareName", mailBodyModel.getMaxLoserShareName());
        valuesMap.put("maxLoserSharePrice", mailBodyModel.getMaxLoserSharePrice());
        valuesMap.put("maxLoserDayLoss", mailBodyModel.getMaxLoserDayLoss());
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        resolvedHeaderString = sub.replace(templateHeaderText);

        if(StringUtils.isBlank(mailBodyModel.getMaxGainerShareName())) {
            resolvedGainerString = "No gains today";
        } else {
            resolvedGainerString = sub.replace(templateGainerText);
        }

        if(StringUtils.isBlank(mailBodyModel.getMaxLoserShareName())) {
            resolvedLoserString = "No loses today";
        } else {
            resolvedLoserString = sub.replace(templateLoserText);
        }
        return resolvedHeaderString + resolvedGainerString + resolvedLoserString;
    }

    private static class MailBodyModel{
        private String userName;
        private String overallWorth;
        private String overallGain;
        private String maxGainerShareName;
        private String maxGainerSharePrice;
        private String maxGainerDayGain;
        private String maxLoserShareName;
        private String maxLoserSharePrice;
        private String maxLoserDayLoss;

        private String getUserName() {
            return userName;
        }

        private void setUserName(String userName) {
            this.userName = userName;
        }

        private String getOverallWorth() {
            return overallWorth;
        }

        private void setOverallWorth(String overallWorth) {
            this.overallWorth = overallWorth;
        }

        private String getOverallGain() {
            return overallGain;
        }

        private void setOverallGain(String overallGain) {
            this.overallGain = overallGain;
        }

        private String getMaxGainerShareName() {
            return maxGainerShareName;
        }

        private void setMaxGainerShareName(String maxGainerShareName) {
            this.maxGainerShareName = maxGainerShareName;
        }

        private String getMaxGainerSharePrice() {
            return maxGainerSharePrice;
        }

        private void setMaxGainerSharePrice(String maxGainerSharePrice) {
            this.maxGainerSharePrice = maxGainerSharePrice;
        }

        private String getMaxGainerDayGain() {
            return maxGainerDayGain;
        }

        private void setMaxGainerDayGain(String maxGainerDayGain) {
            this.maxGainerDayGain = maxGainerDayGain;
        }

        private String getMaxLoserShareName() {
            return maxLoserShareName;
        }

        private void setMaxLoserShareName(String maxLoserShareName) {
            this.maxLoserShareName = maxLoserShareName;
        }

        private String getMaxLoserSharePrice() {
            return maxLoserSharePrice;
        }

        private void setMaxLoserSharePrice(String maxLoserSharePrice) {
            this.maxLoserSharePrice = maxLoserSharePrice;
        }

        private String getMaxLoserDayLoss() {
            return maxLoserDayLoss;
        }

        private void setMaxLoserDayLoss(String maxLoserDayLoss) {
            this.maxLoserDayLoss = maxLoserDayLoss;
        }
    }
}
