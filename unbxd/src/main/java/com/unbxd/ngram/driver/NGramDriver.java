package com.unbxd.ngram.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import net.sf.extjwnl.JWNLException;

import com.unbxd.ngram.core.NGramEngine;

public class NGramDriver {

	public static void main(String[] args) throws IOException, JWNLException {
		Scanner scan = new Scanner(System.in);
		try{
			while (true) {
				
				System.out.println("Enter 1 to process the file");
				System.out.println("Enter 2 for custom input");
				System.out.println("Enter 0 to exit");
				System.out.print("Input:");
				
				String input = scan.next().toLowerCase();
				switch(Integer.valueOf(input)){
				
				case 1:
					InputStream in = NGramEngine.class.getClass().getResourceAsStream("/words.txt");
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			        String line;
			        Long startTime = System.currentTimeMillis();
			        while ((line = reader.readLine()) != null) {
			        	System.out.println("--------------------------------------------------------------------------------");
			        	NGramEngine.processString(line);
			            System.out.println("--------------------------------------------------------------------------------");
			        }
			        Long endTime = System.currentTimeMillis();
			        System.out.println("Total Time taken to process file:"+(endTime-startTime)+"ms");
					break;
				case 2:
					while(true){
						System.out.print("Enter a string or # to go to main menu: ");
						input = scan.next().toLowerCase();
						if("#".equals(input)){
							break;
						}
						NGramEngine.processString(input);
					}
					break;
				case 0:
					System.exit(1);
				}
			}
		}finally{
			scan.close();
		}
	}
}
