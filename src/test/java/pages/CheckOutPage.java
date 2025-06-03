package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;

public class CheckOutPage extends BasePage {

	public CheckOutPage(WebDriver driver) {
		super(driver);
	}

	By pleaseWaitModal = By.xpath("//*[contains(text(),'Confirming availability of your seats')]");

	@FindBy(id = "fname")
	WebElement userName;

	@FindBy(id = "age")
	WebElement userAge;

	@FindBy(id = "contactEmail")
	WebElement userEmail;

	@FindBy(id = "mobileNumber")
	WebElement userNumber;

	@FindBy(xpath = "//span[@class='checkboxWpr']//b[1]")
	WebElement confirmTheState;

	@FindBy(xpath = "//*[@class=\"mainContainer\"]")
	List<WebElement> insuranceTab;
	
	@FindBy(xpath = "//*[@class=\"mainContainer\"]//input")
	List<WebElement> insuranceCheckBoxes; 

	@FindBy(xpath = "(//span[@class='sc-lhVmIH gLODGR'])[2]")
	WebElement deniedInsurance;
	
	@FindBy(xpath = "//div[@class='font12 appendBottom12']/following-sibling::div[1]")
	WebElement continueButton;

	/**
	 * Enter passenger details including name, age, gender, email, and mobile number
	 */
	public void enterFormDetails(String name, String age, String gender, String mail, String number) {
		userName.sendKeys(name);
		userAge.sendKeys(age);
		WebElement genderperfference = driver.findElement(By.xpath("//div[normalize-space(text())='" + gender + "']"));
		genderperfference.click();
		userEmail.sendKeys(mail);
		userNumber.sendKeys(number);
		confirmTheState.click();
		if (!insuranceTab.isEmpty()) {
			int checkBoxCounts = insuranceCheckBoxes.size();
			if(checkBoxCounts == 2 ) {
				deniedInsurance.click();
			} 		
		}
		continueButton.click();
	}

}
