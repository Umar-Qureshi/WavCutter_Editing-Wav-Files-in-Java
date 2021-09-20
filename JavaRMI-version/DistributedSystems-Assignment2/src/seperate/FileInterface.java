package seperate;

import java.rmi.Remote;
import java.rmi.RemoteException;

//Interface allows functions to be called remotely as if they were local functions
public interface FileInterface extends Remote {

    public byte[] downloadFile(String fileName) throws RemoteException;

    public void uploadFile(byte[] content, boolean spook, int start, int end) throws RemoteException;
}