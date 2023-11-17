package org.netology;


import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    public static volatile AtomicInteger three = new AtomicInteger(0);
    public static volatile AtomicInteger four = new AtomicInteger(0);
    public static volatile AtomicInteger five = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        new Thread(() -> {
            checkChars(texts, 3, three);
        }).start();
        new Thread(() -> {
            checkChars(texts, 4, four);
        }).start();
        new Thread(() -> {
            checkChars(texts, 5, five);
        }).start();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void checkChars(String[] texts, int countChars, AtomicInteger value) {
        Thread threadThree1 = new Thread(() -> {
            palindrome(texts, countChars, value);
        });
        Thread threadThree2 = new Thread(() -> {
            oneChar(texts, countChars, value);
        });
        Thread threadThree3 = new Thread(() -> {
            increaseChar(texts, countChars, value);
        });

        threadThree1.start();
        threadThree2.start();
        threadThree3.start();
        try {
            threadThree1.join();
            threadThree2.join();
            threadThree3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Красивых слов с длиной " + countChars + ": " + value);
    }

    public static void palindrome(String[] texts, int wordLength, AtomicInteger value) {
        StringBuilder string = new StringBuilder();
        String reverceStr = null;
        for (String str : texts) {
            if (str.length() == wordLength)
                reverceStr = string.append(str).reverse().toString(); //проверка на длину слова
            else continue;
            if (str.equals(reverceStr)) { //если слово - палиндром
                value.getAndIncrement();
            }
        }
    }

    public static void oneChar(String[] texts, int wordLength, AtomicInteger value) {
        StringBuilder string = new StringBuilder();
        String reverceStr = null;
        for (String str : texts) {
            if (str.length() == wordLength)
                reverceStr = string.append(str).reverse().toString(); //проверка на длину слова
            else continue;
            if(str.chars().allMatch(x->x == 'a')) value.getAndIncrement();
        }
    }

    public static void increaseChar(String[] texts, int wordLength, AtomicInteger value) {
        int prev = -1;
        for (String str : texts) {
            for (int i : str.getBytes()) {
                if (prev == -1) prev = i;
                else if (prev >= i) {
                    prev = -1;
                    break;
                }
            }
            if (prev != -1) value.getAndIncrement();
        }
    }
}