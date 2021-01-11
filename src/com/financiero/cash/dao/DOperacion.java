/*
 * To change this template, choose Tools | Templates selectListaBusqDocumento
 * and open the template in the editor.
 */
package com.financiero.cash.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sistemas
 */
public class DOperacion extends DBase {

    public DOperacion() {
        super();
    }

    public ArrayList getEmpresas(String idtarjeta) throws SQLException {

        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        try {
            conn = DriverManager.getConnection(url, usuario, password);
            //pstmt = conn.prepareStatement("SELECT distinct Id_Empresa, Nombre FROM WEB_User_Empresas_Servicios_VIEW  WHERE Usuario = ? Order By Nombre asc");
            
            pstmt = conn.prepareStatement("SELECT DISTINCT Empresas.Id_Empresa as Id_Empresa, Empresas.Nombre as Nombre " +
            		"FROM Empresas  " +
            		"INNER JOIN EasySeguridad.dbo.UserEmpServ ON Empresas.ID_Empresa = EasySeguridad.dbo.UserEmpServ.ID_Empresa " +
            		" Inner Join Servicios ON EasySeguridad.dbo.UserEmpServ.Id_Servicio = Servicios.Id_Servicio " +
            		" Inner Join EasySeguridad.dbo.Usuarios ON EasySeguridad.dbo.Usuarios.NombreCorto = EasySeguridad.dbo.UserEmpServ.Usuario " +
            		" Inner Join Contratos ON (Contratos.Id_Empresa = EasySeguridad.dbo.UserEmpServ.Id_Empresa AND Contratos.Id_Servicio = EasySeguridad.dbo.UserEmpServ.Id_Servicio) " +
            		" WHERE (EasySeguridad.dbo.Usuarios.Tarjeta = ? )  " +
            		"AND (EasySeguridad.dbo.UserEmpServ.Estado = '1') " +
            		//"AND (Contratos.Estado = 'A') " +
            		"order by 2");
            
            pstmt.setString(1, idtarjeta);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map item = new HashMap();
                item.put("idempresa", rs.getString("Id_Empresa"));
                item.put("nombres", rs.getString("Nombre"));
                lista.add(item);
            }

        } catch (SQLException sqle) {
            throw (sqle);
        } finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
            return lista;
        }

    }

    public ArrayList getServiciosxEmpresas(String id) throws SQLException {

        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        try {
            conn = DriverManager.getConnection(url, usuario, password);            

            pstmt = conn.prepareStatement("SELECT   DISTINCT  dbo.Contratos.Id_Servicio,dbo.Servicios.Descripcion_Servicio " +
            		"FROM dbo.Empresas " +
            		"INNER JOIN EasySeguridad.dbo.UserEmpServ ON dbo.Empresas.Id_Empresa = EasySeguridad.dbo.UserEmpServ.ID_Empresa " +
            		"INNER JOIN dbo.Servicios ON EasySeguridad.dbo.UserEmpServ.ID_Servicio = dbo.Servicios.Id_Servicio " +
            		"INNER JOIN dbo.Contratos ON dbo.Contratos.Id_Empresa = dbo.Empresas.Id_Empresa AND dbo.Contratos.Id_Servicio = dbo.Servicios.Id_Servicio " +
            		"INNER JOIN EasySeguridad.dbo.Usuarios ON EasySeguridad.dbo.UserEmpServ.Usuario = EasySeguridad.dbo.Usuarios.NombreCorto " +
            		"WHERE   dbo.Contratos.Id_Empresa=? " +
            		"Order By Descripcion_Servicio asc");            
                      
             


            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map item = new HashMap();
                item.put("idservicio", rs.getString("Id_Servicio"));
                item.put("servicio", rs.getString("Descripcion_Servicio"));
                lista.add(item);
            }

        } catch (SQLException sqle) {
            throw (sqle);
        } finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
            return lista;
        }

    }

    public ArrayList getOrdenes(String idEmpresa, String idServicio, String fecha1, String fecha2) throws SQLException {

        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        //fecha1="01/01/2006";
        //fecha2="01/12/2010";

        try {
            conn = DriverManager.getConnection(url, usuario, password);
            pstmt = conn.prepareStatement("select * "
                    + " FROM Consulta_Sobres_VIEW  "
                    + " WHERE Id_Empresa in (?)  "
                    + " AND Id_Servicio = ? "
                    + " AND Fecha_Inicio_Proceso BETWEEN convert(datetime, ?, 103) "
                    + " AND (convert(datetime, ?, 103)+1) "
                    + " order by Fecha_Creacion_Date");
            

 
            pstmt.setString(1, idEmpresa);
            pstmt.setString(2, idServicio);
            pstmt.setString(3, fecha1);
            pstmt.setString(4, fecha2);
            rs = pstmt.executeQuery();

            ////System.out.println(pstmt);

            while (rs.next()) {
                Map item = new HashMap();
                item.put("Id_Sobre", rs.getString("Id_Sobre"));
                item.put("Nombre", rs.getString("Nombre"));
                item.put("Descripcion_Servicio", rs.getString("Descripcion_Servicio"));
                item.put("Referencia", rs.getString("Referencia"));
                item.put("Numero_Items", rs.getString("Numero_Items"));
                item.put("Fecha_Creacion", rs.getString("Fecha_Creacion"));
                item.put("Valor_Sobre", rs.getString("Valor_Sobre"));
                item.put("Estado_Sobre", rs.getString("Estado_Sobre"));
                item.put("Cuenta", rs.getString("Cuenta"));
                item.put("Inicio", rs.getString("Inicio"));
                item.put("Vencimiento", rs.getString("Vencimiento"));
                item.put("Codigo_Moneda", rs.getString("Codigo_Moneda"));
                item.put("MensajeProceso", rs.getString("Estado_Sobre"));
                lista.add(item);
            }

        } catch (SQLException sqle) {
            throw (sqle);
        } finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
            return lista;
        }

    }

    public ArrayList getItems(int idOrden) throws SQLException {

        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        try {
            conn = DriverManager.getConnection(url, usuario, password);
            pstmt = conn.prepareStatement("select Id_Item, Pais,Banco,FormaPago,Tipo_Cuenta +'|'+ Numero_Cuenta as Cuenta,ContraPartida,Referencia,CONVERT(varchar,Valor,1) as Valor,Moneda,MensajeProceso,NombreContrapartida,Referencia_adicional, secuencial_Cobro,Id_sobre,Fecha_Proceso,Saldo " +
            		"FROM Consulta_Items_Bancos_VIEW  " +
            		"WHERE Id_Sobre = ? " +
            		"ORDER BY Id_Item");
            
            pstmt.setInt(1, idOrden);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map item = new HashMap();
                item.put("Id_Item", rs.getString("Id_Item"));
                item.put("Pais", rs.getString("Pais"));
                item.put("Banco", rs.getString("Banco"));
                item.put("Cuenta", rs.getString("Cuenta"));
                item.put("ContraPartida", rs.getString("ContraPartida"));
                item.put("Referencia", rs.getString("Referencia"));
                item.put("Valor", rs.getString("Valor"));
                item.put("Moneda", rs.getString("Moneda"));
                item.put("Fecha_Proceso", rs.getString("Fecha_Proceso"));
                item.put("MensajeProceso", rs.getString("MensajeProceso"));
                lista.add(item);
            }

        } catch (SQLException sqle) {
            throw (sqle);
        } finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
            return lista;
        }

    }

    public ArrayList getResumenItems(int idDetalle) throws SQLException {

        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        try {
            conn = DriverManager.getConnection(url, usuario, password);
            pstmt = conn.prepareStatement("select Id_Item, Pais,Banco,FormaPago,Tipo_Cuenta +'|'+ Numero_Cuenta as Cuenta,ContraPartida,Referencia,CONVERT(varchar,Valor,1) as Valor,Moneda,MensajeProceso,NombreContrapartida,Referencia_adicional, secuencial_Cobro,Id_sobre,Fecha_Proceso,Saldo " +
            		"FROM Consulta_Items_Bancos_VIEW  " +
            		"WHERE Id_Sobre = ? ORDER BY Id_Item");
            pstmt.setInt(1, idDetalle);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map item = new HashMap();
                item.put("Id_Item", rs.getString("Id_Item"));
                item.put("Pais", rs.getString("Pais"));
                item.put("Banco", rs.getString("Banco"));
                item.put("Cuenta", rs.getString("Cuenta"));
                item.put("ContraPartida", rs.getString("ContraPartida"));
                item.put("Referencia", rs.getString("Referencia"));
                item.put("Valor", rs.getString("Valor"));
                item.put("Moneda", rs.getString("Moneda"));
                item.put("Fecha_Proceso", rs.getString("Fecha_Proceso"));
                item.put("MensajeProceso", rs.getString("MensajeProceso"));
                lista.add(item);
            }

        } catch (SQLException sqle) {
            throw (sqle);
        } finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
            return lista;
        }

    }
}
