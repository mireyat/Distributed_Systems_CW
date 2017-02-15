/*
 * Created on 01-Mar-2016
 */
package udp;

/* import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
*/
import java.io.*;
import java.net.*;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;

	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;
		String 		message;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		// TO-DO: Construct UDP client class and try to send messages
		UDPClient client_class= new UDPClient();
		client_class.testLoop(serverAddr, recvPort, countTo);
	}

	public UDPClient() {
		// TO-DO: Initialise the UDP socket for sending data
		try{
			sendSoc= new DatagramSocket();
		}catch(SocketException e){
			System.out.println("UDP socket wasn't created properly, error");
		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		int				tries = 0;

		// TO-DO: Send the messages to the server
		for(int i=0; i<countTo; i++){
			String total_num_msgs = Integer.toString(countTo);		
			String sequence_num = Integer.toString(i);
			String message_to_send = new String();
			message_to_send = total_num_msgs + ";" + sequence_num;

			send(message_to_send, serverAddr, recvPort);
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int				payloadSize;
		byte[]				pktData;
		DatagramPacket		pkt;
		payloadSize = payload.length();
		pktData = new byte[payloadSize];
		pktData = payload.getBytes();

		// TO-DO: build the datagram packet and send it to the server
		pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);		
		try{
			sendSoc.send(pkt);
		}catch(IOException e){
			System.out.println("Datagram packet couldn't be sent to the server, error");
			System.exit(-1);
		}
	}
}
