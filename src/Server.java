import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements  INews{

    private int serverPort, maxItrs;
    private ServerSocket serverSocket;
    private boolean isStopped;
    private Thread runningThread;
    BulletInBoard board;
    Log log = new Log();
    private Server(int serverPort, int maxItrs) {
        board = new BulletInBoard();
        log = new Log();
        this.serverPort = serverPort;
        this.maxItrs = maxItrs;
    }

    public static void main(String[] args) {

        //parsing arguments
        int serverPort = Integer.parseInt(args[0]);
        int maxItrs = Integer.parseInt(args[1]);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        System.out.println("Starting server ...");
        try {
        	String server = "Server";
        	Server tcpServer = new Server(serverPort, maxItrs);
        	Server stub =
                (Server) UnicastRemoteObject.exportObject(tcpServer, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(server, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }

    
    
    
  	@Override
  	public String update(String news) throws RemoteException {
  		BoardServer boardServer = new BoardServer(board, log, news);
  		 new Thread(boardServer).start();
  		return boardServer.getRespond();
  	}
  	/*
    @Override
    public void run() {

        //create main thread
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }

        

        //create server socket
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //start main loop
        while (!isStopped()) {

            //accept client socket
            Socket client = null;
            try {
                client = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            new Thread(new BoardServer(board, client, log)).start();

            if (--maxItrs == 0) {
                stop();
            }
        }

        System.out.println("Server closed ...");

    }

    public synchronized void stop(){

        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    private synchronized boolean isStopped() {

        return this.isStopped;
    }

    
  
}
