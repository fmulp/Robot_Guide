/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmlcreator;



import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author Пользователь
 */
public class XmlCreator extends JFrame implements ActionListener{
    private Excursion excursion=new Excursion();
    
    
    private JEditorPane listShowpiece=new JEditorPane();
    
    private JPanel jp1=new JPanel(new GridLayout(4, 2,5,5));
    private GridLayout mainLayot = new GridLayout(1, 2,5,5);
    private JButton buttonGo=new JButton("gen!");
    private JButton addShowpiece=new JButton("add showpiece");
    private JEditorPane nameExcursion = new JEditorPane();
    private JEditorPane idExcursion = new JEditorPane();
    public XmlCreator(){
        this.setLayout(mainLayot);
        listShowpiece.setEditable(false);
        idExcursion.setText("0");
        jp1.add(new JLabel("name excursion",SwingConstants.RIGHT));
        jp1.add(nameExcursion);
        jp1.add(new JLabel("id excursion",SwingConstants.RIGHT));
        jp1.add(idExcursion);
        jp1.add(new JLabel(""));
        jp1.add(addShowpiece);
        jp1.add(new JLabel(""));
        jp1.add(buttonGo);
        this.add(jp1);
        this.add(listShowpiece);
        nameExcursion.setText("ex1");
        addShowpiece.addActionListener(this);
        buttonGo.addActionListener(this);

        this.setSize(640, 480);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==this.addShowpiece){
            new addShowpeice(this);            
        }else if(e.getSource()==this.buttonGo){
            excursion.name=this.nameExcursion.getText();
            excursion.id=Integer.parseInt(this.idExcursion.getText());
            excursion.genXml();
        }

    }
    public void addShowpieces(Showpiece s){
        excursion.showpieces.add(s);
        listShowpiece.setText(listShowpiece.getText()+s.getName()+"\n");
        
    }
}

class addShowpeice extends JDialog implements ActionListener{
    
    private JEditorPane name = new JEditorPane();
    private JEditorPane xCoor = new JEditorPane();
    private JEditorPane yCoor = new JEditorPane();
    private JEditorPane phi = new JEditorPane();
    private JEditorPane description = new JEditorPane();
    private JEditorPane audio = new JEditorPane();
    private JEditorPane time = new JEditorPane();
    private GridLayout mainLayot = new GridLayout(8, 2,5,5);
    private JButton buttonGo=new JButton("add");
    {
        this.setLayout(mainLayot);
        buttonGo.addActionListener(this);

        this.add(new JLabel("name",SwingConstants.RIGHT));
        this.add(name);
        this.add(new JLabel("x",SwingConstants.RIGHT));
        this.add(xCoor);
        this.add(new JLabel("y",SwingConstants.RIGHT));
        this.add(yCoor);
        this.add(new JLabel("phi",SwingConstants.RIGHT));
        this.add(phi);
        this.add(new JLabel("description",SwingConstants.RIGHT));
        this.add(description);
        this.add(new JLabel("audio",SwingConstants.RIGHT));
        this.add(audio);
        this.add(new JLabel("time",SwingConstants.RIGHT));
        this.add(time);
        this.add(new JLabel(""));
        this.add(buttonGo);
        
        name.setText("name");
        description.setText("description");
        audio.setText("audio");
        time.setText("0");
        xCoor.setText("0");
        yCoor.setText("0");
        phi.setText("0");
        this.setSize(320, 240);
        this.setVisible(true);
    }
    public addShowpeice(){        
        
    }
    XmlCreator x=null;
    public addShowpeice(XmlCreator x) {
        this.x=x;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==buttonGo){
            //to doooo
            Showpiece tmp=new Showpiece(
                    Integer.parseInt(xCoor.getText()),
                    Integer.parseInt(yCoor.getText()),
                    Double.parseDouble(phi.getText()),
                    name.getText(),
                    description.getText(),
                    audio.getText(),
                    Integer.parseInt(time.getText())
                    );
            if(x!=null){
                x.addShowpieces(tmp);
            }
            this.setVisible(false);
        }
    }
    
}