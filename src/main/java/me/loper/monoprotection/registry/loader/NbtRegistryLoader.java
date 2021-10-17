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

package me.loper.monoprotection.registry.loader;

import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import dev.waterdog.waterdogpe.WaterdogPE;

import java.io.InputStream;

/**
 * Loads NBT data from the given resource path.
 */
public class NbtRegistryLoader implements RegistryLoader<String, NbtMap> {

    @Override
    public NbtMap load(String input) {
        InputStream stream = getResource(input);
        try (NBTInputStream nbtInputStream = NbtUtils.createNetworkReader(stream)) {
            return (NbtMap) nbtInputStream.readTag();
        } catch (Exception e) {
            throw new AssertionError("Failed to load registrations for " + input, e);
        }
    }

    private InputStream getResource(String resource) {
        InputStream stream = WaterdogPE.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            throw new AssertionError("Unable to find resource: " + resource);
        }
        return stream;
    }
}