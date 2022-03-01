package skrifer.github.com.thread;

import sun.misc.Contended;

/**
 * 常规 缓存行(在cpu 核使用L1 L2 L3缓存时会顺带将目标元素的相邻元素也一同复制) 的大小是64bytes,
 * 在涉及多核处理 相邻或很靠近的元素时，常常需要在彼此做缓存数据同步，非常消耗性能
 * 最好的办法就是将目标元素用一堆无意义元素隔离开，隔离的距离必须大于缓存行的容量，以此来规避减少缓存数据同步
 */
public class ContendedTest {

    /**
     * jvm实现的缓存行保证隔离
     */
    @Contended
    public class T {
        private long target;
    }

    /**
     * 自己怼来避免缓存行的多个target同步数据 导致性能消耗问题
     */
    public class Y {
        private long p1;
        private long p2;
        private long p3;
        private long p4;
        private long p5;
        private long p6;
        private long p7;
        private long target;
        private long p8;
        private long p9;
        private long p10;
        private long p11;
        private long p12;
        private long p13;
        private long p14;

    }
}
