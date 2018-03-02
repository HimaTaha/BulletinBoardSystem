
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INews extends Remote {
	
	String setNews(String news) throws RemoteException;
	String getNews() throws RemoteException;

}
