package io.github.oakdh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.fazecast.jSerialComm.SerialPort;

public class App 
{
    static int PACKET_SIZE = 4 * 4; // bytes
    public static void main( String[] args )
    {   
        try
        {
            SerialPort sp = SerialPort.getCommPort("COM4");
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
                if (sp.bytesAvailable() % PACKET_SIZE == 0)
                {
                    int packetCount = sp.bytesAvailable() / PACKET_SIZE;

                    for (int i = 0; i < packetCount; i++)
                    {
                        byte[] readBuffer = new byte[PACKET_SIZE];

                        System.out.println(ByteBuffer.wrap(readBuffer, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
                        System.out.println(ByteBuffer.wrap(readBuffer, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
                        System.out.println(ByteBuffer.wrap(readBuffer, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
                        System.out.println(ByteBuffer.wrap(readBuffer, 12, 4).getInt());

                        System.out.print("END LOOP\n");
                    }
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
