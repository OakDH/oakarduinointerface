package io.github.oakdh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

import org.json.JSONObject;

import com.fazecast.jSerialComm.SerialPort;

public class App 
{
    static int PACKET_SIZE = 4 * 3 + 2; // bytes

    static boolean SAVE_TO_DB = false;

    public static void main( String[] args )
    {   
        //HTTPHandler.init();

        try
        {
            SerialPort sp = SerialPort.getCommPort("/dev/ttyUSB0");
            sp.setComPortParameters(9600, 8, 1, 0);
            sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, Integer.MAX_VALUE, 0);

            if (sp.openPort()) {
                System.out.println("Port is open :)");
            } else {
                System.out.println("Failed to open port :(");
                return;
            }		
            
            while (true)
            {
                byte[] readBuffer = new byte[PACKET_SIZE];
                sp.readBytes(readBuffer, readBuffer.length);

                float temperature =     ByteBuffer.wrap(readBuffer, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                float humidity =        ByteBuffer.wrap(readBuffer, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                float soil_moisture =   ByteBuffer.wrap(readBuffer, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                short id =              ByteBuffer.wrap(readBuffer, 12, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();

                System.out.printf("Data for box #%d:\n Temp:\t\t %f,\n Hum:\t\t %f,\n SoilM:\t\t %f\n\n", id, temperature, humidity, soil_moisture);

                if (SAVE_TO_DB)
                {
                    JSONObject response =  HTTPHandler.sendMessage(String.format(Locale.US, "save_measurements/%f/%f/%f/%d/%d", temperature, humidity, soil_moisture, id, System.currentTimeMillis() / 1000L));

                    if (response.getInt("status") == 0)
                    {
                        System.out.println(" Successfully saved to database.");
                    }
                    else
                    {
                        System.out.println(" Error saving to database.");
                    }
                }
                
                System.out.print("END LOOP\n\n");
            }

            // if (sp.closePort()) {
            //     System.out.println("Port is closed :)");
            // } else {
            //     System.out.println("Failed to close port :(");
            //     return;
            // }
        } catch (Exception e) {e.printStackTrace();}
    }

    public void sendPacketToServer(float f, float h, float s, short i)
    {
        
    }
}
