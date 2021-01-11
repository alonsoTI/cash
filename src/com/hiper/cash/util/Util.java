/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.util;

import static com.hiper.cash.util.CashConstants.DDMMYY;
import static com.hiper.cash.util.CashConstants.DD_MM_YYYY;
import static com.hiper.cash.util.CashConstants.FR_DOUBLE;
import static com.hiper.cash.util.CashConstants.STR_DDMMYY;
import static com.hiper.cash.util.CashConstants.STR_DD_MM_YYYY;
import static com.hiper.cash.util.CashConstants.STR_YYYYMMDD;
import static com.hiper.cash.util.CashConstants.YYYYMMDD;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.financiero.cash.util.TipoMoneda;

/**
 * 
 * @author esilva Modificado por andy 16/06/2011
 */
public class Util {

	static Logger logger = Logger.getLogger(Util.class);
	private static DateFormat dfFecha;
	private final static Locale localeUS = new Locale("en_US");

	public static String getFechaActualSQL() {
		java.util.Calendar fecha = java.util.Calendar.getInstance();
		int dd = fecha.get(java.util.Calendar.DAY_OF_MONTH);
		int mm = fecha.get(java.util.Calendar.MONTH) + 1;
		String tmpDD = "" + dd + "";
		String tmpMM = "" + mm + "";
		int yy = fecha.get(java.util.Calendar.YEAR);
		if (dd < 10) {
			tmpDD = "0" + dd;
		}
		if (mm < 10) {
			tmpMM = "0" + mm;
		}
		String fechaactual = yy + "" + tmpMM + "" + tmpDD;
		return fechaactual;
	}
	
	

	public static String formatearMonto(String monto, int nroDecimales) {
		String result = "";
		if (monto != null && monto.trim().length() > 0) {
			if (nroDecimales > 0) {
				try {
					double importe = (double) (Double.parseDouble(monto.trim()) / Math
							.pow(10, nroDecimales));
					// le damos el formato a el numero generado
					DecimalFormatSymbols dfs = new DecimalFormatSymbols();
					dfs.setDecimalSeparator('.');
					dfs.setGroupingSeparator(' ');
					dfs.setMonetaryDecimalSeparator('.');
					dfs.setNaN("");
					DecimalFormat df = new DecimalFormat();
					df.setDecimalFormatSymbols(dfs);
					df.setMaximumFractionDigits(nroDecimales);
					df.setMinimumFractionDigits(nroDecimales);

					result = df.format(importe);

				} catch (Exception ex) {
					result = "";
					logger.error("ERROR:" + ex.toString(),ex);
				}
			} else {
				result = monto;
			}
		}
		return result;
	}
	
	public static String formatearMonto(double monto, int nroDecimales) {
		String result = "";
		try {
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator('.');
			dfs.setGroupingSeparator(' ');
			dfs.setMonetaryDecimalSeparator('.');
			dfs.setNaN("");
			DecimalFormat df = new DecimalFormat();
			df.setDecimalFormatSymbols(dfs);
			df.setMaximumFractionDigits(nroDecimales);
			df.setMinimumFractionDigits(nroDecimales);
			result = df.format(monto);
		} catch (Exception ex) {			
			logger.error("ERROR:" + ex.toString(),ex);
		}
		return result;
	}
		

	public static String formatearMontoConComa(String monto, int nroDecimales) {
		String result = "";
		if (monto != null && monto.trim().length() > 0) {
			if (nroDecimales > 0) {
				try {
					double importe = (double) (Double.parseDouble(monto.trim()) / Math
							.pow(10, nroDecimales));
					// le damos el formato a el numero generado
					DecimalFormatSymbols dfs = new DecimalFormatSymbols();
					dfs.setDecimalSeparator('.');
					dfs.setGroupingSeparator(',');
					dfs.setMonetaryDecimalSeparator('.');
					dfs.setNaN("");
					DecimalFormat df = new DecimalFormat();
					df.setDecimalFormatSymbols(dfs);
					df.setMaximumFractionDigits(nroDecimales);
					df.setMinimumFractionDigits(nroDecimales);

					result = df.format(importe);

				} catch (Exception ex) {
					result = "";
					logger.error("ERROR:" + ex.toString());
				}
			} else {
				result = monto;
			}
		}
		return result;
	}

	/**
	 * sumaSolesDolares, Suma dos montos en soles y dolares retornando todo en
	 * soles o todo en dolares dependiendo del modo
	 * 
	 * @param modo
	 *            , Moneda de salida. Soles-> sol ; Dolares-> dol
	 * @param soles
	 *            , monto en soles
	 * @param dolares
	 *            , monto en dolares
	 * @param tipoCambio
	 *            , Tipo de cambio a aplicar en la conversion de moneda.
	 * @return
	 */
	public static String sumaSolesDolares(String modo, double soles,
			double dolares, double tipoCambio) {
		String result = "";
		if (modo != null && modo.trim().length() > 0) {
			if (tipoCambio > 0) {
				try {
					double total = 0.0;
					if ("sol".equalsIgnoreCase(modo)) {
						total = soles + dolares * tipoCambio;
					} else {
						total = dolares + (soles / tipoCambio);
					}
					// le damos el formato a el numero generado
					DecimalFormatSymbols dfs = new DecimalFormatSymbols();
					dfs.setDecimalSeparator('.');
					dfs.setGroupingSeparator(' ');
					dfs.setMonetaryDecimalSeparator('.');
					dfs.setNaN("");
					DecimalFormat df = new DecimalFormat();
					df.setDecimalFormatSymbols(dfs);
					df.setMaximumFractionDigits(2);
					df.setMinimumFractionDigits(2);

					result = df.format(total);
				} catch (Exception ex) {
					result = "";
					logger.error("ERROR:" + ex.toString());
				}
			}
		}
		return result;
	}

	/**
	 * formatearMontoSalida
	 * 
	 * @param monto
	 * @param nroDecimales
	 * @return
	 */
	public static String formatearMontoSalida(String monto, int nroDecimales) {
		String result = monto;
		if (monto != null && monto.trim().length() > 0) {
			if (nroDecimales > 0) {
				try {
					if (monto.contains(".")) {
						// 110.22
						int indPto = monto.indexOf('.');
						int lonCad = monto.length();
						if ((lonCad - indPto - 1) > nroDecimales) {
							result = monto.substring(0,
									(indPto + nroDecimales + 1));
							result = result.replace('.', ' ');
							result = result.replaceAll(" ", "");
						} else if ((lonCad - indPto - 1) < nroDecimales) {
							result = monto
									+ repetirConcatenar(
											"0",
											(nroDecimales - (lonCad - indPto - 1)));
							result = result.replace('.', ' ');
							result = result.replaceAll(" ", "");
						} else { // tiene el numero de decimales exacto
							result = result.replace('.', ' ');
							result = result.replaceAll(" ", "");
						}
					} else {
						result = monto + repetirConcatenar("0", nroDecimales);
					}
				} catch (Exception ex) {
					result = "";
					logger.error("ERROR:" + ex.toString());
				}
			} else {
				result = monto;
			}
		}
		return result;
	}

	private static String repetirConcatenar(String cadena, int nroVeces) {
		String result = null;
		if (cadena != null && cadena.length() > 0 && nroVeces > 0) {
			result = "";
			for (int h = 0; h < nroVeces; h++) {
				result = result + cadena;
			}
		}
		return result;
	}

	public static String monedaSalida(String moneda) {
		String result = "";
		if (moneda != null && moneda.trim().length() > 0) {
			if (moneda.equalsIgnoreCase("PEN")
					|| moneda.equalsIgnoreCase("S/.")
					|| moneda.equalsIgnoreCase("604")) {
				result = "604";
			} else if (moneda.equalsIgnoreCase("USD")
					|| moneda.equalsIgnoreCase("US$")
					|| moneda.equalsIgnoreCase("840")) {
				result = "840";
			}
		}
		return result;
	}

	public static String monedaSalidaSEDAPAL(String moneda) {
		String result = "";
		if (moneda != null && moneda.trim().length() > 0) {
			if (moneda.equalsIgnoreCase("PEN")
					|| moneda.equalsIgnoreCase("S/.")
					|| moneda.equalsIgnoreCase("604")) {
				result = "PEN";
			} else if (moneda.equalsIgnoreCase("USD")
					|| moneda.equalsIgnoreCase("US$")
					|| moneda.equalsIgnoreCase("840")) {
				result = "USD";
			}
		}
		return result;
	}

	public static double convertirDouble(String monto) {
		double resul = 0.0;
		try {
			resul = Double.parseDouble(monto);
		} catch (NumberFormatException nfe) {
			logger.error("ERROR:" + nfe.toString());
			resul = 0.0;
		}
		return resul;
	}

	/**
	 * 
	 * @param monto
	 *            , entrada a formatear, ejem: 101.6513211
	 * @param nroDecimales
	 *            , numero de decimales que se desean obtener, ejem: 2
	 * @return String conteniendo el decimal formateado, ejem: 101.65
	 */
	public static String formatearDecimal(double monto, int nroDecimales) {
		String result = "";
		if (monto > 0) {
			if (nroDecimales > 0) {
				try {
					DecimalFormatSymbols dfs = new DecimalFormatSymbols();
					dfs.setDecimalSeparator('.');
					dfs.setGroupingSeparator(',');
					dfs.setMonetaryDecimalSeparator('.');
					dfs.setNaN("");
					DecimalFormat df = new DecimalFormat();
					df.setDecimalFormatSymbols(dfs);
					df.setMaximumFractionDigits(nroDecimales);
					df.setMinimumFractionDigits(nroDecimales);

					result = df.format(monto);

				} catch (Exception ex) {
					result = "";
					logger.error("ERROR:" + ex.toString());
				}
			} else {
				result = "" + monto;
			}
		}
		return result;
	}

	public static String stringToHtml(String cadena) {
		String result = "";
		if (cadena != null && cadena.length() > 0) {
			result = cadena;
			result = result.replaceAll("á", "&aacute;");
			result = result.replaceAll("é", "&eacute;");
			result = result.replaceAll("í", "&iacute;");
			result = result.replaceAll("ó", "&oacute;");
			result = result.replaceAll("ú", "&uacute;");
			result = result.replaceAll("Á", "&Aacute;");
			result = result.replaceAll("É", "&Eacute;");
			result = result.replaceAll("Í", "&Iacute;");
			result = result.replaceAll("Ó", "&Oacute;");
			result = result.replaceAll("Ú", "&Uacute;");
			result = result.replaceAll("Ü", "&Uuml;");
			result = result.replaceAll("ü", "&uuml;");
			result = result.replaceAll("ñ", "&ntilde;");
			result = result.replaceAll("Ñ", "&Ntilde;");
		}
		return result;
	}

	public static String htmltoString(String cadena) {
		String result = "";
		if (cadena != null && cadena.length() > 0) {
			result = cadena;
			result = result.replaceAll("&aacute;", "á");
			result = result.replaceAll("&eacute;", "é");
			result = result.replaceAll("&iacute;", "í");
			result = result.replaceAll("&oacute;", "ó");
			result = result.replaceAll("&uacute;", "ú");
			result = result.replaceAll("&Aacute;", "Á");
			result = result.replaceAll("&Eacute;", "É");
			result = result.replaceAll("&Iacute;", "Í");
			result = result.replaceAll("&Oacute;", "Ó");
			result = result.replaceAll("&Uacute;", "Ú");
			result = result.replaceAll("&Uuml;", "Ü");
			result = result.replaceAll("&uuml;", "ü");
			result = result.replaceAll("&ntilde;", "ñ");
			result = result.replaceAll("&Ntilde;", "Ñ");
		}
		return result;
	}

	public static String formatearMontoSinEspacio(String monto, int nroDecimales) {
		String result = "";
		if (monto != null && monto.trim().length() > 0) {
			if (nroDecimales > 0) {
				try {
					double importe = (double) (Double.parseDouble(monto.trim()) / Math
							.pow(10, nroDecimales));
					// le damos el formato a el numero generado
					DecimalFormatSymbols dfs = new DecimalFormatSymbols();
					dfs.setDecimalSeparator('.');
					dfs.setGroupingSeparator(' ');
					dfs.setMonetaryDecimalSeparator('.');
					dfs.setNaN("");
					DecimalFormat df = new DecimalFormat();
					df.setDecimalFormatSymbols(dfs);
					df.setMaximumFractionDigits(nroDecimales);
					df.setMinimumFractionDigits(nroDecimales);
					result = df.format(importe);

				} catch (Exception ex) {
					result = "0.0";
					logger.error("ERROR:" + ex.toString());
				}
			} else {
				result = monto;
			}
		}
		if (result != null) {
			result = result.replaceAll(" ", "");
		}
		return result;
	}

	public static String formatearMontoNvo(String monto) {
		String result = "";
		if (monto != null && monto.trim().length() > 0) {

			try {
				double importe = (double) (Double.parseDouble(monto.trim()));
				// le damos el formato a el numero generado
				DecimalFormatSymbols dfs = new DecimalFormatSymbols();
				dfs.setDecimalSeparator('.');
				dfs.setGroupingSeparator(',');
				dfs.setMonetaryDecimalSeparator('.');
				dfs.setNaN("");
				DecimalFormat df = new DecimalFormat();
				df.setDecimalFormatSymbols(dfs);
				df.setMaximumFractionDigits(2);
				df.setMinimumFractionDigits(2);

				result = df.format(importe);

			} catch (Exception ex) {
				result = "0.0";
				logger.error("ERROR:" + ex.toString());
			}

		}
		// if(result != null){
		// result = result.replaceAll(" ","");
		// }
		return result;
	}

	public static String ajustarDato(String cadena, int longitud) {
		String result = "";
		int lonCadena = 0;
		if (cadena != null && cadena.trim().length() > 0) {
			if (longitud > 0) {
				try {
					lonCadena = cadena.trim().length();
					if (lonCadena > longitud) {
						result = cadena.trim().substring(0, longitud);
					} else if (lonCadena < longitud) {
						result = cadena.trim()
								+ repetirConcatenar(" ", longitud - lonCadena);
					} else { // si la cadena tiene el tamaño maximo
						result = cadena.trim();
					}
				} catch (Exception ex) {
					result = repetirConcatenar(" ", longitud);
					logger.error("ERROR:" + ex.toString());
				}
			}
		} else { // sino tiene data igual se debe completar con espacios
			result = repetirConcatenar(" ", longitud);
		}
		return result;
	}

	/**
	 * 
	 * @param moneda
	 * @return
	 */
	public static String monedaMostrar(String codmoneda) {
		String result = codmoneda;
		if (codmoneda != null && codmoneda.trim().length() > 0) {
			if (codmoneda.equalsIgnoreCase("PEN")
					|| codmoneda.equalsIgnoreCase("S/.")
					|| codmoneda.equalsIgnoreCase("604")) {
				result = "S/.";
			} else if (codmoneda.equalsIgnoreCase("USD")
					|| codmoneda.equalsIgnoreCase("US$")
					|| codmoneda.equalsIgnoreCase("840")) {
				result = "US$";
			}
		}
		return result;
	}

	public static String completarCampo(String szCampo, int iSize,
			String szFiller, int iAlign) {
		String szTemp = "";
		szCampo = szCampo == null ? "" : szCampo;
		if (szCampo.length() < iSize) {
			szTemp = rellenar(iSize - szCampo.length(), szFiller);
			if (iAlign == 1)
				szCampo = szCampo + szTemp;
			else
				szCampo = szTemp + szCampo;
		} else if (szCampo.length() > iSize)
			szCampo = szCampo.substring(0, iSize);
		return szCampo;
	}

	public static String rellenar(int size, String szFill) {
		String szCadena = "";
		for (int i = 0; i < size; i++)
			szCadena = szCadena.concat(szFill);
		return szCadena;
	}

	public static List buscarServiciosxEmpresa(HashMap servicioEmpresa,
			String idServicio) {
		List listaEmpresas = new ArrayList();
		Collection col = servicioEmpresa.keySet();
		Iterator it = col.iterator();
		String rucEmpresa = "";
		String idServ = "";
		List servicios = new ArrayList();
		while (it.hasNext()) {

			try {
				// System.out.println("rucEmpresa laa:  "+ (String)it.next());
				rucEmpresa = (String) it.next();
				/*
				System.out.println("rucEmpresa laa:  " + rucEmpresa);
				System.out.println("resultadooo: "
						+ Constantes.RUC_EMPRESA_OPERACIONES_CASH
								.equals(rucEmpresa));*/
				if (Constantes.RUC_EMPRESA_OPERACIONES_CASH.equals(rucEmpresa)) {
					//System.out.println("entro a iffffffffffffffffffffffffffffffff");
				} else {
					servicios = (List) servicioEmpresa.get(rucEmpresa);
					//System.out.println("tamaño servicios: " + servicios.size());
					for (int i = 0; i < servicios.size(); i++) {
						idServ = (String) servicios.get(i);
						//System.out.println("IdServicio: " + idServ);
						if (idServ.equalsIgnoreCase(idServicio)) {
							listaEmpresas.add(rucEmpresa);
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);				
			}
		}

		return listaEmpresas;
	}
	
	public static String validarServicio(Map servicioEmpresa,String idServicio) {
		Collection col = servicioEmpresa.keySet();
		Iterator it = col.iterator();
		String rucEmpresa = "";
		String idServ = "";
		List servicios = new ArrayList();		
		
		while (it.hasNext()) {
			try {
				rucEmpresa = (String) it.next();				
				if (Constantes.RUC_EMPRESA_OPERACIONES_CASH.equals(rucEmpresa)) {
					return null;
				} else {
					servicios = (List) servicioEmpresa.get(rucEmpresa);
					for (int i = 0; i < servicios.size(); i++) {
						idServ = (String) servicios.get(i);
						if (idServ.equalsIgnoreCase(idServicio)) {
							return rucEmpresa;
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);				
			}
		}
		return null;
	}
	
	
	public static boolean verificarServicio(HashMap servicioEmpresa,String idServicio) {
		Collection col = servicioEmpresa.keySet();
		Iterator it = col.iterator();
		String rucEmpresa = "";
		String idServ = "";
		List servicios = new ArrayList();		
		
		while (it.hasNext()) {
			try {
				rucEmpresa = (String) it.next();				
				if (Constantes.RUC_EMPRESA_OPERACIONES_CASH.equals(rucEmpresa)) {
					return true;
				} else {
					servicios = (List) servicioEmpresa.get(rucEmpresa);
					for (int i = 0; i < servicios.size(); i++) {
						idServ = (String) servicios.get(i);
						if (idServ.equalsIgnoreCase(idServicio)) {
							return true;
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);				
			}
		}

		return false;
	}


	public static String cero_left(String data, int numceros) {

		String ceros = "";
		int num = numceros - data.length(); //

		for (int i = 0; i < num; i++) {
			ceros += "0";
		}

		data = ceros + data;
		return data;
	}

	public static String strFecha(Date fecha) {
		return strFecha(fecha, CashConstants.DD_MM_YYYY);
	}
	
	public static String strHora(Date fecha) {
		dfFecha = new SimpleDateFormat("HH:mm");
		return dfFecha.format(fecha);
	}
	
	public static Date fecha(String cadena){
		   try{
			   dfFecha = new SimpleDateFormat(STR_DD_MM_YYYY);
			   return dfFecha.parse(cadena);
		   }catch(Exception e){
			   return new Date();
		   }	   
	}
	
	public static Date obtenerFecha(String cadena, String pattern) {
		try {
			dfFecha = new SimpleDateFormat(pattern);
			return dfFecha.parse(cadena);
		} catch (ParseException e) {
			throw new RuntimeException("Error parseando fecha", e);
		}
	}

	public static String strFecha() {
		return strFecha(new Date());
	}
	
	public static String strFecha(String fecha,int formato){
		Date  d = fecha(fecha);
		return strFecha(d, CashConstants.YYYYMMDD);
	}

	public static String strFecha(Date fecha, int formato) {
		if( fecha != null ){
		switch (formato) {
		case DD_MM_YYYY:
			dfFecha = new SimpleDateFormat(STR_DD_MM_YYYY);
			return dfFecha.format(fecha);
		case DDMMYY:
			dfFecha = new SimpleDateFormat(STR_DDMMYY);
			return dfFecha.format(fecha);
		case YYYYMMDD:
			dfFecha = new SimpleDateFormat(STR_YYYYMMDD);
			return dfFecha.format(fecha);
		default:
			dfFecha = new SimpleDateFormat(STR_DD_MM_YYYY);
			return dfFecha.format(fecha);
		}
		}else{
			return "";
		}
	}

	public static int compareToFechaSistema(String fecha) {
		String fechaActual = strFecha(new Date(), YYYYMMDD);
		int mesSistema = Integer.parseInt(fechaActual.substring(4, 6));
		int anoSistema = Integer.parseInt(fechaActual.substring(0, 4));
		int diaSistema = Integer.parseInt(fechaActual.substring(6, 8));

		int mesFecha = Integer.parseInt(fecha.substring(4, 6));
		int anoFecha = Integer.parseInt(fecha.substring(0, 4));
		int diaFecha = Integer.parseInt(fecha.substring(6, 8));

		if (anoFecha == anoSistema) {
			if (mesFecha == mesSistema) {
				if (diaFecha == diaSistema) {
					return 0;
				} else {
					if (diaFecha < diaSistema) {
						return -1;
					} else {
						return 1;
					}
				}
			} else {
				if (mesFecha < mesSistema) {
					return -1;
				} else {
					return 1;
				}

			}
		} else {
			if (anoFecha < anoSistema) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	public static String getMonedaCash(String monedaIBS){
		if( monedaIBS.equals(CashConstants.VAL_IBS_SOLES) ){
			return  CashConstants.VAL_SOLES_SIMB;
		}
		else{
			if( monedaIBS.equals(CashConstants.VAL_IBS_DOLARES) ){
				return  CashConstants.VAL_DOLARES_SIMB;
			}else{
				if( monedaIBS.equals(CashConstants.VAL_IBS_EUROS) ){
					return  CashConstants.VAL_EUROS_SIMB;
				}
				else{
					return "";
				}
			}
		}
	}
	
	public static String formatearNombre(String cadena) {
		char inicio = cadena.charAt(0);		
		StringBuilder sb = new StringBuilder();		
		sb = sb.append(Character.toUpperCase(inicio)).append(cadena.substring(1));
		return sb.toString();
	}

	public static String strHTML(String cadena) {
		char caracteres[] = { 8217, '<', '>', '{', '}', '[', ']', '^', '?',
				'!', '¿', '°', 'ª', '«', '»', '`' };
		int n = caracteres.length;
		char charVacio = 32;

		for (int i = 0; i < n; i++) {
			cadena = cadena.replace(caracteres[i], charVacio);
		}
		return cadena;
	}

	public static String formatDouble2(double valor) {
		return String.format(FR_DOUBLE, valor).replace(",", ".");
	}
	
	/**
	 * Divide dividendo by divisor redondeando hacia arriba
	 * 
	 * @param dividendo
	 * @param divisor
	 * @return
	 */
	public static int dividirConRedondeoHaciaArriba(int dividendo, int divisor) {
		return (dividendo + divisor - 1) / divisor;
	}
	
	public static void propagateHibernateException(Exception exception){
		throw new RuntimeException(Constantes.MENSAJE_ERROR_CONEXION_HIBERNATE,exception);
	}
	
	 public static Document parse(String xml, boolean validation) throws JDOMException, IOException {
	        SAXBuilder saxbuilder = new SAXBuilder(validation);
	        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(xml.getBytes());
	        return saxbuilder.build(bytearrayinputstream);
	 }
	 
	 public static String strRequest(String mensaje){
		 StringBuilder sb = new StringBuilder("<request>").append("<mensaje>").append(mensaje).append("</mensaje>").append("</request>");
		 return sb.toString();
	 }
	 
	 public static String strResponse(String mensaje){
		 StringBuilder sb = new StringBuilder("<response>").append("<mensaje>").append(mensaje).append("</mensaje>").append("</response>");
		 return sb.toString();
	 }
	 
	 
	 
	

	
	
	
	
	
	

	

	
	
	public static String obtenerDescripcionTipoCuenta(String codigoTipoCuenta, String monedaTipoCuenta){
		String labelMoneda = TipoMoneda.getLabelMoneda(monedaTipoCuenta);
		if("AH".equals(codigoTipoCuenta)){
			return "Ahorros "+labelMoneda;
		}
		if("CC".equals(codigoTipoCuenta)){
			return "Corriente "+labelMoneda;
		}
		return "";
	}
	
	public static String formatearMonto(double monto, String pattern){
		DecimalFormat decimalFormat =(DecimalFormat)NumberFormat.getInstance(localeUS);
		decimalFormat.applyPattern(pattern);	
		return decimalFormat.format(monto);
	}
	
    public static String debugMap(Map map)
    {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry> iter = map.entrySet().iterator();
        while (iter.hasNext())
        {
            Entry entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext())
            {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();
    }
}