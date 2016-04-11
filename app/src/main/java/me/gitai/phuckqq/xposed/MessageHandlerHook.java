package me.gitai.phuckqq.xposed;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.annotation.TargetApi;
import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserHandle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import me.gitai.phuckqq.BuildConfig;
import me.gitai.phuckqq.Constant;
import me.gitai.phuckqq.data.ServiceMsg;

import me.gitai.library.utils.L;
import me.gitai.library.utils.SharedPreferencesUtil;
import me.gitai.library.utils.StringUtils;

import com.qq.jce.wup.UniPacket;
import com.qq.taf.jce.HexUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by gitai on 16-2-29.
 */
public class MessageHandlerHook implements IXposedHookLoadPackage {

    private Class<?> QQAppInterface,SessionInfo,ChatActivityFacade;

    private void hook1(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException,NoSuchFieldException,IllegalAccessException{
        String className = "com.tencent.mobileqq.app.QQAppInterface";
        String methodName = "a";

        Class<?> ToServiceMsg = lpparam.classLoader.loadClass("com.tencent.qphone.base.remote.ToServiceMsg");

        L.d("Hooking a(ToServiceMsg toServiceMsg)");

        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, methodName,
                ToServiceMsg,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        L.d("hook1->beforeHookedMethod");
                        ServiceMsg toServiceMsg = new ServiceMsg(param.args[0]);
                        L.d(toServiceMsg.toString().replace(",", "\n"));
                    }
                });
    }

    private void hook2(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException,NoSuchFieldException,IllegalAccessException{
        String className = "com.tencent.mobileqq.app.QQAppInterface";
        String methodName = "a";

        Class<?> FromServiceMsg = lpparam.classLoader.loadClass("com.tencent.qphone.base.remote.FromServiceMsg");
        Class<?> ToServiceMsg = lpparam.classLoader.loadClass("com.tencent.qphone.base.remote.ToServiceMsg");

        L.d("Hooking a(ToServiceMsg toServiceMsg, FromServiceMsg fromServiceMsg)");

        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, methodName,
                ToServiceMsg, FromServiceMsg,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        //L.d("hook2->beforeHookedMethod");
                        ServiceMsg toServiceMsg = new ServiceMsg(param.args[0]);
                        ServiceMsg fromServiceMsg = new ServiceMsg("FromServiceMsg", param.args[1]);
                        //L.d(toServiceMsg.toString().replace(",", "\n") + "\n" + fromServiceMsg.toString().replace(",", "\n"));

                        OnReceive(toServiceMsg, fromServiceMsg);
                        //Unipacket(fromServiceMsg.getWupBuffer(), "FileUploadResp", (Object)new FileUploadResp())
                    }
                });
    }

    public Object OnReceive(ServiceMsg toServiceMsg, ServiceMsg fromServiceMsg) throws UnsupportedEncodingException{
        String string = fromServiceMsg.getServiceCmd();
        L.d("decodeRespMsg cmd: " + string);
        switch(string){
            case "OnlinePush.PbPushGroupMsg":
            case "OnlinePush.PbPushDisMsg":
            case "OnlinePush.PbC2CMsgSync":
            case "NearFieldTranFileSvr.NotifyList":
            case "NearFieldDiscussSvr.NotifyList":
                byte[] arrby = fromServiceMsg.getWupBuffer();
                if (arrby != null) {
                    L.d("WupBuffer toString:" + new String(arrby));
                    L.d("WupBuffer toString UTF-8:" + new String(arrby, "UTF-8"));
                    L.d("WupBuffer Hex:" + StringUtils.bytesToHex(arrby));
                    L.d("WupBuffer Length:" + arrby.length);
                    int n =  -4 + arrby.length;
                    L.d("WupBuffer Length -4 :" + n);
                    byte[] arrby1 = new byte[n];
                    System.arraycopy(fromServiceMsg.getWupBuffer(), 4, arrby1, 0, n);
                    fromServiceMsg.putWupBuffer(arrby1);
                    L.d("Msg Decryted : " + new String(fromServiceMsg.getWupBuffer(), "UTF-8"));
                }
                break;
            case "MessageSvc.PushNotify":
                //return (RequestPushNotify)this.a(fromServiceMsg.getWupBuffer(), "req_PushNotify", (Object)new RequestPushNotify());
                break;
            case "MessageSvc.GetMsgV4":
                //SvcResponseGetMsgV2 svcResponseGetMsgV2 = (SvcResponseGetMsgV2)this.a(fromServiceMsg.getWupBuffer(), "resp_GetMsgV2", (Object)new SvcResponseGetMsgV2());
                //if (svcResponseGetMsgV2 != null) {
                //    ReportLog.a("Video", "Receive message packet: seq = " + fromServiceMsg.getRequestSsoSeq() + " " + "size = " + svcResponseGetMsgV2.vMsgInfos.size());
                //    return svcResponseGetMsgV2;
                //}
                //return null;
                break;
            case "MessageSvc.DelMsgV2":
                //SvcResponseDelMsgV2 svcResponseDelMsgV2 = (SvcResponseDelMsgV2)this.a(fromServiceMsg.getWupBuffer(), "resp_DelMsgV2", (Object)new SvcResponseDelMsgV2());
                //if (svcResponseDelMsgV2 != null) {
                //    return svcResponseDelMsgV2;
                //}
                //return null;
                break;
            case "MessageSvc.ReqOffFilePack":
                //t(ToServiceMsg toServiceMsg, FromServiceMsg fromServiceMsg)
                break;
            case "TransService.ReqTmpChatPicDownload":
                //RespTmpChatPicDownload respTmpChatPicDownload = (RespTmpChatPicDownload)this.a(fromServiceMsg.getWupBuffer(), "RespTmpChatPicDownload", (Object)new RespTmpChatPicDownload());
                //if (respTmpChatPicDownload == null) {
                //  respTmpChatPicDownload = null;
                //}
                //return respTmpChatPicDownload;
                break;
            case "MessageSvc.GroupMsgReadConfirm":
                //return (SvcResponseGroupMsgReadConfirm)this.a(fromServiceMsg.getWupBuffer(), "resp_GroupMsgReadConfirm", (Object)new SvcResponseGroupMsgReadConfirm());
                break;
            case "MessageSvc.DisMsgReadConfirm":
                //return (SvcResponseSetConfMsgRead)this.a(fromServiceMsg.getWupBuffer(), "resp_DisMsgReadConfirm", (Object)new SvcResponseSetConfMsgRead());
                break;
            case "MessageSvc.MsgReadedReport":
                //return (SvcResponseMsgReadedReport)this.a(fromServiceMsg.getWupBuffer(), "resp_MsgReadedReport", (Object)new SvcResponseMsgReadedReport());
                break;
            case "MessageSvc.SetRoamMsgAllUser":
                //return this.u(toServiceMsg, fromServiceMsg);
                //return (SvcResponseSetRoamMsg)this.a(fromServiceMsg.getWupBuffer(), "resp_SetRoamMsg", (Object)new SvcResponseSetRoamMsg());
                break;
            case "MessageSvc.SetRoamMsg":
                //return this.v(toServiceMsg, fromServiceMsg);
                //return (SvcResponseSetRoamMsg)this.a(fromServiceMsg.getWupBuffer(), "resp_SetRoamMsg", (Object)new SvcResponseSetRoamMsg());
                break;
            case "MessageSvc.DelRoamMsg":
                //return this.w(toServiceMsg, fromServiceMsg);
                //return (SvcResponseDelRoamMsg)this.a(fromServiceMsg.getWupBuffer(), "resp_DelRoamMsg", (Object)new SvcResponseDelRoamMsg());
                break;
            case "ADMsgSvc.PushMsg":
                //return this.x(toServiceMsg, fromServiceMsg);
                //return (AdMsgInfo)this.a(fromServiceMsg.getWupBuffer(), "PushADMsg", (Object)new AdMsgInfo());
                break;
            case "OnlinePush.ReqPush":
                //return this.l(toServiceMsg, fromServiceMsg);
                //return (SvcReqPushMsg)this.a(fromServiceMsg.getWupBuffer(), "req", (Object)new SvcReqPushMsg());
                break;
            case "MessageSvc.PushReaded":
                //return this.m(toServiceMsg, fromServiceMsg);
                //return (SvcRequestPushReadedNotify)this.a(fromServiceMsg.getWupBuffer(), "req", (Object)new SvcRequestPushReadedNotify());
                break;
            case "TransService.ReqGetSign":
                //return this.c(toServiceMsg, fromServiceMsg);
                //RespGetSign respGetSign = (RespGetSign)this.a(fromServiceMsg.getWupBuffer(), "RespGetSign", (Object)new RespGetSign());
                //if (respGetSign != null && respGetSign.iReplyCode == 0) {
                //    return new MessageFactoryReceiver$SigStruct(this, respGetSign.vKey, respGetSign.vSign);
                //}
                //long l = respGetSign == null ? 0x7F7F7F7F : (long)respGetSign.iReplyCode;
                //this.a(toServiceMsg, l);
                //fromServiceMsg.extraDatcmd2HandlerMap.putLong("ServerReplyCode", l);
                //return null;
                break;
            case "StreamSvr.RespUploadStreamMsg":
                //return this.d(toServiceMsg, fromServiceMsg);
                //SCRespUploadStreamMsg sCRespUploadStreamMsg = (SCRespUploadStreamMsg)this.a(fromServiceMsg.getWupBuffer(), "SCRespUploadStreamMsg", (Object)new SCRespUploadStreamMsg());
                //if (sCRespUploadStreamMsg == null) {
                //    return null;
                //}
                //StreamInfo streamInfo = sCRespUploadStreamMsg.stStreamInfo;
                //return new MessageFactoryReceiver$UploadStreamStruct(StreamDataManager.a(streamInfo.iMsgId, 0), sCRespUploadStreamMsg.shResetSeq, streamInfo.shFlowLayer, streamInfo, sCRespUploadStreamMsg.result);
                break;
            case "MessageSvc.SendVideoMsg":
                //return this.b(toServiceMsg, fromServiceMsg);
                //if (QLog.isColorLevel()) {
                //    QLog.d("push", 2, "decodeVideoChatStatus");
                //}
                //return null;
                break;
            case "StreamSvr.PushStreamMsg":
                //return this.e(toServiceMsg, fromServiceMsg);
                //SCPushStreamMsg sCPushStreamMsg = (SCPushStreamMsg)this.a(fromServiceMsg.getWupBuffer(), "SCPushStreamMsg", (Object)new SCPushStreamMsg());
                //if (sCPushStreamMsg == null) {
                //    return null;
                //}
                //StreamInfo streamInfo = sCPushStreamMsg.stStreamInfo;
                //StreamData streamData = sCPushStreamMsg.stStreamData;
                //long l = sCPushStreamMsg.lKey;
                //Object[] arrobject = new Object[]{l, streamInfo, streamData, sCPushStreamMsg.bubbleID};
                //if (QLog.isColorLevel()) {
                //    QLog.d(this.b, 2, "decodeServerPushStream: vipBubbleID:" + arrobject[3]);
                //}
                //return arrobject;
                break;
            case "AccostSvc.ClientMsg":
                //return this.k(toServiceMsg, fromServiceMsg);
                //return (RespClientMsg)this.a(fromServiceMsg.getWupBuffer(), "RespClientMsg", (Object)new RespClientMsg());
                break;
            case "AccostSvc.ReqInsertBlackList":
                //return this.i(toServiceMsg, fromServiceMsg);
                /*RespInsertBlackList respInsertBlackList = (RespInsertBlackList)this.a(fromServiceMsg.getWupBuffer(), "RespInsertBlackList", (Object)new RespInsertBlackList());
                fromServiceMsg.extraDatcmd2HandlerMap.putString("insertUin", toServiceMsg.extraData.getString("insertUin"));
                if (respInsertBlackList.stHeader.eReplyCode != 0) {
                    respInsertBlackList = null;
                }
                return respInsertBlackList;*/
                break;
            case "AccostSvc.ReqDeleteBlackList":
                //return this.j(toServiceMsg, fromServiceMsg);
                /*RespDeleteBlackList respDeleteBlackList = (RespDeleteBlackList)this.a(fromServiceMsg.getWupBuffer(), "RespDeleteBlackList", (Object)new RespDeleteBlackList());
                fromServiceMsg.extraDatcmd2HandlerMap.putString("deleteUin", toServiceMsg.extraData.getString("deleteUin"));
                if (respDeleteBlackList.stHeader.eReplyCode != 0) {
                    respDeleteBlackList = null;
                }
                return respDeleteBlackList;*/
                break;
            case "AccostSvc.ReqGetBlackList":
                //return this.h(toServiceMsg, fromServiceMsg);
                //return (RespGetBlackList)this.a(fromServiceMsg.getWupBuffer(), "RespGetBlackList", (Object)new RespGetBlackList());
                break;
            case "AccostSvc.SvrMsg":
                //return this.g(toServiceMsg, fromServiceMsg);
                //return (SvrMsg)this.a(fromServiceMsg.getWupBuffer(), "SvrMsg", (Object)new SvrMsg());
                break;
            case "MessageSvc.PullGroupMsgSeq":
                //return this.y(toServiceMsg, fromServiceMsg);
                //SvcResponsePullGroupMsgSeq svcResponsePullGroupMsgSeq = (SvcResponsePullGroupMsgSeq)this.a(fromServiceMsg.getWupBuffer(), "resp_PullGroupMsgSeq", (Object)new SvcResponsePullGroupMsgSeq());
                //if (QLog.isColorLevel()) {
                //    QLog.d("MessageService", 2, "decodePullGroupMsgNumResp res" + (Object)svcResponsePullGroupMsgSeq);
                //}
                //return svcResponsePullGroupMsgSeq;
                break;
            case "TransService.FileUploadReq":
                //return this.A(toServiceMsg, fromServiceMsg);
                //return (FileUploadResp)this.a(fromServiceMsg.getWupBuffer(), "FileUploadResp", (Object)new FileUploadResp());
                break;
            case "TransService.FileDownloadReq":
                //return this.B(toServiceMsg, fromServiceMsg);
                //return (FileDownloadResp)this.a(fromServiceMsg.getWupBuffer(), "FileDownloadResp", (Object)new FileDownloadResp());
                break;
            case "TransService.FileDeleteReq":
                //return this.C(toServiceMsg, fromServiceMsg);
                //return (FileDeleteResp)this.a(fromServiceMsg.getWupBuffer(), "FileDeleteResp", (Object)new FileDeleteResp());
                break;
            case "MessageSvc.RequestPushStatus":
                //return this.z(toServiceMsg, fromServiceMsg);
                //return (RequestPushStatus)this.a(fromServiceMsg.getWupBuffer(), "req_PushStatus", (Object)new RequestPushStatus());
                break;
        }
        return null;
    }

    private static Map cmd2HandlerMap;

    private static Map Cmd2Handler() {
        if (cmd2HandlerMap == null) {
            cmd2HandlerMap = new HashMap();
            cmd2HandlerMap.put("AccostSvc.SvrMsg", new int[]{0, 2});
            cmd2HandlerMap.put("ProfileService.getGroupInfoReq", new int[]{19, 2});
            cmd2HandlerMap.put("AccostSvc.ClientMsg", new int[]{0, 2});
            cmd2HandlerMap.put("ProfileService.GetSimpleInfo", new int[]{6, 1});
            cmd2HandlerMap.put("AccostSvc.ReqDeleteBlackList", new int[]{0});
            cmd2HandlerMap.put("AccostSvc.ReqInsertBlackList", new int[]{0});
            cmd2HandlerMap.put("AccostSvc.ReqGetBlackList", new int[]{0});
            cmd2HandlerMap.put("TransService.ReqGetSign", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.GroupMsgReadConfirm", new int[]{0});
            cmd2HandlerMap.put("SharpSvr.s2c", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PbGetRoamMsg", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.DelRoamMsg", new int[]{0});
            cmd2HandlerMap.put("SharpSvr.c2sack", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.RequestPushStatus", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.DisMsgReadConfirm", new int[]{0});
            cmd2HandlerMap.put("ADMsgSvc.PushMsg", new int[]{0});
            cmd2HandlerMap.put("StreamSvr.PushStreamMsg", new int[]{0});
            cmd2HandlerMap.put("OnlinePush.ReqPush", new int[]{0});
            cmd2HandlerMap.put("OnlinePush.PbPushTransMsg", new int[]{0});
            cmd2HandlerMap.put("OnlinePush.PbC2CMsgSync", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PbBindUinGetMsg", new int[]{0});
            cmd2HandlerMap.put("PbMessageSvc.PbBindUinMsgReadedConfirm", new int[]{0});
            cmd2HandlerMap.put("OnlinePush.PbPushDisMsg", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PullGroupMsgSeq", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PbGetOneDayRoamMsg", new int[]{0});
            cmd2HandlerMap.put("StreamSvr.RespUploadStreamMsg", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.SetRoamMsg", new int[]{0});
            cmd2HandlerMap.put("TransService.ReqOffFilePack", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PbGetGroupMsg", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PbGetDiscussMsg", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.DelMsgV2", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.SetRoamMsgAllUser", new int[]{0});
            cmd2HandlerMap.put("OnlinePush.PbPushGroupMsg", new int[]{0});
            cmd2HandlerMap.put("VideoSvc.Send", new int[]{0});
            cmd2HandlerMap.put("VideoCCSvc.PutInfo", new int[]{0});
            cmd2HandlerMap.put("StreamSvr.UploadStreamMsg", new int[]{0});
            cmd2HandlerMap.put("MultiVideo.s2c", new int[]{0});
            cmd2HandlerMap.put("NearFieldTranFileSvr.NotifyList", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.MsgReadedReport", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PbSendMsg", new int[]{0, 8});
            cmd2HandlerMap.put("MessageSvc.PushReaded", new int[]{0});
            cmd2HandlerMap.put("OnlinePush.RespPush", new int[]{0});
            cmd2HandlerMap.put("TransService.ReqTmpChatPicDownload", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PbGetMsg", new int[]{0});
            cmd2HandlerMap.put("MultiVideo.c2sack", new int[]{0});
            cmd2HandlerMap.put("PbMessageSvc.PbDelOneRoamMsg", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.PushNotify", new int[]{0});
            cmd2HandlerMap.put("MessageSvc.GetMsgV4", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.PullDisMsgSeq", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.PbGetMsg", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.GetMsgV2", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.PbGetDiscussMsg", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.PbGetGroupMsg", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.PullDisGroupSeq", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.NoticeEnd", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.infoLogin", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.PullGroupMsgSeq", new int[]{0});
            cmd2HandlerMap.put("RegPrxySvc.getOffMsg", new int[]{0});
            cmd2HandlerMap.put("ProfileService.Pb.ReqNextSystemMsg", new int[]{0});
            cmd2HandlerMap.put("ProfileService.Pb.ReqSystemMsgRead", new int[]{0});
            cmd2HandlerMap.put("ProfileService.Pb.ReqSystemMsg", new int[]{0});
            cmd2HandlerMap.put("ProfileService.Pb.ReqSystemMsgNew", new int[]{0});
            cmd2HandlerMap.put("ProfileService.Pb.ReqSystemMsgNew.Friend", new int[]{0});
            cmd2HandlerMap.put("ProfileService.Pb.ReqSystemMsgNew.Group", new int[]{0});
            cmd2HandlerMap.put("ProfileService.Pb.ReqSystemMsgAction", new int[]{0});
            cmd2HandlerMap.put("PbMessageSvc.PbMsgReadedReport", new int[]{0});
            cmd2HandlerMap.put("friendlist.delFriend", new int[]{1});
            cmd2HandlerMap.put("friendlist.GetAutoInfoReq", new int[]{1});
            cmd2HandlerMap.put("friendlist.SetGroupReq", new int[]{1});
            cmd2HandlerMap.put("friendlist.GetLastLoginInfoReq", new int[]{1});
            cmd2HandlerMap.put("friendlist.getFriendGroupList", new int[]{1});
            cmd2HandlerMap.put("friendlist.MovGroupMemReq", new int[]{1});
            cmd2HandlerMap.put("friendlist.GetSimpleOnlineFriendInfoReq", new int[]{1});
            cmd2HandlerMap.put("BumpSvc.ReqComfirmContactFriend", new int[]{1});
            cmd2HandlerMap.put("friendlist.addFriend", new int[]{1});
            cmd2HandlerMap.put("friendlist.getUserAddFriendSetting", new int[]{1});
            cmd2HandlerMap.put("OidbSvc.0x5d1_0", new int[]{1});
            cmd2HandlerMap.put("OidbSvc.0x4fc_30", new int[]{1});
            cmd2HandlerMap.put("DevLockAuthSvc.RecommendAuth", new int[]{1});
            cmd2HandlerMap.put("DevLockAuthSvc.ConfirmAuth", new int[]{1});
            cmd2HandlerMap.put("SummaryCard.ReqSearch", new int[]{1});
            cmd2HandlerMap.put("SummaryCard.ReqCondSearch", new int[]{1});
            cmd2HandlerMap.put("AvatarInfoSvr.QQHeadUrlReq", new int[]{1});
            cmd2HandlerMap.put("ProfileService.ReqGetSettings", new int[]{1});
            cmd2HandlerMap.put("StatSvc.register", new int[]{1});
            cmd2HandlerMap.put("ProfileService.GetRichSig", new int[]{1});
            cmd2HandlerMap.put("StatSvc.DelDevLoginInfo", new int[]{1});
            cmd2HandlerMap.put("StatSvc.BindUin", new int[]{1});
            cmd2HandlerMap.put("ProfileService.ReqSetSettings", new int[]{1});
            cmd2HandlerMap.put("StatSvc.SvcReqKikOut", new int[]{1});
            cmd2HandlerMap.put("ProfileService.CheckUpdateReq", new int[]{1});
            cmd2HandlerMap.put("ProfileService.ChangeFriendName", new int[]{1});
            cmd2HandlerMap.put("ProfileService.SetRichSig", new int[]{1});
            cmd2HandlerMap.put("IncreaseURLSvr.QQHeadUrlReq", new int[]{1});
            cmd2HandlerMap.put("StatSvc.GetDevLoginInfo", new int[]{1});
            cmd2HandlerMap.put("friendlist.GetOnlineInfoReq", new int[]{1});
            cmd2HandlerMap.put("OidbSvc.0x476_146", new int[]{1});
            cmd2HandlerMap.put("OidbSvc.0x476_147", new int[]{1});
            cmd2HandlerMap.put("ProfileService.ReqBatchProcess", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x8c9_2", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x88d_0", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x88d_7", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x8b8_1", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x88d_10", new int[]{19});
            cmd2HandlerMap.put("ProfileService.GroupMngReq", new int[]{19});
            cmd2HandlerMap.put("friendlist.GetMultiTroopInfoReq", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x8fd_0", new int[]{19});
            cmd2HandlerMap.put("friendlist.GetTroopListReqV2", new int[]{19});
            cmd2HandlerMap.put("friendlist.GetTroopAppointRemarkReq", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x899_0", new int[]{19});
            cmd2HandlerMap.put("friendlist.ModifyGroupInfoReq", new int[]{19});
            cmd2HandlerMap.put("friendlist.getTroopMemberList", new int[]{19});
            cmd2HandlerMap.put("group_member_card.get_group_member_card_info", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x89e_0", new int[]{19});
            cmd2HandlerMap.put("friendlist.getTroopRemark", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x88d_1", new int[]{21, 19});
            cmd2HandlerMap.put("OidbSvc.0x8a0_0", new int[]{19});
            cmd2HandlerMap.put("friendlist.ModifyGroupCardReq", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x8bb_2", new int[]{19});
            cmd2HandlerMap.put("OidbSvc.0x8bb_3", new int[]{19});
            cmd2HandlerMap.put("VisitorSvc.ReqFavorite", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqUpdateQQFace", new int[]{2});
            cmd2HandlerMap.put("OidbSvc.0x491_100", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqUpdateIntro", new int[]{2});
            cmd2HandlerMap.put("VisitorSvc.ReqGetVoterList", new int[]{2});
            cmd2HandlerMap.put("PttCenterSvr.ReqBody", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqHYMakeFriendsCard", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqCommonCard", new int[]{2});
            cmd2HandlerMap.put("VisitorSvc.ReqGetVisitorList", new int[]{2});
            cmd2HandlerMap.put("SummaryCard.ReqSummaryCard", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqFaceInfo", new int[]{2});
            cmd2HandlerMap.put("SummaryCard.ReqVoiceManage", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqGetFace", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqDelFace", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqGetCardSwitch", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqPicSafetyCheck", new int[]{2});
            cmd2HandlerMap.put("MobileQQ.SendPortraitDownloadVerifyCode", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqMakeFriendsCard", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqSetCard", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqSetCardSwitch", new int[]{2});
            cmd2HandlerMap.put("OidbSvc.0x490_100", new int[]{2});
            cmd2HandlerMap.put("VisitorSvc.ReqVote", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqAddFace", new int[]{2});
            cmd2HandlerMap.put("MCardSvc.ReqHYCommonCard", new int[]{2});
            cmd2HandlerMap.put("ProfileService.SetUserInfoReq", new int[]{2});
            cmd2HandlerMap.put("ProfileService.GetSglUsrFullInfo", new int[]{2});
            cmd2HandlerMap.put("ProfileService.getFriendInfoReq", new int[]{2});
            cmd2HandlerMap.put("SQQzoneSvc.getCover", new int[]{2});
            cmd2HandlerMap.put("NeighborSvc.ReqGetNeighbors", new int[]{3});
            cmd2HandlerMap.put("NearbyGroup.GetGroupList", new int[]{3});
            cmd2HandlerMap.put("EncounterSvc.ReqGetEncounter", new int[]{3});
            cmd2HandlerMap.put("LBS.AddressService", new int[]{3});
            cmd2HandlerMap.put("NeighborSvc.ReqGetSwitches", new int[]{3});
            cmd2HandlerMap.put("NeighborSvc.ReqSetStateSwitch", new int[]{3});
            cmd2HandlerMap.put("NeighborSvc.ReqGetPoint", new int[]{3});
            cmd2HandlerMap.put("NeighborSvc.ReqSetUserState", new int[]{3});
            cmd2HandlerMap.put("QzoneService.knrsNew", new int[]{3});
            cmd2HandlerMap.put("NearbyGroup.ReqGetAreaList", new int[]{3});
            cmd2HandlerMap.put("NearbyGroup.ReqGetGroupInArea", new int[]{3});
            cmd2HandlerMap.put("OidbSvc.0x568_20", new int[]{3});
            cmd2HandlerMap.put("LbsShareSvr.nearby_shops", new int[]{3});
            cmd2HandlerMap.put("LbsShareSvr.location", new int[]{3});
            cmd2HandlerMap.put("LbsShareSvr.get_shops_by_ids", new int[]{3});
            cmd2HandlerMap.put("ConfigService.ClientReq", new int[]{4});
            cmd2HandlerMap.put("ConfigService.GetResourceReq", new int[]{4});
            cmd2HandlerMap.put("MobileTipsSvc.TipsReport", new int[]{4});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqGetDiscussInteRemark", new int[]{6});
            cmd2HandlerMap.put("QRCodeSvc.discuss_geturl", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqGetDiscuss", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqCreateDiscuss", new int[]{6});
            cmd2HandlerMap.put("QRCodeSvc.discuss_decode", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqJoinDiscuss", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqSetDiscussFlag", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqChangeDiscussName", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqAddDiscussMember", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqSetDiscussAttr", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqGetDiscussInfo", new int[]{6});
            cmd2HandlerMap.put("QQServiceDiscussSvc.ReqQuitDiscuss", new int[]{6});
            cmd2HandlerMap.put("OidbSvc.0x865_3", new int[]{6});
            cmd2HandlerMap.put("OidbSvc.0x870_4", new int[]{6});
            cmd2HandlerMap.put("OidbSvc.0x870_5", new int[]{6});
            cmd2HandlerMap.put("QzoneService.GetNewAndUnread", new int[]{7});
            cmd2HandlerMap.put("TransService.FileUploadReq", new int[]{8});
            cmd2HandlerMap.put("TransService.FileDeleteReq", new int[]{8});
            cmd2HandlerMap.put("TransService.FileDownloadReq", new int[]{8});
            cmd2HandlerMap.put("RegPrxySvc.PushParam", new int[]{9});
            cmd2HandlerMap.put("RegPrxySvc.infoAndroid", new int[]{9});
            cmd2HandlerMap.put("BQMallSvc.TabOpReq", new int[]{11});
            cmd2HandlerMap.put("OidbSvc.0x490_92", new int[]{11});
            cmd2HandlerMap.put("AvatarUpdate.checkUpdate", new int[]{12});
            cmd2HandlerMap.put("AuthSvr.ThemeAuth", new int[]{13});
            cmd2HandlerMap.put("SpecialRemind.Service", new int[]{14});
            cmd2HandlerMap.put("ClubContentUpdate.Req", new int[]{15});
            cmd2HandlerMap.put("OidbSvc.0x7a1_0", new int[]{16});
            cmd2HandlerMap.put("OidbSvc.0x7a0_0", new int[]{16});
            cmd2HandlerMap.put("OidbSvc.0x7a2_0", new int[]{16});
            cmd2HandlerMap.put("SsoSnsSession.Cmd0x3_SubCmd0x3_FuncDelBlockList", new int[]{17});
            cmd2HandlerMap.put("SsoSnsSession.Cmd0x3_SubCmd0x1_FuncGetBlockList", new int[]{17});
            cmd2HandlerMap.put("SsoSnsSession.Cmd0x3_SubCmd0x2_FuncAddBlockList", new int[]{17});
            cmd2HandlerMap.put("StatSvc.InSaveTraffic", new int[]{18});
            cmd2HandlerMap.put("StatSvc.OutSaveTraffic", new int[]{18});
            cmd2HandlerMap.put("GroupSvc.JoinGroupLink", new int[]{19});
            cmd2HandlerMap.put("mq_crm.get_menu", new int[]{20});
            cmd2HandlerMap.put("mq_crm.send_key", new int[]{20});
            cmd2HandlerMap.put("EqqAccountSvc.get_eqq_list", new int[]{20});
            cmd2HandlerMap.put("CrmSvcEx.ReportLbs", new int[]{20});
            cmd2HandlerMap.put("GroupFileAppSvr.CopyTo", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x897_0", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x89b_1", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x8a1_0", new int[]{21});
            cmd2HandlerMap.put("OpenGroupSvc.GroupActivityInfo", new int[]{21});
            cmd2HandlerMap.put("GroupFileAppSvr.DelFile", new int[]{21});
            cmd2HandlerMap.put("GroupFileAppSvr.FeedMsgV2", new int[]{21});
            cmd2HandlerMap.put("GroupFileAppSvr.Upload", new int[]{21});
            cmd2HandlerMap.put("GroupFileAppSvr.TransFile", new int[]{21});
            cmd2HandlerMap.put("CommunityForum.GetLatestPost", new int[]{21});
            cmd2HandlerMap.put("MobileqqApp.IsHasMoreApp", new int[]{21});
            cmd2HandlerMap.put("GroupFileAppSvr.Download", new int[]{21});
            cmd2HandlerMap.put("GroupFileAppSvr.Resend", new int[]{21});
            cmd2HandlerMap.put("GroupActivity.GetList", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x78f_1", new int[]{21});
            cmd2HandlerMap.put("GroupFileAppSvr.GetFileList", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x5d6_1", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x88c_1", new int[]{21});
            cmd2HandlerMap.put("GroupFileAppSvr.GetFileListV2", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x852_35", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x580_1", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x852_48", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x89a_0", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x570_8", new int[]{21});
            cmd2HandlerMap.put("VideoShareSrv.get_video_src", new int[]{21});
            cmd2HandlerMap.put("OidbSvc.0x568_21", new int[]{21});
            cmd2HandlerMap.put("SecCheckSigSvc.UploadReq", new int[]{22, 23, 25});
            cmd2HandlerMap.put("SecSafeChkSvc.MainCmd", new int[]{27});
            cmd2HandlerMap.put("SecIntChkSvc.MainCmd", new int[]{26});
            cmd2HandlerMap.put("OidbSvc.0x5d2_0", new int[]{28});
            cmd2HandlerMap.put("OidbSvc.0x5d4_0", new int[]{28});
            cmd2HandlerMap.put("ClubInfoSvc.GetVipInfoReq", new int[]{29});
            cmd2HandlerMap.put("HornSvc1.GetNearHorn", new int[]{31});
            cmd2HandlerMap.put("HornSvc2.PublishHorn", new int[]{31});
            cmd2HandlerMap.put("HornSvc2.DelMyHorn", new int[]{31});
            cmd2HandlerMap.put("HornSvc3.GetMyHorn", new int[]{31});
            cmd2HandlerMap.put("CardPayControl.queryChannel", new int[]{32});
            cmd2HandlerMap.put("CardPayControl.queryOrderIndex", new int[]{32});
            cmd2HandlerMap.put("CardPayControl.queryPayOrder", new int[]{32});
            cmd2HandlerMap.put("CardPayControl.queryOrderDetail", new int[]{32});
            cmd2HandlerMap.put("RedTouchSvc.EntranceSetting", new int[]{34});
            cmd2HandlerMap.put("SecuritySvc.GetConfig", new int[]{41});
            cmd2HandlerMap.put("NearFieldDiscussSvr.ReqJoinDiscuss", new int[]{40});
            cmd2HandlerMap.put("NearFieldDiscussSvr.ReqGetList", new int[]{40});
            cmd2HandlerMap.put("NearFieldDiscussSvr.ReqExit", new int[]{40});
            cmd2HandlerMap.put("NearFieldDiscussSvr.NotifyList", new int[]{40});
            cmd2HandlerMap.put("QQiSvc.translate", new int[]{36});
            cmd2HandlerMap.put("QQiSvc.querypublicaccountwhitelist", new int[]{38});
            cmd2HandlerMap.put("QQiSvc.fs_account", new int[]{42});
            cmd2HandlerMap.put("QQiSvc.gti_balance_info", new int[]{42});
            cmd2HandlerMap.put("QQiSvc.sendUinInfo", new int[]{42});
            cmd2HandlerMap.put("QQiSvc.tencentpay_buy_goods", new int[]{42});
            cmd2HandlerMap.put("QQiSvc.translate", new int[]{36});
            cmd2HandlerMap.put("QQiSvc.querypublicaccountwhitelist", new int[]{38});
        }
        return cmd2HandlerMap;
    }

    private Object Unipacket(byte[] arrby, String string, Object object){
        if (arrby == null) {
            return null;
        }
        UniPacket uniPacket = new UniPacket();
        try {
            uniPacket.setEncodeName("utf-8");
            uniPacket.decode(arrby);
        }
        catch (RuntimeException var6_5) {
            return null;
        }
        catch (Exception var5_6) {
            return null;
        }
        return uniPacket.getByClass(string, object);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Application app = AndroidAppHelper.currentApplication();

        L.setLogcatEnable(app, true);
        L.setLogToFileEnable(true, app, "/sdcard/phuckqq/logs");
        L.setXposedMode(true);

        L.d(lpparam.packageName);
        if ("com.tencent.qq.kddi".equals(lpparam.packageName)) {
            L.d("PhuckQQ initializing...");

            L.i("Phone manufacturer: %s", Build.MANUFACTURER);
            L.i("Phone model: %s", Build.MODEL);
            L.i("Android version: %s", Build.VERSION.RELEASE);
            L.i("Xposed bridge version: %d", XposedBridge.XPOSED_BRIDGE_VERSION);
            L.i("Captcha version: %s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);

            try {
                hook1(lpparam);
            } catch (Throwable e) {
                L.d("Failed to hook1 QQ handler" + "\n" + Log.getStackTraceString(e));
                throw e;
            }
            try {
                hook2(lpparam);
            } catch (Throwable e) {
                L.d("Failed to hook2 QQ handler" + "\n" + Log.getStackTraceString(e));
                throw e;
            }
            L.d("PhuckQQ initialization complete!");
        }
    }
}
