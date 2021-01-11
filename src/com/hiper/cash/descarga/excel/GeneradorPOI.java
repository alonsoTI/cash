/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.descarga.excel;

import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanConsMovHistoricosXML;
import com.hiper.cash.xml.bean.BeanConsRelBanco;
import com.hiper.cash.xml.bean.BeanConsultaSaldosXML;
import com.hiper.cash.xml.bean.BeanTypeAccount;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 *
 * @author jwong
 */
public class GeneradorPOI {

    /**
     * jwong 26/03/2009 crearExcel metodo que encapsula la creacion de un documento excel
     * @param titConsulta, titulo de la hoja del documento excel
     * @param lblColumnas, nombre de las columnas del reporte excel
     * @param lstData, contenido del reporte
     * @return
     */
    public static HSSFWorkbook crearExcel(String titConsulta, ArrayList lblColumnas, ArrayList lstData, BeanConsMovHistoricosXML beanXML, String tituloReporte) {
        HSSFWorkbook libroXLS = null;
        HSSFSheet hojaXLS = null;
        HSSFRow filaXLS = null;
        HSSFCell celdaXLS = null;
        //estilo para el titulo
        HSSFCellStyle estiloTit = null;
        HSSFFont fuenteTit = null;
        //estilo para la data
        HSSFCellStyle estiloDat = null;
        HSSFFont fuenteDat = null;

        HSSFCellStyle estiloDatCent = null;

        HSSFCellStyle estiloTitDet = null;
        HSSFFont fuenteTitDet = null;

        libroXLS = new HSSFWorkbook();

        //Estilos para la cabecera de la hoja de calculo
        estiloTit = libroXLS.createCellStyle();
        estiloDat = libroXLS.createCellStyle();
        estiloDatCent = libroXLS.createCellStyle();
        estiloTitDet = libroXLS.createCellStyle();

        fuenteTit = libroXLS.createFont();
        fuenteDat = libroXLS.createFont();
        fuenteTit.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //En negrita
        fuenteTit.setFontHeight((short)160);
        fuenteTit.setFontName("Arial");
        fuenteTit.setColor(HSSFColor.WHITE.index);//jmoreno 28-08-09

        fuenteDat.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); //Sin negrita
        fuenteDat.setFontHeight((short)160);
        fuenteDat.setFontName("Arial");        

        estiloTit.setFont(fuenteTit);
        estiloTit.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTit.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTit.setFillForegroundColor(HSSFColor.BLUE.index);//jmoreno 28-08-09
        estiloTit.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        fuenteTitDet = libroXLS.createFont();
        fuenteTitDet.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //En negrita
        fuenteTitDet.setFontHeight((short)160);
        fuenteTitDet.setFontName("Arial");
        estiloTitDet.setFont(fuenteTitDet);
        estiloTitDet.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTitDet.setFillForegroundColor(HSSFColor.YELLOW.index);
        estiloTitDet.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        estiloDat.setFont(fuenteDat);
        estiloDat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderTop(HSSFCellStyle.BORDER_THIN);

        estiloDatCent.setFont(fuenteDat);
        estiloDatCent.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        hojaXLS = libroXLS.createSheet(titConsulta);

        //Creamos la cabecera de la hoja de calculo
        int z = 0;

        String lblCol = null;

        if(tituloReporte!=null){ //jwong 16/08/2009 para incluir el titulo del reporte
            filaXLS = hojaXLS.createRow(z++);
            lblCol = tituloReporte;
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            filaXLS = hojaXLS.createRow(z++);
        }

        if(beanXML!=null){ //si existe el bean crearemos la cabecera con el detalle de la cuenta
            //crear una celda con el ancho del numero de columnas enviadas
            filaXLS = hojaXLS.createRow(z++);
            lblCol = "DETALLE DE SALDO";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            
            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Titular:" + beanXML.getM_Titular();
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            lblCol = "Moneda:" + beanXML.getM_Moneda();
            celdaXLS = filaXLS.createCell((short)1);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)1,(short)((lblCol.length()+10) * 256));
            lblCol = "Cuenta:" + beanXML.getM_Cuenta();
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)((lblCol.length()+10) * 256));

            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Saldo Disponible";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            lblCol = beanXML.getM_SaldoDisponible();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)((lblCol.length()+10) * 256));

            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Saldo Retenido";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            lblCol = beanXML.getM_SaldoRetenido();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)((lblCol.length()+10) * 256));
            
            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Saldo Contable";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            lblCol = beanXML.getM_SaldoContable();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)((lblCol.length()+10) * 256));

            //crear una celda con el ancho del numero de columnas enviadas
            filaXLS = hojaXLS.createRow(z++);
            filaXLS = hojaXLS.createRow(z++);
            lblCol = "DETALLE DE MOVIMIENTOS";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            
        }


        filaXLS = hojaXLS.createRow(z++);
        //creamos las columnas del reporte
        for(int h=0 ; h<lblColumnas.size() ; h++){
            lblCol = (String)lblColumnas.get(h);
            celdaXLS = filaXLS.createCell((short)h);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)h,(short)(((lblCol==null?"":lblCol).length()+10) * 256));
        }

        //Ahora colocamos toda la data de la hoja de calculo
        short anchoColumna = 0;
        short anchoData = 0;
        for (int h=0;h<lstData.size();h++) {
            //creamos una nueva fila
            filaXLS = hojaXLS.createRow((z++));
            //obtenemos cada una de las filas
            String fila[] = (String[])lstData.get(h);
            for(int k=0 ; k<fila.length ; k++){
                celdaXLS = filaXLS.createCell((short)k);
                celdaXLS.setCellStyle(estiloDat);
                celdaXLS.setCellValue(fila[k]);
                anchoColumna = hojaXLS.getColumnWidth((short)k);
                anchoData = (short)(((fila[k]==null?"":fila[k]).length() + 10) * 256);
                if (anchoData > anchoColumna){
                    hojaXLS.setColumnWidth((short)k, anchoData);
                }
            }
        }
        return libroXLS;
    }

    
    public static HSSFWorkbook crearExcelLetras(String titConsulta, ArrayList lblColumnas, ArrayList lstData, String [] cabecera, String tituloReporte) {
        HSSFWorkbook libroXLS = null;
        HSSFSheet hojaXLS = null;
        HSSFRow filaXLS = null;
        HSSFCell celdaXLS = null;
        //estilo para el titulo
        HSSFCellStyle estiloTit = null;
        HSSFFont fuenteTit = null;
        //estilo para la data
        HSSFCellStyle estiloDat = null;
        HSSFFont fuenteDat = null;

        HSSFCellStyle estiloDatCent = null;

        HSSFCellStyle estiloTitDet = null;
        HSSFFont fuenteTitDet = null;

        libroXLS = new HSSFWorkbook();

        //Estilos para la cabecera de la hoja de calculo
        estiloTit = libroXLS.createCellStyle();
        estiloDat = libroXLS.createCellStyle();
        estiloDatCent = libroXLS.createCellStyle();
        estiloTitDet = libroXLS.createCellStyle();

        fuenteTit = libroXLS.createFont();
        fuenteDat = libroXLS.createFont();
        fuenteTit.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //En negrita
        fuenteTit.setFontHeight((short)160);
        fuenteTit.setFontName("Arial");
        fuenteTit.setColor(HSSFColor.WHITE.index);//jmoreno 28-08-09

        fuenteDat.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); //Sin negrita
        fuenteDat.setFontHeight((short)160);
        fuenteDat.setFontName("Arial");        

        estiloTit.setFont(fuenteTit);
        estiloTit.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTit.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTit.setFillForegroundColor(HSSFColor.BLUE.index);//jmoreno 28-08-09
        estiloTit.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        fuenteTitDet = libroXLS.createFont();
        fuenteTitDet.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //En negrita
        fuenteTitDet.setFontHeight((short)160);
        fuenteTitDet.setFontName("Arial");
        estiloTitDet.setFont(fuenteTitDet);
        estiloTitDet.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTitDet.setFillForegroundColor(HSSFColor.YELLOW.index);
        estiloTitDet.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        estiloDat.setFont(fuenteDat);
        estiloDat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderTop(HSSFCellStyle.BORDER_THIN);

        estiloDatCent.setFont(fuenteDat);
        estiloDatCent.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        hojaXLS = libroXLS.createSheet(titConsulta);

        //Creamos la cabecera de la hoja de calculo
        int z = 0;

        String lblCol = null;

        if(tituloReporte!=null){ //jwong 16/08/2009 para incluir el titulo del reporte
            filaXLS = hojaXLS.createRow(z++);
            lblCol = tituloReporte;
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            filaXLS = hojaXLS.createRow(z++);
        }

        if(cabecera!=null){ 


            
            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Numero Cuenta:";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            
            lblCol = cabecera[0];
            celdaXLS = filaXLS.createCell((short)1);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)1,(short)((lblCol.length()+10) * 256));
            
            
            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Ruc:" ;
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            
            lblCol = cabecera[1];
            celdaXLS = filaXLS.createCell((short)1);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)1,(short)((lblCol.length()+10) * 256));
            
            filaXLS = hojaXLS.createRow(z++);            
           
            
                        
        }

        
        filaXLS = hojaXLS.createRow(z++);
        lblCol = "DETALLE LETRA";
        celdaXLS = filaXLS.createCell((short)0);
        celdaXLS.setCellStyle(estiloTit);
        celdaXLS.setCellValue(lblCol);
        hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
        filaXLS = hojaXLS.createRow(z++);

        filaXLS = hojaXLS.createRow(z++);
        //creamos las columnas del reporte
        for(int h=0 ; h<lblColumnas.size() ; h++){
            lblCol = (String)lblColumnas.get(h);
            celdaXLS = filaXLS.createCell((short)h);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)h,(short)(((lblCol==null?"":lblCol).length()+10) * 256));
        }

        //Ahora colocamos toda la data de la hoja de calculo
        short anchoColumna = 0;
        short anchoData = 0;
        for (int h=0;h<lstData.size();h++) {
            //creamos una nueva fila
            filaXLS = hojaXLS.createRow((z++));
            //obtenemos cada una de las filas
            String fila[] = (String[])lstData.get(h);
            for(int k=0 ; k<fila.length ; k++){
                celdaXLS = filaXLS.createCell((short)k);
                celdaXLS.setCellStyle(estiloDat);
                celdaXLS.setCellValue(fila[k]);
                anchoColumna = hojaXLS.getColumnWidth((short)k);
                anchoData = (short)(((fila[k]==null?"":fila[k]).length() + 10) * 256);
                if (anchoData > anchoColumna){
                    hojaXLS.setColumnWidth((short)k, anchoData);
                }
            }
        }
        return libroXLS;
    }
    
    public static HSSFWorkbook crearExcelRelacionesBco(String titConsulta, BeanConsRelBanco beanConsRelBco, ArrayList listaRelaciones, String tituloReporte) {
        HSSFWorkbook libroXLS = null;
        HSSFSheet hojaXLS = null;
        HSSFRow filaXLS = null;
        HSSFCell celdaXLS = null;
        //estilo para el titulo
        HSSFCellStyle estiloTit = null;
        HSSFFont fuenteTit = null;
        //estilo para la data
        HSSFCellStyle estiloDat = null;
        HSSFFont fuenteDat = null;

        HSSFCellStyle estiloDatCent = null;

        HSSFCellStyle estiloTitDet = null;
        HSSFFont fuenteTitDet = null;

        libroXLS = new HSSFWorkbook();

        //Estilos para la cabecera de la hoja de calculo
        estiloTit = libroXLS.createCellStyle();
        estiloDat = libroXLS.createCellStyle();
        estiloDatCent = libroXLS.createCellStyle();
        estiloTitDet = libroXLS.createCellStyle();

        fuenteTit = libroXLS.createFont();
        fuenteDat = libroXLS.createFont();
        fuenteTit.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //En negrita
        fuenteTit.setFontHeight((short)160);
        fuenteTit.setFontName("Arial");
        fuenteTit.setColor(HSSFColor.WHITE.index);//jmoreno 28-08-09

        fuenteDat.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); //Sin negrita
        fuenteDat.setFontHeight((short)160);
        fuenteDat.setFontName("Arial");

        estiloTit.setFont(fuenteTit);
        estiloTit.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloTit.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTit.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTit.setFillForegroundColor(HSSFColor.BLUE.index);//jmoreno 28-08-09
        estiloTit.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        fuenteTitDet = libroXLS.createFont();
        fuenteTitDet.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //En negrita
        fuenteTitDet.setFontHeight((short)160);
        fuenteTitDet.setFontName("Arial");
        estiloTitDet.setFont(fuenteTitDet);
        estiloTitDet.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloTitDet.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTitDet.setFillForegroundColor(HSSFColor.YELLOW.index);
        estiloTitDet.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        estiloDat.setFont(fuenteDat);
        estiloDat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloDat.setBorderTop(HSSFCellStyle.BORDER_THIN);

        estiloDatCent.setFont(fuenteDat);
        estiloDatCent.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloDatCent.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        hojaXLS = libroXLS.createSheet(titConsulta);

        //Creamos la cabecera de la hoja de calculo
        int z = 0;

        String lblCol = null;

        if(tituloReporte!=null){ //jwong 16/08/2009 para incluir el titulo del reporte
            filaXLS = hojaXLS.createRow(z++);
            lblCol = tituloReporte;
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            filaXLS = hojaXLS.createRow(z++);
        }
        
        if(beanConsRelBco!=null){
            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Sectorista";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));

            lblCol = "Funcionario Cash";
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)((lblCol.length()+10) * 256));
            

            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Nombre:";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            lblCol = beanConsRelBco.getM_NameSec();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)1);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)1,(short)((lblCol.length()+10) * 256));

            lblCol = "Nombre:";
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)((lblCol.length()+10) * 256));
            lblCol = beanConsRelBco.getM_NameFunc();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)3);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)3,(short)((lblCol.length()+10) * 256));

            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Teléfono:";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            lblCol = beanConsRelBco.getM_PhoneSec();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)1);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)1,(short)((lblCol.length()+10) * 256));

            lblCol = "Teléfono:";
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)((lblCol.length()+10) * 256));
            lblCol = beanConsRelBco.getM_PhoneFunc();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)3);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)3,(short)((lblCol.length()+10) * 256));

            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Email:";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
            lblCol = beanConsRelBco.getM_EmailSec();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)1);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)1,(short)((lblCol.length()+10) * 256));

            lblCol = "Email:";
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloTitDet);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)((lblCol.length()+10) * 256));
            lblCol = beanConsRelBco.getM_EmailFunc();
            lblCol = lblCol==null?"":lblCol;
            celdaXLS = filaXLS.createCell((short)3);
            celdaXLS.setCellStyle(estiloDat);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)3,(short)((lblCol.length()+10) * 256));
        }
        
        filaXLS = hojaXLS.createRow(z++);
        lblCol = "DETALLE DE RELACIONES CON EL BANCO";
        celdaXLS = filaXLS.createCell((short)0);
        celdaXLS.setCellStyle(estiloTit);
        celdaXLS.setCellValue(lblCol);
        hojaXLS.setColumnWidth((short)0,(short)((lblCol.length()+10) * 256));
        
        BeanTypeAccount beanType = null;
        BeanConsultaSaldosXML beanaccount = null;
        //colocamos la data en la hoja de calculo
        for(int i=0 ; i<listaRelaciones.size() ; i++){
            filaXLS = hojaXLS.createRow(z++);
            beanType = (BeanTypeAccount)listaRelaciones.get(i);

            lblCol = (String)beanType.getM_Description();
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)(((lblCol==null?"":lblCol).length()+10) * 256));
            
            
            filaXLS = hojaXLS.createRow(z++);
            lblCol = "Cuenta";
            celdaXLS = filaXLS.createCell((short)0);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)0,(short)(((lblCol==null?"":lblCol).length()+10) * 256));

            lblCol = "Moneda";
            celdaXLS = filaXLS.createCell((short)1);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)1,(short)(((lblCol==null?"":lblCol).length()+10) * 256));

            lblCol = "Saldo Disponible";
            celdaXLS = filaXLS.createCell((short)2);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)2,(short)(((lblCol==null?"":lblCol).length()+10) * 256));

            lblCol = "Saldo Contable";
            celdaXLS = filaXLS.createCell((short)3);
            celdaXLS.setCellStyle(estiloTit);
            celdaXLS.setCellValue(lblCol);
            hojaXLS.setColumnWidth((short)3,(short)(((lblCol==null?"":lblCol).length()+10) * 256));
            
            for(int j=0 ; j<beanType.getM_Accounts().size() ; j++){
                beanaccount = (BeanConsultaSaldosXML)beanType.getM_Accounts().get(j);

                filaXLS = hojaXLS.createRow(z++);
                lblCol = beanaccount.getM_Cuenta();
                lblCol = lblCol==null?"":lblCol;
                celdaXLS = filaXLS.createCell((short)0);
                celdaXLS.setCellStyle(estiloDatCent);
                celdaXLS.setCellValue(lblCol);
                hojaXLS.setColumnWidth((short)0,(short)(((lblCol==null?"":lblCol).length()+10) * 256));

                lblCol = beanaccount.getM_Moneda();
                lblCol = lblCol==null?"":lblCol;
                celdaXLS = filaXLS.createCell((short)1);
                celdaXLS.setCellStyle(estiloDatCent);
                celdaXLS.setCellValue(lblCol);
                hojaXLS.setColumnWidth((short)1,(short)(((lblCol==null?"":lblCol).length()+10) * 256));

                lblCol = beanaccount.getM_SaldoDisponible();
                lblCol = lblCol==null?"":lblCol;
                celdaXLS = filaXLS.createCell((short)2);
                celdaXLS.setCellStyle(estiloDatCent);
                celdaXLS.setCellValue(lblCol);
                hojaXLS.setColumnWidth((short)2,(short)(((lblCol==null?"":lblCol).length()+10) * 256));

                lblCol = beanaccount.getM_SaldoContable();
                lblCol = lblCol==null?"":lblCol;
                celdaXLS = filaXLS.createCell((short)3);
                celdaXLS.setCellStyle(estiloDatCent);
                celdaXLS.setCellValue(lblCol);
                hojaXLS.setColumnWidth((short)3,(short)(((lblCol==null?"":lblCol).length()+10) * 256));
            }
        }
        return libroXLS;
    }

	/**
	 * ¨ Crea un excel con una cantidad maximo de filas por hoja
	 * 
	 * @param titConsulta
	 * @param lblColumnas
	 * @param lstData
	 * @param tituloReporte
	 * @return
	 */    
	@SuppressWarnings("rawtypes")
	public static HSSFWorkbook crearExcel(String titConsulta, List lblColumnas,
			List lstData, String tituloReporte,boolean agregarMensaje) {
		HSSFWorkbook libroXLS = new HSSFWorkbook();
		int tamañoResultados = lstData.size();
		int numeroPaginas = Util.dividirConRedondeoHaciaArriba(
				tamañoResultados, Constantes.MAXIMA_CANTIDAS_FILAS_EXCEL);
		List listaPaginada = null;
		for (int numeroPagina = 1; numeroPagina <= numeroPaginas; numeroPagina++) {
			int inicioPagina = (numeroPagina - 1)
					* Constantes.MAXIMA_CANTIDAS_FILAS_EXCEL;
			int finPagina = inicioPagina
					+ Constantes.MAXIMA_CANTIDAS_FILAS_EXCEL;
			if (finPagina > tamañoResultados) {
				finPagina = tamañoResultados;
			}
			listaPaginada = lstData.subList(inicioPagina, finPagina);
			crearSheetExcel(titConsulta+numeroPagina, lblColumnas, listaPaginada,
					tituloReporte, libroXLS, agregarMensaje);
		}		
		return libroXLS;
	}
	
	public static HSSFSheet crearSheetExcel(String titConsulta, List lblColumnas,
			List lstData, String tituloReporte, HSSFWorkbook workbook,boolean  agregarMensaje) {
		HSSFCellStyle estiloTit = crearEstiloTitulo(workbook);		
		HSSFCellStyle estiloDat = crearEstiloDatos(workbook);		
		HSSFSheet hojaXLS = workbook.createSheet(titConsulta);
		int z = 0;
		String lblCol = null;
		HSSFRow filaXLS = null;
		HSSFCell celdaXLS = null;
		if (tituloReporte != null) {
			filaXLS = hojaXLS.createRow(z++);
			lblCol = tituloReporte;
			celdaXLS = filaXLS.createCell((short) 0);
			celdaXLS.setCellStyle(estiloTit);
			celdaXLS.setCellValue(lblCol);
			hojaXLS.setColumnWidth((short) 0,
					(short) ((lblCol.length() + 10) * 256));
			filaXLS = hojaXLS.createRow(z++);
		}
		if(agregarMensaje){
			filaXLS = hojaXLS.createRow(z++);
			lblCol = "Se muestran los primeros 10000 registros. Si requiere la informacion completa comunicarse con Soporte Cash";
			celdaXLS = filaXLS.createCell((short) 0);
			celdaXLS.setCellStyle(estiloDat);
			celdaXLS.setCellValue(lblCol);
			hojaXLS.setColumnWidth((short) 0,
					(short) ((lblCol.length() + 10) * 256));
			filaXLS = hojaXLS.createRow(z++);
		}
		filaXLS = hojaXLS.createRow(z++);
		String aux[] = null;
		// creamos las columnas del reporte
		for (int h = 0; h < lblColumnas.size(); h++) {
			lblCol = (String) lblColumnas.get(h);
			aux = lblCol.split(",");
			celdaXLS = filaXLS.createCell((short) h);
			celdaXLS.setCellStyle(estiloTit);
			celdaXLS.setCellValue(aux[0]);
			hojaXLS.setColumnWidth((short) h, (short) (((aux[0] == null ? ""
					: aux[0]).length() + 10) * 256));
		}

		// Ahora colocamos toda la data de la hoja de calculo
		short anchoColumna = 0;
		short anchoData = 0;
		List lfila = null;
		String valor = "";
		for (int h = 0; h < lstData.size(); h++) {
			filaXLS = hojaXLS.createRow((z++));
			lfila = (List) lstData.get(h);
			for (int k = 0; k < lfila.size(); k++) {
				valor = (String) lfila.get(k);
				celdaXLS = filaXLS.createCell((short) k);
				celdaXLS.setCellStyle(estiloDat);
				celdaXLS.setCellValue(valor);
				anchoColumna = hojaXLS.getColumnWidth((short) k);
				anchoData = (short) (((valor == null ? "" : valor).length() + 10) * 256);
				if (anchoData > anchoColumna) {
					hojaXLS.setColumnWidth((short) k, anchoData);
				}
			}
		}
		return hojaXLS;
	}
	
	private static HSSFCellStyle crearEstiloTitulo(HSSFWorkbook workbook){
		HSSFFont fuenteTit = workbook.createFont();
		fuenteTit.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fuenteTit.setFontHeight((short) 160);
		fuenteTit.setFontName("Arial");
		fuenteTit.setColor(HSSFColor.WHITE.index);
		HSSFCellStyle estiloTit = workbook.createCellStyle();
		estiloTit.setFont(fuenteTit);
		estiloTit.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloTit.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		estiloTit.setBorderRight(HSSFCellStyle.BORDER_THIN);
		estiloTit.setBorderTop(HSSFCellStyle.BORDER_THIN);
		estiloTit.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloTit.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloTit.setFillForegroundColor(HSSFColor.BLUE.index);
		estiloTit.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return estiloTit;
	}
	
	private static HSSFCellStyle crearEstiloDatos(HSSFWorkbook workbook){
		HSSFFont fuenteDat = workbook.createFont();		
		fuenteDat.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		fuenteDat.setFontHeight((short) 160);
		fuenteDat.setFontName("Arial");			
		HSSFCellStyle estiloDat = workbook.createCellStyle();
		estiloDat.setFont(fuenteDat);
		estiloDat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloDat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		estiloDat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		estiloDat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return estiloDat;
	}

}