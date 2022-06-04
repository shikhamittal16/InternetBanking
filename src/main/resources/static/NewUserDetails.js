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
    var obj = {
        FullName : name ,
        DOB : dob,
        SpouseName : spouse,
        FatherName : fathername,
        Address : address,
        City : city,
        State : state,
        PinCode : pincode,
        Religion : religion,
        Category : category,
        PanCardNumber : panCard,
        AadharNo : aadhar,
        voterId : voter,
        MobileNumber : mobile,
        AlternateMobileNo : alternateMobile,
        Gender : gender,
        MaritalStatus : marital,
        AccountType : accountType,
        Qualification : qualification,
        Occupation : occupation,
        emailId : email,
        NomineeName : nominee,
        NomineeRelation : nomineeReltn,
        NomineeDOB : nomineeDob,
        BankName : bankName,
        IFSC : Ifsc,
    };
    $.ajax({
        url : "saveNewUserDetails" ,
        type : "GET" ,
        data : obj ,
        success : function (data){
            if(data === "success"){
                window.location.href = "newUserDetails2";
            }
            else{
                swal({
                    title: "Error !",
                    text: "Please try Again .",
                    button: "OK !",
                });
            }
        }
    })
}