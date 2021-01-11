package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TxListFieldDetailId generated by hbm2java
 */
public class TxListFieldDetailId  implements java.io.Serializable {


     private String clfdtableS;
     private String clfdtableD;
     private String clfdcodeS;
     private String clfdcodeD;

    public TxListFieldDetailId() {
    }

    public TxListFieldDetailId(String clfdtableS, String clfdtableD, String clfdcodeS, String clfdcodeD) {
       this.clfdtableS = clfdtableS;
       this.clfdtableD = clfdtableD;
       this.clfdcodeS = clfdcodeS;
       this.clfdcodeD = clfdcodeD;
    }
   
    public String getClfdtableS() {
        return this.clfdtableS;
    }
    
    public void setClfdtableS(String clfdtableS) {
        this.clfdtableS = clfdtableS;
    }
    public String getClfdtableD() {
        return this.clfdtableD;
    }
    
    public void setClfdtableD(String clfdtableD) {
        this.clfdtableD = clfdtableD;
    }
    public String getClfdcodeS() {
        return this.clfdcodeS;
    }
    
    public void setClfdcodeS(String clfdcodeS) {
        this.clfdcodeS = clfdcodeS;
    }
    public String getClfdcodeD() {
        return this.clfdcodeD;
    }
    
    public void setClfdcodeD(String clfdcodeD) {
        this.clfdcodeD = clfdcodeD;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TxListFieldDetailId) ) return false;
		 TxListFieldDetailId castOther = ( TxListFieldDetailId ) other; 
         
		 return ( (this.getClfdtableS()==castOther.getClfdtableS()) || ( this.getClfdtableS()!=null && castOther.getClfdtableS()!=null && this.getClfdtableS().equals(castOther.getClfdtableS()) ) )
 && ( (this.getClfdtableD()==castOther.getClfdtableD()) || ( this.getClfdtableD()!=null && castOther.getClfdtableD()!=null && this.getClfdtableD().equals(castOther.getClfdtableD()) ) )
 && ( (this.getClfdcodeS()==castOther.getClfdcodeS()) || ( this.getClfdcodeS()!=null && castOther.getClfdcodeS()!=null && this.getClfdcodeS().equals(castOther.getClfdcodeS()) ) )
 && ( (this.getClfdcodeD()==castOther.getClfdcodeD()) || ( this.getClfdcodeD()!=null && castOther.getClfdcodeD()!=null && this.getClfdcodeD().equals(castOther.getClfdcodeD()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getClfdtableS() == null ? 0 : this.getClfdtableS().hashCode() );
         result = 37 * result + ( getClfdtableD() == null ? 0 : this.getClfdtableD().hashCode() );
         result = 37 * result + ( getClfdcodeS() == null ? 0 : this.getClfdcodeS().hashCode() );
         result = 37 * result + ( getClfdcodeD() == null ? 0 : this.getClfdcodeD().hashCode() );
         return result;
   }   


}

