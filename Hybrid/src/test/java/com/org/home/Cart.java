package com.org.home;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.org.common.BaseTestCase;
import com.org.pages.Home;

public class Cart extends BaseTestCase {

	Home homeDash;

	@Override
	public void initPages() {
		homeDash = new Home();
		homeDash.navigateToApplication();
	}

	@Test(priority = 1, description = "add an itesm to the cart")
	public void Add_An_Item_To_Cart() {
		System.out.println(formatToDigitalClock(1643));
		homeDash.pageNavigate("Men", "Men's");
		homeDash.addItemToCart("Tops");
		homeDash.viewItemFromCart();
	}

	@Test(priority = 2, description = "add itesm to wishlist")
	public void addItesmToWishList() {
		homeDash.pageNavigate("Men", "Men's");
		homeDash.saveToCart("Tops");
	}

	public String formatToDigitalClock(long miliSeconds) {
		int hours = (int) (TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24);
		int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60);
		int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60);
		if (hours > 0)
			return String.format("%d:%02d:%02d", hours, minutes, seconds);
		else if (minutes > 0)
			return String.format("00:%02d:%02d", minutes, seconds);
		else if (seconds > 0)
			return String.format("00:00:%02d", seconds);
		else
			return "00:00";
	}

}
