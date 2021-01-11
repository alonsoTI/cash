package com.financiero.cash.delegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.financiero.cash.dao.hibernate.HibernateHistoricoDAO;
import com.financiero.cash.dao.service.HistoricoDAO;
import com.financiero.cash.ui.model.DetalleOrdenEC;
import com.financiero.cash.ui.model.EmpresaEC;
import com.financiero.cash.ui.model.OrdenHistoricoEC;
import com.financiero.cash.ui.model.ServicioEC;
import com.hiper.cash.util.Util;

public class HistoricosDelegate {
	
	private Logger logger = Logger.getLogger(this.getClass());

	private static HistoricosDelegate instanciaUnica;

	private HistoricoDAO daoHistoricos;
	
	public HistoricosDelegate() {
		try{
			daoHistoricos = new HibernateHistoricoDAO();
		}catch(Exception e){
			logger.error("Creacion del HistoricoDelegate", e);
		}
	}
	
	public static HistoricosDelegate  getInstance() {
		if (instanciaUnica == null) {
			instanciaUnica = new HistoricosDelegate();
		}
		return instanciaUnica;
	}	
	
	public List<EmpresaEC> getEmpresasEC(String nroTarjeta) throws Exception{		
		List<EmpresaEC> empresas = new ArrayList<EmpresaEC>();		
		List<String> ids=  daoHistoricos.obtenerIdEmpresa(nroTarjeta);
		List<Integer> codigos = daoHistoricos.obtenerCodigoEmpresaEasyCash(ids);
		empresas  = daoHistoricos.getEmpresas(codigos);
		return empresas;
	}
	
	
	public List<ServicioEC> getServiciosEC(String empresa) throws Exception{		
		if( empresa.equals("00")){
			return new ArrayList<ServicioEC>();
		}else{
			return daoHistoricos.getServicios(empresa);	
		}		
	}	
	
	
	public List<OrdenHistoricoEC> getOrdenes(String empresa, String servicio,
			Date fInicio, Date fFinal,String referencia,int inicio,int nroRegistros)throws Exception{
		return daoHistoricos.getOrdenes(empresa, servicio, fInicio, fFinal, referencia, inicio, nroRegistros);
	}
	
	public int cuentaOrdenes(String empresa, String servicio,
			Date fInicio, Date fFinal,String referencia)throws Exception{
		return daoHistoricos.cuentaOrdenes(empresa, servicio, fInicio, fFinal, referencia);
	}
	
	public List<DetalleOrdenEC> getDetallesOrden(int orden,String referencia,int inicio,int nroRegistros)throws Exception{		
		return daoHistoricos.getDetallesOrden(orden, referencia, inicio, nroRegistros);
	}
	
	public int cuentaDetallesOrden(int orden,String referencia)throws Exception{
		return daoHistoricos.cuentaDetallesOrden(orden, referencia);		
	}
	
	public String nombreEmpresa(int idEmpresa) throws Exception{
		return daoHistoricos.buscarNombreEmpresa(idEmpresa);
	}
	
	public String nombreServicio(int idServicio) throws Exception{
		return daoHistoricos.buscarNombreServicio(idServicio);
	}	
	
	public String genearArchivoOrdenHistoricoEC(List<OrdenHistoricoEC> ordenes){
		StringBuilder sb = new StringBuilder();
		for( String columna : COL_ORDEN_HISTORICO_EC ){
			sb.append(Util.ajustarDato(columna, columna.length()));		
		}
		sb.append("\r\n");
		int k = 0;
		for( OrdenHistoricoEC orden : ordenes ){
			k = 0;
			sb.append(Util.ajustarDato(orden.getIdSobre().toString(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getCuenta(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getCodigoMoneda(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getReferencia(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getEstadoSobre(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getFechaInicio(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getFechaVencimiento(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getNroItems().toString(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getValorSobre().toString(),COL_ORDEN_HISTORICO_EC[k++].length()));
			sb.append("\r\n");
		}
		
		return sb.toString();
	}	

	public HSSFWorkbook generarExcelOrdenHistoricoEC(String titulo,
			 List<OrdenHistoricoEC> ordenes) {
		
		HSSFWorkbook libroXLS = null;
		HSSFSheet hojaXLS = null;
		HSSFRow filaXLS = null;
		HSSFCell celdaXLS = null;
		HSSFCellStyle estiloTitulo = null;
		HSSFFont fuenteTitulo = null;		
		HSSFCellStyle estiloData = null;
		HSSFFont fuenteData = null;
		
		libroXLS = new HSSFWorkbook();
		estiloTitulo = libroXLS.createCellStyle();
		estiloData = libroXLS.createCellStyle();
	

		fuenteTitulo = libroXLS.createFont();
		fuenteData = libroXLS.createFont();
		
		fuenteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
		fuenteTitulo.setFontHeight((short) 160);
		fuenteTitulo.setFontName("Arial");
		fuenteTitulo.setColor(HSSFColor.WHITE.index);

		fuenteData.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		fuenteData.setFontHeight((short) 160);
		fuenteData.setFontName("Arial");

		estiloTitulo.setFont(fuenteTitulo);
		estiloTitulo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setBorderRight(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setBorderTop(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloTitulo.setFillForegroundColor(HSSFColor.BLUE.index);	
		estiloTitulo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		
		hojaXLS = libroXLS.createSheet(titulo);
		int z = 0;
		if (titulo != null) { 							
			filaXLS = hojaXLS.createRow(z++);
			celdaXLS = filaXLS.createCell((short) 0);
			celdaXLS.setCellStyle(estiloTitulo);
			celdaXLS.setCellValue(titulo);
			hojaXLS.setColumnWidth((short) 0,
					(short) ((titulo.length() + 10) * 256));
			filaXLS = hojaXLS.createRow(z++);
		}
		filaXLS = hojaXLS.createRow(z++);
		int h = 0;
		for( String columna : COL_ORDEN_HISTORICO_EC ){
			celdaXLS = filaXLS.createCell((short) h);
			celdaXLS.setCellStyle(estiloTitulo);
			celdaXLS.setCellValue(columna);
			hojaXLS.setColumnWidth((short) h, (short) (((columna == null ? ""
					: columna).length() + 10) * 256));
			h++;
		}		
		int k= 0;		
		for( OrdenHistoricoEC orden : ordenes){
			k = 0;
			filaXLS = hojaXLS.createRow(z++);
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getIdSobre(), z, k);
			k++;
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getCuenta(), z, k);			
			k++;	
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getCodigoMoneda(), z, k);
			k++;
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getReferencia(), z, k);			
			k++;	
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getEstadoSobre(), z, k);
			k++;
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getFechaInicio(), z, k);			
			k++;	
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getFechaVencimiento(), z, k);
			k++;
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getNroItems(), z, k);			
			k++;	
			crearFilaOrdenHistoricoEC(hojaXLS,filaXLS ,estiloData, orden.getValorSobre(), z, k);		
			
		}
		return libroXLS;
	}
	
	private static final String[] COL_ORDEN_HISTORICO_EC = {"ORDEN     ","CUENTA                   ","MONEDA  ","REFERENCIA                                        ","ESTADO               ","FECHA INICIO ","FECHA VENCIMIENTO ","NRO ITEMS  ","VALOR   "}; 
	private static final String[] COL_DETALLE_ORDEN_EC = {"ORDEN      ","ITEM      ","PAIS     ","BANCO           ",
					"CUENTA                    ","CONTRAPARTIDA				","REFERENCIA                                      ",
					"CONTRAPARTIDA ADICIONAL                     ","REFERENCIA ADICIONAL                   ",
					"ESTADO               ","MONEDA	","FECHA PROCESO	","VALOR	"
					}; 

	
	private void crearFilaOrdenHistoricoEC( HSSFSheet hojaXLS,HSSFRow filaXLS ,HSSFCellStyle estiloData,Object campo,int z,int k){
		short anchoColumna = 0;
		short anchoData = 0;		
		HSSFCell celdaXLS = null;				
		celdaXLS = filaXLS.createCell((short) k);
		celdaXLS.setCellStyle(estiloData);			
		String valor = campo.toString();		
		celdaXLS.setCellValue(valor.toString());		
		anchoColumna = hojaXLS.getColumnWidth((short) k);
		anchoData = (short) (((valor == null ? "" : valor).length() + 10) * 256);
		if (anchoData > anchoColumna) {
			hojaXLS.setColumnWidth((short) k, anchoData);
		}			
	}
	
	public String generarArchivoDetallesOrdenEC(List<DetalleOrdenEC> ordenes){
		StringBuilder sb = new StringBuilder();		
		for( String columna : COL_DETALLE_ORDEN_EC ){
			sb.append(Util.ajustarDato(columna, columna.length()));		
		}
		sb.append("\r\n");
		int k = 0;
		for( DetalleOrdenEC orden : ordenes ){
			k = 0;
			sb.append(Util.ajustarDato(orden.getIdSobre().toString(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getIdDetalleOrden().toString(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getPais(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getBanco(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getCuenta(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getContraPartida(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getReferencia(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getContraPartidaAdicional(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getReferenciaAdicional(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getMensajeProceso(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getMoneda(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getFechaProceso().toString(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append(Util.ajustarDato(orden.getValor().toString(),COL_DETALLE_ORDEN_EC[k++].length()));
			sb.append("\r\n");
		}		
	
		return sb.toString();
	}	

	
	
	public HSSFWorkbook generarExcelDetallesOrdenEC(String titulo,
			 List<DetalleOrdenEC> ordenes) {
		
		HSSFWorkbook libroXLS = null;
		HSSFSheet hojaXLS = null;
		HSSFRow filaXLS = null;
		HSSFCellStyle estiloTitulo = null;
		HSSFFont fuenteTitulo = null;		
		HSSFCellStyle estiloData = null;
		HSSFFont fuenteData = null;
		HSSFCell celdaXLS = null;
		
		libroXLS = new HSSFWorkbook();
		estiloTitulo = libroXLS.createCellStyle();
		estiloData = libroXLS.createCellStyle();
	

		fuenteTitulo = libroXLS.createFont();
		fuenteData = libroXLS.createFont();
		
		fuenteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
		fuenteTitulo.setFontHeight((short) 160);
		fuenteTitulo.setFontName("Arial");
		fuenteTitulo.setColor(HSSFColor.WHITE.index);

		fuenteData.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		fuenteData.setFontHeight((short) 160);
		fuenteData.setFontName("Arial");

		estiloTitulo.setFont(fuenteTitulo);
		estiloTitulo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setBorderRight(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setBorderTop(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloTitulo.setFillForegroundColor(HSSFColor.BLUE.index);	
		estiloTitulo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		int nroHoja = 1;
		int z = 0;
		
		
		int k= 0;		
		int filas = 0;		
		int NRO_PAGINA_EXCEL = 10000;
		Iterator<DetalleOrdenEC> itOrdenes = ordenes.iterator();		
		  
		 hojaXLS = libroXLS.createSheet(titulo+ ""+(nroHoja++));
		 z = 0;
		if (titulo != null) { 							
			filaXLS = hojaXLS.createRow(z++);
			celdaXLS = filaXLS.createCell((short) 0);
			celdaXLS.setCellStyle(estiloTitulo);
			celdaXLS.setCellValue(titulo);
			hojaXLS.setColumnWidth((short) 0,(short) ((titulo.length() + 10) * 256));
			filaXLS = hojaXLS.createRow(z++);
		}
		filaXLS = hojaXLS.createRow(z++);
		int h = 0;
		for( String columna : COL_DETALLE_ORDEN_EC ){
			celdaXLS = filaXLS.createCell((short) h);
			celdaXLS.setCellStyle(estiloTitulo);
			celdaXLS.setCellValue(columna);
			hojaXLS.setColumnWidth((short) h, (short) (((columna == null ? "": columna).length() + 10) * 256));
			h++;
		}
		DetalleOrdenEC orden;
		 while( itOrdenes.hasNext() && filas < NRO_PAGINA_EXCEL ){
			k=0;
			orden = itOrdenes.next();
			filaXLS = hojaXLS.createRow(z++);
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getIdSobre(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getIdDetalleOrden(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getPais(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getBanco(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getCuenta(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getContraPartida(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getReferencia(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getContraPartidaAdicional(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getReferenciaAdicional(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getMensajeProceso(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getMoneda(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getFechaProceso(), z, k);
			k++;
			crearFilaDetalleOrdenEC(hojaXLS,filaXLS ,estiloData, orden.getValor(), z, k);
			k++;
			filas++;
		}		
	
		return libroXLS;
	}
	
	private void crearFilaDetalleOrdenEC( HSSFSheet hojaXLS,HSSFRow filaXLS ,HSSFCellStyle estiloData,Object campo,int z,int k){
		short anchoColumna = 0;
		short anchoData = 0;		
		HSSFCell celdaXLS = null;				
		celdaXLS = filaXLS.createCell((short) k);		
		celdaXLS.setCellStyle(estiloData);			
		String valor = "";
		if( campo != null ){
			valor = campo.toString();
		}
		celdaXLS.setCellValue(valor.toString());		
		anchoColumna = hojaXLS.getColumnWidth((short) k);
		anchoData = (short) (((valor == null ? "" : valor).length() + 10) * 256);
		if (anchoData > anchoColumna) {
			hojaXLS.setColumnWidth((short) k, anchoData);
		}	
		
	}
}

