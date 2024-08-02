package skrifer.github.com.es.Enum;

public enum ESIndexEnum {

    BIZ_INDEX("biz_index", "业务索引", 1, 1);


    private String name, desc;
    private int shards, replicas;

    ESIndexEnum(String name, String desc, int shards, int replicas) {
        this.name = name;
        this.desc = desc;
        this.shards = shards;
        this.replicas = replicas;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getShards() {
        return shards;
    }

    public int getReplicas() {
        return replicas;
    }
}
