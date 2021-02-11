import processing.core.PApplet;

public class ConvolutionFilter implements PixelFilter {
    private int size;

    public ConvolutionFilter() {};

    public ConvolutionFilter(int size) {
        this.size = size;
    }


    private double[][] blurKernel3x3 =
            {   {1.0 / 9, 1.0 / 9, 1.0 / 9},
                {1.0 / 9, 1.0 / 9, 1.0 / 9},
                {1.0 / 9, 1.0 / 9, 1.0 / 9} };

    private double[][] outlineKernel3x3 =
            {   {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1} };

    private double[][] embossKernel3x3 =
            {   {-2, -1, 0},
                {-1, 1, 1},
                {0, 1, 2} };

    private double[][] Gaussian5x5 =
            {   {1, 4, 7, 4, 1},
                {4, 16, 26, 16, 4},
                {7, 26, 41, 26, 7},
                {4, 16, 26, 16, 4},
                {1, 4, 7, 4, 1} };

    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();
        short[][] outputPixels = img.getBWPixelGrid();  // <-- overwrite these values
        double[][] kernel = genBlurBox( size );

        int border = kernel.length/2;
        for (int r = border; r < img.getHeight() - border; r++) {
            for (int c = border; c < img.getWidth() - border; c++) {
                outputPixels[r][c] = findWeightedAverageAt(r, c, pixels, kernel);
            }
        }

        img.setPixels(outputPixels);
        return img;
    }

    private short findWeightedAverageAt(int r, int c, short[][] pixels, double[][] kernel) {
        int width = kernel.length/2;

        short sum = 0;
        double sum_kernel = 0;
        for (int dr = -width; dr <= width ; dr++) {
            for (int dc = -width; dc <= width; dc++) {
                sum += pixels[r+dr][c+dc] * kernel[width+dr][width+dc];
                sum_kernel += kernel[width+dr][width+dc];
            }
        }
        return (short)(sum/sum_kernel);
    }

    private double[][] genBlurBox(int n) {
        // Set size to 3x3 if n is < than 3 or even
        if (n < 3 || n%2 == 0 ){
            n = 3;
        }
        double[][] out = new double[n][n];
        double val = 1.0/(n*n);
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                out[r][c] = val;
            }
        }
        return out;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
    }
}