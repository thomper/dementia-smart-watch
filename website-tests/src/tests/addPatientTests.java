package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class addPatientTests {

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
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'My Patients')]")));
		} catch (Exception e) {
			fail("cannot locate the link, page didn't load");
		}
		driver.findElement(By.linkText("My Patients")).click();
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
	public void addPatientPageLoads() {
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Add Patients')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("Add Patients")).click();
		
		try {
			pageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Add patients page failed to load");
		}
		
	}
	
	@Test
	public void checkClientValidation() throws InterruptedException {
		WebElement fName = null;
		WebElement lName = null;
		
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Add Patients')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("Add Patients")).click();
		
		WebDriverWait addPatientPageLoad = new WebDriverWait(driver, 30);	
		try {
			fName = addPatientPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			lName = driver.findElement(By.id("lastName"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		fName.sendKeys(new String[] {"%$#"});
		lName.click();
		Thread.sleep(3000);
		
		try {
			driver.findElements(By.xpath("//*[contains(text(), 'First name can only')]"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
	}
	
	@Test
	public void checkServerValidation() throws InterruptedException {
		WebElement fName = null;
		WebElement patientForm = null;
		
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Add Patients')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("Add Patients")).click();
		
		WebDriverWait addPatientPageLoad = new WebDriverWait(driver, 30);	
		try {
			fName = addPatientPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			patientForm = driver.findElement(By.className("pure-form"));
		} catch (Exception e) {
			fail("cannot locate element, something has gone wrong");
		}
		
		fName.sendKeys(new String[] {"%$#"});
		Thread.sleep(3000);
		patientForm.submit();
		Thread.sleep(3000);
		
		try {
			driver.findElements(By.xpath("//*[contains(text(), 'Some information you entered')]"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		Thread.sleep(3000);
		driver.get("http://localhost:8080/assignment/Home.jsp");
	}
	
	@Test
	public void addingPatientWorks() throws InterruptedException, ClassNotFoundException, SQLException {
		WebElement firstName = null;
		WebElement lastName = null;
		WebElement emergName = null;
		WebElement age = null;
		WebElement medication = null;
		WebElement address = null;
		WebElement suburb = null;
		WebElement emergSuburb = null;
		WebElement emergNum = null;
		WebElement emergAddress = null;
		WebElement conNum = null;
		WebElement form = null;
		
		WebDriverWait addPatientPageLoad = new WebDriverWait(driver, 30);	
		try {
			addPatientPageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Add Patients')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("Add Patients")).click();
		
		try {
			form = addPatientPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.className("pure-form")));
		} catch (Exception e) {
			fail("add patients page failed to load");
		}
		Thread.sleep(5000);
		
		try {
			firstName = driver.findElement(By.id("firstName"));
			lastName = driver.findElement(By.id("lastName"));
			emergName = driver.findElement(By.id("emergName"));
			age = driver.findElement(By.id("age"));
			medication = driver.findElement(By.id("medication"));
			address = driver.findElement(By.id("address"));
			conNum = driver.findElement(By.id("conNum"));
			emergNum = driver.findElement(By.id("emergNum"));
			suburb = driver.findElement(By.id("suburb"));
			emergAddress = driver.findElement(By.id("emergAddress"));
			emergSuburb = driver.findElement(By.id("emergSuburb"));
		} catch (Exception e) {
			fail("Cannot locate element, page may have failed to load");
		}
		
		firstName.sendKeys(new String[] {"jason"});
		lastName.sendKeys(new String[] {"johnston"});
		emergName.sendKeys(new String[] {"hello"});
		medication.sendKeys(new String[] {"hello"});
		address.sendKeys(new String[] {"hello"});
		age.sendKeys(new String[] {"73"});
		conNum.sendKeys(new String[] {"12345678"});
		emergNum.sendKeys(new String[] {"12345678"});
		emergAddress.sendKeys(new String[] {"hello"});
		emergSuburb.sendKeys(new String[] {"hello"});
		suburb.sendKeys(new String[] {"hello"});
		Thread.sleep(5000);
		form.submit();
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			st.executeUpdate("UPDATE patients SET patientID = 8 WHERE fName = 'jason'");
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		
		st.close();
		conn.close();
		
		driver.findElement(By.xpath("//a[contains(@href, '#')]")).click();
		
		Thread.sleep(3000);
	}

}
