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

public class WavClient {
    static void WAVCLIENT(File file, boolean spook, int start, int end) throws Exception {

        //Making a connection with the server
        Socket echo = new Socket("localhost", 3500);
        DataInputStream br = new DataInputStream(echo.getInputStream());
        DataOutputStream dos = new DataOutputStream(echo.getOutputStream());

        //Sending the WAV file
        FileInputStream fis = new FileInputStream(file);
            //FileInputStream fis = new FileInputStream("Freddie_Mercury-The_Great_Pretender_WAV.wav");
            //FileInputStream fis = new FileInputStream("Billy_Joel-Vienna.wav");

        byte[] buffer = new byte[4096];

        //sending start and end times for trimming and a bool on whether to add spooky effect or not to the Server
        dos.writeInt(start);
        dos.flush();
        dos.writeInt(end);
        dos.flush();
        dos.writeBoolean(spook);

        //Writing the whole wave file to the server
        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }
        br.close();
        fis.close();
        dos.close();
        echo.close();


        //Receiving the edited file
        //(Basically the Client right now has become the "Server" and is receiving the edited file from the Server (which is acting like a client when it is sending))
        try {
            ServerSocket serverSocket2 = new ServerSocket(4300);
            Socket clientSocket = serverSocket2.accept();


            File file7 = new File("src\\Client_WAV_file.wav");
            File file8 = new File("Client_WAV_file.wav");
            //cleaning files so there are no conflicts
            file7.delete();
            file8.delete();

            //This file transfer code I found from this link: https://gist.github.com/CarlEkerot/2693246
            DataInputStream dis3 = new DataInputStream(clientSocket.getInputStream());
            FileOutputStream fos3 = new FileOutputStream("src\\Client_WAV_file.wav");
            byte[] buffer3 = new byte[4096];

            int filesize = 2147483647;
            int read = 0;
            int totalRead = 0;
            int remaining = filesize;
            while((read = dis3.read(buffer3, 0, Math.min(buffer3.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                fos3.write(buffer3, 0, read);
            }
                //System.out.println("read " + totalRead + " bytes.");

            fos3.close();
            dis3.close();
            clientSocket.close();
            serverSocket2.close();

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port 4300 or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}