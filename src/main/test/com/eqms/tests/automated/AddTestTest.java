package com.eqms.tests.automated;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

@SuppressWarnings("unused")
public class AddTestTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:8080/";
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAddTestTest() throws Exception {
		
		driver.get(baseUrl + "EQMS/auth/login");
		Thread.sleep(1000);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("andrzej.kowalski@gmail.com");
		Thread.sleep(1000);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("andrzejkowalski1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("Tests")).click();
		Thread.sleep(1000);
		
		new Select(driver.findElement(By.id("subjectDropDown"))).selectByVisibleText("Testowy przedmiot - edytowany");
		try {
			assertEquals("There is no tests.", driver.findElement(By.cssSelector("div.well")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		Thread.sleep(1000);
		
		driver.findElement(By.id("addTestBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addNewGroup")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addNewQuestion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addNewAnswer")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addNewAnswer")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Test name is required.", driver.findElement(By.id("testName-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Time for test is required.", driver.findElement(By.id("timeForTest-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Mark 2 is required.", driver.findElement(By.id("mark2-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Group name is required.", driver.findElement(By.id("groupName_1-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Content of question is required.", driver.findElement(By.id("contentOfQuestion_1-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Content of answer is required.", driver.findElement(By.id("contentOfAnswer_1-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("This select is required.", driver.findElement(By.id("whetherCorrectSelect_1-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Content of answer is required.", driver.findElement(By.id("contentOfAnswer_2-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("This select is required.", driver.findElement(By.id("whetherCorrectSelect_2-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.id("testName")).clear();
		driver.findElement(By.id("testName")).sendKeys("Przyk≥adowa nazwa testu");
		Thread.sleep(1000);
		driver.findElement(By.id("timeForTest")).clear();
		driver.findElement(By.id("timeForTest")).sendKeys("60");
		Thread.sleep(1000);
		driver.findElement(By.id("mark2")).clear();
		driver.findElement(By.id("mark2")).sendKeys("20");
		Thread.sleep(1000);
		driver.findElement(By.id("mark3")).clear();
		driver.findElement(By.id("mark3")).sendKeys("40");
		Thread.sleep(1000);
		driver.findElement(By.id("mark3_5")).clear();
		driver.findElement(By.id("mark3_5")).sendKeys("60");
		Thread.sleep(1000);
		driver.findElement(By.id("mark4")).clear();
		driver.findElement(By.id("mark4")).sendKeys("80");
		Thread.sleep(1000);
		driver.findElement(By.id("mark4_5")).clear();
		driver.findElement(By.id("mark4_5")).sendKeys("90");
		Thread.sleep(1000);
		driver.findElement(By.id("mark5")).clear();
		driver.findElement(By.id("mark5")).sendKeys("98");
		Thread.sleep(1000);
		driver.findElement(By.id("groupName_1")).clear();
		driver.findElement(By.id("groupName_1")).sendKeys("Przyk≥adowa nazwa grupy");
		Thread.sleep(1000);
		driver.findElement(By.id("contentOfQuestion_1")).clear();
		driver.findElement(By.id("contentOfQuestion_1")).sendKeys("Przyk≥adowa zawartoúÊ pytania");
		Thread.sleep(1000);
		driver.findElement(By.id("inputImageForQuestion_1")).clear();
		driver.findElement(By.id("inputImageForQuestion_1")).sendKeys("D:\\eqms_automated_tests_directory\\1.jpg");
		Thread.sleep(1000);
		driver.findElement(By.id("contentOfAnswer_1")).clear();
		driver.findElement(By.id("contentOfAnswer_1")).sendKeys("Pierwsza odpowiedü");
		Thread.sleep(1000);
		driver.findElement(By.id("whetherCorrectSelect_1")).click();
		new Select(driver.findElement(By.id("whetherCorrectSelect_1"))).selectByVisibleText("true");
		Thread.sleep(1000);
		driver.findElement(By.id("inputImageForAnswer_1")).clear();
		driver.findElement(By.id("inputImageForAnswer_1")).sendKeys("D:\\eqms_automated_tests_directory\\2.jpg");
		Thread.sleep(1000);
		driver.findElement(By.id("contentOfAnswer_2")).clear();
		driver.findElement(By.id("contentOfAnswer_2")).sendKeys("Druga odpowiedü");
		Thread.sleep(1000);
		driver.findElement(By.id("whetherCorrectSelect_2")).click();
		new Select(driver.findElement(By.id("whetherCorrectSelect_2"))).selectByVisibleText("false");
		driver.findElement(By.id("inputImageForAnswer_2")).clear();
		driver.findElement(By.id("inputImageForAnswer_2")).sendKeys("D:\\eqms_automated_tests_directory\\3.jpg");
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();
		
		Thread.sleep(1000);
		new Select(driver.findElement(By.id("subjectDropDown"))).selectByVisibleText("Testowy przedmiot - edytowany");
		try {
			assertEquals("Przyk≥adowa nazwa testu", driver.findElement(By.cssSelector("td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.cssSelector("button.btn.btn-default")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
