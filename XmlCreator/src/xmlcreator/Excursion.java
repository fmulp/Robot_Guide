/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmlcreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import xmlcreator.Showpiece;

/**
 *
 * @author Пользователь
 */
public class Excursion {
    public List<Showpiece> showpieces = new ArrayList<Showpiece>();
    public String name=null;
    public int id=0;
    public void genXml(){
        //PrintStream ps=System.out;
        PrintStream ps=null;
        File f=null;
        try {
            f=new File(name+".xml");
            ps = new PrintStream(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Excursion.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        ps.println("<excursion id=\""+id+"\" name=\""+name+"\" >");
        for(Showpiece i:showpieces){
            ps.println("\t<showpiece id=\""+i.getId()+"\" name=\""+i.getName()+"\">");
            ps.println("\t\t<x> "+ i.getX() +" </x>");
            ps.println("\t\t<y> "+ i.getX() +" </y>");
            ps.println("\t\t<phi> "+ i.getPhi() +" </phi>");
            ps.println("\t\t<description> "+ i.getDescription() +" </description>");
            ps.println("\t\t<audio> "+ i.getAudio() +" </audio>");
            ps.println("\t\t<time> "+ i.getTime() +" </time>");
            ps.println("\t</showpiece>");
        }
        ps.println("</excursion>");

        ps.flush();
        ps.close();
        
    }
}
