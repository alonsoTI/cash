package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TaFuncionarioServicioId generated by hbm2java
 */
public class TaFuncionarioServicioId  implements java.io.Serializable {


     private long cfsidServEmp;
     private String cfsidFuncionario;

    public TaFuncionarioServicioId() {
    }

    public TaFuncionarioServicioId(long cfsidServEmp, String cfsidFuncionario) {
       this.cfsidServEmp = cfsidServEmp;
       this.cfsidFuncionario = cfsidFuncionario;
    }
   
    public long getCfsidServEmp() {
        return this.cfsidServEmp;
    }
    
    public void setCfsidServEmp(long cfsidServEmp) {
        this.cfsidServEmp = cfsidServEmp;
    }
    public String getCfsidFuncionario() {
        return this.cfsidFuncionario;
    }
    
    public void setCfsidFuncionario(String cfsidFuncionario) {
        this.cfsidFuncionario = cfsidFuncionario;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TaFuncionarioServicioId) ) return false;
		 TaFuncionarioServicioId castOther = ( TaFuncionarioServicioId ) other; 
         
		 return (this.getCfsidServEmp()==castOther.getCfsidServEmp())
 && ( (this.getCfsidFuncionario()==castOther.getCfsidFuncionario()) || ( this.getCfsidFuncionario()!=null && castOther.getCfsidFuncionario()!=null && this.getCfsidFuncionario().equals(castOther.getCfsidFuncionario()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getCfsidServEmp();
         result = 37 * result + ( getCfsidFuncionario() == null ? 0 : this.getCfsidFuncionario().hashCode() );
         return result;
   }   


}


