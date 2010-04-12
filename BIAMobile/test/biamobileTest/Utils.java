package biamobileTest;

/**
 * Utility method.  Need to implement this as JMUnit did not support assertEquals(byte arrays).
 * 	(Would simply compare the values of the pointers instead of the arrays).
 * @author aaron
 *
 */
public class Utils
{
	// tests if two byte arrays are equal
	/**
	 * Tests if two byte arrays are equal
	 * @param  a
	 * @param  b
	 * 
	 * @return True if equal, false otherwise.
	 */
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
