package com.kazurayam.ks.testobject

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.context.TestCaseContext

@RunWith(JUnit4.class)
public class TestObjectUsageReporterTest {

	private Associator associator
	private TestSuiteContext testSuiteContext
	private TestCaseContext testCaseContext
	private Path outputDir
	private TestObjectUsageReporter reporter

	@Before
	void setup() {
		associator = new Associator()
		testSuiteContext = new MockTestSuiteContext("Test Suites/" + this.class.getName())
		associator.beforeTestSuite(testSuiteContext)
		testCaseContext = new MockTestCaseContext("Test Cases/" + this.class.getName())
		outputDir = Paths.get(RunConfiguration.getProjectDir()).resolve("build/tmp")
		Files.createDirectories(outputDir)
		reporter = new TestObjectUsageReporter.Builder(associator, testSuiteContext).outputDir(outputDir)
				.build()
	}

	@Test
	void test_title_top() {
		String content = reporter.report()
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_TOP))
	}

	@Test
	void test_testSuite_emitted() {
		String content = reporter.report()
		assertTrue(content.contains('- Test Suite:'))
	}

	@Test
	void test_UNUSED_emitted() {
		reporter = new TestObjectUsageReporter.Builder(associator, testSuiteContext).outputDir(outputDir)
				.composition(["UNUSED"])
				.build()
		String content = reporter.report()
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_UNUSED))
	}

	@Test
	void test_COUNT_emitted() {
		reporter = new TestObjectUsageReporter.Builder(associator, testSuiteContext).outputDir(outputDir)
				.composition(["COUNT"])
				.build()
		String content = reporter.report()
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_COUNT))
	}

	@Test
	void test_REVERSE_emitted() {
		reporter = new TestObjectUsageReporter.Builder(associator, testSuiteContext).outputDir(outputDir)
				.composition(["REVERSE"])
				.build()
		String content = reporter.report()
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_REVERSE))
	}

	@Test
	void test_FORWARD_emitted() {
		reporter = new TestObjectUsageReporter.Builder(associator, testSuiteContext).outputDir(outputDir)
				.composition(["FORWARD"])
				.build()
		String content = reporter.report()
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_FORWARD))
	}

	@Test
	void test_4types_emitted() {
		reporter = new TestObjectUsageReporter.Builder(associator, testSuiteContext).outputDir(outputDir)
				.composition([
					"UNUSED",
					"COUNT",
					"REVERSE",
					"FORWARD"
				])
				.build()
		String content = reporter.report()
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_UNUSED))
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_COUNT))
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_REVERSE))
		assertTrue(content.contains(TestObjectUsageReporter.TITLE_FORWARD))
		reporter.write()
	}

	
	
	
	//-------------------------------------------------

	/**
	 * Mock implementation of TestSuiteContext for test
	 */
	class MockTestSuiteContext implements TestSuiteContext {
		private String id
		MockTestSuiteContext(String id) {
			this.id = id
		}
		@Override
		String getStatus() {
			throw new UnsupportedOperationException("i am a mock")
		}
		@Override
		String getTestSuiteId() {
			return id
		}
	}

	/**
	 * Mock implementation of TestCaseContett for test
	 */
	class MockTestCaseContext implements TestCaseContext {
		private String id
		MockTestCaseContext(String id) {
			this.id = id
		}
		@Override
		String getMessage() {
			throw new UnsupportedOperationException("i am a mock")
		}
		@Override
		String getTestCaseId() {
			return id
		}
		@Override
		String getTestCaseStatus() {
			throw new UnsupportedOperationException("i am a mock")
		}
		@Override
		Map<String, Object> getTestCaseVariables() {
			throw new UnsupportedOperationException("i am a mock")
		}
		@Override
		boolean isSkipped() {
			throw new UnsupportedOperationException("i am a mock")
		}
		@Override
		void skipThisTestCase() {
			throw new UnsupportedOperationException("i am a mock")
		}
	}
}
