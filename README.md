TestObject Usage Report
====================

<!-- START doctoc -->
<!-- END doctoc -->

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

