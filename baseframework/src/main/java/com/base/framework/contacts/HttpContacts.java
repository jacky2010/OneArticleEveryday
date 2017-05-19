package com.base.framework.contacts;

/**
 * Created by Administrator on 2016/8/4.
 */
public class HttpContacts {
    //    public static final String BASE_URL = "http://115.159.121.164/SbTestApi/api/";
//    /UploadFiles/SlamBallZoon/Img/20161205/20161205175745_5748large.jpg
  //  public static final String BASE_URL = "http://app.slamball.asia/api/";
    public static final String BASE_URL = "http://115.159.121.164:8001/api/";
   // public static final String BASE_HTML_URL = "http://web.slamball.asia/";
    public static final String BASE_HTML_URL = "http://115.159.121.164:8001/";
    //public static final String IMG_BASE_URL = "http://res.slamball.asia:11000";
    public static final String IMG_BASE_URL = "http://115.159.121.164:8001";
    //用户接口
    public static final String REGISER = BASE_URL + "register";
    //登陆
    public static final String LOGIN = BASE_URL + "login/mobileuserlogin";
    //用户管理
    public static final String SLAMBALLUSER = BASE_URL + "slamballuser";
    //获得徽章列表
    public static final String BADGELIST = BASE_URL + "badge";
    //获取用户信息+徽章信息
    public static final String GETUSERINFO = BASE_URL + "slamballuser";

    public static final String SIGN = BASE_URL + "signlog";
    //会员管理
    public static final String MEMBER = BASE_URL + "member";
    //商城
    public static final String SLAMSHOP=BASE_URL+"slamshop";
    //支付
    public static final String RECHARGE = BASE_URL + "recharge";
    //文章管理
    public static final String ARTICLELIST = BASE_URL + "articlelist";
    //找回密码
    public static final String FINDPWD = BASE_URL + "findpwd";
    //腾讯云处理
    public static final String TENCENT = BASE_URL + "tencent";
    //文件上传
   // public static final String UPLOAD_FILE = "http://res.slamball.asia:11000/api/fileupload/fileOption";
    public static final String UPLOAD_FILE = "http://115.159.121.164:8001/api/fileupload/fileOption";
    //第三方登录
    public static final String OTHERLOGIN = BASE_URL + "login";
    //运动圈
    public static String SLAMBALLZOON = BASE_URL + "slamballzoon";
    //获取修改手机号验证码
    public static final String MOBILECODE = BASE_URL + "updateusermobileno";
    //修改手机号
    public static final String UPDATEMOBILE = BASE_URL + "updateusermobileno";
    //应用版本
    public static String SOFTWAREVERSION = BASE_URL + "softwareversion";
    //提交反馈数据
    public static final String ADDFEEDBACK = BASE_URL + "feedback";
    //账单明细
    public static final String SLAMCOIN = BASE_URL + "slamcoin";
    //我的余额
    public static String MYBALANCE = BASE_URL + "slamcoin/getMySlamCoin";
    public static String DRAW = BASE_URL + "draw";
    //补签卡
    public static String REPLENISHSIGNCARD = BASE_URL + "ReplenishSignCard";
    //启动也图片
    public static String SYSPARAMETERS = BASE_URL + "sysparameters";
    //直播的预约
    public static String VIDEO_ORDER = BASE_URL + "slamballlvbespeak";
    public static String PAGEMAP = BASE_URL + "pagemap";
    //发布pk
    public static String BATTLE = BASE_URL + "battle";
    //我的关注
    public static String MYFOCUS = BASE_URL + "slamballuser";
    public static String VOTING = BASE_URL + "votingactivity";

    //获取背景图
    public static final String BGPIC = "http://115.159.121.164:8001/api/sysparameters/getLogoImage";
}
