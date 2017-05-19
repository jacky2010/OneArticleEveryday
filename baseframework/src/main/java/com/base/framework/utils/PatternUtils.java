package com.base.framework.utils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/10.
 */
public class PatternUtils {
    //邮箱正则表达式
    private static Pattern emailPat = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    //手机号正则表达式
    //20160709 褚枫修改手机号匹配规则
    private static Pattern mobilePhonePat = Pattern.compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");

    private static Pattern stringPat=Pattern.compile("^[a-zA-Z0-9]{1,}$");
    /**
     * 验证数字和字母
     */
    public static boolean matchStr(String string){
        return stringPat.matcher(string).matches();
    }

    /**
     * 检验邮箱
     *
     * @param emailAddress
     * @return
     */
    public static boolean matcherEmail(String emailAddress) {
        return emailPat.matcher(emailAddress).matches();
    }

    /**
     * 检验移动电话
     * @param mobilephone
     * @return
     */
    public static boolean matcherMobilePhone(String mobilephone) {
        return mobilePhonePat.matcher(mobilephone).matches();
    }
}
