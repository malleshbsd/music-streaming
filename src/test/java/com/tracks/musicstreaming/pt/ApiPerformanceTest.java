package com.tracks.musicstreaming.pt;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ApiPerformanceTest {

    private static final String API_URL = "http://localhost:8080/music/platform/v1/tracks/sorted";
    private static final int NUM_REQUESTS = 100;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool size

        for (int i = 0; i < NUM_REQUESTS; i++) {
            executor.submit(() -> {
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    long startTime = System.currentTimeMillis();

                    HttpGet request = new HttpGet(API_URL);
                    HttpResponse response = httpClient.execute(request);

                    long duration = System.currentTimeMillis() - startTime;
                    System.out.println("Response Code: " + response.getStatusLine().getStatusCode() + " | Duration: " + duration + " ms");
                } catch (IOException e) {
                    System.err.println("Request failed: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}