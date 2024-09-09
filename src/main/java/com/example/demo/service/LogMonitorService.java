package com.example.demo.service;

import com.example.demo.model.CronJob;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class LogMonitorService {

    // Monitor logs for a given process (using log file path as input)
    public String monitorProcessLogs(String logFilePath) {
        try {
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                return "Log file not found: " + logFilePath;
            }

            // Tail the log file and check for issues
            ProcessBuilder processBuilder = new ProcessBuilder("tail", "-f", logFilePath);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder logResult = new StringBuilder();

            reader.lines().forEach(line -> {
                if (line.contains("ERROR") || line.contains("exit")) {
                    logResult.append("Error: ").append(line).append("\n");
                }
            });

            if (logResult.length() > 0) {
                return logResult.toString(); // If errors found
            } else {
                return "OK"; // No errors
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error while reading logs: " + e.getMessage();
        }
    }

    // Monitor logs for all cron jobs
    public void monitorAllCronJobLogs(List<CronJob> cronJobs) {
        for (CronJob cronJob : cronJobs) {
            String logFilePath = getLogFilePath(cronJob.getCommand());
            monitorProcessLogs(logFilePath);
        }
    }

    // Mock method to get log file path from command
    public String getLogFilePath(String command) {
        // Mock example: create a log file path based on the process command
        return "/var/log/" + command.split(" ")[0] + ".log";
    }
}
