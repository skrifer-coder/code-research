package skrifer.github.com.designpattern.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * 观察者模式
 * 定义:
 * Define a one-to-many dependency between objects so that when one object changes state,all its dependents are notified and updated automatically.
 * （定义对象间一种一对多的依赖关系，使得每 当一个对象改变状态，则所有依赖于它的对象都会得到通知并被自动更新。）
 * <p>
 * ● Subject被观察者 定义被观察者必须实现的职责，它必须能够动态地增加、取消观察者。它一般是抽象类 或者是实现类，仅仅完成作为被观察者必须实现的职责：管理观察者并通知观察者。
 * ● Observer观察者 观察者接收到消息后，即进行update（更新方法）操作，对接收到的信息进行处理。
 * ● ConcreteSubject具体的被观察者 定义被观察者自己的业务逻辑，同时定义对哪些事件进行通知。
 * ● ConcreteObserver具体的观察者 每个观察在接收到消息后的处理反应是不同，各个观察者有自己的处理逻辑。
 * <p>
 * 它和责任链模式的最大区别就是观察者广播链在传播的过程中消息是随时更改 的，它是由相邻的两个节点协商的消息结构；而责任链模式在消息传递过程中基本上保持消 息不可变，如果要改变，也只是在原有的消息上进行修正。
 * <p>
 * java.util.Observable  java.util.Observer jdk已经实现的观察者接口
 */
public class Observer_ {

    //被观察者
    public abstract class Subject {
        //定义一个观察者数组
        private Vector<Observer> obsVector = new Vector<Observer>();

        //增加一个观察者
        public void addObserver(Observer o) {
            this.obsVector.add(o);
        }

        //删除一个观察者
        public void delObserver(Observer o) {
            this.obsVector.remove(o);
        }

        //通知所有观察者
        public void notifyObservers() {
            for (Observer o : this.obsVector) {
                o.update();
            }
        }
    }

    //具体被观察者
    public class ConcreteSubject extends Subject {
        //具体的业务
        public void doSomething() {
            /** do something */
            super.notifyObservers();
        }
    }

    public interface Observer {
        //更新方法
        public void update();
    }

    public class ConcreteObserver implements Observer {
        //实现更新方法
        public void update() {
            System.out.println("接收到信息，并进行处理！");
        }
    }

    public void process(String[] args) {
        //创建一个被观察者
        ConcreteSubject subject = new ConcreteSubject();
        //定义一个观察者
        Observer obs = new ConcreteObserver();
        //观察者观察被观察者
        subject.addObserver(obs);
        //观察者开始活动了
        subject.doSomething();
    }

    public static void main(String[] args) {
//        new Observer_().process(args);
        new Observer_().processWithJDK();
    }

    //----------------------------jdk内建实现接口-----------------------------

    public abstract class 被观察者接口 extends Observable {
        abstract void doSomething();

        public void setModify(){
            super.setChanged();
        }
    }

    public class 被观察者 extends 被观察者接口 {
        @Override
        public void doSomething() {
//            System.out.println("被观察者 在洗澡");
            super.notifyObservers("被观察者 在洗澡");
        }
    }

    public interface 观察者接口 extends java.util.Observer {
        void 暗中观察();
    }

    public class 观察者 implements 观察者接口 {
        @Override
        public void 暗中观察() {
            System.out.println("观察者在暗中观察");
        }

        @Override
        public void update(Observable o, Object arg) {
            暗中观察();
            System.out.println(arg);
        }
    }

    public void processWithJDK() {
        观察者接口 观察者 = new 观察者();
        被观察者 被观察者 = new 被观察者();
        被观察者.setModify();
        被观察者.addObserver(观察者);
        被观察者.doSomething();

    }
}