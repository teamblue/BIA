package mobileDesktopHandler;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;
import javax.obex.ServerRequestHandler;
import java.io.*;

/**
 * 
 * @author 	Adam Palaniuk
 * @class  	DesktopRequestHandler
 * @purpose Class which functions as OBEX request handler for desktop requests
 * 			Currently outputs data from single request to test throughput from desktop
 */

public class DesktopRequestHandler extends ServerRequestHandler 
{
    public int onPut(Operation operation) {
        try 
        {
        	int data; 															// holds payload data
            HeaderSet headerSet = operation.getReceivedHeaders();
            String requestName = (String) headerSet.getHeader(HeaderSet.NAME);
            
            if (requestName != null) 
                System.out.println("Name of Put Request:" + requestName);

            InputStream inputStream = operation.openInputStream();

            StringBuffer buffer = new StringBuffer();
            
            while ((data = inputStream.read()) != -1) 
            {
                buffer.append((char) data);
            }

            System.out.println("Data Received: " + buffer.toString());

            operation.close();
            
            return ResponseCodes.OBEX_HTTP_OK;
            
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
        }
    }
}