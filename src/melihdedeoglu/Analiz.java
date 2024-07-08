package melihdedeoglu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analiz {
	public static List<File> sinifBulma(File tumdosya) {
	   	 
        List<File> siniflar = new ArrayList<>();
        
        File[] dosyalar = tumdosya.listFiles();
        if (dosyalar != null) {
            for (File dosya : dosyalar) {
                if (dosya.isFile() && dosya.getName().endsWith(".java") && !dosya.getName().startsWith("I")) { //Sonu .java olanlar ve interface olmayanları getir.
                	siniflar.add(dosya);
                } else if (dosya.isDirectory()) {
                	siniflar.addAll(sinifBulma(dosya));
                }
            }
        }
        return siniflar;
	}

	public static void satirAnaliz(List<File> siniflar) {
	
    	for (File sinif : siniflar) {
                                        
            int javadocsatiri = 0;
            int yorumsatiri=0;
            int kodsatiri=0;
            int toplamsatir = 0;
            int fonksiyonsayisi=0;
            boolean sinifmi=false;
            float YG;
            float YH;
            float sapma;

            try (BufferedReader sinifokuma = new BufferedReader(new FileReader(sinif))) {
            	
                String tumsatir;
                
                String regex1 ="[a-z|.|\s]*//[a-z|.|\s]*"; 
                Pattern oruntu=Pattern.compile(regex1);
                
                String regex2 ="(public|private|protected|default)(a-z|.|\s)*(void|int|float|double|String|static|boolean|I|.)(a-z|.|\\s)*\\((a-z|.|\s)*\\)(a-z|.|\s)*";
                Pattern oruntu2=Pattern.compile(regex2);
                
                String regex3 ="[a-z|.|\s]*class[a-z|.|\s]*";
                Pattern oruntu3=Pattern.compile(regex3);
                
                String str = sinif.getName();
                String yeniStr = str.replace(".java", "");
                String regex4 ="(public|private|protected|default)(a-z|.|\s)*" + yeniStr + "(a-z|.|\s)*" ;
                Pattern oruntu4=Pattern.compile(regex4);
                
                boolean javadocmu = false;
                boolean yorummu = false;
                
                while ((tumsatir = sinifokuma.readLine()) != null) {
                	
                	Matcher eslesme = oruntu.matcher(tumsatir);
                	Matcher eslesme2 = oruntu2.matcher(tumsatir);
                	Matcher eslesme3 = oruntu3.matcher(tumsatir);
                	Matcher eslesme4 = oruntu4.matcher(tumsatir);
                	toplamsatir++;
                	
                	tumsatir = tumsatir.trim();
                	
                	if (eslesme3.find()) { //İçinde sınıf olanları ayırır.
                		sinifmi=true;
                	}
                	else if(eslesme4.find() && !tumsatir.endsWith(";")) {
                		fonksiyonsayisi++;
                		
                	}
                	else if (eslesme2.find() && !tumsatir.endsWith(";")) {               		
                		fonksiyonsayisi++;
                    }
					
                	if(tumsatir.startsWith("/**")){
                		javadocmu = true;                         
                    } 
                	else if(tumsatir.startsWith("/*")){
                		yorummu = true;                         
                    }  
                	else if (tumsatir.endsWith("*/")) {
                		yorummu = false; 
                        javadocmu = false;
                    }
                	
                	if (tumsatir.isEmpty()) {
                        //Boşsa bir şey yapmıyoruz.
                    }
                	else if (tumsatir.startsWith("*") && javadocmu==true) {                           
                        javadocsatiri++;
                    } 
                    else if (tumsatir.startsWith("*") && yorummu==true) {                           
                        yorumsatiri++;
                       
                    }                                   
                    else if (eslesme.find() && !tumsatir.endsWith("{") ) {
                    	if(tumsatir.startsWith("//")) {
                    		yorumsatiri++;
                    		
                    	}
                    	else {
                    		kodsatiri++;
                    		yorumsatiri++;
                    		System.out.println("\n"+tumsatir);
                    	}                      	
                    }                       
                    else {
                    	if(tumsatir.startsWith("/**")){
                                                   
                        } 
                    	else if(tumsatir.startsWith("/*")){
                                                   
                        }  
                    	else if (tumsatir.endsWith("*/")) {
                            
                        }
                    	else if (tumsatir.startsWith("*")) {
                    		
                    	}                  	
                    	else
                    		kodsatiri++;
                    		System.out.println("\n"+tumsatir);
                    }
                	
                }
                    	
                
            } catch (IOException e) {
            	e.printStackTrace();
            }
            
            if(fonksiyonsayisi==0) {
            	YG = 0;
            	YH = 0;
            }       	
            else {
            	YG = (float) ((((float)javadocsatiri + yorumsatiri) * 0.8) / fonksiyonsayisi);
            	YH = (float) (((float)kodsatiri / fonksiyonsayisi) * 0.3) ;
            }
            	
            if(YH==0)
            	sapma=0;
            else
            	sapma= ((100 * YG) / YH) - 100;
            
            DecimalFormat format = new DecimalFormat("#.##");
            format.setRoundingMode(java.math.RoundingMode.HALF_UP);
            String yuvarlanmis = format.format(sapma);
            
            if(sinifmi==true) {
            	System.out.println("Sınıf: " + sinif.getName());
                System.out.println("Javadoc Satır Sayısı: " + javadocsatiri);             
                System.out.println("Yorum Satır Sayısı: " + yorumsatiri);
                System.out.println("Kod Satır Sayısı: " + kodsatiri);
                System.out.println("LOC: " + toplamsatir);
                System.out.println("Fonksiyon Sayısı: " + fonksiyonsayisi);               
                System.out.println("Yorum Sapma Yüzdesi: % " + yuvarlanmis);
                System.out.println("--------------------------------");
            }
            
        }
	}
}
