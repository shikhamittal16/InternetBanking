package com.example.internet_banking.Entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;
    private String transDescription;
    private BigDecimal transactionAmount;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date transactionDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userAccountId")
    private UserAccountInfo userTransactions;

}
