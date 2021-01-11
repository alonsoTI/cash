package com.hiper.cash.dao.ws.ibs;

import static com.hiper.cash.util.CashConstants.RES_IBS;

public interface ConstantesIBS {
	//transferencias
	String APP_REGISTRO_APROBACION = RES_IBS.getString("transferencias.registraApruebaRechaza.app");
	String TRX_REGISTRO_APROBACION = RES_IBS.getString("transferencias.registraApruebaRechaza.trx");
	short LONG_REGISTRO_APROBACION = Short.parseShort(RES_IBS
			.getString("transferencias.registraApruebaRechaza.length"));

	String APP_REGISTRO_APROBACION_IB = RES_IBS.getString("transferencias.registraApruebaRechazaIB.app");
	String TRX_REGISTRO_APROBACION_IB = RES_IBS.getString("transferencias.registraApruebaRechazaIB.trx");
	short LONG_REGISTRO_APROBACION_IB = Short.parseShort(RES_IBS
			.getString("transferencias.registraApruebaRechazaIB.length"));

	String APP_CONSULTA_DETALLE = RES_IBS.getString("transferencias.consultaDetalle.app");
	String TRX_CONSULTA_DETALLE = RES_IBS.getString("transferencias.consultaDetalle.trx");
	short LONG_CONSULTA_DETALLE = Short
			.parseShort(RES_IBS.getString("transferencias.consultaDetalle.length"));

	String APP_CONSULTA_TRANSFERENCIAS = RES_IBS.getString("transferencias.consultaTransferencias.app");
	String TRX_CONSULTA_TRANSFERENCIAS = RES_IBS.getString("transferencias.consultaTransferencias.trx");
	short LONG_CONSULTA_TRANSFERENCIAS = Short.parseShort(RES_IBS
			.getString("transferencias.consultaTransferencias.length"));
	
	String APP_VALIDAR_CTAS_CONSULTAR_LIMITES = RES_IBS.getString("transferencias.validarCuentasConsultarLimites.app");
	String TRX_VALIDAR_CTAS_CONSULTAR_LIMITES = RES_IBS.getString("transferencias.validarCuentasConsultarLimites.trx");
	short LONG_VALIDAR_CTAS_CONSULTAR_LIMITES = Short.parseShort(RES_IBS
			.getString("transferencias.validarCuentasConsultarLimites.length"));
	
	
	//tipo de cambio
	String APP_CONSULTA_TIPO_CAMBIO = RES_IBS.getString("consultas.tipoCambio.app");
	String TRX_CONSULTA_TIPO_CAMBIO = RES_IBS.getString("consultas.tipoCambio.trx");
	short LONG_CONSULTA_TIPO_CAMBIO = Short.parseShort(RES_IBS
			.getString("consultas.tipoCambio.length"));
		
	
	
}
