package melihdedeoglu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Klonlama {
	public static String linktenisimAlma(String link) {
		
        String[] ayrilmis = link.split("/");
        return ayrilmis[ayrilmis.length - 1].split("\\.git")[0];
	}
	
	public static List<File> klonlamaislemi(String link) {
		
        List<File> siniflar = new ArrayList<>();
        
        try {
        	File directory = new File(linktenisimAlma(link));
            if (directory.exists()) {
            	varOlanDosyayiSilme(directory); //Önceden kopyaladıysak siliyoruz.
            }
            
            Process islem = Runtime.getRuntime().exec("git clone " + link);
            int exitCode = islem.waitFor();
            
            if (exitCode == 0) {    //Link doğru demek.
                
                
                File directory2 = new File(linktenisimAlma(link));
                siniflar.addAll(Analiz.sinifBulma(directory2)); //Sınıf dosyalarını liste yapıyoruz.
            } 
            else {
                System.out.println("Github linki doğru değil.");
            }
            
        } 
        catch (InterruptedException | IOException  e) {
            e.printStackTrace();
        }
        
        return siniflar;
	}

	public static void varOlanDosyayiSilme(File tumdosya) {
	 
        if (tumdosya.isDirectory()) {
        	
            File[] dosyalar = tumdosya.listFiles();
            
            if (dosyalar != null) {
                for (File dosya : dosyalar) {
                	varOlanDosyayiSilme(dosya);
                }
            }
        }
        
        tumdosya.delete();
    }
}
