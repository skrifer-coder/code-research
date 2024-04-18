package skrifer.github.com.swagger.controller;

import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import skrifer.github.com.swagger.dto.UserDTO;

@Api(tags = "用户管理")
@RestController
public class UserController {

    // 对于swagger，尽量不要使用@RequestMapping！！！！！！！！！！！！！！！！！！！！！
    // 因为@RequestMapping支持任意请求方式，swagger会为这个接口生成7种请求方式的接口文档！！！！！！！！！！！！！！！！！！！
    @ApiOperation(value = "用户测试", notes = "用户测试notes")
    @PostMapping("/info")
    public UserDTO info(@RequestBody UserDTO userDTO) {
        return new UserDTO();
    }

    /**
     * 对于非实体类参数，可以使用@ApiImplicitParams和@ApiImplicitParam来声明请求参数。
     *
     * @return
     * @ApiImplicitParams用在方法头上，@ApiImplicitParam定义在@ApiImplicitParams里面，一个@ApiImplicitParam对应一个参数。
     * @ApiImplicitParam常用配置项： name：用来定义参数的名字，也就是字段的名字,可以与接口的入参名对应。如果不对应，也会生成，所以可以用来定义额外参数！
     * value：用来描述参数
     * required：用来标注参数是否必填
     * paramType有path,query,body,form,header等方式，但对于对于非实体类参数的时候，
     * 常用的只有path,query,header；body和form是不常用的。body不适用于多个零散参数的情况，只适用于json对象等情况。
     * 【如果你的接口是form-data,x-www-form-urlencoded的时候可能不能使用swagger页面API调试，
     * 但可以在后面讲到基于BootstrapUI的swagger增强中调试，基于BootstrapUI的swagger支持指定form-data或x-www-form-urlencoded】
     */
    @ApiOperation(value = "用户GET_QUERY测试", notes = "用户GET_QUERY测试notes")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param1",//参数名字
                    value = "参数1",//参数的描述
                    required = true,//是否必须传入
                    //paramType定义参数传递类型：有path,query,body,form,header
                    paramType = "query"
            )
            ,
            @ApiImplicitParam(name = "param2",//参数名字
                    value = "参数2",//参数的描述
                    required = true,//是否必须传入
                    paramType = "query"
            )
    })
    public String getInfo(String param1, String param2) {
        return "getInfo";
    }

    @ApiOperation(value = "用户GET测试", notes = "用户GET测试notes")
    @GetMapping("/info/{py1}/{py2}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "py1",//参数名字
                    value = "参数1",//参数的描述
                    required = true,//是否必须传入
                    //paramType定义参数传递类型：有path,query,body,form,header
                    paramType = "path"
            )
            ,
            @ApiImplicitParam(name = "py2",//参数名字
                    value = "参数2",//参数的描述
                    required = true,//是否必须传入
                    paramType = "path"
            )
    })
    public String getInfoByPath(@PathVariable String py1, @PathVariable String py2) {
        return "getInfoByPath";
    }

    @ApiOperation(value = "用户GET测试", notes = "用户GET测试notes")
    @GetMapping("/info/header")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "h1",//参数名字
                    value = "参数1",//参数的描述
                    required = true,//是否必须传入
                    //paramType定义参数传递类型：有path,query,body,form,header
                    paramType = "header"
            )
            ,
            @ApiImplicitParam(name = "h2",//参数名字
                    value = "参数2",//参数的描述
                    required = true,//是否必须传入
                    paramType = "header"
            )
    })
    public String getInfoByHeader(@RequestHeader String h1, @RequestHeader String h2) {
        return "getInfoByPath";
    }


    // 有文件上传时要用@ApiParam，用法基本与@ApiImplicitParam一样，不过@ApiParam用在参数上
    // 或者你也可以不注解，swagger会自动生成说明
    @ApiOperation(value = "上传文件",notes = "上传文件")
    @PostMapping(value = "/upload")
    public String upload(@ApiParam(value = "图片文件", required = true) MultipartFile uploadFile){
        String originalFilename = uploadFile.getOriginalFilename();

        return originalFilename;
    }


    // 多个文件上传时，**swagger只能测试单文件上传**
    @ApiOperation(value = "上传多个文件",notes = "上传多个文件")
    @PostMapping(value = "/upload2",consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public String upload2(@ApiParam(value = "图片文件", required = true,allowMultiple = true)MultipartFile[] uploadFile){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < uploadFile.length; i++) {
            System.out.println(uploadFile[i].getOriginalFilename());
            sb.append(uploadFile[i].getOriginalFilename());
            sb.append(",");
        }
        return sb.toString();
    }



    // 既有文件，又有参数
    @ApiOperation(value = "既有文件，又有参数",notes = "既有文件，又有参数")
    @PostMapping(value = "/upload3")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",
                    value = "图片新名字",
                    required = true
            )
    })
    public String upload3(@ApiParam(value = "图片文件", required = true)MultipartFile uploadFile,
                          String name){
        String originalFilename = uploadFile.getOriginalFilename();

        return originalFilename+":"+name;
    }

    // 响应是非实体类,此时不能增加字段注释，所以其实swagger推荐使用实体类
    @ApiOperation(value = "非实体类响应",notes = "非实体类")
    @ApiResponses({
            @ApiResponse(code=101,message = "diy"),
            @ApiResponse(code=200,message = "调用成功"),
            @ApiResponse(code=401,message = "无权限" )
    }
    )
    @PostMapping("/role2")
    public String role2(){
        return " {\n" +
                " name:\"广东\",\n" +
                "     citys:{\n" +
                "         city:[\"广州\",\"深圳\",\"珠海\"]\n" +
                "     }\n" +
                " }";
    }

}
