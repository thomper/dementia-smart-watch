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

public class loginTests {

	private static WebDriver driver;
	
	@Before
	public void setUp() {
		driver = new ChromeDriver();
		System.setProperty("webdriver.chrome.driver", "C:/Users/Dawn/Dropbox/INB372/Selenium/chromedriver_win32/chromedriver.exe");
		driver.get("http://localhost:8080/assignment/index.jsp");
		driver.manage().window().maximize();
	}
	
	@After
	public void tearDown() {
		driver.close();
	}
	
	@Test
	public void loginSuccess() throws InterruptedException {
		
		WebElement form = null;
		WebElement username = null;
		WebElement password = null;
		
		WebDriverWait pageLoaded = new WebDriverWait(driver, 30);
		try {
			form = pageLoaded.until(ExpectedConditions.presenceOfElementLocated(By.className("pure-form")));
		} catch (Exception e) {
			fail("Unable to locate element, Page not loaded");
		}
		
		try {
			username = driver.findElement(By.name("username"));
		} catch (Exception e) {
			fail("Fail to locate username element");
		}
		
		try {
			password = driver.findElement(By.name("password"));
		} catch (Exception e) {
			fail("Fail to locate password element");
		}

		Thread.sleep(2000);
		username.sendKeys(new String[] {"j"});
		password.sendKeys(new String[] {"qwerty"});
		Thread.sleep(5000);
		form.submit();
		
		WebDriverWait loginWait = new WebDriverWait(driver, 30);
		try {
			loginWait.until(ExpectedConditions.presenceOfElementLocated(By.id("alert")));
		} catch (Exception e) {
			fail("Login failed or home page not loaded");
		}	
	}
	
	@Test
	public void loginButtonDisabledNoFormData() throws InterruptedException {
		
		WebElement formButton = null;
		boolean loginFail = false;
		
		WebDriverWait pageLoaded = new WebDriverWait(driver, 30);
		try {
			pageLoaded.until(ExpectedConditions.presenceOfElementLocated(By.className("pure-form")));
		} catch (Exception e) {
			fail("Unable to locate element, Page not loaded");
		}
		
		try {
			formButton = driver.findElement(By.className("pure-button"));
		} catch (Exception e) {
			fail("Unable to locate element, Page not loaded");
		}
		
		formButton.click();
		Thread.sleep(5000);
		
		try {
			driver.findElement(By.name("username"));
			loginFail = true;
		} catch (Exception e) {
			loginFail = false;
			fail("Fail to locate username element");
		}
		
		if (loginFail) {
			return;
		} else {
			fail("Unexpected behaviour happened");
		}
	}
	
	@Test
	public void loginFail() throws InterruptedException {
		
		WebElement form = null;
		WebElement username = null;
		WebElement password = null;
		
		WebDriverWait pageLoaded = new WebDriverWait(driver, 30);
		try {
			form = pageLoaded.until(ExpectedConditions.presenceOfElementLocated(By.className("pure-form")));
		} catch (Exception e) {
			fail("Unable to locate element, Page not loaded");
		}
		
		try {
			username = driver.findElement(By.name("username"));
		} catch (Exception e) {
			fail("Fail to locate username element");
		}
		
		try {
			password = driver.findElement(By.name("password"));
		} catch (Exception e) {
			fail("Fail to locate password element");
		}
		
		Thread.sleep(2000);
		username.sendKeys(new String[] {"j"});
	  	password.sendKeys(new String[] {"jj"});
	  	form.submit();
	  	Thread.sleep(5000);  // Let the user actually see something!
	  	
		WebDriverWait loginFail = new WebDriverWait(driver, 30);
		try {
			loginFail.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'An error has occurred')]")));
		} catch (Exception e) {
			fail("Login failed or home page not loaded");
		}	
	}	
	
	@Test
	public void checkHomePageNotLoggedInError() throws InterruptedException {
		Thread.sleep(5000);
		driver.get("http://localhost:8080/assignment/Home.jsp");
		
		Thread.sleep(5000);
		WebDriverWait loginNotFound = new WebDriverWait(driver, 30);
		try {
			loginNotFound.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'You are not logged in')]")));
		} catch (Exception e) {
			fail("Login not found when accessing home page - error page not displayed");
		}
	}
}
