package com.kazurayam.ks.testobject


import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
public class AssociationTracerTest {

	AssociationTracer tracer

	@Before
	void setup() {
		tracer = AssociationTracer.getInstance()
		tracer.trace("testCaseX", "testObjectY")
	}

	@Test
	void test_callersAll() {
		assertTrue(tracer.callersAll().contains("testCaseX"))
	}

	@Test
	void test_calleesAll() {
		assertTrue(tracer.calleesAll().contains("testObjectY"))
	}

	@Test
	void test_callersOf() {
		assertTrue(tracer.callersOf("testObjectY").contains("testCaseX"))
	}

	@Test
	void test_calleesBy() {
		assertTrue(tracer.calleesBy("testCaseX").contains("testObjectY"))
	}
}
