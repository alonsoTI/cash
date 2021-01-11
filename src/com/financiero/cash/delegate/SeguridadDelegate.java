package com.financiero.cash.delegate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.financiero.cash.exception.NotAvailableException;
import com.financiero.cash.exception.NotFoundException;
import com.hiper.cash.dao.TmAccesoDAO;
import com.hiper.cash.dao.TxDisponibilidadDAO;
import com.hiper.cash.dao.hibernate.TmAccesoDAOHibernate;
import com.hiper.cash.dao.jdbc.TxDisponibilidadDAOJDBC;
import com.hiper.cash.dao.ws.SixLinux;
import com.hiper.cash.domain.TmAcceso;
import com.hiper.cash.domain.TxDisponibilidad;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.EstadoTarjeta;
import com.financiero.cash.service.SeguridadService;

public class SeguridadDelegate implements SeguridadService {

	private static SeguridadDelegate instanciaUnica;
	private static final String FILE_XML_BLOQUEO = CashConstants.RES_FINANCIERO.getString("file_xml_bloqueo");
	private TmAccesoDAO accesoDAO = new TmAccesoDAOHibernate();

	public static SeguridadDelegate getInstance() {
		if (instanciaUnica == null) {
			instanciaUnica = new SeguridadDelegate();
		}
		return instanciaUnica;
	}

	private SeguridadDelegate() {
	}

	private TxDisponibilidad buscarHorarioModulo(String idModulo)
			throws NotAvailableException{		
		try{
			TxDisponibilidad disponibilidad = new TxDisponibilidad();
			File f = new File(FILE_XML_BLOQUEO);
			if (!f.exists()) {
				crearXML(f);
			}
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(f);
			Element raiz = doc.getRootElement();
			List<Element> horarios = raiz.getChildren("horario");
			Iterator<Element> it = horarios.iterator();
			Element horario;
			boolean encontrado = false;
			String id;
			while (it.hasNext() && !encontrado) {
				horario = it.next();
				id = horario.getAttributeValue("id");
				if (id.equals(idModulo)) {
					disponibilidad.setId(id);
					disponibilidad.setfInicio(new Date(Long.parseLong(horario.getChildText("inicio"))));
					disponibilidad.setfFinal(new Date(Long.parseLong(horario.getChildText("final"))));
					encontrado = true;
				}
			}
			if (!encontrado) {
				throw new NotAvailableException("ERROR: El modulo no se encuentra registrado en el XML de validaciones");
			}
			return disponibilidad;
		}catch(SQLException e){
			throw new NotAvailableException("ERROR: El acceso a la base datos no se realizo correctamente");
		}catch(IOException e){
			throw new NotAvailableException("ERROR: El archivo XML de horarios no se creo correctamente");
		}catch (JDOMException e){
			throw new NotAvailableException("ERROR: XML de validacion de horario no se puede leer");
		}		
	}

	private void crearXML(File f) throws SQLException, IOException  {
		try{
			Document doc;
			TxDisponibilidadDAO daoDisponibilidad = new TxDisponibilidadDAOJDBC();
			List<TxDisponibilidad> lista = daoDisponibilidad.findAll();
			doc = new Document(crearDisponibilidad(lista));
			XMLOutputter out = new XMLOutputter();
			FileOutputStream file = new FileOutputStream(f);
			out.output(doc, file);
			file.flush();
			file.close();
		}catch(SQLException e){
			throw e;
		}		
	}

	/* (non-Javadoc)
     * @see com.financiero.cash.delegate.SeguridadService#verificaDisponibilidad(java.lang.String)
     */
	@Override
    public boolean verificaDisponibilidad(String idModulo) throws Exception {
		TxDisponibilidad disponibilidad = buscarHorarioModulo(idModulo);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(disponibilidad.getfInicio());
		GregorianCalendar fecha = new GregorianCalendar();
		if (fecha.get(Calendar.HOUR_OF_DAY) < gc.get(Calendar.HOUR_OF_DAY)) {
			return false;
		} else {
			if (fecha.get(Calendar.HOUR_OF_DAY) == gc.get(Calendar.HOUR_OF_DAY)) {
				if (fecha.get(Calendar.MINUTE) < gc.get(Calendar.MINUTE)) {
					return false;
				}
			}
		}
		gc.setTime(disponibilidad.getfFinal());
		if (fecha.get(Calendar.HOUR_OF_DAY) > gc.get(Calendar.HOUR_OF_DAY)) {
			return false;
		} else {
			if (fecha.get(Calendar.HOUR_OF_DAY) == gc.get(Calendar.HOUR_OF_DAY)) {
				if (fecha.get(Calendar.MINUTE) >= gc.get(Calendar.MINUTE)) {
					return false;
				}
			}
		}
		return true;
	}

	private Element crearDisponibilidad(List<TxDisponibilidad> horarios) {
		Element disponibilidad = new Element("disponibilidad");
		for (TxDisponibilidad horario : horarios) {
			disponibilidad.addContent(crearHorarioModulo(horario.getId(),
					horario.getfInicio().getTime(), horario.getfFinal()
							.getTime()));
		}

		return disponibilidad;
	}

	private Element crearHorarioModulo(String idModulo, long inicio, long fin) {
		Element disponibilidad = new Element("horario");
		disponibilidad.setAttribute("id", idModulo);
		Element horaInicio = new Element("inicio").setText(String.valueOf(inicio));
		Element horaFinal = new Element("final").setText(String.valueOf(fin));
		disponibilidad.addContent(horaInicio);
		disponibilidad.addContent(horaFinal);

		return disponibilidad;
	}
	

	/* (non-Javadoc)
     * @see com.financiero.cash.delegate.SeguridadService#generarCoordenada()
     */
	@Override
    public String  generarCoordenada()throws Exception{
		SixLinux cliente = SixLinux.getInstance();
		String trama =  cliente.getTrama("seguridad.tco.solicitarCoordenada.app", "seguridad.tco.solicitarCoordenada.trx", "seguridad.tco.solicitarCoordenada.length","*");
		return trama.substring(36,38);		
	}
	
	
	public static String  codificarCoordenada(String coordenada)throws Exception{
		int fila = Integer.valueOf(coordenada.substring(0,1));
		int columna = Integer.valueOf(coordenada.substring(1, 2));			
		StringBuilder sb = new StringBuilder();
		switch( fila ){
			case 0 : sb = sb.append("A");
					break;
			case 1 : sb = sb.append("B");
					break;			 
			case 2 : sb = sb.append("C");
			 		break;
			case 3 : sb = sb.append("D");
			 		break;
			case 4 : sb = sb.append("E");
			 		break;
			case 5 : sb = sb.append("F");
					break;					 
		}
		sb = sb.append(columna+1);
		return sb.toString();
	}
	
	
	/* (non-Javadoc)
     * @see com.financiero.cash.delegate.SeguridadService#obtenerTCO(java.lang.String)
     */
	@Override
    public String obtenerTCO(String tarjeta) throws Exception{
		SixLinux cliente = SixLinux.getInstance();
		String message = new StringBuilder(tarjeta).append("*").toString(); 
		String trama = cliente.getTrama("seguridad.tco.validarAfiliacion.app", "seguridad.tco.validarAfiliacion.trx", "seguridad.tco.validarAfiliacion.length",message);
		String tarjetaCoordenada = trama.substring(37,53);
		int estado = Integer.parseInt(trama.substring(36,37));	
		if( EstadoTarjeta.ACTIVA.ordinal() == estado ){ 
			return tarjetaCoordenada;
		}else if ( EstadoTarjeta.CANCELADA.ordinal() == estado ){
			throw new NotFoundException("Tarjeta Cancelada/Inactiva");
		}else if ( EstadoTarjeta.NO_AFILIADA.ordinal() == estado ){
			throw new NotAvailableException("Tarjeta No Afiliada");
		}else{
			throw new Exception("Estado de TCO No Determinado");
		}
	}
	
	private boolean esFechaActual(Date fechaAcceso){		
		Calendar calActual = Calendar.getInstance();
		calActual.setTime(new Date());
		Calendar calAcceso = Calendar.getInstance();
		calAcceso.setTime(fechaAcceso);
		
		if( calAcceso.get(Calendar.DAY_OF_MONTH) == calActual.get(Calendar.DAY_OF_MONTH)
			&& calAcceso.get(Calendar.MONTH) == calActual.get(Calendar.MONTH)
			&& calAcceso.get(Calendar.YEAR) == calActual.get(Calendar.YEAR)			
		)	{
			return true;
		}
			
		return false;
	}
	
	/* (non-Javadoc)
     * @see com.financiero.cash.delegate.SeguridadService#validarActividadTCO(java.lang.String, int, int)
     */
	@Override
    public boolean validarActividadTCO(String tco,int maximoNumeroIntentos,int maximoNumeroBloqueos)throws Exception{
		TmAcceso acceso = accesoDAO.getTmAcceso(tco);		
		if( acceso != null ){
			if( acceso.getNumeroBloqueo()>=maximoNumeroBloqueos ){
				throw new NotFoundException("Tarjeta Supero el Limite de Bloqueos Consecutivos");
			}else{
				if(esFechaActual(acceso.getFechaAcceso())){				
					if( acceso.getNumeroIntento()>= maximoNumeroIntentos ){
						return false;
					}
				}else{
					acceso.setNumeroIntento(0);					
					acceso.setFechaAcceso(new Date());
					accesoDAO.actualizar(acceso);
				}
			}						
		}else{			
			accesoDAO.crear(tco);
		}
		return true;
	}
	
	
	/* (non-Javadoc)
     * @see com.financiero.cash.delegate.SeguridadService#esUsuarioSoloConsulta(java.lang.String)
     */
	@Override
    public boolean esUsuarioSoloConsulta(String tarjeta) throws Exception{
		return	accesoDAO.esConsulta(tarjeta);		
	}
	
	/* (non-Javadoc)
     * @see com.financiero.cash.delegate.SeguridadService#validarCoordenada(java.lang.String, java.lang.String, java.lang.String, int)
     */
	@Override
    public boolean validarCoordenada(String tco,String coordenada, String clave,int maximoNumeroIntentos) throws Exception{		
		SixLinux cliente = SixLinux.getInstance();
		StringBuilder tarjeta = new StringBuilder(tco.substring(0,14)).append(coordenada);
		String tramaPinPad=cliente.getToken(tarjeta.toString(), clave);
		tarjeta = tarjeta.append("   ");		
		StringBuilder pin = new StringBuilder(tramaPinPad.substring(21, 37));		
		StringBuilder message=new StringBuilder(tarjeta.toString()).append(pin).append("*");
		String trama = cliente.getTramaNoLog("seguridad.tco.validarCoordenada.app", "seguridad.tco.validarCoordenada.trx", "seguridad.tco.validarCoordenada.length",message.toString());		
		String codigo = trama.substring(17,21);
		TmAcceso acceso = accesoDAO.getTmAcceso(tco);
		if( CashConstants.VAL_IBS_EXITO.equals(codigo)	){			
			acceso.setTarjeta(tco);
			acceso.setNumeroIntento(0);
			acceso.setNumeroBloqueo(0);
			accesoDAO.actualizar(acceso);
			return true;				
		}else{						
			int numeroIntento=acceso.getNumeroIntento();
			numeroIntento++;
			acceso.setNumeroIntento(numeroIntento);			
			if( numeroIntento>= maximoNumeroIntentos ){
				int numeroBloqueo = acceso.getNumeroBloqueo();
				numeroBloqueo++;
				acceso.setNumeroBloqueo(numeroBloqueo);
				accesoDAO.actualizar(acceso);				
				throw new NotAvailableException("Tarjeta supero el numero de intentos permitidos");
			}else{
				accesoDAO.actualizar(acceso);
			}
			return false;			
		}		
	}	
	
	
}
