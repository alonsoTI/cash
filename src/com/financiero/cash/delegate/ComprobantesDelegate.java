package com.financiero.cash.delegate;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.entidad.BeanDetalleOrden;
import com.hiper.cash.entidad.BeanOrden;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Util;

public class ComprobantesDelegate {

	TpDetalleOrdenDao detalleOrdenDAO = new TpDetalleOrdenDaoHibernate();
	TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();

	private static ComprobantesDelegate instanciaUnica;

	public ComprobantesDelegate() {
		// TODO Auto-generated constructor stub
	}

	public static ComprobantesDelegate getInstance() {
		if (instanciaUnica == null) {
			instanciaUnica = new ComprobantesDelegate();
		}
		return instanciaUnica;
	}

	public List<BeanOrden> buscarOrdenesReferencia(List<Long> servicios,
			String estado, String fechaInicio, String fechaFin,
			String referencia, int inicio, int nroRegistros) throws Exception {
		return ordenDAO.buscarOrdenesReferencia(servicios, estado, fechaInicio,
				fechaFin, referencia, inicio, nroRegistros);
	}
	
	

	public int contarOrdenesReferencia(List<Long> servicios, String estado,
			String fechaInicio, String fechaFin, String referencia)
			throws Exception {
		return ordenDAO.contarOrdenesReferencia(servicios, estado, fechaInicio,
				fechaFin, referencia);
	}
		
	public List<BeanOrden> buscarOrdenesReferenciaNoTrx(List<Long> servicios,
			String estado, String fechaInicio, String fechaFin,
			String referencia, int inicio, int nroRegistros) throws Exception {				
		return ordenDAO.buscarOrdenesReferenciaNoTrx(servicios, estado, fechaInicio,
				fechaFin, referencia, inicio, nroRegistros);
	}
	
	public int contarOrdenesReferenciaNoTrx(List<Long> servicios, String estado,
			String fechaInicio, String fechaFin, String referencia)
			throws Exception {
		return ordenDAO.contarOrdenesReferenciaNoTrx(servicios, estado, fechaInicio,
				fechaFin, referencia);
	}
	
	public List<BeanOrden> buscarOrdenesReferenciaTrx(List<Long> servicios,
			String estado, String fechaInicio, String fechaFin,
			String referencia, int inicio, int nroRegistros) throws Exception {		
		return ordenDAO.buscarOrdenesReferenciaTrx(servicios, estado, fechaInicio,
				fechaFin, referencia, inicio, nroRegistros);
	}
	
	public int contarOrdenesReferenciaTrx(List<Long> servicios, String estado,
			String fechaInicio, String fechaFin, String referencia)
			throws Exception {
		return ordenDAO.contarOrdenesReferenciaTrx(servicios, estado, fechaInicio,
				fechaFin, referencia);
	}
	
	
	public List<BeanDetalleOrden> selectDetallePagoReferencia(String tipo,long orden,long servicio,String referencia,
			int inicio,int maximo) throws Exception{		
		if( tipo.equals(CashConstants.ITEMS_PROCESADO )){			
			return detalleOrdenDAO.selectDetallePagoReferenciaTrx(orden, servicio,referencia, inicio,maximo);			
		}else{
			return detalleOrdenDAO.selectDetallePagoReferenciaNoTrx(orden, servicio,referencia, inicio,maximo);
		}
	}
	
	public int countDetallePagoReferencia(String tipo,long orden,long servicio,String referencia) throws Exception{
		if( tipo.equals(CashConstants.ITEMS_PROCESADO )){
			return detalleOrdenDAO.cuentaDetallePagoReferenciaTrx(orden, servicio,	referencia);
		}else{
			return detalleOrdenDAO.cuentaDetallePagoReferenciaNoTrx(orden, servicio,	referencia);
		}
	}
	
	
	public String generarArchivoOrdenes(List<BeanOrden> ordenes) {
		StringBuilder sb = new StringBuilder();
		for (String columna : COL_ORDEN) {
			sb.append(Util.ajustarDato(columna, columna.length()));
		}
		sb.append("\r\n");
		int k = 0;
		for (BeanOrden orden : ordenes) {
			k = 0;
			sb.append(Util.ajustarDato(orden.getM_IdOrden(), COL_ORDEN[k++]
					.length()));
			sb.append(Util.ajustarDato(orden.getM_Servicio(), COL_ORDEN[k++]
					.length()));
			sb.append(Util.ajustarDato(orden.getM_CuentaCargo(), COL_ORDEN[k++]
					.length()));
			sb.append(Util.ajustarDato(orden.getM_Referencia(), COL_ORDEN[k++]
					.length()));
			sb.append(Util.ajustarDato(orden.getM_FecInicio(), COL_ORDEN[k++]
					.length()));
			sb.append(Util.ajustarDato(orden.getM_FecVenc(), COL_ORDEN[k++]
					.length()));
			sb.append(Util.ajustarDato(orden.getM_DescripEstado(),
					COL_ORDEN[k++].length()));
			sb.append(Util.ajustarDato(orden.getM_Items(), COL_ORDEN[k++]
					.length()));
			sb.append(Util.ajustarDato(orden.getM_ValorSoles(), COL_ORDEN[k++]
					.length()));
			sb.append(Util.ajustarDato(orden.getM_ValorDolares(),
					COL_ORDEN[k++].length()));
			sb.append(Util.ajustarDato(orden.getM_ValorEuros(), COL_ORDEN[k++]
					.length()));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	private static final String[] COL_ORDEN = { "ORDEN   ",
			"SERVICIO                    ", "CUENTA                   ",
			"REFERENCIA                                        ",
			"FECHA INICIO ", "FECHA VENCIMIENTO ", "ESTADO         ",
			"NRO ITEMS  ", "SOLES    ", "DOLARES   ", "EUROS     " };

	public HSSFWorkbook generarExcelOrdenes(String titulo,
			List<BeanOrden> ordenes) {
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
		filaXLS = hojaXLS.createRow(z++);
		int h = 0;
		for (String columna : COL_ORDEN) {
			celdaXLS = filaXLS.createCell((short) h);
			celdaXLS.setCellStyle(estiloTitulo);
			celdaXLS.setCellValue(columna);
			hojaXLS.setColumnWidth((short) h, (short) (((columna == null ? ""
					: columna).length() + 10) * 256));
			h++;
		}
		int k = 0;

		for (BeanOrden orden : ordenes) {
			k = 0;
			filaXLS = hojaXLS.createRow(z++);
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden.getM_IdOrden(),
					z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden.getM_Servicio(),
					z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden
					.getM_CuentaCargo(), z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden
					.getM_Referencia(), z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData,
					orden.getM_FecInicio(), z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden.getM_FecVenc(),
					z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden
					.getM_DescripEstado(), z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden.getM_Items(), z,
					k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden
					.getM_ValorSoles(), z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden
					.getM_ValorDolares(), z, k);
			k++;
			crearFilaOrden(hojaXLS, filaXLS, estiloData, orden
					.getM_ValorEuros(), z, k);
			k++;
		}

		return libroXLS;
	}

	private void crearFilaOrden(HSSFSheet hojaXLS, HSSFRow filaXLS,
			HSSFCellStyle estiloData, Object campo, int z, int k) {
		short anchoColumna = 0;
		short anchoData = 0;
		HSSFCell celdaXLS = null;
		celdaXLS = filaXLS.createCell((short) k);
		celdaXLS.setCellStyle(estiloData);
		String valor = "";
		if (campo != null) {
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
