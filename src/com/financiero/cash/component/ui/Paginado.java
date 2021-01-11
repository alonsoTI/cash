package com.financiero.cash.component.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author RONVIL 
 * 
 * @param <E> clase de representacion en la interfaz de usuario
 * @version 1.0
 * @since 1.5
 * 
 */

public abstract class Paginado<E> {

	protected int nroItemsPagina;
	private int nroPagina=1;
		

	public Paginado(int nro) {
		this.nroItemsPagina = nro;
	}

	public abstract int getNroTotalItems();
	public abstract List<E> getItemsPagina();
	
	public List<E> getItems(){
		int tmpNroPagina = this.nroPagina;
		int tmpNroItemsPagina =  this.nroItemsPagina;
		List<E> items = new ArrayList<E>();
		this.nroPagina = 1;
		this.nroItemsPagina =  this.getNroTotalItems();		
		items =  this.getItemsPagina();
		this.nroPagina =  tmpNroPagina;
		this.nroItemsPagina = tmpNroItemsPagina;
		
		return items;
	}
	
	public int getNroItemsPagina() {
		return nroItemsPagina;
	}

	public int getNroPagina() {
		return nroPagina;
	}	
	
	public List<Integer> getPaginas(){
		List<Integer> lista  = new ArrayList<Integer>();
	
		return lista;
	}

	public int getPaginaInicio() {
		return 1;
	}

	public int getPaginaFinal() {	
		int nroTotalItems = getNroTotalItems();
		
		int i = (nroTotalItems/this.nroItemsPagina);
		if( nroTotalItems % nroItemsPagina != 0 ){
			i++;
		}
		return i;
	}
	
	public int getPaginaSiguiente(){
		return nroPagina+1;
	}
	
	public int getPaginaAnterior(){
		return nroPagina-1;
	}	

	
	public boolean getExisteSiguiente(){
		return (nroPagina)*nroItemsPagina+1 <= getNroTotalItems();		
	}
	
	public boolean getExisteAnterior(){
		return nroPagina>1;
	}
	
	
	public int getItemInicioPagina() {
		return (nroPagina-1)* nroItemsPagina;
	}

	public int getItemFinalPagina() {
		int i = getItemInicioPagina() + nroItemsPagina - 1;
		int count = getNroTotalItems() - 1;		
		if (i > count) {
			i = count;
		}
		if (i < 0) {
			i = 0;
		}

		return i;
	}
	
	public void setNroPagina(int nroPagina) {
		this.nroPagina = nroPagina;
	}
	
	public String getEncabezado(){
		StringBuilder sb = new StringBuilder("Pagina ");
		sb = sb.append(this.nroPagina).append(" de ").append(this.getPaginaFinal());
		return sb.toString();
	}

}
