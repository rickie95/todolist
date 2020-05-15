package com.riccardomalavolti.apps.todolist.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MessageBoxFactory {
	
	private JFrame parent;

	public MessageBoxFactory(MainView parent) {
		this.parent = parent;
	}

	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this.parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showErrorMessage(JFrame parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
}
