package com.hlopes.cm.vision;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.hlopes.cm.models.Field;
import com.hlopes.cm.models.FieldEvent;
import com.hlopes.cm.models.FieldObserver;

@SuppressWarnings("serial")
public class FieldButton extends JButton implements FieldObserver, MouseListener {

	private final Color BG_PADRAO = new Color(184, 184, 184);
	private final Color BG_MARCADO = new Color(8, 179, 247);
	private final Color BG_EXPLODIR = new Color(255, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);

	private Field field;

	public FieldButton(Field field) {
		this.field = field;
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));

		setOpaque(true);
		addMouseListener(this);
		field.registerObserver(this);
	}

	@Override
	public void eventoOcorreu(Field campo, FieldEvent evento) {
		switch (evento) {
		case ABRIR:
			aplcarEstiloAbrir();
			break;

		case MARCAR:
			aplcarEstiloMarcar();
			break;

		case DESMARCAR:
			aplcarEstiloPadrao();
			break;

		case EXPLODIR:
			aplcarEstiloExplodir();
			break;
		default:
			aplcarEstiloPadrao();
		}
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	private void aplcarEstiloPadrao() {
		setBorder(BorderFactory.createBevelBorder(0));
		setBackground(BG_PADRAO);
		setText("");
	}

	private void aplcarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");
	}

	private void aplcarEstiloMarcar() {
		setBackground(BG_MARCADO);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplcarEstiloAbrir() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		if (field.isMinado()) {
			setBackground(BG_EXPLODIR);
			return;
		}
		setBackground(BG_PADRAO);

		switch ((int) field.minasVisinhaca()) {
		case 1:
			setForeground(TEXTO_VERDE);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}

		String valor = !field.vizinhacaSegura() ? field.minasVisinhaca() + "" : "";
		setText(valor);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			field.abrir();
		} else {
			field.alternateMark();
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
