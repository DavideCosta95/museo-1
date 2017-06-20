package it.uniroma3.progetto.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.ServletContextEvent;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploaderImmagine {

    private static final String TOMCAT_HOME_PATH = System.getProperty("catalina.home");
    private static final String PERCORSO_IMMAGINI = TOMCAT_HOME_PATH + File.separator + "immagini";

    private static final File CARTELLA_IMMAGINI = new File(PERCORSO_IMMAGINI);
    private static final String ABSOLUTE_PATH_CARTELLA_IMMAGINI = CARTELLA_IMMAGINI.getAbsolutePath() + File.separator;


    public void creaCartellaImmagini() {
        if (!CARTELLA_IMMAGINI.exists())
        	CARTELLA_IMMAGINI.mkdirs();
    }


	public void creaFileImmagine(String nome, MultipartFile file) {
        try {

            File immagine = new File(this.getClass().getResource("/static/immagini/").getPath()+""+nome);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(immagine));
            stream.write(file.getBytes());
            stream.close();
        } catch (Exception e) {
        	e.printStackTrace();
     
        }
    }
	
	public void rimuoviImmagine(String nome){
		File file = getPercorsoFile(nome);
		file.delete();
	}
	
	public File getPercorsoFile(String nomeImmagine){
       return new File(ABSOLUTE_PATH_CARTELLA_IMMAGINI + nomeImmagine);
	}

}