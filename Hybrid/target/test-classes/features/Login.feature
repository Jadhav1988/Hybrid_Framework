Feature: Verify whether the user is able to login and logout from the application

@Login
Scenario: Login with the valid user details
	Given user is on home page 
	When user enters partnerId
	And user enters user name 
	And user enters password
	And click on login
	Then dashboard page should be display with logged in user detail

@LogOut
Scenario: logout from the EAP application
	Given user is logged in to the eap application
	When user click on logout button
	Then user should redirect to the login page
	