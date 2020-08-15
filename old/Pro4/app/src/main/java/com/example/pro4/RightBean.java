package com.example.pro4;

class RightBean extends BaseBean {

    private String tag;
    private String name;

    public RightBean(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String tagStr() {
        return null;
    }
}
