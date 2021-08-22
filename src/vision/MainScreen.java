package com.hlopes.cm.vision;

import javax.swing.JFrame;

import com.hlopes.cm.models.Board;

@SuppressWarnings("serial")
public class MainScreen extends JFrame{
	
	
	public MainScreen() {
		
		Board board = new Board(16, 30, 1); 
		
		add(new BoardPanel(board));
		setTitle("Mine Sweaper");
		setSize(690, 438); 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new MainScreen();
	}
}
