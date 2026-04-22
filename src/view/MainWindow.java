package view;

import controller.Controller;
import controller.FifoScheduler;
import controller.RoundRobinScheduler;
import java.awt.*;
import javax.swing.*;
import model.ProcessModel;
import model.ProcessQueue;

public class MainWindow extends JFrame{

    private JPanel northPanel, southPanel, eastPanel;
    private CanvasPanel canvas;

    private JButton startButton, pauseButton, resetButton, addButton;

    private JTextArea areaText;
    private JScrollPane logScrollPane;

    private JComboBox<String> algorithmComboBox;
    private JSlider slider;

    private Controller controller;
    private ProcessQueue queue;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public MainWindow(){
        setTitle("Quantum Scheduler");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);

        createPanels();
        createNorth();
        createCenter();

        queue = new ProcessQueue();

        // 🔥 ligar fila ao canvas
        canvas.setQueue(queue);
        // 🔥 criar controller

        // 🔥 TESTE: adicionar processos
        queue.addProcess(new ProcessModel("P1", 30));
        queue.addProcess(new ProcessModel("P2", 20));
        queue.addProcess(new ProcessModel("P3", 10));   

        createSouth();
        createEast();

        setVisible(true);
    }

    private  void createPanels(){
        // North panel
        northPanel = new JPanel();
        northPanel.setBackground(new Color(0, 0,0));

        // South panel
        southPanel = new JPanel();
        southPanel.setBackground(new Color(0,0,0));

        // East panel
        eastPanel = new JPanel();
        eastPanel.setBackground(new Color(0, 0,0));

        // add all panel in mainPanel
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        add(eastPanel,BorderLayout.EAST);
    }

    private  void createNorth(){
        // Button start
        startButton = new JButton("START");
        startButton.addActionListener(e ->controller.initSimulation());

        // Button pause
        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(e ->controller.pauseSimulation());

        // Button reset
        resetButton = new JButton("RESET");
        resetButton.addActionListener(e ->controller.resetSimulation());

        // Add button
        addButton = new JButton("ADD");
        addButton.addActionListener(e ->controller.addProcess(new ProcessModel("P" + (queue.getQueue().size() + 1), slider.getValue() * 10)));

        // Add buttons is their panels
        northPanel.add(startButton);
        northPanel.add(pauseButton);
        northPanel.add(resetButton);
        northPanel.add(addButton);
    }

    private void createCenter(){
        // Panel canva
        canvas = new CanvasPanel();
        add(canvas, BorderLayout.CENTER);
    }

    public void createSouth(){
        areaText = new JTextArea(20, 30);
        logScrollPane = new JScrollPane(areaText);

        southPanel.setLayout(new BorderLayout());
        southPanel.add(logScrollPane, BorderLayout.CENTER);
    }

    private void createEast(){
        algorithmComboBox = new JComboBox<>(new String[]{"Round Robin", "FIFO"});
        slider = new JSlider(1,10,5);

        algorithmComboBox.addActionListener(e -> {

            String selected = (String) algorithmComboBox.getSelectedItem();
            
            if (selected.equals("Round Robin")) {
                
                controller.setScheduler(new RoundRobinScheduler(queue));
                log("🔄 Algorithm changed to Round Robin.");

            } else if (selected.equals("FIFO")) {

                controller.setScheduler(new FifoScheduler(queue, canvas));
                log("🔄 Algorithm changed to FIFO.");

            }
        });

        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        eastPanel.setLayout(new BorderLayout());
        eastPanel.add(algorithmComboBox, BorderLayout.NORTH);
        eastPanel.add(slider, BorderLayout.SOUTH);
    }

    public CanvasPanel getCanvas(){
        return canvas;
    }

    public JButton getStartButton(){
        return startButton;
    }

    public JButton getPauseButton(){
        return pauseButton;
    }

    public JButton getResetButton(){
        return resetButton;
    }

    public JButton getAddButton(){
        return addButton;
    }


    public JSlider getSlider(){
        return slider;
    }

    public JComboBox<String> getAlgorithComboBox(){
        return algorithmComboBox;
    }

    public void log(String message){
        areaText.append(message + "\n");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            Controller ctrl = new Controller(window.queue, window.canvas, window);
            window.setController(ctrl);
        });
    }
}
