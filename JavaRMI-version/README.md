# Distributed Systems Assignment 2 Fall2020

This project I used Linux to make and run this program/assignment

---

To run this program Please first install JavaFX, I would have uploaded it but it was larger then 25mb.

Official Download link: https://gluonhq.com/products/javafx/ , I downloaded the stable version 11.02

To Install JavaFX into IntelliJ follow this official guide: https://openjfx.io/openjfx-docs/#install-javafx

Or watch this video tutorial: https://www.youtube.com/watch?v=BHj6zbH3inI

Once JavaFx and its libraries are installed properly follow the next steps:

---


## How to Run

cd into src folder

Run rmiregistry in a seperate terminal
```
rmiregistry
```


then compile all files at once with the following command (in another terminal):
```
javac Client/WavClient.java seperate/*.java
```

Then open the server from src folder:
```
java seperate.FileServer 
```

Now Run Main from the ide and you will be able to use my program!

---
Screenshot on how to run:
![HowToRun](https://user-images.githubusercontent.com/22453457/97255297-aef2d980-17e6-11eb-9fb1-1e0dcff5514a.png)




