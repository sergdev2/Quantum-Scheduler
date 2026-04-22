package controller;

import model.ProcessModel;
import model.ProcessQueue;
import model.StateProcess;

public class RoundRobinScheduler implements InterfaceScheduler {

    private final ProcessQueue queue;
    private final int quantum = 4;

    public RoundRobinScheduler(ProcessQueue queue) {
        this.queue = queue;
    }

    @Override
    public ProcessModel executeStep() {
        // 🔥 Implementação do passo a passo do Round Robin
        if (queue.isEmptyProcess()) {
            return null;
        }
        ProcessModel p = queue.nextProcess();
        if (p == null) {
            return null;
        }

        // nextProcess() já define o estado como EXECUTING; executa o quantum diretamente
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

        return p;
    }
}

