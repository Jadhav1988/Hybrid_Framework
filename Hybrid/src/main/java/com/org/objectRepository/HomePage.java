package com.org.objectRepository;

public interface HomePage {
	// all the menus write down the text like Men, Women,Jeans,Active,Purpose,Fierce
	String SECTIONS = "//span[contains(text(),'%s')]/following-sibling::a[starts-with(@id,'cat-label')]";
	String HEADER_TEXT = "//a[contains(@class,'breadcrumbs-link')]";
	String KIDS_LINK = "//*[@id='cat-label-176705']";
	String KIDS_HEADER = "(//div[@class='logo ']//span[@class='screen-reader-text'])[1]";
	String SIGN_IN_CREATE = "//div[@id='customer-web-service-SignInBlockFrontend']";
	String SIGN_IN = "(//span[@data-property='GLB_SIGNIN'])[last()]/parent::button";
	String SIGN_IN_EMAIL = "email-sign-in";// ID
	String SIGN_IN_PASSWORD = "login-password-field-sign-in";// ID
	String LOGED_IN = "//span[contains(text(),'Sign In') and @property='GLB_SIGNIN']/parent::button";
	String USER_DETAILS = "(//span[@data-property='ACT_HEY_USER'])[1]";
	String CREATE_ACCOUNT = "(//span[text()=' Create Account'])[2]";
	String CREATE_EMAIL_ADDRESS = "email-create-account";// ID
	String CREATE_PASSWORD = "login-password-field-create-account";// ID
	String CONTINUE = "//span[@property='GLB_CONTINUE']";
	String MEN_SUB_CAT = "//a[contains(@class,'grid-nav')and contains(text(),'%s')]";
	String FIRST_ITEM = "(//li[contains(@class,'product-card')]//div[contains(@class,'product-template')])[1]";
	String COLORS = "//*[@aria-labelledby='colors']//img";
	String COLORS_LAST = "(//*[@aria-labelledby='colors']//img)[last()]";
	String SIZE_LAST = "(//input[@name='size-primary'][not(@disabled)])[last()]";
	String QUANTITY = "quantity";// NAME
	String ADDTOBAG = "//button[contains(@class,'add-to-bag')]";
	String BAG = "//a[@data-aui='shopping-bag-nav-link']";
	String SAVE_LATER = "//button[@aria-label='Save For Later']";
}
