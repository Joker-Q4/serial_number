package com.joker.plugin.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;

import java.util.regex.Pattern;

/**
 * @author Joker
 * @since 2021/08/18
 */
public class ValidationUtil {

    /**
     * 判断文档是否可写
     * @param editor 编辑器
     * @return 结果
     */
    public static ValidationInfo isWritable(Editor editor){
        if (!editor.getDocument().isWritable()) {
            return new ValidationInfo(StringMessage.CANNOT_BE_WRITTEN);
        }
        return null;
    }

    /***
     * 判断是否为空字符串
     * @param text 字符串数组
     * @return 结果
     */
    public static boolean hasText(String... text){
        if(text == null){
            Messages.showMessageDialog(StringMessage.CANNOT_BE_BLANK, StringMessage.ERROR_TITLE, Messages.getInformationIcon());
            return false;
        }
        for (String str : text) {
            if (!StringUtils.hasText(str)) {
                Messages.showMessageDialog(StringMessage.CANNOT_BE_BLANK, StringMessage.ERROR_TITLE, Messages.getInformationIcon());
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为Long类型
     * @param str 字符串
     * @return 结果
     */
    public static boolean beLongType(String... str){
        if(str == null){
            Messages.showMessageDialog(StringMessage.TYPE_LONG, StringMessage.ERROR_TITLE, Messages.getInformationIcon());
            return false;
        }
        for (String s : str) {
            if(!isIntegerOrLong(s)){
                Messages.showMessageDialog(StringMessage.TYPE_LONG, StringMessage.ERROR_TITLE, Messages.getInformationIcon());
                return false;
            }
        }
        return true;
    }

    /**
     * 正则匹配是否全部为数字 允许为复数
     * @param str 字符串
     * @return 结果
     */
    private static boolean isIntegerOrLong(String str) {
        if (null == str || str.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^-?\\d+$");
        return pattern.matcher(str).matches();
    }
}
