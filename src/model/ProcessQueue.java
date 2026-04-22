package model;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessQueue {

    private ConcurrentLinkedQueue<ProcessModel> queue;

    public ProcessQueue(){
        queue = new ConcurrentLinkedQueue<>();
    }

    public void addProcess(ProcessModel p){
        if(p == null){
            return;
        }
        p.setState(StateProcess.READY);
        queue.add(p);
    }

    public ProcessModel nextProcess(){
       ProcessModel p = queue.poll();

        if (p != null) {
            p.setState(StateProcess.EXECUTING);
        }

        return p;
    }

    public void requere(ProcessModel p){
        if(p != null){
            p.setState(StateProcess.READY);
            queue.add(p);
        }
    }
    
    public ProcessModel peekNextProcess(){
        return queue.peek();
    }

    public boolean isEmptyProcess(){
        return queue.isEmpty();
    }

    public int size(){
        return queue.size();
    }

    public void clear(){
        queue.clear();
    }

    public ConcurrentLinkedQueue<ProcessModel> getQueue(){
        return queue;
    }
}

