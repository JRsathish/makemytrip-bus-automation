package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;


public class HomePage extends BasePage {
	

	public HomePage(WebDriver driver) {
		super(driver);
	}

	@FindBy(css = "span[data-cy='closeModal']")
	WebElement closeButton;
	
	By closeModalButton = By.cssSelector("span.commonModal__close");

    /**
     * Closes the login modal popup if it is present and visible.
     */
	public void closeLoginPopupIfVisible() {
	    List<WebElement> closeButtons = driver.findElements(closeModalButton);
	    if (!closeButtons.isEmpty() && closeButtons.get(0).isDisplayed()) {
	        closeButtons.get(0).click();
	    } 
	}	
}
