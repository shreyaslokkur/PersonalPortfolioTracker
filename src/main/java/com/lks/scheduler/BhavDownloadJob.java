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

package com.lks.scheduler;

import com.lks.core.BhavModel;
import com.lks.core.PortfolioModel;
import com.lks.generator.ExcelGenerator;
import com.lks.generator.PortfolioGenerator;
import com.lks.models.User;
import com.lks.notifications.EmailNotification;
import com.lks.parser.CSVParser;
import com.lks.scraper.NSEBhavScraper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BhavDownloadJob {

    @Autowired
    CSVParser csvParser;

    @Autowired
    NSEBhavScraper nseBhavScraper;

    @Autowired
    EmailNotification emailNotification;

    @Autowired
    ExcelGenerator excelGenerator;

    @Autowired
    PortfolioGenerator portfolioGenerator;


    @Scheduled(cron = "0 0/2 * * * ?")
    public void execute(){
            nseBhavScraper.scrapeBhavFromNSE();
        Map<String, BhavModel> bhavModelMap = csvParser.parseCSV();
        Map<User, List<PortfolioModel>> userListMap = portfolioGenerator.generatePortfolioForAllUsers(bhavModelMap);
        for(User user: userListMap.keySet()) {
            String fileName = excelGenerator.generateExcel(userListMap.get(user), user);
            emailNotification.generateAndSendEmail(fileName, userListMap.get(user), user);
        }
    }


}
