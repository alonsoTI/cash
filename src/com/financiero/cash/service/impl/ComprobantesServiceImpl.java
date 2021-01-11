package com.financiero.cash.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.financiero.cash.service.ComprobantesService;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmFormatoDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmFormatoDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.domain.TaDetalleFormato;
import com.hiper.cash.domain.TmFormato;
import com.hiper.cash.entidad.BeanConsultaEstado;
import com.hiper.cash.entidad.BeanMovimiento;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanTotalesConsMov;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;

public class ComprobantesServiceImpl implements ComprobantesService {
	private Logger logger = Logger.getLogger(ComprobantesServiceImpl.class);
	
	private TaServicioxEmpresaDao servEmpDao = new TaServicioxEmpresaDaoHibernate();
	private TpDetalleOrdenDao detalleOrdenDao = new TpDetalleOrdenDaoHibernate();
	private TaServicioxEmpresaDaoHibernate servicioEmpresaDao = new TaServicioxEmpresaDaoHibernate();
	private TmFormatoDao detalleFormatoDao = new TmFormatoDaoHibernate();
	private TaOrdenDao taOrdenDao = new TaOrdenDaoHibernate();

	public List<BeanMovimiento> consultarMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida,
			BeanPaginacion bpag) {
		logger.info("Inicio consultarMovimientos()");
		List<BeanMovimiento> movimientos = null;
		try {
			String tipo[] = servEmpDao.selectTipoServicioxEmpresa(Long
					.parseLong(idServEmp));			
					movimientos = seleccionarMovimientosNuevosCobros(tipo[1],
							idServEmp, estado, fecini, fecfin, referencia,
							contrapartida, bpag);			
		} catch (Exception e) {
			logger.error("Error en la consulta de movimientos", e);
		}
		logger.info("Fin consultarMovimientos()");
		return movimientos;
	}
			
	
	public BeanTotalesConsMov obtenerMontosMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida) {
		logger.info("Inicio obtenerMontosMovimientos()");		
		BeanTotalesConsMov totales = new BeanTotalesConsMov();
		try {			
			List<Object[]> queryResult=null;			
			queryResult = detalleOrdenDao.obtenerMontosMovimientosNuevosCobros(idServEmp, estado, fecini, fecfin, referencia, contrapartida);				
				if(queryResult!=null && queryResult.size()>0){
					Iterator iter = queryResult.iterator();
					totales = new BeanTotalesConsMov();
					long numRegistros = 0;
					BigDecimal m_MontoVentSoles = new BigDecimal("0.0").setScale(2);
					BigDecimal m_MontoVentDolares = new BigDecimal("0.0").setScale(2);
					BigDecimal m_MontoNetoSoles = new BigDecimal("0.0").setScale(2);
					BigDecimal m_MontoNetoDolares = new BigDecimal("0.0").setScale(2);
					while (iter.hasNext()) {
						Object[] obj_mov = (Object[]) iter.next();
						numRegistros = numRegistros
								+ (Long.parseLong(obj_mov[0].toString()));
						if ("PEN".equals(obj_mov[1])) {
							m_MontoVentSoles = m_MontoVentSoles
									.add((obj_mov[2] != null) ? (BigDecimal) obj_mov[2]
											: new BigDecimal("0.0").setScale(2));
							m_MontoNetoSoles = m_MontoNetoSoles
									.add((obj_mov[3] != null) ? (BigDecimal) obj_mov[3]
											: new BigDecimal("0.0").setScale(2));
						} else if ("USD".equals(obj_mov[1])) {

							m_MontoVentDolares = m_MontoVentDolares
									.add((obj_mov[2] != null) ? (BigDecimal) obj_mov[2]
											: new BigDecimal("0.0").setScale(2));
							m_MontoNetoDolares = m_MontoNetoDolares
									.add((obj_mov[3] != null) ? (BigDecimal) obj_mov[3]
											: new BigDecimal("0.0").setScale(2));
						}
					}
					totales.setM_NumItems(String.valueOf(numRegistros));
					totales.setNumeroItems(numRegistros);
					totales.setM_MontoVentDolares(Util
							.formatearMontoNvo(m_MontoVentDolares.toString()));
					totales.setM_MontoVentSoles(Util
							.formatearMontoNvo(m_MontoVentSoles.toString()));
					totales.setM_MontoNetoDolares(Util
							.formatearMontoNvo(m_MontoNetoDolares.toString()));
					totales.setM_MontoNetoSoles(Util
							.formatearMontoNvo(m_MontoNetoSoles.toString()));
			}			
		} catch (Exception e) {
			logger.error("Error en la consulta de movimientos", e);
		}
		logger.info("Fin obtenerMontosMovimientos()");
		return totales;
	}

	public String obtenerTipoServicio(String idServEmp) {
		String tipoServicio = null;
		String tipo[] = servEmpDao.selectTipoServicioxEmpresa(Long
				.parseLong(idServEmp));
		if (tipo != null && tipo.length == 2) {
			tipoServicio = tipo[1];
		}
		return tipoServicio;
	}	
		
	private List<BeanMovimiento> seleccionarMovimientosNuevosCobros(
			String tipo, String idServEmp, String estado, String fecini,
			String fecfin, String referencia, String contrapartida,
			BeanPaginacion bpag) {
		logger.info("Inicio seleccionarMovimientosNuevosCobros()");
		List<BeanMovimiento> resultado = null;					
		List<Object[]> resultQuery = detalleOrdenDao
						.seleccionarMovimientosNuevosCobros(idServEmp, estado,
								fecini, fecfin, referencia, contrapartida, bpag);			
		resultado = obtenerResultadosEnBeanMovimiento(resultQuery, tipo);	
		logger.info("Fin seleccionarMovimientosNuevosCobros()");
		return resultado;
	}
		
	private List<BeanMovimiento> obtenerResultadosEnBeanMovimiento(
			List<Object[]> queryResult, String tipo) {
		Iterator<Object[]> iter = queryResult.iterator();
		BeanMovimiento beanMov;
		List<BeanMovimiento> listaResult = new ArrayList<BeanMovimiento>();
		BigDecimal m_MontoVentSoles = new BigDecimal("0.0").setScale(2);
		BigDecimal m_MontoVentDolares = new BigDecimal("0.0").setScale(2);
		BigDecimal m_MontoNetoSoles = new BigDecimal("0.0").setScale(2);
		BigDecimal m_MontoNetoDolares = new BigDecimal("0.0").setScale(2);
		while (iter.hasNext()) {
			Object[] obj_mov = iter.next();
			beanMov = new BeanMovimiento();
			beanMov.setM_FechaProceso(Fecha
					.convertFromFechaSQL((String) obj_mov[0]));
			beanMov.setM_Pais("PE");
			beanMov.setM_Banco("BCO PICHINCHA");
			beanMov.setM_FormaPago((obj_mov[1] == null) ? " - " : obj_mov[1]
					.toString());
			if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equalsIgnoreCase(tipo)) {
				beanMov.setM_CuentaDestino((obj_mov[2] == null) ? " - "
						: obj_mov[2].toString());
				beanMov.setM_Cuenta("0");
				beanMov.setM_NroRefBanco("");
				beanMov.setM_NroDocumento((obj_mov[11] == null) ? " - "
						: obj_mov[11].toString());
			} else if (Constantes.TX_CASH_TIPO_SERVICIO_COBRO
					.equalsIgnoreCase(tipo)) {
				beanMov.setM_Cuenta("");
				beanMov.setM_NroRefBanco((obj_mov[11] == null) ? " - "
						: obj_mov[11].toString());
			}
			beanMov.setM_Contrapartida((obj_mov[3] == null) ? " - "
					: obj_mov[3].toString());
			beanMov.setM_Nombre((obj_mov[4] == null) ? " - " : obj_mov[4]
					.toString());
			beanMov.setM_ValorEnviado((obj_mov[5] == null) ? " 0.00 " : Util
					.formatearMontoNvo(obj_mov[5].toString()));
			beanMov.setM_ValorPro((obj_mov[13] == null) ? " 0.00 " : Util
					.formatearMontoNvo(obj_mov[13].toString()));
			beanMov.setM_ValorNeto((obj_mov[14] == null) ? " 0.00 " : Util
					.formatearMontoNvo(obj_mov[14].toString()));
			beanMov.setM_Moneda((obj_mov[6] == null) ? " - " : obj_mov[6]
					.toString());
			beanMov.setM_Estado((obj_mov[7] == null) ? " - " : obj_mov[7]
					.toString());
			beanMov.setM_Referencia((obj_mov[8] == null) ? " - " : obj_mov[8]
					.toString());
			beanMov.setM_Descripcion((obj_mov[10] == null) ? " - "
					: obj_mov[10].toString());

			if (Constantes.TX_CASH_TIPO_SERVICIO_COBRO.equalsIgnoreCase(tipo)
					&& obj_mov[16] == null) {
				String ref = (obj_mov[8] == null) ? " - " : obj_mov[8]
						.toString();
				String nom = (obj_mov[4] == null) ? " - " : obj_mov[4]
						.toString();
				beanMov.setM_Referencia(nom);
				beanMov.setM_Descripcion(ref);
			}
			beanMov.setM_IdOrden(obj_mov[9].toString());
			beanMov.setM_Desglose("0.00");
			beanMov.setM_Oficina((obj_mov[15] == null) ? " - " : obj_mov[15]
					.toString());
			beanMov.setM_Empresa((obj_mov[12] == null) ? " - " : obj_mov[12]
					.toString());

			listaResult.add(beanMov);
			// sumarizado por tipo de moneda
			if (!" - ".equals(beanMov.getM_Moneda().trim())) {
				if ("PEN".equals(beanMov.getM_Moneda())) {
					m_MontoVentSoles = m_MontoVentSoles
							.add((obj_mov[13] != null) ? (BigDecimal) obj_mov[13]
									: new BigDecimal("0.0").setScale(2));
					m_MontoNetoSoles = m_MontoNetoSoles
							.add((obj_mov[14] != null) ? (BigDecimal) obj_mov[14]
									: new BigDecimal("0.0").setScale(2));
				} else if ("USD".equals(beanMov.getM_Moneda().trim())) {
					m_MontoVentDolares = m_MontoVentDolares
							.add((obj_mov[13] != null) ? (BigDecimal) obj_mov[13]
									: new BigDecimal("0.0").setScale(2));
					m_MontoNetoDolares = m_MontoNetoDolares
							.add((obj_mov[14] != null) ? (BigDecimal) obj_mov[14]
									: new BigDecimal("0.0").setScale(2));
				}
			}
		}		
		return listaResult;
	}

	
	
	private String obtenerQueryExportacionMovimientos(String servicioEmpresa, StringBuilder separador,List lblColumnas ){
		int formatoSalida = servicioEmpresaDao.selectCodFormatoOut(Integer
				.valueOf(servicioEmpresa));
		TmFormato formatoBean = detalleFormatoDao
				.obtenerFormatoById(formatoSalida);
		StringBuilder campos = new StringBuilder();
		if (formatoBean != null) {
			separador.append(formatoBean.getDfseparador());			
			obtenerCamposConsultaDesdeTablas(formatoBean, campos,
					lblColumnas);
		}else{
			throw new RuntimeException("El codigo de formato no existe");
		}
		String query = "";
		if (lblColumnas.size() > 0) {
			String selectClauses = campos.toString();			
			query = "select " + selectClauses
					+" from "+Constantes.NOMBRE_TABLA_ORDEN+ " ta join "+ Constantes.NOMBRE_TABLA_DETALLE_ORDEN_TRX
					+" tp on (ta.cOrIdOrden = tp.cDOIdOrden and ta.cOrIdServicioEmpresa=tp.cDOIdServicioEmpresa) ";					
			String whereClauses = " where tp.cdoIdServicioEmpresa = :idServEmp and tp.flagEstadoRep=0 ";					
			query += whereClauses;			
		}
		return query;
	}
			
	
	public void obtenerCamposConsultaDesdeTablas(TmFormato formatoBean,
			StringBuilder campos, List<String> lblColumnas) {
		int i = 0;
		for (TaDetalleFormato detalleFormato : formatoBean
				.getTaDetalleFormatos()) {
			lblColumnas.add(detalleFormato.getDescripcion() + ","
					+ detalleFormato.getNdflongitudCampo());
			String nombreCampo = detalleFormato.getDdfnombreCampo().trim();
			if (Constantes.CAMPO_DBU_MONEDA.equals(nombreCampo)) {
				agregarComa(campos, i);				
				appendConTrim(campos,"tp.cDOMoneda",Constantes.ID_CAMPO_MONEDA);
			} else if (Constantes.CAMPO_DBU_REFERENCIA_SOBRE
					.equals(nombreCampo)) {
				agregarComa(campos, i);				
				appendConTrim(campos,"ta.dOrReferencia",Constantes.ID_CAMPO_REFERENCIAORDEN);
			} else if (Constantes.CAMPO_DBU_PAIS.equals(nombreCampo)) {
				agregarComa(campos, i);				
				append(campos,Constantes.CAMPO_PAIS,Constantes.ID_CAMPO_PAIS);
			} else if (Constantes.CAMPO_DBU_PAIS_ADICIONAL.equals(nombreCampo)) {
				agregarComa(campos, i);				
				append(campos,Constantes.CAMPO_PAIS,Constantes.ID_CAMPO_ADICIONALPAIS);
			} else if (Constantes.CAMPO_DBU_NOMBREBANCO.equals(nombreCampo)) {
				agregarComa(campos, i);				
				appendConTrim(campos,Constantes.CAMPO_BA_NOMBREBANCO,Constantes.ID_CAMPO_NOMBREBANCO);
			}
			else if(Constantes.CAMPO_DBU_VALORPROCESADO.equals(nombreCampo)||Constantes.CAMPO_DBU_VALOR_PROCESADO_MOVIMIENTO.equals(nombreCampo)) {
				agregarComa(campos, i);
				campos.append("replace(	RIGHT('00000000000'+ convert(varchar,tp.nDOMontoVentanilla),14),'.','') as montoprocesado");
			}
			else if (Constantes.CAMPO_DBU_VALORENVIADO.equals(nombreCampo)) {
				agregarComa(campos, i);
				campos.append("replace(	RIGHT('00000000000'+ convert(varchar,coalesce(tp.nDoMonto,0)),14),'.','') as montoEnviado");			
			} 
			else if (Constantes.CAMPO_DBU_FECHAPROCESO.equals(nombreCampo)||Constantes.CAMPO_DBU_FECHA_MOVIMIENTO.equals(nombreCampo)) {
				agregarComa(campos, i);
				campos.append(" convert(varchar,CONVERT(datetime, tp.fDOFechaProceso, 101),103) as fechaProceso ");			
			}			
			else if (Constantes.CAMPO_DBU_COMPROBANTECOBRO.equals(nombreCampo)) {
				agregarComa(campos, i);
				append(campos,Constantes.CAMPO_BLANCO,"comprobanteCobro");
		 	}
			else if (Constantes.CAMPO_DBU_NUMEROSUNAT.equals(nombreCampo)) {
				agregarComa(campos, i);
				append(campos,Constantes.CAMPO_BLANCO,"numeroSunat");				
		 	}
			else if (Constantes.CAMPO_DBU_NOMEMPRESA.equals(nombreCampo)) {
				agregarComa(campos, i);				
				appendConTrim(campos," ta.nombreEmpresa ","nombreEmpresa");
		 	}
			else if (Constantes.CAMPO_DBU_VALORNETO.equals(nombreCampo)||Constantes.CAMPO_DBU_VALORNETO_MOVIMIENTO.equals(nombreCampo)) {
				agregarComa(campos, i);
				campos.append(" case when(coalesce(tp.nDOMontoComCliente,0)> 0 ) then replace(RIGHT('00000000000'+ convert(varchar,coalesce(tp.nDoMonto,0)+coalesce(tp.nDOMontoMora,0)),14),'.','') else '' end as valorNeto ");
		 	}			
			else if (Constantes.CAMPO_DBU_FORMA_COBRO_RECAUDACION.equals(nombreCampo)) {
				agregarComa(campos, i);				
				append(campos,Constantes.CAMPO_FORMACOBRO_REC,Constantes.ID_CAMPO_FORMACOBRO_REC);
		 	}
			else if (Constantes.CAMPO_DBU_ESTADO.equals(nombreCampo)||Constantes.CAMPO_DBU_ESTADO_MOVIMIENTO.equals(nombreCampo)) {
				agregarComa(campos, i);
				campos.append(" tp.nombreEstadoItem as estadoProceso ");
		 	}
			else if (Constantes.CAMPO_DBU_PROCESO_OK.equals(nombreCampo)) {
				agregarComa(campos, i);
				append(campos, Constantes.CAMPO_PROCESO_OK,Constantes.ID_CAMPO_PROCESO_OK);
		 	}
			else if (Constantes.CAMPO_DBU_BANCOFINANCIERO.equals(nombreCampo)) {
				agregarComa(campos, i);
				append(campos,Constantes.CAMPO_BANCOFINANCIERO,Constantes.ID_CAMPO_BANCOFINANCIERO);
		 	}
			else if (Constantes.CAMPO_DBU_NUMDOC_REFERENCIA.equals(nombreCampo)) {
				agregarComa(campos, i);
				append(campos,aplicarTrim(Constantes.CAMPO_DO_NUMDOC) +"+'|'+"+aplicarTrim(Constantes.CAMPO_DO_REFERENCIA),Constantes.ID_CAMPO_NUMDOCUMENTO_REFERENCIA);
		 	}
			else if (Constantes.CAMPO_DBU_REFERENCIA_ADICIONAL.equals(nombreCampo)) {
				agregarComa(campos, i);
				appendConTrim(campos, Constantes.CAMPO_DO_REFERENCIA,nombreCampo);
		 	}
			else {
				agregarComa(campos, i);				
				campos.append("tp." + nombreCampo.replaceFirst("DBu", "DO"));
				
			}
			i++;
		}
	}
	
	private String aplicarTrim(String sentenciaSql) {
		return "RTRIM(LTRIM(" + sentenciaSql + "))";
	}
	
	private void append(StringBuilder campos, String campo,String columnName) {
		campos.append(campo +" as "+columnName);
	}
	
	private void appendConTrim(StringBuilder campos, String campo, String columnName) {
		campos.append(aplicarTrim(campo) + " as "+columnName);
	}
	public List seleccionarMovimientosExportacion(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida, String formato, StringBuilder separador,List lblColumnas) {
		logger.info("Inicio seleccionarMovimientosExportacion()");
		List listaRegistros = new ArrayList();	
		try {				
			String strQuery = obtenerQueryExportacionMovimientos(idServEmp,separador, lblColumnas );
			String topQuery= strQuery.substring(0,7)+" top 10000 "+strQuery.substring(7);
			List resultados = detalleOrdenDao.ejecutarConsultaMovimientosExportacion(topQuery, idServEmp, estado, fecini, fecfin, referencia, contrapartida, formato,true);											
			Iterator iter = resultados.iterator();
			List detalleMovimiento = null;
			if ("txt".equals(formato)) {
				while (iter.hasNext()) {
					Object[] obj_mov = (Object[]) iter.next();
					detalleMovimiento = new ArrayList();
					for (int i = 0; i < obj_mov.length; i++) {
						if (obj_mov[i] == null) {
							detalleMovimiento.add(" - ");
						} else {
							if ("java.math.BigDecimal"
									.equalsIgnoreCase(obj_mov[i].getClass()
											.getName())) {
								detalleMovimiento.add(obj_mov[i].toString()
										.replaceFirst("\\.", ""));
							} else {
								detalleMovimiento.add(obj_mov[i].toString());
							}
						}
					}
					listaRegistros.add(detalleMovimiento);
				}
			} else {
				while (iter.hasNext()) {
					Object[] obj_mov = (Object[]) iter.next();
					detalleMovimiento = new ArrayList();
					for (int i = 0; i < obj_mov.length; i++) {
						detalleMovimiento.add((obj_mov[i] == null) ? " - "
								: obj_mov[i].toString());
					}
					listaRegistros.add(detalleMovimiento);
				}
			}
			int indexFrom = strQuery.indexOf("from");			
			String countQuery = strQuery.substring(0,7)+"count(*)"+ strQuery.substring(indexFrom-1);
			List resultadoCount = detalleOrdenDao.ejecutarConsultaMovimientosExportacion(countQuery, idServEmp, estado, fecini, fecfin, referencia, contrapartida, formato,false);														
			int count = Integer.valueOf((resultadoCount.get(0)).toString());
			if(count<=10000){
				//indica que los resultados devueltos son todos los que existen
				listaRegistros.add("1");
			}					
		} catch (Exception e) {
			logger.error("Error en la consulta de movimientos", e);
		}
		logger.info("Fin seleccionarMovimientosExportacion()");
		return listaRegistros;
	}
	
	public List obtenerConsultaDeEstadoOrdenes(String idServEmp, String estado, String fecini, String fecfin, 
            String referencia, String contrapartida,BeanTotalesConsMov beanTotales) {
		List resultado = new ArrayList();        
        BigDecimal m_montoSoles = new BigDecimal("0.0").setScale(2);
        BigDecimal m_montoDolares = new BigDecimal("0.0").setScale(2);
        BigDecimal m_montoVentSoles = new BigDecimal("0.0").setScale(2);
        BigDecimal m_montoVentDolares = new BigDecimal("0.0").setScale(2);
        List result = null;
        try{
            result = taOrdenDao.getConsultaOrdEstado(idServEmp, estado, fecini, fecfin, referencia, contrapartida, beanTotales);         
            Iterator it = result.iterator();
            BeanConsultaEstado bean = null;
            int numReg = 0;
            while (it.hasNext()) {
                Object[] obj_ord = (Object[]) it.next();
                bean = new BeanConsultaEstado();
                bean.setM_Empresa((obj_ord[0] == null)?" - ":obj_ord[0].toString());
                bean.setM_Servicio((obj_ord[1] == null)?" - ":obj_ord[1].toString());
                bean.setM_IdOrden((obj_ord[2] == null)?" - ":obj_ord[2].toString());
                bean.setM_Estado((obj_ord[3] == null)?" - ":obj_ord[3].toString());
                bean.setM_Moneda((obj_ord[4] == null)?" - ":obj_ord[4].toString());
                bean.setM_Registros((obj_ord[5] == null)?" - ":obj_ord[5].toString());
                bean.setM_Monto((obj_ord[6] == null)?"0.00":Util.formatearMontoNvo(obj_ord[6].toString()));
                bean.setM_MontoVentanilla((obj_ord[7] == null)?"0.00":Util.formatearMontoNvo(obj_ord[7].toString()));
                 if(!" - ".equals(bean.getM_Moneda().trim())){
                    if("PEN".equals(bean.getM_Moneda())){
                        m_montoSoles = m_montoSoles.add((obj_ord[6] != null) ? (BigDecimal)obj_ord[6] : new BigDecimal("0.0").setScale(2));
                        m_montoVentSoles = m_montoVentSoles.add((obj_ord[7] != null) ? (BigDecimal)obj_ord[7] : new BigDecimal("0.0").setScale(2));
                    }else if("USD".equals(bean.getM_Moneda().trim())){
                        m_montoDolares = m_montoDolares.add((obj_ord[6] != null) ? (BigDecimal)obj_ord[6] : new BigDecimal("0.0").setScale(2));
                        m_montoVentDolares = m_montoVentDolares.add((obj_ord[7] != null) ? (BigDecimal)obj_ord[7] : new BigDecimal("0.0").setScale(2));
                    }
                }
                numReg+=((obj_ord[5] == null)? 0 : Integer.parseInt(obj_ord[5].toString()));
                resultado.add(bean);
            }
            beanTotales.setM_NumItems(String.valueOf(numReg));
            beanTotales.setM_MontoDolares(Util.formatearMontoNvo(m_montoDolares.toString()));
            beanTotales.setM_MontoSoles(Util.formatearMontoNvo(m_montoSoles.toString()));
            beanTotales.setM_MontoVentDolares(Util.formatearMontoNvo(m_montoVentDolares.toString()));
            beanTotales.setM_MontoVentSoles(Util.formatearMontoNvo(m_montoVentSoles.toString()));                        
        } catch (Exception e) {
             logger.error("Error en obtenerConsultaDeEstadoOrdenes",e);
             return null;
        }
        return resultado;
    }
	
	public int obtenerCantidadMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida) {
		return detalleOrdenDao.obtenerCantidadMovimientos(idServEmp, estado, fecini, fecfin, referencia, contrapartida);
	}
	
	private  void agregarComa(StringBuilder campos, int posicion) {
		if (posicion != 0) {
			campos.append(" , ");
		}
	}


	public TmFormatoDao getDetalleFormatoDao() {
		return detalleFormatoDao;
	}


	public void setDetalleFormatoDao(TmFormatoDao detalleFormatoDao) {
		this.detalleFormatoDao = detalleFormatoDao;
	}
	
	
}
