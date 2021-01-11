/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.financiero.cash.util.TipoTransferencia;

public class TransferenciasForm extends ActionForm{   
	private static final long serialVersionUID = -6252280668668977753L;
	private String m_Empresa;
    private String m_Servicio;

    private String m_CtaCargo;
    private String m_CtaAbono;
    private String m_Monto;
    private String m_Moneda;
    private String m_Referencia;

    private String m_NumCuentaAbonoCci;
    private String m_BancoBenef;
    private String m_TipoDocBenef;
    private String m_NumDocBenef;
    private String m_NombreBenef;
    private String m_ApePatBenef;
    private String m_ApeMatBenef;
    private String m_DireccionBenef;
    private String m_TlfBenef;
    
    private String m_CtaAbonoEntidad;
    private String m_CtaAbonoOficina;
    private String m_CtaAbonoCuenta;
    private String m_CtaAbonoControl;

    //jwong 08/05/2009
    private String m_IdTipoDocBenef;

    //jwong 11/05/2009
    private String m_FlagCliente;

        //jmoreno 21/07/09
    private String m_NombreBanco;
    
    //andqui 09/06/2011
    private String m_IdTipoTransf;
    
    private String m_RazonSocial;
    
    private String cuentaCargo;
    private String cuentaAbono;
    private String referencia;    
    private double monto;
    private String moneda;
    private String codigoMoneda;
    private String nombreEmpresa;
	private String codigoEmpresa;
    private String documento;
    private String nroDocumento;
    
    private String nombres;
    private String apellidoPaterno;
    private String apellidoPaterno2;
    private String apellidoMaterno;
    private String apellidoMaterno2;
    private String telefono;
    private String direccion;
    private String razonSocial;
    
    private String ruc;
    private String dni;
    private String numeroDocumento;
    
    private TipoTransferencia tipo;
    private int correlativoDiario;
   
	
	   
    public void reset(ActionMapping mapping, HttpServletRequest request) {    	
        super.reset(mapping, request);  
        monto = 0.0;
        referencia = "";  
        codigoMoneda = "";
        cuentaAbono = "";
        cuentaCargo = "";
        m_FlagCliente = "";
        apellidoMaterno = "";
        apellidoPaterno = "";
        ruc = "";
        nombres = "";
        razonSocial = "";
        dni = "";
        telefono = "";
        direccion = "";
        m_NombreBanco = "";
        m_CtaAbonoEntidad = "";
        m_CtaAbonoOficina = "";
        m_CtaAbonoCuenta = "";
        m_CtaAbonoControl = "";
        documento="";
        numeroDocumento="";
        apellidoMaterno2="";
        apellidoPaterno2="";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public String getM_Empresa() {
        return m_Empresa;
    }

    public void setM_Empresa(String m_Empresa) {
        this.m_Empresa = m_Empresa;
    }

    public String getM_Servicio() {
        return m_Servicio;
    }

    public void setM_Servicio(String m_Servicio) {
        this.m_Servicio = m_Servicio;
    }

    public String getM_CtaCargo() {
        return m_CtaCargo;
    }

    public void setM_CtaCargo(String m_CtaCargo) {
        this.m_CtaCargo = m_CtaCargo;
    }

    public String getM_CtaAbono() {
        return m_CtaAbono;
    }

    public void setM_CtaAbono(String m_CtaAbono) {
        this.m_CtaAbono = m_CtaAbono;
    }

    public String getM_Monto() {
        return m_Monto;
    }

    public void setM_Monto(String m_Monto) {
        this.m_Monto = m_Monto;
    }

    public String getM_Moneda() {
        return m_Moneda;
    }

    public void setM_Moneda(String m_Moneda) {
        this.m_Moneda = m_Moneda;
    }

    public String getM_Referencia() {
        return m_Referencia;
    }

    public void setM_Referencia(String m_Referencia) {
        this.m_Referencia = m_Referencia;
    }

    public String getM_NumCuentaAbonoCci() {
        return m_NumCuentaAbonoCci;
    }

    public void setM_NumCuentaAbonoCci(String m_NumCuentaAbonoCci) {
        this.m_NumCuentaAbonoCci = m_NumCuentaAbonoCci;
    }

    public String getM_BancoBenef() {
        return m_BancoBenef;
    }

    public void setM_BancoBenef(String m_BancoBenef) {
        this.m_BancoBenef = m_BancoBenef;
    }

    public String getM_TipoDocBenef() {
        return m_TipoDocBenef;
    }

    public void setM_TipoDocBenef(String m_TipoDocBenef) {
        this.m_TipoDocBenef = m_TipoDocBenef;
    }

    public String getM_NumDocBenef() {
        return m_NumDocBenef;
    }

    public void setM_NumDocBenef(String m_NumDocBenef) {
        this.m_NumDocBenef = m_NumDocBenef;
    }

    public String getM_NombreBenef() {
        return m_NombreBenef;
    }

    public void setM_NombreBenef(String m_NombreBenef) {
        this.m_NombreBenef = m_NombreBenef;
    }

    public String getM_ApePatBenef() {
        return m_ApePatBenef;
    }

    public void setM_ApePatBenef(String m_ApePatBenef) {
        this.m_ApePatBenef = m_ApePatBenef;
    }

    public String getM_ApeMatBenef() {
        return m_ApeMatBenef;
    }

    public void setM_ApeMatBenef(String m_ApeMatBenef) {
        this.m_ApeMatBenef = m_ApeMatBenef;
    }

    public String getM_DireccionBenef() {
        return m_DireccionBenef;
    }

    public void setM_DireccionBenef(String m_DireccionBenef) {
        this.m_DireccionBenef = m_DireccionBenef;
    }

    public String getM_TlfBenef() {
        return m_TlfBenef;
    }

    public void setM_TlfBenef(String m_TlfBenef) {
        this.m_TlfBenef = m_TlfBenef;
    }

    public String getM_CtaAbonoEntidad() {
        return m_CtaAbonoEntidad;
    }

    public void setM_CtaAbonoEntidad(String m_CtaAbonoEntidad) {
        this.m_CtaAbonoEntidad = m_CtaAbonoEntidad;
    }

    public String getM_CtaAbonoOficina() {
        return m_CtaAbonoOficina;
    }

    public void setM_CtaAbonoOficina(String m_CtaAbonoOficina) {
        this.m_CtaAbonoOficina = m_CtaAbonoOficina;
    }

    public String getM_CtaAbonoCuenta() {
        return m_CtaAbonoCuenta;
    }

    public void setM_CtaAbonoCuenta(String m_CtaAbonoCuenta) {
        this.m_CtaAbonoCuenta = m_CtaAbonoCuenta;
    }

    public String getM_CtaAbonoControl() {
        return m_CtaAbonoControl;
    }

    public void setM_CtaAbonoControl(String m_CtaAbonoControl) {
        this.m_CtaAbonoControl = m_CtaAbonoControl;
    }

    /**
     * @return the m_IdTipoDocBenef
     */
    public String getM_IdTipoDocBenef() {
        return m_IdTipoDocBenef;
    }

    /**
     * @param m_IdTipoDocBenef the m_IdTipoDocBenef to set
     */
    public void setM_IdTipoDocBenef(String m_IdTipoDocBenef) {
        this.m_IdTipoDocBenef = m_IdTipoDocBenef;
    }

    /**
     * @return the m_FlagCliente
     */
    public String getM_FlagCliente() {    	
        return m_FlagCliente;
    }
    
    public char getM_FlagCliente2() {    	
        if(StringUtils.isNotEmpty(m_FlagCliente)){
        	return m_FlagCliente.charAt(0);
        }else{
        	return ' ';
        }
    }
    

    /**
     * @param m_FlagCliente the m_FlagCliente to set
     */
    public void setM_FlagCliente(String m_FlagCliente) {
        this.m_FlagCliente = m_FlagCliente;
    }

    public String getM_NombreBanco() {
        return m_NombreBanco;
    }

    public void setM_NombreBanco(String m_NombreBanco) {
        this.m_NombreBanco = m_NombreBanco;
    }

	public String getM_IdTipoTransf() {
		return m_IdTipoTransf;
	}

	public void setM_IdTipoTransf(String mIdTipoTransf) {
		m_IdTipoTransf = mIdTipoTransf;
	}
	
	public void setM_RazonSocial(String mRazonSocial) {
		m_RazonSocial = mRazonSocial;
	}
	
	public String getM_RazonSocial() {
		return m_RazonSocial;
	}

	public String getCuentaCargo() {
		return cuentaCargo;
	}

	public void setCuentaCargo(String cuentaCargo) {
		this.cuentaCargo = cuentaCargo;
	}

	public String getCuentaAbono() {
		return cuentaAbono;
	}

	public void setCuentaAbono(String cuentaAbono) {
		this.cuentaAbono = cuentaAbono;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	
	public String getMoneda() {
		return moneda;
	}
	public void setCodigoMoneda(String codigoMoneda) {
		this.codigoMoneda = codigoMoneda;
	}
	
	public String getCodigoMoneda() {
		return codigoMoneda;
	}
	
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	public void setTipo(TipoTransferencia tipo) {
		this.tipo = tipo;
	}
	
	public TipoTransferencia getTipo() {
		return tipo;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	
	public String getNroDocumento() {
		return nroDocumento;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombre) {
		this.nombres = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	public String getRazonSocial() {
		return razonSocial;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public String getDni() {
		return dni;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	
	public String getRuc() {
		return ruc;
	}

	public int getCorrelativoDiario() {
		return correlativoDiario;
	}

	public void setCorrelativoDiario(int correlativoDiario) {
		this.correlativoDiario = correlativoDiario;
	}	

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getApellidoPaterno2() {
		return apellidoPaterno2;
	}

	public void setApellidoPaterno2(String apellidoPaterno2) {
		this.apellidoPaterno2 = apellidoPaterno2;
	}

	public String getApellidoMaterno2() {
		return apellidoMaterno2;
	}

	public void setApellidoMaterno2(String apellidoMaterno2) {
		this.apellidoMaterno2 = apellidoMaterno2;
	}
	
	
}
