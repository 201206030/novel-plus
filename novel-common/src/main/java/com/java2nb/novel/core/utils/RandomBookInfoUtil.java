package com.java2nb.novel.core.utils;

import java.util.Random;

/**
 * 随机生成小说信息工具类
 * @author Administrator
 */
public class RandomBookInfoUtil {

    /**
     * 根据评分获取访问人数
     * */
    public static Long getVisitCountByScore(Float score){
        Long visitCount ;

        if(score > 9){
            visitCount = 100000 + new Random(100000).nextLong();
        }else if(score > 8){
            visitCount = 10000 + new Random(10000).nextLong();
        }else if(score > 7){
            visitCount = 1000 + new Random(1000).nextLong();
        }else if(score > 6){
            visitCount = 100 + new Random(100).nextLong();
        }else{
            visitCount = new Random(100).nextLong();
        }


        return  visitCount;

    }

    /**
     * 根据访问人数获取评分
     * */
    public static Float getScoreByVisitCount(Long visitCount){
        Float score;
        if(visitCount>100000) {
            score = 8.9f;
        }else if(visitCount>10000){
            score = 8.0f+(visitCount/10000)*0.1f;
        }else if(visitCount>1000){
            score = 7.0f+(visitCount/1000)*0.1f;
        }else if(visitCount>100){
            score = 6.0f+(visitCount/100)*0.1f;
        }else{
            score = 6.0f;
        }
        return score;
    }
    /**
     * 获取分类名
     * */
    public static String getCatNameById(Integer catId) {
        String catName = "其他";

        switch (catId) {
            case 1: {
                catName = "玄幻奇幻";
                break;
            }
            case 2: {
                catName = "武侠仙侠";
                break;
            }
            case 3: {
                catName = "都市言情";
                break;
            }
            case 4: {
                catName = "历史军事";
                break;
            }
            case 5: {
                catName = "科幻灵异";
                break;
            }
            case 6: {
                catName = "网游竞技";
                break;
            }
            case 7: {
                catName = "女生频道";
                break;
            }
            case 8: {
                catName = "轻小说";
                break;
            }
            case 9: {
                catName = "漫画";
                break;
            }
            default: {
                break;
            }


        }
        return catName;
    }
}
