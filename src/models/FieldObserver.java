package models;

@FunctionalInterface
public interface FieldObserver {
	
	public void eventoOcorreu(Field campo, FieldEvent evento);

}
