package btserver;

public interface BluetoothRequest
{
  // The method called when data was received
  //public void dataReceived( byte[] data, int bytes );
  
  // The method called when data was requested
  public byte[] dataRequested( byte[] data, int bytes );
}

