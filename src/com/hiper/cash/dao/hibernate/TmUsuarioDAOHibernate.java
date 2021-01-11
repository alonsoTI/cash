package com.hiper.cash.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;

import com.financiero.cash.dao.EntityDAO;
import com.hiper.cash.dao.TmUsuarioDAO;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TmUsuario;
import com.hiper.cash.util.Constantes;

public class TmUsuarioDAOHibernate implements TmUsuarioDAO {

	
	public TmUsuario buscarId(String codigo) throws SQLException {
		try{
			Session s = HibernateUtil.getSeguridadSessionFactory().openSession();
			return EntityDAO.findById(s, TmUsuario.class, codigo);		
			
		}catch(Exception e){
			throw new SQLException(e);
		}
	}

	@Override
	public List<TmUsuario> buscarTodos() {
		List<TmUsuario> resultados = null;
		Session session = null;
		try {
			session = HibernateUtil.getSeguridadSessionFactory().openSession();
			resultados = (List<TmUsuario>) session.createQuery("from TmUsuario").list();
		} catch (Exception e) {
			throw new RuntimeException(Constantes.MENSAJE_ERROR_CONEXION_HIBERNATE, e);
		} finally {
			session.close();
		}
		return resultados;
	}
	
	

}
