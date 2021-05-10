import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.Associator
import com.kazurayam.ks.testobject.Reporter
import com.kazurayam.ks.testobject.TestObjectUsageReporter
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

class AssociatorDriver {

	private static final String OUTPUTDIR = "build/reports"

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
		Reporter summary = new TestObjectUsageReporter.Builder(associator, testSuiteContext)
							// .composition(["UNUSED", "COUNT"])
							.outputDir(outputDir)
							.outputFilename('testobject_usage_summary.md')
							.build()
		summary.write()
		
		Reporter full = new TestObjectUsageReporter.Builder(associator, testSuiteContext)
							.composition(["UNUSED", "COUNT", "REVERSE", "FORWARD"])
							.outputDir(outputDir)
							.outputFilename('testobject_usage_full.md')
							.build()
		full.write()
		
		WebUI.comment("TestObject Usage Report was written into ${outputDir.toAbsolutePath()}")
	}
}