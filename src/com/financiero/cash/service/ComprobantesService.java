package com.financiero.cash.service;

import java.util.List;

import com.hiper.cash.dao.TmFormatoDao;
import com.hiper.cash.entidad.BeanMovimiento;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanTotalesConsMov;

public interface ComprobantesService {
	List<BeanMovimiento> consultarMovimientos(String idServEmp, String estado,
			String fecini, String fecfin, String referencia,
			String contrapartida,
			BeanPaginacion bpag);

	String obtenerTipoServicio(String idServEmp);
	
	List seleccionarMovimientosExportacion(String idServEmp, String estado, String fecini, String fecfin,
            String referencia,String contrapartida,String formato, StringBuilder separador,List lblColumnas);
	
	void setDetalleFormatoDao(TmFormatoDao detalleFormatoDao);
	
	BeanTotalesConsMov obtenerMontosMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida);
	
	List obtenerConsultaDeEstadoOrdenes(String idServEmp, String estado, String fecini, String fecfin, 
            String referencia, String contrapartida,BeanTotalesConsMov beanTotales);
	
	int obtenerCantidadMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida);
}
