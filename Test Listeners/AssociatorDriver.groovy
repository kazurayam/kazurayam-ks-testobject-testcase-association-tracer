import com.kazurayam.ks.testobject.Associator
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

class AssociatorDriver {
	
	private Associator associator
	
	AssociatorDriver() {
		associator = new Associator()
		associator.setOutputDir("./build/testcase_testobject_association")
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
		associator.afterTestSuite(testSuiteContext)
	}
}