package tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests each of the individual test suites.
 * 
 * @author Samuel Pauls
 */
public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		// Add each file of test cases here.
		suite.addTestSuite(LibraryTests.class);
		suite.addTestSuite(HTTPObjectTests.class);
		suite.addTestSuite(ConnectionTests.class);
		
		return suite;
	}
}
