package test;

public class Request implements btserver.BluetoothRequest
{
  public Request()
  {
  }
  
  /*public void dataReceived(byte[] data, int bytes)
  {
  System.out.println( "Data received." );
  }*/

  public byte[] dataRequested(byte[] data, int bytes)
  {
    System.out.println( "Data requested" );
    
    return data;
  }
}