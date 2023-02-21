package skrifer.github.com.http.rbac;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

import static java.lang.String.format;

public class ResponseMessage<T> implements Serializable {
    private static final long serialVersionUID = 8992436576262574064L;

    @Getter
    @Setter
    protected String message;

    @Getter
    @Setter
    protected T result;

    @Getter
    @Setter
    protected int status;

    @Getter
    @Setter
    protected String code;

    @Getter
    @Setter
    protected LinkedHashSet<String> fields;

    @Getter
    @Setter
    protected Long timestamp;

    /**
     * 过滤字段：指定需要序列化的字段
     */
    @Getter
    protected transient Map<Class<?>, Set<String>> includes;

    /**
     * 过滤字段：指定不需要序列化的字段
     */
    @Getter
    protected transient Map<Class<?>, Set<String>> excludes;


    public ResponseMessage() {

    }

    public static <T> ResponseMessage<T> error(String message) {
        return error(500, CodeEnum.UNKNOWN.getValue(), message);
    }

    public static <T> ResponseMessage<T> error(String message, Object... args) {
        return error(500, message, args);
    }

    public static <T> ResponseMessage<T> error(int status, String message) {
        return error(status, CodeEnum.UNKNOWN.getValue(), message);
    }

    public static <T> ResponseMessage<T> error(int status, String message, Object... args) {
        return error(status, CodeEnum.UNKNOWN.getValue(), message, args);
    }

    public static <T> ResponseMessage<T> error(int status, String code, String message) {
        return error(status, code, message, null);
    }

    public static <T> ResponseMessage<T> error(int status, String code, String message, Object... args) {
        ResponseMessage<T> msg = new ResponseMessage<>();
        msg.message = format(message, args);
        msg.status(status);
        msg.code(code);
        return msg.putTimeStamp();
    }

    public static <T> ResponseMessage<T> ok() {
        return ok(null);
    }

    public static <T> ResponseMessage<T> ok(T result) {
        return new ResponseMessage<T>()
                .result(result)
                .putTimeStamp()
                .code(CodeEnum.SUCCESS.getValue())
                .status(200);
    }

    private ResponseMessage<T> putTimeStamp() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public ResponseMessage<T> result(T result) {
        this.result = result;
        return this;
    }

    public ResponseMessage<T> include(Class<?> type, String... fields) {
        return include(type, Arrays.asList(fields));
    }

    public ResponseMessage<T> include(Class<?> type, Collection<String> fields) {
        if (includes == null) {
            includes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        fields.forEach(field -> {
            if (field.contains(".")) {
                String tmp[] = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        include(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                }
            } else {
                getStringListFromMap(includes, type).add(field);
            }
        });
        return this;
    }

    public ResponseMessage<T> exclude(Class type, Collection<String> fields) {
        if (excludes == null) {
            excludes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        fields.forEach(field -> {
            if (field.contains(".")) {
                String tmp[] = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        exclude(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                }
            } else {
                getStringListFromMap(excludes, type).add(field);
            }
        });
        return this;
    }

    public ResponseMessage<T> exclude(Collection<String> fields) {
        if (excludes == null) {
            excludes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        Class type;
        if (getResult() != null) {
            type = getResult().getClass();
        } else {
            return this;
        }
        exclude(type, fields);
        return this;
    }

    public ResponseMessage<T> include(Collection<String> fields) {
        if (includes == null) {
            includes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        Class type;
        if (getResult() != null) {
            type = getResult().getClass();
        } else {
            return this;
        }
        include(type, fields);
        return this;
    }

    public ResponseMessage<T> exclude(Class type, String... fields) {
        return exclude(type, Arrays.asList(fields));
    }

    public ResponseMessage<T> exclude(String... fields) {
        return exclude(Arrays.asList(fields));
    }

    public ResponseMessage<T> include(String... fields) {
        return include(Arrays.asList(fields));
    }

    protected Set<String> getStringListFromMap(Map<Class<?>, Set<String>> map, Class type) {
        return map.computeIfAbsent(type, k -> new HashSet<>());
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }

    public ResponseMessage<T> status(int status) {
        this.status = status;
        return this;
    }

    public ResponseMessage<T> code(String code) {
        this.code = code;
        return this;
    }

    public ResponseMessage<T> fields(LinkedHashSet<String> fields) {
        this.fields = fields;
        return this;
    }

    public ResponseMessage<T> field(String field) {
        if (this.fields == null) {
            synchronized (this) {
                if (this.fields == null) {
                    this.fields = new LinkedHashSet<>();
                }
            }
        }
        this.fields.add(field);
        return this;
    }

//    public <E> E getResult(Class<E> clazz) {
//        return BeanMapper.deepMap(result, clazz);
//    }
//
//    public boolean is1xxInformational() {
//        return HttpStatusEnum.valueOf(this.status).is1xxInformational();
//    }
//
//    public boolean is2xxSuccessful() {
//        return HttpStatusEnum.valueOf(this.status).is2xxSuccessful();
//    }
//
//    public boolean is3xxRedirection() {
//        return HttpStatusEnum.valueOf(this.status).is3xxRedirection();
//    }
//
//    public boolean is4xxClientError() {
//        return HttpStatusEnum.valueOf(this.status).is4xxClientError();
//    }
//
//    public boolean is5xxServerError() {
//        return HttpStatusEnum.valueOf(this.status).is5xxServerError();
//    }
}
