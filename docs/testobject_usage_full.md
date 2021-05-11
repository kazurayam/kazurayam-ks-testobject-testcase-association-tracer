# Test Object Usage Report

- Test Suite: Test Suites/main/TS_CURA
- started at: 2021-05-10T16:31:02

**Be careful !**: The reference count reported here depends on which Test Suite you executed and how it ran (PASSED or FAILED). It is highly likely that another Test Suite will emit different values. 

## Unused Test Object List

This table shows the list of Test Objects prepared in the Object Repository and were not used during this time of Test Suite run.

| # | Test Object ID | in Repos? | reference count |
| - | -------------- | --------- | --------------: |
| 12 | `Page_CURA Healthcare Service/td_20` | true | 0 |


## Test Object Reference Count

This table shows the list of two types of TestObjects:
1. All Test Objects found in the `Object Repository`
2. `TestObject` instances created dynamically in Test Cases during this time of Test Suite run.
The table includes *Reference Count*. The Reference Count shows the number of Test Cases which refered to each TestObjects by calling either of `com.kms.katalon.core.testobject.ObjectRepository.findTestObject(testObjectId)` or `new com.kms.katalon.core.testobject.TestObject(testObjectId)`.

| # | Test Object ID | in Repos? | Reference Count |
| - | -------------- | --------- | --------------: |
| 1 | `(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[20]` | false | 1 |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | 2 |
| 3 | `Page_CURA Healthcare Service/a_Make Appointment` | true | 2 |
| 4 | `Page_CURA Healthcare Service/button_Book Appointment` | true | 2 |
| 5 | `Page_CURA Healthcare Service/button_Login` | true | 2 |
| 6 | `Page_CURA Healthcare Service/input_Apply for hospital readmission_hospit_63901f` | true | 2 |
| 7 | `Page_CURA Healthcare Service/input_Medicaid_programs` | true | 2 |
| 8 | `Page_CURA Healthcare Service/input_Password_password` | true | 2 |
| 9 | `Page_CURA Healthcare Service/input_Username_username` | true | 2 |
| 10 | `Page_CURA Healthcare Service/input_Visit Date (Required)_visit_date` | true | 2 |
| 11 | `Page_CURA Healthcare Service/select_Tokyo CURA Healthcare Center        _5b4107` | true | 2 |
| 12 | `Page_CURA Healthcare Service/td_20` | true | 0 |
| 13 | `Page_CURA Healthcare Service/textarea_Comment_comment` | true | 2 |
| 14 | `Visit Date` | false | 1 |


## Reverse Lookup Detail

| # | Test Object ID | in Repos? | used by Test Case |
| - | -------------- | --------- | ----------------- |
| 1 | `(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[20]` | false | `Test Cases/main/TC_CURA` |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | `Test Cases/main/TC_CURA` |
| 2 | `Page_CURA Healthcare Service/a_Go to Homepage` | true | `Test Cases/main/TC_CURA - more` |
| 3 | `Page_CURA Healthcare Service/a_Make Appointment` | true | `Test Cases/main/TC_CURA` |
| 3 | `Page_CURA Healthcare Service/a_Make Appointment` | true | `Test Cases/main/TC_CURA - more` |
| 4 | `Page_CURA Healthcare Service/button_Book Appointment` | true | `Test Cases/main/TC_CURA` |
| 4 | `Page_CURA Healthcare Service/button_Book Appointment` | true | `Test Cases/main/TC_CURA - more` |
| 5 | `Page_CURA Healthcare Service/button_Login` | true | `Test Cases/main/TC_CURA` |
| 5 | `Page_CURA Healthcare Service/button_Login` | true | `Test Cases/main/TC_CURA - more` |
| 6 | `Page_CURA Healthcare Service/input_Apply for hospital readmission_hospit_63901f` | true | `Test Cases/main/TC_CURA` |
| 6 | `Page_CURA Healthcare Service/input_Apply for hospital readmission_hospit_63901f` | true | `Test Cases/main/TC_CURA - more` |
| 7 | `Page_CURA Healthcare Service/input_Medicaid_programs` | true | `Test Cases/main/TC_CURA` |
| 7 | `Page_CURA Healthcare Service/input_Medicaid_programs` | true | `Test Cases/main/TC_CURA - more` |
| 8 | `Page_CURA Healthcare Service/input_Password_password` | true | `Test Cases/main/TC_CURA` |
| 8 | `Page_CURA Healthcare Service/input_Password_password` | true | `Test Cases/main/TC_CURA - more` |
| 9 | `Page_CURA Healthcare Service/input_Username_username` | true | `Test Cases/main/TC_CURA` |
| 9 | `Page_CURA Healthcare Service/input_Username_username` | true | `Test Cases/main/TC_CURA - more` |
| 10 | `Page_CURA Healthcare Service/input_Visit Date (Required)_visit_date` | true | `Test Cases/main/TC_CURA` |
| 10 | `Page_CURA Healthcare Service/input_Visit Date (Required)_visit_date` | true | `Test Cases/main/TC_CURA - more` |
| 11 | `Page_CURA Healthcare Service/select_Tokyo CURA Healthcare Center        _5b4107` | true | `Test Cases/main/TC_CURA` |
| 11 | `Page_CURA Healthcare Service/select_Tokyo CURA Healthcare Center        _5b4107` | true | `Test Cases/main/TC_CURA - more` |
| 12 | `Page_CURA Healthcare Service/textarea_Comment_comment` | true | `Test Cases/main/TC_CURA` |
| 12 | `Page_CURA Healthcare Service/textarea_Comment_comment` | true | `Test Cases/main/TC_CURA - more` |
| 13 | `Visit Date` | false | `Test Cases/main/TC_CURA - more` |


## Forward Reference Detail

**WARNING**: The information here depends on when and which Test Suite you executed.

| # | Test Case refers | Test Object | in Repos? |
| - | ---------------- | ----------- | --------- |
| 1 | `Test Cases/main/TC_CURA` | `(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[20]` | false |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/a_Go to Homepage` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/a_Make Appointment` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/button_Book Appointment` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/button_Login` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/input_Apply for hospital readmission_hospit_63901f` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/input_Medicaid_programs` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/input_Password_password` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/input_Username_username` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/input_Visit Date (Required)_visit_date` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/select_Tokyo CURA Healthcare Center        _5b4107` | true |
| 1 | `Test Cases/main/TC_CURA` | `Page_CURA Healthcare Service/textarea_Comment_comment` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/a_Go to Homepage` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/a_Make Appointment` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/button_Book Appointment` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/button_Login` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/input_Apply for hospital readmission_hospit_63901f` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/input_Medicaid_programs` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/input_Password_password` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/input_Username_username` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/input_Visit Date (Required)_visit_date` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/select_Tokyo CURA Healthcare Center        _5b4107` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Page_CURA Healthcare Service/textarea_Comment_comment` | true |
| 2 | `Test Cases/main/TC_CURA - more` | `Visit Date` | false |


