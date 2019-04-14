package org.yah.games.gameoflife.java2d;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.List;

import org.yah.games.gameoflife.universe.Universe;

public class UniverseImageProducer implements ImageProducer {

	private static final int HINTS = ImageConsumer.TOPDOWNLEFTRIGHT | ImageConsumer.SINGLEFRAME;

	private final List<ImageConsumer> consumers = new ArrayList<>();

	private final ColorModel colorModel = ColorModel.getRGBdefault();

	private Universe lastUniverse;

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
		if (lastUniverse != null)
			produce(ic, lastUniverse);
	}

	public void update(Universe universe) {
		consumers.forEach(c -> produce(c, universe));
		lastUniverse = universe;
	}

	@Override
	public void requestTopDownLeftRightResend(ImageConsumer ic) {
		startProduction(ic);
	}

	private void produce(ImageConsumer ic, Universe universe) {
		ic.setHints(HINTS);
		ic.setColorModel(colorModel);
		ic.setDimensions(universe.width(), universe.height());
		ic.setPixels(0, 0, universe.width(), universe.height(), colorModel, universe.getPixels(), 0,
				universe.width());
		ic.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
	}

}
