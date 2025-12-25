package com.taa.eneslab2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

class HttpReachabilityTest {

    @Test
    void exampleDotComIsReachable() throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://example.com"))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        int status = response.statusCode();
        // Accept any 2xx as reachable
        Assertions.assertTrue(status <10,
                "Expected 2xx response from https://example.com but got: " + status);
    }
}

