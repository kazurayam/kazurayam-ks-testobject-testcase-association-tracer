# TestObject Usage Report

- TestSuite: Test Suites/main/TS_CURA

## Test Object Reference Count

| # | Test Object ID | in Repos? | reference count |
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
