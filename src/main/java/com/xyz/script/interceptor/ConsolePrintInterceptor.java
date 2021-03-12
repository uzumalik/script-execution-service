package com.xyz.script.interceptor;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This class is responsible to intercept all the console prints and storing it
 * in the passed StringBuilder
 *
 * @author Mohammad Uzair
 */
public class ConsolePrintInterceptor extends PrintStream {

    private StringBuilder builder;


    public ConsolePrintInterceptor(OutputStream out, StringBuilder builder)
    {
        super(out, true);
        this.builder = builder;
    }
    @Override
    public void print(String s)
    {
        super.print(s);
        builder.append(s);
    }
}
