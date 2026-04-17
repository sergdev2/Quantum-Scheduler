/*package controller;

import model.ProcessModel;
import model.ProcessQueue;
import model.StateProcess;
import view.CanvasPanel;

public class SchedulerController {

    private ProcessQueue queue;
    private CanvasPanel canvas;
    private boolean running;

    public SchedulerController(ProcessQueue queue, CanvasPanel canvas){
        this.queue = queue;
        this.canvas = canvas;
    }

    public  void start(){
        if(running) return;

        running = true;

        new Thread(() -> schedulerController()).start();
    }
    
    public void stop(){
        running = false;
    }

    private void reset(){
        running = false;
    }

    private void schedulerController(){

        int quantum = 4;

        // Codig of Round Robin
        while(running){
            if(queue.isEmptyProcess()){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {}
                continue;
            }

            ProcessModel p = queue.nextProcess();

            if(p == null){
                continue;
            }

            p.setState(StateProcess.EXECUTING);

            for(int i = 0; i < quantum; i++){
                p.executorTick();

                canvas.repaint();

                if(p.getState() == StateProcess.FINISHED){
                    break;
                }
            }
            
            if(p.getState() != StateProcess.FINISHED){
                p.setState(StateProcess.READY);
                queue.addProcess(p);
            }

            try{
                Thread.sleep(300);
            }catch(InterruptedException e){}
        }
    }
}
*/