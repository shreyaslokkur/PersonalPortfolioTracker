package com.lks;

import com.lks.generator.ExcelGenerator;
import com.lks.generator.PortfolioGenerator;
import com.lks.notifications.EmailNotification;
import com.lks.parser.CSVParser;
import com.lks.scheduler.BhavDownloadJob;
import com.lks.scraper.NSEBhavScraper;
import org.springframework.context.annotation.Bean;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 2/7/16
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class BeanConfiguration {

    @Bean
    public CSVParser getCSVParser() {
        return new CSVParser();
    }

    @Bean
    public NSEBhavScraper getNSEBhavScraper() {
        return new NSEBhavScraper();
    }

    @Bean
    public EmailNotification getEmailNotification() {
        return new EmailNotification();
    }

    @Bean
    public ExcelGenerator getExcelGenerator() {
        return new ExcelGenerator();
    }

    @Bean
    public PortfolioGenerator getPortfolioGenerator() {
        return new PortfolioGenerator();
    }

    @Bean
    public BhavDownloadJob getBhavDownloadJob() {
        return new BhavDownloadJob();
    }

    @Bean
    public BhavTrigger getBhavDownloadTrigger() {
        return new BhavTrigger();
    }
}
