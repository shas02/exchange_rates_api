package com.exchange_rates.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConnectionInfo {
   private int responseCode;
   private String responseMessage;
   private String content;
}
