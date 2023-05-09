package com.exchange_rates.api.service;

import com.exchange_rates.api.dto.ConnectionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class Connector {
    public ConnectionInfo connect(URL url) {
        ConnectionInfo connectionInfo = new ConnectionInfo(100100, "Site not available", null);
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connectionInfo.setResponseCode(connection.getResponseCode());
            connectionInfo.setResponseMessage(connection.getResponseMessage());
            connectionInfo.setContent(new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
            connection.disconnect();
        } catch (IOException e) {
            log.error("Connection error: ", e);
        }
        return connectionInfo;
    }
}
