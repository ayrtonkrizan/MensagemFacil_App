package com.mobservice.mensagemfacil;

import java.util.ArrayList;

/**
 * Created by ayrtonkrizan on 23/08/18.
 */

public class Messages {
    private String text;
    private ArrayList<String> phones;
    private boolean sent;

    public Messages(String text, ArrayList<String> phones, boolean sent){
        this.text = text;
        this.phones = phones;
        this.sent = sent;
    }

    public Messages(){

    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }


    @Override
    public String toString() {
        return "Messages{" +
                "text='" + text + '\'' +
                ", phones='" + phones + '\'' +
                ", sent=" + sent +
                '}';
    }

    public String getPhonesToSMS() {
        if(phones.size() == 0)
            return "";
        if(phones.size() == 1)
            return phones.get(0);
        String ret = "";
        for (String s : phones) {
            ret += s + "; ";
        }

        return ret.substring(0, ret.length()-2);
    }
}
