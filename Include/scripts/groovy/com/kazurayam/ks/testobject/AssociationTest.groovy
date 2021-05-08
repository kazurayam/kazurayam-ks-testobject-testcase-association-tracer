package com.kazurayam.ks.testobject

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
public class AssociationTest {

	Association assoc

	@Before
	void setup() {
		assoc = new Association("testCaseX", "testObjectY")
	}

	@Test
	void test_getCaller() {
		assertThat("testCaseX", is(assoc.getCaller()))
	}

	@Test
	void test_getCallee() {
		assertThat("testObjectY", is(assoc.getCallee()))
	}

	@Test
	void test_equals() {
		Association other = new Association("testCaseX", "testObjectY")
		assertThat(other, is(assoc))
	}

	@Test
	void test_toString() {
		String str = assoc.toString()
		assertTrue(str.contains("caller"))
		assertTrue(str.contains("testCaseX"))
		assertTrue(str.contains("callee"))
		assertTrue(str.contains("testObjectY"))
	}
}
