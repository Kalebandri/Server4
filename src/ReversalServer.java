import java.io.*;
import java.net.*;
import java.util.*;

class Reversal extends Thread {
	private Socket socket;
	private String client;
	
	Reversal(Socket s) {
		socket = s;
		client = s.getRemoteSocketAddress().toString();
		System.out.println("Incoming connection from " + client);
	}
	
	private static String reverse(String str) {
		StringBuilder sb = new StringBuilder();
		int len = str.length();
		for (int i = len - 1; i >= 0; i--)
			sb.append(str.charAt(i));
		return sb.toString();
	}
	
	public void run() {
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader (socket.getInputStream())
					);
			PrintWriter out = new PrintWriter(
					socket.getOutputStream(),
					true);
			
			out.println("Hello! Welcome to the program.");
			out.println("Enter a line with only a period to quit\n");
			
			while (true) {
				String request = in.readLine();
				if (request == null || request.equals("."))
					break;
				System.out.println("Reversal request for: " + request);
				
				String rev = reverse(request);
				out.println("Server replies \"" + rev + "\"");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				socket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Close connection from " + client);
		}
	}
}

public class ReversalServer {
	public static void main(String[] args) throws IOException {
		try (ServerSocket listener = new ServerSocket(9090)) {
			System.out.println("The server is running...");
			while (true) {
				Socket s = listener.accept();
				Thread t = new Reversal(s);
				t.start();
			}
		}
	}
}
