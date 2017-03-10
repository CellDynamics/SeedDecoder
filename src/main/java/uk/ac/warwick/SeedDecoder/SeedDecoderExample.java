package uk.ac.warwick.SeedDecoder;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import uk.ac.warwick.wsbc.quimp.plugin.randomwalk.Point;
import uk.ac.warwick.wsbc.quimp.plugin.randomwalk.RandomWalkException;
import uk.ac.warwick.wsbc.quimp.plugin.randomwalk.RandomWalkSegmentation;
import uk.ac.warwick.wsbc.quimp.plugin.randomwalk.RandomWalkSegmentation.Seeds;

/**
 * Example how to use QuimP API for decoding seed images.
 * 
 * Provided is the RGB image with scribbled pixels. As result we will get list of annotated pixels.
 *
 */
public class SeedDecoderExample {
  /**
   * Entry point.
   * 
   * @param args
   * @throws RandomWalkException
   */
  public static void main(String[] args) throws RandomWalkException {
    // load RGB image, green pixels are background, red foreground
    ImagePlus seedImage = IJ.openImage("src/main/resources/segtest_small_seed.tif");
    seedImage.show();
    // decode red and green pixels and produce two separate images with FG pixels and BG pixels
    // only.
    Map<Seeds, ImageProcessor> ret =
            RandomWalkSegmentation.decodeSeeds(seedImage, Color.RED, Color.GREEN);
    // ret is a Map that is addressed by enum Seeds
    // here is what decodeSeed returned (temporarily converted to ImagePlus)
    new ImagePlus("FG", ret.get(Seeds.FOREGROUND)).show();
    new ImagePlus("FG", ret.get(Seeds.BACKGROUND)).show();
    // now convert those images into list of points
    Map<Seeds, List<Point>> list = RandomWalkSegmentation.convertToList(ret);
    // do something with result
    System.out.println(list.get(Seeds.FOREGROUND).toString());
  }
}
