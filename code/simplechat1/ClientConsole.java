// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        if (message.charAt(0) == '#') {
	  switch(message.toLowerCase()) {
	    case "#quit":
		System.out.println("Disconnecting from server...");
		client.closeConnection();
		System.out.println("Exiting. Goodbye. ");
		System.exit(0);
		break;
	    case "#logoff":
		client.closeConnection();
		System.out.println("Disconnected from server. ");
		break;
	     case "#sethost": 
		if (client.isConnected()) {
		  System.out.println("Error: Cannot set host while connected.");
		} else {
		System.out.print("/nEnter host name: ");
		BufferedReader reader =  
                   new BufferedReader(new InputStreamReader(System.in));
		String host = reader.readLine();
		client.setHost(host);
		System.out.println("Host set. ");
		}
		break;
	     case "#setport":
		if (client.isConnected()) {
		  System.out.println("Error: Cannot set port while connected.");
		} else {
		System.out.print("/nEnter port: ");
		BufferedReader reader2 =  
                   new BufferedReader(new InputStreamReader(System.in));
		String input = reader2.readLine();
		int port = Integer.parseInt(input);
		client.setPort(port);
		System.out.println("Port set. ");
		}
		break;
	     case "#login":
		try 
		{
		  client.openConnection();
		  System.out.println("Logged in. ");
		}
		catch(IOException e)
		{
		  System.out.println("Error: you are already logged in.");
		}
		break;
	     case "#gethost":
		System.out.println("Host: " + client.getHost());
		break;
	     case "#getport":
		System.out.println("Port: " + client.getPort());
		break;
	     case "#getid":
		System.out.println("ClientID: " + client.getClientID());
		break;
	     default:
		System.out.println("Command << " + message + " >> does not exist.");
	  }
	} else {
	client.handleMessageFromClientUI(client.getClientID() + "> " + message); }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************

  protected void connectionClosed() 
  {
    System.out.println("Server is no longer connected. ");
  }

  protected void connectionException(Exception exception) 
  {}
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number
    String clientID = "";

    try
    {
      clientID = args[0]; //Get clientID from command line
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
      System.exit(0);
    }

    try
    {
      port = Integer.parseInt(args[1]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    try
    {
      host = args[2];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }

    ClientConsole chat= new ClientConsole(host, port);
    chat.client.setClientID(clientID);
    chat.accept();  //Wait for console data
    System.out.println("ClientID: " + chat.client.getClientID());
  }
}
//End of ConsoleChat class
