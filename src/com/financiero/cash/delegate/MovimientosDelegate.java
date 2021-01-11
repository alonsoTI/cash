package com.financiero.cash.delegate;

import static com.hiper.cash.util.Util.formatDouble2;
import static com.hiper.cash.util.CashConstants.*;

import java.util.ArrayList;
import java.util.List;


import com.hiper.cash.dao.ws.SixLinux;
import com.hiper.cash.xml.bean.BeanConsultaSaldosXML;

public class MovimientosDelegate {
	
	
	public MovimientosDelegate() {

	}
	
	public List<BeanConsultaSaldosXML> obtenerSaldos(String codigo) throws Exception{
		List<BeanConsultaSaldosXML> lista =  new ArrayList<BeanConsultaSaldosXML>();
		int nroCuentas,indice;
		String cuenta = "            ";
		StringBuilder sbMensaje = new StringBuilder(codigo).append(cuenta);
		
		String respuesta;
		BeanConsultaSaldosXML bean = new BeanConsultaSaldosXML();
		String tipo;
		do{			
			respuesta =  obtenerTramaSaldos(sbMensaje.toString());
			indice = 111;		
			nroCuentas = Integer.valueOf(respuesta.substring(indice,indice += 3));			
			for (int i = 0; i < nroCuentas; i++) {
				indice += 5;
				bean = new BeanConsultaSaldosXML();
				bean.setM_Cuenta(respuesta.substring(indice, indice += 12));
				bean.setM_SignoContable(respuesta.substring(indice, indice += 1));
				indice += 17;
				bean.setM_DescripcionCuenta(respuesta.substring(indice,	indice += 30));
				indice += 1;
				tipo = respuesta.substring(indice,	indice += 3);
				if( tipo.equals(VAL_IBS_SOLES)){
					bean.setM_Moneda(VAL_SOLES_SIMB);
					bean.setM_DescripcionCuenta(bean.getM_DescripcionCuenta().trim() + " " + VAL_SOLES_DESC);
				}else{
					bean.setM_Moneda(VAL_DOLARES_SIMB);
					bean.setM_DescripcionCuenta(bean.getM_DescripcionCuenta().trim() + " " + VAL_DOLARES_DESC);
				}			
				bean.setM_SaldoContable(formatDouble2(valor(respuesta,indice, indice += 14)));	
				bean.setM_SaldoDisponible(formatDouble2(valor(respuesta,indice,indice += 14)));
				indice += 28;
				bean.setM_SaldoRetenido(formatDouble2(valor(respuesta,indice, indice += 14)));		
				lista.add(bean);
			}
			sbMensaje = new StringBuilder(codigo).append(bean.getM_Cuenta());
		}while( nroCuentas==25 );
		return lista;
	}
	
	private String obtenerTramaSaldos(String message)
	throws Exception {
		
		String app = RES_IBS.getString("consultas.relacionesBanco.obtenerCuentas.app");
		String trx = RES_IBS.getString("consultas.relacionesBanco.obtenerCuentas.trx");
		short length = Short.parseShort(RES_IBS	.getString("consultas.relacionesBanco.obtenerCuentas.length"));
		SixLinux cliente = SixLinux.getInstance();
		String trama = cliente.enviarMensaje(app, trx, length, message);
		return trama;

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
					double entero = Double.valueOf(segmento.substring(0, longitud - 2));
					if( entero > 0 ){
						valor = entero + Double.valueOf(decimal.toString()); 
					}else{
						valor = entero - Double.valueOf(decimal.toString()); 
					}				
				}
			}
			return valor;
		} catch (NumberFormatException e) {
			return 0.0;
		} catch (Exception e) {
			throw e;
		}
	}	
	
}
