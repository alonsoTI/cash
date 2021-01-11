<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252" />
<!-- 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
-->

<META HTTP-EQUIV="Pragma" CONTENT="public">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-store">
<!-- 
<META HTTP-EQUIV="Expires" CONTENT="0">
-->

<script language="JavaScript" type="text/JavaScript">
    function show_popup(){
        //abrimos la ventana del login en una de dimensiones 800x600
        var url;
        var h,w;
        //url = "login.jsp";
        url = "login.do?do=iniClean";//jwong 27-08-09
        w = 810;
        h = 630;
        var vent = window.open(url,"login","height="+(h-30)+",width="+(w-10)+",top=300,left=200,scrollbars=no,resizable=yes,toolbar=no,menubar=no,location=no,status=yes,url=no");
        if(vent==null){
            alert("No se pudo abrir ventana emergente, revise la configuracion de su browser");
        }
        else{
            vent.focus();
        }
    }
</script>
<title>Cash Financiero</title>
</head>
<body onload="javascript:show_popup();">
    <br>
    <p align="center">
        Si tiene problemas visualizando ventanas emergentes haga click
        <a href="javascript:show_popup();">aqui</a>
    </p>
    </br>
</body>
</html>