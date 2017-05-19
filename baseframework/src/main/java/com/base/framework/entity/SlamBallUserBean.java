package com.base.framework.entity;


import com.base.framework.contacts.AppContacts;
import com.base.framework.utils.LOG;
import com.base.framework.utils.ObjectsSerializableUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/10.
 */
public class SlamBallUserBean implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;

    /**
     * Area :
     * AreaName :
     * AuthDate : 2016-08-12 17:49:40.437
     * Birthday :
     * City :
     * CityName :
     * Country :
     * CountryName :
     * Email :
     * Experience : 0
     * Facebook :
     * Gender :
     * HeaderImg :
     * IsDeleted : false
     * JPushCode :
     * LastLoginIp :
     * LastLoginTime : 2016-08-12 17:49:40.437
     * MicroBlog :
     * MobileNo : 18678233061
     * NickName :
     * Province :
     * QQ :
     * RegisterDate : 2016-08-12 17:49:40.437
     * SlamBallUserID : 108
     * SlamCoins : 0.0
     * StateName :
     * Street :
     * UserAccount : SLAMBALL20160000038
     * UserAuth : 0
     * UserCode : 20160000038
     * UserInfoID : 23
     * UserPassword : CAF1A3DFB505FFED0D024130F58C5CFA
     * UserProfile :
     * Uuid :
     * WeChat :
     */

    private String Area;
    private String AreaName;
    private String AuthDate;
    private String Birthday;
    private String City;
    private String CityName;
    private String Country;
    private String CountryName;
    private String Email;
    private int Experience;
    private String Facebook;
    private String Gender;
    private String HeaderImg;
    private boolean IsDeleted;
    private String JPushCode;
    private String LastLoginIp;
    private String LastLoginTime;
    private String MicroBlog;
    private String MobileNo;
    private String NickName;
    private String Province;
    private String QQ;
    private String RegisterDate;
    private int SlamBallUserID;
    private double SlamCoins;
    private String StateName;
    private String Street;
    private String UserAccount;
    private int UserAuth;
    private String UserCode;
    private int UserInfoID;
    private String UserPassword;
    private String UserProfile;
    private String Uuid;
    private String WeChat;
    /**
     * JudgeFirstLogin : false
     * JudgeMember : false
     * JudgeSetPwd : true
     */

    private boolean JudgeFirstLogin;
    private boolean JudgeMember;
    private boolean JudgeSetPwd;

    /**
     * MemberLevelLogo :
     * UserLevelDescn :
     * UserLevelLogo :
     * VIPLevel :
     */

    private String MemberLevelLogo;
    private String UserLevelDescn;
    private String UserLevelLogo;
    private String VIPLevel;
    private String TencentSign;
    private static SlamBallUserBean instance;
    /**
     * IdentityCode : 20160920155151
     * IsMember : false
     */

    private String IdentityCode;


    private String IsTeacher;

    /**
     * TencentSign : eJxlj1FrgzAUhd-9FeKrYzOJqeugD3E6a0nHREXci4hJu6w1DTEtLWP-fcUVJuy*fh-n3PNl2bbtFDS-b7vucJSmMRfFHfvJdjzn7g8qJVjTmgZp9g-ysxKaN*3GcD1CgDGGnjd1BOPSiI24GTkl65BQCj0w88ZDE3lgu2Zs-E3zrxggNMNTRWxHuI6z5zQKO*n3Jzd9qMTFL*phGQVZ1NdlWoFkv1bVR5*-JjJ*O0IiQjenuHgpWann3nKnVtn7Xmk2UOKea4pJljxuP4N0JWJJFotJpRE9vz0UQDAPEAQTeuJ6EAc5CtdVGEA07nKsb*sHnsRegA__
     */
    public static SlamBallUserBean getInstance() {
        if (instance == null) {
             SlamBallUserBean cacheObj = ObjectsSerializableUtils.getCacheObj(AppContacts.USER_SERIALIZE_NAME);
            instance = cacheObj;
            if (instance != null) {
               LOG.e(instance.toString()+"---------------read    ");
            }
            return instance;
        } else {
            return instance;
        }
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setInstance(SlamBallUserBean instance) {
        SlamBallUserBean.instance = instance;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String Area) {
        this.Area = Area;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String AreaName) {
        this.AreaName = AreaName;
    }

    public String getAuthDate() {
        return AuthDate;
    }

    public void setAuthDate(String AuthDate) {
        this.AuthDate = AuthDate;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public int getExperience() {
        return Experience;
    }

    public void setExperience(int Experience) {
        this.Experience = Experience;
    }

    public String getFacebook() {
        return Facebook;
    }

    public void setFacebook(String Facebook) {
        this.Facebook = Facebook;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getHeaderImg() {
        return HeaderImg;
    }

    public void setHeaderImg(String HeaderImg) {
        this.HeaderImg = HeaderImg;
    }

    public boolean isIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    public String getJPushCode() {
        return JPushCode;
    }

    public void setJPushCode(String JPushCode) {
        this.JPushCode = JPushCode;
    }

    public String getLastLoginIp() {
        return LastLoginIp;
    }

    public void setLastLoginIp(String LastLoginIp) {
        this.LastLoginIp = LastLoginIp;
    }

    public String getLastLoginTime() {
        return LastLoginTime;
    }

    public void setLastLoginTime(String LastLoginTime) {
        this.LastLoginTime = LastLoginTime;
    }

    public String getMicroBlog() {
        return MicroBlog;
    }

    public void setMicroBlog(String MicroBlog) {
        this.MicroBlog = MicroBlog;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo) {
        this.MobileNo = MobileNo;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String Province) {
        this.Province = Province;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(String RegisterDate) {
        this.RegisterDate = RegisterDate;
    }

    public int getSlamBallUserID() {
        return SlamBallUserID;
    }

    public void setSlamBallUserID(int SlamBallUserID) {
        this.SlamBallUserID = SlamBallUserID;
    }

    public double getSlamCoins() {
        return SlamCoins;
    }

    public void setSlamCoins(double SlamCoins) {
        this.SlamCoins = SlamCoins;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String Street) {
        this.Street = Street;
    }

    public String getUserAccount() {
        return UserAccount;
    }

    public void setUserAccount(String UserAccount) {
        this.UserAccount = UserAccount;
    }

    public int getUserAuth() {
        return UserAuth;
    }

    public void setUserAuth(int UserAuth) {
        this.UserAuth = UserAuth;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public int getUserInfoID() {
        return UserInfoID;
    }

    public void setUserInfoID(int UserInfoID) {
        this.UserInfoID = UserInfoID;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String UserPassword) {
        this.UserPassword = UserPassword;
    }

    public String getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(String UserProfile) {
        this.UserProfile = UserProfile;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String Uuid) {
        this.Uuid = Uuid;
    }

    public String getWeChat() {
        return WeChat;
    }

    public void setWeChat(String WeChat) {
        this.WeChat = WeChat;
    }

    public String getMemberLevelLogo() {
        return MemberLevelLogo;
    }

    public void setMemberLevelLogo(String MemberLevelLogo) {
        this.MemberLevelLogo = MemberLevelLogo;
    }

    public String getUserLevelDescn() {
        return UserLevelDescn;
    }

    public void setUserLevelDescn(String UserLevelDescn) {
        this.UserLevelDescn = UserLevelDescn;
    }

    public String getUserLevelLogo() {
        return UserLevelLogo;
    }

    public void setUserLevelLogo(String UserLevelLogo) {
        this.UserLevelLogo = UserLevelLogo;
    }

    public String getVIPLevel() {
        return VIPLevel;
    }

    public void setVIPLevel(String VIPLevel) {
        this.VIPLevel = VIPLevel;
    }

    public String getTencentSign() {
        return TencentSign;
    }

    public void setTencentSign(String TencentSign) {
        this.TencentSign = TencentSign;
    }

    @Override
    public String toString() {
        return "SlamBallUserBean{" +
                "Area='" + Area + '\'' +
                ", AreaName='" + AreaName + '\'' +
                ", AuthDate='" + AuthDate + '\'' +
                ", Birthday='" + Birthday + '\'' +
                ", City='" + City + '\'' +
                ", CityName='" + CityName + '\'' +
                ", Country='" + Country + '\'' +
                ", CountryName='" + CountryName + '\'' +
                ", Email='" + Email + '\'' +
                ", Experience=" + Experience +
                ", Facebook='" + Facebook + '\'' +
                ", Gender='" + Gender + '\'' +
                ", HeaderImg='" + HeaderImg + '\'' +
                ", IsDeleted=" + IsDeleted +
                ", JPushCode='" + JPushCode + '\'' +
                ", LastLoginIp='" + LastLoginIp + '\'' +
                ", LastLoginTime='" + LastLoginTime + '\'' +
                ", MicroBlog='" + MicroBlog + '\'' +
                ", MobileNo='" + MobileNo + '\'' +
                ", NickName='" + NickName + '\'' +
                ", Province='" + Province + '\'' +
                ", QQ='" + QQ + '\'' +
                ", RegisterDate='" + RegisterDate + '\'' +
                ", SlamBallUserID=" + SlamBallUserID +
                ", SlamCoins=" + SlamCoins +
                ", StateName='" + StateName + '\'' +
                ", Street='" + Street + '\'' +
                ", UserAccount='" + UserAccount + '\'' +
                ", UserAuth=" + UserAuth +
                ", UserCode='" + UserCode + '\'' +
                ", UserInfoID=" + UserInfoID +
                ", UserPassword='" + UserPassword + '\'' +
                ", UserProfile='" + UserProfile + '\'' +
                ", Uuid='" + Uuid + '\'' +
                ", WeChat='" + WeChat + '\'' +
                ", JudgeFirstLogin=" + JudgeFirstLogin +
                ", JudgeMember=" + JudgeMember +
                ", JudgeSetPwd=" + JudgeSetPwd +
                ", MemberLevelLogo='" + MemberLevelLogo + '\'' +
                ", UserLevelDescn='" + UserLevelDescn + '\'' +
                ", UserLevelLogo='" + UserLevelLogo + '\'' +
                ", VIPLevel='" + VIPLevel + '\'' +
                ", TencentSign='" + TencentSign + '\'' +
                ", IdentityCode='" + IdentityCode + '\'' +
                '}';
    }

    public void writeObj() {
        LOG.e(toString()+"---------------write    ");
        ObjectsSerializableUtils.serializeObj(this,AppContacts.USER_SERIALIZE_NAME);
    }

    public String getIdentityCode() {
        return IdentityCode;
    }

    public void setIdentityCode(String IdentityCode) {
        this.IdentityCode = IdentityCode;
    }

    public boolean isJudgeFirstLogin() {
        return JudgeFirstLogin;
    }

    public void setJudgeFirstLogin(boolean JudgeFirstLogin) {
        this.JudgeFirstLogin = JudgeFirstLogin;
    }

    public boolean isJudgeMember() {
        return JudgeMember;
    }

    public void setJudgeMember(boolean JudgeMember) {
        this.JudgeMember = JudgeMember;
    }

    public boolean isJudgeSetPwd() {
        return JudgeSetPwd;
    }

    public void setJudgeSetPwd(boolean JudgeSetPwd) {
        this.JudgeSetPwd = JudgeSetPwd;
    }

    public String getIsTeacher() {
        return IsTeacher;
    }

    public void setIsTeacher(String IsTeacher) {
        this.IsTeacher = IsTeacher;
    }
}
