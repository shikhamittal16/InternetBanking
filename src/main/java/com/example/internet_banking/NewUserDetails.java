package com.example.internet_banking;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewUserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false , unique = true)
    private Long AccountNo;
    private String FullName;
    private String DOB;
    private String SpouseName;
    private String FatherName;
    private String Address;
    private String City;
    private String State;
    private String PinCode;
    private String Religion;
    private String Category;
    @Column(unique = true)
    private String PanCardNumber;
    @Column(unique = true)
    private String AadharNo;
    @Column(unique = true)
    private String voterId;
    private String MobileNumber;
    private String AlternateMobileNo;
    private String Gender;
    private String MaritalStatus;
    private String AccountType;
    private String Qualification;
    private String Occupation;
    private String emailId;
    private String NomineeName;
    private String NomineeRelation;
    private String NomineeDOB;
    private String BankName;
    private String IFSC;
    @Column(nullable = false)
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date createdOn;
    private String BankBranch;
    private String District;
    private String loginId;
    private String password;
    private BigDecimal Amount;


}

