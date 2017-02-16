/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RMISecurityManager;
import java.net.MalformedURLException;

import common.MessageInfo;

public class RMIClient {

        public static void main(String[] args) {

                RMIServerI iRMIServer = null;

                // Check arguments for Server host and number of messages
                if (args.length < 2){
                        System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
                        System.exit(-1);
                }

                String urlServer = new String("rmi://" + args[0] + "/RMIServer");
                int numMessages = Integer.parseInt(args[1]); // total number of messages

                // TO-DO: Initialise Security Manager
                if(System.getSecurityManager()==null){
                        System.setSecurityManager(new RMISecurityManager());
                }

                // TO-DO: Bind to RMIServer
                try{
			// Registry r = LocateRegistry.getRegistry(6000);
                        iRMIServer = (RMIServerI) Naming.lookup(urlServer);
                }catch(RemoteException e){
                        System.out.println("Remote Exception error ocurred 1.");
			e.printStackTrace();
                        System.exit(-1);
                }catch(NotBoundException e){
			System.out.println("Lookup didn't work, Not Bound Exception error ocurred.");
                        System.exit(-1);		
		}catch(MalformedURLException e){
                        System.out.println("Malformed URL Exception error ocurred.");
                        System.exit(-1);
                } 

                // TO-DO: Attempt to send messages the specified number of times
		try{
			for(int i=0; i<numMessages; i++){
                                MessageInfo message_sent = new MessageInfo(numMessages, i);
                                iRMIServer.receiveMessage(message_sent);
                        }
		}catch(RemoteException e){
                        System.out.println("Remote Exception error ocurred 2.");
                        System.exit(-1);			
		}
        }
}
