package com.lks;

import com.lks.core.PortfolioGenerator;
import com.lks.generator.ExcelGenerator;
import com.lks.notifications.EmailNotification;
import com.lks.parser.CSVParser;
import com.lks.scraper.NSEBhavScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PersonalPortfolioTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalPortfolioTrackerApplication.class, args);
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
}
