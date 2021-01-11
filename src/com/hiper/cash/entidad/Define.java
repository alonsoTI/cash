package com.hiper.cash.entidad;

public final class Define {
  //tabla Version
  public static final String tlb_TMVERSION = "tmVersion";
  public static final String fld_CVRVERSIONID = "cVrVersionId";
	public static final String fld_DVRVERSIONNUMBER = "dVrVersionNumber";
	public static final String fld_DVRDESCRIPTION = "dVrDescription";
	public static final String fld_CVRMODIFYUSER = "cVrModifyUser";
	public static final String fld_FVRMODIFYDATE = "fVrModifyDate";
	public static final String fld_HVRMODIFYTIME = "hVrModifyTime";
  //jwong 29/10/07 para el manejo de multiaplicaciones
  public static final String fld_CVRAPPLICATIONID = "cVrApplicationId";
  
  //tabla tmDownloadProfile
  public static final String tlb_TMDOWNLOADPROFILE = "tmDownloadProfile";
  public static final String fld_CDPPROFILEID = "cDPProfileID";
  public static final String fld_DDPPROFILENAME = "dDPProfileName";
  public static final String fld_DDPDESCRIPTION = "dDPDescription";
	public static final String fld_CDPVERSIOID = "cDPVersioID";
	public static final String fld_HDPDOWNLOADTIME = "hDPDownloadTime";
	public static final String fld_FDPAPLICDATE = "fDPAplicDate";
	public static final String fld_HDPTOLERANCEDWNTIME = "hDPToleranceDwnTime";
	public static final String fld_CDPMODIFYUSER = "cDPModifyUser";
	public static final String fld_FDPMODIFYDATE = "fDPModifyDate";
	public static final String fld_HDPMODIFYTIME = "hDPModifyTime";
  
  //tabla txFileTransferLog
  public static final String tlb_TXFILETRANSFERLOG = "txFileTransferLog";
  public static final String fld_NFTGENCOUNTER = "nFTGenCounter";
	public static final String fld_CFTMERCHANTID = "cFTMerchantId";
	public static final String fld_CFTTERMINALNUM = "cFTTerminalNum";
	public static final String fld_CFTTERMINALSN = "cFTTerminalSN";
	public static final String fld_FFTTXNDATE = "fFTTxnDate";
	public static final String fld_HFTTXNTIME = "hFTTxnTime";
	public static final String fld_CFTTXNTYPE = "cFTTxnType";
  
  //tabla tmMerchantChain
  public static final String tlb_TMMERCHANTCHAIN = "tmMerchantChain";
	public static final String fld_CMCCHAINID = "cMCChainID";
	public static final String fld_DMCCHAINNAME = "dMCChainName";
	public static final String fld_DMCDESCRIPTION = "dMCDescription";
	public static final String fld_DMCRUC = "dMCRUC";
  public static final String fld_CMCMODIFYUSER = "cMCModifyUser";
	public static final String fld_FMCMODIFYDATE = "fMCModifyDate";
	public static final String fld_HMCMODIFYTIME = "hMCModifyTime";
  
  //tabla taMerchantProfileApplication
  public static final String tlb_TAMERCHANTPROFILEAPPLICATION = "taMerchantProfileApplication";
	public static final String fld_CMAMERCHANTPROFILEID = "cMAMerchantProfileId";
	public static final String fld_CMAAPPLICATIONID = "cMAApplicationID";
  
  //table tmMerchant
  //jhuaman 14-08-07
  public static final String tlb_TMMERCHANT = "tmMerchant";
  
  public static final String fld_CMRDOWNLOADPROFILE = "cMrDownloadProfile";
  public static final String fld_CMRMERCHANTCHAIN = "cMrMerchantChain";
  //jwong 07/09/07 
  public static final String fld_CMRFORCEUPDATE = "cMrForceUpdate";
  public static final String fld_CMRMERCHANTID = "cMrMerchantId";
  public static final String fld_DMRMERCHANTNAME = "dMrMerchantName";
  //jwong 24/10/07
  public static final String fld_CMRPROFILEID = "cMrProfileId";
  
  //table tmTerminal
  public static final String tlb_TMTERMINAL = "tmTerminal";
  public static final String fld_CTRMERCHANTID = "cTrMerchantId";
  public static final String fld_CTRTERMINALNUM = "cTrTerminalNum";
  public static final String fld_NTRNROHOPPER = "nTrNroHopper";
  
  public static final String fld_CTRCODMONEDA1 = "cTrCodMoneda1";
  public static final String fld_NTRNROMONEDA1 = "nTrNroMoneda1";
  public static final String fld_CTRCODMONEDA2 = "cTrCodMoneda2";
  public static final String fld_NTRNROMONEDA2 = "nTrNroMoneda2";
  public static final String fld_CTRCODMONEDA3 = "cTrCodMoneda3";
  public static final String fld_NTRNROMONEDA3 = "nTrNroMoneda3";
  public static final String fld_CTRCODMONEDA4 = "cTrCodMoneda4";
  public static final String fld_NTRNROMONEDA4 = "nTrNroMoneda4";
  
  public static final String fld_CTRFILESVERSION = "cTrFilesVersion";
  public static final String fld_CTRDOWNLOADSTATE = "cTrDownloadState";
  public static final String fld_NTRDOWNLOADATTEMPTS = "nTrDownloadAttempts";
  public static final String fld_FTRLASTDOWNLOADDATE = "fTrLastDownloadDate";
  public static final String fld_HTRLASTDOWNLOADTIME = "hTrLastDownloadTime";
  
  public static final String fld_FTRLASTECHODATE = "fTrLastEchoDate";
  public static final String fld_HTRLASTECHOTIME = "hTrLastEchoTime";
  public static final String fld_FTRLASTTRXDATE = "fTrLastTrxDate";
  public static final String fld_HTRLASTTRXTIME = "hTrLastTrxTime";
  
  public static final String fld_CTRTERMINALSTATUS = "cTrTerminalStatus";
  public static final String fld_CTRPRINTERSTATUS = "cTrPrinterStatus";
  public static final String fld_CTRHOPPER1STATUS = "cTrHopper1Status";
  public static final String fld_CTRHOPPER2STATUS = "cTrHopper2Status";
  public static final String fld_CTRHOPPER3STATUS = "cTrHopper3Status";
  public static final String fld_CTRHOPPER4STATUS = "cTrHopper4Status";
  
  public static final String fld_CTRFORCEUPDATE = "cTrForceUpdate";
  
  //table txParameter
  public static final String tlb_TXPARAMETER = "txParameter";
  public static final String fld_CPMPARAMETERID = "cPmParameterId";
  public static final String fld_NPMNUMERICVALUE = "nPmNumericValue";
  public static final String fld_DPMTEXTVALUE = "dPmTextValue";
  public static final String fld_DPMDESCRIPTION = "dPmDescription";
  public static final String fld_CPMMODIFYUSER = "cPmModifyUser";
  public static final String fld_FPMMODIFYDATE = "fPmModifyDate";
  public static final String fld_HPMMODIFYTIME = "hPmModifyTime";
  
  //jwong 10/09/07
  //table tpTerminaHis
  public static final String tlb_TPTERMINALHIS = "tpTerminalHis";
  
	public static final String fld_CTHMERCHANTID = "cThMerchantId";
	public static final String fld_CTHTERMINALNUM = "cThTerminalNum";
	public static final String fld_DTHTERMINALSN = "dThTerminalSN";
	public static final String fld_DTHDESCRIPTION = "dThDescription";
	public static final String fld_CTHAPLICACION = "cThAplicacion";
	public static final String fld_CTHMODIFYUSER = "cThModifyUser";
	public static final String fld_FTHMODIFYDATE = "fThModifyDate";
	public static final String fld_HTHMODIFYTIME = "hThModifyTime";
	public static final String fld_CTHOPERATIONTYPE = "cThOperationType";
  
  //jwong 24/10/07
  //table tmApplication
  public static final String tlb_TMAPPLICATION = "tmApplication";
  
	public static final String fld_CAPAPPLICATIONID = "cApApplicationID";
  public static final String fld_DAPAPPLICATIONNAME = "dApApplicationName";
  public static final String fld_DAPAPPLICATIONFLOW = "dApApplicationFlow";
  public static final String fld_NAPAPPLICATIONFLOWSIZE = "nApApplicationFlowSize";
  public static final String fld_CAPMODIFYUSER = "cApModifyUser";
  public static final String fld_FAPMODIFYDATE = "fApModifyDate";
  public static final String fld_HAPMODIFYTIME = "hApModifyTime";
  
  //jwong 24/10/07
  //table taTerminalApplication
  public static final String tlb_TATERMINALAPPLICATION = "taTerminalApplication";
  
  public static final String fld_CTAAPPLICATIONID = "cTAApplicationID";
  public static final String fld_CTATERMINALSN = "cTATerminalSN";
  public static final String fld_NTAFILESGROUP = "nTAFilesGroup";
  public static final String fld_CTAFILESVERSION = "cTAFilesVersion";
  public static final String fld_CTASTATUS = "cTAStatus";
  public static final String fld_CTAMODIFYUSER = "cTAModifyUser";
  public static final String fld_FTAMODIFYDATE = "fTAModifyDate";
  public static final String fld_HTAMODIFYTIME = "hTAModifyTime";
  //jwong 30/10/07 para el manejo del tama�o actual de una aplicacion en el terminal
  public static final String fld_NTAAPPLICATIONFLOWSIZE = "nTAApplicationFlowSize";
  
  //table taDwnProfileVersion
  public static final String tlb_TADWNPROFILEVERSION = "taDwnProfileVersion";
  
  public static final String fld_CDPVPROFILEID = "cDPVProfileId";
  public static final String fld_CDPVVERSIONID = "cDPVVersionId";
  public static final String fld_CDPVAPPLICATIONID = "cDPVApplicationId";
  public static final String fld_CDPVMODIFYUSER = "cDPVModifyUser";
  public static final String fld_FDPVMODIFYDATE = "fDPVModifyDate";
  public static final String fld_HDPVMODIFYTIME = "hDPVModifyTime";
  
  //jwong 26/12/07 tabla tpTransactionLog
  public static final String tlb_TPTRANSACTIONLOG = "tpTransactionLog";
  public static final String fld_NTXGENCOUNTER = "nTxGenCounter";
}