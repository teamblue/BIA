package biamobileTest;

import jmunit.framework.cldc11.TestSuite;

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
