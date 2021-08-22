package com.hlopes.cm.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Field {

	private final int linha;
	private final int coluna;

	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;

	private List<Field> vizinhos = new ArrayList<>();
	private Set<FieldObserver> observadores = new HashSet<>();
	
	public void registerObserver(FieldObserver observer) {
		observadores.add(observer);
	}
	
	private void notifyObservers(FieldEvent event) {
		observadores.stream().forEach(obs -> obs.eventoOcorreu(this, event));
	}
	
	public Field(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	boolean addVizinho(Field vizinho) {
		boolean candidatoAVizinhoLinha = linha != vizinho.linha;
		boolean candidatoAVizinhocoluna = coluna != vizinho.coluna;

		boolean isDiagonal = candidatoAVizinhocoluna && candidatoAVizinhoLinha;

		int diferencaLinha = Math.abs(linha - vizinho.linha);
		int diferencacoluna = Math.abs(coluna - vizinho.coluna);
		int deltaDif = diferencacoluna + diferencaLinha;

		if (isDiagonal && deltaDif == 2) {
			vizinhos.add(vizinho);
			return true;
		} else if (!isDiagonal && deltaDif == 1) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}

	public void alternateMark() {
		if (!aberto) {
			marcado = !marcado;
		}
		
		if(marcado) {
			notifyObservers(FieldEvent.MARCAR);
		} else {			
			notifyObservers(FieldEvent.DESMARCAR);
		}
	}

	void minar() {
		minado = true;
	}

	public boolean abrir() {
		if (!aberto && !marcado) {

			if (minado) {
				notifyObservers(FieldEvent.EXPLODIR);
				return true;
			}

			setAberto(true);

			if (vizinhacaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean vizinhacaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(aberto) {
			notifyObservers(FieldEvent.ABRIR);
		}					
	}

	public boolean isMarcado() {
		return marcado;
	}
	
	public boolean isMinado() {
		return minado;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	boolean objetivoAlcacado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;

		return desvendado || protegido;
	}

	public long minasVisinhaca() {
		return vizinhos.stream().filter(v -> v.minado).count();
	}

	void reinicar() {
		aberto = false;
		minado = false;
		marcado = false;
		notifyObservers(FieldEvent.REINICIAR);
	}

}
