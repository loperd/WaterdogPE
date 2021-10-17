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

import me.loper.monoprotection.registry.loader.RegistryLoader;

import java.util.function.Supplier;

/**
 * A simple registry with no defined mapping or input type. Designed to allow
 * for simple registrations of any given type without restrictions on what
 * the input or output can be.
 *
 * @param <M> the value being held by the registry
 */
public class SimpleRegistry<M> extends Registry<M> {
    private <I> SimpleRegistry(I input, RegistryLoader<I, M> registryLoader) {
        super(input, registryLoader);
    }

    /**
     * Creates a new registry with the given {@link RegistryLoader} supplier. The
     * input type is not specified here, meaning the loader return type is either
     * predefined, or the registry is populated at a later point.
     *
     * @param registryLoader the registry loader supplier
     * @param <I> the input type
     * @param <M> the returned mappings type
     * @return a new registry with the given RegistryLoader supplier
     */
    public static <I, M> SimpleRegistry<M> create(Supplier<RegistryLoader<I, M>> registryLoader) {
        return new SimpleRegistry<>(null, registryLoader.get());
    }

    /**
     * Creates a new registry with the given {@link RegistryLoader} supplier
     * and input.
     *
     * @param input the input
     * @param registryLoader the registry loader supplier
     * @param <I> the input type
     * @param <M> the returned mappings type
     * @return a new registry with the given RegistryLoader supplier
     */
    public static <I, M> SimpleRegistry<M> create(I input, Supplier<RegistryLoader<I, M>> registryLoader) {
        return new SimpleRegistry<>(input, registryLoader.get());
    }

    /**
     * Creates a new registry with the given {@link RegistryLoader}. The
     * input type is not specified here, meaning the loader return type is either
     * predefined, or the registry is populated at a later point.
     *
     * @param registryLoader the registry loader
     * @param <I> the input type
     * @param <M> the returned mappings type
     * @return a new registry with the given RegistryLoader supplier
     */
    public static <I, M> SimpleRegistry<M> create(RegistryLoader<I, M> registryLoader) {
        return new SimpleRegistry<>(null, registryLoader);
    }

    /**
     * Creates a new registry with the given {@link RegistryLoader} and input.
     *
     * @param input the input
     * @param registryLoader the registry loader
     * @param <I> the input type
     * @param <M> the returned mappings type
     * @return a new registry with the given RegistryLoader supplier
     */
    public static <I, M> SimpleRegistry<M> create(I input, RegistryLoader<I, M> registryLoader) {
        return new SimpleRegistry<>(input, registryLoader);
    }
}