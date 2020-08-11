package com.example.pro4;

class LeftBean extends BaseBean{

    private String typeName;
    private String tag;

    public LeftBean(){}

    public LeftBean(String typeName){
        this.typeName = typeName;
    }

    public LeftBean(String tag,String typeName){
        this.typeName = typeName;
        this.tag = tag;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String tagStr() {
        return tag;
    }

}
