package com.hiper.cash.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hiper.cash.dao.TxDisponibilidadDAO;
import com.hiper.cash.domain.TxDisponibilidad;

public class TxDisponibilidadDAOJDBC implements TxDisponibilidadDAO{

	
	public List<TxDisponibilidad> findAll() throws SQLException {
		List<TxDisponibilidad> lista = new ArrayList<TxDisponibilidad>();
		TxDisponibilidad disponibilidad;
		Connection cn=null;
		try{
			cn =  DBManager.getConnectionDBSeguridad();		
			String sql = "select * from TxDisponibilidad";
			PreparedStatement ps = cn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ){
				disponibilidad =new TxDisponibilidad();
				disponibilidad.setId(rs.getString(1));
				disponibilidad.setfInicio(rs.getTime(3));
				disponibilidad.setfFinal(rs.getTime(4));
				lista.add(disponibilidad);
			}
			rs.close();
			ps.close();						
		}catch(SQLException e){
			throw e;	
		}finally{
			cn.close();
		}
		return lista;
	}
	

}
