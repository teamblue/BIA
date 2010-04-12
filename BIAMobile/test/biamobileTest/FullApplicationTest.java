package biamobileTest;

import java.io.IOException;

import com.sun.midp.io.Util;

import biamobile.DesktopRequestHandler;
import jmunit.framework.cldc11.TestCase;

/**
 * Tests out the Full application (sending a request to DesktopRequestHandler).  Must launch BIATestSever before launching this test.
 * @author aaron
 *
 */
public class FullApplicationTest extends TestCase
{

	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public FullApplicationTest()
	{
		super(1, "FullApplicationTest");
	}
	
	private void testRequestResponse()
	{
		DesktopRequestHandler drh = new DesktopRequestHandler();
		
		byte[] response = null;
		
		try
		{
			response = drh.sendRequest(MobileTestConstants.getFullApplicationTestRequestBluetooth().getBytes());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String responseStr = new String(response);
		
		assertEquals(responseStr, MobileTestConstants.getExpectedResponse());
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
				testRequestResponse();
			break;
		}
	}

}
