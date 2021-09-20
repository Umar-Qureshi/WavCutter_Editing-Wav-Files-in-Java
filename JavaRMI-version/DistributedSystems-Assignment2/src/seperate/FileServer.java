package seperate;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FileServer {
    public static void main(String argv[]) {

        try {
            System.out.println("Server is Open/Running: ");
            FileInterface fi = new WavServer();
            FileInterface stub = (FileInterface) UnicastRemoteObject.exportObject(fi, 0);

            //Create a registry named "FileServer" that clients can lookup
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("FileServer", stub);
            System.out.println("Server is now bounded to the remote object");

        } catch(Exception e) {
            System.out.println("FileServer: "+e.getMessage());
            e.printStackTrace();
        }
    }
}