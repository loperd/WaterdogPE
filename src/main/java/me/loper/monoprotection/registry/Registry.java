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

import me.loper.monoprotection.registry.loader.NbtRegistryLoader;
import me.loper.monoprotection.registry.loader.RegistryLoader;

import java.util.function.Consumer;

/**
 * A wrapper around a value which is loaded based on the output from the provided
 * {@link RegistryLoader}. This class is primarily designed to hold a registration
 * of some kind, however no limits are set on what it can hold, as long as the
 * specified RegistryLoader returns the same value type that is specified in the
 * generic.
 *
 * <p>
 * Below, a RegistryLoader is taken in the constructor. RegistryLoaders have two
 * generic types: the input, and the output. The input is what it takes in, whether
 * it be a string which references to a file, or nothing more than an integer. The
 * output is what it generates based on the input, and should be the same type as
 * the {@link M} generic specified in the registry.
 *
 * <p>
 * Registries can be very simple to create. Here is an example that simply parses a
 * number given a string:
 *
 * <pre>
 * {@code
 *     public static final SimpleRegistry<Integer> STRING_TO_INT = SimpleRegistry.create("5", Integer::parseInt);
 * }
 * </pre>
 *
 * <p>
 * This is a simple example which really wouldn't have much of a practical use,
 * however it demonstrates a fairly basic use case of how this system works. Typically
 * though, the first parameter would be a location of some sort, such as a file path
 * where the loader will load the mappings from. The NBT registry is a good reference
 * point for something both simple and practical. See {@link Registries#BIOMES_NBT} and
 * {@link NbtRegistryLoader}.
 *
 * @param <M> the value being held by the registry
 */
public abstract class Registry<M> {
    protected M mappings;

    /**
     * Creates a new instance of this class with the given input and
     * {@link RegistryLoader}. The input specified is what the registry
     * loader needs to take in.
     *
     * @param input the input
     * @param registryLoader the registry loader
     * @param <I> the input type
     */
    protected <I> Registry(I input, RegistryLoader<I, M> registryLoader) {
        this.mappings = registryLoader.load(input);
    }

    /**
     * Gets the underlying value held by this registry.
     *
     * @return the underlying value held by this registry.
     */
    public M get() {
        return this.mappings;
    }

    /**
     * Sets the underlying value held by this registry.
     * Clears any existing data associated with the previous
     * value.
     *
     * @param mappings the underlying value held by this registry
     */
    public void set(M mappings) {
        this.mappings = mappings;
    }

    /**
     * Registers what is specified in the given
     * {@link Consumer} into the underlying value.
     *
     * @param consumer the consumer
     */
    public void register(Consumer<M> consumer) {
        consumer.accept(this.mappings);
    }
}