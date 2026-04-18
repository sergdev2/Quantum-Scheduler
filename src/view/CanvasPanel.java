package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import model.ProcessModel;
import model.ProcessQueue;

public class CanvasPanel extends JPanel{

    private ProcessQueue queue;

    public CanvasPanel(){
        setBackground(Color.WHITE);
    }

    public void setQueue(ProcessQueue queue){
        this.queue = queue;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // ✨ suavização (DICA DO ENUNCIADO)
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(queue == null){
            return;
        }

        int i = 0;

        List<ProcessModel> snapshot = new ArrayList<>(queue.getQueue());

        for(ProcessModel p: snapshot){

            int y = i * 60 + 20;

            double progress = p.getProgress();
            progress = Math.max(0, Math.min(1, progress));

            int barWidth = (int)(progress * 400);

            // background barra
            g.setColor(Color.DARK_GRAY);
            g.fillRect(100, y, 400, 25);

            // Progress
            g.setColor(Color.GREEN);
            g.fillRect(100, y, barWidth, 25);

            // contorn
            g.setColor(Color.BLACK);
            g.drawRect(100, y, 400, 25);

            // Pregress name
            g.setColor(Color.BLACK);
            g.drawString(p.getName(), 20, y + 18);

            // Porcent
            g.drawString((int)(progress * 100) + "%", 520, y + 18);

            i++;
        }
    }

}
