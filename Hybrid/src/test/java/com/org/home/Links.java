package com.org.home;

import org.testng.annotations.Test;
import com.org.common.BaseTestCase;
import com.org.pages.Home;

public class Links extends BaseTestCase {

	Home homeDash;

	@Override
	public void initPages() {
		homeDash = new Home();
		homeDash.navigateToApplication();
	}

	@Test(priority = 0, description = "Log into the application")
	public void Application_Login() {
		homeDash.loginToApp("testautoteam786@gmail.com", "General@12");
	}

	@Test(priority = 1, description = "Verify Mens Wear Page Navigation")
	public void Test_Mens_Wear_Page() {
		homeDash.pageNavigate("Men", "Men's");
	}

	@Test(priority = 2, description = "Verify Womens Wear Page Navigation")
	public void Test_Womens_Wear_Page() {
		homeDash.pageNavigate("Women", "Women's");
	}

	@Test(priority = 6, description = "Verify Kids Wear Page Navigation")
	public void Test_Kids_Wear_Page() {
		homeDash.isKidsTabOpened();
	}

	@Test(priority = 4, description = "add an itesm to the cart")
	public void Add_An_Item_To_Cart() {
		homeDash.pageNavigate("Men", "Men's");
		homeDash.addItemToCart("Tops");
		homeDash.viewItemFromCart();
	}

	@Test(priority = 5, description = "Test “Save for Later” option ")
	public void Save_For_Later_Option() {
		homeDash.pageNavigate("Men", "Men's");
		homeDash.saveToCart("Tops");
	}

}
