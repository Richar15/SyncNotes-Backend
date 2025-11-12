package com.unicartagena.SyncNotes.hackrender;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
@EnableScheduling
public class CronJob {
    @Scheduled(cron = "0 */5 * * * *")
    public void executeTask() {
        System.out.println("🕒 Ejecutando tarea automática cada 5 minutos: " + LocalDateTime.now());
    }
}
