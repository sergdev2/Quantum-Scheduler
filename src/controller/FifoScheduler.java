package controller;

/*
*  🔥 1. Criar um controller para o FIFO
*  🔥 2. Criador Sergio Tiago
 */
import model.ProcessModel;
import model.ProcessQueue;
import model.StateProcess;
import view.CanvasPanel;

public class FifoScheduler implements InterfaceScheduler {

    private ProcessQueue queue;
    private CanvasPanel canvas;
    private boolean running;

    public FifoScheduler(ProcessQueue queue, CanvasPanel canvas) {
        this.queue = queue;
        this.canvas = canvas;
    }

    @Override
    private void executeStep() {

        if (queue.isEmptyProcess()) {
            return;
        }

        ProcessModel p = queue.nextProcess();

        p.setState(StateProcess.EXECUTING);
        p.executorTick();
        canvas.repaint();

        if (p.getState() == StateProcess.FINISHED) {
            p.setState(StateProcess.FINISHED);
        }
    }
}
