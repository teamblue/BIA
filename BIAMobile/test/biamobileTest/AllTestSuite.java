package biamobileTest;

import jmunit.framework.cldc11.TestSuite;

/**
 * Suite of all tests for Mobile Application.  In order to run this, you must first run the BIATestServer (the ApplicationTest relies on this server to run).
 * @author aaron
 *
 */
public class AllTestSuite extends TestSuite
{

	/**
	 * TestSuite Class constructor initializes the test suite.
	 */
	public AllTestSuite()
	{
		super("AllTestSuite");
		this.setupSuite();
	}

	/**
	 * This method adds all suite test cases to be run.
	 */
	private void setupSuite()
	{
		//JMUnit-BEGIN
		add(new biamobileTest.RequestHTTPViewTest());
		add(new biamobileTest.TestRemoteConnectionHandler());
		add(new biamobileTest.FullApplicationTest());
		//JMUnit-END
	}

}
