package io.github.oakdh;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.fazecast.jSerialComm.SerialPort;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {   
        try
        {
            SerialPort sp = SerialPort.getCommPort("/dev/ttyUSB0");
            sp.setComPortParameters(9600, 8, 1, 0);
            sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

            if (sp.openPort()) {
                System.out.println("Port is open :)");
                } else {
                System.out.println("Failed to open port :(");
                return;
            }		
            
            while (true)
            {
                if (sp.bytesAvailable() > 0)
                {
                    byte[] readBuffer = new byte[sp.bytesAvailable()];
                    int numRead = sp.readBytes(readBuffer, readBuffer.length);

                    System.out.print(new String(readBuffer));
                    System.out.print("END LOOP");
                }
            }

            // if (sp.closePort()) {
            //     System.out.println("Port is closed :)");
            // } else {
            //     System.out.println("Failed to close port :(");
            //     return;
            // }
        } catch (Exception e) {e.printStackTrace();}
    }
}
