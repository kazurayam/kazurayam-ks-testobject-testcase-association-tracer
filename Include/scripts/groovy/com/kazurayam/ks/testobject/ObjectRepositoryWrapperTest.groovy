package com.kazurayam.ks.testobject

import static org.hamcrest.CoreMatchers.*
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

	@Test
	void test_getAllTestObjectIDs_base() {
		Set<String> reposContents = instance.getAllTestObjectIDs()
		assertTrue("reposContents should not be null", reposContents != null)
		assertTrue("reposContents.size() should be >0", reposContents.size() > 0)
	}

	@Test
	void test_getAllTestObjectIDs_contains_td20() {
		Set<String> reposContents = instance.getAllTestObjectIDs()
		assertTrue(reposContents.contains("Page_CURA Healthcare Service/td_20"))
	}

	@Test
	void test_getAllTestObjectIDs_once() {
		Set<String> reposContents = instance.getAllTestObjectIDs()
		int count = 0
		reposContents.each { entry ->
			if (entry == "Page_CURA Healthcare Service/a_Go to Homepage") {
				count += 1
			}
		}
		assertThat("an entry should appear once, no less, no more; but was ${count}", count, is(1))
	}
}
