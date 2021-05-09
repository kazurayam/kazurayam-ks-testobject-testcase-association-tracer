package com.kazurayam.ks.testobject

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
public class ObjectRepositoryWrapperTest {

	ObjectRepositoryWrapper instance

	@Before
	void setup() {
		instance = new ObjectRepositoryWrapper()
	}

	@Test
	void test_includes_true() {
		assertTrue(instance.includes("Page_CURA Healthcare Service/a_Make Appointment"))
	}

	@Test
	void test_includes_false1() {
		assertFalse(instance.includes("Appointment"))
	}

	@Test
	void test_includes_false2() {
		assertFalse(instance.includes("foo_bar_baz"))
	}

	@Test
	void test_includes_false3() {
		assertFalse(instance.includes("Visit Date"))
	}
}
