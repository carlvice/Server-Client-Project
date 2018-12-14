# Server-Client-Project

Steps to execute the program: -

This program is consisting of two files. “Server.java” and “Client.java”

	1. Start the server
		a. Open command prompt
		b. Compile the Server.java file using “javac Server.java” and then run it using “java Server”.
		c. It will start the Server and a GUI will pop up on the screen
		d. This GUI consist of three components. Two are text areas where HTTP request and HTTP response messages will be displayed, and one is a list which will display the connected clients.
		e. As there is no client connected yet, hence these fields just contain information about server and their own heading.
		
	2. Start the client
		a. Open a new command prompt (Please make sure the command prompt in which the server is executed is not closed)
		b. Compile the Client.java file using “javac Client.java” and then run it using “java Client”.
		c. It will start the Client and a GUI will pop up on the screen
		d. This GUI consist of two components. One is a text area in which Client will print the response received by server and another is “Exit button” which will kill the client process and close the window.
		
	3. You can repeat step 2 to create more clients just make sure each client is executed from a new command prompt window and all other command prompt windows are still running.
	
	4. As soon as Client connect to server, they start exchanging messages and that client will be in the connected client list on server GUI. They will keep exchanging message until the client is terminated by user.
	
	5. As a client get disconnected, User will be notified as its name will disappear from the connected client list from the server. (Please note, sometime the name of the disconnected client will take some time to disappear from connected client list. This happens when the server is in waiting mode when the client got disconnected. As soon as the waiting mode ends, server will update its connected client list). To disconnect a client, you can use “Exit” button or close button of GUI window of client.
	
	6. To close the server, you can use close button of GUI window of server.
	
Notes

	1. If you start client without starting the server, the client will display the message saying, “server is offline”. You need to close the client and start the server and then again start the client.
	
	2. If server is running on a port already and you try to re-run the server on same port, the program will not let you do that. It will kill the new instance of server as soon as it begins.
	
	3. If you close the server while client(s) is connected. The client will react same as step 1 and you need to follow the same process again.
	
REFERENCES: -

1. JAVAFX REFERRED FROM JAVA THE COMPLETE REFERENCE 9TH EDITION BY HERBERT SCHILDT CHAPTER 34.

2. HTTP DATE FORMAT REFERRED FROM https://stackoverflow.com/questions/7707555/getting-date-in-http-format-in-java

3. HTTP MESSAGES REFERRED FROM https://www.tutorialspoint.com/http/http_responses.htm

4. RANDOM NUMBER GENERATOR FUNCTION REFERRED FROM https://www.mkyong.com/java/java-generate-random-integers-in-a-range/
