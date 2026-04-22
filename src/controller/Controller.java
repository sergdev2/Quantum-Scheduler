package controller;

import java.util.List;
import javax.swing.SwingWorker;
import model.ProcessModel;
import model.ProcessQueue;
import view.CanvasPanel;
import view.MainWindow;

public class Controller {

    private ProcessQueue queue;
    private CanvasPanel canvas;
    private MainWindow mainWindow;

    private SwingWorker<Void, ProcessModel> worker;
    private InterfaceScheduler scheduler;

    private boolean isRunning = false;

    public Controller(ProcessQueue queue, CanvasPanel canvas, MainWindow mainWindow) {
        this.queue = queue;
        this.canvas = canvas;
        this.mainWindow = mainWindow;
        this.scheduler = new RoundRobinScheduler(queue);
    }

    public void setScheduler(InterfaceScheduler scheduler) {
        boolean wasRunning = isRunning;
        if (wasRunning) {
            pauseSimulation();
        }
        this.scheduler = scheduler;
        if (wasRunning) {
            initSimulation();
        }
    }

    public void initSimulation() {
        // 🔥 Iniciar a animação do escalonamento
        // Pode ser um loop que chama o método de execução do escalonador e atualiza a interface

        if (isRunning) {
            return; // Já está rodando
        }

        isRunning = true;

        worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {

                while (isRunning) {

                    if (queue.isEmptyProcess()) {
                        Thread.sleep(250); // Esperar um pouco antes de verificar novamente
                        continue;
                    }

                    ProcessModel executed = scheduler.executeStep();       // 🔥 Executar um passo do escalonamento

                    // 🔥 Atualizar a interface (canvas, log, etc.)
                    if (executed != null) {
                        publish(executed); // Enviar o processo executado para o método process()  
                    }

                    Thread.sleep(250);

                }
                return null;
            }

            @Override
            protected void process(List<ProcessModel> chunks) {
                ProcessModel p = chunks.get(chunks.size() - 1);

                canvas.repaint();

                mainWindow.log("▶ Process " + p.getName() + " executed a step.");
            }
        };
        worker.execute();
    }

    public void pauseSimulation() {
        isRunning = false;
        if (worker != null) {
            worker.cancel(true);
        }
    }

    public void resetSimulation() {
        pauseSimulation();
        queue.clear();
        canvas.repaint();
        mainWindow.log("🔄 Simulation reset.");
    }

    public void addProcess(ProcessModel process) {
        queue.addProcess(process);
        canvas.repaint();
        mainWindow.log("➕ Process " + process.getName() + " added with " + process.getRealTime() + "ms.");
    }
}
