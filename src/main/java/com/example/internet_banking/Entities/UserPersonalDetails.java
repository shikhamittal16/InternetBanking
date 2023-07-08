package com.example.internet_banking.Entities;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPersonalDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private String FullName;
    private String DOB;
    private String SpouseName;
    private String FatherName;
    private String Address;
    private String City;
    private String State;
    private String aadhar;
    private String PinCode;
    private String Religion;
    private String Category;
    private String PanCardNumber;
    private String voterId;
    private String AlternateMobileNo;
    private String mobileNo;
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
    @OneToOne(mappedBy = "userDetails" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserAccountInfo userAccountInfo;
    @JoinColumn(name = "accountNo")
    @OneToOne(fetch = FetchType.LAZY)
    private UserAccountInfo accountInfo;
}

