package xap.lui.core.filemgr;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageHelper {
	
	public static BufferedImage getnewimage(BufferedImage srcBufImage,
			int width, int height,boolean isautoscale) {
		if(srcBufImage.getWidth() == 0 || srcBufImage.getHeight() == 0) return null;
		BufferedImage bufTarget = null;
		double sx = 1;
		double sy = 1;
		if(width < 1)
			width = srcBufImage.getWidth();
		if(height < 1)
			height = srcBufImage.getHeight();
		if(isautoscale){
			double swh =(double) srcBufImage.getWidth()/srcBufImage.getHeight();
			int maxwidth = (int)(swh * height);
			if(maxwidth > width){
				height = (int) (width /swh);
			}else{
				width = maxwidth;
			}
		}
		
		sx = (double) width / srcBufImage.getWidth();
		sy = (double) height / srcBufImage.getHeight();
		

		int type = srcBufImage.getType();
		if (type == BufferedImage.TYPE_CUSTOM) {
			ColorModel cm = srcBufImage.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width,
					height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			bufTarget = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			bufTarget = new BufferedImage(width, height, type);

		Graphics2D g = bufTarget.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(srcBufImage,
				AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return bufTarget;
	}
}
