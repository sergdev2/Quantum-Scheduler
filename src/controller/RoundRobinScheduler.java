package controller;

import javax.swing.SwingWorker;
import java.util.List;

import model.ProcessModel;
import model.ProcessQueue;
import model.StateProcess;
import view.CanvasPanel;

public class RoundRobinScheduler {

    private SwingWorker<Void, ProcessModel> worker;
    private ProcessQueue queue;
    private CanvasPanel canvas;
    private boolean running;

    public RoundRobinScheduler(ProcessQueue queue, CanvasPanel canvas){
        this.queue = queue;
        this.canvas = canvas;
    }

    public void start(){
        running = true;
        initSimulation();
    }

    public void stop(){
        running = false;

        if(worker != null){
            worker.cancel(true);
        }
    }

    public void initSimulation(){

        if(worker != null && !worker.isDone()){
            worker.cancel(true);
        }

        worker = new SwingWorker<Void, ProcessModel>() {

            @Override
            protected Void doInBackground() throws Exception {

                int quantum = 4;

                while(running && !isCancelled()){

                    if(queue.isEmptyProcess()){
                        Thread.sleep(300);
                        continue;
                    }

                    ProcessModel p = queue.nextProcess();

                    if(p == null) continue;

                    p.setState(StateProcess.EXECUTING);

                    for(int i = 0; i < quantum; i++){

                        p.executorTick();

                        publish(p); // 🔥 atualiza UI corretamente

                        if(p.getState() == StateProcess.FINISHED){
                            break;
                        }

                        Thread.sleep(100);
                    }

                    if(p.getState() != StateProcess.FINISHED){
                        p.setState(StateProcess.READY);
                        queue.addProcess(p);
                    }
                }

                return null;
            }

            @Override
            protected void process(List<ProcessModel> chunks){
                canvas.repaint();
            }
        };

        worker.execute(); // 🔥 ESSENCIAL
    }
}