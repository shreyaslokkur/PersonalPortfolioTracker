package com.lks;

import com.lks.core.BeanConfiguration;
import com.lks.core.PPTException;
import com.lks.generator.PortfolioGenerator;
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

import java.util.logging.Logger;

@SpringBootApplication
public class PersonalPortfolioTrackerApplication {

    static Logger logger = Logger.getLogger(PersonalPortfolioTrackerApplication.class.getName());

	public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(BeanConfiguration.class, args);
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
            logger.info("Unable to start scheduler for job" + e.getMessage());
            e.printStackTrace();
        } catch (PPTException e) {
            logger.info("Encountered a fatal exception : "+ e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Encountered an exception running the scheduler : "+ e.getMessage());
            e.printStackTrace();
        }

    }


}
