package skrifer.github.com.http.response;

import lombok.Data;

import java.util.List;

@Data
public class KEDAJDAPIAreaResponse extends KEDAJDAPIBaseResponse{
    private List<Domain> domains;

    @Data
    public static class Domain{
        private String moid;
        private String parent_moid;
        private String name;
        private String type;
    }
}
