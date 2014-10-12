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

function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

function checkAge() {
	var age = document.getElementById("age").value;
	var ageMessage = document.getElementById("ageMessage");
	if (isNumber(age) && age > 0 && age <= 140 && age%1===0) {
		ageMessage.innerHTML = "";
	} else {
		ageMessage.style.color = "#ff6666";
		ageMessage.innerHTML = "&nbsp; Age can only be a digit";
		return false;
	}
}

function checkMedication() {
	var medication = document.getElementById("medication").value;
	var medicationMessage = document.getElementById("medicationMessage");
	var validChars = /^[ ]*[A-Za-z0-9][-'A-Za-z0-9 ]*$/;
	var validMed = medication.match(validChars);
	if (validMed == null) {
		medicationMessage.style.color = "#ff6666";
		medicationMessage.innerHTML = "&nbsp; Invalid input, please only enter text, and do not separate multiple medications with any special chars";
		return false;
	} else {
		medicationMessage.innerHTML = "";
	}
}

function checkAddress() {
	var address = document.getElementById("address").value;
	var addressMessage = document.getElementById("addressMessage");
	var validChars = /^[ ]*[A-Za-z0-9][A-Za-z0-9 ]*$/;
	var validAddress = address.match(validChars);
	if (validAddress == null) {
		addressMessage.style.color = "#ff6666";
		addressMessage.innerHTML = "&nbsp; Please only enter a street number and a street name";
	} else {
		addressMessage.innerHTML = "";
	}
}

function checkSuburb() {
	var suburb = document.getElementById("suburb").value;
	var suburbMessage = document.getElementById("suburbMessage");
	var validChars = /^[ ]*[A-Za-z][A-Za-z ]*$/;
	var validSuburb = suburb.match(validChars);
	if (validSuburb == null) {
		suburbMessage.style.color = "#ff6666";
		suburbMessage.innerHTML = "&nbsp; Please only enter a Suburb name";
	} else {
		suburbMessage.innerHTML = "";
	}
}

function checkConNum() {
	var mobile = document.getElementById("conNum").value;
    var mobileMessage = document.getElementById("conNumMessage");
    var validChars = /^(\+|\d)[0-9]{7,16}$/;
	var validMobile = mobile.match(validChars);
	if (validMobile == null) {
		mobileMessage.style.color = "#ff6666";
		mobileMessage.innerHTML ="&nbsp; Invalid contact number";
		return false;
	}else{
		mobileMessage.innerHTML ="";
	}
}

function checkEmergName() {
	var emergName = document.getElementById("emergName").value;
	var emergNameMessage = document.getElementById("emergNameMessage");
	var validChars = /^[ ]*[A-Za-z][A-Za-z -]*$/;
	var validEmergName = emergName.match(validChars);
	if (validEmergName == null) {
		emergNameMessage.style.color = "#ff6666";
		emergNameMessage.innerHTML = "&nbsp; Please only enter a name";
	} else {
		emergNameMessage.innerHTML = "";
	}
}

function checkEmergAddress() {
	var emergAddress = document.getElementById("emergAddress").value;
	var emergAddressMessage = document.getElementById("emergAddressMessage");
	var validChars = /^[ ]*[A-Za-z0-9][A-Za-z0-9 ]*$/;
	var validAddress = emergAddress.match(validChars);
	if (validAddress == null) {
		emergAddressMessage.style.color = "#ff6666";
		emergAddressMessage.innerHTML = "&nbsp; Please only enter a street number and a street name";
	} else {
		emergAddressMessage.innerHTML = "";
	}
}

function checkEmergSuburb() {
	var emergSuburb = document.getElementById("emergSuburb").value;
	var emergSuburbMessage = document.getElementById("emergSuburbMessage");
	var validChars = /^[ ]*[A-Za-z][A-Za-z ]*$/;
	var validSuburb = emergSuburb.match(validChars);
	if (validSuburb == null) {
		emergSuburbMessage.style.color = "#ff6666";
		emergSuburbMessage.innerHTML = "&nbsp; Please only enter a Suburb name";
	} else {
		emergSuburbMessage.innerHTML = "";
	}
}

function checkEmergNum() {
	var alternateMobile = document.getElementById("emergNum").value;
    var alternateMobileMessage = document.getElementById("emergNumMessage");
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