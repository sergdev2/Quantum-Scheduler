package view;

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

    private RoundRobinScheduler controller;
    private ProcessQueue queue;

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
        controller = new RoundRobinScheduler(queue, canvas);

        // 🔥 TESTE: adicionar processos
        queue.addProcess(new ProcessModel("P1", 30));
        queue.addProcess(new ProcessModel("P2", 20));
        queue.addProcess(new ProcessModel("P3", 10));   

        createSouth();
        craeteEast();
        
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
        startButton.addActionListener(e ->controller.start());

        // Button pause
        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(e ->controller.stop());

        // Button reset
        resetButton = new JButton("RESET");
        resetButton.addActionListener(e ->{});

        // Add button
        addButton = new JButton("ADD");
        addButton.addActionListener(e ->{});

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
        areaText = new JTextArea();
        logScrollPane = new JScrollPane(areaText);
        
        southPanel.setLayout(new BorderLayout());
        southPanel.add(logScrollPane, BorderLayout.CENTER);
    }

    private void craeteEast(){
        algorithmComboBox = new JComboBox<>(new String[]{"Round Robin", "FIFO"});
        slider = new JSlider(1,10,5);

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
        new MainWindow();
    }
}
