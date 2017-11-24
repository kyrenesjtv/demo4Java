package me.kyrene.demo.io;

import java.util.LinkedList;

/**
 * Created by wanglin on 2017/11/24.
 */
public class MyQueue<E> {

    private LinkedList<E> queue;

    public MyQueue(){
        queue = new LinkedList<E>();
    }

    public void add(E e){
        queue.addFirst(e);
    }

    public E remove(){
        return queue.removeFirst();
    }

    public boolean isQueueEmpty(){
        return queue.isEmpty();
    }
}
