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
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javax.sound.sampled.*;


//I found code for the file Selector from here: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm

public class Main extends Application{

    //global variables and objects to make life a bit easier
    public static File gbl_file;
    public static File gbl_file2;
    public static int gbl_min;
    public static int gbl_sec;
    public static Clip clip;
    private Desktop desktop = Desktop.getDesktop();

    //Code for the main UI Resides here
    @Override
    public void start(Stage stage) throws Exception {

        //objects to use for the file selector
        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Select WAV file");
        final Button openButton2 = new Button("Select WAV file");

        stage.setTitle("WAV_ClientServerApp");
        stage.setWidth(900);
        stage.setHeight(750);

        VBox root = new VBox();

        //All the lables, button etc
        Label warningg = new Label("DO NOT USE HEADPHONES, I am sure this app will work with headphones but no need to risk your ears");
        Label welcome = new Label("Welcome to the WAV Client Server app where you can edit your .wav files!");
        Label select = new Label("Select your WAV file below:"); Label select2 = new Label("Select your WAV file below:");
        Label wavselect = new Label("File chosen: "); Label wavselect2 = new Label("File chosen: ");
        Label AudioLen = new Label("Audio Length: ");
        Label TrimMsg = new Label("Select the range of audio you would like to keep (if you do not want to Trim ignore this section)");
        Label lblstart = new Label("Start at:  (example input  1:32)");
        TextField start=new TextField();
        start.setText("0:00");
        Label lblend = new Label("End at:    (example input  2:47)");
        TextField end=new TextField();
        Label SpookyMsg = new Label("Select below if you would like to add a wierd spooky effect");
        RadioButton r1 = new RadioButton("Spooky Effect");
        Button btnEdit_WAV = new Button("EDIT WAV"); //btnEdit_WAV.setBackground(Color.GREEN);
        Separator separator1 = new Separator();
        Label AudioPlayer = new Label("AudioPlayer:");
        Button btnPlay = new Button("PLAY");
        Button btnStop = new Button("STOP");
        Button btnExit = new Button("EXIT");
        btnExit.setOnAction(e -> Platform.exit());
        Label lb1 =  new Label("\n"); Label lb2 =  new Label("\n"); Label lb3 =  new Label("\n"); Label lb4 =  new Label("\n"); Label lb5 =  new Label("\n"); Label lb6 =  new Label("\n"); Label lb7 =  new Label("\n");

        //Placing all the labels and button in the correct order from top down
        root.getChildren().addAll(warningg, lb1, welcome, lb2, select, openButton, wavselect, AudioLen, TrimMsg, lblstart, start, lblend, end, SpookyMsg, r1, lb3, btnEdit_WAV, lb4, lb5);
        root.getChildren().addAll(separator1, AudioPlayer, select2, openButton2, wavselect2, btnPlay, btnStop, lb6, lb7, btnExit);

        //Button Action for Selecting a Wav file
        //I found code for the file Selector from here: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        configureFileChooser(fileChooser);
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            wavselect.setText("File chosen: " + file);

                            //Calling Duration function which will give my files length in seconds, I will use this to place how long the song is on the UI
                            float duration = 0;
                            try {
                                duration = getDuration(file);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            //Seconds conversion code I found from here: https://www.w3resource.com/java-exercises/basic/java-basic-exercise-55.php
                            int Seconds = (int) (duration % 60);
                            int Hours = (int) (duration / 60);
                            int Minutes = Hours % 60;
                            Hours = Hours / 60;

                            //Editing the Audio Length label to show how long the text is and setiing the end textfield to have max length automatically
                            AudioLen.setText("Audio Length: "+Minutes+":"+Seconds);
                            end.setText(Minutes+":"+Seconds);
                            gbl_min = Minutes;
                            gbl_sec = Seconds;
                            //Saving the file chosen into a global variable
                            gbl_file = file;
                        }
                    }
                });

        //This button action retrieves all data from the UI and send it off to the Wav Client to execute and contact the ServerClient
        btnEdit_WAV.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                //Parsing the start and end time the user wants to trim and converting it to seconds
                String[] startSplit = (start.getText()).split(":");
                String[] endSplit = (end.getText()).split(":");
                int s1 = Integer.parseInt(startSplit[0]);
                int s2 = Integer.parseInt(startSplit[1]);
                int e1 = Integer.parseInt(endSplit[0]);
                int e2 = Integer.parseInt(endSplit[1]);
                int start_time = s1*60+s2;
                int end_time = e1*60+e2;

                //Send off the data and let WavClient execute
                try {
                    //gbl_file is the file user selected to be edited, r1 is a bool and if true it e=means the spooky effect should be placed on the audio
                    WavClient.WAVCLIENT(gbl_file, r1.isSelected(), start_time, end_time);
                    display();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        //Selecting Wav file for the Audio Player
        openButton2.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        configureFileChooser(fileChooser);
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            wavselect2.setText("File chosen: " + file);
                            gbl_file2 = file;
                        }
                    }
                });

        //Found Code to play .wav audio in java from here: https://stackoverflow.com/posts/11025384/edit
        btnPlay.setOnAction( new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        try {
                            AudioInputStream stream;
                            AudioFormat format;
                            DataLine.Info info;

                            stream = AudioSystem.getAudioInputStream(gbl_file2);
                            format = stream.getFormat();
                            info = new DataLine.Info(Clip.class, format);
                            clip = (Clip) AudioSystem.getLine(info);
                            clip.open(stream);
                            clip.start();
                        }
                        catch (Exception e2) {
                        }
                    }
                });

        //Stops audio from playing
        btnStop.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                //audioPlayer.stop();
                clip.stop();
            }
        });

        //Paints the UI on screen
        root.setPadding(new Insets(5, 5, 5, 5));
        root.setSpacing(5);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //Code for file selector
    //I found code for the file Selector from here: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View Wav FIles");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WAV", "*.wav")
        );
    }
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    //Audio Duration code found from here: https://gist.github.com/edwardyoon/0ac61cc393c50918474921b69629e668
    public static float getDuration(File file) throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        long audioFileLength = file.length();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        float durationInSeconds = (audioFileLength / (frameSize * frameRate));
        return (durationInSeconds);
    }

    //Popup Window Code from: http://www.learningaboutelectronics.com/Articles/How-to-create-a-pop-up-window-in-JavaFX.php
    public static void display()
    {
        Path parentPath = FileSystems.getDefault().getPath(new String("./")).toAbsolutePath().getParent();
        File file77 = new File(parentPath+"\\src\\Client_WAV_file.wav");
        float duration = 0;
        try { duration = getDuration(file77); } catch (Exception exception) { exception.printStackTrace(); }
        int Seconds = (int) (duration % 60);
        int Hours = (int) (duration / 60);
        int Minutes = Hours % 60;
        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Saved!");
        popupwindow.setWidth(1100);
        popupwindow.setHeight(250);
        Label label1 = new Label("The File has been saved to "+file77+" with and Audio Length of "+Minutes+":"+Seconds);
        Label label2 = new Label("Feel free to move the file and save with a different name or Play it in the Audio player below!");
        Button button1 = new Button("Close this pop up window");
        button1.setOnAction(e -> popupwindow.close());
        VBox layout= new VBox();
        layout.getChildren().addAll(label1,label2,button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }
    @Override
    public void stop() throws Exception {
        System.out.println("bye");
    }

    public static void main(String[] args) {

        launch();

    }
}
