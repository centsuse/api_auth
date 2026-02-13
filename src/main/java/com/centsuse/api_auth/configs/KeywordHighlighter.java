package com.centsuse.api_auth.configs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.Comparator;

public class KeywordHighlighter {

    /**
     * 将文档中的关键字替换为带<em>标签的格式
     * @param document 原始文档内容
     * @param keywords 关键字数组
     * @return 替换后的文档内容
     */
    public static String highlightKeywords(String document, String[] keywords) {
        if (document == null || keywords == null || keywords.length == 0) {
            return document;
        }

        // 按长度降序排序，避免短关键字在长关键字之前匹配（比如先匹配"Java"再匹配"JavaScript"）
        String[] sortedKeywords = Arrays.copyOf(keywords, keywords.length);
        Arrays.sort(sortedKeywords, Comparator.comparingInt(String::length).reversed());

        // 构建正则表达式模式，不区分大小写，使用单词边界确保完整单词匹配
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("\\b(");

        for (int i = 0; i < sortedKeywords.length; i++) {
            if (i > 0) {
                patternBuilder.append("|");
            }
            // 对关键字进行转义，防止正则表达式特殊字符干扰
            patternBuilder.append(Pattern.quote(sortedKeywords[i]));
        }
        patternBuilder.append(")\\b");

        Pattern pattern = Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(document);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            // 保留原始大小写，用<em>标签包裹匹配到的关键字
            String matchedWord = matcher.group();
            String replacement = "<em>" + matchedWord + "</em>";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 更严格的方法，只匹配完全相同的单词（包括大小写）
     * @param document 原始文档内容
     * @param keywords 关键字数组
     * @return 替换后的文档内容
     */
    public static String highlightKeywordsExact(String document, String[] keywords) {
        if (document == null || keywords == null || keywords.length == 0) {
            return document;
        }

        String[] sortedKeywords = Arrays.copyOf(keywords, keywords.length);
        Arrays.sort(sortedKeywords, Comparator.comparingInt(String::length).reversed());

        String result = document;
        for (String keyword : sortedKeywords) {
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 直接替换，保持原始大小写
                result = result.replace(keyword, "<em>" + keyword + "</em>");
            }
        }

        return result;
    }

    // 测试示例
    public static void main(String[] args) throws IOException {
        String document = new String(Files.readAllBytes(Paths.get("F:\\workspace\\api_auth\\src\\main\\java\\com\\centsuse\\api_auth\\configs\\document.txt")));
        String[] keywords = {"西游记", "行者", "猪八戒", "西游"};

        System.out.println("原始文档：");
//        System.out.println(document);
        long old = System.currentTimeMillis();

        System.out.println("\n高亮后（不区分大小写）：" + old);
        for (int i = 0; i < 100; i++) {
            highlightKeywords(document, keywords);
        }
        System.out.println("\n高亮后（区分大小写）：" + (System.currentTimeMillis() - old));
//        System.out.println(highlightKeywordsExact(document, keywords));

        // 测试包含特殊字符的情况
//        String document2 = "这个问题涉及C++和C#编程。需要小心处理。";
//        String[] keywords2 = {"C++", "C#"};
//
//        System.out.println("\n特殊字符测试：");
//        System.out.println("原始：" + document2);
//        System.out.println("高亮：" + highlightKeywords(document2, keywords2));
    }
}