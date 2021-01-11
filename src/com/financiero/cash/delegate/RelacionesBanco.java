package com.financiero.cash.delegate;

import static com.hiper.cash.util.CashConstants.DDMMYY;
import static com.hiper.cash.util.CashConstants.NRO_PAGINA_IBS;
import static com.hiper.cash.util.CashConstants.RES_IBS;
import static com.hiper.cash.util.Util.strFecha;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.financiero.cash.adapter.CuotaAdapter;
import com.financiero.cash.adapter.LiquidadorAdapter;
import com.financiero.cash.adapter.PrestamoAdapter;
import com.hiper.cash.dao.ws.SixLinux;
import com.hiper.cash.util.Util;

public class RelacionesBanco {

	enum TipoCronograma {DEUDA, PAGO};

	private String nroCuenta;
	private TipoCronograma tipo;

	public RelacionesBanco() {
		
	}

	
	public RelacionesBanco(String nroCuenta, String tipoCronograma) {
		if (tipoCronograma.equals("1")) {
			this.tipo = TipoCronograma.PAGO;
		} else {
			this.tipo = TipoCronograma.DEUDA;
		}
		this.nroCuenta = nroCuenta;
	}

	public int obtenerNroCuotasCronograma() throws Exception {
		String trama = obtenerTramaCronograma(1);
		int i = 36;
		return Integer.parseInt(trama.substring(i, i += 3));
	}

	public PrestamoAdapter obtenerPrestamo() throws Exception {
		List<CuotaAdapter> coutas = new ArrayList<CuotaAdapter>();
		int nroPagina = 1;
		String trama = obtenerTramaCronograma(nroPagina);
		PrestamoAdapter prestamo = new PrestamoAdapter(nroCuenta);
		int indice = 36;		
		if (tipo == TipoCronograma.PAGO) {
			prestamo.setNroCuotas(Integer.parseInt(trama.substring(indice,indice += 3)));
			prestamo.setTasa(IGV(trama, indice, indice += 9));
			prestamo.setFechaApertura(fecha(trama.substring(indice, indice += 6)));
			prestamo.setFechaVencimiento(fecha(trama.substring(indice,indice += 6)));
			prestamo.setSaldo(valor(trama, indice, indice += 13));
			prestamo.setInteres(valor(trama, indice, indice += 13));
			prestamo.setMora(valor(trama, indice, indice += 13));
			prestamo.setICV(valor(trama, indice, indice += 13));
			prestamo.setTotal(valor(trama, indice, indice += 13));
			prestamo.setComision(valor(trama, indice, indice += 13));
			prestamo.setIGV(valor(trama, indice, indice += 13));
			prestamo.setSeguro(valor(trama, indice, indice += 13));
		} else {
			prestamo.setNroCuotas(Integer.parseInt(trama.substring(indice,indice += 3)));
			prestamo.setTasa(IGV(trama, indice, indice += 9));
			prestamo.setFechaApertura(fecha(trama.substring(indice, indice += 6)));
			prestamo.setFechaVencimiento(fecha(trama.substring(indice,indice += 6)));
			prestamo.setSaldo(valor(trama, indice, indice += 11));
			prestamo.setInteres(valor(trama, indice, indice += 11));
			prestamo.setMora(valor(trama, indice, indice += 11));
			prestamo.setICV(valor(trama, indice, indice += 11));
			prestamo.setTotal(valor(trama, indice, indice += 11));
			prestamo.setComision(valor(trama, indice, indice += 11));
			prestamo.setIGV(valor(trama, indice, indice += 11));
			prestamo.setSeguro(valor(trama, indice, indice += 11));
		}
		int nroCuotas = prestamo.getNroCuotas().intValue();
		if (nroCuotas > 0) {
			int ultimaIteracion = nroCuotas % NRO_PAGINA_IBS;
			int nroTotal = (int) nroCuotas / NRO_PAGINA_IBS;
			if (ultimaIteracion != 0) {
				nroTotal++;
			}
			if (nroPagina <= nroTotal) {
				if (nroPagina == nroTotal && ultimaIteracion != 0) {
					coutas.addAll(generarCuotasPagina(trama, ultimaIteracion));
				} else {
					coutas.addAll(generarCuotasPagina(trama, NRO_PAGINA_IBS));
				}
			}
		}
		prestamo.setCuotas(coutas);
		return prestamo;

	}

	public List<CuotaAdapter> generarCuotasPagina(String trama,
			int nroIteraciones) throws Exception {
		List<CuotaAdapter> coutas = new ArrayList<CuotaAdapter>();
		try {
			CuotaAdapter couta = new CuotaAdapter();
			int indice;
			if (tipo == TipoCronograma.DEUDA) {
				indice = 148;
			} else {
				indice = 327;
			}
			for (int i = 0; i < nroIteraciones; i++) {
				couta = new CuotaAdapter();
				if (tipo == TipoCronograma.DEUDA) {
					couta.setNro(Integer.valueOf(trama.substring(indice,indice += 3).trim()));
					couta.setFechaVencimiento(fecha(trama.substring(indice,	indice += 6)));
					couta.setPrincipal(valor(trama, indice, indice += 11));
					couta.setInteres(valor(trama, indice, indice += 11));
					couta.setMora(valor(trama, indice, indice += 11));
					couta.setInteresCompensatorio(valor(trama, indice,indice += 11));
					couta.setTotalCuota(valor(trama, indice, indice += 11));
					indice += 11;
					couta.setComision(valor(trama, indice, indice += 11));
					couta.setIGV(valor(trama, indice, indice += 11));
					couta.setSeguro(valor(trama, indice, indice += 11));
				} else {					
					couta.setNro(Integer.valueOf(trama.substring(indice,indice += 3).trim()));					
					couta.setFechaVencimiento(fecha(trama.substring(indice,	indice += 6)));				
					couta.setPrincipal(valor(trama, indice, indice += 13));					
					couta.setInteres(valor(trama, indice, indice += 13));					
					couta.setMora(valor(trama, indice, indice += 13));				
					couta.setInteresCompensatorio(valor(trama, indice,indice += 13));					
					couta.setTotalCuota(valor(trama, indice, indice += 13));				
					indice += 13;
					couta.setComision(valor(trama, indice, indice += 13));					
					couta.setInteresMoratorio(valor(trama, indice,	indice += 13));					
					couta.setSeguro(valor(trama, indice, indice += 13));				
					couta.setIGV(valor(trama, indice, indice += 13));					
					indice += 39;
				}
				coutas.add(couta);
			}
			return coutas;
		} catch (Exception e) {
			if( coutas.size() == 17 ){
				return coutas;
			}
			throw e;
		}
	}

	public List<CuotaAdapter> generarCronogramaPagina(int nroPagina)
			throws Exception {
		List<CuotaAdapter> coutas = new ArrayList<CuotaAdapter>();
		try {
			String trama = obtenerTramaCronograma(nroPagina);
			int i = 36;
			int nroCuotas = Integer.parseInt(trama.substring(i, i += 3));
			if (nroCuotas > 0) {
				int ultimaIteracion = nroCuotas % NRO_PAGINA_IBS;
				int nroTotal = (int) nroCuotas / NRO_PAGINA_IBS;
				if (ultimaIteracion != 0) {
					nroTotal++;
				}
				if (nroPagina <= nroTotal) {
					if (nroPagina == nroTotal && ultimaIteracion != 0) {
						coutas.addAll(generarCuotasPagina(trama,
								ultimaIteracion));
					} else {
						coutas
								.addAll(generarCuotasPagina(trama,
										NRO_PAGINA_IBS));
					}
				}
			}

			return coutas;
		} catch (Exception e) {
			throw e;
		}
	}

	public PrestamoAdapter obtenerPrestamoTotal() throws Exception {
		PrestamoAdapter prestamo = obtenerPrestamo();
		int nroPaginas = prestamo.getNroCuotas() / NRO_PAGINA_IBS;
		int ultimaIteracion = prestamo.getNroCuotas() % NRO_PAGINA_IBS;
		if (ultimaIteracion != 0) {
			nroPaginas++;
		}
		List<CuotaAdapter> cuotas;
		for (int i = 2; i <= nroPaginas; i++) {
			cuotas = generarCronogramaPagina(i);
			prestamo.agregarCuotas(cuotas);
		}
		return prestamo;
	}
	
	private static final String[] COL_LIQUIDADOR= { 
		"FECHA LIQUIDACION               ", 
		"NRO CUOTAS                      ",
		"CLIENTE                         ", 
		"MONEDA                          ", 
		"FECHA VENCIMIENTO CUOTA         ",
		"FECHA VENCIMIENTO PROXIMA CUOTA ", 
		"INT. COMPENSATORIO VENCIDO      ",
		"INTERES MORATORIO               ",
		"COMISION CUOTA VENCIDA          ",	
		"PAGO PRINCIPAL                  ",
		"PAGO INTERES                    ",
		"SEGUROS                         ",
		"IGV                             ", 
		"SEGURO TODO RIESGO              ", 
		"PORTES                          ",
		"ITF                             ",
		"TOTAL A PAGAR                   ", 
		"TOTAL + ITF                     " };
	
	private String[]  strLiquidador(LiquidadorAdapter l){
		String liquidador[] = new String[18];
		int i = 0;
		liquidador[i++] = l.getStrFechaLiquidacion();
		liquidador[i++] = l.getNroCuotas().toString();
		liquidador[i++] = l.getCliente();
		liquidador[i++] = l.getMoneda();
		liquidador[i++] = l.getStrFechaVencimiento();
		liquidador[i++] = l.getStrFechaVencimientoProximaCuota();
		liquidador[i++] = l.getInteresMoratorio().toString();
		liquidador[i++] = l.getInteresMoratorio().toString();
		liquidador[i++] = l.getComisionCuotaVencida().toString();
		liquidador[i++] = l.getPagoPrincipal().toString();
		liquidador[i++] = l.getPagoInteres().toString();
		liquidador[i++] = l.getSeguro().toString();
		liquidador[i++] = l.getIGV().toString();
		liquidador[i++] = l.getSeguroTodoRiesgo().toString();
		liquidador[i++] = l.getPortes().toString();
		liquidador[i++] = l.getITF().toString();
		liquidador[i++] = l.getTotal().toString();
		liquidador[i++] = String.valueOf(l.getTotalITF());
		return liquidador;
	}
	
	public String generarArchivoLiquidador(String nroCuenta,int nroCoutas,Date fecha) throws Exception{
		StringBuilder sb = new StringBuilder();
		LiquidadorAdapter l = generarLiquidador(nroCuenta, nroCoutas, fecha);
		String strPrestamo[] = strLiquidador(l);
		for(  int i = 0 ; i < 18 ; i++ ){			
			sb.append(Util.ajustarDato(COL_LIQUIDADOR[i], COL_LIQUIDADOR[i].length()));
			sb.append("\t");
			sb.append(Util.ajustarDato(strPrestamo[i], strPrestamo[i].length()));
			sb.append("\r\n");
		}
		
		return sb.toString();
	}
	
	public HSSFWorkbook generarExcelLiquidador(String nroCuenta,int nroCoutas,Date fecha) throws Exception {
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

		String titulo = "LIQUIDADOR";
		hojaXLS = libroXLS.createSheet(titulo);
		int z = 0;		
		int h = 0;
		LiquidadorAdapter l = generarLiquidador(nroCuenta, nroCoutas, fecha);
		String strPrestamo[] = strLiquidador(l);
		for(  int i = 0 ; i < 18 ; i++ ){
			filaXLS = hojaXLS.createRow(z++);				
			h = 0;			
			celdaXLS = filaXLS.createCell((short) h);			
			celdaXLS.setCellStyle(estiloTitulo);
			celdaXLS.setCellValue(COL_LIQUIDADOR[i]);
			hojaXLS.setColumnWidth((short) h, (short) (COL_LIQUIDADOR[i].length() * 256));		
			h++;	
			celdaXLS = filaXLS.createCell((short) h);		
			celdaXLS.setCellStyle(estiloData);
			celdaXLS.setCellValue(strPrestamo[i]);
			hojaXLS.setColumnWidth((short) h, (short) (strPrestamo[i].length() * 256));			
		}
		
		return libroXLS;
	}

	private static final String[] COL_CRONOGRAMA = { "NRO  ", "FECHA       ",
			"PRINCIPAL     ", "INTERES  ", "COMISION  ", "MORA   ", "IGV    ",
			"ICV    ", "TOTAL    ", "SEGURO  " };
	private static final String[] COL_PRESTAMO = {	"Nro Cuotas ","Tasa          ","Fecha Apertura     ",
													"Saldo      ","Total Interes ","Fecha Vencimiento  ",
													"Total Mora ","IGV           ","Int. Comp. Vencido ",
													"Comision   ","Seguro        ","Total Pagar        "};

	public String generarArchivoCronograma() throws Exception {
		StringBuilder sb = new StringBuilder();

		PrestamoAdapter p;
		p = obtenerPrestamoTotal();	
		int nroCuotas = p.getNroCuotas();
		int ultimaIteracion = nroCuotas % NRO_PAGINA_IBS;
		int nroPaginas = (int) nroCuotas / NRO_PAGINA_IBS;
		if (ultimaIteracion != 0) {
			nroPaginas++;
		}
		
		String strPrestamo[] = prestamo(p);
		for(  int i = 0 ; i < 12 ; i++ ){
			if( i % 3 == 0 ){
				sb.append("\r\n");
			}
			sb.append(Util.ajustarDato(COL_PRESTAMO[i], COL_PRESTAMO[i].length()));
			sb.append("\t");
			sb.append(Util.ajustarDato(strPrestamo[i], strPrestamo[i].length()));
			sb.append("\t");
		}
		sb.append("\r\n");
		sb.append("\r\n");
		for (String columna : COL_CRONOGRAMA) {
			sb.append(Util.ajustarDato(columna, columna.length()));
		}
		sb.append("\r\n");
		List<CuotaAdapter> cuotas = p.getCuotas();
		int k = 0;
		for (CuotaAdapter cuota : cuotas) {
			k = 0;
			sb.append(Util.ajustarDato(cuota.getNro().toString(),
					COL_CRONOGRAMA[k++].length()));
			sb.append(Util.ajustarDato(cuota.getStrFechaVencimiento(),
					COL_CRONOGRAMA[k++].length()));
			sb.append(Util.ajustarDato(cuota.getPrincipal(),
					COL_CRONOGRAMA[k++].length()));
			sb.append(Util.ajustarDato(cuota.getInteres(), COL_CRONOGRAMA[k++]
					.length()));
			sb.append(Util.ajustarDato(cuota.getComision(), COL_CRONOGRAMA[k++]
					.length()));
			sb.append(Util.ajustarDato(cuota.getMora(), COL_CRONOGRAMA[k++]
					.length()));
			sb.append(Util.ajustarDato(cuota.getIGV(), COL_CRONOGRAMA[k++]
					.length()));
			sb.append(Util.ajustarDato(cuota.getInteresCompensatorio(),
					COL_CRONOGRAMA[k++].length()));
			sb.append(Util.ajustarDato(cuota.getTotalCuota(),
					COL_CRONOGRAMA[k++].length()));
			sb.append(Util.ajustarDato(cuota.getSeguro(), COL_CRONOGRAMA[k++]
					.length()));
			sb.append("\r\n");
		}

		return sb.toString();
	}
	
	private String[] prestamo(PrestamoAdapter p){
		String[] prestamo = new String[12];
		int i = 0;
		prestamo[i++] = p.getNroCuotas().toString();
		prestamo[i++] = p.getTasa().toString();
		prestamo[i++] = p.getStrFechaApertura();
		prestamo[i++] = p.getSaldo().toString();
		prestamo[i++] = p.getInteres().toString();
		prestamo[i++] = p.getStrFechaVencimiento();
		prestamo[i++] = p.getMora().toString();
		prestamo[i++] = p.getIGV().toString();
		prestamo[i++] = p.getICV().toString();
		prestamo[i++] = p.getComision().toString();
		prestamo[i++] = p.getSeguro().toString();
		prestamo[i++] = p.getTotal().toString();
		return prestamo;
	}
	
	public HSSFWorkbook generarExcelCronograma() throws Exception {

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

		String titulo = "CRONOGRAMA";
		hojaXLS = libroXLS.createSheet(titulo);
		int z = 0;		
		int h = 0;
		PrestamoAdapter p;
		p = obtenerPrestamoTotal();
		String strPrestamo[] = prestamo(p);
		for(  int i = 0 ; i < 12 ; i++ ){
			if( i % 3 == 0 ){
				filaXLS = hojaXLS.createRow(z++);
				h = 0;
			}
			celdaXLS = filaXLS.createCell((short) h);			
			celdaXLS.setCellStyle(estiloTitulo);
			celdaXLS.setCellValue(COL_PRESTAMO[i]);
			hojaXLS.setColumnWidth((short) h, (short) (COL_PRESTAMO[i].length() * 256));		
			h++;	
			celdaXLS = filaXLS.createCell((short) h);		
			celdaXLS.setCellStyle(estiloData);
			celdaXLS.setCellValue(strPrestamo[i]);
			hojaXLS.setColumnWidth((short) h, (short) (strPrestamo[i].length() * 256));
			h++;
		}
		h = 0;
		filaXLS = hojaXLS.createRow(z++);		
		filaXLS = hojaXLS.createRow(z++);		
		for (String columna : COL_CRONOGRAMA) {
			celdaXLS = filaXLS.createCell((short) h);
			celdaXLS.setCellStyle(estiloTitulo);
			celdaXLS.setCellValue(columna);
			hojaXLS.setColumnWidth((short) h, (short) (columna.length() * 256));
			h++;
		}
		int k = 0;	
		for (CuotaAdapter cuota : p.getCuotas()) {
			k = 0;
			filaXLS = hojaXLS.createRow(z++);
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota.getNro(), z, k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota
					.getStrFechaVencimiento(), z, k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota.getPrincipal(),
					z, k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota.getInteres(), z,
					k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota.getComision(),
					z, k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota.getMora(), z, k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota.getIGV(), z, k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota
					.getInteresCompensatorio(), z, k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota.getTotalCuota(),
					z, k);
			k++;
			crearFilaCuota(hojaXLS, filaXLS, estiloData, cuota.getSeguro(), z,
					k);
			k++;

		}
		return libroXLS;
	}

	private void crearFilaCuota(HSSFSheet hojaXLS, HSSFRow filaXLS,
			HSSFCellStyle estiloData, Object campo, int z, int k) {
		short anchoColumna = 0;
		short anchoData = 0;
		HSSFCell celdaXLS = null;
		celdaXLS = filaXLS.createCell((short) k);
		celdaXLS.setCellStyle(estiloData);
		String valor = campo.toString();
		celdaXLS.setCellValue(valor.toString());
		anchoColumna = hojaXLS.getColumnWidth((short) k);
		anchoData = (short) (COL_CRONOGRAMA[k].length() * 256);
		if (anchoData > anchoColumna) {
			hojaXLS.setColumnWidth((short) k, anchoData);
		}

	}

	public LiquidadorAdapter generarLiquidador(String nroCuenta, int nroCoutas,
			Date fecha) throws Exception {
		try {
			LiquidadorAdapter liquidador = new LiquidadorAdapter(nroCuenta);
			liquidador.setNroCuotas(nroCoutas);
			String trama = obtenerTramaLiquidador(nroCuenta, nroCoutas, fecha);
			int indice = 36;
			liquidador.setFechaVencimiento(fecha(trama.substring(indice,
					indice += 6)));
			liquidador.setCliente(trama.substring(indice, indice += 38));
			liquidador.setMoneda(trama.substring(indice, indice += 16));
			liquidador.setFechaVencimientoProximaCuota(fecha(trama.substring(
					indice, indice += 6)));
			if (nroCoutas > 0) {
				if (trama.contains("&")) {
					indice += 4;
				}
				liquidador.setPagoPrincipal(valor(trama, indice, indice += 15));
				indice++;
				liquidador.setPagoInteres(valor(trama, indice, indice += 15));
				indice++;
				liquidador.setICV(valor(trama, indice, indice += 15));
				liquidador.setInteresMoratorio(valor(trama, indice,
						indice += 15));
				liquidador.setPortes(valor(trama, indice, indice += 15));
				liquidador.setComisionCuotaVencida(valor(trama, indice,
						indice += 15));
				liquidador.setSeguro(valor(trama, indice, indice += 15));
				liquidador.setIGV(valor(trama, indice, indice += 15));
				liquidador.setSeguroTodoRiesgo(valor(trama, indice,
						indice += 15));
				liquidador.setTotal(valor(trama, indice, indice += 15));
				indice++;
				//System.out.println("VALOR: " +trama.substring(indice,indice+15) );
				liquidador.setITF(valorRedondeo(trama, indice, indice += 15));
				liquidador.calcularTotal();
				//liquidador.setTotalITF(Double.);
			}
			return liquidador;
		} catch (Exception e) {
			throw e;
		}
	}

	private String obtenerTramaCronograma(int nroPagina) throws Exception {
		if (tipo == TipoCronograma.PAGO) {
			return obtenerTramaCronogramaPago(nroCuenta, nroPagina);
		} else {
			return obtenerTramaCronogramaDeuda(nroCuenta, nroPagina);
		}
	}

	private String obtenerTramaCronogramaDeuda(String nroCuenta, int nroPagina)
			throws Exception {
		StringBuilder sbMensaje = new StringBuilder(strNro(nroPagina))
				.append(nroCuenta);
		String app = RES_IBS
				.getString("consultas.relacionesBanco.cronogramaDeuda.app");
		String trx = RES_IBS
				.getString("consultas.relacionesBanco.cronogramaDeuda.trx");
		String message = sbMensaje.toString();
		short length = Short.parseShort(RES_IBS
				.getString("consultas.relacionesBanco.cronogramaDeuda.length"));
		SixLinux cliente = SixLinux.getInstance();
		String trama = cliente.enviarMensaje(app, trx, length, message);
		return trama;

	}
	
	 private Date fecha(String cadena) {
	        try {
	            DateFormat df = new SimpleDateFormat("ddMMyy");
	            return df.parse(cadena);
	        } catch (Exception e) {
	            return new Date();
	        }
	    }

	private String obtenerTramaCronogramaPago(String nroCuenta, int nroPagina)
			throws Exception {
		StringBuilder sbMensaje = new StringBuilder(strNro(nroPagina))
				.append(nroCuenta);
		String app = RES_IBS.getString("consultas.relacionesBanco.cronogramaPago.app");
		String trx = RES_IBS.getString("consultas.relacionesBanco.cronogramaPago.trx");
		String message = sbMensaje.toString();
		short length = Short.parseShort(RES_IBS.getString("consultas.relacionesBanco.cronogramaPago.length"));
		SixLinux cliente = SixLinux.getInstance();
		String trama = cliente.enviarMensaje(app, trx, length, message);
		return trama;
	}

	public String obtenerTramaLiquidador(String nroCuenta, int nroCuotas,
			Date fecha) throws Exception {
		StringBuilder sbMensaje = new StringBuilder(nroCuenta).append(
				strNro(nroCuotas)).append(strFecha(fecha, DDMMYY));
		String app = RES_IBS
				.getString("consultas.relacionesBanco.liquidador.app");
		String trx = RES_IBS
				.getString("consultas.relacionesBanco.liquidador.trx");
		String message = sbMensaje.toString();
		short length = Short.parseShort(RES_IBS
				.getString("consultas.relacionesBanco.liquidador.length"));
		SixLinux cliente = SixLinux.getInstance();
		String trama = cliente.enviarMensaje(app, trx, length, message);
		return trama;
	}

	private String strNro(int nroCuotas) {
		StringBuilder sb = new StringBuilder();
		if (nroCuotas < 10) {
			sb = sb.append("00").append(nroCuotas);
		} else if (nroCuotas < 100) {
			sb = sb.append("0").append(nroCuotas);
		} else {
			sb = sb.append(nroCuotas);
		}
		return sb.toString();
	}
	
	

	private Double IGV(String trama, int inicio, int fin) throws Exception {
		try {
			int longitud;
			double valor = 0.0;
			String segmento;
			segmento = trama.substring(inicio, fin).trim();
			longitud = segmento.length();
			if (longitud != 0) {
				StringBuilder decimal = new StringBuilder("0.");
				if (longitud == 1 || longitud == 2) {
					decimal = decimal.append(segmento);
					valor = Double.parseDouble(decimal.toString());
				} else {
					decimal = decimal.append(segmento.substring(3));
					valor = Double.valueOf(segmento.substring(0, 3))
							+ Double.valueOf(decimal.toString());
				}
			}
			return valor;
		} catch (NumberFormatException e) {
			return 0.0;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private Double valorRedondeo(String trama, int inicio, int fin) throws Exception {
		try {
			int longitud;
			double valor = 0.0;
			String segmento;
			segmento = trama.substring(inicio, fin).trim();
			longitud = segmento.length();
			if (longitud != 0) {
				StringBuilder decimal = new StringBuilder("0.");
				if (longitud == 1 || longitud == 2) {
					decimal = decimal.append(segmento);
					valor = Double.parseDouble(decimal.toString());
				} else {
					String numero = segmento.substring(longitud - 2);					
					int n = Integer.valueOf(String.valueOf(numero.charAt(1)));
					if( n < 5 ){
						n = 0;
					}else{
						n = 5;
					}
					decimal = decimal.append(numero.charAt(0)).append(n);				
					valor = Double.valueOf(segmento.substring(0, longitud - 2))
							+ Double.valueOf(decimal.toString());
				}
			}
			return valor;
		} catch (NumberFormatException e) {
			return 0.0;
		} catch (Exception e) {
			throw e;
		}
	}


	private Double valor(String trama, int inicio, int fin) throws Exception {
		try {
			int longitud;
			double valor = 0.0;
			String segmento;
			segmento = trama.substring(inicio, fin).trim();
			longitud = segmento.length();
			if (longitud != 0) {
				StringBuilder decimal = new StringBuilder("0.");
				if (longitud == 1 || longitud == 2) {
					decimal = decimal.append(segmento);
					valor = Double.parseDouble(decimal.toString());
				} else {
					decimal = decimal.append(segmento.substring(longitud - 2));				
					valor = Double.valueOf(segmento.substring(0, longitud - 2))
							+ Double.valueOf(decimal.toString());
				}
			}
			return valor;
		} catch (NumberFormatException e) {
			return 0.0;
		} catch (Exception e) {
			throw e;
		}
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public String getTipo() {
		if (tipo == TipoCronograma.PAGO) {
			return "1";
		} else {
			return "2";
		}

	}
}
