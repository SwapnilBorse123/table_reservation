function sendAjax(){
	try{	
					
		console.log('Function called');
		var trade = document.getElementById("tradeDDL");
		var deal = trade.options[trade.selectedIndex].text;
		var unwindstatus=document.getElementById("unwindCheckbox").checked;
		if(unwindstatus == true){
			handleUpfrontTermination(deal);
			console.log("Ajax Called");
			$("#unwind-ddl").removeClass("no-display");
			console.log("Looking for Ajax JSON");
			var security_id=omsService.getDropDownValue("security");
			var folder = omsService.getDropDownValue("folder");
			var cp = omsService.getDropDownValue("counterparty");
			var tr_id = document.getElementById("transactionIndicator").value;
			var e = document.getElementById("tradeDDL");
			var dealType = e.options[e.selectedIndex].text;
			console.log('Send Ajax '+tr_id)
			omsService.callAJAX(security_id,folder,cp,tr_id,dealType);
		}else if(unwindstatus==false){
			if("CreditDefaultSwapDeal" == deal){
				$("#unwind-ddl").addClass("no-display");
				document.getElementById('terminalDiv').style.display = 'none';
				document.getElementById('upfrontDiv').style.display = 'block';
			}else if("InterestRateSwapDeal" == deal){
				$("#unwind-ddl").addClass("no-display");
				document.getElementById('terminalDiv').style.display = 'none';
			}else if("FXOTCOptionDeal" == deal){
				$("#unwind-ddl").addClass("no-display");
				document.getElementById('rowfive_upfront').style.display = 'none';
				document.getElementById('cModeDiv').style.display='none';
				document.getElementById('strikeSpotDiv').style.display = 'block';
				document.getElementById('premiumDiv').style.display = 'block';
				hideNotional();
			}else if("EquitySwapDeal" == deal){
				$("#unwind-ddl").addClass("no-display");
				document.getElementById('kondor-id').style.display = 'none';
			}
		
		}
	}catch(e){
		console.log(e);	
	}	
}	
	
function handleUpfrontTermination(deal){
	if("CreditDefaultSwapDeal" == deal ){
		document.getElementById('upfrontDiv').style.display = 'none';
		document.getElementById('terminalDiv').style.display = 'block';
		//Now set values for upfront fee and upfront fee date as double quotes
		document.getElementById("upfrontFee").value="";	
		document.getElementById("upfrontFeeDate").value="";
	}else if("InterestRateSwapDeal" == deal){ // Deal is IRSDEAL
		document.getElementById('terminalDiv').style.display = 'block';		
	}else if("FXOTCOptionDeal" == deal){
		document.getElementById('rowfive_upfront').style.display = 'block';
		document.getElementById('strikeSpotDiv').style.display = 'none';
		document.getElementById('premiumDiv').style.display = 'none';
	}else if('EquitySwapDeal'==deal){
		document.getElementById('kondor-id').style.display = 'block';
	}
}

function dataPopulate(responseData){
	console.log("Success fetched data of length "+responseData.data.length);
	$('#selectedDeals').empty()
	if(responseData.data.length!=0){
		$('#selectedDeals').append($('<option>', { 
					value: "select",
					text : "Select Trade",
					id:"TradeDDL" 
				}));
		$.each(responseData.data, function(i,item){
		// Create and append the new options into the select list
		try{
			$('#selectedDeals').append($('<option>', { 
					value: item.value,
					text : item.value,
					id:item.id
					
				}));
		}catch(e){
			console.log(e);
		}   
	 });
	}else{
		$('#selectedDeals').append($('<option>', { 
			value: "NA",
			text : "No Deals",
			id:"NA" 
		}));
	}
	 
}

function fxotcdataPopulate(responseData){
	console.log("Success fetched data of length "+responseData.data.length);
	$('#selectedDeals').empty()
	if(responseData.data.length!=0){
		$('#selectedDeals').append($('<option>', { 
					value: "select",
					text : "Select Trade",
					id:"TradeDDL" 
				}));
		$.each(responseData.data, function(i,item){
		// Create and append the new options into the select list
		try{
			$('#selectedDeals').append($('<option>', { 
					value: item.value,
					text : item.value,
					id:item.id 
				}));
		}catch(e){
			console.log(e);
		}   
	 });
	}else{
		$('#selectedDeals').append($('<option>', { 
			value: "NA",
			text : "No Deals",
			id:"NA" 
		}));
	}
	 
}

function assignHiddenDivVal(){
	try{
		document.getElementById('strikePrice').value="";
		document.getElementById('spotRate').value="";
	}catch(e){
		console.log("Hidden values assignment failed");
	}
}

function sendFXOTCAjax(){
	try{

		var trade = document.getElementById("tradeDDL");
		var deal = trade.options[trade.selectedIndex].text;
		var unwindstatus=document.getElementById("unwindCheckbox").checked;
		if(unwindstatus == true){
			document.getElementById('rowfive_upfront').style.display = 'block';
			assignHiddenDivVal();
			console.log('Function called');
			var trade = document.getElementById("tradeDDL");
			var deal = trade.options[trade.selectedIndex].text;
			var unwindstatus=document.getElementById("unwindCheckbox").checked;
			$("#unwind-ddl").removeClass("no-display");
			console.log("Looking for Ajax JSON");
			var security_id=document.getElementById("security").value;
			var folder = document.getElementById("folder").value;
			var cp = document.getElementById("counterparty").value;
			var tr_id = document.getElementById("transactionIndicator").value;
			var callPut = document.getElementById("callPutIndicator").value;
			var exerciseType = document.getElementById("exerciseType").value;
			var expDate = document.getElementById("expirationDate").value;
			var valuation = document.getElementById("valuation").value;
			var e = document.getElementById("tradeDDL");
			var dealType = e.options[e.selectedIndex].text;
			console.log('Send Ajax '+tr_id)
			omsService.callFXOTCAJAX(security_id,folder,cp,tr_id,callPut,exerciseType,expDate,dealType,valuation);

		}else {
			$("#unwind-ddl").addClass("no-display");
			document.getElementById('rowfive_upfront').style.display = 'none';
			document.getElementById('cModeDiv').style.display='none';
		}
	}catch(e){
		console.log(e);	
	}	
}	
