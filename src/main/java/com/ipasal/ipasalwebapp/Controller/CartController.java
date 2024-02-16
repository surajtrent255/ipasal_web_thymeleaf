package com.ipasal.ipasalwebapp.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ipasal.ipasalwebapp.dto.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ipasal.ipasalwebapp.Services.ProductServices;

@Controller
@SessionAttributes({"cart", "totalPrice"})
@RequestMapping("/cart")
public class CartController {
	private final ProductServices productServices;

	Logger logger = LoggerFactory.getLogger(CartController.class);

	
	public CartController(ProductServices productServices) {
		this.productServices = productServices;
	}

	@SuppressWarnings("unchecked")
	protected int checkItemInCartSession(int itemId, HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<ProductDTO> cartItems = new ArrayList<>();

		if (session.getAttribute("cart") != null) {
			cartItems = (List<ProductDTO>) session.getAttribute("cart");
		}

		for (int i = 0; i < cartItems.size(); i++) {
			if (cartItems.get(i).getProductId() == itemId) {
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Secured({"ROLE_CUSTOMER","ROLE_ANONYMOUS"})
	@RequestMapping(value = "/add/{itemId}", method = RequestMethod.POST)
	public @ResponseBody String addItemToCart(@PathVariable("itemId") int itemId, HttpServletRequest request) {
		List<ProductDTO> products = new ArrayList<>();
		ProductDTO product = ((List<ProductDTO>) productServices.getProductById(itemId).getData()).get(0);
		product.setQuantity(1);
		int availableProducts = product.getAvailableItems();
		if (request.getSession().getAttribute("cart") == null) {
			products.add(product);

		} else {
			List<ProductDTO> cartItems = new ArrayList<>();
			HttpSession session = request.getSession();
			cartItems = (List<ProductDTO>) session.getAttribute("cart");
			int itemIndexInCartList = checkItemInCartSession(itemId, request);
			if (itemIndexInCartList > -1) {
				if(cartItems.get(itemIndexInCartList).getQuantity() < availableProducts) {
					cartItems.get(itemIndexInCartList).setQuantity(cartItems.get(itemIndexInCartList).getQuantity() + 1);
					
				}
				else {
					return "Failure";
				}
			} else {
					cartItems.add(product);
			}

			products = cartItems;

		}

		request.getSession().setAttribute("cart", products);
		request.getSession().setAttribute("totalPrice", this.calculateTotalPrice(products));
		return "Success";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/remove/{cartItemNo}", method = RequestMethod.POST)
	public @ResponseBody String removeItemFromCart(@PathVariable("cartItemNo") int cartItemNo,
			HttpServletRequest request) {
		List<ProductDTO> products;
		HttpSession session = request.getSession();
		if (session.getAttribute("cart") != null) {
			products = (List<ProductDTO>) session.getAttribute("cart");
			products.remove(cartItemNo);
			if(products.size() <= 0) {
				session.removeAttribute("cart");
				session.removeAttribute("totalPrice");
			} else {
				session.setAttribute("cart", products);
				session.setAttribute("totalPrice", this.calculateTotalPrice(products));
			}
		} else {
			return "You are trying to remove item from empty cart!";
		}

		
		return "Successfully removed item from cart";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateCart", method = RequestMethod.POST)
	public @ResponseBody String updateCart(@RequestBody int[] quantityOfEachItems, HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<ProductDTO> products = new ArrayList<>();

		products = (List<ProductDTO>) session.getAttribute("cart");

		for (int i = 0; i < products.size(); i++) {
			products.get(i).setQuantity(quantityOfEachItems[i]);
		}

		request.getSession().setAttribute("cart", products);
		return "Successfully updated the cart";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/allProductsInCart", method = RequestMethod.GET)
	public @ResponseBody List<ProductDTO> getAllProductsInCart(HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<ProductDTO> products = new ArrayList<>();
		products = (List<ProductDTO>) session.getAttribute("cart");
		if (products != null && products.size() > 0) {
			return products;
		} else {
			return new ArrayList<ProductDTO>();
		}
	}
	
	
	public double calculateTotalPrice(List<ProductDTO> products) {
		Double totalPrice = 0d;
		if(products != null & products.size() > 0) {
			for(int i = 0; i < products.size(); i++) {
				totalPrice += products.get(i).getRate() * products.get(i).getQuantity();
			}
		}
		return totalPrice;
	}
}
