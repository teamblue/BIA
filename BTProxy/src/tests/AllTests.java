package tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests each of the individual test suites.
 */
public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		// Add each file of test cases here.
		suite.addTestSuite(LibraryTests.class);
		suite.addTestSuite(HTTPObjectTests.class);

		return suite;
	}
}
