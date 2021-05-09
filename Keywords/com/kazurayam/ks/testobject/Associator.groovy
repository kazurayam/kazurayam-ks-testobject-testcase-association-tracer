package com.kazurayam.ks.testobject

import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

import internal.GlobalVariable

public class Associator {

	public static final String GLOBALVARIABLE_CURRENT_TESTCASEID  = "CURRENT_TESTCASEID__"

	private Boolean hasModifiedKatalonClasses = false

	public Associator() {}

	/**
	 * 
	 * @param testSuiteContext
	 */
	public void beforeTestSuite(TestSuiteContext testSuiteContext) {
		//println "Associator::beforeTestSuite() was called by ${testSuiteContext.getTestSuiteId()}"
		this.hasModifiedKatalonClasses = modifyKatalonClasses()
	}

	/**
	 * 
	 * @param testCaseContext
	 */
	public void beforeTestCase(TestCaseContext testCaseContext) {
		//println "Associator::beforeTestCase() was called by ${testCaseContext.getTestCaseId()}"
		ExpandoGlobalVariable.addGlobalVariable(
				GLOBALVARIABLE_CURRENT_TESTCASEID, testCaseContext.getTestCaseId())
		if (! hasModifiedKatalonClasses) {
			// TestClass was executed immediately without wrapping TestSuite
			this.hasModifiedKatalonClasses = modifyKatalonClasses()
		}
	}

	public static AssociationTracer getTracer() {
		return AssociationTracer.getInstance()
	}

	/**
	 * modify methods of Katalon-builtin classes
	 * - com.kms.katalon.core.testobject.ObjectRepository::findTestObject(String testObjectRelativeId)
	 * - com.kms.katalon.core.testobject.TestObject::constructor
	 * 
	 * so that these methods notify the AssociationTracer of the pair of TestCaseID and TestObjectID
	 * when invoked; effectively the AssociationTracer can record which TestCase used which TestObject.
	 */
	/**
	 * The source code of Katalon API is here:
	 * - TestObject https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/testobject/TestObject.java
	 * - ObjectRepository https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/testobject/ObjectRepository.java
	 * 
	 * Learned the Groovy Metaprogramming at
	 * - https://stackoverflow.com/questions/5907432/groovy-adding-code-to-a-constructor
	 * - https://www.baeldung.com/groovy-metaprogramming
	 */
	Boolean modifyKatalonClasses() {
		AssociationTracer tracer = AssociationTracer.getInstance()
		//
		ObjectRepository.metaClass.'static'.invokeMethod = { String methodName, args ->
			String msgPrefix = "[Associator::modifyKatalonClasses] Caller \'${GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASEID]}\'"
			if (methodName == "findTestObject") {
				String testObjectId = ObjectRepository.getTestObjectId(args[0])
				//println msgPrefix + " called \'ObjectRepository.findTestObject(\"${testObjectId}\", ...)\'"
				// notify the AssociationTracer singleton instance
				// of the pair of (TestCaseId, TestObjectId)
				tracer.trace(GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASEID], testObjectId)
			}
			return delegate.metaClass.getMetaMethod(methodName, args).invoke(delegate, args)
		}
		//
		TestObject.metaClass.constructor = { String testObjectId ->
			String msgPrefix = "[Associator::modifyKatalonClasses] Caller \'${GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASEID]}\'"
			//println msgPrefix + " called \'new TestObject(\"${testObjectId}\")\'"
			// notify the AssociationTracer singleton instance
			// of the pair of (TestCaseId, TestObjectId)
			tracer.trace(GlobalVariable[GLOBALVARIABLE_CURRENT_TESTCASEID], testObjectId)
			// use reflection to get the original constructor
			def constructor = TestObject.class.getConstructor(String.class)
			// create the new instance and return it just as the original constructor does
			return constructor.newInstance(testObjectId)
		}
		return true
	}
}
