/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gustavolessa.blockchain.menu;

import com.gustavolessa.blockchain.services.data.DataValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract Menu class
 */
public abstract class Menu {

    protected String title;
    protected List<String> options;
    protected boolean exit;
    protected Scanner in;

    /**
     * Menu constructor that takes a ZooData as argument.
     */
    public Menu() {
        this.title = "Menu";
        this.options = new ArrayList<>();
        this.in = new Scanner(System.in);
    }

    public abstract void optionSelector();

    /**
     * This method displays and activate the menu option selector.
     * While the exit option is not chosen it will keep the menu on a loop.
     */
    public final void startMenu() {
        if (!this.exit) {
            this.displayMenu();
            this.optionSelector();
        }
    }

    public void exit() {
        this.exit = true;
    }

    /**
     * This method sets the menu's options
     *
     * @param options (String[])
     */
    public final void setOptions(String[] options) {
        for (String s : options) this.options.add(s);
    }


    /**
     * This method displays the menu options on CLI.
     */
    private final void displayMenu() {
        System.out.println();
        System.out.println("+-----------------------------------------------------------+");
    System.out.println("|               "+this.title+"               |");
        System.out.println("+-----------------------------------------------------------+");

        // for (int i = 0; i < this.title.length(); i++) System.out.print("-");
        //System.out.println();
        for (int i = 0; i < this.options.size(); i++) {
            System.out.println(this.options.get(i));
        }

        System.out.println("+-----------------------------------------------------------+");

    }

    /**
     * This method sets the menu's title
     *
     * @param title
     */
    public final void setTitle(String title) {
        this.title = title;
    }
}
