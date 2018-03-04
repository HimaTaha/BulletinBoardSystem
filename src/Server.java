import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.sql.rowset.Joinable;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements  INews{

    private int  serverPort, maxItrs;
    private String serverIP;
    private ServerSocket serverSocket;
    private boolean isStopped;
    private Thread runningThread;
    BulletInBoard board;
    Log log = new Log();
    private Server(int serverPort, int maxItrs,String serverIp) {
        board = new BulletInBoard();
        log = new Log();
        this.serverPort = serverPort;
        this.maxItrs = maxItrs;
        this.serverIP = serverIp;
    }

    public static void main(String[] args) {

        //parsing arguments
    	String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        int maxItrs = Integer.parseInt(args[2]);
        String server = "server"; 
        /*
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        */
        System.out.println("Starting server ...");
        try {
        	System.setProperty("java.rmi.server.hostname",serverIP);
        	LocateRegistry.createRegistry(serverPort);
            Registry registry = LocateRegistry.getRegistry(serverIP, serverPort);
        	Server tcpServer = new Server(serverPort, maxItrs, serverIP);
        	INews stub =
                (INews) UnicastRemoteObject.exportObject(tcpServer, serverPort);

            registry.rebind(server, stub);
            System.out.println("server bound");
        } catch (Exception e) {
            System.err.println("server exception:");
            e.printStackTrace();
        }
    }

    
    
    
  	@Override
  	public  synchronized String update(String news) throws RemoteException {
  		BoardServer boardServer = new BoardServer(board, log, news);
  		Thread thread = new Thread(boardServer);
  		thread.start();
  		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		//new Thread(boardServer).start();
  		return boardServer.getRespond();
  	}

  
}
