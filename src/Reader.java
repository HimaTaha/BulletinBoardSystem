import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Reader {

    public static void main(String[] args) {
        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String ID = args[2];
        String maxNumAcc = args[3];
        String rmiRegistry = args[4];
        try {
            
            Registry registry = LocateRegistry.getRegistry(serverIP, Integer.parseInt(rmiRegistry));
            read(serverIP, ID, maxNumAcc, registry , rmiRegistry);
        } catch (Exception e) {
            System.err.println(" exception:");
            e.printStackTrace();
        }
        

        return;
    }

    private static void read(String serverIP, String ID, String maxNumAcc, Registry registry , String rmiRegistry) {

        int repeats = Integer.parseInt(maxNumAcc);
        PrintWriter log = null;
        String rSeq, sSeq, oVal;
        String server = rmiRegistry;
        try {
            log = new PrintWriter("log"+ID+".txt", "UTF-8");
			log.println("repeats = " + repeats);
            log.println("Client type: Reader");
            log.println("Client Name: " + ID);
            log.println("rSeq"+"\t\t"+"sSeq"+"\t\t"+"oVal");
            log.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        INews client = null;
		try {
	        for (int i = 0; i < repeats; i++) {
	        	client =  (INews) registry.lookup(server);
	            //send read request
	            String msg = client.update("read"+"\t\t"+ID+"\n");
	            String[] segments = msg.split("\t\t");
	            oVal = segments[0];
	            sSeq = segments[1];
	            rSeq = segments[2];
	            //writing logs
	            log.println(rSeq+"\t\t"+sSeq+"\t\t"+oVal);
	            log.flush();
	            //sleep between operations
	            try {
	                Thread.sleep(getRandomSec());
	            } catch (InterruptedException e) {
	            	log.println("Error hass ocured ");
	                e.printStackTrace();
	            }
	        }
		} catch (RemoteException | NotBoundException e1) {
			// TODO Auto-generated catch block
			log.println(e1.toString());
			log.flush();
			e1.printStackTrace();
		}
        log.close();
    }
    
    public synchronized static long getRandomSec() {

        Random r = new Random();
        int Low = 0;
        int High = 10000;
        return r.nextInt(High - Low) + Low;
    }
}
