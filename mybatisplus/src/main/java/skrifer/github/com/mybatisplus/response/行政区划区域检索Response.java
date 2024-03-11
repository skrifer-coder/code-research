/**
 * Copyright 2024 json.cn
 */
package skrifer.github.com.mybatisplus.response;
import java.util.List;

/**
 * Auto-generated: 2024-02-28 11:2:4
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/
 */
public class 行政区划区域检索Response {

    private int status;
    private String message;
    private String result_type;
    private List<行政区划区域检索Result> results;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }
    public String getResult_type() {
        return result_type;
    }

    public void setResults(List<行政区划区域检索Result> results) {
        this.results = results;
    }
    public List<行政区划区域检索Result> getResults() {
        return results;
    }

}