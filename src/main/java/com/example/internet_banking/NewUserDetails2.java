package com.example.internet_banking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewUserDetails2 implements Serializable {
    private static final long serialVersionUID = -1075011096034744674L;
    @EmbeddedId
    private NewUserDetailsPK newUserDetailsPK;
    @Column(unique = true , nullable = false)
    private String loginId;
    private String password;
    private BigDecimal Amount;

}
