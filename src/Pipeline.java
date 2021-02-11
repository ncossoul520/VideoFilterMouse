import javafx.scene.effect.BoxBlur;
import processing.core.PApplet;

import javax.swing.*;
import java.util.ArrayList;

public class Pipeline implements PixelFilter {
    private ArrayList<PixelFilter> filters = new ArrayList<>();

    public Pipeline() {
        ConvolutionFilter blurring = new ConvolutionFilter(7);
        ThresholdFilter thresholding = new ThresholdFilter(230);
        ClusterFilter clustering = new ClusterFilter(2);

        filters.add( blurring );
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
        for ( PixelFilter filter : filters) {
            filter.drawOverlay(window, original, filtered);
        }
    }
}