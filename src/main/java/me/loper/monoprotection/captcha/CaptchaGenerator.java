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

import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.loper.monoprotection.MonoProtection;
import me.loper.monoprotection.palette.ColorPalette;

import java.awt.*;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CaptchaGenerator {

    private final Collection<Font> fonts = new ObjectArrayList<>();
    private final CaptchaPainter painter = new CaptchaPainter();
    private final Random random = ThreadLocalRandom.current();

    public CaptchaGenerator(Collection<Font> fonts) {
        this.fonts.addAll(fonts);
    }

    public void addFont(Font font) {
        this.fonts.add(font);
    }

    public Captcha generate(String answear) {
        Font font = Iterables.get(this.fonts, this.random.nextInt(fonts.size()));

        int id = ThreadLocalRandom.current().nextInt(0, ColorPalette.count());
        Color color = ColorPalette.fromId(id).getColor();

        return new Captcha(this.painter.draw(font, color, answear), answear);
    }

    public static class Worker implements Runnable {

        private final MonoProtection monoProtection;

        public Worker(MonoProtection monoProtection) {
            this.monoProtection = monoProtection;
        }

        @Override
        public void run() {
            try {
                Captcha captcha = this.monoProtection.getGenerator().generate(this.createAnswear());

                this.monoProtection.getCaptchaStore().addCaptcha(captcha);
            } catch (IllegalStateException ex) {
                this.monoProtection.getLogger().critical("Error occured while creating Captcha.", ex);
            }
        }

        private String createAnswear() {
            return String.valueOf(ThreadLocalRandom.current().nextLong(1111, 9999));
        }
    }
}
