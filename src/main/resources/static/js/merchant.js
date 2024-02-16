	// Function to go back to previous page
	function goBack() {
			window.history.back();
	}
	 	

	//Form validation using jQuery//
 	$(function() {
		
		$("#merchantName_error_message").hide();
		$("#businessType_error_message").hide();
		$("#merchantType_error_message").hide();
		$("#merchantDescription_error_message").hide();
		$("#street_error_message").hide();
		$("#city_error_message").hide();
		$("#contactPrimary_error_message").hide();
		
		
		let error_merchantName = false;
		let error_businessType = false;
		let error_merchantType = false;
		let error_merchantDescription = false;
		let error_street = false;
		let error_city = false;
		let error_contactPrimary = false;
		
		
		
		$("#merchant-name").focusout(function(){
			check_merchantName();
		});
		
		$("#business-type").focusout(function(){
			check_businessType();
		});
		
		$("#merchant-type").focusout(function(){
			check_merchantType();
		});
		
		$("#merchant-description").focusout(function(){
			check_merchantDescription();
		});
		
		$("#street-address").focusout(function(){
			check_street();
		});
		
		$("#city").focusout(function(){
			check_city();
		});
		
		$("#contact-primary").focusout(function(){
			check_contactPrimary();
		});
		
		
		function check_merchantName() {
			let merchantName = $("#merchant-name").val();
			if(merchantName !== '') {
				$("#merchantName_error_message").hide();
				$("#merchant-name").css("border-bottom", "2px solid #34F458");
			} else {
				$("#merchantName_error_message").html("Please enter the Merchant Name!");
				$("#merchantName_error_message").show();
				$("#merchant-name").css("border-bottom", "2px solid #F90A0A");
				error_merchantName = true;
			}
		} 
		
		function check_businessType() {
			let businessType = $("#business-type").val();
			if(businessType !== '') {
				$("#businessType_error_message").hide();
				$("#business-type").css("border-bottom", "2px solid #34F458");
			} else {
				$("#businessType_error_message").html("Please specify the type of business type!");
				$("#businessType_error_message").show();
				$("#business-type").css("border-bottom", "2px solid #F90A0A");
				error_businessType = true;
			}
		} 
		
		function check_merchantType() {
			let merchantType = $("#merchant-type").val();
			if(merchantType !== '') {
				$("#merchantType_error_message").hide();
				$("#merchant-type").css("border-bottom", "2px solid #34F458");
			} else {
				$("#merchantType_error_message").html("Could you tell us what type of merchant you are?");
				$("#merchantType_error_message").show();
				$("#merchant-type").css("border-bottom", "2px solid #F90A0A");
				error_merchantType = true;
			}
		} 
		
		function check_merchantDescription() {
			let merchantDescription = $("#merchant-description").val();
			if(merchantDescription !== '') {
				$("#merchantDescription_error_message").hide();
				$("#merchant-description").css("border", "2px solid #34F458");
			} else {
				$("#merchantDescription_error_message").html("Could you provide a short description of the merchant!");
				$("#merchantDescription_error_message").show();
				$("#merchant-description").css("border", "2px solid #F90A0A");
				error_merchantDescription = true;
			}
		} 
		
		function check_street() {
			let street = $("#street-address").val();
			if(street !== '') {
				$("#street_error_message").hide();
				$("#street-address").css("border-bottom", "2px solid #34F458");
			} else {
				$("#street_error_message").html("Street address is a required field");
				$("#street_error_message").show();
				$("#street-address").css("border-bottom", "2px solid #F90A0A");
				error_street = true;
			}
		} 
		
		function check_city() {
			let city = $("#city").val();
			if(city !== '') {
				$("#city_error_message").hide();
				$("#city").css("border-bottom", "2px solid #34F458");
			} else {
				$("#city_error_message").html("City is required");
				$("#city_error_message").show();
				$("#city").css("border-bottom", "2px solid #F90A0A");
				error_city = true;
			}
		} 
		
		function check_contactPrimary() {
			let contactPrimary = $("#contact-primary").val();
			if(contactPrimary !== '') {
				$("#contactPrimary_error_message").hide();
				$("#contact-primary").css("border-bottom", "2px solid #34F458");
			} else {
				$("#contactPrimary_error_message").html("Phone number is required!");
				$("#contactPrimary_error_message").show();
				$("#contact-primary").css("border-bottom", "2px solid #F90A0A");
				error_contactPrimary = true;
			}
		} 
		
		
	$(function() {$("#merchant-entry-submit").click(function(event) {
		event.preventDefault();
			
		error_merchantName = false;
		error_businessType = false;
		error_merchantType = false;
		error_merchantDescription = false;
		error_street = false;
		error_city = false;
		error_contactPrimary = false;
			
			
		check_merchantName();
		check_businessType();
		check_merchantType();
		check_merchantDescription();
		check_street();
		check_city();
		check_contactPrimary();
			
		if(error_merchantName === false && error_businessType === false && 
			error_merchantType === false && error_merchantDescription === false && 
			error_street === false && error_city === false && error_contactPrimary === false) {
				
				
			let merchantName = $("#merchant-name").val();
			let businessType = $("#business-type").val();
			let merchantType = $("#merchant-type").val();
			let merchantDesc = $("#merchant-description").val();
			let streetAddress = $("#street-address").val();
			let city = $("#city").val();
			let contactPrimary = $("#contact-primary").val();
			let contactSecondary;
			if($("#contact-secondary") == "") {
				contactSecondary = 0;
			}
			else {
				contactSecondary = $("#contact-secondary").val();
			}
		

			// Every data retrieved from form should be validated and sanitized.
			// For now data is not sanitized. Always validate form data before
			// sending it to server.
			let merchant = {};
			merchant.merchantName = merchantName;
			merchant.businessType = businessType;
			merchant.merchantType = merchantType;
			merchant.merchantDesc = merchantDesc;
			merchant.street = streetAddress;
			merchant.city = city;
			merchant.contactPrimary = contactPrimary;
			merchant.contactSecondary = contactSecondary;


			// Now create an ajax request to send merchant details to the
			// server.
			$.ajax({
				method: "POST",
				url: contextPath + "/merchant/add",
				data: JSON.stringify(merchant),
				contentType: "application/json",
				success: function(result) {
					swal({
						text: "Merchant added successfully!",
						icon: "success",
						closeOnClickOutside: false,
						closeOnEsc: false
					}).then(function () {
								window.location = contextPath+"/admin";
							});
				},
				error: function(error) {
					alert(error);
				}
			});
		} else {
			swal({
            	icon: "info",
            	text: "Please enter the required fields correctly :(",
        	});
		}		
	});
	
});
	});
 	
 	
	
	
	const action = "next";
	
	// For deleting merchant
	function deleteMerchant(merchantId) {
		swal({
			title : "Delete Merchant!",
			text : "Are you sure you want to delete this merchant?",
			icon : "warning",
			dangerMode : true,
			buttons : true,
			closeOnClickOutside : false,
			closeOnEsc : false
		})
			.then(function(clickedOk) {
				if (clickedOk) {
					$(".loader-holder").show();
					$.ajax({
						url : contextPath + "/merchant/" + merchantId,
						method : "DELETE",
						success : function(response) {
							$(".loader-holder").hide();
							swal({
								icon : "success",
								text : response
							})
								.then(function(clickedOk) {
									window.location.href = contextPath + "/admin/merchantList";
								});
						},
						error : function(error) {
							$(".loader-holder").hide();
							swal({
								icon : "error",
								text : error
							});
						}
					});
				}
			});
		}
	

	
	// For searching the merchant in the merchant list page

	$("#search-merchant").on('keyup', function (e) {
		let tBody = $("#merchant-body");
		if (e.keyCode == 13) {
			tBody.empty();
			e.preventDefault();
			let searchKey = $("#search-merchant").val().trim();
			$.ajax({
				url: contextPath + "/merchant/search?searchKey=" + encodeURI(searchKey),
				method: "GET",
				success: function(data){
					let returnedData = data.data;
					let length = (returnedData && returnedData.length) || 0;
					if(returnedData !== undefined && length > 0) {
						let tableData ="";
						returnedData.forEach(element => {
							tableData += `<tr>
											<td>${element.merchantId}</td>
											<td>${element.merchantName}</td>
											<td>${element.street},  ${element.city}</td>
											<td>${element.contactPrimary}</td>
											<td><a href="${contextPath}/admin/editMerchant/${element.merchantId}">Edit</a></td>
											<td>
												<button
													class="btn btn-danger pull-right"
													onclick="deleteMerchant(${element.merchantId})">Delete Merchant
												</button></td>
											</tr>`;
						});
					
					tBody.empty();
					tBody.append(tableData);	
				} else {
					swal({
                    	icon: "info",
                    	text: "Sorry! No such results found! :(",
                	});
					}		
				},
				error: function(error){
					alert(JSON.stringify(error));
					}
				});
			}
		}); 
		  
	
	
	
	// For editing the merchant details, getting the values
	var merchantInfo = {};
	var updateMerchantInfo = function () {
	    merchantInfo.merchantId = $("input[name='merchantId']").val();
	    merchantInfo.merchantName = $("input[name='merchantName']").val();
	    merchantInfo.businessType = $("input[name='businessType']").val();
	    merchantInfo.merchantType = $("input[name='merchantType']").val();
	    merchantInfo.merchantDesc = $("textarea[name='description']").val();
	    merchantInfo.street = $("input[name='street']").val();
	    merchantInfo.city = $("input[name='city']").val();
	    merchantInfo.contactPrimary = $("input[name='contactPrimary']").val();
	    merchantInfo.contactSecondary = $("input[name='contactSecondary']").val();



	    // function for editing the merchant info
	    if (merchantInfo != null) {									// first checks if the data provided is null or not
	        $(".loader-holder").show();
	        $.ajax({
	            url: contextPath + "/merchant/" + merchantInfo.merchantId,
	            method: "PUT",
	            contentType: "application/json",
	            data: JSON.stringify(merchantInfo),
	            success: function (data) {
	                $(".loader-holder").hide();
	                swal({
	                    text: data,
	                    icon: "success",
	                }).then(function (btnVal) {
	                    if (btnVal) {
	                        window.location = contextPath + "/admin/editMerchant/" + merchantInfo.merchantId;
	                    }
	                });
	            },
	            error: function (error) {
	                $(".loader-holder").hide();
	                swal({
	                    icon: "error",
	                    text: error.responseText
	                });
	            }
	        });
	    }
	}

	
	
	
	//Form validation for Edit merchant//
 	$(function() {
		$("#merchantName_error_message").hide();
		$("#businessType_error_message").hide();
		$("#merchantType_error_message").hide();
		$("#merchantDescription_error_message").hide();
		$("#street_error_message").hide();
		$("#city_error_message").hide();
		$("#contactPrimary_error_message").hide();
		
		let error_merchantName = false;
		let error_businessType = false;
		let error_merchantType = false;
		let error_merchantDescription = false;
		let error_street = false;
		let error_city = false;
		let error_contactPrimary = false;
		
		
		
		$("#merchant-name").focusout(function(){
			check_merchantName();
		});
		
		$("#business-type").focusout(function(){
			check_businessType();
		});
		
		$("#merchant-type").focusout(function(){
			check_merchantType();
		});
		
		$("#merchant-description").focusout(function(){
			check_merchantDescription();
		});
		
		$("#street-address").focusout(function(){
			check_street();
		});
		
		$("#city").focusout(function(){
			check_city();
		});
		
		$("#contact-primary").focusout(function(){
			check_contactPrimary();
		});
		
		
		function check_merchantName() {
			let merchantName = $("#merchant-name").val();
			if(merchantName !== '') {
				$("#merchantName_error_message").hide();
				$("#merchant-name").css("border-bottom", "2px solid #34F458");
			} else {
				$("#merchantName_error_message").html("Please enter the Merchant Name!");
				$("#merchantName_error_message").show();
				$("#merchant-name").css("border-bottom", "2px solid #F90A0A");
				error_merchantName = true;
			}
		} 
		
		function check_businessType() {
			let businessType = $("#business-type").val();
			if(businessType !== '') {
				$("#businessType_error_message").hide();
				$("#business-type").css("border-bottom", "2px solid #34F458");
			} else {
				$("#businessType_error_message").html("Please specify the type of business type!");
				$("#businessType_error_message").show();
				$("#business-type").css("border-bottom", "2px solid #F90A0A");
				error_businessType = true;
			}
		} 
		
		function check_merchantType() {
			let merchantType = $("#merchant-type").val();
			if(merchantType !== '') {
				$("#merchantType_error_message").hide();
				$("#merchant-type").css("border-bottom", "2px solid #34F458");
			} else {
				$("#merchantType_error_message").html("Could you tell us what type of merchant you are?");
				$("#merchantType_error_message").show();
				$("#merchant-type").css("border-bottom", "2px solid #F90A0A");
				error_merchantType = true;
			}
		} 
		
		function check_merchantDescription() {
			let merchantDescription = $("#merchant-description").val();
			if(merchantDescription !== '') {
				$("#merchantDescription_error_message").hide();
				$("#merchant-description").css("border", "2px solid #34F458");
			} else {
				$("#merchantDescription_error_message").html("Could you provide a short description of the merchant!");
				$("#merchantDescription_error_message").show();
				$("#merchant-description").css("border", "2px solid #F90A0A");
				error_merchantDescription = true;
			}
		} 
		
		function check_street() {
			let street = $("#street-address").val();
			if(street !== '') {
				$("#street_error_message").hide();
				$("#street-address").css("border-bottom", "2px solid #34F458");
			} else {
				$("#street_error_message").html("Street address is a required field");
				$("#street_error_message").show();
				$("#street-address").css("border-bottom", "2px solid #F90A0A");
				error_street = true;
			}
		} 
		
		function check_city() {
			let city = $("#city").val();
			if(city !== '') {
				$("#city_error_message").hide();
				$("#city").css("border-bottom", "2px solid #34F458");
			} else {
				$("#city_error_message").html("City is required");
				$("#city_error_message").show();
				$("#city").css("border-bottom", "2px solid #F90A0A");
				error_city = true;
			}
		} 
		
		function check_contactPrimary() {
			let contactPrimary = $("#contact-primary").val();
			if(contactPrimary !== '') {
				$("#contactPrimary_error_message").hide();
				$("#contact-primary").css("border-bottom", "2px solid #34F458");
			} else {
				$("#contactPrimary_error_message").html("Phone number is required!");
				$("#contactPrimary_error_message").show();
				$("#contact-primary").css("border-bottom", "2px solid #F90A0A");
				error_contactPrimary = true;
			}
		} 
	// calls merchant info update function when done button is clicked.
	$("#edit-merchant-submit").on("click", function (event) {
	    event.preventDefault();
	    
	    error_merchantName = false;
		error_businessType = false;
		error_merchantType = false;
		error_merchantDescription = false;
		error_street = false;
		error_city = false;
		error_contactPrimary = false;
			
			
		check_merchantName();
		check_businessType();
		check_merchantType();
		check_merchantDescription();
		check_street();
		check_city();
		check_contactPrimary();
	    
		
		if(error_merchantName === false && error_businessType === false && 
				error_merchantType === false && error_merchantDescription === false && 
				error_street === false && error_city === false && error_contactPrimary === false) {
	    
		    var editMode = $("input[name='merchant-management-status']:checked").val();
		    if (editMode == "yes") {
		        swal({
		            text: "Do you really want to update merchant info?",
		            icon: "info",
		            buttons: true
		        }).then(function (confirmation) {
		            if (confirmation) {
		                updateMerchantInfo();
		            }
		        });
		    } else {
		        swal({
		            text: "Please enable edit mode first!",
		            icon: "info"
		        });
	    }
		} else {
			swal({
	        	icon: "info",
	        	text: "Please enter the required fields correctly :(",
	    	});
		}	
	});
 });

