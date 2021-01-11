package com.hiper.cash.servlets;

import static com.hiper.cash.util.CashConstants.NOMBRE_REPORTE_CONSULTA_TRANSFERENCIAS;
import static com.hiper.cash.util.CashConstants.NOMBRE_REPORTE_DETALLE_TRANSFERENCIA;
import static com.hiper.cash.util.CashConstants.STR_DD_MM_YYYY;
import static com.hiper.cash.util.CashConstants.STR_YYYYMMDD;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.financiero.cash.beans.TransferenciaBean;
import com.financiero.cash.delegate.TransferenciasDelegate;
import com.financiero.cash.util.TipoTransferencia;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.util.Util;

public class JasperReportServlet extends HttpServlet {
	private static final long serialVersionUID = -8226772480674311636L;
	TransferenciasDelegate transferenciasDelegate = TransferenciasDelegate.getInstance();
	TmEmpresaDao empresaDao = new TmEmpresaDaoHibernate();

	@Override
	public void init() throws ServletException {
		super.init();

	}

	private static final Logger log = Logger.getLogger(JasperReportServlet.class);


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JasperReportServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String reporttype = request.getParameter("reportType");
		String nombreReporte = request.getParameter("reportName");
		if (log.isDebugEnabled()) {
			log.debug("NombreReporte:  " + nombreReporte);
			log.debug("TipoReporte:  " + reporttype);
		}
		if(StringUtils.isBlank(reporttype)||StringUtils.isBlank(nombreReporte)){
			throw new RuntimeException("Faltan parametros para el reporte");
		}		
		Locale locale = new Locale("en", "US");
		ServletContext context = this.getServletConfig().getServletContext();
		String path = context.getRealPath("/jasper");
		JasperPrint jasperPrint = null;
		Map parameters = null;
		try {
			if (NOMBRE_REPORTE_DETALLE_TRANSFERENCIA.equals(nombreReporte)) {
				String idTrx = request.getParameter("idTrx");
				String idEmpresa = request.getParameter("idEmpresa");
				TransferenciaBean transferencia = transferenciasDelegate.getTransferencia(idTrx, idEmpresa);
				String nombreEmpresa = null;
				TmEmpresa empresa = empresaDao.selectEmpresaByCode(idEmpresa);
				if (empresa != null) {
					nombreEmpresa = empresa.getDemNombre().toUpperCase();
				}
				parameters = obtenerParametrosReporteDetalleTransferencia(locale, context, transferencia,
						nombreEmpresa);
				String plantillaJasper = NOMBRE_REPORTE_DETALLE_TRANSFERENCIA;
				if (transferencia.getTipo() == TipoTransferencia.CP) {
					plantillaJasper += "CP";
				}
				if (transferencia.getTipo() == TipoTransferencia.CT) {
					plantillaJasper += "CT";
				}
				if (transferencia.getTipo() == TipoTransferencia.IT) {
					plantillaJasper += "IT";
				}
				jasperPrint = JasperFillManager.fillReport(path + "/" + plantillaJasper + ".jasper",
						parameters, new JRBeanCollectionDataSource(transferencia.getAcciones()));
			}
			if (NOMBRE_REPORTE_CONSULTA_TRANSFERENCIAS.equals(nombreReporte)) {
				String idEmpresa = request.getParameter("idEmpresa");
				String tipoTransferencia = request.getParameter("tipoTransferencia");
				String tipoDocumento = request.getParameter("tipoDocumento");
				String nroDocumento = request.getParameter("nroDocumento");
				String estadoTransferencia = request.getParameter("estadoTransferencia");
				String codigoMoneda = request.getParameter("codigoMoneda");
				String fechaIni = request.getParameter("fechaIni");
				if(StringUtils.isNotBlank(fechaIni)){
					Date dateFechaFinal = Util.obtenerFecha(fechaIni, STR_DD_MM_YYYY);
					SimpleDateFormat formatter = new SimpleDateFormat(STR_YYYYMMDD);
					fechaIni = formatter.format(dateFechaFinal);
				}				
				String fechaFin = request.getParameter("fechaFin");
				if(StringUtils.isNotBlank(fechaFin)){
					Date dateFechaFinal = Util.obtenerFecha(fechaFin, STR_DD_MM_YYYY);
					SimpleDateFormat formatter = new SimpleDateFormat(STR_YYYYMMDD);
					fechaFin = formatter.format(dateFechaFinal);
				}
				List<TransferenciaBean> transferencias = transferenciasDelegate.buscarTransferencias(
						idEmpresa, tipoTransferencia, tipoDocumento, nroDocumento, estadoTransferencia,
						codigoMoneda, fechaIni, fechaFin);
				parameters = new HashMap();
				parameters.put(JRParameter.REPORT_LOCALE, locale);
				jasperPrint = JasperFillManager.fillReport(path + "/"
						+ NOMBRE_REPORTE_CONSULTA_TRANSFERENCIAS + ".jasper", parameters,
						new JRBeanCollectionDataSource(transferencias));
			}
		} catch (JRException e1) {
			log.error("error en jasper", e1);
			throw new RuntimeException("error en jasper", e1);
		}
		if (log.isDebugEnabled()) {
			log.debug("Report Created... in " + reporttype + " Format");
		}
		OutputStream ouputStream = response.getOutputStream();
		JRExporter exporter = null;
		if ("pdf".equalsIgnoreCase(reporttype)) {
			exporter = exportarPdf(response, jasperPrint, ouputStream);

		}		
		else if ("xls".equalsIgnoreCase(reporttype)) {
			exporter = exportarExcel(response, jasperPrint, ouputStream);
		}			
		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new ServletException(e);
		} finally {
			if (ouputStream != null) {
				try {
					ouputStream.close();
				} catch (IOException ex) {
					log.error("error generando reporte", ex);
				}
			}
		}
	}

	private Map obtenerParametrosReporteDetalleTransferencia(Locale locale, ServletContext context,
			TransferenciaBean transferencia, String nombreEmpresa) throws MalformedURLException {
		Map parameters;
		parameters = new HashMap();
		parameters.put(JRParameter.REPORT_LOCALE, locale);
		parameters.put("numero", transferencia.getNumero());
		parameters.put("nombreEmpresa", nombreEmpresa);
		parameters.put("estado", transferencia.getEstado().getNombre());
		parameters.put("fecha", transferencia.getFechaRegistro());
		parameters.put("referencia", transferencia.getReferencia());
		parameters.put("usuarioRegistro", transferencia.getNombreUsuarioRegistro());
		parameters.put("descripcionCuentaCargo", transferencia.getDescripcionCuentaCargo());
		parameters.put("cuentaCargoFormateado", transferencia.getCuentaCargoFormateado());
		parameters.put("descripcionCuentaAbono", transferencia.getDescripcionCuentaAbono());
		parameters.put("cuentaAbonoFormateado", transferencia.getCuentaAbonoFormateado());
		parameters.put("moneda", transferencia.getMoneda());
		parameters.put("monto", transferencia.getMonto());
		parameters.put(
				"titular",
				StringUtils.trimToEmpty(transferencia.getApellidoPaterno()) + " "
						+ StringUtils.trimToEmpty(transferencia.getApellidoMaterno()) + " "
						+ StringUtils.trimToEmpty(transferencia.getNombres()));
		parameters.put("idTitular",
				transferencia.getDocumento() + " " + transferencia.getNroDocumento());
		parameters.put("cuentaAbono", transferencia.getCuentaAbono());
		parameters.put("banco", transferencia.getNombreBanco());
		parameters.put("mismo", transferencia.getLabelMismo());
		parameters.put("direccion", transferencia.getDireccion());
		parameters.put("simboloMonedaAbono", transferencia.getSimboloMonedaAbono());
		parameters.put("montoAbonado", transferencia.getMontoAbonado());
		parameters.put("IMAGE_DIR", context.getResource("/img/reportes/").toString());
		return parameters;
	}

	private JRExporter exportarExcel(HttpServletResponse response, JasperPrint jasperPrint,
			OutputStream ouputStream) {
		JRExporter exporter;
		response.setContentType("application/xls");
		response.setHeader("Content-Disposition", "inline; filename=\"report.xls\"");
		exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		return exporter;
	}

	private JRExporter exportarPdf(HttpServletResponse response, JasperPrint jasperPrint,
			OutputStream ouputStream) {
		JRExporter exporter;
		response.reset();
		response.setContentType("application/pdf");		
		response.setHeader("Content-Disposition", "inline; filename=\"report.pdf\"");
		exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		return exporter;
	}

}
