package com.kazurayam.ks.testobject

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.testobject.TestObject

@RunWith(JUnit4.class)
public class AssociatorTest {

	@Test
	void test_modifyTestObject_onthefly() {
		String testObjectId = "test_modifyTestObject"
		TestObject tObj = new TestObject(testObjectId)
		List<String> callees = AssociationTracer.getInstance().calleesAll()
		assertTrue("callees: ${callees} does not contain \"${testObjectId}\"", callees.contains(testObjectId))
	}
	
	@Test
	void test_TestObject_ObjectRepository() {
		String testObjectId = "Page_CURA Healthcare Service/a_Go to Homepage"
		TestObject tObj = findTestObject(testObjectId)
		List<String> callees = AssociationTracer.getInstance().calleesAll()
		assertTrue("callees: ${callees} does not contain \"${testObjectId}\"", callees.contains(testObjectId))
	}
}
