package com.newlabs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Roulette {
    private static final String DONE = "done";
    private static final String FILENAME = "numbers.txt";
    private static final int MAX_OUTPUT = 20;
    private Scanner scanner = new Scanner(System.in);
    private List<Number> numberHistory = new ArrayList<>();

    public static void main(String[] args) {
        Roulette roulette = new Roulette();

        roulette.start();
    }

    private void start() {
        getNumbers();
        System.out.println("ROULETTE -- ");
        System.out.println("Enter number: (done when complete)");
        while (scanner.hasNext()) {
            String input = scanner.next();

            if (DONE.equalsIgnoreCase(input)) {saveNumbers();return;}
            try {
                if (Integer.valueOf(input) > 36) {
                    System.out.println("Invalid entry.");
                    continue;
                }
            } catch (Exception ex) {
                System.out.println("Invalid entry.");
                continue;
            }

            Number selectedNumber = new Number(input);
            numberHistory.add(selectedNumber);
            clearScreen();
            printLast();
            printStats();
            System.out.println("Enter number: (done when complete)");
        }
    }

    private void printLast() {
        int numbersPrinted = 0;
        for (int i = numberHistory.size() - 1; i >= 0; i--) {
            if (numbersPrinted++ == MAX_OUTPUT) {break;}
            Number num = numberHistory.get(i);
            System.out.println(String.format("%d: %3s\t%6s\t\t12: %6s\tCol: %6s", numbersPrinted, num.number, num.color, num.block, num.column));
        }
        System.out.println("");
    }

    private void printStats() {
        double firstBlock = 0;
        double secondBlock = 0;
        double thirdBlock = 0;

        double firstCol = 0;
        double secondCol = 0;
        double thirdCol = 0;

        double even = 0;
        double odd = 0;

        double green = 0;

        int size = numberHistory.size();

        for (Number num: numberHistory) {
            switch (num.block) {
                case Green:
                    green++;
                    break;
                case First:
                    firstBlock++;
                    break;
                case Second:
                    secondBlock++;
                    break;
                case Third:
                    thirdBlock++;
            }

            switch (num.column) {
                case First:
                    firstCol++;
                    break;
                case Second:
                    secondCol++;
                    break;
                case Third:
                    thirdCol++;
            }

            switch (num.even) {
                case 0:
                    odd++;
                    break;
                case 1:
                    even++;
            }
        }

        double greenpercent = green / size * 100;
        double firstpercent = firstBlock / size * 100;
        double secondpercent = secondBlock / size * 100;
        double thirdpercent = thirdBlock / size * 100;

        System.out.println("Block statistics: ");
        String output = String.format("Green: %.1f\tFirst: %.1f\tSecond: %.1f\tThird: %.1f", greenpercent, firstpercent, secondpercent, thirdpercent);
        System.out.println(output);
        System.out.println("");

        firstpercent = firstCol / size * 100;
        secondpercent = secondCol / size * 100;
        thirdpercent = thirdCol / size * 100;

        System.out.println("Column statistics: ");
        output = String.format("Green: %.1f\tFirst: %.1f\tSecond: %.1f\tThird: %.1f", greenpercent, firstpercent, secondpercent, thirdpercent);
        System.out.println(output);
        System.out.println("");

        firstpercent = even / size * 100;
        secondpercent = odd / size * 100;

        System.out.println("Even statistics: ");
        output = String.format("Green: %.1f\tEven: %.1f\tOdd: %.1f", greenpercent, firstpercent, secondpercent);
        System.out.println(output);
        System.out.println("");
    }

    private void saveNumbers() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILENAME));
            for (Number number: numberHistory) {
                bufferedWriter.write(number.number);
                bufferedWriter.write("\n");
            }

            bufferedWriter.close();
        } catch (Exception ex) {
            System.out.println("Unable to save previous numbers.");
        }
    }

    private void getNumbers() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FILENAME));
            String number;
            String numberAcc = "";

            while ((number = bufferedReader.readLine()) != null) {
                numberAcc += number + ",";
            }

            String[] numbers = numberAcc.split(",");

            for (String num: numbers) {
                numberHistory.add(new Number(num));
            }

            bufferedReader.close();
        } catch (Exception ex) {
            System.out.println("Unable to retrieve previous numbers.");
        }
    }

    private void clearScreen() {
        for (int i = 0; i < 30; i++) {
            System.out.println("");
        }
    }

    private class Number {
        String number;
        Color color;
        Column column;
        int even;
        Block block;

        Number(String number) {
            this.number = number;

            switch (number) {
                case "0":
                case "00":
                    this.color = Color.Green;
                    break;
                case "1":
                case "3":
                case "5":
                case "7":
                case "9":
                case "12":
                case "14":
                case "16":
                case "18":
                case "19":
                case "21":
                case "23":
                case "25":
                case "27":
                case "30":
                case "32":
                case "34":
                case "36":
                    this.color = Color.Red;
                    break;
                default:
                    this.color = Color.Black;
            }

            switch (number) {
                case "0":
                case "00":
                    this.column = Column.Green;
                    break;
                case "1":
                case "4":
                case "7":
                case "10":
                case "13":
                case "16":
                case "19":
                case "22":
                case "25":
                case "28":
                case "31":
                case "34":
                    this. column = Column.First;
                    break;
                case "2":
                case "5":
                case "8":
                case "11":
                case "14":
                case "17":
                case "20":
                case "23":
                case "26":
                case "29":
                case "32":
                case "35":
                    this.column = Column.Second;
                    break;
                default:
                    this.column = Column.Third;
            }

            int value = Integer.valueOf(number);
            if (value == 0) {
                this.block = Block.Green;
            } else if (value >= 1 && value <= 12) {
                this.block = Block.First;
            } else if (value >= 13 && value <= 24) {
                this.block = Block.Second;
            } else {
                this.block = Block.Third;
            }

            if (value == 0) {
                this.even = 2;
            } else {
                this.even = 1 - (value % 2);
            }
        }
    }

    private enum Color {
        Green,
        Red,
        Black
    }

    private enum Block {
        Green,
        First,
        Second,
        Third
    }

    private enum Column {
        Green,
        First,
        Second,
        Third
    }
}
