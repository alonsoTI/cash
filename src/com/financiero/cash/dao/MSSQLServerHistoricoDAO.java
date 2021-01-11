package com.financiero.cash.dao;


import java.util.Date;
import java.util.List;

import com.financiero.cash.dao.service.HistoricoDAO;
import com.financiero.cash.ui.model.DetalleOrdenEC;
import com.financiero.cash.ui.model.EmpresaEC;
import com.financiero.cash.ui.model.OrdenHistoricoEC;
import com.financiero.cash.ui.model.ServicioEC;

public class MSSQLServerHistoricoDAO implements HistoricoDAO{

	@Override
	public String buscarNombreEmpresa(int idEmpresa) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buscarNombreServicio(int idServicio) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int cuentaDetallesOrden(int orden, String referencia)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int cuentaOrdenes(String empresa, String servicio, Date fInicio,
			Date fFinal, String referencia) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<DetalleOrdenEC> getDetallesOrden(int orden, String referencia,
			int inicio, int nroRegistros) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmpresaEC> getEmpresas(List<Integer> codigos) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrdenHistoricoEC> getOrdenes(String empresa, String servicio,
			Date fInicio, Date fFinal, String referencia, int inicio,
			int nroRegistros) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServicioEC> getServicios(String empresa) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> obtenerCodigoEmpresaEasyCash(List<String> idEmpresa)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> obtenerIdEmpresa(String nroTarjeta) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	


	

}
