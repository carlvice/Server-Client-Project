//Name :- Aashish Jha
//Student Id :- 1001649477

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class Server extends Application { //Extends Application to implement JavaFX features

	ServerSocket ss; //Server Socket which will act as port to listen incoming request
	ServerHTTP shtp; //SeverHTTP class reference which will be used later in the program
	Text label1 = new Text("\nServer is sending to client\n");//Headings for GUI
	Text label2 = new Text("\nServer is receiving from client\n");//Headings for GUI
	TextArea textArea = new TextArea("Server is running ...\n");//Text area for GUI
	TextArea textArea2= new TextArea("Server is running ...\n");//Text area for GUI
	ListView<String> clientList = new ListView<String>();//List the connected client
	
	public static void main(String[] args) {

		launch(args); // Starting GUI thread 
		System.exit(0);// Terminate programs on using the close button of JavaFX window

	}

	@Override
	public void start(Stage stage) throws Exception {

		shtp = new ServerHTTP(); // Creating new instance of ServerHTTP to start the server
		// JavaFX referred from Java The Complete Reference 9th Edition by Herbert
		// Schildt Chapter 34.
		FlowPane rootNode = new FlowPane();
		stage.setTitle("Server");
		Scene scene = new Scene(rootNode, 1024, 768);
		stage.setScene(scene);
		//textArea.setPrefSize(320, 480);
		//clientList.setPrefSize(320, 240);
		clientList.getItems().add("Connected Clients");
		rootNode.getChildren().add(clientList);
		rootNode.getChildren().add(label1);
		rootNode.getChildren().add(textArea2);
		rootNode.getChildren().add(label2);
		rootNode.getChildren().add(textArea);
		stage.show();
		//Above lines are for GUI

	}

	class ServerHTTP implements Runnable { // Implements Runnable for Thread

		Thread t = null;
		String response = "";

		public ServerHTTP() {
			t = new Thread(this, "Server"); // Assign a thread to current instance of ServerHTTP
			t.start();// Starting the thread. It calls void run()
		}

		@Override
		public void run() {
			
			try {
				ss = new ServerSocket(1234); // Create  a Server Socket at port (1234)
			} catch (IOException e) {

				System.exit(0); // If port is busy or server is already running then it will terminate the new instance.

			}
			while (true) {
				try {
					Socket s = ss.accept(); // Accepting incoming connection
					new ClientThread(s); // Fork a thread for new client
				} catch (IOException e) {
					
				}

			}

		}

		class ClientThread implements Runnable {

			final Socket s; //Socket for connected client
			final Thread t;//Thread forked to connected client
			int waitTime;// Wait time provided by client to server for waiting or sleep of server
			boolean flag = true;// Flag is used to make sure that connected client list will add the client name once only.
			String clientName, response;// Client name and Server Response for client

			ClientThread(Socket s) {
				this.s = s; //Assigning the provided socket to class variable socket.
				t = new Thread(this, "Client Thread");// Assigning thread to connected client
				t.start();// Starting the thread

			}

			@Override
			public void run() {
				try {
					final DataOutputStream dout = new DataOutputStream(s.getOutputStream());// Fetching the output stream of client
					final DataInputStream din = new DataInputStream(s.getInputStream());// Fetching input stream of client

					while (true) { //Below lines will loop forever as to ensure the availability of Server all the time
						String request = din.readUTF();// Fetching the request message sent by the Client
						String clientInput[] = request.split("\r\n");// Decoding the HTTP request message from Client
						waitTime = Integer.parseInt(clientInput[clientInput.length - 1]);//Getting the wait time from HTTP request message
						clientName = s.getPort() + "";// Getting the port number on which client is connected and using it as Client's name
						Platform.runLater(new Runnable() {// For updating GUI

							@Override
							public void run() {
								
								if (flag) {
									clientList.getItems().add("Client " + clientName); // Adding client name to connected client list
									flag = false;
								}

								textArea.appendText(request+"\n");// displaying client's HTTP request message on GUI 
								textArea.appendText("\n Server is waiting for " + waitTime + " seconds \n ");// Server announcement
							}
						});

						Thread.sleep(waitTime * 1000);// Making server to wait for "waittime" number of seconds
						response = "Server waited " + waitTime + " seconds for Client " + clientName;// Server is informing about its wait time
						String first = "HTTP/1.1 200 OK \r\n";
						//HTTP date format referred from https://stackoverflow.com/questions/7707555/getting-date-in-http-format-in-java
						Calendar calendar = Calendar.getInstance();
					    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
					    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						String second = "Date:" + dateFormat.format(calendar.getTime()) + " \r\n";
						String third = "Server:JavaFX HTTP Server GUI \r\n";
						String fourth = "Content-Length:" + (response.getBytes("UTF-8")).length + " \r\n";
						String fifth = "Content-Type:text/plain \r\n";
						String sixth = "Connection:Closed";
						//Above lines are HTTP response message from Server to Client 
						// referred from https://www.tutorialspoint.com/http/http_responses.htm
						dout.writeUTF(first + second + third + fourth + fifth + sixth + " \r\n" + response);// Sending HTTP response to client 
						Platform.runLater(new Runnable() {// displaying HTTP response message on GUI
							
							@Override
							public void run() {
								
								textArea2.appendText(" \r\n "+first + second + third + fourth + fifth + sixth + " \r\n" + response+" \r\n ");
								
							}
						});

					}

				} catch (Exception e) {
					
					//When Client disconnected
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							textArea.appendText("\n Client " + clientName + " got disconnected \n");
							clientList.getItems().remove("Client " + clientName);// Remove client name from connected client list

						}
					});
					try {
						s.close();// Close the socket as client is no longer connected
					} catch (IOException e1) {
						
					}
				}

			}

		}
	}

}
