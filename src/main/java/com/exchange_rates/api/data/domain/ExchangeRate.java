package com.exchange_rates.api.data.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@MappedSuperclass
@NoArgsConstructor
@Data
public class ExchangeRate {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @CreationTimestamp
   private Date createDate;

   @UpdateTimestamp
   private Date lastModifiedDate;

   @NotBlank
   private String currencyCodeA;

   @NotBlank
   private String currencyCodeB;

   @NotNull
   private Date date;

   @NotNull
   private Double rateSell;

   @NotNull
   private Double rateBuy;

   @NotNull
   private Double rateCross;
}
