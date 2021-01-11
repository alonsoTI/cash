package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TaCamposRequeridosId generated by hbm2java
 */
public class TaCamposRequeridosId  implements java.io.Serializable {


     private long ccridTipoPagoServ;
     private long ccridServEmp;
     private String ccridTipoPago;
     private int ccrposicion;

    public TaCamposRequeridosId() {
    }

    public TaCamposRequeridosId(long ccridTipoPagoServ, long ccridServEmp, String ccridTipoPago, int ccrposicion) {
       this.ccridTipoPagoServ = ccridTipoPagoServ;
       this.ccridServEmp = ccridServEmp;
       this.ccridTipoPago = ccridTipoPago;
       this.ccrposicion = ccrposicion;
    }
   
    public long getCcridTipoPagoServ() {
        return this.ccridTipoPagoServ;
    }
    
    public void setCcridTipoPagoServ(long ccridTipoPagoServ) {
        this.ccridTipoPagoServ = ccridTipoPagoServ;
    }
    public long getCcridServEmp() {
        return this.ccridServEmp;
    }
    
    public void setCcridServEmp(long ccridServEmp) {
        this.ccridServEmp = ccridServEmp;
    }
    public String getCcridTipoPago() {
        return this.ccridTipoPago;
    }
    
    public void setCcridTipoPago(String ccridTipoPago) {
        this.ccridTipoPago = ccridTipoPago;
    }
    public int getCcrposicion() {
        return this.ccrposicion;
    }
    
    public void setCcrposicion(int ccrposicion) {
        this.ccrposicion = ccrposicion;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TaCamposRequeridosId) ) return false;
		 TaCamposRequeridosId castOther = ( TaCamposRequeridosId ) other; 
         
		 return (this.getCcridTipoPagoServ()==castOther.getCcridTipoPagoServ())
 && (this.getCcridServEmp()==castOther.getCcridServEmp())
 && ( (this.getCcridTipoPago()==castOther.getCcridTipoPago()) || ( this.getCcridTipoPago()!=null && castOther.getCcridTipoPago()!=null && this.getCcridTipoPago().equals(castOther.getCcridTipoPago()) ) )
 && (this.getCcrposicion()==castOther.getCcrposicion());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getCcridTipoPagoServ();
         result = 37 * result + (int) this.getCcridServEmp();
         result = 37 * result + ( getCcridTipoPago() == null ? 0 : this.getCcridTipoPago().hashCode() );
         result = 37 * result + this.getCcrposicion();
         return result;
   }   


}


