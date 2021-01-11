package com.hiper.cash.domain;
// Generated 23/12/2009 01:49:55 PM by Hibernate Tools 3.2.1.GA



/**
 * TaEntidadEmpresaId generated by hbm2java
 */
public class TaEntidadEmpresaId  implements java.io.Serializable {


     private String ceeidEntidad;
     private String deetipo;
     private long ceeidServEmpresa;

    public TaEntidadEmpresaId() {
    }

    public TaEntidadEmpresaId(String ceeidEntidad, String deetipo, long ceeidServEmpresa) {
       this.ceeidEntidad = ceeidEntidad;
       this.deetipo = deetipo;
       this.ceeidServEmpresa = ceeidServEmpresa;
    }
   
    public String getCeeidEntidad() {
        return this.ceeidEntidad;
    }
    
    public void setCeeidEntidad(String ceeidEntidad) {
        this.ceeidEntidad = ceeidEntidad;
    }
    public String getDeetipo() {
        return this.deetipo;
    }
    
    public void setDeetipo(String deetipo) {
        this.deetipo = deetipo;
    }
    public long getCeeidServEmpresa() {
        return this.ceeidServEmpresa;
    }
    
    public void setCeeidServEmpresa(long ceeidServEmpresa) {
        this.ceeidServEmpresa = ceeidServEmpresa;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TaEntidadEmpresaId) ) return false;
		 TaEntidadEmpresaId castOther = ( TaEntidadEmpresaId ) other; 
         
		 return ( (this.getCeeidEntidad()==castOther.getCeeidEntidad()) || ( this.getCeeidEntidad()!=null && castOther.getCeeidEntidad()!=null && this.getCeeidEntidad().equals(castOther.getCeeidEntidad()) ) )
 && ( (this.getDeetipo()==castOther.getDeetipo()) || ( this.getDeetipo()!=null && castOther.getDeetipo()!=null && this.getDeetipo().equals(castOther.getDeetipo()) ) )
 && (this.getCeeidServEmpresa()==castOther.getCeeidServEmpresa());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCeeidEntidad() == null ? 0 : this.getCeeidEntidad().hashCode() );
         result = 37 * result + ( getDeetipo() == null ? 0 : this.getDeetipo().hashCode() );
         result = 37 * result + (int) this.getCeeidServEmpresa();
         return result;
   }   


}


