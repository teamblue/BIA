package btcommon;

final public class Constants 
{
  private Constants()
  {
    // Intentionally private, so that the class can't be instantiated
  }
  
  public static final String  SERVICE_UUID        = "E6FEC3B275744C079B2F8883DBE38937";
  public static final int     TRANSFER_MAX_BYTES  = 100;
  public static final String  END_TRANSFER_CODE   = "-BT?T!END!T?BT-";  
  
  public static final boolean containsEndingCode( String line )
  {
    return line.indexOf( Constants.END_TRANSFER_CODE ) != -1;
  }
}
