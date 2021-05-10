package com.kazurayam.ks.testobject

import java.nio.file.Paths
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

import internal.GlobalVariable

public class Associator {

	public static final String GLOBALVARIABLE_CURRENT_TESTSUITE_ID = "CURRENT_TESTSUITE_ID__"
	public static final String GLOBALVARIABLE_CURRENT_TESTSUITE_TIMESTAMP = "CURRENT_TESTSUITE_TIMESTAMP__"
	public static final String GLOBALVARIABLE_CURRENT_TESTCASE_ID = "CURRENT_TESTCASE_ID__"

	private Boolean hasModifiedKatalonClasses = false

	public Associator() {}

	/**
	 * 
	 * @param testSuiteContext
	 */
	public void beforeTestSuite(TestSuiteContext testSuiteContext) {
		//println "Associator::beforeTestSuite() was called by ${testSuiteContext.getTestSuiteId()}"
		ExpandoGlobalVariable.addGlobalVariable(
				GLOBALVARIABLE_CURRENT_TESTSUITE_ID, testSuiteContext.getTestSuiteId())
		ExpandoGlobalVariable.addGlobalVariable(
				GLOBALVARIABLE_CURRENT_TESTSUITE_TIMESTAMP, this.getTestSuiteTimestamp())
		// cast a spell!
		this.hasModifiedKatalonClasses = modifyKatalonClasses()
	}

	/**
	 * @return Local time stamp when the Test Suite started, such as "2021-12-03T10:15:30"
	 */
	String getTestSuiteTimestamp() throws DateTimeParseException {
		String reportFolder = RunConfiguration.getReportFolder()
		if (reportFolder != null) {
			String katalonTimestamp = Paths.get(reportFolder).getFileName().toString()
			DateTimeFormatter inputDtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
			DateTimeFormatter outputDtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME
			return outputDtf.format(inputDtf.parse(katalonTimestamp))
		} else {
			return "N/A"
		}
	}

	/**
	 * 
	 * @param testCaseContext
	 */
	public void beforeTestCase(TestCaseContext testCaseContext) {
		//println "Associator::beforeTestCase() was called by ${testCaseContext.getTestCaseId()}"
		ExpandoGlobalVariable.addGlobalVariable(
				GLOBALVARIABLE_CURRENT_TESTCASE_ID, testCaseContext.getTestCaseId())
		if (! hasModifiedKatalonClasses) {
			// TestClass was executed immediately without wrapping TestSuite
			// so cast a spell now!
			this.hasModifiedKatalonClasses = modifyKatalonClasses()
		}
	}

	public static AssociationTracer getTracer() {
		return AssociationTracer.getInstance()
	}

	/**
	 * modify methods of Katalon-builtin classes
	 * - com.kms.katalon.core.testobject.ObjectRepository::findTestObject(String testObjectRelativeId)
	 * - com.kms.katalon.core.testobject.TestObject::constructor
	 * 
	 * so that these methods notify the AssociationTracer of the pair of TestCaseID and TestObjectID
	 * when invoked; effectively the AssociationTracer can record which TestCase used which TestObject.
	 */
	/**
	 * The source code of Katalon API is here:
	 * - TestObject https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/testobject/TestObject.java
	 * - ObjectRepository https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/testobject/ObjectRepository.java
	 * 
	 * Learned the Groovy Metaprogramming at
	 * - https://stackoverflow.com/questions/5907432/groovy-adding-code-to-a-constructor
	 * - https://www.baeldung.com/groovy-metaprogramming
	 */
	Boolean modifyKatalonClasses() {
		AssociationTracer tracer = AssociationTracer.getInstance()
		//
		ObjectRepository.metaClass.'static'.invokeMethod = { String methodName, args ->
			if (methodName == "findTestObject") {
				String testObjectId = ObjectRepository.getTestObjectId(args[0]).replaceAll("Object Repository/", "")
				// notify the AssociationTracer singleton instance
				// of the pair of (TestCaseId, TestObjectId)
				tracer.trace(GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASE_ID], testObjectId)
			}
			return delegate.metaClass.getMetaMethod(methodName, args).invoke(delegate, args)
		}
		//
		TestObject.metaClass.constructor = { String testObjectId ->
			// notify the AssociationTracer singleton instance
			// of the pair of (TestCaseId, TestObjectId)
			tracer.trace(GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASE_ID],
					testObjectId.replaceAll("Object Repository/", ''))
			// use reflection to get the original constructor
			def constructor = TestObject.class.getConstructor(String.class)
			// create the new instance and return it just as the original constructor does
			return constructor.newInstance(testObjectId)
		}
		return true
	}
}
