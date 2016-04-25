package com.siemens.ctic2.Test61400;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class DataPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 707260567150656189L;
	private String[] line = new String[3];
	private JLabel[] label = new JLabel[3];
	private String[] data = {"", "", ""};

	public DataPanel(String title, String[] line) {
		this.setLayout(new GridLayout(4, 1));
		JLabel titleLabel = createText("  " + title, "");
		this.add(titleLabel);
		
		for (int i = 0; i < 3; i++) {
			this.line[i] = line[i] + "  ";
			label[i] = createText(this.line[i], data[i]);
			label[i].setHorizontalAlignment(SwingConstants.TRAILING);
			this.add(label[i]);
		}
		
		this.setBorder(new LineBorder(Color.lightGray, 5));
		this.setBackground(Color.white);
	}
	
	private JLabel createText(String text, String data) {
		text = text.replace("%", data);
		JLabel label = new JLabel(text);
		Font font = new Font("Arial", Font.BOLD, 20);
		label.setFont(font);
		return label;
	}
	
	public void setData(int i, String data) {
		this.data[i] = data;
		this.label[i].setText(line[i].replace("%", data));
	}
}
