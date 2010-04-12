package biamobileTest;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import jmunit.framework.cldc11.TestCase;
import biamobile.RemoteConnectionHandler;

/**
 * Tests out the RemoteConnectionHandler class, to make sure requests are properly sent/received.  Must launch BIATestServer before launching this test.
 * @author aaron
 *
 */
public class TestRemoteConnectionHandler extends TestCase
{
	
	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public TestRemoteConnectionHandler()
	{
		super(2, "TestRemoteConnectionHandler");
	}

	public void performConnectTCP1Test()
	{
		RemoteConnectionHandler rch = new RemoteConnectionHandler();
		
		byte[] response = null;
		
		try
		{
			response = rch.performConnectTCP(MobileTestConstants.getTestServerHostName(),
				MobileTestConstants.getServerPort(), MobileTestConstants.getTCPRequestMessage().getBytes());
		}
		
		catch(IOException e)
		{
			System.out.println("IOException:");
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		if (response != null)
		{
			String respStr = new String(response);
			assertTrue(respStr.equals(MobileTestConstants.getExpectedResponse()));
		}
		
		else
		{
			fail("No response");
		}
	}

	public void performConnectHTTP1Test()
	{
		RemoteConnectionHandler rch = new RemoteConnectionHandler();
		
		byte[] response = null;
		
		try
		{
			Hashtable t = new Hashtable();
			t.put("Host", MobileTestConstants.getTestServerHostName() + ":" + MobileTestConstants.getServerPort());
			
			response = rch.performConnectHTTP(HttpConnection.GET, t, MobileTestConstants.getTestServerHostName(), "/",
				MobileTestConstants.getServerPort(), MobileTestConstants.getHTTPRequestMessage().getBytes());
		}
		
		catch(IOException e)
		{
			System.out.println("IOException:");
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		if (response != null)
		{
			String respStr = new String(response);
			assertTrue(respStr.equals(MobileTestConstants.getExpectedResponse()));
		}
		
		else
		{
			fail("No response");
		}
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
				performConnectTCP1Test();
				break;
			case 1:
				performConnectHTTP1Test();
				break;
		}
	}

}
