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

# Background

There are 2 ways of creating a Test Object.

- statically prepared in "Object Repository"
- dynamically instanciated by "new TestObject(id)" in script.


# Problem to solve

# Solution

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

The report will be compiled as a by-product of running a Test Suite.

## Cautions

May not be right!

# Appendiex

## Table Of Contents 

You may have notices that I added a Table Of Contents in this README. 
I used a GitHub Action named "toc-generator".

- https://dev.classmethod.jp/articles/auto-generate-toc-on-readme-by-actions/

