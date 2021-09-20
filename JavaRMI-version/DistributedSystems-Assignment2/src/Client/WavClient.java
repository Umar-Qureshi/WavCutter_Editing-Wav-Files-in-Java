package Client;
/*
DO NOT USE HEADPHONES, I am sure headphones will work fine but I do not want to risk it

Name: Umar Qureshi
Student Number: 100591742
Course: SOFE 4790U Distributed Systems
Professor: Dr. Qusay Mahmoud

Sources I have used:
The Code that Dr. Q has provided on Canvas, specifically MTEchoServer.java
File transfer code: https://gist.github.com/CarlEkerot/2693246
 */

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;

import seperate.FileInterface;

public class WavClient {
    //file is song file, spook is for spook sound effect, start end is the duration of the song user wants
    public static void WAVCLIENT(File file, boolean spook, int start, int end) throws Exception {

        //Get machine name
        String machineName = InetAddress.getLocalHost().getHostName();

        try {
            //connect to registry and use fileinterface to use methods in WavServer
            Registry registry = LocateRegistry.getRegistry(machineName);
            FileInterface fi = (FileInterface) registry.lookup("FileServer");

            //receiving the song user chose from Main class
            String fileName = file.getName();
            byte buffer[] = new byte[(int)file.length()];

            //buffer stream to upload song file to WavServer to be edited
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            input.read(buffer,0,buffer.length);
            input.close();
            //using the fileInterface to call the uploadFile function to send song and other parameters
            fi.uploadFile(buffer, spook, start, end);

            System.out.println("Successfully sent "+fileName);
        } catch(Exception e) {
            System.err.println("FileServer exception: "+ e.getMessage());
            e.printStackTrace();
        }
        //sleep for one second to ensure edited file is saved before attempting to retrieve it
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        try {
            String name = "//" + machineName + "/FileServer";
            FileInterface fi = (FileInterface) Naming.lookup(name);

            //retrieve Client_WAV_file.wav
            byte[] filedata = fi.downloadFile("Client_WAV_file.wav");

            //output the buffer stream into the a file
            File clientFile = new File("Client_WAV_file.wav");
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(clientFile.getName()));
            output.write(filedata,0, filedata.length);
            output.flush();
            output.close();
            System.out.println("Successfully retrieved "+clientFile);
        } catch(Exception e) {
            System.err.println("FileServer exception: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}
