package tests;

import org.testng.annotations.Test;
import pages.AvailableBusesPage;
import pages.BusSearchPage;
import pages.CheckOutPage;
import pages.HomePage;

public class MakeMyTripBusTests extends TestBase {

    @Test(dataProvider = "bookingData")
    public void makeMyTripBusBookingFlow(String from, String to, String date,String name, String age, String gender, String email, String number)throws InterruptedException {

        HomePage homePage = new HomePage(driver);
        BusSearchPage busSearchPage = new BusSearchPage(driver);
        AvailableBusesPage availableBusesPage = new AvailableBusesPage(driver);
        CheckOutPage checkOutPage = new CheckOutPage(driver);

        test.info("Closing login popup if visible.");
        homePage.closeLoginPopupIfVisible();

        test.info("Entering 'From' city: " + from);
        busSearchPage.setFormCity(from);

        test.info("Entering 'To' city: " + to);
        busSearchPage.setToCity(to);

        test.info("Selecting journey date: " + date);
        busSearchPage.selectJourneyDate(date);

        test.info("Clicking search button.");
        busSearchPage.clickSearchButton();

        try {
            test.info("Retrieving available buses.");
            int count = availableBusesPage.getAvailableBuses();
            if (count != 0) {
                test.info("Selecting seat.");
                availableBusesPage.checkIfSeatisSelected();
                test.info("Filling passenger details.");
                checkOutPage.enterFormDetails(name, age, gender, email, number);
                test.pass("Booking flow completed for " + from + " to " + to);
            } else {
                test.warning("No buses found. Redirecting to homepage.");
            }
        } catch (Exception e) {
            test.fail("Booking failed from " + from + " to " + to + " on " + date + ". Reason: " + e.getMessage());
        } finally {
            driver.get("https://www.makemytrip.com/bus-tickets/");
        }
    }
}
