package com.hiper.cash.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.hiper.cash.dao.TmFormatoDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TmFormato;
import com.hiper.cash.util.Constantes;

public class TmFormatoDaoHibernate implements TmFormatoDao {

	private static Logger logger = Logger
			.getLogger(TmFormatoDaoHibernate.class);

	public TmFormato obtenerFormatoById(int formatoId){
		TmFormato formatoBean=null;
		Session session = null;
		try {			
			session = HibernateUtil.getSessionFactory().openSession();			
			formatoBean =  (TmFormato)session.get(TmFormato.class, formatoId);					
		} catch (Exception e) {			
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_HIBERNATE,e);
		}
		finally{
			session.close();
		}
		return formatoBean;
	}

    @Override
    public List<String> obtenerNombresCampos(int formatoId)
    {
        Session session = null;
        List<String> nombresCampos = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            nombresCampos = session
                    .createQuery("select ddfnombreCampo from TaDetalleFormato where id.cdfidFormato = ? order by id.ndfposicionCampo asc")
                    .setParameter(0, formatoId).list();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error consultando el formato", e);
        }
        finally
        {
            session.close();
        }
        return nombresCampos;
    }

}
