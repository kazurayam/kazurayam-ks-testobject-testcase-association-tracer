import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.Associator
import com.kazurayam.ks.testobject.Reporter
import com.kazurayam.ks.testobject.TestObjectsUsageReporter
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

class AssociatorDriver {

	private static final String OUTPUTDIR = "build/TestObjectsUsage"

	private Associator associator
	private Path outputDir = Paths.get(RunConfiguration.getProjectDir()).resolve(OUTPUTDIR)

	AssociatorDriver() {
		associator = new Associator()
	}

	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		associator.beforeTestSuite(testSuiteContext)
	}

	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		associator.beforeTestCase(testCaseContext)
	}

	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		Reporter reporter =
				new TestObjectsUsageReporter.Builder(associator, testSuiteContext)
				.outputDir(outputDir)
				.build()
		reporter.report()
		WebUI.comment("TestObject Usage Report was written into ${outputDir.toAbsolutePath()}")
	}
}