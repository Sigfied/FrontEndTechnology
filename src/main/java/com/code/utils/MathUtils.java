package com.code.utils;

/**
 * 随机数生成工具类
 * @Description: 随机数生成工具类
 * @author: GYJ
 * @date: 2022/6/22 12:38
 */
public class MathUtils {

    private static final String DEFAULT_DIGITS =                    "0";
    private static final String FIRST_DEFAULT_DIGITS =              "1";

    /**
     * @param target    目标数字
     * @param length    需要补充到的位数, 补充默认数字[0], 第一位默认补充[1]
     * @return          补充后的结果
     */
    public static String makeUpNewData(String target, int length){
        return makeUpNewData(target, length, DEFAULT_DIGITS);
    }

    /**
     * @param target    目标数字
     * @param length    需要补充到的位数
     * @param add       需要补充的数字, 补充默认数字[0], 第一位默认补充[1]
     * @return          补充后的结果
     */
    public static String makeUpNewData(String target, int length, String add){
        if(target.startsWith("-")) {
            target.replace("-", "");
        }
        if(target.length() >= length) {
            return target.substring(0, length);
        }
        StringBuffer sb = new StringBuffer(FIRST_DEFAULT_DIGITS);
        for (int i = 0; i < length - (1 + target.length()); i++) {
            sb.append(add);
        }
        return sb.append(target).toString();
    }

    /**
     * 生产一个随机的指定位数的字符串数字
     * @param length 传7
     * @return 返回字符串
     */
    public static String randomDigitNumber(int length){
        //1000+8999=9999
        int start = Integer.parseInt(makeUpNewData("", length));
        //9000
        int end = Integer.parseInt(makeUpNewData("", length + 1)) - start;
        return (int)(Math.random() * end) + start + "";
    }

    public static String getPrimaryKey(){
        //随机7位数
        return MathUtils.makeUpNewData(Thread.currentThread().hashCode()+"", 3)+   MathUtils.randomDigitNumber(7);
    }

}