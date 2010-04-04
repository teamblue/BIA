package test;

import javax.bluetooth.BluetoothStateException;

import org.junit.*;

import btclient.SimpleBluetoothClient;

/*
 *  This test relies on:
 *  - the bt emu server running
 *  - the btserver running
 */
public class TestSimpleBluetoothClient {

	SimpleBluetoothClient client;

	@Before
	public void setUp() throws BluetoothStateException {
		client = new SimpleBluetoothClient();
		Assert.assertNull("no client yet", client.client);
		Assert.assertFalse("not yet connected", client.connected);
		client.setup();
		Assert.assertNotNull("client", client.client);
		Assert.assertTrue("not yet connected", client.connected);
		// Nothing to do here
	}

	@After
	public void tearDown() {
		client.tearDown();
		Assert.assertFalse("no longer connected", client.connected);
	}

	@Test
	public void testSimpleWrite() throws BluetoothStateException {
		String result = client.write("http://127.0.0.1:4567");
		Assert.assertNotNull(result);
		System.out.println(result);
		Assert.assertTrue("should have expected result:", "hi".equals(result.trim()));
	}
}