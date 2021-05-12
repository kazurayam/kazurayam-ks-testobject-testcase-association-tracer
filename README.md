TestObject Usage Report
====================

- author: kazurayam
- date: May 2021

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [Background](#background)
- [Problem to solve](#problem-to-solve)
- [Solution](#solution)
- [Description](#description)
  - [Sample report](#sample-report)
    - [(1) Unused Test Object report](#1-unused-test-object-report)
    - [(2) Reference Count report](#2-reference-count-report)
    - [(3) Reverse Lookup Detail report](#3-reverse-lookup-detail-report)
    - [(4) Forward Lookup Detail report](#4-forward-lookup-detail-report)
  - [CAUTION](#caution)
  - [WARNING](#warning)
    - [Test Case failures make a report unreliable](#test-case-failures-make-a-report-unreliable)
    - ["if ... then ... else" statement makes a report unreliable](#if--then--else-statement-makes-a-report-unreliable)
  - [How to install the plug-in into your Katalon Studio](#how-to-install-the-plug-in-into-your-katalon-studio)
  - [How to let your project to compile a report](#how-to-let-your-project-to-compile-a-report)
    - [(1) create a Test Listener](#1-create-a-test-listener)
    - [(2) run a Test Suite, then you will get a reports](#2-run-a-test-suite-then-you-will-get-a-reports)
  - [How the plug-in is designed](#how-the-plug-in-is-designed)
    - [(1) Test Listener interfaces tests with plug-in](#1-test-listener-interfaces-tests-with-plug-in)
    - [(2) how the reports are compiled](#2-how-the-reports-are-compiled)
    - [(3) custom reports?](#3-custom-reports)
- [Conclusion](#conclusion)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

This is a [Katalon Studio](https://www.katalon.com/katalon-studio/) project where I develop a plug-in module named
*kazurayam-ks-testobject-usage-report*.

You can download the zip file from at the 
[Releases](https://github.com/kazurayam/kazurayam-ks-testobject-usage-report/releases) page, unzip it, open it with your local Katalon Studio.

This project was developed with Katalon Studio ver7.9.1 Free vesion, but it should work on any version 7.0+.

# Background

Most of the built-in WebUI Keywords of Katalon Studio requires a parameter of type `com.kms.katalon.core.testobject.TestObject`, for example:

- [`WebUI.click`](https://docs.katalon.com/katalon-studio/docs/webui-click.html)
- [`WebUI.setText`](https://docs.katalon.com/katalon-studio/docs/webui-set-text.html)
- [`WebUI.getText`](https://docs.katalon.com/katalon-studio/docs/webui-get-text.html)
- [`WebUI.verifyElementPresent`](https://docs.katalon.com/katalon-studio/docs/webui-verify-element-present.html)

As a Katalon project grows, the number of Test Objects increases. Especially when you use [Record Web Utility](https://docs.katalon.com/katalon-studio/docs/record-web-utility.html), it generates a lot of Test Objects automatically. In the end you will find hundreds or thousands of entries in the `Object Repository` folder. Here arises a problem. A significant portion of the entries in the Object Repository will be occupied by unused garbages. A user shared his case in [this post](https://forum.katalon.com/t/performance-issue-for-show-unused-test-objects/51791/8).

Katalon Studio v7 introduced a tool: [Test Objects Refactoring](https://docs.katalon.com/katalon-studio/docs/test-objects-refactoring.html) to help users who want to keep their projects tidy. We can find unused entries in the "Object Repository" by **Tools > Test Object > Show unused Test Objects**.

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

I usually take the Style C because I prefer writing locators (XPath, CSS Selectors) manually in the code; I do not need the [Record Web Utility](https://docs.katalon.com/katalon-studio/docs/record-web-utility.html) to write correct locators. But the [Test Objects Refactoring](https://docs.katalon.com/katalon-studio/docs/test-objects-refactoring.html) tool does not support the Style C. So the tool is not useful for me. [This post](https://forum.katalon.com/t/no-way-to-know-which-object-in-repository-is-being-used-in-scripts/51669/7) mentioned that the tool is designed for the common users. I would accept that. I agree that the Style C is not common.

OK, I would develop a solution myself.

# Solution

I have developed a plug-in named *kazurayam-ks-testobject-usage-report*. You can use it in a *Test Listener*. When you execute a Test Suite, the plug-in monitors invocations of `findTestObject(...)` and `new TestObject(...)` by the Test Cases in the Test Suite. The plug-in can recognize all of the Style A, B and C.

When the Test Suite finishes, the plug-in compiles some text files in Markdown format where you can find information which Test Objects were refered to by which Test Cases. The report includes the reference count of each Test Objects. It shows you a list of unused Test Objects.

# Description

## Sample report

A sample report is available:

- [testobject_usage_full.md](docs/testobject_usage_full.md).


### (1) Unused Test Object report

This report list the Test Case with reference count of 0.

| # | Test Object ID | in Repos? | reference count |
| - | -------------- | --------- | --------------: |
| 12 | `Page_CURA Healthcare Service/td_20` | true | 0 |

### (2) Reference Count report

This report list all of Test Objects prepared in the Object Repository (in Repos? is *true*) and the Test Objects dynamically instanciated by `"new TestObject(id)"` (in Repos? is *false*). The report shows the number of Test Cases that refer to each Test Object.

Test Objects with 0 reference count ("unused") will be included in this list.

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

The report shows a list of all used Test Objects, associated with list of Test Cases that refered each Test Objects. It is sorted by Test Object IDs.

> The unused Test Objects will not appear here.

| # | Test Object ID | in Repos? | used by Test Case |
| - | -------------- | --------- | ----------------- |
| 1 | `(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[20]` | false | `Test Cases/main/TC_CURA` |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | `Test Cases/main/TC_CURA` |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | `Test Cases/main/TC_CURA - more` |
| . | .. | .. | .. |


### (4) Forward Lookup Detail report

This report shows a list of *(from Test Case -> to Test Object)* associations sorted by the Test Case IDs.

>The unused Test Objects will not appear here.

| # | Test Case refers | Test Object | in Repos? |
| - | ---------------- | ----------- | --------- |
| 1 | `Test Cases/main/TC_CURA` | `(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[20]` | false |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/a_Go to Homepage` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/a_Make Appointment` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/button_Book Appointment` | true |
| . | .. | .. | .. |


## CAUTION

You need to be careful in reading the reports.

The plug-in does not literally scan the source codes of Test Case. Rather, the plug-in monitors the runtime behavior of Test Case-A,B,C which are bundled in a specific Test Suite you executed. So the report can only be correct about the Test Case-A,B,C. If you have other Test Cases-X,Y,Z that are not bundled in the Test Suite, the report can't count the references to Test Objects by Test Case-X,Y,Z.

Therefore I would recommend you to create a special Test Suite for compiling "Test Object Usage Report". It's name can be any; e.g, **TS_runAllTestCases**. This Test Suite should bundle all of Test Cases so that the plug-in can compile reports as comprehensive as possible.

## WARNING

### Test Case failures make a report unreliable 

"Test Object Usage Report" will be unreliable when one or more Test Cases failed and stopped. The statements after a failure will be skipped. The plug-in will become ignorant of the skipped statements; then the report becomes unreliable. You should fix all failures in Test Cases first.

>I am sure you would look at the failures first, as garbages in the Object Repository is low profile.

### "if ... then ... else" statement makes a report unreliable

The following Test Case is too difficult for the plug-in:

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

When this code rans, sometimes the "foo" TestObject will be seen used and the "bar" unused. At other times the "foo" will be seen unused and the "bar" used. It depends on the "condition". The plug-in can't count that *both of "foo" and "bar" are used*.

>The following code shows a possible workaround for this difficulty:

```
TestObject foo = new TestObject("foo")
TestObject bar = new TestObject("bar)
if (conditon) {
    // use foo
} else {
    // use bar
}
```

## How to install the plug-in into your Katalon Studio

Here I assume you have already created a Katalon Studio project with running Test Cases and a Test Suite.

1. download `kazurayam-ks-testobject-usage-report-x.x.x.jar` from the [Releases](https://github.com/kazurayam/kazurayam-ks-testobject-usage-report/releases) page.
2. place the jar in the `<projectDir>/Plugins` folder
3. stop and restart Katalon Studio

## How to let your project to compile a report

### (1) create a Test Listener

You need to create a [Test Listener](https://docs.katalon.com/katalon-studio/docs/fixtures-listeners.html) in your project. The name can be any. Please copy and paste the following code:

- [`Test Listeners/AssociationDriver`](Test%20Listeners/AssociatorDriver.groovy)

No code change is required. It will run in any project.

### (2) run a Test Suite, then you will get a reports

Just choose a Test Suite and execute it.

Once the Test Suite finished, you will find a new folder `<projectDir>/build/reports` is created and find 2 text files generated:

- [build/reports/testobject_usage_full.md](docs/testobject_usage_full.md)
- [build/reports/testobject_usage_summary.md](docs/testobject_usage_summary.md)

## How the plug-in is designed

Do you want to know the internal of the plug-in? OK, let me tell you a bit about it.

### (1) Test Listener interfaces tests with plug-in

The [`Test Listeners/AssociatorDriver`](Test%20Listeners/AssociatorDriver.groovy) delegates the magical processing to an instance of `com.kazurayam.ks.testobject.Associator`.

```
import com.kazurayam.ks.testobject.Associator

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

The `Associator` instance dynamically modifies the implementation of following 2 Katalon classes:

- [`com.kms.katalon.core.testobject.TestObject`](https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/testobject/TestObject.java)
- [`com.kms.katalon.core.testobject.ObjectRepository`](https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/testobject/ObjectRepository.java) 

Magic spell `modifyKatalonClasses()` looks like this:

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
    ...
```

I employed [Groovy's Metaprogramming technique](https://groovy-lang.org/metaprogramming.html#metaprogramming_emc) here.

Let me dictate this code:

- When a test case invokes `ObjectRepository.findTestObject(id)` method, then the *modified version* of `findTestObject` method notifies the `AssociationTracer` instance of *(TestCaseId, TestObjectId)* association.
- The `AssociationTracer` records everything notified. 
- An invokation of `new TestObject(id)` method will do the same: notifies the `AssociationTracer` of the *(TestCaseId, TestObjectId)* association. 
- Eventually the `AssocationTracer` instance will know all of the *(TestCaseId, TestObjectId)* associations that appeared during a Test Suite run.

The `Test Listeners/AssociatorDriver` can get access to the information recorded by `AssociationTracer` via the `accociator` variable for compiling reports finally.

### (2) how the reports are compiled

`@AfterTestSuite`-annotated method of the `Test Listeners/AssociatorDriver` calls `TestObjectUsageReporter` object to compile reports. An instance of fully-charged `Associator` and the Path information to save files into will be passed as parameters.

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

An output file from the `TestObjectUsageReporter` can be composed of 4 types of reports: "UNUSUED", "COUNT", "REVERSE" and "FORWARD". The method call is optional; will default to `.composition(["UNUSED", "COUNT"])`.

By **compositon(List[String])** method of `com.kazurayam.ks.testobject.TestObjectUsageReporter.Buidler` class, you can specify which type to print, how these should be ordered. For example, `.composition(["COUNT","REVERSE")` and `.composition(["REVERSE","COUNT")` will compile the same set of component reports but in opposit order.

>Beware that the "REVERSE" and "FORWARD" report could be very long if you have many Test Cases + Test Objects.

By **outputDir(Path)** method, you can optionally specify the directory to save the output files. `outputDir(Path)` is optional; will default to `"./build/reports"`.

By **outputFilename(String)** method, you can specify the name of the output file. It is optional; will default to `"testobject_usage_report.md"`.

The sample [`Test Listeners/AssociatorDriver`](Test%20Listeners/AssociatorDriver.groovy) generates 2 files. You can change the code to emit as many files as you want.

### (3) custom reports?

`com.kazurayam.ks.testobject.TestObjectUsageReporter` class generates output in [Markdown](https://guides.github.com/features/mastering-markdown/) format. You can develop your custom reporter and use it in the Test Listener. Please read the soure of [TestObjectUsageReporter](Keywords/com/kazurayam/ks/testobject/TestObjectUsageReporter.groovy) as an example.

# Conclusion

I have developed this plug-in for my own sake. I hope it could be useful for other Katalon users.

<!--
You may have notices that I added a Table Of Contents in this README. I used a GitHub Action named "toc-generator".

- https://dev.classmethod.jp/articles/auto-generate-toc-on-readme-by-actions/
-->
