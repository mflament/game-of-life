package org.yah.games.gameoflife.java2d;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.List;

import org.yah.games.gameoflife.java2d.universe.Universe;

public class UniverseImageProducer implements ImageProducer {

	private static final int HINTS = ImageConsumer.TOPDOWNLEFTRIGHT | ImageConsumer.SINGLEFRAME;

	private final List<ImageConsumer> consumers = new ArrayList<>();

	private Universe universe;

	private final ColorModel colorModel = ColorModel.getRGBdefault();

	public UniverseImageProducer(Universe universe) {
		super();
		this.universe = universe;
	}

	@Override
	public void addConsumer(ImageConsumer ic) {
		consumers.add(ic);
	}

	@Override
	public boolean isConsumer(ImageConsumer ic) {
		return consumers.contains(ic);
	}

	@Override
	public void removeConsumer(ImageConsumer ic) {
		consumers.remove(ic);
	}

	@Override
	public void startProduction(ImageConsumer ic) {
		consumers.add(ic);
		produce(ic);
	}

	public void update(Universe universe) {
		this.universe = universe;
		consumers.forEach(this::produce);
	}

	@Override
	public void requestTopDownLeftRightResend(ImageConsumer ic) {
		startProduction(ic);
	}

	private void produce(ImageConsumer ic) {
		ic.setHints(HINTS);
		ic.setColorModel(colorModel );
		ic.setDimensions(universe.width(), universe.height());
		ic.setPixels(0, 0, universe.width(), universe.height(), colorModel, universe.getPixels(), 0,
				universe.width());
		ic.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
	}

}
