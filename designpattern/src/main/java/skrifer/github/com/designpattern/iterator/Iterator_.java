package skrifer.github.com.designpattern.iterator;

import java.util.Vector;

/**
 * 仅供了解
 * 迭代器模式(如果你是做Java开发，尽量不要自己写迭代器模式！省省吧，使用Java提供的Iterator一 般就能满足你的要求了!!!!!!!!!!!!!!)
 * 迭代器模式（Iterator Pattern）目前已经是一个没落的模式，基本上没人会单独写一个迭 代器，除非是产品性质的开发，其定义如下：
 * Provide a way to access the elements of an aggregate object sequentially without exposing its underlying representation.
 * （它提供一种方法访问一个容器对象中各个元素，而又不需暴露该 对象的内部细节。）
 * <p>
 * ● Iterator抽象迭代器
 * 抽象迭代器负责定义访问和遍历元素的接口，而且基本上是有固定的3个方法：
 * first()获 得第一个元素，next()访问下一个元素，isDone()是否已经访问到底部（Java叫做hasNext()方 法）。
 * <p>
 * ● ConcreteIterator具体迭代器 具体迭代器角色要实现迭代器接口，完成容器元素的遍历。
 * ● Aggregate抽象容器 容器角色负责提供创建具体迭代器角色的接口，必然提供一个类似createIterator()这样的 方法，在Java中一般是iterator()方法。
 * ● Concrete Aggregate具体容器 具体容器实现容器接口定义的方法，创建出容纳迭代器的对象。
 *
 * 简单地说，迭代器就类似于一个数据库中的游标，可以在一个容器内上下翻滚，遍历所 有它需要查看的元素。
 */
public class Iterator_ {

    //抽象迭代器
    public interface Iterator {
        //遍历到下一个元素
        public Object next();
        //是否已经遍历到尾部

        public boolean hasNext();
        //删除当前指向的元素

        public boolean remove();
    }

    //具体迭代器
    public class ConcreteIterator implements Iterator {
        private Vector vector = new Vector();
        //定义当前游标
        public int cursor = 0;

        @SuppressWarnings("unchecked")
        public ConcreteIterator(Vector _vector) {
            this.vector = _vector;
        }//判断是否到达尾部

        public boolean hasNext() {
            if (this.cursor == this.vector.size()) {
                return false;
            } else {
                return true;
            }
        }//返回下一个元素

        public Object next() {
            Object result = null;
            if (this.hasNext()) {
                result = this.vector.get(this.cursor++);
            } else {
                result = null;
            }
            return result;
        }//删除当前元素

        public boolean remove() {
            this.vector.remove(this.cursor);
            return true;
        }
    }

    // 抽象容器
    public interface Aggregate {
        //是容器必然有元素的增加
        public void add(Object object);
        //减少元素

        public void remove(Object object);
        //由迭代器来遍历所有的元素

        public Iterator iterator();
    }

    //具体容器
    public class ConcreteAggregate implements Aggregate {
        //容纳对象的容器
        private Vector vector = new Vector();
        //增加一个元素

        public void add(Object object) {
            this.vector.add(object);
        }//返回迭代器对象

        public Iterator iterator() {
            return new ConcreteIterator(this.vector);
        }//删除一个元素

        public void remove(Object object) {
            this.remove(object);
        }
    }

    public class Client {
        public void main(String[] args) {
            //声明出容器
            Aggregate agg = new ConcreteAggregate();
            //产生对象数据放进去
            agg.add("abc");
            agg.add("aaa");
            agg.add("1234"); //遍历一下
            Iterator iterator = agg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
    }
}
