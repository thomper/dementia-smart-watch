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

public class alertDisplayTests {

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
	public void checkLostAlertDisplay() throws InterruptedException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			st.executeUpdate("INSERT INTO patientalerts (patientID, alertTime, alertDate, alertLat, alertLong) VALUES (6, 0, 0, 0.0, 0.0)");
			st.executeUpdate("UPDATE patients SET status = 'LOST' WHERE patientID = 6");
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		
		st.close();
		conn.close();
		Thread.sleep(7000);

		if (driver.getPageSource().contains("is lost and may need assistance")) {
			return;
		} else {
			fail("Cannot locate the alert, ajax not functioning");
		}
	}
	
	@Test
	public void checkCollapseAlertDisplay() throws InterruptedException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			st.executeUpdate("INSERT INTO patientcollapses (patientID, collapseTime, collapseDate, collapseLat, collapseLong) VALUES (6, 0, 0, 0.0, 0.0)");
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		
		st.close();
		conn.close();
		Thread.sleep(7000);

		if (driver.getPageSource().contains("has collapsed and may need assistance")) {
			return;
		} else {
			fail("Cannot locate the alert, ajax not functioning");
		}
	}
	
	@Test
	public void checkBatteryAlertDisplay() throws InterruptedException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			st.executeUpdate("INSERT INTO patientbatteryalerts (patientID, alertTime, alertDate, batterylevel) VALUES (6, 0, 0, '15%')");
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		
		st.close();
		conn.close();
		Thread.sleep(7000);

		if (driver.getPageSource().contains("devices battery is low and needs charging, current level:")) {
			return;
		} else {
			fail("Cannot locate the alert, ajax not functioning");
		}
	}
	
	@Test
	public void checkMultipleAlertDisplay() throws InterruptedException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			st.executeUpdate("INSERT INTO patientbatteryalerts (patientID, alertTime, alertDate, batterylevel) VALUES (6, 0, 0, '15%')");
			st.executeUpdate("INSERT INTO patientalerts (patientID, alertTime, alertDate, alertLat, alertLong) VALUES (8, 0, 0, 0.0, 0.0)");
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		
		st.close();
		conn.close();
		Thread.sleep(7000);

		System.out.println(driver.getPageSource());
		
		if (driver.getPageSource().contains("Joshua Johnston</a> devices battery is low and needs charging, current level:") && driver.getPageSource().contains("jason johnston</a> has pressed the panic button and may need assistance.")) {
			return;
		} else {
			fail("Cannot locate the alert, ajax not functioning");
		}
	}
	
	@Test
	public void checkMultipleAlertSamePatientDisplay() throws InterruptedException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			st.executeUpdate("INSERT INTO patientbatteryalerts (patientID, alertTime, alertDate, batterylevel) VALUES (6, 0, 0, '15%')");
			st.executeUpdate("INSERT INTO patientalerts (patientID, alertTime, alertDate, alertLat, alertLong) VALUES (8, 0, 0, 0.0, 0.0)");
			st.executeUpdate("INSERT INTO patientbatteryalerts (patientID, alertTime, alertDate, batterylevel) VALUES (8, 0, 0, '15%')");
			st.executeUpdate("INSERT INTO patientalerts (patientID, alertTime, alertDate, alertLat, alertLong) VALUES (6, 0, 0, 0.0, 0.0)");
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		
		st.close();
		conn.close();
		Thread.sleep(7000);

		System.out.println(driver.getPageSource());
		
		if (driver.getPageSource().contains("Joshua Johnston</a> devices battery is low and needs charging, current level:") && driver.getPageSource().contains("jason johnston</a> has pressed the panic button and may need assistance.") && driver.getPageSource().contains("jason johnston</a> devices battery is low and needs charging, current level:") && driver.getPageSource().contains("Joshua Johnston</a> has pressed the panic button and may need assistance.")) {
			return;
		} else {
			fail("Cannot locate the alert, ajax not functioning");
		}
	}
	
	@Test
	public void checkAlertDismiss() throws InterruptedException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			st.executeUpdate("INSERT INTO patientalerts (patientID, alertTime, alertDate, alertLat, alertLong) VALUES (6, 0, 0, 0.0, 0.0)");
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		
		Thread.sleep(7000);
		driver.getPageSource();
		
		driver.findElement(By.xpath("//a[contains(@href, 'javascript:changeStatus(6)')]")).click();
		Thread.sleep(3000);
	}
	
	@Test
	public void checkPanicAlertDisplay() throws InterruptedException, ClassNotFoundException, SQLException {	
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db", "agile374", "dementia374");
			st = conn.createStatement();
			st.executeUpdate("INSERT INTO patientalerts (patientID, alertTime, alertDate, alertLat, alertLong) VALUES (6, 0, 0, 0.0, 0.0)");
		} catch (Exception e) {
			System.out.println("Something has gone horribly wrong");
		}
		
		st.close();
		conn.close();
		Thread.sleep(7000);

		if (driver.getPageSource().contains("has pressed the panic button and may need assistance")) {
			return;
		} else {
			fail("Cannot locate the alert, ajax not functioning");
		}
	}

}
