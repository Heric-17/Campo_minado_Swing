package com.hlopes.cm.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Board implements FieldObserver {
	final private int linhas;
	final private int colunas;
	final private int minas;

	public void forEachField(Consumer<Field> funcao) {
		fields.forEach(funcao);
	}
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public int getMinas() {
		return minas;
	}

	private final List<Field> fields = new ArrayList<>();
	private final Set<Consumer<Boolean>> observadores = new HashSet<>();

	public void registerObserver(Consumer<Boolean> observer) {
		observadores.add(observer);
	}

	private void notifyObservers(boolean result) {
		observadores.stream().forEach(obs -> obs.accept(result));
	}

	@Override
	public void eventoOcorreu(Field campo, FieldEvent evento) {
		if (evento == FieldEvent.EXPLODIR) {
			mostrarMinas();
			notifyObservers(false);
		} else if (objetivoAlcancado()) {
			notifyObservers(true);
		}
	}

	public Board(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;

		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}

	public void abrir(int linha, int coluna) {
		fields.parallelStream().filter(c -> c.getColuna() == coluna && c.getLinha() == linha).findFirst()
				.ifPresent(c -> c.abrir());
	}

	private void mostrarMinas() {
		fields.stream().
		filter(c -> c.isMinado()).
		filter(c -> !c.isMarcado()).
		forEach(c -> c.setAberto(true));
	}

	public void marcar(int linha, int coluna) {
		fields.parallelStream().filter(c -> c.getColuna() == coluna && c.getLinha() == linha).findFirst()
				.ifPresent(c -> c.alternateMark());
	}

	private void gerarCampos() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Field f1 = new Field(i, j);
				f1.registerObserver(this);
				fields.add(f1);
			}

		}

	}

	private void associarVizinhos() {
		for (Field field1 : fields) {
			for (Field field2 : fields) {
				field1.addVizinho(field2);
			}
		}
	}

	private void sortearMinas() {
		int minasArmadas = 0;
		do {
			int aleatorio = (int) (Math.random() * fields.size());

			fields.get(aleatorio).minar();

			minasArmadas = (int) fields.stream().filter(m -> m.isMinado()).count();
		} while (minasArmadas < minas);
	}

	public boolean objetivoAlcancado() {
		return fields.stream().allMatch(c -> c.objetivoAlcacado());
	}

	public void restart() {
		fields.stream().forEach(c -> c.reinicar());
		sortearMinas();
	}

}
