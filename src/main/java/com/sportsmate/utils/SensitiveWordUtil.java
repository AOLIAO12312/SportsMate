package com.sportsmate.utils;

import java.util.HashSet;
import java.util.Set;

public class SensitiveWordUtil {
    private static final Set<String> FORBIDDEN_KEYWORDS = new HashSet<>();

    static {
        // 初始化违禁关键词，可根据实际情况添加更多
        // 我看违禁词大全写的，和我以及组内其他成员没有任何关系（）
        FORBIDDEN_KEYWORDS.add("傻逼");
        FORBIDDEN_KEYWORDS.add("二货");
        FORBIDDEN_KEYWORDS.add("赌");
        FORBIDDEN_KEYWORDS.add("骚");
        FORBIDDEN_KEYWORDS.add("贱");
        FORBIDDEN_KEYWORDS.add("涩");
        FORBIDDEN_KEYWORDS.add("A片");
        FORBIDDEN_KEYWORDS.add("操你");
        FORBIDDEN_KEYWORDS.add("操死你");
        FORBIDDEN_KEYWORDS.add("CN");
        FORBIDDEN_KEYWORDS.add("系统消息");
        FORBIDDEN_KEYWORDS.add("温馨提示");
        FORBIDDEN_KEYWORDS.add("18");
        FORBIDDEN_KEYWORDS.add("成人");
        FORBIDDEN_KEYWORDS.add("淫乱");
        FORBIDDEN_KEYWORDS.add("奸");
        FORBIDDEN_KEYWORDS.add("中出");
        FORBIDDEN_KEYWORDS.add("学潮");
        FORBIDDEN_KEYWORDS.add("挡坦克");
        FORBIDDEN_KEYWORDS.add("六四");
        FORBIDDEN_KEYWORDS.add("64");
        FORBIDDEN_KEYWORDS.add("89");
        FORBIDDEN_KEYWORDS.add("八九");
        FORBIDDEN_KEYWORDS.add("民运");
        FORBIDDEN_KEYWORDS.add("AV");
        FORBIDDEN_KEYWORDS.add("fuck");
        FORBIDDEN_KEYWORDS.add("noob");
        FORBIDDEN_KEYWORDS.add("bitch");
    }

    public static boolean containsForbiddenKeyword(String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }
        // 去除输入内容中的空格并转换为小写
        String processedContent = content.replaceAll("\\s+", "").toLowerCase();
        for (String keyword : FORBIDDEN_KEYWORDS) {
            // 去除关键词中的空格并转换为小写
            String processedKeyword = keyword.replaceAll("\\s+", "").toLowerCase();
            if (processedContent.contains(processedKeyword)) {
                return true;
            }
        }
        return false;
    }
}
