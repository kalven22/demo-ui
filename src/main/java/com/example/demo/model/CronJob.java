package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CronJob {
    private String schedule;
    private String command;

    public CronJob(String schedule, String command) {
        this.schedule = schedule;
        this.command = command;
    }

    @Override
    public String toString() {
        return "CronJob{" +
                "schedule='" + schedule + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
