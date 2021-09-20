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

//I have placed the WavServer in a seperate folder to prove they are using sockets to communicate and to keep everything organized
package seperate;

import java.net.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.sound.sampled.*;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

//Class to make the connection to the client and then send the task off to a thread
public class WavServer {
    public static void main(String[] args) {

        int portNumber = 3500;
        WavServer es = new WavServer();
        es.run(portNumber);
     }
     public void run(int portNumber) {
        try {
            //Listening on port 3500 to make a connection with the client
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while(true) {
               Socket client = serverSocket.accept();
               Connection cc = new Connection(client);
            }
        } catch(Exception e) {
           System.out.println("Exception: "+e);
        }
    }
}

class Connection extends Thread {
    //make an object client which is gained from every individual Socket
    Socket client;
    PrintWriter out;
    BufferedReader in;

    public Connection(Socket s) { // constructor
       client = s;
       try {
           out = new PrintWriter(client.getOutputStream(), true);
           in = new BufferedReader(new InputStreamReader(client.getInputStream()));
       } catch (IOException e) {
           try {
             client.close();
           } catch (IOException ex) {
             System.out.println("Error while getting socket streams.."+ex);
           }
           return;
       }
        this.start(); // Thread starts here...this start() will call run()
    }

    public void run() {
      try {
          Path parentPath = FileSystems.getDefault().getPath(new String("./")).toAbsolutePath().getParent();
          DataInputStream br = new DataInputStream(client.getInputStream());

          //Reading the variables from Client
          int start = br.readInt();
          int end = br.readInt();
          boolean spook = br.readBoolean();

          //Doing some cleaning to avoid conflict
          File file11 = new File(parentPath+"\\seperate\\Server_WAV_file2.wav");
          File file22 = new File(parentPath + "\\seperate\\Server_WAV_file3.wav");
          file11.delete();
          file22.delete();


          //Calling save function to save file on the Serverside
          saveFile(client);

          //Trim Wav files using the start and end times received from client
          TrimAudio((parentPath+"\\seperate\\Server_WAV_file.wav"), (parentPath+"\\seperate\\Server_WAV_file2.wav"), start, (end-start));

          //Edit Pitch Sortof
          editPitch((parentPath + "\\seperate\\Server_WAV_file2.wav"), (parentPath + "\\seperate\\Server_WAV_file3.wav"));



          //Sending edited file back
          Socket echo2 = new Socket("localhost", 4300);
          //This file transfer code I found from this link: https://gist.github.com/CarlEkerot/2693246
          DataInputStream br2 = new DataInputStream(echo2.getInputStream());
          DataOutputStream dos2 = new DataOutputStream(echo2.getOutputStream());

          FileInputStream fis2;
          //If spook is true send the file that has been been edited with a strange filter, otherwise just send the trimmed audio
          if (spook) {
              fis2 = new FileInputStream(parentPath + "\\seperate\\Server_WAV_file3.wav");
          }else{
              fis2 = new FileInputStream(parentPath + "\\seperate\\Server_WAV_file2.wav");
          }

          byte[] buffer2 = new byte[4096];
          //Writing the edited file to Client
          while (fis2.read(buffer2) > 0) {
              dos2.write(buffer2);
          }

          fis2.close();
          dos2.close();
          client.close();

       } catch (IOException | UnsupportedAudioFileException e) {
           System.out.println("Exception caught...");
           System.out.println(e.getMessage());
       }
    }

    //Saves the file Server Side
    private void saveFile(Socket clientSock) throws IOException {

        Path parentPath = FileSystems.getDefault().getPath(new String("./")).toAbsolutePath().getParent();

        //This file transfer code I found from this link: https://gist.github.com/CarlEkerot/2693246
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        //Saved file where WavServer.java is located (saved on Serverside)
        FileOutputStream fos = new FileOutputStream(parentPath + "\\seperate\\Server_WAV_file.wav");

        byte[] buffer = new byte[4096];
        int filesize = 2147483647;
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;

        //read Byte by Byte and save
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            fos.write(buffer, 0, read);
        }
        System.out.println("read " + totalRead + " bytes.");

        fos.close();
        dis.close();
    }

        //Found and used code from Martin Dow from here: https://stackoverflow.com/questions/7546010/obtaining-an-audioinputstream-upto-some-x-bytes-from-the-original-cutting-an-au/7547123#7547123
        public static void TrimAudio(String sourceFileName, String destinationFileName, int startSecond, int secondsToCopy) {
            AudioInputStream inputStream = null;
            AudioInputStream shortenedStream = null;
            try {
                File file = new File(sourceFileName);
                AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
                AudioFormat format = fileFormat.getFormat();
                inputStream = getAudioInputStream(file);
                int bytesPerSecond = format.getFrameSize() * (int)format.getFrameRate();
                inputStream.skip(startSecond * bytesPerSecond);
                long framesOfAudioToCopy = secondsToCopy * (int)format.getFrameRate();
                shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
                File destinationFile = new File(destinationFileName);
                AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (inputStream != null) try { inputStream.close(); } catch (Exception e) { System.out.println(e); }
                if (shortenedStream != null) try { shortenedStream.close(); } catch (Exception e) { System.out.println(e); }
            }
        }

    //found this code and edited the sample rate until i got a unique sound that i like to call "spooky effect"
    //https://www.technetexperts.com/web/change-the-pitch-of-audio-using-java-sound-api/
    public static void editPitch(String sourceFileName, String destinationFileName) throws IOException, UnsupportedAudioFileException {
        File file = new File(sourceFileName);
        File outputfile = new File(destinationFileName);
        final AudioInputStream in1 = getAudioInputStream(file);
        final AudioFormat inFormat = getOutFormat(in1.getFormat());
        final AudioInputStream in2 = getAudioInputStream(inFormat, in1);
        AudioSystem.write(in2, AudioFileFormat.Type.WAVE, outputfile);
    }
    private static AudioFormat getOutFormat(AudioFormat inFormat) {
        int ch = inFormat.getChannels();
        float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, 1950, 16, ch, ch * 2, rate,
                inFormat.isBigEndian());
    }
}

