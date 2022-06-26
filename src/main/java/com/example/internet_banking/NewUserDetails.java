package com.example.internet_banking;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewUserDetails implements Serializable {
    private static final long serialVersionUID = -1075011096034744674L;
    @EmbeddedId
    private NewUserDetailsPK newUserDetailsPK;
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
    private String voterId;
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
    @JoinColumns({
            @JoinColumn(name = "ACCOUNT_NO", referencedColumnName = "ACCOUNT_NO", insertable = false, updatable = false),
            @JoinColumn(name = "AADHAR_NO", referencedColumnName = "AADHAR_NO", insertable = false, updatable = false),
            @JoinColumn(name = "MOBILE_NO" , referencedColumnName = "MOBILE_NO" , insertable = false , updatable = false)})
    @OneToOne(optional = false , fetch = FetchType.LAZY)
    private NewUserDetails2 newUserDetails2;
}

