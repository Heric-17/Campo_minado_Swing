package com.hlopes.cm.vision;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hlopes.cm.models.Board;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	public BoardPanel(Board board) {
		setLayout(new GridLayout(board.getLinhas(), board.getColunas()));

		board.forEachField(c -> {
			add(new FieldButton(c));
		});
		
		board.registerObserver(e -> {
			SwingUtilities.invokeLater(()->{
				if(e) {
					JOptionPane.showMessageDialog(this, "You win :)");
				} else {				
					JOptionPane.showMessageDialog(this, "You Lose :(");
				}
				board.restart();
			});
		});

	}
}
