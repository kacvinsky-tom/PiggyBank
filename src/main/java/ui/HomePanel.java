package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class HomePanel {

    private final JPanel panel = new JPanel(new GridBagLayout());

    private final double income = new Random().ints(0,1000).findFirst().getAsInt();
    private final double expenses = -new Random().ints(0,1000).findFirst().getAsInt();

    HomePanel(){
        createHomePanel();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void createHomePanel(){
//        panel.setBackground(new Color(250, 255, 255));
        Font fontSubTitle = new Font("arial", Font.PLAIN, 30);
        Font fontTitle = new Font("arial", Font.BOLD, 40);
        Font fontNumbers = new Font("arial", Font.BOLD, 25);
        addJlabel("Your balance",1,0,0, 0, fontTitle, Color.black, panel);
        addJlabel("Income",0,1,40, 0, fontSubTitle, Color.black, panel);
        addJlabel("Margin",1,2,40, 0, fontSubTitle, Color.black, panel);
        addJlabel("Expenses",2,1,40, 10, fontSubTitle, Color.black, panel);
        addJlabel(income + "€",0,2,40, 0, fontNumbers, new Color(101, 168, 47), panel);
        addJlabel(income+expenses + "€",1,3,40, 0, fontNumbers, Color.black, panel);
        addJlabel(expenses + "€",2,2,40, 0, fontNumbers, new Color(168, 43, 43), panel);
    }

    private void addJlabel(String text, int gridx, int gridy, int ipadx, int ipady, Font font, Color color, JPanel panel){
        var label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(color);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = gridx;
        c.gridy = gridy;
        c.ipadx = ipadx;
        c.ipady = ipady;
        if (text.equals("Your balance"))
            c.insets = new Insets(0,0,50,0);

        if (text.equals("Margin")){
            c.insets = new Insets(0,0,10,0);
        }
        label.setFont(font);
        panel.add(label, c);
    }
}
