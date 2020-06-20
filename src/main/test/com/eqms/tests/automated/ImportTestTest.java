package com.eqms.tests.automated;

import java.util.regex.Pattern;
import java.util.List;
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

import com.eqms.model.Answer;
import com.eqms.model.GroupOfQuestions;
import com.eqms.service.TestService;

@SuppressWarnings("unused")
@ContextConfiguration (locations = {
		"file:src/main/resources/config/spring-database.xml",
		"file:src/main/resources/config/spring-security.xml",
		"file:src/main/resources/config/applicationContext.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportTestTest {
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
	public void testImportTest() throws Exception {
		
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
	    driver.findElement(By.id("subjectNameModal")).clear();
	    driver.findElement(By.id("subjectNameModal")).sendKeys("Testowy przedmiot - import");
	    Thread.sleep(1000);
	    driver.findElement(By.id("addSubjectBtnModal")).click();
	    Thread.sleep(1000);
		
		driver.findElement(By.linkText("Import / Export")).click();
		Thread.sleep(1000);
		
		new Select(driver.findElement(By.id("subjectDropDownForImport"))).selectByVisibleText("Testowy przedmiot - import");
		Thread.sleep(1000);
		
		com.eqms.model.Test lastTest = getTestService().getAllTests(Order.desc("testId"), 1).get(0);
		Integer lastTestId = lastTest.getTestId();
		
		driver.findElement(By.id("inputTestFile")).clear();
		driver.findElement(By.id("inputTestFile")).sendKeys("D:\\eqms_output_directory\\tests\\test_" + lastTestId + ".tg");
		Thread.sleep(1000);
		
		driver.findElement(By.id("previewImportBtn")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector("div.modal-footer > button.btn.btn-default")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Test doesn't contains any errors.", driver.findElement(By.cssSelector("#importResultDivContent > h4")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		driver.findElement(By.id("submitImportForm")).click();
		Thread.sleep(5000);
		try {
			assertEquals("The test has been successfully added.", driver.findElement(By.cssSelector("#importResultDivContent > h4")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		driver.findElement(By.linkText("Tests")).click();
		Thread.sleep(1000);
		new Select(driver.findElement(By.id("subjectDropDown"))).selectByVisibleText("Testowy przedmiot - import");
		Thread.sleep(2000);
		try {
			assertEquals("Przyk³adowa nazwa testu - edytowana", driver.findElement(By.cssSelector("td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("1", driver.findElement(By.xpath("//table[@id='tests_table']/tbody/tr/td[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("1", driver.findElement(By.xpath("//table[@id='tests_table']/tbody/tr/td[3]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("65", driver.findElement(By.xpath("//table[@id='tests_table']/tbody/tr/td[4]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		driver.findElement(By.id("editTestBtn")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("collapseGroup")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("td.details-control")).click();
		Thread.sleep(3000);
		
		GroupOfQuestions lastGroup = getTestService().getAllGroups(Order.desc("groupId"), 1).get(0);
		Integer lastGroupId = lastGroup.getGroupId();
		
		/*List<Answer> lastTwoAnswers = getTestService().getAllAnswers(Order.desc("answerId"), 2);
		Integer lastAnswerId = lastTwoAnswers.get(0).getAnswerId();
		Integer lastAnswerIdBeforeLast = lastTwoAnswers.get(1).getAnswerId();
		
		System.out.println("Last group id = " + lastGroupId);
		System.out.println("Last answer id = " + lastAnswerId);
		System.out.println("Last answer id before last = " + lastAnswerIdBeforeLast);*/
		
		try {
			assertEquals("Przyk³adowa nazwa grupy", driver.findElement(By.id("groupName")).getText());
			System.out.println("driver.findElement(By.id(\"groupName\")).getText() = " + driver.findElement(By.id("groupName")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("1", driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > #numberOfQuestions")).getText());
			System.out.println("driver.findElement(By.cssSelector(\"#group_\" + lastGroupId + \" > div.col-sm-12 > #numberOfQuestions\")).getText() = " + driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > #numberOfQuestions")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Przyk³adowa zawartoœæ pytania", driver.findElement(By.cssSelector("td.sorting_1")).getText());
			System.out.println("driver.findElement(By.cssSelector(\"td.sorting_1\")).getText() = " + driver.findElement(By.cssSelector("td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("2", driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[2]")).getText());
			System.out.println("driver.findElement(By.xpath(\"table[@id='question_table_\" + lastGroupId + \"']/tbody/tr/td[2]\")).getText() = " + driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("1", driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[3]")).getText());
			System.out.println("driver.findElement(By.xpath(\"table[@id='question_table_\" + lastGroupId + \"']/tbody/tr/td[3]\")).getText() = " + driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[3]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		/*try {
			assertEquals("Druga odpowiedŸ", driver.findElement(By.cssSelector("#answer_" + lastAnswerId + " > td.sorting_1")).getText());
			System.out.println("driver.findElement(By.cssSelector(\"#answer_\" + lastAnswerId + \" > td.sorting_1\")).getText() = " + driver.findElement(By.cssSelector("#answer_" + lastAnswerId + " > td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("false", driver.findElement(By.xpath("//tr[@id='answer_" + lastAnswerId + "']/td[2]")).getText());
			System.out.println("driver.findElement(By.xpath(\"tr[@id='answer_\" + lastAnswerId + \"']/td[2]\")).getText() = " + driver.findElement(By.xpath("//tr[@id='answer_" + lastAnswerId + "']/td[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Pierwsza odpowiedŸ", driver.findElement(By.cssSelector("#answer_" + lastAnswerIdBeforeLast + " > td.sorting_1")).getText());
			System.out.println("driver.findElement(By.cssSelector(\"#answer_\" + lastAnswerIdBeforeLast + \" > td.sorting_1\")).getText() = " + driver.findElement(By.cssSelector("#answer_" + lastAnswerIdBeforeLast + " > td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("true", driver.findElement(By.xpath("//tr[@id='answer_" + lastAnswerIdBeforeLast + "']/td[2]")).getText());
			System.out.println("driver.findElement(By.xpath(\"tr[@id='answer_\" + lastAnswerIdBeforeLast + \"']/td[2]\")).getText() = " + driver.findElement(By.xpath("//tr[@id='answer_" + lastAnswerIdBeforeLast + "']/td[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}*/
		Thread.sleep(3000);
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
