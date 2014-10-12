//Check that the first name uses only letters (upper and lower) and can have a hyphen or apostrophe
function checkFirstName() {
	var firstName = document.getElementById("firstName").value;
    var firstNameMessage = document.getElementById("firstNameMessage");
    var validChars = /^[a-zA-Z][-'''a-zA-Z]+$/;//regex expression for letters upper/lower and hyphen or apostrophe
	var validFirstName = firstName.match(validChars);//check that firstname input follows the regex
	//if regex fails display message to user or leave message blank
	if (validFirstName == null) {
		firstNameMessage.style.color = "#ff6666";
		firstNameMessage.innerHTML ="&nbsp; First name can only contain letters (can include a hyphen and/or apostrophe).";
		return false;
	}else{
		firstNameMessage.innerHTML ="";
	}
}


//Check that the last name uses only letters (upper and lower) and can have a hyphen or apostrophe
function checkLastName() {
	var lastName = document.getElementById("lastName").value;
    var lastNameMessage = document.getElementById("lastNameMessage");
    var validChars = /^[a-zA-Z][-'''a-zA-Z]+$/;
	var validLastName = lastName.match(validChars);
	if (validLastName == null) {
		lastNameMessage.style.color = "#ff6666";
		lastNameMessage.innerHTML ="&nbsp; Last name can only contain letters (can include a hyphen and/or apostrophe).";
		return false;
	}else{
		lastNameMessage.innerHTML ="";
	}
}

function checkMobile() {
	var mobile = document.getElementById("mobile").value;
    var mobileMessage = document.getElementById("mobileMessage");
    var validChars = /^(\+|\d)[0-9]{7,16}$/;
	var validMobile = mobile.match(validChars);
	if (validMobile == null) {
		mobileMessage.style.color = "#ff6666";
		mobileMessage.innerHTML ="&nbsp; Invalid mobile number";
		return false;
	}else{
		mobileMessage.innerHTML ="";
	}
}

function checkAlternateMobile() {
	var alternateMobile = document.getElementById("alternateMobile").value;
    var alternateMobileMessage = document.getElementById("alternateMobileMessage");
    var validChars = /^(\+|\d)[0-9]{7,16}$/;
	var validAlternateMobile = alternateMobile.match(validChars);
	if (validAlternateMobile == null) {
		alternateMobileMessage.style.color = "#ff6666";
		alternateMobileMessage.innerHTML ="&nbsp; Invalid contact number";
		return false;
	}else{
		alternateMobileMessage.innerHTML ="";
	}
}


//Check email is in a valid format - x@y.zz (character@character.atLeastTwoDigits)
function checkEmail() {
	var email = document.getElementById("email").value;
    var emailMessage = document.getElementById("emailMessage");
	var atpos = email.indexOf("@");
	var dotpos = email.lastIndexOf(".");
	if (atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
		emailMessage.style.color = "#ff6666";
  		emailMessage.innerHTML = "&nbsp Invalid email address.";
		return false;
  	}else{
  		emailMessage.innerHTML = "";
  	}
}


//Check that the username uses only letters (upper and lower) and/or numbers
function checkUsername() {
	var username = document.getElementById("username").value;
    var usernameMessage = document.getElementById("usernameMessage");
    var validChars = /^[a-zA-Z0-9]+$/;
	var validUsername = username.match(validChars);
	if (validUsername == null) {
		usernameMessage.style.color = "#ff6666";
		usernameMessage.innerHTML ="&nbsp; Username can only contain letters and/or numbers.";
		return false;
	}else{
		usernameMessage.innerHTML ="";
	}
}


//Check that the password contains only alphanumeric characters and is at least six digits long
function checkPassword() {
    var password = document.getElementById("password").value;
    var passwordMessage = document.getElementById("passwordMessage");
    var charMin = 6;
    var validChars = /^[a-zA-Z0-9]+$/;
    var validPassword = validChars.test(password);
	if (validPassword == false) {
		passwordMessage.style.color = "#ff6666";
		passwordMessage.innerHTML ="&nbsp; Invalid character(s) used.";
		return false;
	}
    else if(password.length < charMin) {
    	passwordMessage.style.color = "#ff6666";
		passwordMessage.innerHTML ="&nbsp; Password must be at least six digits long.";
        return false;
    } else {
		passwordMessage.innerHTML = "";
	}
}

//Check that the passwords match - adds span text to right of input to confirm
function checkConfirmPassword() {
    var password = document.getElementById("password");
    var confirmPassword = document.getElementById("confirmPassword");
    var toMatch = "#66cc66";
    var notToMatch = "#ff6666";
    if (password.value == confirmPassword.value) {
        confirmPasswordMessage.style.color = toMatch;
        confirmPasswordMessage.innerHTML = "&nbsp; Passwords match."
    }else{
        confirmPasswordMessage.style.color = notToMatch;
        confirmPasswordMessage.innerHTML = "&nbsp; Passwords do not match."
    }
}