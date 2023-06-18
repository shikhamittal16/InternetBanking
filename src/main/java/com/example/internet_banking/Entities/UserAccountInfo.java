package com.example.internet_banking.Entities;


import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAccountInfo {
    @Id
    private String accountNo ;
    private String loginUserName ;
    private String password ;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userDetailsId")
    private NewUserDetails userDetails ;
    private BigDecimal depositBalance ;
}
