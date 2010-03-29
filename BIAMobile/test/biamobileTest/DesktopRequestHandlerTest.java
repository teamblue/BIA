package biamobileTest;

import biamobile.DesktopRequestHandler;
import jmunit.framework.cldc11.TestCase;

public class DesktopRequestHandlerTest extends TestCase
{

	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public DesktopRequestHandlerTest()
	{
		super(1, "DesktopRequestHandlerTest");
	}
	
	public void testExtractRemoteHost()
	{
		DesktopRequestHandler drh = new DesktopRequestHandler();
		
		String actualResponse;
		String expectedResponse;
		
		// test null
		actualResponse = drh.extractRemoteHost(null);
		expectedResponse = null;
		assertNull(actualResponse);
		
		// test 1
		actualResponse = drh.extractRemoteHost("GET http://localhost HTTP/1.0 \r\n\r\n".getBytes());
		expectedResponse = "localhost";
		assertEquals(expectedResponse, actualResponse);
		
		// test 2
		actualResponse = drh.extractRemoteHost("GET http://www.google.ca/ HTTP/1.0 \r\n\r\n".getBytes());
		expectedResponse = "www.google.ca";
		assertEquals(expectedResponse, actualResponse);
		
		// test 3
		actualResponse = drh.extractRemoteHost("GET http://www.google.ca/more/even_more/index.html HTTP/1.0 \r\n\r\n".getBytes());
		expectedResponse = "www.google.ca";
		assertEquals(expectedResponse, actualResponse);
	}	

	/**
	 * This method stores all the test methods invocation. The developer must
	 * implement this method with a switch-case. The cases must start from 0 and
	 * increase in steps of one until the number declared as the total of tests
	 * in the constructor, exclusive. For example, if the total is 3, the cases
	 * must be 0, 1 and 2. In each case, there must be a test method invocation.
	 * 
	 * @param testNumber the test to be executed.
	 * @throws Throwable anything that the executed test can throw.
	 */
	public void test(int testNumber) throws Throwable
	{
		switch (testNumber)
		{
			case 0:
				testExtractRemoteHost();
			break;
		}
	}

}
