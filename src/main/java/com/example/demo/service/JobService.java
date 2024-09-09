package com.example.demo.service;

import com.example.demo.model.CronJobStatus;
import org.springframework.stereotype.Service;
import com.example.demo.model.CronJob;
import com.example.demo.model.CronJobStatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class JobService {

    // This pattern matches the cron schedule and the command part (basic example)
    private static final Pattern CRON_PATTERN = Pattern.compile("^([\\d\\*\\/\\,\\-\\s]+)\\s+(.+)$");

    private final LogMonitorService logMonitorService;

    public JobService(LogMonitorService logMonitorService) {
        this.logMonitorService = logMonitorService;
    }

    // Combined method to check the health status of all cron jobs
    public List<CronJobStatus> checkCronJobHealth() {
        List<CronJobStatus> jobStatuses = new ArrayList<>();

        // 1. Get the list of cron jobs
        List<CronJob> cronJobs = getCronJobs();

        // 2. Monitor each cron job's log and get the health status
        for (CronJob cronJob : cronJobs) {
            String logFilePath = logMonitorService.getLogFilePath(cronJob.getCommand());
            String logStatus = logMonitorService.monitorProcessLogs(logFilePath);

            // 3. Create a status object based on log monitoring
            CronJobStatus status = new CronJobStatus(cronJob.getCommand(), logStatus);
            jobStatuses.add(status);
        }

        return jobStatuses;
    }

    public List<CronJob> getCronJobs() {
        List<CronJob> cronJobs = new ArrayList<>();
        try {
            // Use ProcessBuilder to run 'crontab -l' command to list all cron jobs
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", "crontab -l");
            Process process = processBuilder.start();

            // Read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> lines = reader.lines().toList();

            // Parse each line of the crontab output
            for (String line : lines) {
                Matcher matcher = CRON_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String schedule = matcher.group(1); // Cron schedule
                    String command = matcher.group(2);  // Command/process being run
                    cronJobs.add(new CronJob(schedule, command));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            cronJobs.add(new CronJob("Error", "Error reading cron jobs: " + e.getMessage()));
        }
        return cronJobs;
    }


}
