/*
 * Created on 01-Mar-2016
 */
package udp;

/*
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
*/
import java.io.*;
import java.net.*;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages = null;
	private boolean close;


	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server_object = new UDPServer(recvPort);
		try{
		server_object.run();
		}catch(SocketTimeoutException e){
			System.out.println("Timeout");
		}
	}


	private void run() throws SocketTimeoutException{
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		
		while(!close){
			pacSize = 2500;
			pacData = new byte[2500];

			pac = new DatagramPacket(pacData, pacSize);
			try{ 
				recvSoc.setSoTimeout(30000);
				recvSoc.receive(pac);
			}catch(IOException e){
				System.out.println("The packet wasn't recieved properly, error");
				System.exit(-1);
			}
			String data_to_send = new String(pac.getData(), 0, pac.getLength());
			processMessage(data_to_send);		
		}

	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			msg = new MessageInfo(data);
		}catch(Exception e){
			System.out.println("Error when creating a new MessageInfo object");
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(receivedMessages == null){
			receivedMessages = new int[msg.totalMessages];
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum]=1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if((msg.messageNum+1) == msg.totalMessages){
			close = true;
				
			int[] lost_msgs; // array to store lost message numbers
			lost_msgs = new int[msg.totalMessages];	
			String lost= new String("no");
			int m=0;
	
			for(int i=0; i< msg.totalMessages; i++){
				if(receivedMessages[i] != 1){
					lost= "yes";
					lost_msgs[m]= (i+1);
					m++;
				}
			}
			
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


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try{
		recvSoc = new DatagramSocket(rp);
		}catch(SocketException e){
			System.out.println("The UDP socket for recieving data couldn't be initialised, error");
			System.exit(-1);		
		}

		close = false;

		// Done Initialisation
		System.out.println("UDPServer ready");
	}
}
