
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INews extends Remote {
	
	String update(String news) throws RemoteException;

}
