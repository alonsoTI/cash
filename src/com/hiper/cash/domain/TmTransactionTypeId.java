package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TmTransactionTypeId generated by hbm2java
 */
public class TmTransactionTypeId  implements java.io.Serializable {


     private String ctttransactionId;
     private String cttaplicacion;

    public TmTransactionTypeId() {
    }

    public TmTransactionTypeId(String ctttransactionId, String cttaplicacion) {
       this.ctttransactionId = ctttransactionId;
       this.cttaplicacion = cttaplicacion;
    }
   
    public String getCtttransactionId() {
        return this.ctttransactionId;
    }
    
    public void setCtttransactionId(String ctttransactionId) {
        this.ctttransactionId = ctttransactionId;
    }
    public String getCttaplicacion() {
        return this.cttaplicacion;
    }
    
    public void setCttaplicacion(String cttaplicacion) {
        this.cttaplicacion = cttaplicacion;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TmTransactionTypeId) ) return false;
		 TmTransactionTypeId castOther = ( TmTransactionTypeId ) other; 
         
		 return ( (this.getCtttransactionId()==castOther.getCtttransactionId()) || ( this.getCtttransactionId()!=null && castOther.getCtttransactionId()!=null && this.getCtttransactionId().equals(castOther.getCtttransactionId()) ) )
 && ( (this.getCttaplicacion()==castOther.getCttaplicacion()) || ( this.getCttaplicacion()!=null && castOther.getCttaplicacion()!=null && this.getCttaplicacion().equals(castOther.getCttaplicacion()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCtttransactionId() == null ? 0 : this.getCtttransactionId().hashCode() );
         result = 37 * result + ( getCttaplicacion() == null ? 0 : this.getCttaplicacion().hashCode() );
         return result;
   }   


}

