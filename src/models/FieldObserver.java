package com.hlopes.cm.models;

@FunctionalInterface
public interface FieldObserver {
	
	public void eventoOcorreu(Field campo, FieldEvent evento);

}
