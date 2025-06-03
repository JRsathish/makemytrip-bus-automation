package pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;

public class BusSearchPage extends BasePage {

	public BusSearchPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(css = "input[data-cy='fromCityVal']")
	WebElement fromInput;

	@FindBy(xpath = "//input[contains(@placeholder,\"From\")]")
	WebElement fromField;

	@FindBy(xpath = "//input[contains(@placeholder,\"To\")]")
	WebElement toField;

	@FindBy(css = "label[for='travelDate']")
	WebElement travelDateLabel;

	@FindBy(css = "span.DayPicker-NavButton--next")
	WebElement nextMonthBtn;

	@FindBy(id = "search_button")
	WebElement searcButton;

	
	/**
	 * Enter the source city in the "From" field
	 */
	public void setFormCity(String fromCity) throws InterruptedException {
        fromInput.click(); 
        wait.until(ExpectedConditions.visibilityOf(fromField)).sendKeys(fromCity);
        Thread.sleep(1500); // Wait for suggestions to populate
        fromField.sendKeys(Keys.ARROW_DOWN, Keys.ENTER); // Select the first suggestion
	}

	
	/**
	 * Enter the destination city in the "To" field
	 */
	public void setToCity(String toCity) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(toField)).sendKeys(toCity);
        Thread.sleep(1500); // Wait for suggestions to populate
        toField.sendKeys(Keys.ARROW_DOWN, Keys.ENTER); // Select the first suggestion
	}

	
	/**
	 * Choose the desired travel date from the calendar
	 */
	public void selectJourneyDate(String targetDate) throws InterruptedException {

		// Parse the input date string into LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(targetDate, formatter);

		// Extract individual date parts
		String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		int day = date.getDayOfMonth();
		int year = date.getYear();

		// Format expected aria-label, e.g. "Wed Jun 25 2025"
		String ariaLabel = String.format("%s %s %02d %d", dayOfWeek, month, day, year);

		// Loop to navigate through months until the target date is found
		while (true) {
			List<WebElement> days = driver.findElements(By.xpath("//div[@aria-label='" + ariaLabel + "']"));

			if (!days.isEmpty()) {
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0,120)");
				WebElement dateElement = days.get(0);
				Thread.sleep(500);
				wait.until(ExpectedConditions.elementToBeClickable(dateElement)).click();
				break; 
			} else {
				wait.until(ExpectedConditions.elementToBeClickable(nextMonthBtn)).click();
			}
		}
	}

	
	/**
	 * Click the Search button to look for first available buses
	 */
	public void clickSearchButton() {
		searcButton.click();
	}

}
