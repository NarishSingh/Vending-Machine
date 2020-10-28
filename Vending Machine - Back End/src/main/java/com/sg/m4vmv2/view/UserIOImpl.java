package com.sg.m4vmv2.view;

import java.util.Scanner;

/**
 * Implementation of UserIO interface
 *
 * @author Narish
 */
public class UserIOImpl implements UserIO {

    final private Scanner input = new Scanner(System.in);

    /**
     * Print a message
     *
     * @param message {String} message to be printed to console
     */
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    /**
     * Print a prompt and read line
     *
     * @param prompt {String} prompt to be printed to console
     * @return {String} user's String
     */
    @Override
    public String readString(String prompt) {
        System.out.print(prompt);
        String userInput = input.nextLine();

        return userInput;
    }

    /**
     * Print a prompt and read in an integer
     *
     * @param prompt {String} prompt to be printed to console
     * @return {int} user's integer
     */
    @Override
    public int readInt(String prompt) {
        System.out.print(prompt);
        int userInput = Integer.parseInt(input.nextLine());

        return userInput;
    }

    /**
     * Print a prompt and read in an integer, accepting only if it is within
     * specified range
     *
     * @param prompt {String} prompt to be printed to console
     * @param min    {int} minimum integer for acceptable inputs
     * @param max    {int} maximum integer for acceptable inputs
     * @return {int} user's integer in range
     */
    @Override
    public int readInt(String prompt, int min, int max) {
        int userInput;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter integer: ");
            userInput = Integer.parseInt(input.nextLine());
        } while (userInput < min || userInput > max);

        return userInput;
    }

    /**
     * Print a prompt and read in a double
     *
     * @param prompt {String} prompt to be printed to console
     * @return {double} user's double
     */
    @Override
    public double readDouble(String prompt) {
        System.out.print(prompt);
        double userInput = Double.parseDouble(input.nextLine());

        return userInput;
    }

    /**
     * Print a prompt and read in a double, accepting only if it is within
     * specified range
     *
     * @param prompt {String} prompt to be printed to console
     * @param min    {double} minimum double for acceptable inputs
     * @param max    {double} maximum double for acceptable inputs
     * @return {double} user's double in range
     */
    @Override
    public double readDouble(String prompt, double min, double max) {
        double userInput;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter double: ");
            userInput = Double.parseDouble(input.nextLine());
        } while (userInput < min || userInput > max);

        return userInput;
    }

    /**
     * Print a prompt and read in a float
     *
     * @param prompt {String} prompt to be printed to console
     * @return {float} user's float
     */
    @Override
    public float readFloat(String prompt) {
        System.out.print(prompt);
        float userInput = Float.parseFloat(input.nextLine());

        return userInput;
    }

    /**
     * Print a prompt and read in a float, accepting only if it is within
     * specified range
     *
     * @param prompt {String} prompt to be printed to console
     * @param min    {float} minimum float for acceptable inputs
     * @param max    {float} maximum float for acceptable inputs
     * @return {float} user's float in range
     */
    @Override
    public float readFloat(String prompt, float min, float max) {
        float userInput;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter float: ");
            userInput = Float.parseFloat(input.nextLine());
        } while (userInput < min || userInput > max);

        return userInput;
    }

    /**
     * Print a prompt and read in a long
     *
     * @param prompt {String} prompt to be printed to console
     * @return {long} user's long
     */
    @Override
    public long readLong(String prompt) {
        System.out.print(prompt);
        long userInput = Long.parseLong(input.nextLine());

        return userInput;
    }

    /**
     * Print a prompt and read in a long, accepting only if it is within
     * specified range
     *
     * @param prompt {String} prompt to be printed to console
     * @param min    {long} minimum long for acceptable inputs
     * @param max    {long} maximum long for acceptable inputs
     * @return {long} user's long in range
     */
    @Override
    public long readLong(String prompt, long min, long max) {
        long userInput;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter long: ");
            userInput = Long.parseLong(input.nextLine());
        } while (userInput < min || userInput > max);

        return userInput;
    }

}
