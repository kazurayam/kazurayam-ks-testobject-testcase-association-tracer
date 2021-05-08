package com.kazurayam.ks.testobject

import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.configuration.RunConfiguration
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import internal.GlobalVariable
import java.util.stream.Collectors

public class Associator {

	public static final String GLOBALVARIABLE_CURRENT_TESTSUITEID = "CURRENT_TESTSUITEID__"
	public static final String GLOBALVARIABLE_CURRENT_TESTCASEID  = "CURRENT_TESTCASEID__"

	public static final String OUTPUT_DIR_DEFAULT = "build/TestCase_TestObject_Association"
	public static final String REPORT_NAME = "TestObjects_usage_report.md"

	private Boolean hasModifiedTestObjectClass = false

	private Path outputDir

	public Associator() {
		//
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		outputDir = projectDir.resolve(OUTPUT_DIR_DEFAULT)
		if (!Files.exists(outputDir)) {
			Files.createDirectories(outputDir)
		}
	}

	public void setOutputDir(String dir) {
		Objects.requireNonNull(dir)
		outputDir = Paths.get(dir)
		if (!Files.exists(outputDir)) {
			Files.createDirectories(outputDir)
		}
	}

	/**
	 * 
	 * @param testSuiteContext
	 */
	public void beforeTestSuite(TestSuiteContext testSuiteContext) {
		//println "Associator::beforeTestSuite() was called by ${testSuiteContext.getTestSuiteId()}"
		ExpandoGlobalVariable.addGlobalVariable(
				GLOBALVARIABLE_CURRENT_TESTSUITEID, testSuiteContext.getTestSuiteId())
		this.hasModifiedTestObjectClass = modifyTestObjectClass()
	}

	/**
	 * 
	 * @param testCaseContext
	 */
	public void beforeTestCase(TestCaseContext testCaseContext) {
		//println "Associator::beforeTestCase() was called by ${testCaseContext.getTestCaseId()}"
		ExpandoGlobalVariable.addGlobalVariable(
				GLOBALVARIABLE_CURRENT_TESTCASEID, testCaseContext.getTestCaseId())
		if (! hasModifiedTestObjectClass) {
			// TestClass was executed immediately without wrapping TestSuite
			this.hasModifiedTestObjectClass = modifyTestObjectClass()
		}
	}

	/**
	 * 
	 * @param testSuiteContext
	 */
	public void afterTestSuite(TestSuiteContext testSuiteContext) {
		//println "Associator::afterTestSuite() was called by ${testSuiteContext.getTestSuiteId()}"
		AssociationTracer tracer = AssociationTracer.getInstance()
		report(outputDir)
	}

	/**
	 * modify com.kms.katalon.core.testobject.TestObject class 
	 * so that its constructor notifies the AssociationTracer of 
	 * the pair of TestCaseID and TestObjectID; effectively
	 * the AssociationTracer can record which TestCase used which TestObject.
	 * 
	 * @param tracer
	 */
	/* Learned
	 * https://stackoverflow.com/questions/5907432/groovy-adding-code-to-a-constructor
	 */
	Boolean modifyTestObjectClass() {
		AssociationTracer tracer = AssociationTracer.getInstance()
		TestObject.metaClass.constructor = { String objectId ->
			println "[Associator::modifyTestObjectClass] Caller \'${GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASEID]}\' executed \'new TestObject(\"${objectId}\")\'"

			// notify the AssociationTracer singleton instance
			// of the pair of (TestCaseId, TestObjectId)
			tracer.trace(GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASEID], objectId)

			// use reflection to get the original constructor
			def constructor = TestObject.class.getConstructor(String.class)
			// create the new instance and return it just as the original constructor does
			return constructor.newInstance(objectId)
		}
		return true
	}

	/**
	 * compile a report with a list of TestObjects appearing in this project.
	 * Each TestObject is associated with information which TestCase called it,
	 * and if the TestObject is backed by files in the Object Repository,
	 * or if it is dynamically instantiated.
	 * 
	 * @param tracer
	 * @param outputDir
	 */
	private void report(Path outputDir) {
		AssociationTracer tracer = AssociationTracer.getInstance()
		List<String> testObjectsAll = tracer.calleesAll()
		ObjectRepositoryWrapper repos = new ObjectRepositoryWrapper()
		StringBuilder sb = new StringBuilder()
		sb.append("# TestObjects Usage Report\n\n")
		sb.append("- TestSuite: ${GlobalVariable[GLOBALVARIABLE_CURRENT_TESTSUITEID]}\n\n")
		sb.append("| TestObject is | in Object Repository? | used by TestCase |\n")
		sb.append("| ------------- | --------------------- | ---------------- |\n")
		testObjectsAll.each { testObject ->
			List<String> testCases = tracer.callersOf(testObject)
			Boolean inRepos = repos.contains(testObject)
			testCases.each { testCase ->
				sb.append("| ${testObject} | ${inRepos} | ${testCase} |")
				sb.append("\n")
			}
		}
		sb.append("\n")
		print sb.toString()
	}

}
