package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestSuiteContext

import internal.GlobalVariable
import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable

/**
 * compile a report with a list of TestObjects appearing in this project.
 * Each TestObject is associated with information which TestCase called it,
 * and if the TestObject is backed by files in the Object Repository,
 * or if it is dynamically instantiated.
 *
 * @param tracer
 * @param outputDir
 */
public class TestObjectUsageReporter implements Reporter {

	public static final enum ReportType {
		UNUSED,
		COUNT,
		REVERSE,
		FORWARD
	}

	public static final TITLE_TOP = "Test Object Usage Report"
	public static final TITLE_UNUSED = "Unused Test Object List"
	public static final TITLE_COUNT = "Test Object Reference Count"
	public static final TITLE_REVERSE = "Reverse Lookup Detail"
	public static final TITLE_FORWARD = "Forward Reference Detail"

	private Associator associator
	private TestSuiteContext testSuiteContext
	private Path outputDir
	private String outputFilename
	private List<ReportType> composition

	public static class Builder {

		public static final String OUTPUT_DIR_DEFAULT = "build/reports"
		public static final String OUTPUT_FILENAME = "testobject_usage_report.md"

		// Required parameters
		private final Associator associator
		private final TestSuiteContext testSuiteContext

		// Optional parameters - initialized to default values
		private Path outputDir = Paths.get(RunConfiguration.getProjectDir()).resolve(OUTPUT_DIR_DEFAULT)
		private String outputFilename = OUTPUT_FILENAME
		private List<ReportType> composition = [
			ReportType.UNUSED,
			ReportType.COUNT
		]

		public Builder(Associator associator, TestSuiteContext testSuiteContext) {
			this.associator = associator
			this.testSuiteContext = testSuiteContext
		}

		public Builder outputDir(String outputDir) {
			Path dir = Paths.get(outputDir)
			this.outputDir(dir)	
		}
		
		public Builder outputDir(Path outputDir) {
			this.outputDir = outputDir
			return this
		}

		public Builder outputFilename(String outputFileName) {
			this.outputFilename = outputFileName
			return this
		}

		public Builder composition(List<String> requestList) {
			this.composition.clear()
			List<ReportType> list = new ArrayList<ReportType>()
			requestList.forEach { request ->
				try {
					this.composition.add(ReportType.valueOf(request))
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("ReportType \"${request}\" is unknown.", e)
				}
			}
			return this
		}

		public TestObjectUsageReporter build() {
			return new TestObjectUsageReporter(this)
		}
	}

	private TestObjectUsageReporter(Builder builder) {
		associator = builder.associator
		testSuiteContext = builder.testSuiteContext
		outputDir = builder.outputDir
		outputFilename = builder.outputFilename
		composition = builder.composition
	}

	@Override
	Path getOutputDir() {
		return outputDir
	}

	@Override
	String getOutputFilename() {
		return outputFilename
	}

	@Override
	public String report() {
		AssociationTracer tracer = associator.getTracer()
		ObjectRepositoryWrapper repos = new ObjectRepositoryWrapper()
		StringBuilder sb = new StringBuilder()
		sb.append("# ${TITLE_TOP}\n\n")
		sb.append("- Test Suite: ${GlobalVariable[Associator.GLOBALVARIABLE_CURRENT_TESTSUITE_ID]}\n")
		sb.append("- started at: ${GlobalVariable[Associator.GLOBALVARIABLE_CURRENT_TESTSUITE_TIMESTAMP]}\n")
		sb.append("\n")
		sb.append("**WARNING**: The information here depends on when and which Test Suite you executed.\n\n")
		//
		composition.forEach({ request ->
			switch (request) {
				case ReportType.UNUSED:
					compileUnusedTestObjectList(sb, tracer, repos)
					sb.append("\n\n")
					break
				case ReportType.COUNT:
					compileTestObjectReferenceCount(sb, tracer, repos)
					sb.append("\n\n")
					break
				case ReportType.REVERSE:
					compileReverseLookupDetail(sb, tracer, repos)
					sb.append("\n\n")
					break
				case ReportType.FORWARD:
					compileForwardReferenceDetail(sb, tracer, repos)
					sb.append("\n\n")
					break
				default:
					throw new IllegalStateException("${request} is not supported")
			}
		})
		return sb.toString()
	}

	@Override
	public void write() {
		if (!Files.exists(outputDir)) {
			Files.createDirectories(outputDir)
		}
		Path output = outputDir.resolve(outputFilename)
		output.toFile().text = this.report()
	}


	/**
	 * compile "Unused Test Object List", or "Unused report"
	 */
	private void compileUnusedTestObjectList(StringBuilder sb,
			AssociationTracer tracer, ObjectRepositoryWrapper repos) {
		sb.append("## ${TITLE_UNUSED}\n\n")
		sb.append("This table shows the list of Test Objects in the Object Repository"
				+ " which were not used during this time of Test Suite run.\n\n")
		sb.append("| # | Test Object ID | in Repos? | reference count |\n")
		sb.append("| - | -------------- | --------- | --------------: |\n")
		Set<String> testObjects = tracer.allCallees()
		testObjects.addAll(repos.getAllTestObjectIDs())
		List<String> sorted = new ArrayList<String>(testObjects).toSorted()
		sorted.eachWithIndex { testObject, index ->
			Boolean inRepos = repos.includes(testObject)
			Set<String> callers = tracer.callersOf(testObject)
			if (callers.size() == 0) {
				sb.append("| ${index + 1} | `${testObject}` | ${inRepos} | ${callers.size()} |")
				sb.append("\n")
			}
		}
	}



	/**
	 * compile "TestObject Reference Count", or "Count report"
	 *  
	 * @param sb
	 * @param tracer
	 * @param repos
	 */
	private void compileTestObjectReferenceCount(StringBuilder sb,
			AssociationTracer tracer, ObjectRepositoryWrapper repos) {
		sb.append("## ${TITLE_COUNT}\n\n")
		/*
		 sb.append("This table shows the list of two types of TestObjects:\n"
		 + "1. All Test Objects found in the `Object Repository`\n"
		 + "2. `TestObject` instances created dynamically in Test Cases during this time of Test Suite run.\n"
		 + "The table includes *Reference Count*."
		 + " The Reference Count shows the number of Test Cases"
		 + " which refered to each TestObjects by calling either of"
		 + " `com.kms.katalon.core.testobject.ObjectRepository.findTestObject(testObjectId)`"
		 + " or"
		 + " `new com.kms.katalon.core.testobject.TestObject(testObjectId)`."
		 + "\n\n")
		 */
		sb.append("| # | Test Object ID | in Repos? | Reference Count |\n")
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


	/**
	 * compile "Reverse TestObject Lookup Detail", or "Reverse report"
	 * @param sb
	 * @param tracer
	 * @param repos
	 */
	private void compileReverseLookupDetail(StringBuilder sb,
			AssociationTracer tracer, ObjectRepositoryWrapper repos) {
		sb.append("## ${TITLE_REVERSE}\n\n")
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

	/**
	 * compile "Forward TestObject Lookup Detail", or "Forward report"
	 * 
	 * @param sb
	 * @param tracer
	 * @param repos
	 */
	private void compileForwardReferenceDetail(StringBuilder sb,
			AssociationTracer tracer, ObjectRepositoryWrapper repos) {
		sb.append("## ${TITLE_FORWARD}\n\n")
		sb.append("| # | Test Case refers | Test Object | in Repos? |\n")
		sb.append("| - | ---------------- | ----------- | --------- |\n")
		List<String> testCases = new ArrayList<String>(tracer.allCallers()).toSorted()
		testCases.eachWithIndex { testCase, index ->
			List<String> testObjects = new ArrayList<String>(tracer.calleesBy(testCase)).toSorted()
			testObjects.each { testObject ->
				Boolean inRepos = repos.includes(testObject)
				sb.append("| ${index + 1} | `${testCase}` | `${testObject}` | ${inRepos} |")
				sb.append("\n")
			}
		}
	}
}
