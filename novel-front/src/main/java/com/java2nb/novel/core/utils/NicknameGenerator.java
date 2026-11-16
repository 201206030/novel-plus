package com.java2nb.novel.core.utils;

import java.util.concurrent.ThreadLocalRandom;

public class NicknameGenerator {

    private static final String[] ADJECTIVES = {
        "快乐", "可爱", "温柔", "勇敢", "聪明",
        "阳光", "安静", "自由", "幸运", "慵懒",
        "调皮", "神秘", "浪漫", "清新", "善良"
    };

    private static final String[] NOUNS = {
        "小猫", "小狗", "兔子", "熊猫", "狐狸",
        "星星", "月亮", "云朵", "橘子", "柠檬",
        "鲸鱼", "海豚", "松鼠", "布丁", "奶茶"
    };

    public static String generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];

        return adjective + noun + random.nextInt(1000, 10000);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generate());
        }
    }
}
