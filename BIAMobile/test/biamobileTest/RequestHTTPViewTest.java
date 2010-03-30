package biamobileTest;

import biamobile.DesktopRequestHandler;
import biamobile.RequestHTTPView;
import jmunit.framework.cldc11.TestCase;

public class RequestHTTPViewTest extends TestCase
{

	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public RequestHTTPViewTest()
	{
		super(3, "RequestHTTPViewTest");
	}
	
	public void testExtractRemoteHost()
	{
		RequestHTTPView httpView;
		
		String actualResponse;
		String expectedResponse;
		
		// test null
		httpView = new RequestHTTPView(null);
		actualResponse = httpView.getRemoteHost();
		expectedResponse = null;
		assertNull(actualResponse);
		
		// test 1
		httpView = new RequestHTTPView("GET http://localhost HTTP/1.0 \r\n\r\n".getBytes());
		actualResponse = httpView.getRemoteHost();
		expectedResponse = "localhost";
		assertEquals(expectedResponse, actualResponse);
		
		// test 2
		httpView = new RequestHTTPView("GET http://www.google.ca/ HTTP/1.0 \r\n\r\n".getBytes());
		actualResponse = httpView.getRemoteHost();
		expectedResponse = "www.google.ca";
		assertEquals(expectedResponse, actualResponse);
		
		// test 3
		httpView = new RequestHTTPView("GET http://www.google.ca/more/even_more/index.html HTTP/1.0 \r\n\r\n".getBytes());
		actualResponse = httpView.getRemoteHost();
		expectedResponse = "www.google.ca";
		assertEquals(expectedResponse, actualResponse);
	}
	
	public void testGetRawData()
	{
		RequestHTTPView httpView;
		
		byte[] dataToSend;
		
		// test null
		httpView = new RequestHTTPView(null);
		assertNull(httpView.getRawData());
		
		dataToSend = "GET http://localhost HTTP/1.0 \r\n\r\n".getBytes();
		
		httpView = new RequestHTTPView(dataToSend);
		assertEquals(dataToSend, httpView.getRawData());
	}
	
	// tests if two byte arrays are equal
	private boolean byteArrayEqual(byte[] a, byte[] b)
	{
		if (a == b) return true;
		if (a == null || b == null) return false; // not both null, since this was checked by previous test
		
		if (a.length != b.length)
		{
			return false;
		}
		for (int i = 0; i < a.length; i++)
		{
			if (a[i] != b[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void testGetRequestData()
	{
		RequestHTTPView httpView;
		
		byte[] dataToSend;
		byte[] expectedData;
		
		// test null
		httpView = new RequestHTTPView(null);
		assertNull(httpView.getRequestData());
		
		dataToSend = "GET http://localhost/ HTTP/1.0 \r\n\r\n".getBytes();
		expectedData = "GET / HTTP/1.0 \r\n\r\n".getBytes();
		
		httpView = new RequestHTTPView(dataToSend);
		
		assertTrue(byteArrayEqual(expectedData, httpView.getRequestData())); // have to use byteArrayEqual as assertEquals(byte[], byte[]) just checks if pointers are same
		
		dataToSend = "GET http://www.google.ca/page/index.html HTTP/1.0 \r\n\r\n".getBytes();
		expectedData = "GET /page/index.html HTTP/1.0 \r\n\r\n".getBytes();
		
		httpView = new RequestHTTPView(dataToSend);
		assertTrue(byteArrayEqual(expectedData, httpView.getRequestData()));
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
			
			case 1:
				testGetRawData();
			break;
			
			case 2:
				testGetRequestData();
			break;
		}
	}

}
