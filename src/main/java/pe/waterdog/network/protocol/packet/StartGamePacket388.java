/**
 * Copyright 2020 WaterdogTEAM
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pe.waterdog.network.protocol.packet;

import com.nukkitx.nbt.NbtList;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;

public class StartGamePacket388 extends StartGamePacket {

    private byte[] blockPaletteData;
    private boolean changedPalette = false;

    public void setBlockPaletteData(byte[] blockPaletteData) {
        this.blockPaletteData = blockPaletteData;
    }

    public byte[] getBlockPaletteData() {
        return this.blockPaletteData;
    }

    public void setChangedPalette(boolean changedPalette) {
        this.changedPalette = changedPalette;
    }

    public boolean isChangedPalette() {
        return this.changedPalette;
    }

    @Override
    public void setBlockPalette(NbtList<NbtMap> blockPalette) {
        if (this.getBlockPalette() != blockPalette){
            this.changedPalette = true;
            super.setBlockPalette(blockPalette);
        }
    }
}
