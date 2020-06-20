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

import com.eqms.model.Answer;
import com.eqms.model.GroupOfQuestions;
import com.eqms.model.Question;
import com.eqms.service.TestService;

@SuppressWarnings("unused")
@ContextConfiguration (locations = {
		"file:src/main/resources/config/spring-database.xml",
		"file:src/main/resources/config/spring-security.xml",
		"file:src/main/resources/config/applicationContext.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class EditTestTest {
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
	public void testEditTest() throws Exception {
		
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
		driver.findElement(By.id("editTestBtn")).click();
		Thread.sleep(1000);
		
		/**
		 * Edit test
		 */
		driver.findElement(By.id("editTestBtn")).click();
		driver.findElement(By.id("testNameModal")).clear();
		driver.findElement(By.id("testNameModal")).sendKeys("Przyk³adowa nazwa testu - edytowana");
		Thread.sleep(1000);
		driver.findElement(By.id("timeForTestModal")).clear();
		driver.findElement(By.id("timeForTestModal")).sendKeys("65");
		Thread.sleep(1000);
		driver.findElement(By.id("editTestBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Przyk³adowa nazwa testu - edytowana", driver.findElement(By.id("testName")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("65 min", driver.findElement(By.id("timeForTest")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		/**
		 * Add group
		 */
		driver.findElement(By.id("addGroupBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addGroupBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Group name text field is required.", driver.findElement(By.id("groupNameModal-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.id("groupNameModal")).clear();
		driver.findElement(By.id("groupNameModal")).sendKeys("Druga grupa");
		Thread.sleep(1000);
		driver.findElement(By.id("addGroupBtnModal")).click();
		Thread.sleep(2000);
		
		/**
		 * We have to get information about last group from database, because groupId is used in HTML code.
		 */
		GroupOfQuestions lastGroup = getTestService().getAllGroups(Order.desc("groupId"), 1).get(0);
		Integer lastGroupId = lastGroup.getGroupId();
		System.out.println("Id of last group = " + lastGroupId);
		
		driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > div.col-md-5 > #collapseGroup")).click();
		Thread.sleep(2000);
		try {
			assertEquals("2", driver.findElement(By.id("numberOfGroups")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("1", driver.findElement(By.id("numberOfQuestions")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Druga grupa", driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > #groupName")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("0", driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > #numberOfQuestions")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("There is no questions.", driver.findElement(By.cssSelector("#collapseGroup_" + lastGroupId + " > div.col-sm-12")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		/**
		 * Edit group
		 */
		driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > div.col-md-5 > #editGroupBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#editGroupFormModal > div.form-group > #groupNameModal")).clear();
		driver.findElement(By.cssSelector("#editGroupFormModal > div.form-group > #groupNameModal")).sendKeys("Druga grupa - edytowana");
		Thread.sleep(1000);
		driver.findElement(By.id("editGroupBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Druga grupa - edytowana", driver.findElement(By.cssSelector("#group_" + lastGroupId +" > div.col-sm-12 > #groupName")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		/**
		 * Add question
		 */
		driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > div.col-md-5 > #addQuestionBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addQuestionBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Content of question textarea is required.", driver.findElement(By.id("contentOfQuestionModal-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.id("contentOfQuestionModal")).clear();
		driver.findElement(By.id("contentOfQuestionModal")).sendKeys("Drugie pytanie");
		Thread.sleep(1000);
		driver.findElement(By.id("inputImageForQuestionModal")).clear();
		driver.findElement(By.id("inputImageForQuestionModal")).sendKeys("D:\\eqms_automated_tests_directory\\4.jpg");
		Thread.sleep(1000);
		driver.findElement(By.id("addQuestionBtnModal")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("td.details-control")).click();
		Thread.sleep(2000);
		try {
			assertEquals("2", driver.findElement(By.id("numberOfQuestions")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("1", driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > #numberOfQuestions")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Drugie pytanie", driver.findElement(By.cssSelector("td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("0", driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("0", driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[3]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("There is no answers.", driver.findElement(By.cssSelector("div.slider > div.well")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		/**
		 * Edit question
		 */
		driver.findElement(By.id("editQuestionBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#editQuestionFormModal > div.form-group > #contentOfQuestionModal")).clear();
		driver.findElement(By.cssSelector("#editQuestionFormModal > div.form-group > #contentOfQuestionModal")).sendKeys("Drugie pytanie - edytowane");
		Thread.sleep(1000);
		driver.findElement(By.id("editQuestionBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Drugie pytanie - edytowane", driver.findElement(By.cssSelector("td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		/**
		 * Add answer
		 */
		driver.findElement(By.id("addAnswerBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("addAnswerBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Content of answer textarea is required.", driver.findElement(By.id("contentOfAnswerModal-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Please choose true or false to current answer.", driver.findElement(By.id("whetherCorrect-error")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.id("contentOfAnswerModal")).clear();
		driver.findElement(By.id("contentOfAnswerModal")).sendKeys("Druga odpowiedŸ");
		Thread.sleep(1000);
		driver.findElement(By.id("whetherCorrect")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("inputImageForAnswerModal")).clear();
		driver.findElement(By.id("inputImageForAnswerModal")).sendKeys("D:\\eqms_automated_tests_directory\\5.png");
		Thread.sleep(1000);
		driver.findElement(By.id("addAnswerBtnModal")).click();
		Thread.sleep(2000);
		
		/**
		 * We have to get information about last question from database, because questionId is used in HTML code.
		 */
		Question lastQuestion = getTestService().getAllQuestions(Order.desc("questionId"), 1).get(0);
		Integer lastQuestionId = lastQuestion.getQuestionId();
		System.out.println("Id of last question = " + lastQuestionId);
		
		/**
		 * We have to get information about last answer from database, because answerId is used in HTML code.
		 */
		Answer lastAnswer = getTestService().getAllAnswers(Order.desc("answerId"), 1).get(0);
		Integer lastAnswerId = lastAnswer.getAnswerId();
		System.out.println("Id of last answer = " + lastAnswerId);

		try {
			assertEquals("1", driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("1", driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[3]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("Druga odpowiedŸ", driver.findElement(By.cssSelector("#answer_" + lastAnswerId + " > td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("true", driver.findElement(By.xpath("//tr[@id='answer_" + lastAnswerId + "']/td[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		/**
		 * Edit answer
		 */
		driver.findElement(By.id("editAnswerBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#editAnswerFormModal > div.form-group > #contentOfAnswerModal")).clear();
		driver.findElement(By.cssSelector("#editAnswerFormModal > div.form-group > #contentOfAnswerModal")).sendKeys("Druga odpowiedŸ - edytowana");
		Thread.sleep(1000);
		driver.findElement(By.id("editAnswerBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("Druga odpowiedŸ - edytowana", driver.findElement(By.cssSelector("#answer_" + lastAnswerId + " > td.sorting_1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		/**
		 * Delete answer
		 */
		driver.findElement(By.id("deleteAnswerBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmDeleteAnswerBtnModal")).click();
		Thread.sleep(2000);
		try {
			assertEquals("0", driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("0", driver.findElement(By.xpath("//table[@id='question_table_" + lastGroupId + "']/tbody/tr/td[3]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("No data available in table", driver.findElement(By.cssSelector("td.dataTables_empty")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		/**
		 * Delete question
		 */
		driver.findElement(By.id("deleteQuestionBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmDeleteQuestionBtnModal")).click();
		Thread.sleep(2000);
		try {
			assertEquals("1", driver.findElement(By.id("numberOfQuestions")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("0", driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > #numberOfQuestions")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals("No data available in table", driver.findElement(By.cssSelector("td.dataTables_empty")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		
		/**
		 * Delete group
		 */
		driver.findElement(By.cssSelector("#group_" + lastGroupId + " > div.col-sm-12 > div.col-md-5 > #deleteGroupBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmDeleteGroupBtnModal")).click();
		Thread.sleep(1000);
		try {
			assertEquals("1", driver.findElement(By.id("numberOfGroups")).getText());
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
