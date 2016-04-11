package me.gitai.phuckqq.data;

/**
 * Created by gitai on 16-4-10.
 */
/*public enum MsfCommand {
  static {
    registerMsfService = new MsfCommand("registerMsfService", 1);
    unRegisterMsfService = new MsfCommand("unRegisterMsfService", 2);
    loginAuth = new MsfCommand("loginAuth", 3);
    changeUinLogin = new MsfCommand("changeUinLogin", 4);
    delLoginedAccount = new MsfCommand("delLoginedAccount", 5);
    submitVerifyCode = new MsfCommand("submitVerifyCode", 6);
    refreVerifyCode = new MsfCommand("refreVerifyCode", 7);
    refreshTickets = new MsfCommand("refreshTickets", 8);
    getServerTime = new MsfCommand("getServerTime", 9);
    registerPush = new MsfCommand("registerPush", 10);
    unRegisterPush = new MsfCommand("unRegisterPush", 11);
    registerCmdCallback = new MsfCommand("registerCmdCallback", 12);
    resetCmdCallback = new MsfCommand("resetCmdCallback", 13);
    reportMsg = new MsfCommand("reportMsg", 14);
    getServerConfig = new MsfCommand("getServerConfig", 15);
    onConnOpened = new MsfCommand("onConnOpened", 16);
    onReceFirstResp = new MsfCommand("onReceFirstResp", 17);
    onOepnConnAllFailed = new MsfCommand("onOepnConnAllFailed", 18);
    onConnClosed = new MsfCommand("onConnClosed", 19);
    onConnWeakNet = new MsfCommand("onConnWeakNet", 20);
    onNetNeedSignon = new MsfCommand("onNetNeedSignon", 21);
    onInvalidSign = new MsfCommand("onInvalidSign", 22);
    onTokenExpired = new MsfCommand("onTokenExpired", 23);
    onTicketChanged = new MsfCommand("onTicketChanged", 24);
    onRecvConfigPush = new MsfCommand("onRecvConfigPush", 25);
    onRecvVerifyCode = new MsfCommand("onRecvVerifyCode", 26);
    onRecvPushMsg = new MsfCommand("onRecvPushMsg", 27);
    onRecvNotifyMsg = new MsfCommand("onRecvNotifyMsg", 28);
    onOnlineStatusChanged = new MsfCommand("onOnlineStatusChanged", 29);
    _msf_RegPush = new MsfCommand("_msf_RegPush", 30);
    _msf_UnRegPush = new MsfCommand("_msf_UnRegPush", 31);
    _msf_queryPush = new MsfCommand("_msf_queryPush", 32);
    _msf_refreToken = new MsfCommand("_msf_refreToken", 33);
    _msf_kickedAndCleanTokenResp = new MsfCommand("_msf_kickedAndCleanTokenResp", 34);
    _msf_getConfig = new MsfCommand("_msf_getConfig", 35);
    _msf_HeartbeatAlive = new MsfCommand("_msf_HeartbeatAlive", 36);
    _msf_NetException = new MsfCommand("_msf_NetException", 37);
    SEND_WIRELESS_PSWREQ = new MsfCommand("SEND_WIRELESS_PSWREQ", 38);
    SEND_WIRELESS_MEIBAOREQ = new MsfCommand("SEND_WIRELESS_MEIBAOREQ", 39);
    _setMsfSuspend = new MsfCommand("_setMsfSuspend", 40);
    _setMsfResunmed = new MsfCommand("_setMsfResunmed", 41);
    getPluginConfig = new MsfCommand("getPluginConfig", 42);
    regUin_queryMobile = new MsfCommand("regUin_queryMobile", 43);
    regUin_commitMobile = new MsfCommand("regUin_commitMobile", 44);
    regUin_querySmsStat = new MsfCommand("regUin_querySmsStat", 45);
    regUin_reSendSms = new MsfCommand("regUin_reSendSms", 46);
    regUin_commitSmsCode = new MsfCommand("regUin_commitSmsCode", 47);
    regUin_commitPass = new MsfCommand("regUin_commitPass", 48);
    checkRole = new MsfCommand("checkRole", 49);
    changeToken = new MsfCommand("changeToken", 50);
    proxyRegisterPush = new MsfCommand("proxyRegisterPush", 51);
    proxyUnRegisterPush = new MsfCommand("proxyUnRegisterPush", 52);
    appDataIncerment = new MsfCommand("appDataIncerment", 53);
    getAppDataCount = new MsfCommand("getAppDataCount", 54);
    accountTokenSyncCheckSign = new MsfCommand("accountTokenSyncCheckSign", 55);
    appReportLog = new MsfCommand("appReportLog", 56);
    getMsfDebugInfo = new MsfCommand("getMsfDebugInfo", 57);
    reportRdm = new MsfCommand("reportRdm", 58);
    reportSocket = new MsfCommand("reportSocket", 59);
    pushSetConfig = new MsfCommand("pushSetConfig", 60);
    getKey = new MsfCommand("getKey", 61);
    getAlterTickets = new MsfCommand("getAlterTickets", 62);
    openConn = new MsfCommand("openConn", 63);
    getGatewayIp = new MsfCommand("getGatewayIp", 64);
    setMsfConnStatus = new MsfCommand("setMsfConnStatus", 65);
    sendVideoAck = new MsfCommand("sendVideoAck", 66);
    wt_loginAuth = new MsfCommand("wt_loginAuth", 67);
    wt_exchange = new MsfCommand("wt_exchange", 68);
    wt_name2uin = new MsfCommand("wt_name2uin", 69);
    wt_other = new MsfCommand("wt_other", 70);
    _msf_QualityTest = new MsfCommand("_msf_QualityTest", 71);
    wt_GetStWithPasswd = new MsfCommand("wt_GetStWithPasswd", 72);
    wt_GetStWithoutPasswd = new MsfCommand("wt_GetStWithoutPasswd", 73);
    wt_CheckPictureAndGetSt = new MsfCommand("wt_CheckPictureAndGetSt", 74);
    wt_RefreshPictureData = new MsfCommand("wt_RefreshPictureData", 75);
    wt_VerifyCode = new MsfCommand("wt_VerifyCode", 76);
    wt_CloseCode = new MsfCommand("wt_CloseCode", 77);
    wt_GetA1WithA1 = new MsfCommand("wt_GetA1WithA1", 78);
    wt_CheckDevLockStatus = new MsfCommand("wt_CheckDevLockStatus", 79);
    wt_AskDevLockSms = new MsfCommand("wt_AskDevLockSms", 80);
    wt_CheckDevLockSms = new MsfCommand("wt_CheckDevLockSms", 81);
    wt_CloseDevLock = new MsfCommand("wt_CloseDevLock", 82);
    wt_RefreshSMSData = new MsfCommand("wt_RefreshSMSData", 83);
    wt_CheckSMSAndGetSt = new MsfCommand("wt_CheckSMSAndGetSt", 84);
    wt_CheckSMSAndGetStExt = new MsfCommand("wt_CheckSMSAndGetStExt", 85);
    wt_setRegDevLockFlag = new MsfCommand("wt_setRegDevLockFlag", 86);
    wt_RegGetSMSVerifyLoginAccount = new MsfCommand("wt_RegGetSMSVerifyLoginAccount", 87);
    wt_CheckSMSVerifyLoginAccount = new MsfCommand("wt_CheckSMSVerifyLoginAccount", 88);
    wt_RefreshSMSVerifyLoginCode = new MsfCommand("wt_RefreshSMSVerifyLoginCode", 89);
    wt_VerifySMSVerifyLoginCode = new MsfCommand("wt_VerifySMSVerifyLoginCode", 90);
    wt_GetStViaSMSVerifyLogin = new MsfCommand("wt_GetStViaSMSVerifyLogin", 91);
    processGuardModeChange = new MsfCommand("processGuardModeChange", 92);
    MsfCommand[] arrayOfMsfCommand = new MsfCommand[93];
    arrayOfMsfCommand[0] = unknown;
    arrayOfMsfCommand[1] = registerMsfService;
    arrayOfMsfCommand[2] = unRegisterMsfService;
    arrayOfMsfCommand[3] = loginAuth;
    arrayOfMsfCommand[4] = changeUinLogin;
    arrayOfMsfCommand[5] = delLoginedAccount;
    arrayOfMsfCommand[6] = submitVerifyCode;
    arrayOfMsfCommand[7] = refreVerifyCode;
    arrayOfMsfCommand[8] = refreshTickets;
    arrayOfMsfCommand[9] = getServerTime;
    arrayOfMsfCommand[10] = registerPush;
    arrayOfMsfCommand[11] = unRegisterPush;
    arrayOfMsfCommand[12] = registerCmdCallback;
    arrayOfMsfCommand[13] = resetCmdCallback;
    arrayOfMsfCommand[14] = reportMsg;
    arrayOfMsfCommand[15] = getServerConfig;
    arrayOfMsfCommand[16] = onConnOpened;
    arrayOfMsfCommand[17] = onReceFirstResp;
    arrayOfMsfCommand[18] = onOepnConnAllFailed;
    arrayOfMsfCommand[19] = onConnClosed;
    arrayOfMsfCommand[20] = onConnWeakNet;
    arrayOfMsfCommand[21] = onNetNeedSignon;
    arrayOfMsfCommand[22] = onInvalidSign;
    arrayOfMsfCommand[23] = onTokenExpired;
    arrayOfMsfCommand[24] = onTicketChanged;
    arrayOfMsfCommand[25] = onRecvConfigPush;
    arrayOfMsfCommand[26] = onRecvVerifyCode;
    arrayOfMsfCommand[27] = onRecvPushMsg;
    arrayOfMsfCommand[28] = onRecvNotifyMsg;
    arrayOfMsfCommand[29] = onOnlineStatusChanged;
    arrayOfMsfCommand[30] = _msf_RegPush;
    arrayOfMsfCommand[31] = _msf_UnRegPush;
    arrayOfMsfCommand[32] = _msf_queryPush;
    arrayOfMsfCommand[33] = _msf_refreToken;
    arrayOfMsfCommand[34] = _msf_kickedAndCleanTokenResp;
    arrayOfMsfCommand[35] = _msf_getConfig;
    arrayOfMsfCommand[36] = _msf_HeartbeatAlive;
    arrayOfMsfCommand[37] = _msf_NetException;
    arrayOfMsfCommand[38] = SEND_WIRELESS_PSWREQ;
    arrayOfMsfCommand[39] = SEND_WIRELESS_MEIBAOREQ;
    arrayOfMsfCommand[40] = _setMsfSuspend;
    arrayOfMsfCommand[41] = _setMsfResunmed;
    arrayOfMsfCommand[42] = getPluginConfig;
    arrayOfMsfCommand[43] = regUin_queryMobile;
    arrayOfMsfCommand[44] = regUin_commitMobile;
    arrayOfMsfCommand[45] = regUin_querySmsStat;
    arrayOfMsfCommand[46] = regUin_reSendSms;
    arrayOfMsfCommand[47] = regUin_commitSmsCode;
    arrayOfMsfCommand[48] = regUin_commitPass;
    arrayOfMsfCommand[49] = checkRole;
    arrayOfMsfCommand[50] = changeToken;
    arrayOfMsfCommand[51] = proxyRegisterPush;
    arrayOfMsfCommand[52] = proxyUnRegisterPush;
    arrayOfMsfCommand[53] = appDataIncerment;
    arrayOfMsfCommand[54] = getAppDataCount;
    arrayOfMsfCommand[55] = accountTokenSyncCheckSign;
    arrayOfMsfCommand[56] = appReportLog;
    arrayOfMsfCommand[57] = getMsfDebugInfo;
    arrayOfMsfCommand[58] = reportRdm;
    arrayOfMsfCommand[59] = reportSocket;
    arrayOfMsfCommand[60] = pushSetConfig;
    arrayOfMsfCommand[61] = getKey;
    arrayOfMsfCommand[62] = getAlterTickets;
    arrayOfMsfCommand[63] = openConn;
    arrayOfMsfCommand[64] = getGatewayIp;
    arrayOfMsfCommand[65] = setMsfConnStatus;
    arrayOfMsfCommand[66] = sendVideoAck;
    arrayOfMsfCommand[67] = wt_loginAuth;
    arrayOfMsfCommand[68] = wt_exchange;
    arrayOfMsfCommand[69] = wt_name2uin;
    arrayOfMsfCommand[70] = wt_other;
    arrayOfMsfCommand[71] = _msf_QualityTest;
    arrayOfMsfCommand[72] = wt_GetStWithPasswd;
    arrayOfMsfCommand[73] = wt_GetStWithoutPasswd;
    arrayOfMsfCommand[74] = wt_CheckPictureAndGetSt;
    arrayOfMsfCommand[75] = wt_RefreshPictureData;
    arrayOfMsfCommand[76] = wt_VerifyCode;
    arrayOfMsfCommand[77] = wt_CloseCode;
    arrayOfMsfCommand[78] = wt_GetA1WithA1;
    arrayOfMsfCommand[79] = wt_CheckDevLockStatus;
    arrayOfMsfCommand[80] = wt_AskDevLockSms;
    arrayOfMsfCommand[81] = wt_CheckDevLockSms;
    arrayOfMsfCommand[82] = wt_CloseDevLock;
    arrayOfMsfCommand[83] = wt_RefreshSMSData;
    arrayOfMsfCommand[84] = wt_CheckSMSAndGetSt;
    arrayOfMsfCommand[85] = wt_CheckSMSAndGetStExt;
    arrayOfMsfCommand[86] = wt_setRegDevLockFlag;
    arrayOfMsfCommand[87] = wt_RegGetSMSVerifyLoginAccount;
    arrayOfMsfCommand[88] = wt_CheckSMSVerifyLoginAccount;
    arrayOfMsfCommand[89] = wt_RefreshSMSVerifyLoginCode;
    arrayOfMsfCommand[90] = wt_VerifySMSVerifyLoginCode;
    arrayOfMsfCommand[91] = wt_GetStViaSMSVerifyLogin;
    arrayOfMsfCommand[92] = processGuardModeChange;
    $VALUES = arrayOfMsfCommand;
  }
}*/