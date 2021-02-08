import processing.core.PApplet;

import javax.swing.*;
import java.util.ArrayList;

public class Pipeline implements PixelFilter {
    private ArrayList<PixelFilter> filters = new ArrayList<>();

    public Pipeline() {
        ThresholdFilter thresholding = new ThresholdFilter(230);

//        ConvolutionFilter1 blur = new ConvolutionFilter1();
//        outline.setKernel( outline.blurKernel );

        ClusterFilter clustering = new ClusterFilter(2);

        filters.add( thresholding );
        filters.add( clustering );
    }


    @Override
    public DImage processImage(DImage img) {
        for (PixelFilter filter : filters) {
            img = filter.processImage(img);
        }
        return img;
    }

    @Override public void drawOverlay(PApplet window, DImage original, DImage filtered) {
    }
}