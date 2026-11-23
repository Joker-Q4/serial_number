package com.joker.plugin.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;

/**
 * @author Joker
 * @since 2021-08-18
 */
public class ValidationUtil {

    /**
     * 判断文档是否可写
     * @param editor 编辑器
     * @return 错误信息, 可写时返回 null
     */
    public static String isWritable(Editor editor){
        if (!editor.getDocument().isWritable()) {
            return StringMessage.CANNOT_BE_WRITTEN;
        }
        return null;
    }

    /***
     * 判断是否为空字符串
     * @param text 字符串数组
     * @return 结果
     */
    public static boolean hasText(String... text){
        for (String str : text) {
            if (!StringUtils.hasText(str)) {
                Messages.showMessageDialog(StringMessage.CANNOT_BE_BLANK, StringMessage.ERROR_TITLE, Messages.getWarningIcon());
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为合法的 Long 值(含范围校验)
     * @param str 字符串
     * @return 结果
     */
    public static boolean isLongType(String... str){
        for (String s : str) {
            try {
                Long.parseLong(s);
            } catch (NumberFormatException e) {
                Messages.showMessageDialog(StringMessage.TYPE_LONG, StringMessage.ERROR_TITLE, Messages.getWarningIcon());
                return false;
            }
        }
        return true;
    }
}
