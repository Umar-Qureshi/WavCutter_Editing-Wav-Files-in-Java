# WavCutter

## Brief Summary

A  Distributed Client-Server Application which edits WAV Files. The client uploads the .wav file to be edited using the GUI, then the .wav is sent over to the server to be edited and finally the edited file is sent back to the client

More Details found in the report: https://github.com/Umar-Qureshi/WavCutter_Editing-Wav-Files-in-Java/blob/master/WavCutter_Report-UmarQureshi.pdf

## How to run
To run this program Please first install JavaFX, I would have uploaded it but it was larger then 25mb.

Official Download link: https://gluonhq.com/products/javafx/ , I downloaded the stable version 11.02

To Install JavaFX into IntelliJ follow this official guide: https://openjfx.io/openjfx-docs/#install-javafx

Or watch this video tutorial: https://www.youtube.com/watch?v=BHj6zbH3inI

Then save the files Main and WavClient in src, make a folder called seperate and save the WavServer file ther


Open Terminal in Intellij to open the server

first cd into src

then compile WavClient and WavServer like this:

javac WavClient.java

javac seperate\\WavClient.java

Then open server:

java seperate/WavClient.java

(Feel free to look at my [report](https://github.com/Umar-Qureshi/WavCutter_Editing-Wav-Files-in-Java/blob/master/WavCutter_Report-UmarQureshi.pdf) with screenshots to view how to open server)

Now Run Main from the ide and you will be able to use my program!

## Screenshots:

![image](https://user-images.githubusercontent.com/22453457/133960681-1d1a538e-ede9-4f84-986a-8b03c48436c1.png)

## Challenges Faced:

I had not anticipated how difficult this idea I came up with was. My original vision was to have more ways one could edit the WAV file such as increasing and decreasing amplitude/volume, speed, pitch and adding interesting filters on top of sounds. I spent hours searching, I was able to achieve most of them as I could hear them while they ran but there was no way for me to save the effects into a new .WAV File. I do not have much of a solution, some code I found I edited the Sample Rate and it altered how it sounded, it felt like the effect where you are outside and hearing music from inside a building. In the interest of time as my efforts ended up fruitless, I dubbed it the spooky sound effect and made it my main Feature. I think it still counts as a core functionality as the concept of editing how a file sounds are the same, Java sadly isn’t built for editing and saving WAVs with effects.

When writing code to send the file back from Server Side I was having issues with it. I believe my problem was that I was using the same socket to send back the file which I used to receive it so what I did to solve this was to use a new socket and port number and pretend my Server was a client and I sent it back just like how the Client sent it to me, of course the client also received it as my Server did.

I had lots of issues with path names not working as I intended. I had saved my Server file in a different folder to showcase that my program is truly running on sockets and to keep things organised. To solve this, I found functions for path names and with a little of trial and error I found out how to do it correctly.

I have never used a Java GUI before, so it was a nice challenge to take on and use the opportunity to learn it. I watched a few videos on it and implemented the basics learnt with the video along with google for any other additions. Even with my simple, unpolished GUI I think that it does the job well.

When a file was selected the filepath that was saved was locked in the function, I was not able to use it in another function. I decided to make a file object that was global so I could easily save it and use it anywhere. I made other variables global as well to make life a bit easier.

When I ran my GUI I was only able to save one file, If I saved another it would give me some sort of socket already in use message. I looked into it more deeply and realized that I had not closed my Socket or Buffer reader correctly, once I did I was able to save more than once except for one situation.

When I would run the GUI, put on spook filter, save a WAV file, then turn off spook filter then try to save the new Wav file my program would crash. It gave me an error message stating a certain file had not been found when I specifically coded for a file to rename into the missing file. I decided to take a simpler approach where no matter what the spook filter was applied and then with an if statement, I sent the normal file or spook file as opposed to renaming files.

Overall this was a great learning experience as I am not too familiar with java, I got a chance to learn JavaFX GUI and get a better grasp on object oriented programming and of course hands on understanding of sockets.


### Java RMI Version

I also recreated this application using Java RMI instead of a server & client, RMI version can be found here:  https://github.com/Umar-Qureshi/WavCutter_Editing-Wav-Files-in-Java/tree/master/JavaRMI-version

