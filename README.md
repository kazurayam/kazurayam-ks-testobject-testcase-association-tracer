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
  - [How to run the demo](#how-to-run-the-demo)
  - [How to install the plugin into your Katalon Studio](#how-to-install-the-plugin-into-your-katalon-studio)
  - [How to enable the report in your project](#how-to-enable-the-report-in-your-project)
  - [How the plugin is designed](#how-the-plugin-is-designed)
  - [Cautions](#cautions)
- [Appendiex](#appendiex)
  - [Table Of Contents](#table-of-contents)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

This project is a [Katalon Studio](https://www.katalon.com/katalon-studio/) where I have developed a plug-in module named
*kazurayam-ks-testobject-usage-report*. 

This project is zipped and distributed at the 
[Releases](https://github.com/kazurayam/kazurayam-ks-testobject-usage-report/releases) page.
You can download the zip file, unzip it, open it with your local Katalon Studio.

This project was developed using Katalon Studio ver7.9.1, but it should work on any version 7.0+.

I developed this plug-in for my own sake. I suppose it would be useful other Katalon users.

# Background

Most of the built-in WebUI Keywords of Katalon Studio requires a parameter of type `com.kms.katalon.core.testobject.TestObject`, for example:

- [`WebUI.click`](https://docs.katalon.com/katalon-studio/docs/webui-click.html)
- [`WebUI.setText`](https://docs.katalon.com/katalon-studio/docs/webui-set-text.html)
- [`WebUI.getText`](https://docs.katalon.com/katalon-studio/docs/webui-get-text.html)
- [`WebUI.verifyElementPresent`](https://docs.katalon.com/katalon-studio/docs/webui-verify-element-present.html)

As your Katalon project grows, the number of Test Objects increases. Especially when you use [Record Web Utility](https://docs.katalon.com/katalon-studio/docs/record-web-utility.html), it generates a lot of Test Objects automatically. In the end you find hundreds or even thousands of entries in the `Object Repository` folder. Here arises a problem. A significant portion of the entries is actually unused. We tend to find a lot of garges in the Object Repository.

We want to keep our projects tidy. So Katalon Studio provides [Test Objects Refactoring](https://docs.katalon.com/katalon-studio/docs/test-objects-refactoring.html). By **Tools > Test Object > Show unused Test Objects**, we can find unused entries in the "Object Repository".

# Problem to solve

As the [document](https://docs.katalon.com/katalon-studio/docs/test-objects-refactoring.html) describes, the tool has a shortage:

>A reference of Test Object is defined as invocations of findTestObject("test object ID"). If you create a new Test Object programmatically and don't use them with findTestObject, these Test Objects will be counted as "unused".

Let me elaborate this description. The tool can recognize the following case as "used":

```
// Style A
TestObject tObj = findTestObject('Object Repository/Page_CURA Healthcare Service/a_Make Appointment')
```

But, as [this post](https://forum.katalon.com/t/no-way-to-know-which-object-in-repository-is-being-used-in-scripts/51669/3) pointed out, the tool can not recognize the following code as "used":

```
// Style B
def id = 'Object Repository/Page_CURA Healthcare Service/a_Make Appointment'
..
TestObject tObj = findTestObject(id)
```

Also the tool can not recognized the following case as "used":

```
// Style C
TestObject createTestObjectByXPath(String id, String xpath) {
    TestObject o = new TestObject(id)
    o.addProperty("xpath", ConditionType.EQUALS, xpath)
    return o
}
...
def tObj = new TestObject("a_Make Appointment", '//a[@id='btn-make-appointment']')
```

The shortage matters for me. I prefer the Style B, sometimes the Style C, and rarely take the Style A. Therefore the [Test Objects Refactoring](https://docs.katalon.com/katalon-studio/docs/test-objects-refactoring.html) does not help me much. So I need to help myself.

# Solution

I have developed a plugin named *kazurayam-ks-testobject-usage-report*. You will utilize its API in your *Test Listener*. When you execute a Test Suite, the plugin monitors the invocation of `findTestObject(...)` and `new TestObject(...)` by the Test Cases in the Test Suite. The plugin can recognize all of the Style A, B and C.

When the Test Suite finishes, the plugin compiles some text files in Markdown format where you can find information which Test Object was refered to by which Test Cases. The report includes the reference count of each Test Objects. The report includes "0-used Test Object in this time of Test Suite run". A sample is [here](docs/testobject_usage_full.md).



# Description

## Sample report

- [testobject_usage_report.md](docs/testobject_usage_report.md)

### (1) Test Objects in the Object Repository, used by testcases

| # | Test Object ID | in Repos? | reference count |
| - | -------------- | --------- | --------------: |
| . | .. | .. | .. |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | 2 |
| . | .. | .. | .. |

### (2) Test Objects in the Object Repository, unused at all

| # | Test Object ID | in Repos? | reference count |
| - | -------------- | --------- | --------------: |
| . | .. | .. | .. |
| 12 | `Page_CURA Healthcare Service/td_20` | true | 0 |
| . | .. | .. | .. |

### (3) Test Objects dynamically instanciated by code

| # | Test Object ID | in Repos? | reference count |
| - | -------------- | --------- | --------------: |
| . | .. | .. | .. |
| 14 | `Visit Date` | false | 1 |
| . | .. | .. | .. |

## How to run the demo

## How to install the plugin into your Katalon Studio

## How to enable the report in your project

You need to create a [Test Listener](https://docs.katalon.com/katalon-studio/docs/fixtures-listeners.html) in your project. You can copy and paste the 

- [`Test Listeners/AssociationDriver`](Test%20Listeners/AssociatorDriver.groovy)

into your project. Just copy it. No code change for customization is required.



## How the plugin is designed

Do you want to understand how the `kazurayam-ks-testobject-usage-report` plugin is designed?

The entry point for you is to read the source code of 

- [`Test Listeners/AssociatorDriver`](Test%20Listeners/AssociatorDriver.groovy)

Let's have a look at it.




# Appendiex

## Table Of Contents 

You may have notices that I added a Table Of Contents in this README. 
I used a GitHub Action named "toc-generator".

- https://dev.classmethod.jp/articles/auto-generate-toc-on-readme-by-actions/

