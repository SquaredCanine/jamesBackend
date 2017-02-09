package nl.indigobeta.james.dispenser.Controllers;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;
import java.io.PrintWriter;

/**
 * Created by jelle on 06/10/2016.
 */
public class ArduinoController
{
    private static SerialPort serialPort;
    private static Scanner data;
    private static PrintWriter output;

    public ArduinoController()
    {
        comSetup();
    }

    /*
     * Opens the given valve for the given amount of milliseconds.
     */
    public void openValve(final int valveNumber, final double milliSeconds)
    {
        //Open a new thread for the arduino communication
        final Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1500);
                }
                catch(Exception e)
                {

                }

                if(serialPort.openPort())
                {
                    output = new PrintWriter(serialPort.getOutputStream());
                }
                //Write the data to the arduino.
                //The date will be written in the String format: "valveNumber,milliSeconds"
                output.write(valveNumber + "," + milliSeconds);
                output.flush();
            }
        };
        thread.start();
    }

    /*
     * Initialises the serial connection to the arduino.
     */
    private static void comSetup()
    {
        serialPort = SerialPort.getCommPort("/dev/ttyACM0");

        try
        {
            if (!serialPort.openPort())
            {
                System.out.println("Unable to open the port.");
            }
            else
            {
                serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                data = new Scanner(serialPort.getInputStream());
                System.out.println("Port opened succesfully.");
            }
        }


        catch (Exception e)
        {
            System.out.println("Something went wrong in the comsetup. " + e);
        }
    }
}
