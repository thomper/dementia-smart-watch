package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class registerTests {

	private static WebDriver driver;
	
	@Before
	public void setUp() {
		driver = new ChromeDriver();
		System.setProperty("webdriver.chrome.driver", "C:/Users/Dawn/Dropbox/INB372/Selenium/chromedriver_win32/chromedriver.exe");
		driver.get("http://localhost:8080/assignment/index.jsp");
		driver.manage().window().maximize();
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);	
		try {
			accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.linkText("click here")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		driver.findElement(By.linkText("click here")).click();
	}
	
	@After
	public void tearDown() {
		driver.close();
	}
	
	@Test
	public void checkClientValidation() throws InterruptedException {
		WebElement fName = null;
		WebElement lName = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);	
		try {
			fName = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
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
		WebElement registerForm = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);	
		try {
			fName = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			registerForm = driver.findElement(By.className("pure-form"));
		} catch (Exception e) {
			fail("cannot locate element, something has gone wrong");
		}
		
		fName.sendKeys(new String[] {"%$#"});
		registerForm.submit();
		Thread.sleep(3000);
		
		try {
			driver.findElements(By.xpath("//*[contains(text(), 'First name can only')]"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
	}
	
	@Test
	public void checkRegisterWorking() throws InterruptedException {
		WebElement fName = null;
		WebElement lName = null;
		WebElement mobile = null;
		WebElement email = null;
		WebElement alternateMobile = null;
		WebElement username = null;
		WebElement password = null;
		WebElement confirmPassword = null;
		WebElement form = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);	
		try {
			fName = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			form = driver.findElement(By.className("pure-form"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			lName = driver.findElement(By.id("lastName"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			mobile = driver.findElement(By.id("mobile"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			alternateMobile = driver.findElement(By.id("alternateMobile"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			email = driver.findElement(By.id("email"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			username = driver.findElement(By.id("username"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			password = driver.findElement(By.id("password"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		try {
			confirmPassword = driver.findElement(By.id("confirmPassword"));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		
		fName.sendKeys(new String[] {"Jason"});
		lName.sendKeys(new String[] {"Jason"});
		mobile.sendKeys(new String[] {"123456789"});
		alternateMobile.sendKeys(new String[] {"123456789"});
		email.sendKeys(new String[] {"jason@jason.com"});
		username.sendKeys(new String[] {"hello123"});
		password.sendKeys(new String[] {"hello123"});
		confirmPassword.sendKeys(new String[] {"hello123"});
		
		Thread.sleep(5000);
		form.submit();
		
		Thread.sleep(5000);
		WebDriverWait pageLoaded = new WebDriverWait(driver, 30);
		try {
			pageLoaded.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Your Registration Was Successful!')]")));
		} catch (Exception e) {
			fail("Cannot locate element, registration failed");
		}
		
		driver.get("http://localhost:8080/assignment/index.jsp");
		
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

		username.sendKeys(new String[] {"hello123"});
		password.sendKeys(new String[] {"hello123"});
		Thread.sleep(5000);
		form.submit();
		Thread.sleep(3000);
		
		WebElement logout = null;
		try {
			logout = pageLoaded.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Logout')]")));
		} catch (Exception e) {
			fail("Cannot locate element, login failed, registration didn't happen");
		}
		
		logout.click();
	}
	
	@Test
	public void checkPageLoad() throws InterruptedException {
		Thread.sleep(5000);
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);	
		try {
			accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Back To Login")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
	}

}
