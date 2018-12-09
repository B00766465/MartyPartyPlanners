package com.michael;

class Room{
    private String name;
    private String capacity;
    private String feature;
    private String alcoholAllowed;

    public Room(String rm, String cap, String feat, String alco){
        name = rm;
        capacity = cap;
        feature = feat;
        alcoholAllowed = alco;
    }
    public String getRoomName(){return name;}
    public String getCapacity(){return capacity;}
    public String getFeature(){return feature;}
    public String getAlcoholAllowed(){return alcoholAllowed;}

    public String toString(){
        return(name + " " + capacity + " " + feature + " " + alcoholAllowed);
    }

}