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
public class ReverseTestObjectLookupReporter implements Reporter {

	public static final String OUTPUT_FILENAME = "reverse_testobject_lookup.md"

	private Associator associator
	private TestSuiteContext testSuiteContext
	private Path outputDir

	public static class Builder {

		public static final OUTPUTDIR_DEFAULT = "build/reports"

		// Required parameters
		private final Associator associator
		private final TestSuiteContext testSuiteContext

		// Optional parameters - initialized to default values
		private Path outputDir = Paths.get(RunConfiguration.getProjectDir()).resolve(OUTPUTDIR_DEFAULT)

		public Builder(Associator associator, TestSuiteContext testSuiteContext) {
			this.associator = associator
			this.testSuiteContext = testSuiteContext
		}

		public Builder outputDir(Path outputDir) {
			this.outputDir = outputDir
			return this
		}

		public ReverseTestObjectLookupReporter build() {
			return new ReverseTestObjectLookupReporter(this)
		}
	}

	private ReverseTestObjectLookupReporter(Builder builder) {
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
		ObjectRepositoryWrapper repos = new ObjectRepositoryWrapper()
		StringBuilder sb = new StringBuilder()
		sb.append("# Reverse TestObject Lookup\n\n")
		sb.append("- TestSuite: ${testSuiteContext.getTestSuiteId()}\n\n")
		//
		compileTestObjectReferenceCount(sb, tracer, repos)
		sb.append("\n\n")
		//
		compileReverseLookupDetail(sb, tracer, repos)
		sb.append("\n\n")
		//
		compileForwardReferenceDetail(sb, tracer, repos)
		//
		if (!Files.exists(outputDir)) {
			Files.createDirectories(outputDir)
		}
		Path output = outputDir.resolve(OUTPUT_FILENAME)
		output.toFile().text = sb.toString()
	}


	private void compileTestObjectReferenceCount(StringBuilder sb,
			AssociationTracer tracer, ObjectRepositoryWrapper repos) {
		sb.append("## Test Object Reference Count\n\n")
		sb.append("| # | Test Object ID | in Repos? | reference count |\n")
		sb.append("| - | -------------- | --------- | --------------: |\n")
		Set<String> testObjects = tracer.allCallees()
		testObjects.addAll(repos.getAllTestObjectIDs())
		List<String> sorted = new ArrayList<String>(testObjects).toSorted()
		sorted.eachWithIndex { testObject, index ->
			Boolean inRepos = repos.includes(testObject)
			Set<String> callers = tracer.callersOf(testObject)
			sb.append("| ${index + 1} | `${testObject}` | ${inRepos} | ${callers.size()} |")
			sb.append("\n")
		}
	}

	private void compileReverseLookupDetail(StringBuilder sb,
			AssociationTracer tracer, ObjectRepositoryWrapper repos) {
		sb.append("## Reverse Lookup Detail\n\n")
		sb.append("| # | Test Object ID | in Repos? | used by Test Case |\n")
		sb.append("| - | -------------- | --------- | ----------------- |\n")
		List<String> testObjects = new ArrayList<String>(tracer.allCallees()).toSorted()
		testObjects.eachWithIndex { testObject, index ->
			List<String> testCases = new ArrayList<String>(tracer.callersOf(testObject)).toSorted()
			Boolean inRepos = repos.includes(testObject)
			testCases.each { testCase ->
				sb.append("| ${index + 1} | `${testObject}` | ${inRepos} | `${testCase}` |")
				sb.append("\n")
			}
		}
	}

	private void compileForwardReferenceDetail(StringBuilder sb,
		AssociationTracer tracer, ObjectRepositoryWrapper repos) {
	sb.append("## Forward Reference Detail\n\n")
	sb.append("| # | Test Case refers | Test Object | in Repos? |\n")
	sb.append("| - | ---------------- | ----------- | --------- |\n")
	List<String> testCases = new ArrayList<String>(tracer.allCallers()).toSorted()
	testCases.eachWithIndex { testCase, index ->
		List<String> testObjects = new ArrayList<String>(tracer.calleesBy(testCase)).toSorted()
		testObjects.each { testObject ->
			Boolean inRepos = repos.includes(testObject)
			sb.append("| ${index + 1} | `${testCase}` | ${inRepos} | `${testObject}` |")
			sb.append("\n")
		}
	}
}
}
