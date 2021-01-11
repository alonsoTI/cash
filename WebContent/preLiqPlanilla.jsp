<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<html>
    <% String codCliente= (String)request.getAttribute("codCliente"); %>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="config/javascript.js"></script>
  <script type="text/javascript" src="js/Functions.js"></script>
  
  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>

  <script language="JavaScript">
    <!--
	
    function buscar(){
        var frm = document.forms[0];
        var fecIni = document.getElementById("m_FecInicio").value;
        var fecFin = document.getElementById("m_FecFin").value;
        if(validarFechasRelacionadas2(fecIni, fecFin)){
            frm.action = "letras.do?do=buscarPlanilla&from=2";
            frm.submit();
        }
    }
    function disallowDateIni(date) {

      var fecMaxima = document.getElementById("m_FechaMaxAnt").value;
      var fecSel = date.print("%Y%m%d")
      if (  fecSel< fecMaxima) {
          return true; // disable
      }
      return false; // enable other dates
    };
    function disallowDateFin(date) {
      var fecMaxima = document.getElementById("m_FechaMaxPost").value;
      var fecSel = date.print("%Y%m%d")
      if (  fecSel > fecMaxima ) {
        return true; 
      }
      return false; 
    };
    function updateDateIni(cal) {
        var field_0 = document.getElementById("m_FecInicio").value;
        var fecIni = field_0.substr(6,4) + field_0.substr(3,2) + field_0.substr(0,2);
        var field_1 = document.getElementById("m_FecFin").value;
        var fecFin = field_1.substr(6,4) + field_1.substr(3,2) + field_1.substr(0,2);

        if(fecFin<fecIni){
            var date = cal.date;
            var field = document.getElementById("m_FecFin");
            field.value = date.print("%d/%m/%Y");
        }
    }

    function updateDateFin(cal) {

        var field_0 = document.getElementById("m_FecInicio").value;
        var fecIni = field_0.substr(6,4) + field_0.substr(3,2) + field_0.substr(0,2);
        var date = cal.date;
        var fecSel = date.print("%Y%m%d");
        if (  fecSel < fecIni ) {
            alert("La fecha Final debe ser mayor a la fecha Inicial");
            var field = document.getElementById("m_FecFin");
            field.value = field_0;
        }
    }
    -->
</script>

</head>
<body>
<html:form action="letras.do" onsubmit="return false;">
  <logic:notEqual name="habil" value="1">
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr valign="baseline">
        <td align="center" class="TitleRowCierraSession" valign="baseline" height="100%">
             <bean:message key="errors.authorization"/>
        </td>
    </tr>
  </table>
  </logic:notEqual>
  <logic:equal name="habil" value="1">
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" colspan="4" class="Title"><bean:message key="letras.title.preliquidacion"/></td>
	</tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center" >
  	<tr>
	  <td class='CellColRow'>Empresa:</td>
	  <td class='CellColRow2'>
        <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="cemIdEmpresa" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
	  </td>

	  <td class='CellColRow2'>
        <html:radio property="m_Tipo" value="G" >Girador</html:radio>
      </td>
	  <td class='CellColRow2'>
        <html:radio property="m_Tipo" value="A" >Aceptante</html:radio>
      </td>
    </tr>
    <tr>
	  <td class='CellColRow'>Desde:</td>
	  <td class='CellColRow2'>
		<html:text property="m_FecInicio" styleId="m_FecInicio" size="10" maxlength="10" styleClass="CellColRow6" readonly="true"/>
		<button id='btn_ini' name='btn_ini'><img src='img/cal.gif' /></button>
		<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_FecInicio",
			  ifFormat : "%d/%m/%Y",
			  button : "btn_ini",
               weekNumbers : false
			}
		  );
		</script>
	  </td>
	  <td class='CellColRow'>Hasta:</td>
	  <td class='CellColRow2'>
		<html:text property="m_FecFin" styleId="m_FecFin" size="10" maxlength="10" styleClass="CellColRow6" readonly="true"/>
		<button id='btn_fin' name='btn_fin'><img src='img/cal.gif' /></button>
		<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_FecFin",
			  ifFormat : "%d/%m/%Y",
			  button : "btn_fin",
              weekNumbers : false
			}
		  );
		</script>
	  </td>
	</tr>

	<tr align="right">
      <td colspan="4" class="CellColRow5">
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
      </td>

    </tr>
  </table>
  <br>
  <logic:notEmpty name="listaResult">
    <html:hidden property="m_ClientCode" styleId="m_ClientCode" name="beanLetras"></html:hidden>
    <layout:collection name="listaResult" title="letras.title.relacion.planilla" styleClass="FORM" id="letra" width="100%" align="center" sortAction="client">
      <layout:collectionItem title="letras.column.codigo.planilla" name="letra" property="m_Cuenta" sortable="true"  >
            <center>
              <layout:link href="letras.do?do=detallePlanillaLetras&from=2" name="letra"  property="parametrosUrl">
                <layout:write name="letra" property="m_Cuenta"/>
              </layout:link>
           </center>
      </layout:collectionItem>      
      <center>
      <layout:collectionItem title="letras.column.tipo" property="m_Tipo" />
      </center>
      <layout:collectionItem title="letras.column.nombre.girador" property="m_NomAcep" sortable="true" />
      <layout:collectionItem title="letras.column.moneda" property="m_Moneda">
               <p align="center">
            <layout:write name="letra" property="m_Moneda" />
            </p>
          </layout:collectionItem>
      <layout:collectionItem title="letras.column.importe" property="m_Saldo" >
          <p align="right">
            <layout:write name="letra" property="m_Saldo" />&nbsp;
            </p>
      </layout:collectionItem>
      
      <layout:collectionItem title="letras.column.fecha.vencimiento" property="m_FecVenc"  sortable="true" >
        <center>
             <layout:write name="letra" property="m_FecVenc" />
        </center>
      </layout:collectionItem>
      
    </layout:collection>
    <layout:space/>
     <div id="comp0" style="display:none">ANS</div>
    <div id="comp2" style="display:none">ANS</div>
    <div id="comp5" style="display:none">FEC</div>
  </logic:notEmpty>

  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>
  
  <html:hidden property="m_fechaActualComp" styleId="m_fechaActualComp" />
  <html:hidden property="m_FechaMaxAnt" styleId="m_FechaMaxAnt" />
  <html:hidden property="m_FechaMaxPost" styleId="m_FechaMaxPost" />
  </logic:equal>
</html:form>
</body>
</html>