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

public class changePasswordTests {

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
	
	public static void changePassword(String newPassword) throws InterruptedException {
		WebElement passwordForm = null;
		WebElement oldpassword = null;
		WebElement newpassword = null;
		WebElement confirmpassword = null;
		String oldpasswordtext = "qwerty";
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);	
		try {
			passwordForm = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.className("pure-form")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		Thread.sleep(5000);
		
		try {
			oldpassword = driver.findElement(By.name("currentPassword"));
		} catch (Exception e) {
			fail("Fail to locate password element");
		}
		
		try {
			newpassword = driver.findElement(By.name("password"));
		} catch (Exception e) {
			fail("Fail to locate new password element");
		}
		
		try {
			confirmpassword = driver.findElement(By.name("confirmPassword"));
		} catch (Exception e) {
			fail("Fail to locate confirm password element");
		}
		
		if (newPassword.equals("qwerty")) {
			oldpasswordtext = "qwerty1";
		}
		
		oldpassword.sendKeys(new String[] {oldpasswordtext});
		newpassword.sendKeys(new String[] {newPassword});
		confirmpassword.sendKeys(new String[] {newPassword});
		Thread.sleep(5000);
		passwordForm.submit();
	}
	
	@Before
	public void setUp() throws InterruptedException {
		driver = new ChromeDriver();
		System.setProperty("webdriver.chrome.driver", "C:/Users/Dawn/Dropbox/INB372/Selenium/chromedriver_win32/chromedriver.exe");
		driver.get("http://localhost:8080/assignment/index.jsp");
		driver.manage().window().maximize();
		login();
		
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'My Account')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("My Account")).click();
		
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Change Password')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("Change Password")).click();
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
	public void checkClientValidation() throws InterruptedException {
		WebElement oldpassword = null;
		WebElement newpassword = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);
		try {
			oldpassword = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("oldPassword")));
		} catch (Exception e) {
			fail("Cannot locate current password element, page failed to load");
		}
		
		try {
			newpassword = driver.findElement(By.name("password"));
		} catch (Exception e) {
			fail("Fail to locate new password element");
		}
		
		newpassword.sendKeys(new String[] {"%$#"});
		oldpassword.click();
		
		try {
			driver.findElements(By.xpath("//*[contains(text(), 'Invalid character')]"));
		} catch (Exception e) {
			fail("Cannot locate the client side validation");
		}
		
		Thread.sleep(5000);
	}
	
	@Test
	public void checkServerValidation() throws InterruptedException {
		WebElement oldpassword = null;
		WebElement newpassword = null;
		WebElement confirmpassword = null;
		WebElement form = null;
		
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);
		try {
			oldpassword = accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("oldPassword")));
		} catch (Exception e) {
			fail("Cannot locate current password element, page failed to load");
		}
		
		try {
			newpassword = driver.findElement(By.name("password"));
		} catch (Exception e) {
			fail("Fail to locate new password element");
		}
		
		try {
			confirmpassword = driver.findElement(By.name("confirmPassword"));
		} catch (Exception e) {
			fail("Fail to locate new password element");
		}
		
		try {
			form = driver.findElement(By.className("pure-form"));
		} catch (Exception e) {
			fail("Fail to locate new password element");
		}
		
		newpassword.sendKeys(new String[] {"qwerty1"});
		oldpassword.sendKeys(new String[] {"qwerty1"});
		confirmpassword.sendKeys(new String[] {"qwerty1"});
		Thread.sleep(5000);
		form.submit();
		
		try {
			driver.findElements(By.xpath("//*[contains(text(), 'You entered your current')]"));
		} catch (Exception e) {
			fail("Cannot locate the client side validation");
		}
		
		Thread.sleep(5000);
	}
	
	@Test
	public void checkPageLoad() throws InterruptedException {
		WebDriverWait accountPageLoad = new WebDriverWait(driver, 30);
		try {
			accountPageLoad.until(ExpectedConditions.presenceOfElementLocated(By.id("oldPassword")));
		} catch (Exception e) {
			fail("Cannot locate element, page failed to load");
		}
		Thread.sleep(5000);
	}
	
	@Test
	public void checkChangingWorks() throws InterruptedException {
		
		changePassword("qwerty1");
		
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Logout')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.xpath("//a[contains(@href, '#')]")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("Logout")).click();
		Thread.sleep(5000);
		
		driver.get("http://localhost:8080/assignment/index.jsp");
		
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

		username.sendKeys(new String[] {"j"});
		password.sendKeys(new String[] {"qwerty1"});
		Thread.sleep(5000);
		form.submit();
		
		try {
			pageLoaded.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Logout')]")));
		} catch (Exception e) {
			fail("Login with new details failed");
		}
		
		Thread.sleep(3000);
		
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'My Account')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("My Account")).click();
		
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Change Password')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		driver.findElement(By.linkText("Change Password")).click();
		
		changePassword("qwerty");
		driver.findElement(By.xpath("//a[contains(@href, '#')]")).click();
		Thread.sleep(3000);
	}

	@Test
	public void checkNotLoggedIn() throws InterruptedException {
		
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Logout')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		Thread.sleep(5000);
		driver.findElement(By.linkText("Logout")).click();
		
		Thread.sleep(3000);
		driver.get("http://localhost:8080/assignment/ChangePassword.jsp");
		
		Thread.sleep(5000);
		WebDriverWait loginNotFound = new WebDriverWait(driver, 30);
		try {
			loginNotFound.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'You are not logged in')]")));
		} catch (Exception e) {
			fail("Login not found when accessing Change Password page - error page not displayed");
		}
		
		driver.close();
		driver = null;
	}
}
