package com.eqms.tests.automated;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddSubjectTest.class, EditSubjectTest.class, AddTestTest.class, EditTestTest.class, 
				ExportTestTest.class, ImportTestTest.class, DeleteTestTest.class, DeleteSubjectTest.class })
public class EQMSTestSuite {}
