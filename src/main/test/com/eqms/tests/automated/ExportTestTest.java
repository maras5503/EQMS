package com.eqms.tests.automated;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.hibernate.criterion.Order;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eqms.model.GroupOfQuestions;
import com.eqms.service.TestService;

@SuppressWarnings("unused")
@ContextConfiguration (locations = {
		"file:src/main/resources/config/spring-database.xml",
		"file:src/main/resources/config/spring-security.xml",
		"file:src/main/resources/config/applicationContext.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class ExportTestTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Autowired
	private TestService testService;

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:8080/";
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@Test
	public void testExportTest() throws Exception {
		
		driver.get(baseUrl + "EQMS/auth/login");
		Thread.sleep(1000);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("andrzej.kowalski@gmail.com");
		Thread.sleep(1000);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("andrzejkowalski1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("Import / Export")).click();
		Thread.sleep(1000);
		
		GroupOfQuestions lastGroup = getTestService().getAllGroups(Order.desc("groupId"), 1).get(0);
		Integer lastGroupId = lastGroup.getGroupId();
		
		new Select(driver.findElement(By.id("subjectDropDown"))).selectByVisibleText("Testowy przedmiot - edytowany");
		Thread.sleep(1000);
		new Select(driver.findElement(By.id("testDropDown"))).selectByVisibleText("Przyk³adowa nazwa testu - edytowana");
		Thread.sleep(1000);
		driver.findElement(By.id("numberOfQuestions_group_" + lastGroupId + "")).clear();
		driver.findElement(By.id("numberOfQuestions_group_" + lastGroupId + "")).sendKeys("1");
		Thread.sleep(1000);
		driver.findElement(By.id("submitExportForm")).click();
		Thread.sleep(1000);
		
		try {
			assertEquals("The file has been successfully created.", driver.findElement(By.cssSelector("h4")).getText());
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
	
	public TestService getTestService() {
		return testService;
	}

	public void setTestService(TestService testService) {
		this.testService = testService;
	}
}
