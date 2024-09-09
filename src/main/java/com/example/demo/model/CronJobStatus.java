package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CronJobStatus {
    private String command;
    private String status;

    public CronJobStatus(String command, String status) {
        this.command = command;
        this.status = status;
    }

    @Override
    public String toString() {
        return "CronJobStatus{" +
                "command='" + command + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
