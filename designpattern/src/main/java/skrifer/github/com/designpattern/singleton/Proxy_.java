package skrifer.github.com.designpattern.singleton;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理模式
 * 封装被代理对象的具体实现
 */
public class Proxy_ {

    //普通代理模式----------------------------------------------------------

    public interface IGamePlayer {
        void killBoss();

        void login(String user, String password);

        void upgrade();
    }

    /**
     * 实际操作类
     */
    public class GamePlayer implements IGamePlayer {
        private String name = ""; //构造函数限制谁能创建对象，并同时传递姓名

        public GamePlayer(IGamePlayer _gamePlayer, String _name) throws Exception {
            if (_gamePlayer == null) {
                throw new Exception("不能创建真实角色！");
            } else {
                this.name = _name;
            }
        }

        //打怪，最期望的就是杀老怪
        public void killBoss() {
            System.out.println(this.name + "在打怪！");
        }

        //进游戏之前你肯定要登录吧，这是一个必要条件
        public void login(String user, String password) {
            System.out.println("登录名为" + user + "的用户" + this.name + "登录成功！");
        }

        //升级，升级有很多方法，花钱买是一种，做任务也是一种
        public void upgrade() {
            System.out.println(this.name + " 又升了一级！");
        }


    }

    /**
     * 代理类
     */
    public class GamePlayerProxy implements IGamePlayer {
        private IGamePlayer gamePlayer = null;

        //通过构造函数传递要对谁进行代练
        public GamePlayerProxy(String name) {
            try {
                gamePlayer = new GamePlayer(this, name);
            } catch (Exception e) {
                // TODO 异常处理
            }
        }

        //代练杀怪
        public void killBoss() {
            this.gamePlayer.killBoss();
        }

        //代练登录
        public void login(String user, String password) {
            this.gamePlayer.login(user, password);
        }

        //代练升级
        public void upgrade() {
            this.gamePlayer.upgrade();
        }
    }

    /**
     * 场景类
     */
    public class Stage {
        IGamePlayer gameProxy = new GamePlayerProxy("张三");

        {
            gameProxy.login("账号", "密码");
            gameProxy.killBoss();
            gameProxy.upgrade();
        }

    }


    //强制代理模式----------------------------------------------------------

    /**
     * 必须通过真实角色查找到代理角色，否则你不能访问。
     * 甭管你是通过代理类还是通过直接new一个主题角色类，都不能访问，只有通过真实角 色指定的代理类才可以访问，也就是说由真实角色管理代理角色。
     */

    public abstract class Action {
        public abstract void sing();
    }

    public class XiaoMing extends Action {

        public XiaoMing(Action action) {
            if (action instanceof XiaoMingDeJingJiRen == false) {
                throw new IllegalArgumentException("必须是XiaoMingDeJingJiRen!");
            }
        }

        private String name;

        @Override
        public void sing() {
            System.out.println(this.name + "在唱歌!");
        }
    }

    public class XiaoMingDeJingJiRen extends Action {

        private Action action;

        public XiaoMingDeJingJiRen() {
            this.action = new XiaoMing(this);
        }

        @Override
        public void sing() {
            this.action.sing();
        }
    }



}
