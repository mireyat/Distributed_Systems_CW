/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RMISecurityManager;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

        private int totalMessages = -1;
        private int[] receivedMessages;

        public RMIServer() throws RemoteException {
        }

        public void receiveMessage(MessageInfo msg) throws RemoteException {

                // TO-DO: On receipt of first message, initialise the receive buffer
                if(receivedMessages==null){
                        receivedMessages = new int[msg.totalMessages];
                }

                // TO-DO: Log receipt of the message
                receivedMessages[msg.messageNum]=1;

                // TO-DO: If this is the last expected message, then identify
                //        any missing messages
                if((msg.messageNum+1) == msg.totalMessages){
				
			int[] lost_msgs; // array to store lost message numbers
			lost_msgs = new int[msg.totalMessages];	
			String lost= new String();
			lost = "no";
			int m=0;
	
			for(int i=0; i< msg.totalMessages; i++){
				if(receivedMessages[i] != 1){
					lost= "yes";
					lost_msgs[m]= (i+1);
					m++;
				}
			}

				
			System.out.println("Number of messages sent: " + msg.totalMessages);			
			if(lost == "no"){
				System.out.println("There were no messages lost.");
			}			
			else{	
				System.out.println("The messages that were lost where the ones with sequence number: ");
				for(int i=0; i<m; i++){
					System.out.println(lost_msgs[i] + ", ");					
				}	
			}
		}
        }


        public static void main(String[] args) {

                RMIServer rmis = null;

                // TO-DO: Initialise Security Manager
                if(System.getSecurityManager()==null){
                        System.setSecurityManager(new RMISecurityManager());
                }

                // TO-DO: Instantiate the server class
		try{
	                rmis = new RMIServer();
		}catch(RemoteException e){
                        System.out.println("Remote Exception error ocurred 1.");
                        System.exit(-1);			
		}

                // TO-DO: Bind to RMI registry
                rebindServer("RMIServer", rmis);
        }




        protected static void rebindServer(String serverURL, RMIServer server) {

                // TO-DO:
                // Start / find the registry (hint use LocateRegistry.createRegistry(...)
                // If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
                // TO-DO:
                // Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
                // Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
                // expects different things from the URL field.

                try{
			// Registry r= LocateRegistry.createRegistry(6000);
			// r.rebind(serverURL, server);
			LocateRegistry.createRegistry(1099);
			Naming.rebind(serverURL, server);
                }catch(RemoteException e){
                        System.out.println("Remote Exception error ocurred 2.");
                        System.exit(-1);
                }
		catch(MalformedURLException e){
                        System.out.println("Malformed URL Exception error ocurred.");
                        System.exit(-1);
                } 
        }
}


