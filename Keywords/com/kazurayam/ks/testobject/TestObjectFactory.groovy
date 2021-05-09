package com.kazurayam.ks.testobject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject


public class TestObjectFactory {

	private TestObjectFactory() {}

	static TestObject createTestObjectByXPath(String xpath) {
		return createTestObjectByXPath(xpath, xpath)
	}

	static TestObject createTestObjectByXPath(String id, String xpath) {
		TestObject tObj = new TestObject(id)
		tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
		return tObj
	}

	static TestObject createTestObjectByCSS(String selector) {
		return createTestObjectByCSS(selector, selector)
	}

	static TestObject createTestObjectByCSS(String id, String selector) {
		TestObject tObj = new TestObject(id)
		tObj.addProperty("css", ConditionType.EQUALS, selector)
		return tObj
	}
}
