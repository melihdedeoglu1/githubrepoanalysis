package melihdedeoglu;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class Test {

	public static void main(String[] args) {
		BufferedReader linkokuma = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("GitHub linkinizi giriniz: ");
        String link = null;
        
        try {
        	link = linkokuma.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (link != null) {
        	
            List<File> classFiles = Klonlama.klonlamaislemi(link);        
            Analiz.satirAnaliz(classFiles);
            
        }
	}

}
