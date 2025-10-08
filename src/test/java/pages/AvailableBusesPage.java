package pages;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;

public class AvailableBusesPage extends BasePage {

	public AvailableBusesPage(WebDriver driver) {
		super(driver);
	}

	By searchButton = By.xpath("//button[contains(text(),'Search')]");

	@FindBy(xpath = "//*[text()='Selected Seats']/following-sibling::button | //*[text()='Selected Seats']/following::div[contains(@class,\"active\")]")
	WebElement clickContinueButton;

	By seatNumber = By.xpath("//*[text()='Selected Seats']//following::span[1]");

	@FindBy(xpath = "//div[@class='busCard false' or contains(@class, 'BusCard_listingTop')]")
	List<WebElement> busHeader;

	@FindBy(xpath = "//*[self::div or self::button][normalize-space(text())='Select Seats']")
	WebElement selectThebus;

	@FindBy(xpath = "//img[contains(@srcset,'Available')] | //span[@data-testid='seat_horizontal_sleeper_available']")
	List<WebElement> availableSeats;

	@FindBy(xpath = "//span[@data-testid=\"seat_horizontal_sleeper_unavailable\"]")
	List<WebElement> unavailableSeats;

	@FindBy(xpath = "(//div[contains(@class, 'PickUpDropSelection_pickDropContainer__VSr2j')]//div)[3]")
	WebElement fristPickupPoint;

	@FindBy(xpath = "//div[normalize-space(text())='Drop Points']//following::div/div")
	WebElement fristDropPoint;
	
	/**
	 * Fetch the list of available buses and seat information
	 */
	public int getAvailableBuses() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(searchButton));
		if (busHeader.isEmpty()) {
			System.out.println("Buses not available on the date");
			return 0;
		}
		wait.until(ExpectedConditions.visibilityOfAllElements(selectThebus));
		selectThebus.click();
		Random random = new Random();
		int randomIndex = random.nextInt(availableSeats.size());
		wait.until(ExpectedConditions.elementToBeClickable(availableSeats.get(randomIndex))).click();
		return busHeader.size();
	}

	/**
	 * If buses are available, select a seat
	 */
	public void checkIfSeatisSelected() throws InterruptedException {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(seatNumber));
			WebElement seatNumberElement = driver.findElement(seatNumber);
			String selectedSeatNumber = seatNumberElement.getText();
			System.out.println("Select Seat Number: " + selectedSeatNumber);
			String classAttr = clickContinueButton.getAttribute("class");
			if (classAttr.contains("Button_disabled")) {
				fristPickupPoint.click();
				fristDropPoint.click();
				wait.until(ExpectedConditions.elementToBeClickable(clickContinueButton)).click();
			} else if (classAttr.contains("Button_primary")) {
				clickContinueButton.click();
			} else{	
				clickContinueButton.click();
			}
		} catch (NoSuchElementException e) {
			System.out.println("Seat not selected");
		}
	}

}