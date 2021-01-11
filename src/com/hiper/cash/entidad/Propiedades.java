package com.hiper.cash.entidad;

public class Propiedades {


    //jwong 29/12/2008
    private String m_TimeTipoCambio = ""; //indica el tiempo entre actualizacion del tipo de cambio(en minutos)
    //jwong 16/01/2009
    private int m_NroDecimalesWS = -1; //numero de decimales entregados por el webservice(para ser formateado)
    //jwong 18/01/2009 formato de la fecha con la que llega del webservice
    private String m_FormatoFechaWS = "";
    //jwong 18/01/2009 formato de la hora con la que llega del webservice
    private String m_FormatoHoraWS = "";
    //jwong 18/02/2009 formato de fecha de ultimos movimientos que llega del webservice
    private String m_FormatoFechaWSLastMov = "";

    //jwong 25/02/2009 ruta donde se localiza el manual del Cash
    private String m_RutaManual = "";
    private String m_RutaDemoFlash = "";

    private String m_FormatoFechaWS1 = "";
    
    private boolean consultaAfiliacionTCO =  true;
    private boolean consultaTCO = true;
    
    private int limiteIntentosTCO=4;
    private int numeroBloqueosTCO=3;
    

    public Propiedades() {
    }

    public String getM_TimeTipoCambio() {
        return m_TimeTipoCambio;
    }

    public void setM_TimeTipoCambio(String m_TimeTipoCambio) {
        this.m_TimeTipoCambio = m_TimeTipoCambio;
    }

    public int getM_NroDecimalesWS() {
        return m_NroDecimalesWS;
    }

    public void setM_NroDecimalesWS(int m_NroDecimalesWS) {
        this.m_NroDecimalesWS = m_NroDecimalesWS;
    }

    public String getM_FormatoFechaWS() {
        return m_FormatoFechaWS;
    }

    public void setM_FormatoFechaWS(String m_FormatoFechaWS) {
        this.m_FormatoFechaWS = m_FormatoFechaWS;
    }

    public String getM_FormatoHoraWS() {
        return m_FormatoHoraWS;
    }

    public void setM_FormatoHoraWS(String m_FormatoHoraWS) {
        this.m_FormatoHoraWS = m_FormatoHoraWS;
    }

    /**
     * @return the m_FormatoFechaWSLastMov
     */
    public String getM_FormatoFechaWSLastMov() {
        return m_FormatoFechaWSLastMov;
    }

    /**
     * @param m_FormatoFechaWSLastMov the m_FormatoFechaWSLastMov to set
     */
    public void setM_FormatoFechaWSLastMov(String m_FormatoFechaWSLastMov) {
        this.m_FormatoFechaWSLastMov = m_FormatoFechaWSLastMov;
    }

    /**
     * @return the m_RutaManual
     */
    public String getM_RutaManual() {
        return m_RutaManual;
    }

    /**
     * @param m_RutaManual the m_RutaManual to set
     */
    public void setM_RutaManual(String m_RutaManual) {
        this.m_RutaManual = m_RutaManual;
    }

    public String getM_FormatoFechaWS1() {
        return m_FormatoFechaWS1;
    }

    public void setM_FormatoFechaWS1(String m_FormatoFechaWS1) {
        this.m_FormatoFechaWS1 = m_FormatoFechaWS1;
    }

    /**
     * @return the m_RutaDemoFlash
     */
    public String getM_RutaDemoFlash() {
        return m_RutaDemoFlash;
    }

    /**
     * @param m_RutaDemoFlash the m_RutaDemoFlash to set
     */
    public void setM_RutaDemoFlash(String m_RutaDemoFlash) {
        this.m_RutaDemoFlash = m_RutaDemoFlash;
    }


    public int getLimiteIntentosTCO() {
		return limiteIntentosTCO;
	}
    
    public void setLimiteIntentosTCO(int limiteIntentosTCO) {
		this.limiteIntentosTCO = limiteIntentosTCO;
	}
    
    public void setNumeroBloqueosTCO(int numeroBloqueosTCO) {
		this.numeroBloqueosTCO = numeroBloqueosTCO;
	}
    
    public int getNumeroBloqueosTCO() {
		return numeroBloqueosTCO;
	}
    
    public boolean isConsultaTCO() {
		return consultaTCO;
	}
    
    public void setConsultaTCO(boolean consultaTCO) {
		this.consultaTCO = consultaTCO;
	}
    
    public void setConsultaAfiliacionTCO(boolean consultaAfiliacionTCO) {
		this.consultaAfiliacionTCO = consultaAfiliacionTCO;
	}
    
    public boolean isConsultaAfiliacionTCO() {
		return consultaAfiliacionTCO;
	}
}