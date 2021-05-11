TestObject Usage Report
====================

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [Background](#background)
- [Problem to solve](#problem-to-solve)
- [Solution](#solution)
- [Description](#description)
  - [Sample report](#sample-report)
    - [(1) Test Objects in the Object Repository, used by testcases](#1-test-objects-in-the-object-repository-used-by-testcases)
    - [(2) Test Objects in the Object Repository, unused at all](#2-test-objects-in-the-object-repository-unused-at-all)
    - [(3) Test Objects dynamically instanciated by code](#3-test-objects-dynamically-instanciated-by-code)
  - [CAUTION](#caution)
  - [WARNING](#warning)
    - [Test Case failures make the report unreliable](#test-case-failures-make-the-report-unreliable)
    - ["if ... then ... else" makes the report unreliable](#if--then--else-makes-the-report-unreliable)
  - [How to run the demo](#how-to-run-the-demo)
  - [How to install the plugin into your Katalon Studio](#how-to-install-the-plugin-into-your-katalon-studio)
  - [How to enable the report in your project](#how-to-enable-the-report-in-your-project)
  - [How the plugin is designed](#how-the-plugin-is-designed)
- [Appendiex](#appendiex)
  - [Table Of Contents](#table-of-contents)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

This project is a [Katalon Studio](https://www.katalon.com/katalon-studio/) where I have developed a plug-in module named
*kazurayam-ks-testobject-usage-report*. 

This project is zipped and distributed at the 
[Releases](https://github.com/kazurayam/kazurayam-ks-testobject-usage-report/releases) page.
You can download the zip file, unzip it, open it with your local Katalon Studio.

This project was developed using Katalon Studio ver7.9.1, but it should work on any version 7.0+.

I developed this plug-in for my own sake. I hope it could be useful other Katalon users.

# Background

Most of the built-in WebUI Keywords of Katalon Studio requires a parameter of type `com.kms.katalon.core.testobject.TestObject`, for example:

- [`WebUI.click`](https://docs.katalon.com/katalon-studio/docs/webui-click.html)
- [`WebUI.setText`](https://docs.katalon.com/katalon-studio/docs/webui-set-text.html)
- [`WebUI.getText`](https://docs.katalon.com/katalon-studio/docs/webui-get-text.html)
- [`WebUI.verifyElementPresent`](https://docs.katalon.com/katalon-studio/docs/webui-verify-element-present.html)

As your Katalon project grows, the number of Test Objects increases. Especially when you use [Record Web Utility](https://docs.katalon.com/katalon-studio/docs/record-web-utility.html), it generates a lot of Test Objects automatically. In the end you will find hundreds or thousands of entries in the `Object Repository` folder. Here arises a problem. A significant portion of the entries in the Object Repository will be unused garbages. A users shared his case in [this post](https://forum.katalon.com/t/performance-issue-for-show-unused-test-objects/51791/8)

All of us want to keep our projects tidy. So Katalon Studio v7 added a built-in feature: [Test Objects Refactoring](https://docs.katalon.com/katalon-studio/docs/test-objects-refactoring.html). By **Tools > Test Object > Show unused Test Objects**, we can find unused entries in the "Object Repository".

# Problem to solve

As the [document](https://docs.katalon.com/katalon-studio/docs/test-objects-refactoring.html) describes, the tool has a shortage:

>A reference of Test Object is defined as invocations of findTestObject("test object ID"). If you create a new Test Object programmatically and don't use them with findTestObject, these Test Objects will be counted as "unused".

Let me elaborate this description. The tool can recognize the following code in a Test Case script as "used":

```
// Style A
TestObject tObj = findTestObject('Object Repository/Page_CURA Healthcare Service/a_Make Appointment')
```

But, as [this post](https://forum.katalon.com/t/no-way-to-know-which-object-in-repository-is-being-used-in-scripts/51669/3) pointed out, the tool can't recognize the following code as "used":

```
// Style B
def id = 'Object Repository/Page_CURA Healthcare Service/a_Make Appointment'
..
TestObject tObj = findTestObject(id)
```

Also the tool can't recognize the following case as "used":

```
// Style C
class TestObjectFactory {   // my custom Keyword
    static TestObject byXPath(String id, String xpath) {
        TestObject o = new TestObject(id)
        o.addProperty("xpath", ConditionType.EQUALS, xpath)
        return o
    }
}
...
def tObj = TestObjectFactory.byXPath("a_Make Appointment", '//a[@id='btn-make-appointment']')
```

I personally prefer writing locators (XPath, CSS Selectors) manually in the code; I do not need the [Record Web Utility](https://docs.katalon.com/katalon-studio/docs/record-web-utility.html) tool to write correct locators. So usually I take the Style C. But the [Test Objects Refactoring](https://docs.katalon.com/katalon-studio/docs/test-objects-refactoring.html) feature does not support the Style C. Therefore the tool does not help me.

I accept that, as [this post](https://forum.katalon.com/t/no-way-to-know-which-object-in-repository-is-being-used-in-scripts/51669/7) mentioned, the tool is designed for the common users. I see that the Style C is not common. OK, I would help myself.

# Solution

I have developed a plugin named *kazurayam-ks-testobject-usage-report*. You will utilize its API in your *Test Listener*. When you execute a Test Suite, the plugin monitors the invocation of `findTestObject(...)` and `new TestObject(...)` by the Test Cases in the Test Suite. The plugin can recognize all of the Style A, B and C.

When the Test Suite finishes, the plugin compiles some text files in Markdown format where you can find information which Test Object was refered to by which Test Cases. The report includes the reference count of each Test Objects. It includes Test Objects which are used 0 time.

# Description

## Sample report

A sample report is availabe:

- [testobject_usage_full.md](docs/testobject_usage_full.md).


### (1) Unused Test Object report

This report list the Test Case with reference count of 0.

| # | Test Object ID | in Repos? | reference count |
| - | -------------- | --------- | --------------: |
| 12 | `Page_CURA Healthcare Service/td_20` | true | 0 |

### (2) Reference Count report

This report list all of Test Objects prepared in the Object Repository (in Repos? is *true*) and the Test Objects dynamically instanciated by `"new TestObject(id)"` (in Repos? is *false*). The report shows the number of Test Cases that refer to each Test Object.

The "unused Test Object" will be included here.

| # | Test Object ID | in Repos? | reference count |
| - | -------------- | --------- | --------------: |
| . | .. | .. | .. |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | 2 |
| . | .. | .. | .. |
| 12 | `Page_CURA Healthcare Service/td_20` | true | 0 |
| . | .. | .. | .. |
| 14 | `Visit Date` | false | 1 |
| . | .. | .. | .. |

### (3) Reverse Lookup Detail report

The report enumerates the IDs of referer Test Cases of each Test Object.

> The unused Test Objects will not appear here.

| # | Test Object ID | in Repos? | used by Test Case |
| - | -------------- | --------- | ----------------- |
| 1 | `(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[20]` | false | `Test Cases/main/TC_CURA` |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | `Test Cases/main/TC_CURA` |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | `Test Cases/main/TC_CURA - more` |
| . | .. | .. | .. |


### (4) Forward Lookup Detail report

This report shows you which Test Objects were refered to by a Test Case. The report includes all Test Cases bundled in the Test Suite you executed.

> The unused Test Objects will not appear here.

| # | Test Case refers | Test Object | in Repos? |
| - | ---------------- | ----------- | --------- |
| 1 | `Test Cases/main/TC_CURA` | `(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[20]` | false |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/a_Go to Homepage` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/a_Make Appointment` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/button_Book Appointment` | true |
| . | .. | .. | .. |


## CAUTION

You need to be careful in reading the report by my plug-in.

The plug-in does not scan the source codes of all Test Cases. Rather, the plug-in monitors the runtime behavior of Test Case A, B, C which are bundled in a specific Test Suite you executed. So the report can only be correct about the Test Case A, B, C. If you have more Test Case X, Y, Z which are not bundled in the Test Suite you checked, then the report can not count the references to Test Objects by Test Case X, Y, Z.

Therefore it is recommended that you create a Test Suite. Any name it can be, e.g, **TS_runAllTestCases**. This would bundle all of your Test Cases so that the my plugin compiles reports as comprehensive as possible.

## WARNING

### Test Case failures make the report unreliable

The report will be unreliable when any Test Case failed and stopped during a Test Suite run. In that case, the statements after the failure will be skipped. My plugin will not be informed of the behavior of skipped statements. So the report would be unreliable. You should fix all problems in Test Cases first. 

>I am sure you would be tempted to look at the failures first. Garbages in the Object Repository is low profile.

### "if ... then ... else" makes the report unreliable

The following Test Case is too difficult for my plugin:

```
TestObject tObj
if (conditon) {
  tObj = new TestObject("foo")
  ...
} else {
  tObj = new TestObject("bar")
  ...
}
```

In one case, the "foo" TestObject will be seen used, and the "bar" unused. In another case, the "foo" will be seen unused and the "bar" used. These cases depend on the "condition". My plug-in is not intelligent enough to report that *both of "foo" and "bar" are used*.

>A possible workaround for this difficulty for me would be:

```
TestObject foo = new TestObject("foo")
TestObject bar = new TestObject("bar)
if (conditon) {
  // use foo 
} else {
  //use bar
}
```

## How to install the plugin into your Katalon Studio

Here I assume you have already created a Katalon Studio project with running Test Cases and Test Suite. 

1. download `kazurayam-ks-testobject-usage-report-x.x.x.jar` from the [Releases](https://github.com/kazurayam/kazurayam-ks-testobject-usage-report/releases) page.
2. place the jar in the `<projectDir>/Plugins` folder
3. stop&restart Katalon Studio

## How to enable the report in your project

### (1) create a Test Listener

You need to create a [Test Listener](https://docs.katalon.com/katalon-studio/docs/fixtures-listeners.html) in your project. The name can be any. You can copy and paste the following sample code: 

- [`Test Listeners/AssociationDriver`](Test%20Listeners/AssociatorDriver.groovy)

No code change is required. It will run in any project.

### (2) run a Test Suite, then you will get the reports

Just choose your Test Suite and execute it.

Once your Test Suite finished, you will find a new folder `<projectDir>/build/reports` is created and find 2 text files generated:

- [build/reports/testobject_usage_full.md](docs/testobject_usage_full.md)
- [build/reports/testobject_usage_summary.md](docs/testobject_usage_summary.md)

## How the plugin is designed

### (1) Test Listener interfaces your tests and the plugin

The [`Test Listeners/AssociatorDriver`](Test%20Listeners/AssociatorDriver.groovy) delegates the magical processing to an instance of `com.kazurayam.ks.testobject.Associator`.

```
class AssociatorDriver {
	...
  private Associator associator
	...
	AssociatorDriver() {
		associator = new Associator()
	}

	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		associator.beforeTestSuite(testSuiteContext)
	}
  ...
```

The `Associator` instance modifies the implementation of Katalon classes: [`com.kms.katalon.core.testobject.TestObject`](https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/testobject/TestObject.java) and [`com.kms.katalon.core.testobject.ObjectRepository`](https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/testobject/ObjectRepository.java) using [Groovy's Metaprogramming technique](https://groovy-lang.org/metaprogramming.html#metaprogramming_emc). The magic spell looks like this:

```
package com.kazurayam.ks.testobject
...
public class Associator {
  ...
  Boolean modifyKatalonClasses() {

		AssociationTracer tracer = AssociationTracer.getInstance()
    ...
    ObjectRepository.metaClass.'static'.invokeMethod = { String methodName, args ->
			if (methodName == "findTestObject") {
				tracer.trace(GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASE_ID], testObjectId)
			}
			return delegate.metaClass.getMetaMethod(methodName, args).invoke(delegate, args)
		}
		
    TestObject.metaClass.constructor = { String testObjectId ->
			tracer.trace(GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASE_ID],
					testObjectId.replaceAll("Object Repository/", ''))
			def constructor = TestObject.class.getConstructor(String.class)
			return constructor.newInstance(testObjectId)
		}
		return true
	}
```

The `Associator` class uses `AssociationTracer` class which employs the Design Pattern ["Singleton"](https://www.baeldung.com/java-singleton). The `AssociationTracer` instance exists in the Test Listener's scope. When the `ObjectRepository.findTestObject(id)` method is called by your test case, the method notifies the `AssociationTracer`' of an association of *(TestCaseId, TestObjectId)* dynamically. When the `new TestObject(id)` method is invoked, it will do the same. At the end of a Test Suite run, the `AssocationTracer` instance (and indrectly `Associator` as well) will keeps all of the *(TestCaseId, TestObjectId)* that appeared.


### (2) how the reports are compiled

The `@AfterTestSuite`-annotated method of the Test Listener drives `TestObjectUsageReporter` class while passing the instance of fully-charged `Associator` and the Path information to save files into.

```
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
```

One output from the `TestObjectUsageReporter` can be composed of 4 types of reports: "UNUSUED", "COUT", "REVERSE", "FORWARD". By **compositon(List[String])** method of `com.kazurayam.ks.testobject.TestObjectUsageReporter.Buidler` class, you can choose which type to output, and you can specify the order in a file. The method call is optional; will default to `.composition(["UNUSED", "COUNT"])`. The "REVERSE" and "FORWARD" report could be too length if you have hundreds of Test Objects in your project.

By **outputDir(Path)** method, you can optionally specify the directory to save the output file. `outputDir(Path)` is optional; will default to `"./build/reports"`.

By **outputFilename(String)** method, you can specify the name of the output file. It is optional; will default to `"testobject_usage_report.md"`.

The sample [`Test Listeners/AssociatorDriver`](Test%20Listeners/AssociatorDriver.groovy) generates 2 files. You can change the code it to output only 1 file, or more. The location and the name of output file is up to you.

### (3) custom reports?

My `com.kazurayam.ks.testobject.TestObjectUsageReporter` class generates output in [Markdown](https://guides.github.com/features/mastering-markdown/) format, which is my favorite. You can develop your own reporter class and use it alternatively in the `Test Listener/AssociationDriver`. Read the soure [here](Keywords/com/kazurayam/ks/testobject/TestObjectUsageReporter.groovy). You would find everything you need to know to make your own reporter class.

# Conclusion

I have developed my own solution as a downloadable jar. I hope it is useful for others as well.

<!--
You may have notices that I added a Table Of Contents in this README. I used a GitHub Action named "toc-generator".

- https://dev.classmethod.jp/articles/auto-generate-toc-on-readme-by-actions/
-->
