/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package royalsociety;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class RoyalSociety {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Display Html Content of a Website
            // set website:
            String address = "http://royalsociety.org/DServe/dserve.exe?dsqIni=Dserve.ini&dsqApp=Archive&dsqDb=Catalog&dsqCmd=NaviTree.tcl&dsqField=RefNo&dsqItem=EC#HERE";
            StringBuilder textDisp = new StringBuilder();
            ArrayList years = new ArrayList(),
                    people = new ArrayList();
            // set up:
            URL url;
            BufferedReader in = null;

            url = new URL(address);

            try {
                // try open stream!
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                // write line by line into my text
                String line;
                while ((line = in.readLine()) != null) { // try to read line!
                    textDisp.append(line).append("\n");
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
            // (try this!)
            in.close();

            String homePage = textDisp.toString();
            String year;
            int fromIndex = 0;
            int baseIndex;
            while (homePage.indexOf("<td class=\"treeicon1\"><a href=\"", fromIndex) != -1) {
                baseIndex = homePage.indexOf("<td class=\"treeicon1\"><a href=\"", fromIndex)
                        + "<td class=\"treeicon1\"><a href=\"".length();
                year = "http://royalsociety.org" + homePage.substring(baseIndex,
                        homePage.indexOf("\">+</a>", baseIndex));
                year = year.replace("amp;", "");
                years.add(year);
                fromIndex = homePage.indexOf("\">+</a>", baseIndex);
            }

            for (int i = 0; i < years.size(); i++) {
            //for (int i = 0; i< 2; i++) {
                textDisp = new StringBuilder();
                url = new URL(years.get(i).toString());

                try {
                    // try open stream!
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    // write line by line into my text
                    String line;
                    while ((line = in.readLine()) != null) { // try to read line!
                        textDisp.append(line).append("\n");
                    }
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
                // (try this!)
                in.close();

                homePage = textDisp.toString();
                String person;
                fromIndex = 0;
                while (homePage.indexOf("<a class=\"Tree2Show\" href=\"", fromIndex) != -1) {
                    baseIndex = homePage.indexOf("<a class=\"Tree2Show\" href=\"", fromIndex)
                            + "<a class=\"Tree2Show\" href=\"".length();
                    person = "http://royalsociety.org" + homePage.substring(baseIndex,
                            homePage.indexOf("\" onclick=", baseIndex));
                    person = person.replace("amp;", "");
                    people.add(person);
                    System.out.println(person);
                    fromIndex = homePage.indexOf("\" onclick=", baseIndex);
                }
            }
            
            PrintWriter out = new PrintWriter(new FileWriter("./data.csv"));
            String yearField, numberField, titleField, proposersField, personKeyField;
            out.println("\"Year\",\"Number\",\"Title\",\"Proposers\",\"Person Key\"");         
            
            for (int i = 0; i < people.size(); i++) {
            //for (int i = 0; i < 2; i++) {
                textDisp = new StringBuilder();
                url = new URL(people.get(i).toString());

                try {
                    // try open stream!
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    // write line by line into my text
                    String line;
                    while ((line = in.readLine()) != null) { // try to read line!
                        textDisp.append(line).append("\n");
                    }
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
                // (try this!)
                in.close();

                homePage = textDisp.toString();
                baseIndex = homePage.indexOf("'_top'))\">EC/", homePage.indexOf("<td class=\"RefNo\">"));
                if (baseIndex != -1)
                    baseIndex = baseIndex + "'_top'))\">EC/".length();
                
                try{
                    yearField = homePage.substring(baseIndex, baseIndex+4);
                } catch (IndexOutOfBoundsException e) {
                    yearField = "";
                }
                try {
                    numberField = homePage.substring(baseIndex+5, baseIndex+7);
                } catch (IndexOutOfBoundsException e) {
                    numberField = "";
                }               
                try {
                    baseIndex = homePage.indexOf("<td class=\"Title\">");
                    if (baseIndex != -1) {
                        baseIndex = baseIndex + "<td class=\"Title\">".length();
                    }
                    titleField = homePage.substring(baseIndex,
                            homePage.indexOf("</td>", baseIndex)-1);
                } catch (IndexOutOfBoundsException e) {
                    titleField = "";
                }                   
                try {
                    baseIndex = homePage.indexOf("<td class=\"Proposers\">");
                    if (baseIndex != -1) {
                        baseIndex = baseIndex + "<td class=\"Proposers\">".length();
                    }
                    proposersField = homePage.substring(baseIndex,
                            homePage.indexOf("</td>", baseIndex)-1);
                } catch (IndexOutOfBoundsException e) {
                    proposersField = "";
                }
                try {
                    baseIndex = homePage.indexOf("<td class=\"PersonKey\">");
                    if (baseIndex != -1) {
                        baseIndex = baseIndex  + "<td class=\"PersonKey\">".length();
                    }
                    personKeyField = homePage.substring(baseIndex,
                            homePage.indexOf("</td>", baseIndex)-1);
                } catch (IndexOutOfBoundsException e) {
                    personKeyField = "";
                }  
                System.out.println("\""+yearField+"\",\""+
                            numberField+"\",\""+
                            titleField+"\",\""+
                            proposersField+"\",\""+
                            personKeyField+"\"");
                out.println("\""+yearField+"\",\""+
                            numberField+"\",\""+
                            titleField+"\",\""+
                            proposersField+"\",\""+
                            personKeyField+"\"");
            }
            out.close();
        } catch (Exception ex) {
            Logger.getLogger(RoyalSociety.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
