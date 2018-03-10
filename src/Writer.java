import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Writer {

    public static void main(String[] args) {
        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String ID = args[2];
        String maxNumAcc = args[3];
        String rmiRegistry = args[4];
        
        try {
        	 Registry registry = LocateRegistry.getRegistry(serverIP, Integer.parseInt(rmiRegistry));
            write(serverIP, serverPort, ID, maxNumAcc, registry, rmiRegistry);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
        

        return;
    }

    private static void write(String serverIP, int serverPort, String ID, String maxNumAcc, Registry registry,  String rmiRegistry) {

        int repeats = Integer.parseInt(maxNumAcc);
        PrintWriter log = null;
        String rSeq, sSeq;
        String server = rmiRegistry;
        int intID = Integer.parseInt(ID);
        try {
            log = new PrintWriter("log"+ID+".txt", "UTF-8");
            log.println("Client type: Writer");
            log.println("Client Name: " + ID);
            log.println("rSeq"+"\t\t"+"sSeq");
            log.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        INews client = null;
		try {
	        for (int i = 0; i < repeats; i++) {
	        	client = (INews) registry.lookup(server);
	            System.out.println("Writer ...");
	            //send write request
	            System.out.println("Sending write request ...");
	            String msg;
				msg = client.update("write"+"\t\t"+Integer.toString(intID)+"\n");
	            //receive read data
	            String[] segments = msg.split("\t\t");

	            sSeq = segments[1];
	            rSeq = segments[2];

	            //writing logs
	            log.println(rSeq+"\t\t"+sSeq);
	            log.flush();

	            //sleep between operations
	            try {
	                Thread.sleep(getRandomSec());
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
		} catch (RemoteException | NotBoundException e1) {
			log.println(e1.toString());
			log.flush();
			e1.printStackTrace();
		}



        System.out.println("Finished writer " + ID + " ...");
        log.close();
    }
    
    public synchronized static long getRandomSec() {

        Random r = new Random();
        int Low = 0;
        int High = 10000;
        return r.nextInt(High - Low) + Low;
    }
}
