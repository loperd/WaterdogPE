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

/**
 * Represents a registry loader. {@link I} is the input value, which can be anything,
 * but is commonly a file path or something similar. {@link O} represents the output
 * type returned by this, which can also be anything. See {@link NbtRegistryLoader}
 * as a good and simple example of how this system works.
 *
 * @param <I> the input to load the registry from
 * @param <O> the output of the registry
 */
@FunctionalInterface
public interface RegistryLoader<I, O> {

    /**
     * Loads an output from the given input.
     *
     * @param input the input
     * @return the output
     */
    O load(I input);
}