package org.example.sdk.types.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomStringUtils {
    // 定义常用字符集
    private static final String NUMBERS = "0123456789";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALPHANUMERIC = NUMBERS + LOWERCASE_LETTERS + UPPERCASE_LETTERS;

    // 使用 SecureRandom 作为随机数生成器
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @param charSet 字符集
     * @return 随机字符串
     */
    public static String generate(int length, String charSet) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        if (charSet == null || charSet.isEmpty()) {
            throw new IllegalArgumentException("Character set must not be empty");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(charSet.length());
            sb.append(charSet.charAt(randomIndex));
        }
        return sb.toString();
    }

    /**
     * 生成随机字母数字字符串
     *
     * @param length 字符串长度
     * @return 随机字母数字字符串
     */
    public static String generateAlphanumeric(int length) {
        return generate(length, ALPHANUMERIC);
    }

    /**
     * 生成随机数字字符串
     *
     * @param length 字符串长度
     * @return 随机数字字符串
     */
    public static String generateNumeric(int length) {
        return generate(length, NUMBERS);
    }

    /**
     * 生成随机字母字符串
     *
     * @param length 字符串长度
     * @return 随机字母字符串
     */
    public static String generateAlphabetic(int length) {
        return generate(length, LOWERCASE_LETTERS + UPPERCASE_LETTERS);
    }

    /**
     * 生成随机包含特殊字符的字符串
     *
     * @param length 字符串长度
     * @return 随机包含特殊字符的字符串
     */
    public static String generateWithSpecialChars(int length) {
        return generate(length, ALPHANUMERIC + SPECIAL_CHARACTERS);
    }
}
