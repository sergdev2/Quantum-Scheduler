package controller;

import java.util.List;
import javax.swing.SwingWorker;
import model.ProcessModel;
import model.ProcessQueue;
import model.StateProcess;
import view.CanvasPanel;
import view.MainWindow;

public class RoundRobinScheduler {

    private SwingWorker<Void, Object> worker;
    private ProcessQueue queue;
    private CanvasPanel canvas;
    private MainWindow mainWindow;
    private boolean running;

    public RoundRobinScheduler(ProcessQueue queue, CanvasPanel canvas, MainWindow mainWindow) {
        this.queue = queue;
        this.canvas = canvas;
        this.mainWindow = mainWindow;
    }

    public void initSimulation() {

        if (worker != null && !worker.isDone()) {
            return;
        }

        running = true;

        worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {

                int quantum = 4;

                try {
                    while (running && !isCancelled()) {

                        if (queue.isEmptyProcess()) {
                            Thread.sleep(200);
                            continue;
                        }

                        ProcessModel p = queue.nextProcess();

                        if (p == null) {
                            continue;
                        }

                        p.setState(StateProcess.EXECUTING);

                        publish("▶ Process " + p.getName() + " started executing.");

                        for (int i = 0; i < quantum; i++) {

                            p.executorTick();

                            publish(p); // 🔥 atualiza UI corretamente

                            if (p.getState() == StateProcess.FINISHED) {
                                publish("Process " + p.getName() + " finished.");
                                break;
                            }

                            Thread.sleep(100);
                        }

                        if (p.getState() != StateProcess.FINISHED) {
                            p.setState(StateProcess.READY);
                            queue.addProcess(p);
                        }
                    }

                } catch (InterruptedException e) {}

                return null;
            }

            @Override
            protected void process(List<Object> chunks) {

                for (Object obj : chunks) {

                    if (obj instanceof ProcessModel) {
                        canvas.repaint();
                    }

                    if(obj instanceof String){
                        mainWindow.log((String) obj);
                    }
                }   
            }
        };

        worker.execute(); // 🔥 ESSENCIAL
    }

    public void resetSimulation() {
        pauseSimulation();
        queue.getQueue().clear();
        canvas.repaint();
    }

    public void pauseSimulation() {
        running = false;

        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
    }

    public void addProcess(ProcessModel process) {
        queue.addProcess(process);
    }
}
