package com.example.sources.domain;

public enum OrderState {
    NEW,
    ACTIVE,
    PACKING,
    PROBLEM,
    DONE,
    HALF_DONE,
    CANCELLED;

    public static OrderState fromString(String str){
        OrderState result = NEW;
        if(str.equals("ACTIVE"))
            result = ACTIVE;
        if(str.equals("PACKING"))
            result = PACKING;
        if(str.equals("PROBLEM"))
            result = PROBLEM;
        if(str.equals("DONE"))
            result = DONE;
        if(str.equals("HALF_DONE"))
            result = HALF_DONE;
        if(str.equals("CANCELLED"))
            result = CANCELLED;
        return result;
    }
}
