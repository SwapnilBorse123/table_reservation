function resValidation (){}



resValidation.prototype.validateEmpName = function (name) {
	if(name == ''){
		alert("Please fill employee name")
		console.log('validateEmpName retured: ' +false);
		return false
	}else{
		console.log('validateEmpName retured: ' +true);
		return true;
	}
}

resValidation.prototype.validateEmpId = function (id) {
	var reg = new RegExp('^[\\d]+$');
	console.log('validateEmpId retured: ' +reg.test(id));
	return(reg.test(id))
}

resValidation.prototype.validateTime = function (start,end) {
	console.log('start :'+start);
	console.log('end :'+end);
	if(start >= end){
		console.log('validateTime retured: ' +false);
		return false;	
	}
	return true;
}
