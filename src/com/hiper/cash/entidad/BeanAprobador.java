	/**
	 * creado por andy 27/06/2011   q
	 */

package com.hiper.cash.entidad;

public class BeanAprobador {

	private String m_IdOrden;
	private String m_servicio;
	private String m_fecha;
	private String m_hora;
	private String m_aprobador;

	public BeanAprobador() {
	}

	public BeanAprobador(String m_IdOrden, String m_servicio, String m_fecha,
			String m_hora, String m_aprobador) {
		this.m_IdOrden = m_IdOrden;
		this.m_servicio = m_servicio;
		this.m_fecha = m_fecha;
		this.m_hora = m_hora;
		this.m_aprobador = m_aprobador;
	}

	/**
	 * @return the m_IdOrden
	 */
	public String getM_IdOrden() {
		return m_IdOrden;
	}

	/**
	 * @param m_IdOrden
	 *            the m_IdOrden to set
	 */
	public void setM_IdOrden(String m_IdOrden) {
		this.m_IdOrden = m_IdOrden;
	}

	/**
	 * @return the m_servicio
	 */
	public String getM_servicio() {
		return m_servicio;
	}

	/**
	 * @param m_servicio
	 *            the m_servicio to set
	 */
	public void setM_servicio(String m_servicio) {
		this.m_servicio = m_servicio;
	}

	/**
	 * @return the m_fecha
	 */
	public String getM_fecha() {
		return m_fecha;
	}

	/**
	 * @param m_fecha
	 *            the m_fecha to set
	 */
	public void setM_fecha(String m_fecha) {
		this.m_fecha = m_fecha;
	}

	/**
	 * @return the m_hora
	 */
	public String getM_hora() {
		return m_hora;
	}

	/**
	 * @param m_hora
	 *            the m_hora to set
	 */
	public void setM_hora(String m_hora) {
		this.m_hora = m_hora;
	}

	/**
	 * @return the m_aprobador
	 */
	public String getM_aprobador() {
		return m_aprobador;
	}

	/**
	 * @param m_aprobador
	 *            the m_aprobador to set
	 */
	public void setM_aprobador(String m_aprobador) {
		this.m_aprobador = m_aprobador;
	}

}
