package com.siemens.ctic2.Test61400;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HostDialog extends JDialog {
	private JRadioButton local;
	private JRadioButton fritz;
	private JRadioButton other;
	private JTextField host;
	private JButton ok;

	public HostDialog(String title) {
		super();
		this.setTitle(title);
		
		this.setLayout(new GridLayout(6, 1));
		
		local = new JRadioButton("localhost", true);
		fritz = new JRadioButton("ICT Gateway");
		other = new JRadioButton("other");
		host = new JTextField("");
		host.setEnabled(false);
		ok = new JButton("OK");
		
		ButtonGroup group = new ButtonGroup();
		group.add(local);
		group.add(fritz);
		group.add(other);
		
		other.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				host.setEnabled(other.isSelected());
			}});
		
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HostDialog.this.setVisible(false);
			}});
		
		this.add(new JLabel("Connect to:"));
		this.add(local);
		this.add(fritz);
		this.add(other);
		this.add(host);
		this.add(ok);
		
		this.setModal(true);
		this.setSize(200, 160);
	}
	
	protected boolean checkHost() {
		String text = host.getText();
		
		for (char c: text.toCharArray()) {
			if (!Character.isDigit(c) && c != '.') {
				return false;
			}
		}

		return true;
	}

	public String getHost() {
		if (local.isSelected()) {
			return "127.0.0.1";
		} else if (fritz.isSelected()) {
			return "192.168.178.1";
		} else {
			String h = host.getText();
			int col = h.indexOf(':');
			
			if (col < 0) {
				return h;
			} else {
				return h.substring(0, col);
			}
		}
	}
	
	public int getPort() {
		if (local.isSelected()) {
			return 8081;
		} else if (fritz.isSelected()) {
			return 8081;
		} else {
			String h = host.getText();
			int col = h.indexOf(':');
			
			if (col < 0) {
				return 8081;
			} else {
				return Integer.parseInt(h.substring(col+1));
			}
		}
		
	}
}
