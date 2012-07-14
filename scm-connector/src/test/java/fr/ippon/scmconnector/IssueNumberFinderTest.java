package fr.ippon.scmconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IssueNumberFinderTest {

	@Test
	public void testIssueNumberFinding() {
		testSuccess(123, "#123 Comment...");
		testSuccess(456, "Start #456 End");
		testSuccess(789, "Foo#789Bar");
		testSuccess(147, "#147, #258 Multiples...");
		
		testFailure("Nothing");
		testFailure("123 Test");
	}

	private void testSuccess(long expectedNumber, String message) {
		assertEquals(Long.valueOf(expectedNumber), IssueNumberFinder.find(message));
	}

	private void testFailure(String message) {
		assertNull(IssueNumberFinder.find(message));
	}
}
