package com.hiper.cash.paginado;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import com.financiero.cash.adapter.CuotaAdapter;
import com.financiero.cash.adapter.PrestamoAdapter;
import com.financiero.cash.component.ui.Paginado;
import com.financiero.cash.delegate.RelacionesBanco;

public class CronogramaPaginado extends Paginado<CuotaAdapter>{
	
	Logger logger = Logger.getLogger(getClass());
	private RelacionesBanco rb;	
	
	public CronogramaPaginado(int nro,String nroCuenta,String tipo) {
		super(nro);
		rb = new RelacionesBanco(nroCuenta, tipo);		
	}

	@Override
	public List<CuotaAdapter> getItemsPagina() {
		try{
			return rb.generarCronogramaPagina(this.getNroPagina());		
		}catch(Exception e){			
			logger.error("Paginado Cronograma - Items Pagina", e);
			return new ArrayList<CuotaAdapter>();			
		}				
	}

	@Override
	public int getNroTotalItems() {
		try{
			return rb.obtenerNroCuotasCronograma();					
		}catch(Exception e){
			logger.error("Paginado Cronograma - Nro Total Items", e);
			return 0;
		}
	}
	
	public PrestamoAdapter getPrestamo(){
		try{
			return rb.obtenerPrestamo();		
		}catch(Exception e){
			logger.error("Paginado Prestamo - Nro Total Items", e);
			return new PrestamoAdapter("0000");
		}
	}
	
	public PrestamoAdapter getPrestamoTotal(){
		try{
			return rb.obtenerPrestamoTotal();			
		}catch(Exception e){
			logger.error("Paginado Prestamo Total - Nro Total Items", e);
			return new PrestamoAdapter("");
		}
	}
	
	
	public String obtenerCronogramaTexto(){
		try {			
			return rb.generarArchivoCronograma();		
		} catch (Exception e) {
			logger.error("Paginado Prestamo - Cronograma Texto", e);
			return "";
		}
	}
	
	public HSSFWorkbook  obtenerCronogramaExcel(){
		HSSFWorkbook libroXLS;
		try{
			libroXLS = rb.generarExcelCronograma();
		}catch(Exception e){
			logger.error("Paginado Prestamo - Cronograma Texto", e);
			libroXLS = null;			
		}
		return libroXLS;		
	}
	
	public String getNroCuenta() {
		return rb.getNroCuenta();
	}
	
	public String getTipo() {		
		return rb.getTipo();
	}

}
