package mobileDesktopHandler;
// Interface required for object to implement callback

public interface BluetoothRequest 
{

	public byte[] dataRequested(byte[] data, int bytes);
	
}
