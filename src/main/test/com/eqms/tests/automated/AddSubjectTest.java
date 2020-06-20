package com.eqms.tests.automated;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddSubjectTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		
		/*System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\Adrian\\Documents\\chromedriver_win32\\chromedriver.exe");
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("window-size=1900,1040");
		
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new ChromeDriver(capabilities);*/
		
		baseUrl = "http://localhost:8080/";
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@Test
	public void testAddSubjectTest() throws Exception {
		
		driver.get(baseUrl + "EQMS/auth/login");
		Thread.sleep(1000);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("andrzej.kowalski@gmail.com");
		Thread.sleep(1000);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("andrzejkowalski1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("Subjects")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.id("addSubjectBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addSubjectBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Subject name text field is required.", driver.findElement(By.id("subjectNameModal-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.id("subjectNameModal")).clear();
		driver.findElement(By.id("subjectNameModal")).sendKeys("Testowy przedmiot");
		Thread.sleep(1000);
		driver.findElement(By.id("addSubjectBtnModal")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("input.form-control.input-sm")).clear();
		driver.findElement(By.cssSelector("input.form-control.input-sm")).sendKeys("Testowy przedmiot");
		Thread.sleep(1000);
		try {
			assertEquals("Testowy przedmiot", driver.findElement(By.cssSelector("td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		driver.findElement(By.id("addSubjectBtn")).click();		
		Thread.sleep(1000);
		driver.findElement(By.id("subjectNameModal")).clear();
		driver.findElement(By.id("subjectNameModal")).sendKeys("Testowy przedmiot");
		Thread.sleep(1000);
		driver.findElement(By.id("addSubjectBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("This subject already exists! Please enter another subject name.", driver.findElement(By.id("subjectNameModal-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.cssSelector("div.modal-footer > button.btn.btn-default")).click();
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
