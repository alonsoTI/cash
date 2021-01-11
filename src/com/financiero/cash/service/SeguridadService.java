package com.financiero.cash.service;

public interface SeguridadService
{

    public abstract boolean verificaDisponibilidad(String idModulo) throws Exception;

    public abstract String generarCoordenada() throws Exception;

    public abstract String obtenerTCO(String tarjeta) throws Exception;

    public abstract boolean validarActividadTCO(String tco, int maximoNumeroIntentos, int maximoNumeroBloqueos)
            throws Exception;

    public abstract boolean esUsuarioSoloConsulta(String tarjeta) throws Exception;

    public abstract boolean validarCoordenada(String tco, String coordenada, String clave, int maximoNumeroIntentos)
            throws Exception;

}