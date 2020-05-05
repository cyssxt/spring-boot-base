package com.cyssxt.common.basedao.data;

public enum QueryExpression {

    EQUAL("="),
    IN(" in "),
    LIKE(" like "),
    BIGGEREUQALTHAN(" >= "),
    ;
    private String type;

    QueryExpression(String type) {
        this.type = type;
    }

    public String value() {
        return type;
    }
}
