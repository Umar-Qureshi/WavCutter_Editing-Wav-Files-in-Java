package seperate;
/*
DO NOT USE HEADPHONES, I am sure headphones will work fine but I do not want to risk it

Name: Umar Qureshi
Student Number: 100591742
Course: SOFE 4790U Distributed Systems
Professor: Dr. Qusay Mahmoud

 */

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.sound.sampled.*;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import java.rmi.*;
import static java.rmi.server.RemoteServer.getClientHost;

public class WavServer implements FileInterface {

    public WavServer() throws RemoteException{
        super();
    }

    //content is all the bytes of the song user sent, spook is for spook sound effect, start end is the duration of the song user wants
    public void uploadFile(byte[] content, boolean spook, int start, int end) throws RemoteException {
        try {

            //Some file cleaning so previous files do not effect the new ones being made
            Path parentPath = FileSystems.getDefault().getPath(new String("./")).toAbsolutePath().getParent();
            File file111 = new File(parentPath+"/seperate/Server_WAV_file2.wav");
            File file222 = new File(parentPath+"/seperate/Server_WAV_file2.wav");
            File file333 = new File(parentPath + "/seperate/Server_WAV_file3.wav");
            file111.delete();
            file222.delete();
            file333.delete();

            //save the content bytes of the song user sent into Server_WAV_file.wav in the Server folder
            File fileName = new File(parentPath + "/seperate/Server_WAV_file.wav");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
            bos.write(content, 0, content.length);
            bos.flush();
            bos.close();

            String ip = getClientHost();
            System.out.println("Client:"+ip+" is sending file: "+fileName);

            //call the runnn function (nothing to do with threads), it sends parameters tht this function received and never used
            runnn(spook, start, end);

        } catch(Exception e){
            System.out.println("FileImpl: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public byte[] downloadFile(String fileName) {
        try {

            Path parentPath = FileSystems.getDefault().getPath(new String("./")).toAbsolutePath().getParent();
            File dfile = new File(parentPath + "/seperate/Client_WAV_file.wav");
            byte buffer[] = new byte[(int) dfile.length()];

            //Send the desired edited file into the buffered inputstream for the client to retrieve
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(parentPath + "/seperate/Client_WAV_file.wav"));
            bis.read(buffer, 0, buffer.length);
            bis.close();
            return (buffer);

        } catch (Exception e) {
            System.out.println("FileImpl: " + e.getMessage());
            e.printStackTrace();
            return (null);
        }
    }


    //this functions calls other functions to edit file and then it renames the file into the one the client desires
    //spook is for spook sound effect, start end is the duration of the song user wants
    public void runnn(boolean spook, int start, int end) {
        try {
            Path parentPath = FileSystems.getDefault().getPath(new String("./")).toAbsolutePath().getParent();

            //Doing some cleaning to avoid conflict
            File fileToBeSent = new File(parentPath + "/seperate/Client_WAV_file.wav");
            fileToBeSent.delete();

            File renameFile;
            //creating a file that the client will retrieve
            fileToBeSent = new File(parentPath + "/seperate/Client_WAV_file.wav");

            //Trim Wav files using the start and end times received from client
            TrimAudio((parentPath+"/seperate/Server_WAV_file.wav"), (parentPath+"/seperate/Server_WAV_file2.wav"), start, (end-start));
            //Edit Pitch Sort of
            editPitch((parentPath + "/seperate/Server_WAV_file2.wav"), (parentPath + "/seperate/Server_WAV_file3.wav"));


            //If spook is true rename the file that has been been edited with a strange filter, otherwise just the trimmed audio file is renamed
            if (spook) {
                renameFile = new File(parentPath + "/seperate/Server_WAV_file3.wav");
                //renaming the file (server side) to the correct exact name that the client will download
                renameFile.renameTo(fileToBeSent);

            }else{
                renameFile = new File(parentPath + "/seperate/Server_WAV_file2.wav");
                renameFile.renameTo(fileToBeSent);
            }

        } catch (IOException | UnsupportedAudioFileException e) {
            System.out.println("Exception caught...");
            System.out.println(e.getMessage());
        }
    }

    //Found and used code from Martin Dow to trim audio from here: https://stackoverflow.com/questions/7546010/obtaining-an-audioinputstream-upto-some-x-bytes-from-the-original-cutting-an-au/7547123#7547123
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
