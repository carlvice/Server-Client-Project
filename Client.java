//Name :- Aashish Jha
//Student Id :- 1001649477

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;



public class Client extends Application {//Extends Application to implement JavaFX features

	clientHTTP chtp;// Instance of clientHTTP class
	
	TextArea ta = new TextArea("Client Started...\n");// TextArea to show output on GUI

	public static void main(String[] args) {
		
		launch(args);// Starting GUI thread 
		System.exit(0);// Terminate programs on using the close button of JavaFX window
	}

	@Override
	public void start(Stage stage) throws Exception { // GUI 
		
		chtp = new clientHTTP();// Creating new instance of ServerHTTP to start the server
		//JavaFX referred from Java The Complete Reference 9th Edition by Herbert Schildt Chapter 34.
		FlowPane rootNode = new FlowPane();
		stage.setTitle("Client");
		Scene scene = new Scene(rootNode, 640, 480);
		stage.setScene(scene);
		Button exitButton=new Button("Exit");
		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				System.exit(0);// Terminate as user click on "Exit" button
			}
		});
		
		rootNode.getChildren().add(ta);
		rootNode.getChildren().add(exitButton);
		stage.show();
		//ABove lines are for GUI

	}

	public static int getRandomNumbersInRange(int min, int max) {
		// Referred from
		// https://www.mkyong.com/java/java-generate-random-integers-in-a-range/
		// To generate random numbers from min to max inclusive.In this program min=5 and max=15
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;

	}

	class clientHTTP implements Runnable { // Implements Runnable for Thread

		Thread t = null;
		Socket s = null;

		public clientHTTP() {
			
			t = new Thread(this, "Client");// Assign a thread to current instance of ClientHTTP
			t.start();// Starting the thread. It calls void run()
		}

		@Override
		public void run() {
			

			try {
				s = new Socket("localhost", 1234);// Client socket will connect to localhost:1234
				
				final DataOutputStream dout = new DataOutputStream(s.getOutputStream()); // Fetching the output stream of Server
				final DataInputStream din = new DataInputStream(s.getInputStream()); // Fetching the input stream of Server
				while (true) { // It will continuously send random number between 5 and 15 till terminated by user
					String randomNumber = getRandomNumbersInRange(5, 15)+"";// Enclosing the random number in a string as to send it in HTTP request message body
					//referred from https://www.tutorialspoint.com/http/http_responses.htm
					String first="POST / HTTP/1.1 \r\n";
					String second="Host: localhost:"+s.getPort()+" \r\n";
					String third="User-Agent:JavaFX HTTP Client GUI \r\n";
					String fourth="Content-Type:text/plain \r\n";
					String fifth="Content-Length:"+(randomNumber.getBytes("UTF-8")).length+" \r\n";
					//HTTP date format referred from https://stackoverflow.com/questions/7707555/getting-date-in-http-format-in-java
					Calendar calendar = Calendar.getInstance();
				    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
				    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
					String sixth="Date:"+dateFormat.format(calendar.getTime())+"\r\n";
					//Above lines are for HTTP Request message
					dout.writeUTF(first+second+third+fourth+fifth+sixth+"\r\n"+randomNumber);// Sending request message to server
					String response = din.readUTF();// Reading response from server
					if (!response.equals("")) {// Checking is response is not empty
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								
								String responseString[]=response.split("\r\n");// Decoding response message to get body
								ta.appendText(responseString[responseString.length-1] + "\n");// Displaying the response body on GUI
								

							}
						});
						
					}

				} 

			} catch (IOException e) {
				
				
				
				try {
					if(s!=null)
					s.close();// Closing client socket as server is no more reachable or not started yet
					Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							
							ta.appendText("Server is Offline. Press Exit or Close the window");
							
						}
					});
				
				} catch (IOException  e1) {
					// TODO Auto-generated catch block
				
				}
			}
		}

	}

}
