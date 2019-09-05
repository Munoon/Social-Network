package com.train4game.social.web.validators;
@StringFieldsMatch(first = "first", second = "second", message = "Strings don't match")
public class StringFieldsMatchValidatorTestData {
    private String first;
    private String second;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}
