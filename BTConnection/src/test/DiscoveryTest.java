package test;

import javax.bluetooth.BluetoothStateException;

import org.junit.*;
import btclient.BluetoothDiscoverer;
import btcommon.Constants;

public class DiscoveryTest 
{
  private BluetoothDiscoverer discoverer;
  
  @Before
  public void setUp() throws BluetoothStateException
  {
    discoverer = new BluetoothDiscoverer();
  }
  
  @After
  public void tearDown()
  {
    // Nothing to do here
  }
  
  @Test 
  public void testDeviceDiscovery() throws BluetoothStateException
  {          
    discoverer.discoverDevices();
    Assert.assertTrue( "Discoverer found no Bluetooth devices.  This is OK as long as there are actually no devices in range", discoverer.getNumDevicesDiscovered() > 0 );      
  }
  
  @Test
  public void testServiceDiscovery() throws BluetoothStateException
  {
    discoverer.discoverServices( Constants.SERVICE_UUID );
    Assert.assertTrue( "Discoverer failed to find the tethering service.  This is OK as long as such service doesn't actually exist", discoverer.doesServiceExist() );
  }
}
