package testinternet;

import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * HT: This class is a place-holder for the mobile application team.  
 * 
 * Given a URL, this class retrieves the content of the URL and return all of it as a string.
 */
public class RequestHandler 
{
  public RequestHandler()
  {
    
  }
  
  public byte[] fetch( String address )
  {
    byte[] data = null;
    
    /*try
    {
      URL             url = new URL( address );
      BufferedReader  bufferedReader = new BufferedReader( new InputStreamReader( url.openStream() ) );
      String          line;
      StringBuffer    buffer = new StringBuffer();
            
      while ( ( line = bufferedReader.readLine() ) != null ) 
      {
        buffer.append( line );
      }           
      
      data = buffer.toString().getBytes();
    }
    catch ( MalformedURLException e )
    {
      System.out.println( "Status" );
      System.out.println( "Exception: " + e.getMessage() );
    } 
    catch ( IOException e )
    {
      System.out.println( "Exception: " + e.getMessage() );
    }
    
    return data;
    */
    return "OMG WTF".getBytes();
  }
}
