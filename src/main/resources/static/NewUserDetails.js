function SaveNewUserDetails(){
    var name = $('#FullName').val();
    var dob= $('#dob').val();
    var gender= $('#gender').val();
    var fathername= $('#FatherName').val();
    var marital= $('#MaritalStatus').val();
    var spouse= $('#SpouseName').val();
    var mobile= $('#Phn').val();
    var alternateMobile= $('#AlternateMobileNo').val();
    var email= $('#email').val();
    var aadhar= $('#aadhar').val();
    var panCard= $('#pan').val();
    var voter= $('#voterId').val();
    var address= $('#address').val();
    var city= $('#city').val();
    var state= $('#state').val();
    var pincode= $('#PinCode').val();
    var qualification= $('#qualification').val();
    var occupation= $('#Occupation').val();
    var religion= $('#Religion').val();
    var category= $('#Category').val();
    var nominee= $('#NomineeName').val();
    var nomineeDob= $('#NomineeDOB').val();
    var nomineeReltn= $('#NomineeRelation').val();
    var accountType= $('#AccountType').val();
    var bankName= $('#BankName').val();
    var Ifsc= $('#IFSC').val();
    var District = $('#District').val();
    var BankBranch = $('#BankBranch').val();
    if(marital === ""){
        swal({
            title: "Error !",
            text: "Please Enter All the Required Details.",
            button: "OK !",
        });
    }
    if(marital === "Married" && spouse === ""){
        swal({
            title: "Error !",
            text: "Please Enter Spouse Name.",
            button: "OK !",
        });
    }
    else if(marital !== "Married" && fathername === ""){
        swal({
            title: "Error !",
            text: "Please Enter Father Name.",
            button: "OK !",
        });
    }
    else{
        var obj = {
            FullName: name,
            DOB: dob,
            SpouseName: spouse,
            FatherName: fathername,
            Address: address,
            City: city,
            State: state,
            PinCode: pincode,
            Religion: religion,
            Category: category,
            PanCardNumber: panCard,
            AadharNo: aadhar,
            voterId: voter,
            MobileNumber: mobile,
            AlternateMobileNo: alternateMobile,
            Gender: gender,
            MaritalStatus: marital,
            AccountType: accountType,
            Qualification: qualification,
            Occupation: occupation,
            emailId: email,
            NomineeName: nominee,
            NomineeRelation: nomineeReltn,
            NomineeDOB: nomineeDob,
            BankName: bankName,
            IFSC: Ifsc,
            District : District,
            BankBranch : BankBranch,
        };
        $.ajax({
            url: "saveNewUserDetails",
            type: "POST",
            data: obj,
            success: function (data) {
                if (data === "success") {
                    swal({
                        title: "Success!",
                        text: "Your Details has been Saved Successfully.",
                        timer: 1000,
                    }, function (){
                        console.log("hi")
                        window.location.href = "newUserDetails2";
                    });
                }else {
                    swal({
                        title: "Error !",
                        text: "Please try Again .",
                        button: "OK !",
                    });
                }
            }
        })
    }
}

function SaveNewUserDetails2(){
    var accountNo = $('#accountNo').val();
    var Amount = $('#Amount').val();
    var loginId = $('#loginId').val();
    var password = $('#password').val();
    if(accountNo === "" || Amount === "" || loginId === "" || password === ""){
        swal({
            title: "Error !",
            text: "Please fill All Required Details.",
            button: "OK !",
        });
    }
    else if(Amount < 1000){
        swal({
            title: "Error !",
            text: "Amount should be more than 1000.",
            button: "OK !",
        });
    }
    else{
        var obj = {
            loginId : loginId,
            password : password,
            Amount : Amount,
        }
        $.ajax({
            url: "saveNewUserDetails",
            type: "POST",
            data: obj,
            success: function (data) {
                if (data === "success") {
                        swal({
                        title: "Success!",
                        text: "Your Account has been Created Successfully.",
                        timer: 1000,
                    }, function (){
                            window.location.href = "login";
                        });
                }else {
                    swal({
                        title: "Error !",
                        text: "Please try Again .",
                        button: "OK !",
                    });
                }
            }
        })
    }

}