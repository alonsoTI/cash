/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.clienteWS;

import com.hiper.cash.xml.bean.BeanNodoXML;
import java.util.List;

/**
 *
 * @author jwong
 */
public class GenRequestXML {
    public static String getXML(List nodos){
        /*String xml = "<data><![CDATA[" + "<" + "]]>request>";
        BeanNodoXML beanNodo = null;
        for(int i=0 ; i < nodos.size() ; i++){
            beanNodo = (BeanNodoXML)nodos.get(i);
            xml = xml +
                    "<![CDATA[" + "<" + "]]>" + beanNodo.getM_Nodo() + ">" +
                        beanNodo.getM_Valor() +
                    "<![CDATA[" + "<" + "]]>/" + beanNodo.getM_Nodo() + ">";
        }
        xml = xml + "<![CDATA[" + "<" + "]]>/request></data>";
        return xml;*/
        String xml = "<data><request>";
        BeanNodoXML beanNodo = null;
        for(int i=0 ; i < nodos.size() ; i++){
            beanNodo = (BeanNodoXML)nodos.get(i);
            xml = xml +
                    "<" + beanNodo.getM_Nodo() + ">" +
                        beanNodo.getM_Valor() +
                    "</" + beanNodo.getM_Nodo() + ">";
        }
        xml = xml + "</request></data>";
        return xml;
    }
    
    public static String getXMLLogin(List nodos){
        String xml = "";
        BeanNodoXML beanNodo = null;
        for(int i=0 ; i < nodos.size() ; i++){
            beanNodo = (BeanNodoXML)nodos.get(i);
            xml = xml +
                    "<" + beanNodo.getM_Nodo() + ">" +
                        beanNodo.getM_Valor() +
                    "</" + beanNodo.getM_Nodo() + ">";
            
        }
        return xml;
    }
}