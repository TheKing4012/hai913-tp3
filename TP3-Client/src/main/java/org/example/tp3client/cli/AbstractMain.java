package org.example.tp3client.cli;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class AbstractMain {
	public static final String QUIT = "0";

	public static String SERVICE_URL1;
	public static String SERVICE_URL2;
	public static String SERVICE_URL3;
	
	protected void setTestServiceUrl(BufferedReader inputReader)
			throws IOException {

		SERVICE_URL1 = "http://localhost:30050/redhotel/api/";
		SERVICE_URL2 = "http://localhost:30051/ibis/api/";
		SERVICE_URL3 = "http://localhost:30052/ibis/api/";
	}
	
	protected abstract void menu();
}
