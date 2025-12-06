package com.dailycodework.universalpetcare.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;

    @Value("${resend.api.key:}")
    private String resendApiKey;

    @Value("${resend.from.email:onboarding@resend.dev}")
    private String fromEmail;

    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    public void sendEmail(String to, String subject, String senderName, String mailContent) {
        try {
            // Prepare request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);

            // Prepare request body
            Map<String, Object> emailRequest = new HashMap<>();
            emailRequest.put("from", senderName + " <" + fromEmail + ">");
            emailRequest.put("to", new String[]{to});
            emailRequest.put("subject", subject);
            emailRequest.put("html", mailContent);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailRequest, headers);

            // Send email via Resend API
            ResponseEntity<String> response = restTemplate.exchange(
                RESEND_API_URL,
                HttpMethod.POST,
                request,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email sent successfully to {}", to);
            } else {
                log.error("Failed to send email. Status: {}, Response: {}",
                    response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
