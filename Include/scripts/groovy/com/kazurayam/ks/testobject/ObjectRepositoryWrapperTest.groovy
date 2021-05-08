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
	void test_contains_true() {
		assertTrue(instance.contains("Page_CURA Healthcare Service/a_Make Appointment"))
	}
	
	@Test
	void test_contains_false() {
		assertFalse(instance.contains("foo_bar_baz"))
	}
	
}
