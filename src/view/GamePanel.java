package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import model.*;

/**
 * GamePanel: renders the 5×5 game grid.
 * Tile size is computed dynamically from the component's actual width/height
 * so the grid always fills the available space and rescales on window resize.
 */
public class GamePanel extends JPanel {

    private BufferedImage robotImage;
    private BufferedImage fileImage;
    private BufferedImage caseOccupee;
    private BufferedImage robotHoldingFileImage;
    private BufferedImage porte;

    private final int COLS = 5;
    private final int ROWS = 5;

    private Niveau niveau;

    public GamePanel(Niveau niveau) {
        this.niveau = niveau;
        setBackground(new Color(20, 20, 25));
        setDoubleBuffered(true);
        setMinimumSize(new Dimension(200, 200));
        // No fixed preferredSize — fills whatever CENTER gives it

        loadImage("src/assets/images/robot.png",             img -> robotImage            = img);
        loadImage("src/assets/images/porte.png",             img -> porte                 = img);
        loadImage("src/assets/images/interdit.png",          img -> caseOccupee           = img);
        loadImage("src/assets/images/fichier.png",           img -> fileImage             = img);
        loadImage("src/assets/images/robottiensfichier.png", img -> robotHoldingFileImage = img);
    }

    @FunctionalInterface
    private interface ImageConsumer { void accept(BufferedImage img); }

    private void loadImage(String path, ImageConsumer consumer) {
        try { consumer.accept(ImageIO.read(new File(path))); }
        catch (IOException e) { e.printStackTrace(); }
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dynamic tile size: fill the panel while keeping the grid square-ish
        int tileSize = Math.min(getWidth() / COLS, getHeight() / ROWS);
        if (tileSize < 1) return;   // not yet laid out

        int gridW   = tileSize * COLS;
        int gridH   = tileSize * ROWS;
        int offsetX = (getWidth()  - gridW) / 2;
        int offsetY = (getHeight() - gridH) / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                             java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Grid background
        g2d.setColor(new Color(30, 30, 40));
        g2d.fillRect(offsetX, offsetY, gridW, gridH);

        // Draw cells
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = offsetX + col * tileSize;
                int y = offsetY + row * tileSize;

                g2d.setColor(new Color(40, 40, 50));
                g2d.fillRect(x + 1, y + 1, tileSize - 2, tileSize - 2);

                g2d.setColor(new Color(60, 60, 80));
                g2d.drawRect(x, y, tileSize, tileSize);

                TypeTerritoire tt = niveau.getGrille().getTerritoire(col, row);
                if (tt != TypeTerritoire.LIBRE && tt != TypeTerritoire.OCCUPE) {
                    drawPortal(g2d, x, y, tileSize, getPortalColor(tt));
                }
                if (niveau.getGrille().estZoneOccupee(col, row) && caseOccupee != null) {
                    int margin = tileSize / 4;
                    g.drawImage(caseOccupee, x + margin, y + margin,
                                tileSize / 2, tileSize / 2, this);
                }
                if (col == niveau.getTargetX() && row == niveau.getTargetY()) {
                    drawTargetZone(g2d, x, y, tileSize);
                }
            }
        }

        // Outer grid border
        g2d.setColor(new Color(0, 255, 255, 100));
        g2d.drawRect(offsetX, offsetY, gridW - 1, gridH - 1);

        // Robots
        for (Robot robot : niveau.getGrille().getListeRobots()) {
            int x = offsetX + robot.getPositionX() * tileSize;
            int y = offsetY + robot.getPositionY() * tileSize;
            if (robot.getFichierEnMain() != null && robotHoldingFileImage != null) {
                g.drawImage(robotHoldingFileImage, x, y, tileSize, tileSize, this);
            } else if (robotImage != null) {
                int pad = tileSize / 6;
                g.drawImage(robotImage, x + pad, y + pad,
                            tileSize - pad * 2, tileSize - pad * 2, this);
            }
        }

        // Files
        for (Fichier fichier : niveau.getGrille().getListFichiers()) {
            if (!fichier.estTenuParRobot()) {
                int cx = offsetX + fichier.getPosX() * tileSize;
                int cy = offsetY + fichier.getPosY() * tileSize;
                int iconSize = tileSize / 2;
                int pad      = tileSize / 4;

                if (fileImage != null) {
                    g.drawImage(fileImage, cx + pad, cy + pad, iconSize, iconSize, this);
                }

                // File ID label — drawn below the icon, centred on the cell
                String label = "F" + fichier.getId();
                int fontSize = Math.max(9, tileSize / 5);
                g2d.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, fontSize));
                java.awt.FontMetrics fm = g2d.getFontMetrics();
                int lw = fm.stringWidth(label);
                int lx = cx + (tileSize - lw) / 2;
                int ly = cy + pad + iconSize + fm.getAscent() + 1;

                // Shadow
                g2d.setColor(Color.BLACK);
                g2d.drawString(label, lx + 1, ly + 1);
                // Label in bright cyan
                g2d.setColor(new Color(0, 220, 255));
                g2d.drawString(label, lx, ly);
            }
        }
    }

    // ── Drawing helpers ───────────────────────────────────────────────────────

    private void drawTargetZone(Graphics2D g2d, int x, int y, int tileSize) {
        Color c = new Color(0, 255, 100);
        g2d.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 30));
        g2d.fillRect(x + 2, y + 2, tileSize - 4, tileSize - 4);

        float[] dash = {5f, 5f};
        g2d.setStroke(new java.awt.BasicStroke(2, java.awt.BasicStroke.CAP_BUTT,
                java.awt.BasicStroke.JOIN_MITER, 10f, dash, 0f));
        g2d.setColor(c);
        g2d.drawRect(x + 4, y + 4, tileSize - 8, tileSize - 8);

        int fontSize = Math.max(9, tileSize / 7);
        g2d.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, fontSize));
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        String txt = "OUTBOX";
        int tx = x + (tileSize - fm.stringWidth(txt)) / 2;
        int ty = y + (tileSize + fm.getAscent()) / 2 - 4;
        g2d.setColor(Color.BLACK);
        g2d.drawString(txt, tx + 1, ty + 1);
        g2d.setColor(c);
        g2d.drawString(txt, tx, ty);
        g2d.setStroke(new java.awt.BasicStroke(1));
    }

    private Color getPortalColor(TypeTerritoire tt) {
        return switch (tt) {
            case PORTE2, PORTE5 -> new Color(0, 255, 255);
            case PORTE3, PORTE4 -> new Color(255, 0, 255);
            default             -> Color.WHITE;
        };
    }

    private void drawPortal(Graphics2D g2d, int x, int y, int tileSize, Color color) {
        int margin = Math.max(4, tileSize / 8);
        int size   = tileSize - margin * 2;

        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        g2d.fillOval(x + margin - 2, y + margin - 2, size + 4, size + 4);

        g2d.setStroke(new java.awt.BasicStroke(Math.max(1, tileSize / 22),
                java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
        g2d.setColor(color);
        g2d.drawOval(x + margin, y + margin, size, size);

        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 120));
        int in = Math.max(2, tileSize / 11);
        g2d.drawOval(x + margin + in, y + margin + in, size - in * 2, size - in * 2);

        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
        int in2 = Math.max(4, tileSize / 6);
        g2d.fillOval(x + margin + in2, y + margin + in2, size - in2 * 2, size - in2 * 2);

        g2d.setStroke(new java.awt.BasicStroke(1));
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void rafraichirAffichage() {
        repaint();
    }
}
