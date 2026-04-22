package controller;

import javax.swing.SwingWorker;
import model.ProcessModel;
import model.ProcessQueue;
import model.StateProcess;
import view.CanvasPanel;
import view.MainWindow;

public class RoundRobinScheduler implements InterfaceScheduler {

    private SwingWorker<Void, Object> worker;
    private ProcessQueue queue;
    int quantum = 4;

    public RoundRobinScheduler(ProcessQueue queue) {
        this.queue = queue;
    }

    public void executeStep() {
        // 🔥 Implementação do passo a passo do Round Robin
        if (queue.isEmptyProcess()) {
            return;
        }
        ProcessModel p = queue.nextProcess();
        if (p == null) {
            return;
        }

        if (p.getState() == StateProcess.READY) {
            p.setState(StateProcess.EXECUTING);

            for (int i = 0; i < quantum; i++) {
                if (p.getState() == StateProcess.FINISHED) {
                    break;
                }
                p.executorTick();
            }

            if (p.getState() != StateProcess.FINISHED) {
                p.setState(StateProcess.READY);
                queue.addProcess(p);
            }
        }
    }
}

