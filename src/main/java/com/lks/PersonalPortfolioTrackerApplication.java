package com.lks;

import com.lks.generator.PortfolioGenerator;
import com.lks.generator.ExcelGenerator;
import com.lks.notifications.EmailNotification;
import com.lks.notifications.SMSNotification;
import com.lks.parser.CSVParser;
import com.lks.scheduler.BhavDownloadJob;
import com.lks.scraper.NSEBhavScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
public class PersonalPortfolioTrackerApplication {

    static Logger logger = Logger.getLogger(PersonalPortfolioTrackerApplication.class.getName());

	public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(PersonalPortfolioTrackerApplication.class, args);
    }

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
    public SMSNotification getSMSNotification() { return new SMSNotification(); }

    @Bean
    public PPTManagementProperties getPPTManagementProperties() { return new PPTManagementProperties(); }


}
