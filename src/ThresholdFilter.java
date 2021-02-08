import processing.core.PApplet;

public class ThresholdFilter implements PixelFilter {
    private int threshold = 127;

    public ThresholdFilter(int threshold) {
        if (threshold >= 0 && threshold <= 255) {
            this.threshold = threshold;
        }
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] > threshold) {
                    grid[r][c] = 255;
                } else {
                    grid[r][c] = 0;
                }
            }
        }

        img.setPixels(grid);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
    }
}

