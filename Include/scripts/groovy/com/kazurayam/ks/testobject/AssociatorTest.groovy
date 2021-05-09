package com.kazurayam.ks.testobject

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.testobject.TestObject

@RunWith(JUnit4.class)
public class AssociatorTest {

	@Ignore
	@Test
	void test_TestObject_created_on_the_fly() {
		String testObjectId = "test_modifyTestObject"
		TestObject tObj = new TestObject(testObjectId)
		Set<String> callees = AssociationTracer.getInstance().allCallees()
		assertTrue("callees: ${callees} does not contain \"${testObjectId}\"",
				callees.contains(testObjectId))
	}


	@Test
	void test_TestObject_in_the_Object_Repository() {
		String testObjectId = "Page_CURA Healthcare Service/a_Go to Homepage"
		TestObject tObj = findTestObject(testObjectId)
		assertTrue("tObj should not be null, but was", tObj != null)
		//println "tObj.toString(): ${tObj.toString()}"
		Set<String> callees = AssociationTracer.getInstance().allCallees()
		assertTrue("callees: ${callees} does not contain \"${testObjectId}\"",
				callees.contains(testObjectId))
	}
}
