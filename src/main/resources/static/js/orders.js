	/*
	 * [ Cancel order by Admid]
	 * ===========================================================
	 */
	function cancelOrder(orderId) {
		swal({
			title : "Cancel Order!",
			text : "Are you sure you want to cancel this order?",
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
					url : contextPath + "/order/cancel?orderId=" + orderId,
					method : "POST",
					success : function(response) {
						$(".loader-holder").hide();
							swal({
								icon : "success",
								text : response
							})
							.then(function(clickedOk) {
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
	 * [ Cancel order By Customer ]
	 * ===========================================================
	 */
	function cancelOrderByCustomer(userId, orderId) {
		swal({
			title : "Cancel Order!",
			text : "Are you sure you want to cancel this order?",
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
					url : contextPath + "/order/cancel/" + userId + "/" + orderId,
					method : "POST",
					success : function(response) {
						$(".loader-holder").hide();
						swal({
							icon : "success",
							text : response
						})
						.then(function(clickedOk) {
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
	 * [ Deliver order ]
	 * ===========================================================
	 */
	function deliverOrder(orderId) {
		swal({
			title : "Delivered!",
			text : "Are you sure you want to change status of this order?",
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
					url : contextPath + "/order/deliver?orderId="+ orderId,
					method : "POST",
					success : function(response) {
						$(".loader-holder").hide();
						swal({
							text : response,
							icon : "success",
						})
						.then(function(clickedOk) {
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
	 * [ Load more orders for Admin ]
	 * ===========================================================
	 */
	function loadMoreOrdersAdmin(surl) {
		let method = "GET";
		$(".loader-holder").show();
		
		$.ajax({
			url: surl,
			method: method,
			success: function(data) {
				let length = (data.data && data.data.length) || 0;
				if(data.data !== undefined && length > 0) {
					lastSeenId = data.data[length - 1].orderId;
					$(".loader-holder").hide();
					updateOrderViewAdmin(data.data);
				} else {
					$(".loader-holder").hide();
					swal({
						icon: "info",
						text: data.responseJSON.message
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
	
	// Function to load the view into the listing of orders for admin
	function updateOrderViewAdmin(data) {
		$.each(data, function(index, item) {
			let date = new Date(item.orderDate);
			let orderDate = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
			let element = `<tr>
								<td>${item.orderId}</td>
								<td>${item.status}</td>
								<td>${orderDate}</td>
								<td>${item.user.fName} ${item.user.lName} </td>							//Replacing username with Firstname and Lastname to accomodate the guest users
								<td><a href="${contextPath}/admin/order/${item.orderId}">View</a></td>
								<td >
									<button
										class="btn btn-success pull-right"
										onclick="deliverOrder(${item.orderId})">Delivered
									</button>
								</td>
								<td >
									<button
										class="btn btn-danger pull-right"
										onclick="cancelOrder(${item.orderId})">Cancel
									</button>
								</td>
							</tr>`;
		
		$("#orders-container").append(element);
		});
	}
	
	$("#load-more").on("click", function(event) {
		if(window.location.href.indexOf("range?startDate") > -1){
			let surl = contextPath + "/order/processingFromRange?startDate="+ startDate + "&endDate=" + endDate + "&last_seen=" + lastSeenId + "&action=" + action;
			console.log("url----->"+ surl);
			loadMoreOrdersAdmin(surl);	
		}
		else{
		let surl = contextPath + "/order?last_seen=" + lastSeenId + "&action=" + action;
		loadMoreOrdersAdmin(surl);
		}
	});	
	
	
	
	/*
	 * [ Load more orders for Admin in delivered orders]
	 * ===========================================================
	 */
	function loadMoreOrdersAdminDelivered(surl) {
		let method = "GET";
		$(".loader-holder").show();
		
		$.ajax({
			url: surl,
			method: method,
			success: function(data) {
				let length = (data.data && data.data.length) || 0;
				if(data.data !== undefined && length > 0) {
					lastSeenId = data.data[length - 1].orderId;
					$(".loader-holder").hide();
					updateOrderViewAdminAll(data.data);
				} else {
					$(".loader-holder").hide();
					swal({
						icon: "info",
						text: data.responseJSON.message
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
	
	$("#load-more-delivered").on("click", function(event) {
		if(window.location.href.indexOf("range?startDate") > -1){
			let surl = contextPath + "/order/deliveredFromRange?startDate="+ startDate + "&endDate=" + endDate + "&last_seen=" + lastSeenId + "&action=" + action;
			console.log("url----->"+ surl);
			loadMoreOrdersAdminDelivered(surl);	
		}
		else{
		let surl = contextPath + "/order/delivered?last_seen=" + lastSeenId + "&action=" + action;
		loadMoreOrdersAdminDelivered(surl);
		}
	});	
	
//==========================================================	
	

	/*
	 * [ Load more orders for Admin in cancelled orders]
	 * ===========================================================
	 */
	function loadMoreOrdersAdminCancelled(surl) {
		let method = "GET";
		$(".loader-holder").show();
		
		$.ajax({
			url: surl,
			method: method,
			success: function(data) {
				let length = (data.data && data.data.length) || 0;
				if(data.data !== undefined && length > 0) {
					lastSeenId = data.data[length - 1].orderId;
					$(".loader-holder").hide();
					updateOrderViewAdminAll(data.data);
				} else {
					$(".loader-holder").hide();
					swal({
						icon: "info",
						text: data.responseJSON.message
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
	
	$("#load-more-cancelled").on("click", function(event) {
		if(window.location.href.indexOf("range?startDate") > -1){
			let surl = contextPath + "/order/cancelledFromRange?startDate="+ startDate + "&endDate=" + endDate + "&last_seen=" + lastSeenId + "&action=" + action;
			console.log("url----->"+ surl);
			loadMoreOrdersAdminCancelled(surl);	
		}
		else{
		let surl = contextPath + "/order/cancelled?last_seen=" + lastSeenId + "&action=" + action;
		loadMoreOrdersAdminCancelled(surl);
		}
	});	
	
//==========================================================	
	
	/*
	 * [ Load more orders for Admin in order reports]
	 * ===========================================================
	 */
	function loadMoreOrdersAdminAll(surl) {
		let method = "GET";
		$(".loader-holder").show();
		$.ajax({
			url: surl ,
			method: method,
			success: function(data) {
				let length = (data.data && data.data.length) || 0;
				if(data.data !== undefined && length > 0) {
					lastSeenId = data.data[length - 1].orderId;
					$(".loader-holder").hide();
					updateOrderViewAdminAll(data.data);
				} else {
					$(".loader-holder").hide();
					swal({
						icon: "info",
						text: data.responseJSON.message
				//		text: "failed"
					});
				}
			},
			error: function(error) {
				$(".loader-holder").hide();
				swal({
					icon: "error",
					text: error.responseJSON.message
					//text: "failed"
				});
			}
		});
	}
	
	// Function to load the view into the listing of orders for admin
	function updateOrderViewAdminAll(data) {
		$.each(data, function(index, item) {
			let date = new Date(item.orderDate);
			let orderDate = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
			let element = `<tr>
								<td>${item.orderId}</td>
								<td>${item.status}</td>
								<td>${orderDate}</td>
								<td>${item.user.fName} ${item.user.lName} </td>							//Replacing username with Firstname and Lastname to accomodate the guest users
								<td><a href="${contextPath}/admin/order/${item.orderId}">View</a></td>
							</tr>`;
		
		$("#orders-container").append(element);
		});
	}
	
	$("#load-more-all").on("click", function(event) {
		if(window.location.href.indexOf("range?startDate") > -1){
			console.log(startDate);
			console.log(endDate);
			console.log("last seen id--->" + lastSeenId);
			let surl = contextPath + "/order/allFromRange?startDate="+ startDate + "&endDate=" + endDate + "&last_seen=" + lastSeenId + "&action=" + action;
			console.log("url----->"+ surl);
			loadMoreOrdersAdminAll(surl);	
		}
		else{
		let surl = contextPath + "/order/all" + "?last_seen=" + lastSeenId + "&action=" + action;
		console.log("last seen id--->" + lastSeenId);
		console.log("url----->"+ surl);
		loadMoreOrdersAdminAll(surl);
		}
	});	
	
//==========================================================	
		
	
	
	// This section is incomplete
	/*
	 * [ Load more orders for customer]
	 * ===========================================================
	 */
	function updateOrderView(data) {
		$.each(data, function(index, item) {
			let date = new Date(item.orderDate);
			let orderDate = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
			let element = `<tr>
								<td>${(index + 9)}</td>
								<td>${item.orderId}</td>
								<td>${item.status}</td>
								<td>${orderDate}</td>
								<td><a href="${contextPath}/order/${item.orderId}">View</a></td>
								<td >
									<button
										class="btn btn-danger pull-right"
										onclick="cancelOrderByCustomer(item.user.userId, item.orderId)">Cancel
									</button>
								</td>
							</tr>`;
		
		$("#orders-container").append(element);
		});
	}
	
	function loadMoreOrdersCustomer() {
		let method = "GET";
		$.ajax({
			url : contextPath + "/order/user/" + userId + "?last_seen="
					+ lastSeenId + "&action=" + action,
			method : method,
			success : function(data) {
				let length = (data.data && data.data.length) || 0;
				if (data.data !== undefined && length > 0) {
					lastSeenId = data.data[length - 1].orderId;
					updateOrderView(data.data);
				} else {
					swal({
						icon : "info",
						text : data.responseJSON.message
					});
				}
			},
			error : function(error) {
				swal({
					icon : "error",
					text : error.responseJSON.message
				});
			}
		});
	}
	
	$("#load-more-customer").on("click", function(event) {
		loadMoreOrdersCustomer();
	});
	
