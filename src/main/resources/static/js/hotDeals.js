$(function() {
	let categoryHierarchyDom = "";
	function createCategoryHierarchy(categories) {
		categories
				.forEach(function(category) {
					categoryHierarchyDom += `<li><i class="fa fa-caret-down"  data-toggle="collapse" data-target="#data${category.categoryId}" aria-hidden="true"></i>&nbsp <span class="m-r-3">${category.categoryName}</span> <i class="fa fa-play-circle" data-category="${category.categoryId}" aria-hidden="true"></i>`;
					if (category.childCategories
							&& category.childCategories.length > 0) {
						categoryHierarchyDom += "<ul id='data"
								+ category.categoryId
								+ "' class='p-l-5 collapse'>";
						createCategoryHierarchy(category.childCategories)
						categoryHierarchyDom += "</li></ul>";
					}
					categoryHierarchyDom += "</li>";
				});

	}

	/*
	 * if(categoriesData.data && categories.data.length > 0) {
	 * createCategoryHierarchy(categoriesData); }
	 */
	createCategoryHierarchy(categoriesData);
	$("#categoryHierarchy").append(categoryHierarchyDom);

	// expands all categories and sub-categories
	$("#expandCategory").on("click", function(event) {
		event.preventDefault();
		$("#categoryHierarchy ul").each(function(element) {
			$(this).collapse('show');
		});
	});

	// Collapses all the category and subcategories
	$("#collapseCategory").on("click", function(event) {
		event.preventDefault();
		$("#categoryHierarchy ul").each(function(element) {
			$(this).collapse('hide');
		});
	});
});

var urlFilterObj = {
	url : '',
	pageNo : 1,
	lastSeenId : 0,
	customPageSize : 9,
	stockFilter : true,
	action : "first",
	getUrl : function() {
		return this.url;
	},
	setUrl : function(url) {
		if (url.indexOf("last_seen=") < 0) {
			this.url = url + "last_seen=" + this.getLastSeenId() + "&pageSize="
					+ this.getCustomPageSize() + "&action=" + this.getAction();
			if (this.getStockFilter()) {
				this.url += "&stockFilter=In-Stock";
			} else {
				this.url += "&stockFilter=Out-Of-Stock";
			}
		} else {
			var tmpQueryParams = "last_seen=" + this.getLastSeenId()
					+ "&pageSize=" + this.getCustomPageSize() + "&action="
					+ this.getAction() + "&stockFilter=";
			if (this.getStockFilter()) {
				tmpQueryParams += "In-Stock";
			} else {
				tmpQueryParams += "Out-Of-Stock";
			}
			this.url = url.replace(url.substr(url.indexOf("last_seen"),
					url.length), tmpQueryParams);
		}
	},

	getCustomPageSize : function() {
		return this.customPageSize;
	},

	setCustomPageSize : function(pageSize) {
		this.customPageSize = pageSize;
		this.updateUrl(this.getUrl());
	},
	getStockFilter : function() {
		return this.stockFilter;
	},
	setStockFilter : function(stockFilter) {
		this.stockFilter = stockFilter;
		this.updateUrl(this.getUrl());
	},

	updateUrl : function(url) {
		this.setUrl(url);
	},

	getLastSeenId : function() {
		return this.lastSeenId;
	},
	setLastSeenId : function(lastSeenId) {
		this.lastSeenId = lastSeenId;
	},

	getPageNo : function() {
		return this.pageNo;
	},
	setPageNo : function(pageNo) {
		this.pageNo = pageNo;
	},
	setAction : function(action) {
		this.action = action;
		this.updateUrl(this.getUrl());
	},
	getAction : function() {
		return this.action;
	},
	resetPageNo : function() {
		this.pageNo = 1;
	}
};

// This function returns query parameter vaule from url
function getParameterByName(name, url) {
	name = name.replace(/[\[\]]/g, '\\$&');
	var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'), results = regex
			.exec(url);
	if (!results)
		return null;
	if (!results[2])
		return '';
	return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

// Creates Next Page Link
function createNextPageUrl(lastId, url) {
	let lastSeenId = getParameterByName("last_seen", url);
	let action = getParameterByName("action", url);
	let newUrl = url.replace(lastSeenId, lastId);
	return newUrl.replace(action, "next");
}

// Creates Previous Page Link
function createPrevPageUrl(firstId, url) {
	let lastSeenId = getParameterByName("last_seen", url);
	let action = getParameterByName("action", url);
	let newUrl = url.replace(lastSeenId, firstId);
	return newUrl.replace(action, "prev");
}

// previous name generateNextAndPrevLinksForSearchResults
// generateNextAndPrevLinks
function generateNextAndPrevLinks(firstResultId, lastResultId, totalItems, url) {
	let nextLink = '';
	let prevLink = '';

	if (urlFilterObj.getPageNo() > 1) {
		if (totalItems == urlFilterObj.getCustomPageSize()) {
			nextLink = createNextPageUrl(lastResultId, url);
		}
		// for previous page link.
		prevLink = createPrevPageUrl(firstResultId, url);
	} else {
		if (totalItems == urlFilterObj.getCustomPageSize()) {
			nextLink = createNextPageUrl(lastResultId, url);
		}

	}

	$("#next").attr("href", nextLink);
	$("#prev").attr("href", prevLink);
}

$("#next").on('click', function(event) {
	event.preventDefault();
	urlFilterObj.setPageNo(urlFilterObj.getPageNo() + 1);
	updateInventoryDisplay($(this).attr('href'));
});

$("#prev").on('click', function(event) {
	event.preventDefault();
	if (urlFilterObj.getPageNo() <= 1) {
		urlFilterObj.setPageNo(1);
	} else {
		urlFilterObj.setPageNo(urlFilterObj.getPageNo() - 1);
	}
	updateInventoryDisplay($(this).attr('href'));
});

$("#in-stock").on('click', function(event) {
	event.preventDefault();
	urlFilterObj.setStockFilter(true);
	updateInventoryDisplay(urlFilterObj.getUrl());
});

$("#out-of-stock").on('click', function(event) {
	event.preventDefault();
	urlFilterObj.setStockFilter(false);
	updateInventoryDisplay(urlFilterObj.getUrl());
});

$("#pageSize a").each(function() {
	$(this).on('click', function(event) {
		event.preventDefault();
		var customPageSize = $(this).text();
		urlFilterObj.setCustomPageSize(customPageSize);
		updateInventoryDisplay(urlFilterObj.getUrl());
		$("#pageSizeCounter").text(customPageSize);
	});
});

// Event handler for the items in categories list

$("#categoryHierarchy").on("click", "i", function(event) {
	if ($(this).attr("class") == "fa fa-play-circle") {
		var url = "/products/category/" + $(this).attr("data-category");
		urlFilterObj.setUrl(url + "?");
		updateInventoryDisplay(urlFilterObj.getUrl());

	}
});


function updateInventoryDisplay(url) {
	$(".loader-holder").show();
	$
			.ajax({
				method : "GET",
				url : contextPath + url,
				success : function(data) {
					var tableBody = $("#table-body");
					var tr = "";
					var modalBody = $("#modalArea");
					var modal = "";
					tableBody.empty();
					modalBody.empty();
					if (data.data !== undefined && data.data.length > 0) {
						$
								.each(
										data.data,
										function(index, element) {

											$("#sm-pagination-text")
													.text(
															data.data[data.data.length - 1].productId
																	+ " to "
																	+ data.data[0].productId);
											tr += "<tr>";
											tr += "<td>" + element.productId
													+ "</td>";
											tr += "<td>" + element.productName
													+ "</td>";
											tr += "<td>" + element.rate
													+ "</td>";
											tr += "<td>"
													+ element.availableItems
													+ "</td>";
											tr += "<td>"
													+ (element.availableItems > 0 ? 'In-Stock'
															: 'Out-Of-Stock')
													+ "</td>";
											tr += "<td>";
											tr += `<button class="btn btn-primary" onclick="showAddModal(${element.productId})">Add to HotDeals</button>`;
											tr += "</td>";
											tr += "</tr>";
											//modal code generation
											modal += `<div class="modal fade" id="hotdeal-modal-add${element.productId}"
												data-keyboard="false" data-backdrop="false" role="dialog" aria-hidden="true">
											<div class="modal-dialog modal-lg modal-dialog-centered"
												role="document">
												<div class="modal-content editmediaback">
													<div class="modal-header">
														<h5 class="modal-title">Add Hot Deal</h5>
													</div>
													<div class="modal-content-body">
														<div style="margin: 5px 20px;">
															<span>Product Id: </span><input class="inputtext lower-case" readonly="true" type="text" name="addProductId${element.productId}" value="${element.productId}"><br>
															<span>Product Name: </span><input class="inputtext lower-case" readonly="true" type="text" name="addProductName${element.productId}" value="${element.productName}"><br>
															<span>Old Rate: </span><input class="inputtext lower-case" readonly="true" type="text" name="addProductOldRate${element.productId}" value="${element.actualRate}"><br>
															<span>New Rate: </span><input class="inputtext lower-case" type="text" name="addProductNewRate${element.productId}"><br>
															<span>Finish Date: </span><input class="inputtext lower-case" type="text" id="addDealDate${element.productId}" name="addDealFinishDate${element.productId}"><br>
															
														</div>
													</div>
													<div class="modal-footer">
														<button id="addHotDealBtn${element.productId}" onclick="addNewHotDeal(${element.productId})" class="btn btn-primary">Submit</button>
														<button id="addCancelBtn${element.productId}" onclick="hideAddModal(${element.productId})" class="btn btn-danger">Cancel</button>
													</div>
												</div>
											</div>
										</div>`;

											
										});
						let totalLength = data.data.length;
						generateNextAndPrevLinks(data.data[0].productId,
								data.data[totalLength - 1].productId,
								totalLength, url);
						tableBody.append(tr);
						$(".loader-holder").hide();
						tableBody.show();
						modalBody.append(modal);
						$(".loader-holder").hide();
						modalBody.show();
					} else {
						$(".loader-holder").hide();
						tableBody
								.append("<div> Sorry no results found! </div>");
						tableBody.show();
					}
				},
				error : function(error) {
					$(".loader-holder").hide();
					swal({
						icon : "error",
						text : error.responseJSON.message
					});
				}
			});
}


function showAddModal(productId){
	let addModalId = "hotdeal-modal-add" + productId;
	$('#' + addModalId).modal("show");
	let finishDateField = "addDealDate" + productId;
	 $('#' + finishDateField).datepicker({
	    	dateFormat: "yy-mm-dd" 	
	    });
}

function hideAddModal(productId){
	let addModalId = "hotdeal-modal-add" + productId;
	$('#' + addModalId).modal("hide");
}

// Search Function Event
$("#searchBtn").on('click', function(event) {
	event.preventDefault();
	var searchKey = $("#search-input").val();
	$("#search-input").val('');
	let url = "/products/search?searchKey=" + encodeURI(searchKey);
	if (searchKey != null && searchKey != '') {
		urlFilterObj.setUrl(url + "&");
		updateInventoryDisplay(urlFilterObj.getUrl());

	}
});

function deleteItem(url) {
	$(".loader-holder").show();
	$.ajax({
		url : url,
		method : "DELETE",
		success : function(data) {
			$(".loader-holder").hide();
			swal({
				text : data,
				icon : "success",
				buttons : true
			}).then(function(okBtnPressed) {
				if (okBtnPressed) {
					window.location = contextPath + "/admin/stocks";
				}
			});
		},
		error : function(error) {
			$(".loader-holder").hide();
			swal({
				text : error.responseText,
				icon : 'error',
				buttons : true
			});
		}
	});
}

$("#table-body").on('click', "a", function(event) {
	event.preventDefault();
	if ($(this).text() == "Delete Product") {
		let url = $(this).attr("href");
		swal({
			text : "Do you really want to delete this item?",
			icon : "info",
			buttons : true
		}).then(function(okBtnPressed) {
			if (okBtnPressed) {
				deleteItem(url);
			}
		});

	} else if ($(this).text() == "Details") {
		//Do nothing 
	} else {
		window.location = $(this).attr("href");
	}
});

//Add new Hot Deal
function addNewHotDeal(productId){
	let hotDealModalId = "addProductId" + productId;
	let hotDealModalName = "addProductName" + productId;
	let hotDealModalOldRate = "addProductOldRate" + productId;
	let hotDealModalNewRate = "addProductNewRate" + productId;
	let hotDealModalFinishDate = "addDealFinishDate" + productId;
	let hotDeal = {};
	hotDeal.productId = $("input[name=" + hotDealModalId + "]").val();
	hotDeal.productName= $("input[name=" + hotDealModalName + "]").val();
	hotDeal.oldRate= $("input[name=" + hotDealModalOldRate + "]").val();
	hotDeal.newRate= $("input[name=" + hotDealModalNewRate + "]").val();
	hotDeal.finishDate= $("input[name=" + hotDealModalFinishDate + "]").val();
	hotDeal.hotDeal = 1;
	console.log(hotDeal.productName);
	console.log(contextPath);
	swal({
		title : "Add!",
		text : "Are you sure you want to Add this Hot Deal?",
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
				url : contextPath + "/hot-deals",
				method : "POST",
				contentType: "application/json",
				data: JSON.stringify(hotDeal),
				success : function(response) {
					$(".loader-holder").hide();
						swal({
							icon : "success",
							text : "Hot Deal added successfully"
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