package tests;

import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;

public class accountDetailsTests {

	private static WebDriver driver;
	
	public static void login() throws InterruptedException {
		WebElement form = null;
		WebElement username = null;
		WebElement password = null;
		
		WebDriverWait pageLoaded = new WebDriverWait(driver, 30);
		try {
			form = pageLoaded.until(ExpectedConditions.presenceOfElementLocated(By.className("pure-form")));
		} catch (Exception e) {
			System.out.println("Unable to locate element, Page not loaded");
		}
		
		try {
			username = driver.findElement(By.name("username"));
		} catch (Exception e) {
			System.out.println("Fail to locate username element");
		}
		
		try {
			password = driver.findElement(By.name("password"));
		} catch (Exception e) {
			System.out.println("Fail to locate password element");
		}

		username.sendKeys(new String[] {"j"});
		password.sendKeys(new String[] {"qwerty"});
		form.submit();
	}
	
	@Before
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		System.setProperty("webdriver.chrome.driver", "C:/Users/Dawn/Dropbox/INB372/Selenium/chromedriver_win32/chromedriver.exe");
		driver.get("http://localhost:8080/assignment/index.jsp");
		driver.manage().window().maximize();
		login();
		
		Thread.sleep(2000);
		
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'My Account')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("My Account")).click();
	}
	
	@After
	public void logOut() {
		if (driver != null) {
			WebDriverWait pageLoad = new WebDriverWait(driver, 30);
			try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Logout')]")));
			} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
			}
			driver.findElement(By.linkText("Logout")).click();
			driver.close();
		}
	}
	
	@Test
	public void loadAccountPage() throws InterruptedException {		
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);
		try {
			accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		Thread.sleep(5000);
	}
	
	@Test
	public void checkAccountData() throws InterruptedException, ClassNotFoundException, SQLException {
		
		WebElement firstName = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);
		try {
			firstName = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		String firstNameText = firstName.getAttribute("value");
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT fName, lName FROM carers WHERE carerID='4'");
			rs.next();
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		Thread.sleep(3000);
		
		if (firstNameText.equals(rs.getString(1))) {

		} else {
			fail("Data retreived from database was incorrect");
		}
		
		rs.close();
		st.close();
		conn.close();
	}
	
	@Test
	public void editAccountData() throws InterruptedException {
		WebElement firstName = null;
		WebElement formButton = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);
		try {
			firstName = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		firstName.sendKeys(Keys.chord(Keys.CONTROL, "a"), "jjo");
		
		Thread.sleep(3000);
		
		try {
			formButton = driver.findElement(By.className("pure-button"));
		} catch (Exception e) {
			fail("Unable to locate element, Page not loaded");
		}		
		formButton.click();
		Thread.sleep(2000);
		
		try {
			firstName = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		driver.findElement(By.xpath("//a[contains(@href, '#')]")).click();
		Thread.sleep(3000);
		
		if (firstName.getAttribute("value").equals("jjo")) {
			return;
		} else {
			fail("Data was not edited on database after submitting");
		}
	}
	
	@Test
	public void checkClientValidation() throws InterruptedException {
		WebElement firstName = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);
		try {
			firstName = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		firstName.sendKeys(Keys.chord(Keys.CONTROL, "a"), "j778");
		Thread.sleep(3000);
		
		try {
			driver.findElement(By.id("lastName")).click();
		} catch (Exception e) {
			fail("cannot find element, something unknown happened");
		}
		
		Thread.sleep(5000);
		try {
			driver.findElements(By.xpath("//*[contains(text(), 'only contain')]"));
			return;
		} catch (Exception e) {
			fail("Javascript validation on client side not found");
		}
	}
	
	@Test
	public void checkServerValidation() throws InterruptedException {
		WebElement lastName = null;
		WebElement formButton = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);
		try {
			lastName = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("lastName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		lastName.sendKeys(Keys.chord(Keys.CONTROL, "a"), "j778");
		Thread.sleep(3000);
		
		try {
			formButton = driver.findElement(By.className("pure-button"));
		} catch (Exception e) {
			fail("Unable to locate element, Page not loaded");
		}		
		formButton.click();
		Thread.sleep(2000);
		
		try {
			driver.findElement(By.id("lastName"));
		} catch (Exception e) {
			fail("cannot find element, page not loaded correctly");
		}
		
		Thread.sleep(5000);
		try {
			driver.findElements(By.xpath("//*[contains(text(), 'only contain')]"));
			return;
		} catch (Exception e) {
			fail("Server-side validation not found");
		}
	}
	
	@Test
	public void checkNotLoggedIn() throws InterruptedException {
		
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		try {
		pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Logout')]")));
		} catch (Exception e) {
		fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("Logout")).click();
		
		Thread.sleep(5000);
		driver.get("http://localhost:8080/assignment/AccountDetails.jsp");
		
		Thread.sleep(5000);
		WebDriverWait loginNotFound = new WebDriverWait(driver, 30);
		try {
			loginNotFound.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'You are not logged in')]")));
		} catch (Exception e) {
			fail("Login not found when accessing AccountDetails page - error page not displayed");
		}
		
		driver.close();
		driver = null;
	}
}
