package test;

public class Request implements bluetooth.BluetoothRequest 
{
	public Request()
	{
	}
	
	public byte[] dataRequested(byte[] data, int bytes)
	{
		System.out.println( "Data requested" );
			
		return data;
	}
}
