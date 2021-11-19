package com.jcloud.common.util;

import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符

    public static List<String> technologyColorList = null;

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {

        if (StringUtils.isBlank(htmlStr)) {

            return "";
        }
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签


        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        htmlStr += " ...";
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String delHTMLTag2(String htmlStr) {

        if (StringUtils.isBlank(htmlStr)) {

            return "";
        }
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签


        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        return htmlStr.trim(); // 返回文本字符串
    }


    public static String delOnlyHTMLTag(String htmlStr) {

        if (StringUtils.isBlank(htmlStr)) {

            return "";
        }

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
        return htmlStr;
    }


    /**
     * 去掉html标签
     *
     * @param html
     * @return
     */
    public static String trimHtml(String html) {

        if (StringUtils.isBlank(html)) {
            return "";
        }
        return html.replaceAll("<([^>]*)>", "").replace("&nbsp;", " ").replaceAll("\\s*|\t|\r|\n", "");
    }

    /**
     * 去掉除了<font style=\"color:red\"></font>的html标签并高亮
     * <p>系列化的反恐弹药<font style="color:red">技术</font>，是利用烟火<font style="color:red">技术</font>原理研制出的防御</p>
     *
     * @param html
     * @return
     */
    public static String trimHtmlAndHightLight(String html) {

        if (StringUtils.isBlank(html)) {
            return "";
        }
        //	System.out.println("html="+html);
        String msg = html.replaceAll("<(?!font|/font).*?>", "").replaceAll("\t|\r|\n", "");
//		System.out.println("msg=="+msg);
        return msg;
        //	return html.replaceAll("/style=\"(.*)\"/","").replaceAll("\t|\r|\n", "");
    }


    public static String changeUrlAddress(String fromUrl) {

        //SSOProperties prop = SSOConfig.getSSOProperties();
        //String localDomain=prop.get("local.domain"); //local域名
        String localDomain = "";
        try {
            fromUrl = URLDecoder.decode(fromUrl, "utf-8");

            if (fromUrl.contains("/jyhigh_zone/artificialGroup")) {
                return localDomain + "/jyhigh_zone/logoutArtificialGroup";
            } else if (fromUrl.contains("/jyhigh_zone/invitation/")) {
                String[] str = fromUrl.split("/");
                return localDomain + "/jyhigh_zone/invitationeNoLogin/" + str[str.length - 1];
            } else if (fromUrl.contains("/jyhigh_zone/zone/")) {
                String[] str = fromUrl.split("/");
                return localDomain + "/jyhigh_zone/zoneNoLogin/" + str[str.length - 1];
            } else if (fromUrl.contains("/jyhigh_zone/fillInformation/")) {
                String[] str = fromUrl.split("/");
                return localDomain + "/jyhigh_zone/fillInformationNoLogin/" + str[str.length - 1];
            } else {
                return localDomain + "/jyhigh_zone/logoutArtificialGroup";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return localDomain + "/jyhigh_zone/logoutArtificialGroup";
            // TODO Auto-generated catch block

        }


    }

    /**
     * 去掉html标签,并且截取前len个字
     *
     * @param html
     * @return
     */
    public static String trimHtmlAndLeft(String html, int len) {

        if (html == null) return "";

        String text = HtmlUtil.trimHtml(html);

        if (text.length() > len) {

            return StringUtils.left(text, len) + "...";

        } else {
            return text;
        }


    }

    public static String checkStr(String str) {
        if (StringUtils.isBlank(str)) return "";
        return str.trim();
    }


    /**
     * 得到网页中图片的地址
     */
    public static String getImgStr(String html) {
        if (StringUtils.isBlank(html)) return "";
        List<String> pics = new ArrayList<String>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(html);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        if (pics != null && pics.size() > 0) {
            return pics.get(0);
        } else {
            return "";
        }
    }



    /**
     * 初始化中国地图省份数据
     *
     * @return
     */
    public static List<String> initTechnologyColorData() {
        if (technologyColorList == null) {
            technologyColorList = new ArrayList<String>() {
                private static final long serialVersionUID = 1L;

                {
                    add("#5d9cec");
                    add("#62c87f");
                    add("#f15755");
                    add("#fc863f");
                    add("#2178e9");
                    add("#90EE90");
                    add("#8B008B");
                    add("#FFC1C1");
                    add("#98FB98");
                    add("#191970");
                    add("#668B8B");
                    add("#8EE5EE");
                    add("#00868B");
                    add("#9BCD9B");
                    add("#54FF9F");
                    add("#2E8B57");
                    add("#00FF7F");
                    add("#00FF00");
                    add("#7FFF00");
                    add("#B0C4DE");
                    add("#AFEEEE");
                    add("#556B2F");
                    add("#32CD32");
                    add("#EEE8AA");
                    add("#FFFFE0");
                    add("#DAA520");
                    add("#8B4513");
                    add("#F4A460");

                }
            };

        }

        return technologyColorList;
    }


    /**
     * @param website 网址
     * @param html    把html中的图片的src添加前缀
     */
    public static String insertWebSiteForImg(String html, String website) {
        if (StringUtils.isBlank(html)) return "";
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(html);
        while (m_image.find()) {
            img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                String imgSrc = m.group(1);
                imgSrc = imgSrc.replaceAll("\"", "").replaceAll("\'", "");
                System.out.println("imgSrc=" + imgSrc);
                if (StringUtils.isNotBlank(imgSrc) && StringUtils.startsWith(imgSrc, "/")) {
                    html = html.replace(imgSrc, website + imgSrc);
                }
            }
        }

        return html;
    }


    public static String deleteChineseWord(String str) {
        str = str.replaceAll("-", "");
        if (StringUtils.isBlank(str)) return "";
        Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher mat = pat.matcher(str);

        String result = mat.replaceAll("");
        return result;
    }

    public static void main(String[] args) {
        String s = HtmlUtil.deleteChineseWord("50人民币");
        try {

            System.out.println(DateUtil.parseDateStrictly("2004-06-08 00:00:00", new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"}));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        System.out.println("s=" + s);
    }
}
