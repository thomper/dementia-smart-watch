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

public class patientListTests {

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
	public void updatePatientLinkWorks() throws InterruptedException {
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Change Details')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		Thread.sleep(5000);
		driver.findElement(By.xpath("//a[contains(@href,'PatientDetails.jsp?patientid=6')]")).click();
		
		Thread.sleep(5000);
		
		if (driver.getCurrentUrl().equals("http://localhost:8080/assignment/PatientDetails.jsp?patientid=8")) {
			return;
		} else {
			fail("Patient details page failed to load for an unknown reason");
		}
	}
	
	@Test
	public void patientLocationLinkWorks() throws InterruptedException {
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
		
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Location')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		Thread.sleep(5000);
		driver.findElement(By.xpath("//a[contains(@href,'Map.jsp?patientid=6')]")).click();
		
		Thread.sleep(5000);
		
		if (driver.getCurrentUrl().equals("http://localhost:8080/assignment/Map.jsp?patientid=8")) {
			return;
		} else {
			fail("Map page failed to load for an unknown reason");
		}
	}
	
	@Test
	public void removePatient() throws InterruptedException {
		WebDriverWait pageLoad = new WebDriverWait(driver, 30);
			
		try {
			pageLoad.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[contains(text(), 'Remove Patient')]")));
		} catch (Exception e) {
			fail("Cannot locate the link, page not loaded");
		}
		Thread.sleep(5000);
		driver.findElement(By.xpath("//a[contains(@href,'DeletePatient.jsp?patientid=8')]")).click();
		
		Thread.sleep(5000);
		try {
			driver.findElements(By.xpath("//*[contains(text(), 'jason johnston')]"));
		} catch (Exception e) {
			return;
		}
	}

}
