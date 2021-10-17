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

import me.loper.monoprotection.palette.ColorPalette;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class Captcha {
    private final String answear;

    private final int[] colors;
    private final byte[] imageBytes;

    public Captcha(BufferedImage image, String answear) {
        this.answear = answear;

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", stream);

            this.imageBytes = stream.toByteArray();
        } catch (Throwable ex) {
            throw new IllegalStateException("Error occured while writing image to stream.", ex);
        }

        int[] colors = new int[image.getWidth() * image.getHeight()];

        int id = 0;
        for (int y = 0; y < image.getWidth(); y++) {
            for (int x = 0; x < image.getHeight(); x++) {
                colors[id++] = ColorPalette.toABGR(image.getRGB(x, y));
            }
        }

        this.colors = colors;

        image.flush();
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public int[] getColors() {
        return colors;
    }

    public String getAnswear() {
        return answear;
    }

    public boolean isValid(String answear) {
        return this.answear.equals(answear);
    }
}
