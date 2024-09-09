package com.example.demo.controller;

import com.example.demo.model.CronJobStatus;
import com.example.demo.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs/health")
    public List<CronJobStatus> getCronJobHealthStatus() {
        return jobService.checkCronJobHealth();
    }
}
