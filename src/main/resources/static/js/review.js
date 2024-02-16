	/*[ Goes back to the previous page ]
    ===========================================================*/
	function goBack() {
			window.history.back();
	}
	
	/*
	 * [ Delete a review ]
	 * ===========================================================
	 */
	function deleteReview(reviewId) {
		swal({
			title : "Delete Review!",
			text : "Are you sure you want to delete this review?",
			icon : "warning",
			dangerMode : true,
			buttons : true,
			closeOnClickOutside : false,
			closeOnEsc : false
		})
		.then(
			function(clickedOk) {
				if (clickedOk) {
					$(".loader-holder").show();
						$.ajax({
							url : contextPath + "/review/" + reviewId,
							method : "DELETE",
							success : function(response) {
								$(".loader-holder").hide();
								swal({
									icon : "success",
									text : response
								})
								.then(
									function(clickedOk) {
										window.location.href = contextPath;
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
	
	
	/*
	 * [ Change review status from review details ]
	 * ===========================================================
	 */
	$(function() {$("#reviewStatus-btn-submit").click(function() {
		var status = parseInt($("#review-status").val());
		var reviewId = $("#review-id").val();
		event.preventDefault();
		swal({
			title : "Change Status!",
			text : "Are you sure you want to change the review?",
			icon : "warning",
			dangerMode : true,
			buttons : true,
			closeOnClickOutside : false,
			closeOnEsc : false
		}).then(function(clickedOk){
			if(clickedOk) {
				$(".loader-holder").show();
			$.ajax({
				url : contextPath + "/review/status/" + status + "/reviewId/" + reviewId,
				method : "POST",
				success : function(response) {
					$(".loader-holder").hide();
					swal({
						text : response,
						icon : "success",
					})
					.then(
							function(
									clickedOk) {
								window.location.href = contextPath + "/admin/review/" + reviewId;
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
		});
	});
	
	
	/*
	 * [ Edits the review by user ]
	 * ===========================================================
	 */
	
	let reviewInfo = {};
	let updateReviewInfo = function () {
			
		    // reviewInfo.reviewwId = $("tr td:first-child").text();
		    reviewInfo.reviewId = $("input[name='reviewId']").val();
		    reviewInfo.reviewTitle = $("input[name='reviewTitle']").val();
		    reviewInfo.reviewDesc = $("textarea[name='reviewDesc']").val();
		    reviewInfo.pros = $("input[name='pros']").val();
		    reviewInfo.cons = $("input[name='cons']").val();
		    reviewInfo.rating = $("select[name='rating']").val();
		    
		    // function for editing the review info
		    if (reviewInfo != null) {								// first checks if the data provided is null or not
		        $(".loader-holder").show();
		        $.ajax({
		            url: contextPath + "/review/" + reviewInfo.reviewId,
		            method: "PUT",
		            contentType: "application/json",
		            data: JSON.stringify(reviewInfo),
		            success: function (data) {
		                $(".loader-holder").hide();
		                swal({
		                    text: data,
		                    icon: "success",
		                }).then(function (btnVal) {
		                    if (btnVal) {
		                        window.location = contextPath + "/customer/editReview/" + reviewInfo.reviewId;
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
	
	
	//From Validation for edit review
	$(function() {
		$("#reviewTitle_error_message").hide();
		$("#reviewDesc_error_message").hide();
		
		let error_reviewTitle = false;
		let error_reviewDesc = false;
		
		
		$("#review-title").focusout(function(){
			check_reviewTitle();
		});
		
		$("#review-description").focusout(function(){
			check_reviewDesc();
		});
	
		
		function check_reviewTitle() {
			var reviewTitle = $("#review-title").val();
			if(reviewTitle !== '') {
				$("#reviewTitle_error_message").hide();
				$("#review-id").css("border-bottom", "2px solid #34F458");
			} else {
				$("#reviewTitle_error_message").html("Please enter the Review Title!");
				$("#reviewTitle_error_message").show();
				$("#review-title").css("border-bottom", "2px solid #F90A0A");
				error_reviewTitle = true;
			}
		} 
		
		function check_reviewDesc() {
			var reviewDesc = $("#review-description").val();
			if(reviewDesc !== '') {
				$("#reviewDesc_error_message").hide();
				$("#review-description").css("border-bottom", "2px solid #34F458");
			} else {
				$("#reviewDesc_error_message").html("Please enter the Review Description!");
				$("#reviewDesc_error_message").show();
				$("#review-description").css("border-bottom", "2px solid #F90A0A");
				error_reviewDesc = true;
			}
		} 
	

		// calls review info update function when done button is clicked.
		$("#edit-review-submit").on("click", function (event) {
		    event.preventDefault();
		    
			
			error_reviewTitle = false;
			error_reviewDesc = false;
			
			check_reviewTitle();
			check_reviewDesc();
			
			if(error_reviewTitle == false && error_reviewDesc == false) {
		    
		    var editMode = $("input[name='review-management-status']:checked").val();
		    if (editMode == "yes") {
		        swal({
		            text: "Do you really want to update Review info?",
		            icon: "info",
		            buttons: true
		        }).then(function (confirmation) {
		            if (confirmation) {
		                updateReviewInfo();
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

	
	
	
	//Event Handler For LoadMore Button On ReviewList Page.
	$(function() {
		let completeUrl = location.href;
		const reviewStatus = completeUrl.substr(completeUrl.lastIndexOf('/') + 1);
		console.log("REVIEW STATUS : " + reviewStatus);
		$("#load-more").on('click', function(event) {
			event.preventDefault();
			loadMoreReviews(reviewStatus);
		})
	});
	
 	const action = "next";
	var reviewTable = $("#review-table");
	
	function loadMoreReviews(reviewStatus) {
		let requestUrl = contextPath + "/review";
		switch(reviewStatus) {
			case "approved":
				requestUrl += "/approved?last_seen=" + lastSeenId + "&action=" + action;
				break;
			case "processing":
				requestUrl += "/processing?last_seen=" + lastSeenId + "&action=" + action;
				break;
			case "rejected":
				requestUrl += "/rejected?last_seen=" + lastSeenId + "&action=" + action;
				break;
			default:
				requestUrl += "?last_seen=" + lastSeenId + "&action=" + action;
				break;
		}
		
		$(".loader-holder").show();
		$.ajax({
			url: requestUrl,
			method: "GET",
			success: function(data) {
				let length = (data.data && data.data.length) || 0;
				if(data.data !== undefined && length > 0) {
					lastSeenId = data.data[length - 1].reviewId;
					$(".loader-holder").hide();
					updateReviewView(data.data);
				} else {
					$(".loader-holder").hide();
					swal({
						icon: "info",
						text: "You have reached the end of review list."
					});
				}
			},
			error: function(error) {
				$(".loader-holder").hide();
				swal({
					icon: "error",
					text: error.responseJSON.message
				});
			}
		});
	}
	
	function updateReviewView(data) {
		data.forEach(function(item) {
			let element = `<tr>
				<td>${item.reviewId}</td>
				<td>${item.productName}</td>
				<td>${item.reviewTitle}</td>
				<td>${item.rating}</td
				<td>${item.reviewDate}</td>
				<td>${item.userDto.username}</td>
				<td>${item.status}</td>
				<td><button class='btn btn-danger pull-right' onclick='deleteReview(${item.reviewId})'>Delete Review</button></td>
			</tr>`;
			
			$("#review-container").append(element);
		});
	}
	