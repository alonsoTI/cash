package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TaDetalleFormatoId generated by hbm2java
 */
public class TaDetalleFormatoId  implements java.io.Serializable {


     private int cdfidFormato;
     private int ndfposicionCampo;

    public TaDetalleFormatoId() {
    }

    public TaDetalleFormatoId(int cdfidFormato, int ndfposicionCampo) {
       this.cdfidFormato = cdfidFormato;
       this.ndfposicionCampo = ndfposicionCampo;
    }
   
    public int getCdfidFormato() {
        return this.cdfidFormato;
    }
    
    public void setCdfidFormato(int cdfidFormato) {
        this.cdfidFormato = cdfidFormato;
    }
    public int getNdfposicionCampo() {
        return this.ndfposicionCampo;
    }
    
    public void setNdfposicionCampo(int ndfposicionCampo) {
        this.ndfposicionCampo = ndfposicionCampo;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TaDetalleFormatoId) ) return false;
		 TaDetalleFormatoId castOther = ( TaDetalleFormatoId ) other; 
         
		 return (this.getCdfidFormato()==castOther.getCdfidFormato())
 && (this.getNdfposicionCampo()==castOther.getNdfposicionCampo());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getCdfidFormato();
         result = 37 * result + this.getNdfposicionCampo();
         return result;
   }   


}


