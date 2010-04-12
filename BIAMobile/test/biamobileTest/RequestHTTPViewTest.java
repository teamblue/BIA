package biamobileTest;

import java.util.Hashtable;

import biamobile.DesktopRequestHandler;
import biamobile.RequestHTTPView;
import jmunit.framework.cldc11.TestCase;

/**
 * Tests out the RequestHTTPView code.
 * @author aaron
 *
 */
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
		super(7, "RequestHTTPViewTest");
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
		httpView = new RequestHTTPView("GET http://www.google.ca:877/ HTTP/1.0 \r\n\r\n".getBytes());
		actualResponse = httpView.getRemoteHost();
		expectedResponse = "www.google.ca";
		assertEquals(expectedResponse, actualResponse);
		
		// test 4
		httpView = new RequestHTTPView("GET http://www.google.ca/more/even_more/index.html HTTP/1.0 \r\n\r\n".getBytes());
		actualResponse = httpView.getRemoteHost();
		expectedResponse = "www.google.ca";
		assertEquals(expectedResponse, actualResponse);
		
		// test 5
		httpView = new RequestHTTPView("GET http://www.google.ca:899/more/even_more/index.html HTTP/1.0 \r\n\r\n".getBytes());
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
		
		// test 1
		dataToSend = "GET http://localhost HTTP/1.0 \r\n\r\n".getBytes();
		httpView = new RequestHTTPView(dataToSend);
		assertTrue(Utils.byteArrayEqual(dataToSend, httpView.getRawData()));
	}
	
	public void testGetRequestData()
	{
		RequestHTTPView httpView;
		
		byte[] dataToSend;
		byte[] expectedData;
		
		// test null
		httpView = new RequestHTTPView(null);
		assertNull(httpView.getRequestData());
		
		// test 1
		dataToSend = "GET http://localhost/ HTTP/1.0 \r\n\r\n".getBytes();
		expectedData = "GET / HTTP/1.0 \r\n\r\n".getBytes();
		httpView = new RequestHTTPView(dataToSend);
		assertTrue(Utils.byteArrayEqual(expectedData, httpView.getRequestData())); // have to use byteArrayEqual as assertEquals(byte[], byte[]) just checks if pointers are same
		
		// test 2
		dataToSend = "GET http://www.google.ca/page/index.html HTTP/1.0 \r\n\r\n".getBytes();
		expectedData = "GET /page/index.html HTTP/1.0 \r\n\r\n".getBytes();
		httpView = new RequestHTTPView(dataToSend);
		assertTrue(Utils.byteArrayEqual(expectedData, httpView.getRequestData()));
		
		// test 3
		dataToSend = "GET http://www.google.ca:866/page/index.html HTTP/1.0 \r\n\r\n".getBytes();
		expectedData = "GET /page/index.html HTTP/1.0 \r\n\r\n".getBytes();
		httpView = new RequestHTTPView(dataToSend);
		assertTrue(Utils.byteArrayEqual(expectedData, httpView.getRequestData()));
	}
	
	public void getRemotePortTest()
	{
		RequestHTTPView httpView;
		
		int expectedPort;
		
		// test null
		httpView = new RequestHTTPView(null);
		expectedPort = 80; // defaults to 80
		assertEquals(expectedPort, httpView.getRemotePort());
		
		// test 1
		httpView = new RequestHTTPView("GET http://localhost HTTP/1.0 \r\n\r\n".getBytes());
		expectedPort = 80; // defaults to 80
		assertEquals(expectedPort, httpView.getRemotePort());
		
		// test 2
		httpView = new RequestHTTPView("GET http://www.google.ca/ HTTP/1.0 \r\n\r\n".getBytes());
		expectedPort = 80; // defaults to 80
		assertEquals(expectedPort, httpView.getRemotePort());
		
		// test 3
		httpView = new RequestHTTPView("GET http://www.google.ca:888/ HTTP/1.0 \r\n\r\n".getBytes());
		expectedPort = 888;
		assertEquals(expectedPort, httpView.getRemotePort());
		
		// test 4
		httpView = new RequestHTTPView("GET http://www.google.ca:899/a/page.html HTTP/1.0 \r\n\r\n".getBytes());
		expectedPort = 899;
		assertEquals(expectedPort, httpView.getRemotePort());
	}

	public void getHeadersTest()
	{
		RequestHTTPView httpView;
		Hashtable extractedHash;
		
		String headerOne = "Connection";
		String headerTwo = "User-agent";
		String expectedValueOne = "close";
		String expectedValueTwo = "Mozilla";
		
		httpView = new RequestHTTPView("GET http://localhost HTTP/1.0 \r\nConnection: close\r\nUser-agent: Mozilla\r\n\r\n".getBytes());
		extractedHash = httpView.getHeaderHash();
		
		assertTrue(expectedValueOne.equals(extractedHash.get(headerOne)));
		assertTrue(expectedValueTwo.equals(extractedHash.get(headerTwo)));
	} 
	
	public void getHTTPMethodTest()
	{
		RequestHTTPView httpView;
		String expectedMethod;
		
		httpView = new RequestHTTPView("GET http://localhost HTTP/1.0 \r\nConnection: close\r\nUser-agent: Mozilla\r\n\r\n".getBytes());
		expectedMethod = "GET";
		assertEquals(expectedMethod, httpView.getHTTPMethod());
		
		httpView = new RequestHTTPView("POST http://localhost HTTP/1.0 \r\nConnection: close\r\nUser-agent: Mozilla\r\n\r\n".getBytes());
		expectedMethod = "POST";
		assertEquals(expectedMethod, httpView.getHTTPMethod());
	} 
	
	public void getEntityBodyTest()
	{
		RequestHTTPView httpView;
		byte[] entityBody;
		
		String expectedBody = "There would normally be data here.";
		
		httpView = new RequestHTTPView("GET http://localhost HTTP/1.0 \r\nConnection: close\r\nUser-agent: Mozilla\r\n\r\nThere would normally be data here.".getBytes());
		entityBody = httpView.getEntityBody();
		
		assertTrue(Utils.byteArrayEqual(expectedBody.getBytes(), entityBody));
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
			
			case 3:
				getRemotePortTest();
			break;
			
			case 4:
				getHeadersTest();
			break;
			
			case 5:
				getEntityBodyTest();
			break;
			
			case 6:
				getHTTPMethodTest();
			break;
		}
	}

}
