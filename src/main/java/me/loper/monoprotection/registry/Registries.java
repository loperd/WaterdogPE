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

package me.loper.monoprotection.registry;

import com.nukkitx.nbt.NbtMap;
import me.loper.monoprotection.registry.loader.RegistryLoaders;

/**
 * Holds all the common registries in Geyser.
 */
public class Registries {
    /**
     * A registry holding a CompoundTag of all the known biomes.
     */
    public static final SimpleRegistry<NbtMap> BIOMES_NBT = SimpleRegistry.create("biome_definitions.dat", RegistryLoaders.NBT);

    /**
     * A registry holding a CompoundTag of the known entity identifiers.
     */
    public static final SimpleRegistry<NbtMap> ENTITY_IDENTIFIERS = SimpleRegistry.create("entity_identifiers.dat", RegistryLoaders.NBT);

}