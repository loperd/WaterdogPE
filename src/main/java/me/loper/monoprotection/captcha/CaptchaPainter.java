/*
 * Copyright 2021 WaterdogTEAM
 * Licensed under the GNU General Public License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.loper.monoprotection.captcha;

import com.google.common.base.Preconditions;
import lombok.NonNull;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Random;

final public class CaptchaPainter {
    public final Random rnd = new Random();
    public final Color background;

    public final int height;
    public final int width;

    public CaptchaPainter() {
        this(Color.WHITE, 128, 128);
    }

    public CaptchaPainter(Color background, int width, int height) {
        this.background = background;
        this.height = height;
        this.width = width;
    }

    public BufferedImage draw(@NonNull Font font, @NonNull Color foreground, @NonNull String text) {
        Preconditions.checkArgument(text.length() != 0, "No text given.");

        BufferedImage img = createImage();

        GraphicsFactory graphicsFactory = new GraphicsFactory(this.background, this.width, this.height);
        Graphics2D graphics = graphicsFactory.create(img, font, foreground);

        try {
            FontRenderContext fontRenderContext = graphics.getFontRenderContext();
            GlyphVector vector = graphics.getFont().createGlyphVector(fontRenderContext, text);

            transform(vector);
            draw(graphics, vector);
        } finally {
            graphics.dispose();
        }

        return postProcessImage(img);
    }

    private BufferedImage createImage() {
        return new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    }

    private void draw(Graphics2D graphics, GlyphVector vector) {
        draw(graphics, vector, true);
    }

    @SuppressWarnings("SameParameterValue")
    private void draw(Graphics2D g, GlyphVector vector, boolean outlineEnabled) {
        Rectangle bounds = vector.getPixelBounds(null, 0, height);

        float boundsHeight = (float) bounds.getHeight();
        float boundsWidth = (float) bounds.getWidth();

        float heightScale = height / boundsHeight * (rnd.nextFloat() / 20 + (outlineEnabled ? 0.68f : 0.75f)) * 1;
        float widthScale = width / boundsWidth * (rnd.nextFloat() / 20 + (outlineEnabled ? 0.89f : 0.92f)) * 1;

        float translateHeight = (height - boundsHeight * heightScale) / 2;
        float translateWidth = (width - boundsWidth * widthScale) / 2;

        g.translate(translateWidth, translateHeight);
        g.scale(widthScale, heightScale);

        float boundsX = (float) bounds.getX();
        float boundsY = (float) bounds.getY();

        if (outlineEnabled) {
            float y = Math.signum(rnd.nextFloat() - 0.5f) * 1 * height / 70 + height - boundsY;
            float x = Math.signum(rnd.nextFloat() - 0.5f) * 1 * width / 200 - boundsX;

            g.draw(vector.getOutline(x, y));
        }

        g.drawGlyphVector(vector, -boundsX, height - boundsY);
    }

    private void transform(GlyphVector v) {
        int glyphNum = v.getNumGlyphs();

        Rectangle2D preBounds = null;
        Point2D prePos = null;

        double rotateCur = (rnd.nextDouble() - 0.5) * Math.PI / 8;
        double rotateStep = Math.signum(rotateCur) * (rnd.nextDouble() * 3 * Math.PI / 8 / glyphNum);

        for (int fi = 0; fi < glyphNum; fi++) {
            AffineTransform tr = AffineTransform.getRotateInstance(rotateCur);

            if (rnd.nextDouble() < 0.25) {
                rotateStep *= -1;
            }

            rotateCur += rotateStep;
            v.setGlyphTransform(fi, tr);

            Point2D pos = v.getGlyphPosition(fi);
            Rectangle2D bounds = v.getGlyphVisualBounds(fi).getBounds2D();

            Point2D newPos = prePos == null
                    ? new Point2D.Double(pos.getX() - bounds.getX(), pos.getY())
                    : createPosition(preBounds, pos, bounds);

            v.setGlyphPosition(fi, newPos);

            prePos = newPos;
            preBounds = v.getGlyphVisualBounds(fi).getBounds2D();
        }
    }

    private Point2D createPosition(Rectangle2D preBounds, Point2D pos, Rectangle2D bounds) {
        double min = Math.min(preBounds.getWidth(), bounds.getWidth());
        double calculatedX = preBounds.getMaxX() + pos.getX() - bounds.getX();
        double doubleX = calculatedX - min * (rnd.nextDouble() / 20 + 0.27);

        return new Point2D.Double(doubleX, pos.getY());
    }

    private BufferedImage postProcessImage(BufferedImage img) {
        Rippler.AxisConfig vertical = new Rippler.AxisConfig(
                rnd.nextDouble() * 2 * Math.PI,
                (1 + rnd.nextDouble() * 2) * Math.PI, img.getHeight() / 10.0);
        Rippler.AxisConfig horizontal = new Rippler.AxisConfig(
                rnd.nextDouble() * 2 * Math.PI,
                (2 + rnd.nextDouble() * 2) * Math.PI, img.getWidth() / 100.0);
        Rippler rippler = new Rippler(vertical, horizontal);

        img = rippler.filter(img, createImage());

        float[] blurArray = new float[9];
        fillBlurArray(blurArray);

        ConvolveOp op = new ConvolveOp(new Kernel(3, 3, blurArray), ConvolveOp.EDGE_NO_OP, null);

        return op.filter(img, createImage());
    }

    private void fillBlurArray(float[] array) {
        float sum = 0;
        for (int fi = 0; fi < array.length; fi++) {
            array[fi] = rnd.nextFloat();
            sum += array[fi];
        }
        for (int fi = 0; fi < array.length; fi++) {
            array[fi] /= sum;
        }
    }
}
