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

    @Autowired
    private PPTManagementProperties pptManagementProperties;

    private String templateHeaderText = "Hi ${userName},\n" +
            "\n" +
            "Your current networth is Rs ${overallWorth} (+Rs ${overallGain})\n" +
            "\n";
    private String templateGainerText = "Max gainer today is: ${maxGainerShareName} - Rs ${maxGainerSharePrice} (+Rs ${maxGainerDayGain})\n" +
            "\n";

    private String templateLoserText =
            "Max loser today is : ${maxLoserShareName} - Rs ${maxLoserSharePrice} (+Rs ${maxLoserDayLoss})";

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

        notificationModel.setMaxGainerShareName(maxShareName);
        notificationModel.setMaxGainerDayGain(String.valueOf(maxShareDayGain));
        notificationModel.setMaxGainerSharePrice(String.valueOf(maxSharePrice));

        notificationModel.setMaxLoserShareName(minShareName);
        notificationModel.setMaxLoserDayLoss(String.valueOf(minShareDayLoss));
        notificationModel.setMaxLoserSharePrice(String.valueOf(minSharePrice));

        notificationModel.setOverallWorth(String.valueOf(overallWorth));
        notificationModel.setOverallGain(String.valueOf(overallGain));
        return notificationModel;
    }

    private String generateEmailBody(NotificationModel notificationModel) {
        Map<String, String> valuesMap = new HashMap<String, String>();
        String resolvedHeaderString = null;
        String resolvedGainerString = null;
        String resolvedLoserString = null;
        valuesMap.put("userName", notificationModel.getUserName());
        valuesMap.put("overallWorth", notificationModel.getOverallWorth());
        valuesMap.put("overallGain", notificationModel.getOverallGain());
        valuesMap.put("maxGainerShareName", notificationModel.getMaxGainerShareName());
        valuesMap.put("maxGainerSharePrice", notificationModel.getMaxGainerSharePrice());
        valuesMap.put("maxGainerDayGain", notificationModel.getMaxGainerDayGain());
        valuesMap.put("maxLoserShareName", notificationModel.getMaxLoserShareName());
        valuesMap.put("maxLoserSharePrice", notificationModel.getMaxLoserSharePrice());
        valuesMap.put("maxLoserDayLoss", notificationModel.getMaxLoserDayLoss());
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        resolvedHeaderString = sub.replace(templateHeaderText);

        if(StringUtils.isBlank(notificationModel.getMaxGainerShareName())) {
            resolvedGainerString = "No gains today";
        } else {
            resolvedGainerString = sub.replace(templateGainerText);
        }

        if(StringUtils.isBlank(notificationModel.getMaxLoserShareName())) {
            resolvedLoserString = "No loses today";
        } else {
            resolvedLoserString = sub.replace(templateLoserText);
        }
        return resolvedHeaderString + resolvedGainerString + resolvedLoserString;
    }


}
