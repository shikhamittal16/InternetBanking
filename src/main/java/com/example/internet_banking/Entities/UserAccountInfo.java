package com.example.internet_banking.Entities;


import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAccountInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userAccountId;
    private String accountNo ;
    private String loginUserName ;
    private String password ;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userDetailsId")
    private NewUserDetails userDetails ;
    private BigDecimal depositBalance ;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String crnNumber ;
    private String crnName ;
    private String currency ;
    private BigDecimal withdrawableBalance;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccounts")
    private List<Beneficiaries> beneficiaries;
    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "userTransactions")
    private List<Transactions> transactions;
}
