package com.company.singleton;

public class singletonn {
    private static singletonn instance;


    private  singletonn()
    {
    }

    private static class handler {
        public static singletonn getInstance()
        {
            if (instance == null) {
                instance = new singletonn();
            }
            return instance;
        }
    }
    public static singletonn getInstance()
    {
        return handler.getInstance();
    }
}
