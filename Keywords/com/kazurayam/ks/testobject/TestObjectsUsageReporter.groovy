package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestSuiteContext

import internal.GlobalVariable

/**
 * compile a report with a list of TestObjects appearing in this project.
 * Each TestObject is associated with information which TestCase called it,
 * and if the TestObject is backed by files in the Object Repository,
 * or if it is dynamically instantiated.
 *
 * @param tracer
 * @param outputDir
 */
public class TestObjectsUsageReporter implements Reporter {

	private Associator associator
	private TestSuiteContext testSuiteContext
	private Path outputDir

	public static class Builder {
		// Required parameters
		private final Associator associator
		private final TestSuiteContext testSuiteContext

		// Optional parameters - initialized to default values
		private Path outputDir = Paths.get(RunConfiguration.getProjectDir()).resolve("build/tmp")

		public Builder(Associator associator, TestSuiteContext testSuiteContext) {
			this.associator = associator
			this.testSuiteContext = testSuiteContext
		}

		public Builder outputDir(Path outputDir) {
			this.outputDir = outputDir
			return this
		}

		public TestObjectsUsageReporter build() {
			return new TestObjectsUsageReporter(this)
		}
	}

	private TestObjectsUsageReporter(Builder builder) {
		associator = builder.associator
		testSuiteContext = builder.testSuiteContext
		outputDir = builder.outputDir
	}

	@Override
	Path getOutputDir() {
		return outputDir
	}

	@Override
	public void report() {
		AssociationTracer tracer = associator.getTracer()
		List<String> testObjectsAll = tracer.calleesAll()
		ObjectRepositoryWrapper repos = new ObjectRepositoryWrapper()
		StringBuilder sb = new StringBuilder()
		sb.append("# Test Objects Usage Report\n\n")
		sb.append("- TestSuite: ${testSuiteContext.getTestSuiteId()}\n\n")
		sb.append("## Test Objects in use\n\n")
		sb.append("| Test Object | in Object Repository? | used by Test Case |\n")
		sb.append("| ----------- | --------------------- | ----------------- |\n")
		testObjectsAll.each { testObject ->
			List<String> testCases = tracer.callersOf(testObject)
			Boolean inRepos = repos.includes(testObject)
			testCases.each { testCase ->
				sb.append("| `${testObject}` | ${inRepos} | `${testCase}` |")
				sb.append("\n")
			}
		}
		sb.append("\n\n")
		sb.append("## All Test Objects defined\n\n")
		sb.append("| Test Object | in Object Repository | # of refs |\n")
		sb.append("| ----------- | -------------------- | --------: |\n")

		//
		if (!Files.exists(outputDir)) {
			Files.createDirectories(outputDir)
		}
		Path output = outputDir.resolve("TestObjects_Usage_Report.md")
		output.toFile().text = sb.toString()
	}
}
