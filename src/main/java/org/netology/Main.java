package org.netology;


import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    public static int three = 0;
    public static int four = 0;
    public static int five = 0;

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        Thread thread1 = new Thread(() -> {
            three = comparator(texts, 3);
        });
        Thread thread2 = new Thread(() -> {
            four = comparator(texts, 4);
        });
        Thread thread3 = new Thread(() -> {
            five = comparator(texts, 5);
        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();
        System.out.println("Красивых слов с длиной 3: " + three);
        System.out.println("Красивых слов с длиной 4: " + four);
        System.out.println("Красивых слов с длиной 5: " + five);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int comparator(String[] texts, int wordLength) {
//        LongAdder count = new LongAdder();
        AtomicInteger count = new AtomicInteger(0);
        StringBuilder string = new StringBuilder();
        String reverceStr = null;
        int prev = -1;
        for (String str : texts) {
            if (str.length() == wordLength)
                reverceStr = string.append(str).reverse().toString(); //проверка на длину слова
            else continue;
            if (str.equals(reverceStr)) { //если слово - палиндром
                count.getAndIncrement();
                continue;
            } else if (str.length() % 2 == 1 && str.charAt(str.length() / 2 + 1) == str.charAt(0)) { //если слова состоит только из 1 символа
                count.getAndIncrement();
                continue;
            } else {
                //условие: все последующие буквы слова больше или равны предыдущему
                for (int i : str.getBytes()) {
                    if (prev == -1) prev = i;
                    else if (prev >= i) {
                        prev = -1;
                        break;
                    }
                }
                if (prev != -1) count.getAndIncrement();
            }
            string.delete(0, string.length());
        }
        return count.get();
    }
}