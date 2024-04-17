package nju.mobile67.model;

public enum TypeEnum {

    DEFAULT("default");

    private String name;

    TypeEnum(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }
}
