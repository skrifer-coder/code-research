package skrifer.github.com.designpattern.双分派;

public class Test {
    public static void main(String[] args) {
        //定义一个演员
        AbsActor actor = new OldActor();
        //定义一个角色
        Role role = new KungFuRole();
        //开始演戏
        actor.act(role);
        actor.act(new KungFuRole());


        //入参 在编译期就决定了调用哪个方法 由参数表面类型决定 :静态绑定  重载/ private/final
        //actor 在运行时期决定执行方法 ： 动态绑定 重写

        //双分派 重载前面加一个重写 -> 动态重载

    }
}
