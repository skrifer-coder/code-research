package skrifer.github.com.designpattern.command;

/**
 * 命令模式
 * 命令模式是一个高内聚的模式，其定义为：
 * Encapsulate a request as an object,thereby letting you parameterize clients with different requests,queue or log requests,and support undoable operations.
 * （将一个请求封装成一个对象，从而让你使用不同的请求把客户端参数化，对请 求排队或者记录请求日志，可以提供命令的撤销和恢复功能。）
 * <p>
 * 我们看到三个角色：
 * ● Receive接收者角色 该角色就是干活的角色，命令传递到这里是应该被执行的，具体到我们上面的例子中就 是Group的三个实现类。
 * ● Command命令角色 需要执行的所有命令都在这里声明。
 * ● Invoker调用者角色 接收到命令，并执行命令。
 * <p>
 * 命令模式比较简单，但是在项目中非常频繁地使用，因为它的封装性非常好，把请求方 （Invoker）和执行方（Receiver）分开了，扩展性也有很好的保障，通用代码比较简单。
 *
 * 优点
 * ● 类间解耦 调用者角色与接收者角色之间没有任何依赖关系，调用者实现功能时只需调用Command 抽象类的execute方法就可以，不需要了解到底是哪个接收者执行。
 * ● 可扩展性 Command的子类可以非常容易地扩展，而调用者Invoker和高层次的模块Client不产生严 重的代码耦合。
 * ● 命令模式结合其他模式会更优秀 命令模式可以结合责任链模式，实现命令族解析任务；结合模板方法模式，则可以减少 Command子类的膨胀问题。
 *
 * 缺点:
 * 命令模式也是有缺点的，请看Command的子类：如果有N个命令，问题就出来 了，Command的子类就可不是几个，而是N个，这个类膨胀得非常大，这个就需要读者在项 目中慎重考虑使用
 */
public class Command_ {

    public abstract class Receiver {
        //抽象接收者，定义每个接收者都必须完成的业务
        public abstract void doSomething();
    }

    public class ConcreteReciver1 extends Receiver {
        //每个接收者都必须处理一定的业务逻辑
        public void doSomething() {
        }
    }

    public class ConcreteReciver2 extends Receiver {
        //每个接收者都必须处理一定的业务逻辑
        public void doSomething() {
        }
    }


    public abstract class Command {
        //每个命令类都必须有一个执行命令的方法
        public abstract void execute();
    }


    public class ConcreteCommand1 extends Command {
        //对哪个Receiver类进行命令处理
        private Receiver receiver;

        //构造函数传递接收者
        public ConcreteCommand1(Receiver _receiver) {
            this.receiver = _receiver;
        }

        //必须实现一个命令
        public void execute() {
            //业务处理
            this.receiver.doSomething();
        }
    }

    public class ConcreteCommand2 extends Command {
        //哪个Receiver类进行命令处理
        private Receiver receiver;

        //构造函数传递接收者
        public ConcreteCommand2(Receiver _receiver) {
            this.receiver = _receiver;
        }

        //必须实现一个命令
        public void execute() { //业务处理
            this.receiver.doSomething();
        }
    }

    public class Invoker {
        private Command command;

        //受气包，接受命令
        public void setCommand(Command _command) {
            this.command = _command;
        }

        //执行命令
        public void action() {
            this.command.execute();
        }
    }

    public class Client {
        public void main(String[] args) {
            //首先声明调用者
            Invoker invoker = new Invoker();
            //定义接收者
            Receiver receiver = new ConcreteReciver1();
            //定义一个发送给接收者的命令
            Command command = new ConcreteCommand1(receiver);
            //把命令交给调用者去执行
            invoker.setCommand(command);
            invoker.action();
        }
    }

}
