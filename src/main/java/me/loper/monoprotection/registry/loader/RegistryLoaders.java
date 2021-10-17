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

import java.util.function.Supplier;

/**
 * Holds common {@link RegistryLoader}s or utility methods surrounding them.
 */
public class RegistryLoaders {
    /**
     * The {@link RegistryLoader} responsible for loading NBT.
     */
    public static NbtRegistryLoader NBT = new NbtRegistryLoader();

    /**
     * Wraps the surrounding {@link Supplier} in a {@link RegistryLoader} which does
     * not take in any input value.
     *
     * @param supplier the supplier
     * @param <V> the value
     * @return a RegistryLoader wrapping the given Supplier
     */
    public static <V> RegistryLoader<Object, V> empty(Supplier<V> supplier) {
        return input -> supplier.get();
    }
}