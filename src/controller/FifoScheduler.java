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
    public ProcessModel executeStep() {

        if (queue.isEmptyProcess()) {
            return null;
        }

        // FIFO: peek sem remover — o processo atual executa até terminar
        ProcessModel p = queue.peekNextProcess();

        p.setState(StateProcess.EXECUTING);
        p.executorTick();
        canvas.repaint();

        // Só remove da fila quando o processo terminar (comportamento não-preemptivo)
        if (p.getState() == StateProcess.FINISHED) {
            queue.nextProcess();
        }

        return p;
    }
}
