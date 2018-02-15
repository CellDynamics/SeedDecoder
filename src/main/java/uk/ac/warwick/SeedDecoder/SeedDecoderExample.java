package uk.ac.warwick.SeedDecoder;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.github.celldynamics.quimp.plugin.randomwalk.Point;
import com.github.celldynamics.quimp.plugin.randomwalk.RandomWalkException;
import com.github.celldynamics.quimp.plugin.randomwalk.RandomWalkSegmentation.SeedTypes;
import com.github.celldynamics.quimp.plugin.randomwalk.SeedProcessor;
import com.github.celldynamics.quimp.plugin.randomwalk.Seeds;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 * Example how to use QuimP API for decoding seed images.
 * 
 * <p>API allows you to convert seeds from different sources, such as:
 * <ol>
 * <li>RGB image, where foreground and background are given by specified colors. This mode rather
 * supports only one FG and one BG.
 * <li>Grayscale image, where objects are specified by pixels with different gray level. This mode
 * supports multiple FG objects
 * <li>ImageJ ROI objects, also supports multiple FGs.
 * </ol>
 * After conversion seeds are represented as {@link Seeds} object that can be converted to either
 * binary images (each seed in separate image) or list of points in image coordinates.
 * 
 * @see com.github.celldynamics.quimp.plugin.randomwalk.SeedProcessor
 * @see com.github.celldynamics.quimp.plugin.randomwalk.Seeds
 */
public class SeedDecoderExample {
  /**
   * Entry point.
   * 
   * @param args
   * @throws RandomWalkException
   */
  public static void main(String[] args) throws RandomWalkException {
    // Example of RGB seed decoding
    // open exemplary seed
    ImagePlus seedImage = IJ.openImage("src/main/resources/segtest_small_seed.tif");
    // show it
    seedImage.show();
    // decode red (FG) and green (BG) pixels and produce two separate images with FG pixels and BG
    // pixels only.
    Seeds ret = SeedProcessor.decodeSeedsfromRgb(seedImage,
            Arrays.asList(new Color[] { Color.RED }), Color.GREEN);
    // ret is a Map that is addressed by enum SeedsType, e.g. to get FG seeds as image:
    List<ImageProcessor> foregroundSeedImage = ret.get(SeedTypes.FOREGROUNDS);
    // Returned is List but here it contains only one element as we have only one FG seed (can be
    // empty if conversion was unsuccessful)
    // display it (need to be converted to ImagePlus first).
    new ImagePlus("foreground", foregroundSeedImage.get(0)).show();
    // same for background
    List<ImageProcessor> backgroundSeedImage = ret.get(SeedTypes.BACKGROUND);
    new ImagePlus("background", backgroundSeedImage.get(0)).show();
    // note that Seeds object may not contain SeedTypes.BACKGROUND (for multi object seeds)

    // convert Seeds to list of points - points are coordinates of white pixels from above maps (in
    // image space)
    List<List<Point>> foregroundSeedPoints = ret.convertToList(SeedTypes.FOREGROUNDS);
    // again we have only one FG seed so outer List has only one element
    System.out.println(foregroundSeedPoints.get(0));

    // other things that can be useful

    ImageProcessor preview = SeedProcessor.getSeedsAsGrayscale(ret); // convert to grayscale preview
    new ImageJ(); // bring IJ window and tools up
    new ImagePlus("preview", preview).show(); // curves need to be adjusted

  }
}
