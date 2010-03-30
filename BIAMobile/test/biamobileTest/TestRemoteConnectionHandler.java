package biamobileTest;

import java.io.IOException;
import java.util.Hashtable;

import jmunit.framework.cldc11.TestCase;
import biamobile.RemoteConnectionHandler;

// to run these tests, must run testServer.TestServer first
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
			fail(e.getMessage());
		}
		
		if (response != null)
		{
			String respStr = new String(response);
			System.out.print("\nResponse:" + respStr);
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
			response = rch.performConnectHTTP("GET", new Hashtable(), MobileTestConstants.getTestServerHostName(),
				MobileTestConstants.getServerPort(), MobileTestConstants.getHTTPRequestMessage().getBytes());
		}
		
		catch(IOException e)
		{
			fail(e.getMessage());
		}
		
		if (response != null)
		{
			String respStr = new String(response);
			System.out.print("\nResponse:" + respStr);
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
				//performConnectHTTP1Test();
				break;
		}
	}

}
