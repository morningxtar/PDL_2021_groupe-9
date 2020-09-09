package fr.istic.pdl1819_grp5;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class wikiMain {

    /**
     * mise en place des loggers
     *
     * @param args
     * @throws IOException
     */

    private static Logger logger = Logger.getLogger("wikiMain.class");

    public static void loggerstart() {
        try {
            logger.setLevel(Level.ALL);
            logger.log(Level.INFO, "EXTRACTIONG OF TABLES FROM WIKIPEDIA PAGES");
            FileHandler fichier = new FileHandler("LoggerFile.xml");
            logger.addHandler(fichier);
        } catch (IOException e) {
            logger.log(Level.INFO, "logger did not load");
        }


    }

    public  void extracteurenmarche() throws  IOException{

        loggerstart();
        WikipediaMatrix wiki = new WikipediaMatrix();
        StatExtractor stat = new StatExtractor();
        Set<UrlMatrix> urlMatrixSet;


        File urlsFile = new File("inputdata\\wikiurls.txt");


        if (!urlsFile.exists() && !urlsFile.isDirectory()) {
            logger.log(Level.INFO, "input file note found");
            System.exit(0);
        }

        File directory = new File("output");


        if (!directory.exists() || !directory.isDirectory()) {
            logger.log(Level.INFO, "bad destination path");
            System.exit(0);
        }


        File htmlDir = new File(directory.getAbsoluteFile() + "" + File.separator + "html");
        File wikitextDir = new File(directory.getAbsoluteFile() + "" + File.separator + "wikitext");
        String url;
        String csvFileName;
        htmlDir.mkdir();
        wikitextDir.mkdir();



        //stat before extraction
        FileWriter wikitablestat = new FileWriter("output\\Wkitable_stat.csv");
        logger.log(Level.INFO, "entering of the function which find tables by criteria");
        logger.log(Level.INFO, "Loading..........");

        stat.statbeforeExtraction(urlsFile, wikitablestat);
        logger.log(Level.INFO, "end of searching table by criteria");

        // Html extraction
        wiki.setUrlsMatrix(getListofUrls(urlsFile));
        wiki.setExtractType(ExtractType.HTML);
        logger.log(Level.INFO, "Extracting via html...");
        //System.out.println("Extracting via html...");

        urlMatrixSet = wiki.getConvertResult();


        //save files
        long execHtml = System.currentTimeMillis();//to measure time of execution
        ArrayList<Integer> extractedHTML = new ArrayList<Integer>();
        ArrayList<String> urls = new ArrayList<String>();

        int numberFileHtml = 0; //Creation of the variable which contains the number of files
        for (UrlMatrix urlMatrix : urlMatrixSet) {
            int i = 0;
            url = urlMatrix.getLink();
            urls.add(url);
            //System.out.println(url);

            Set<FileMatrix> fileMatrices = urlMatrix.getFileMatrix();
            for (FileMatrix f : fileMatrices) {
                //extraction des tableaux de type html au format csv
                csvFileName = mkCSVFileName(url.substring(url.lastIndexOf("/") + 1, url.length()), i);
                f.saveCsv(htmlDir.getAbsolutePath() + File.separator + csvFileName);
                i++;
            }
            extractedHTML.add(i);
            numberFileHtml += i; //Number of files = current value of i
        }
        System.out.println("Extractor HTML created " + numberFileHtml + " files.");//affichage du nombre de tableaux extraits
        System.out.println("Temps d'exécution = " + (System.currentTimeMillis() - execHtml) + " ms");

        // Wikitext extraction
        ArrayList<String> urlsWikitext = new ArrayList<String>();
        ArrayList<Integer> extractedWikitext = new ArrayList<Integer>();
        wiki.setUrlsMatrix(getListofUrls(urlsFile));
        wiki.setExtractType(ExtractType.WIKITEXT);
        logger.log(Level.INFO, "Extracting via wikitext...");
        //System.out.println("Extracting via wikitext...");
        urlMatrixSet = wiki.getConvertResult();
        //save files
        long execWiki = System.currentTimeMillis();//to measure time of execution
        int numberFileWiki = 0; //Creation of the variable which contains the number of files
        for (UrlMatrix urlMatrix : urlMatrixSet) {
            Set<FileMatrix> fileMatrices = urlMatrix.getFileMatrix();
            int i = 0;
            url = urlMatrix.getLink();
            //System.out.println(url);
            urlsWikitext.add(url);

            for (FileMatrix f : fileMatrices) {
                //extraction des tableaux de type wikitext en format csv
                csvFileName = mkCSVFileName(url.substring(url.lastIndexOf("/") + 1, url.length()), i);
                f.saveCsv(wikitextDir.getAbsolutePath() + File.separator + csvFileName);
                i++;
            }
            extractedWikitext.add(i);
            numberFileWiki += i; //Number of files = current value of i

        }
        System.out.println("Extractor Wikitext created " + numberFileWiki + " files.");//affichage du nombre de tableaux extraits
        System.out.println("Temps d'exécution = " + (System.currentTimeMillis() - execWiki) + " ms");

        //stat after extracting tables from two ways
        FileMatrix fm = stat.statafterextracting(urls, extractedHTML, extractedWikitext, urlsWikitext);

        //sauvegarde du fichier statistiques apres extraction
        try {
            fm.saveCsv("output\\statsExtractor.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<UrlMatrix> getListofUrls(File inputFile) {
        logger.entering(wikiMain.class.getName(), "getListofUrls", inputFile);
        try {
            Set<UrlMatrix> urlsMatrix = new HashSet<UrlMatrix>();

            BufferedReader br = null;
            br = new BufferedReader(new FileReader(inputFile));
            String BASE_WIKIPEDIA_URL = "https://en.wikipedia.org/wiki/";
            String url;
            String wurl;
            URL uneURL = null;
            while ((url = br.readLine()) != null) {
                wurl = BASE_WIKIPEDIA_URL + url;
                uneURL = new URL(wurl);
                HttpURLConnection connexion = (HttpURLConnection) uneURL.openConnection();
                if (connexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    urlsMatrix.add(new UrlMatrix(wurl));
                }
            }
            logger.exiting(wikiMain.class.getName(), "getListofUrls", urlsMatrix);
            return urlsMatrix;


        } catch (Exception e) {
            logger.log(Level.SEVERE, "ERR_INTERNET_DISCONNECTED");
            return null;


        }


    }
    private static String mkCSVFileName(String url, int n) {
        return url.trim() + "-" + n + ".csv";
    }


    public static void main(String[] args) throws IOException {
        wikiMain wikimain= new wikiMain();
        wikimain.extracteurenmarche();


    }
}