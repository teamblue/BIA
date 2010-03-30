package biamobileTest;

public class Utils
{
	// tests if two byte arrays are equal
	public static boolean byteArrayEqual(byte[] a, byte[] b)
	{
		if (a == b) return true;
		if (a == null || b == null) return false; // not both null, since this was checked by previous test
		
		if (a.length != b.length)
		{
			return false;
		}
		for (int i = 0; i < a.length; i++)
		{
			if (a[i] != b[i])
			{
				return false;
			}
		}
		
		return true;
	}
}
