package com.java2nb.novel.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * @author xiongxiaoyang
 */
public class StringUtil {

    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->HELLO_WORLD
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：HELLO_WORLD->HelloWorld
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 获取字符串有效汉字
     * */
    public static String getChineseValidWord(String origStr){

        //可以替换大部分空白字符， 不限于空格 . 说明:\s 可以匹配空格、制表符、换页符等空白字符的其中任意一个
        origStr = origStr.replaceAll("\\s*","");

       /* //完全清除标点
        origStr = origStr.replaceAll("\\pP","");*/

        //清除所有符号,只留下字母 数字  汉字  共3类.
        origStr = origStr.replaceAll("[\\pP\\p{Punct}]","");

        //去除字母和数字
        origStr = origStr.replaceAll("[A-Za-z0-9]*","");

        return origStr;

    }

    /**
     * 获取字符串英文单词数量
     * */
    public static int getEnglishWordCount(String origStr){
        Pattern pattern = compile("\\b\\w+\\b");
        Matcher matcher = pattern.matcher(origStr);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;

    }

    /**
     * 获取字符串中文汉字数量
     * */
    public static int getChineseWordCount(String origStr){
        Pattern pattern = compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(origStr);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;

    }

    /**
     * 获取字符串有效数字数量
     * */
    public static int getNumberWordCount(String origStr){
        Pattern pattern = compile("\\d+");
        Matcher matcher = pattern.matcher(origStr);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;

    }

    /**
     * 获取字符串有效字数
     * */
    public static int getStrValidWordCount(String origStr){
        return getChineseWordCount(origStr) + getEnglishWordCount(origStr) + getNumberWordCount(origStr);

    }

    public static void main(String[] args) {
        String str = "Welcome to China. 你好呀！中国人，我是1123号程序员，     来给你服务23天. Hello Word";
        System.out.println(getChineseWordCount(str));
        System.out.println(getEnglishWordCount(str));
        System.out.println(getNumberWordCount(str));
    }


}
