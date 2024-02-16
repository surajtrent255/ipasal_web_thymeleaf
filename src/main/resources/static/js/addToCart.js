//NAMESPACE makes new scope for our objects and methods.
//Try to avoid global scope as much as possible.
var NAMESPACE = {};

NAMESPACE.cartList = [];
NAMESPACE.totalItems = 0;

NAMESPACE.cookieExists = function(key) {
	
}

NAMESPACE.rootURL = /*[[${#request.contextPath}]]*/ '';
//This function sets cookie
NAMESPACE.setCookie = function(key, value) {
	/!* console.log("key: "+ key + " value: "+ JSON.stringify(value));*!/
    var expires = new Date();
    expires.setTime(expires.getTime() + (1 * 24 * 60 * 60 * 1000));
    document.cookie = key + '=' + value + ';expires=' + expires.toUTCString()+';path = /';
}

//This function returns cookies
NAMESPACE.getCookie = function(key) {
	var keyValue = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
    return keyValue ? keyValue[2] : null;
}


//This function checks whether item already exists in card.
NAMESPACE.checkItemInCart = function(itemsList, productId) {
	for(var i = 0; i < itemsList.length; i++) {
        if(itemsList[i].productId == productId) {
        	return i;
        }
    }
	
	return false;
};

//This function add items to cart 
//Here items details are added in cookies
NAMESPACE.addToCart = function(productId, rate) {
	console.log("before ajax call");
	$.ajax({
		url: NAMESPACE.rootURL + "/cart/add/" + productId,
		method: "POST",
		success: function(response) {
			NAMESPACE.setCartItems();
			if(response == "Success") {
				swal({
					icon : "success",
					text : "Item added to cart!"
				});
			}
			else if(response == "Failure") {
				swal({
					icon : "warning",
					text : "Cart Limit Reached for this Product!"
				});
			}
		},
		error : function(error) {
			$(".loader-holder").hide();
			swal({
				icon : "error",
				text : "You are not allowed to add items to cart!"
			});
		}
	});
};


//This function updates the number of items in the cart badge
NAMESPACE.updateCartBadge = function(totalNumberOfItemsInCart) {
	$("#cartBadge").html(totalNumberOfItemsInCart);
	$("#cartBadgeMobile").html(totalNumberOfItemsInCart);
}

NAMESPACE.setCartItems = function() {
	$.ajax({
		url: NAMESPACE.rootURL +"/cart/allProductsInCart/",
		method: "GET",
		success: function(response) {
			NAMESPACE.updateCartBadge(response.length);
			var cartItems = response;
			var innerHtml = "";
			var total = 0;

			    for(var i=0; i<cartItems.length; i++) {

			        total = total + (cartItems[i].quantity * cartItems[i].rate);


			     innerHtml =   innerHtml +
			        "<li class=\"header-cart-item\">" +
			        	"<div class=\"header-cart-item-img\">" +
			        		"<img src="  + "'"  + ((cartItems[i].images[0] && cartItems[i].images[0].image) || '/images/item-01.jpg') + "'" + "/>" +
			        	"</div>" +
			        	"<div class=\"header-cart-item-txt\">" +
			        		"<a href=\"#\" class=\"header-cart-item-name\">" +
			        			cartItems[i].productName + 
			        		"</a>\n" +
			        
			        		"<span class=\"header-cart-item-info\">" +
			        			cartItems[i].quantity + " x  Rs. " + cartItems[i].rate +
			        		"</span>" +
			        	"</div>" +
			        "</li>";

			    }
			    if(cartItems.length <= 0) {
			    	$("#cartList").html("<p> No items in the cart. </p>");
			    	$("#cartListMobile").html("<p> No items in cart. </p>");
			    	$("#cart_btns").hide();
			    	$("#cart_btns_mobile").hide();
			    } else {
			    	$("#cartList").html(innerHtml);
			    	$("#cartListMobile").html(innerHtml);
			    	$("#totalPriceMobile").html("Total:  Rs.  " + total);
				    $("#totalPrice").html( "Total:  Rs.  " + total);
				    $("#cart_btns").show();
				    $("#cart_btns_mobile").show();
			    }
			    
			
		}
	});
}

//This function updates total price of each item when their respective quantity is changed.
NAMESPACE.updateQuantityPrice = function(element, itemNumber, rate, inventoryItems) {
	var quantity = parseInt($(element).val());
	if(quantity < inventoryItems) {
	$("#totalOfItem" + itemNumber).text(parseFloat(rate) * quantity);
	} else {
		swal("You have selected the maximum number for this particular item!");
		var qty = inventoryItems;
		$("#qtyOfItem" + itemNumber).val(qty);
		$("#totalOfItem" + itemNumber).text(parseFloat(rate) * qty);
	}
}




//This function decreases quantity by one when "-" button on item list is clicked
NAMESPACE.decreaseQuantity = function(itemNumber, rate, inventoryItems) {
	
	//var cartItems = JSON.parse(getCookie("cartItems"));
	var quantity = $("#qtyOfItem" + itemNumber).val();
	if(quantity <= 1) {
		$("#qtyOfItem" + itemNumber).val(1);
//		document.getElementById("btn-down-cart").disabled = true;
		this.updateQuantityPrice($("#qtyOfItem" + itemNumber), itemNumber, rate, inventoryItems);
	} else {
		$("#qtyOfItem" + itemNumber).val(quantity - 1);
//		document.getElementById("btn-up-cart").disabled = false;
		this.updateQuantityPrice($("#qtyOfItem" + itemNumber), itemNumber, rate, inventoryItems);
	}
	
}


//This function increases quantity by one when "+" button on item list is clicked.
NAMESPACE.increaseQuantity = function(itemNumber, rate, inventoryItems) {
	//var cartItems = JSON.parse(getCookie("cartItems"));
	var quantity = parseInt($("#qtyOfItem" + itemNumber).val());
	if(quantity < inventoryItems) {
//		document.getElementById("btn-up-cart").disabled = false;
	$("#qtyOfItem" + itemNumber).val(++quantity);
	this.updateQuantityPrice($("#qtyOfItem" + itemNumber), itemNumber, rate, inventoryItems);
	} else {
//		document.getElementById("btn-up-cart").disabled = true;
		$("#qtyOfItem" + itemNumber).val();
		this.updateQuantityPrice($("#qtyOfItem" + itemNumber), itemNumber, rate, inventoryItems);
	} 
} 



// This function removes item from cart detail page and updates quantity and reloads the page.
NAMESPACE.removeItemFromCart = function(itemNumber) {
	var itemNumber = parseInt(itemNumber);
	swal({
		title: "Are you sure?",
		icon: "warning",
		buttons: true,
		dangerMode: true
	}).then(function(confimation) {
		if(confimation) {
			$.ajax({
				url: NAMESPACE.rootURL +"/cart/remove/" + itemNumber,
				method: "POST",
				success: function(response) {
					swal({
						text: response,
						icon: "success",
						closeOnClickOutside: false,
						closeOnEsc: false
					}).then(function() {
						location.reload();
					});
				},
			});
			//location.reload();
		}
	});
	
	
	
//	var cartItems = NAMESPACE.cartList;
//	var totalItems = cartItems.length;
//	var newCartItems = [];
//	var totalItemsToDisplay = 0;
//	
//	
//	for(var i = 0; i < totalItems; i++) {
//		if(i == itemNumber) {
//			continue;
//		} else {
//			newCartItems.push(cartItems[i]);
//			totalItemsToDisplay += parseInt(cartItems[i].quantity);
//		}
//		
//	}
//	
//	NAMESPACE.setCookie('cartItems', JSON.stringify(newCartItems));
//	NAMESPACE.setCookie('totalItems', totalItemsToDisplay);
	
}

//This function updates the cart quantity when update cart function is called.
NAMESPACE.updateCart = function(newCartItems) {
	var cartItems = NAMESPACE.cartList; //JSON.parse(NAMESPACE.getCookie("cartItems"));
	var totalItems = newCartItems.length;
	var totalItemsToDisplay = 0;
	var quantityOfEachItem = [];
	for (var i = 0; i < totalItems; i++) {
		quantityOfEachItem[i] = newCartItems[i]['value'];
		totalItemsToDisplay += parseInt(newCartItems[i]['value']);
	}
	//NAMESPACE.setCookie('cartItems', JSON.stringify(cartItems));
	//NAMESPACE.setCookie('totalItems', totalItemsToDisplay);
	
	//Ajax Call to server
	$.ajax({
		url: NAMESPACE.rootURL +"/cart/updateCart/",
		method: "POST",
		data: JSON.stringify(quantityOfEachItem),
		processData: false,
		contentType: "application/json; charset=utf-8",
		success: function(response) {
			//alert(response);
			swal({
				icon: "success",
				text: response,
				closeOnClickOutside: false,
				closeOnEsc: false
			}).then(function() {
				location.reload();
			});
		},
	});
}



//this self invoking function sets CartLists and total items in cart if they exists in cookies
$(function() {
	if(NAMESPACE.getCookie("cartItems")) {
		NAMESPACE.cartList = JSON.parse(NAMESPACE.getCookie("cartItems"));
	}
	
	if(NAMESPACE.getCookie("totalItems")) {
		NAMESPACE.totalItems = parseInt(NAMESPACE.getCookie("totalItems"));
	}
	
	$("#update-cart-btn").on("click", function(event){
		event.preventDefault();
		var qtyArray = $(".table-row :input").serializeArray();
		swal({
			text: "Do you really want to update cart?",
			icon: "info",
			buttons: true
		}).then(function(confimation) {
			if(confimation) {
				NAMESPACE.updateCart(qtyArray);
			}
		});
	});
	
});


