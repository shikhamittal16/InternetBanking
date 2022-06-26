package com.example.internet_banking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDetailsPK implements Serializable {

    @Column(name = "ACCOUNT_NO" , unique = true , nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long AccountNo;
    @Column(name = "AADHAR_NO")
    private String AadharNo;
    @Column(name = "MOBILE_NO")
    private String MobileNumber;
}
