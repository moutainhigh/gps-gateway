package com.zkkj.gps.gateway.terminal.monitor.utils;

import java.util.ArrayList;

/**
 * author : cyc
 * Date : 2019-04-08
 */
public class QueueList<E> extends ArrayList<E> {

    private int length;

    public QueueList(int capacity) {
        this.length = capacity;
    }

    /**
     * 往队列中添加数据
     *
     * @param e
     */
    @Override
    public boolean add(E e) {
        synchronized (this) {
            if (this.size() >= length) {
                this.remove(0);
            }
            return super.add(e);
        }
    }

    /**
     * 虽然加了index角标，但是插入元素的时候还是会加到该队列的尾部
     *
     * @param index
     * @param element
     */
    @Override
    public void add(int index, E element) {
        if (index >= length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.add(element);
    }

    /**
     * 判断该队列是否为空
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0 ? true : false;
    }

    /**
     * 判断队列是否满了
     *
     * @return
     */
    public boolean isFull() {
        return this.size() == length ? true : false;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
