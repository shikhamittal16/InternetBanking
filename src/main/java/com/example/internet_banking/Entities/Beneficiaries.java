package com.example.internet_banking.Entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Beneficiaries {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long beneficiaryId;
    private String beneficiaryAccountNo;
    private String beneficiaryName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "beneficiary_user_account",
            joinColumns = @JoinColumn(name = "beneficiary_id"),
            inverseJoinColumns = @JoinColumn(name = "user_account_id")
    )
    private UserAccountInfo userAccounts;
}
