package com.financiero.cash.dao.service;

import java.util.Date;
import java.util.List;

import com.financiero.cash.ui.model.DetalleOrdenEC;
import com.financiero.cash.ui.model.EmpresaEC;
import com.financiero.cash.ui.model.OrdenHistoricoEC;
import com.financiero.cash.ui.model.ServicioEC;

public interface HistoricoDAO {
	
	//String obtenerUsuarioEasyCash(String nroTarjeta) throws Exception;
	//String obtenerTarjetaEasyCash(String nombreCorto) throws Exception;
	List<String> obtenerIdEmpresa(String nroTarjeta)throws Exception;
	String buscarNombreEmpresa(int idEmpresa) throws Exception;
	String buscarNombreServicio(int idServicio) throws Exception;


	List<Integer> obtenerCodigoEmpresaEasyCash(List<String> idEmpresa)throws Exception;
	
	List<OrdenHistoricoEC> getOrdenes(String empresa,String servicio,Date fInicio,Date fFinal,String referencia,int inicio,int nroRegistros)throws Exception;
	int cuentaOrdenes(String empresa,String servicio,Date fInicio,Date fFinal,String referencia)throws Exception;
	
	List<DetalleOrdenEC> getDetallesOrden(int orden,String referencia,int inicio,int nroRegistros)throws Exception;
	int cuentaDetallesOrden(int orden,String referencia)throws Exception;
	
	List<EmpresaEC> getEmpresas(List<Integer> codigos) throws Exception;
	List<ServicioEC> getServicios(String  empresa) throws Exception;
}
