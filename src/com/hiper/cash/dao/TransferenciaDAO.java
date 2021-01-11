package com.hiper.cash.dao;

import java.util.Map;

import com.financiero.cash.beans.ConsultaTransferenciasBean;
import com.financiero.cash.beans.TransferenciaBean;
import com.financiero.cash.util.AccionTransferencia;
import com.hiper.cash.entidad.BeanPaginacion;

public interface TransferenciaDAO
{

    TransferenciaBean registrar(TransferenciaBean tr, int correlativoDiario);

    TransferenciaBean registrarIB(TransferenciaBean transferencia, int correlativoDiario);

    TransferenciaBean registrarAccion(TransferenciaBean tr, String usuario, AccionTransferencia accion,
            int correlativoDiario);

    TransferenciaBean registrarAccionIB(TransferenciaBean tr, String usuario, AccionTransferencia accion,
            int correlativoDiario);

    ConsultaTransferenciasBean buscarTransferencias(String idEmpresa, String tipoTransferencia, String tipoDocumento,
            String nroDocumento, String estadoTransferencia, String codigoMoneda, String fechaIni, String fechaFin,
            int numeroRegistros, long posicionInicial);

    TransferenciaBean buscarTransferencia(long id, String idEmpresa);

    ConsultaTransferenciasBean buscarTransferenciasPendientes(String idEmpresa, String usuario,
            BeanPaginacion beanPaginacion);

    Map<String, Double> obtenerLimitesTransferencias();

    String validarDatosTransferencia(TransferenciaBean transferencia);

}
