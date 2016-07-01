package com.lks;

import com.lks.core.PortfolioGenerator;
import com.lks.generator.ExcelGenerator;
import com.lks.notifications.EmailNotification;
import com.lks.parser.CSVParser;
import com.lks.scheduler.BhavDownloadJob;
import com.lks.scheduler.BhavTrigger;
import com.lks.scraper.NSEBhavScraper;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PersonalPortfolioTrackerApplication {

	public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(PersonalPortfolioTrackerApplication.class, args);
        createScheduler(applicationContext);
    }

    private static void createScheduler(ApplicationContext applicationContext) {

        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            BhavTrigger trigger = applicationContext.getBean(BhavTrigger.class);
            JobDetail job = JobBuilder.newJob(BhavDownloadJob.class)
                    .withIdentity("bhavDownloadJob", "group1").build();
            scheduler.scheduleJob(job, trigger.getTriggerForBhavDownload());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

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
    public BhavTrigger getBhavDownloadTrigger() {
        return new BhavTrigger();
    }
}
