package edu.vegetable.constants;

public enum QueryTable {

    US("user", "tb_sysuser"), VI("vegetables", "tb_vegeinfo"), VK("vegeknowledge", "tb_vegeknowledge");

    String type;
    String table;

    QueryTable(String type, String table) {
        this.table = table;
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public String getType() {
        return type;
    }
}
