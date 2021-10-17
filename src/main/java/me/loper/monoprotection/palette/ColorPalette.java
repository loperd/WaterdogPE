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

package me.loper.monoprotection.palette;

import java.awt.*;

public enum ColorPalette {

    COLOR_0(-1, -1, -1),
    COLOR_1(-1, -1, -1),
    COLOR_2(-1, -1, -1),
    COLOR_3(-1, -1, -1),
    COLOR_4(39, 125, 89),
    COLOR_5(48, 153, 109),
    COLOR_6(56, 178, 127),
    COLOR_7(29, 94, 67),
    COLOR_8(115, 164, 174),
    COLOR_9(140, 201, 213),
    COLOR_10(163, 233, 247),
    COLOR_11(86, 123, 130),
    COLOR_12(140, 140, 140),
    COLOR_13(171, 171, 171),
    COLOR_14(199, 199, 199),
    COLOR_15(105, 105, 105),
    COLOR_16(0, 0, 180),
    COLOR_17(0, 0, 220),
    COLOR_18(0, 0, 255),
    COLOR_19(0, 0, 135),
    COLOR_20(180, 112, 112),
    COLOR_21(220, 138, 138),
    COLOR_22(255, 160, 160),
    COLOR_23(135, 84, 84),
    COLOR_24(117, 117, 117),
    COLOR_25(144, 144, 144),
    COLOR_26(167, 167, 167),
    COLOR_27(88, 88, 88),
    COLOR_28(0, 87, 0),
    COLOR_29(0, 106, 0),
    COLOR_30(0, 124, 0),
    COLOR_31(0, 65, 0),
    COLOR_32(180, 180, 180),
    COLOR_33(220, 220, 220),
    COLOR_34(255, 255, 255),
    COLOR_35(135, 135, 135),
    COLOR_36(129, 118, 115),
    COLOR_37(158, 144, 141),
    COLOR_38(184, 168, 164),
    COLOR_39(97, 88, 86),
    COLOR_40(54, 76, 106),
    COLOR_41(66, 94, 130),
    COLOR_42(77, 109, 151),
    COLOR_43(40, 57, 79),
    COLOR_44(79, 79, 79),
    COLOR_45(96, 96, 96),
    COLOR_46(112, 112, 112),
    COLOR_47(59, 59, 59),
    COLOR_48(180, 45, 45),
    COLOR_49(220, 55, 55),
    COLOR_50(255, 64, 64),
    COLOR_51(135, 33, 33),
    COLOR_52(50, 84, 100),
    COLOR_53(62, 102, 123),
    COLOR_54(72, 119, 143),
    COLOR_55(38, 63, 75),
    COLOR_56(172, 177, 180),
    COLOR_57(211, 217, 220),
    COLOR_58(245, 252, 255),
    COLOR_59(129, 133, 135),
    COLOR_60(36, 89, 152),
    COLOR_61(44, 109, 186),
    COLOR_62(51, 127, 216),
    COLOR_63(27, 67, 114),
    COLOR_64(152, 53, 125),
    COLOR_65(186, 65, 153),
    COLOR_66(216, 76, 178),
    COLOR_67(114, 40, 94),
    COLOR_68(152, 108, 72),
    COLOR_69(186, 132, 88),
    COLOR_70(216, 153, 102),
    COLOR_71(114, 81, 54),
    COLOR_72(36, 161, 161),
    COLOR_73(44, 197, 197),
    COLOR_74(51, 229, 229),
    COLOR_75(27, 121, 121),
    COLOR_76(17, 144, 89),
    COLOR_77(21, 176, 109),
    COLOR_78(25, 204, 127),
    COLOR_79(13, 108, 67),
    COLOR_80(116, 89, 170),
    COLOR_81(142, 109, 208),
    COLOR_82(165, 127, 242),
    COLOR_83(87, 67, 128),
    COLOR_84(53, 53, 53),
    COLOR_85(65, 65, 65),
    COLOR_86(76, 76, 76),
    COLOR_87(40, 40, 40),
    COLOR_88(108, 108, 108),
    COLOR_89(132, 132, 132),
    COLOR_90(153, 153, 153),
    COLOR_91(81, 81, 81),
    COLOR_92(108, 89, 53),
    COLOR_93(132, 109, 65),
    COLOR_94(153, 127, 76),
    COLOR_95(81, 67, 40),
    COLOR_96(125, 44, 89),
    COLOR_97(153, 54, 109),
    COLOR_98(178, 63, 127),
    COLOR_99(94, 33, 67),
    COLOR_100(125, 53, 36),
    COLOR_101(153, 65, 44),
    COLOR_102(178, 76, 51),
    COLOR_103(94, 40, 27),
    COLOR_104(36, 53, 72),
    COLOR_105(44, 65, 88),
    COLOR_106(51, 76, 102),
    COLOR_107(27, 40, 54),
    COLOR_108(36, 89, 72),
    COLOR_109(44, 109, 88),
    COLOR_110(51, 127, 102),
    COLOR_111(27, 67, 54),
    COLOR_112(36, 36, 108),
    COLOR_113(44, 44, 132),
    COLOR_114(51, 51, 153),
    COLOR_115(27, 27, 81),
    COLOR_116(17, 17, 17),
    COLOR_117(21, 21, 21),
    COLOR_118(25, 25, 25),
    COLOR_119(13, 13, 13),
    COLOR_120(54, 168, 176),
    COLOR_121(66, 205, 215),
    COLOR_122(77, 238, 250),
    COLOR_123(40, 126, 132),
    COLOR_124(150, 154, 64),
    COLOR_125(183, 188, 79),
    COLOR_126(213, 219, 92),
    COLOR_127(112, 115, 48),
    COLOR_128(180, 90, 52),
    COLOR_129(220, 110, 63),
    COLOR_130(255, 128, 74),
    COLOR_131(135, 67, 39),
    COLOR_132(40, 153, 0),
    COLOR_133(50, 187, 0),
    COLOR_134(58, 217, 0),
    COLOR_135(30, 114, 0),
    COLOR_136(34, 60, 91),
    COLOR_137(42, 74, 111),
    COLOR_138(49, 86, 129),
    COLOR_139(25, 45, 68),
    COLOR_140(0, 1, 79),
    COLOR_141(0, 1, 96),
    COLOR_142(0, 2, 112),
    COLOR_143(0, 1, 59),
    COLOR_144(113, 124, 147),
    COLOR_145(138, 152, 180),
    COLOR_146(161, 177, 209),
    COLOR_147(85, 93, 110),
    COLOR_148(25, 57, 112),
    COLOR_149(31, 70, 137),
    COLOR_150(36, 82, 159),
    COLOR_151(19, 43, 84),
    COLOR_152(76, 61, 105),
    COLOR_153(93, 75, 128),
    COLOR_154(108, 87, 149),
    COLOR_155(57, 46, 78),
    COLOR_156(97, 76, 79),
    COLOR_157(119, 93, 96),
    COLOR_158(138, 108, 112),
    COLOR_159(73, 57, 59),
    COLOR_160(25, 93, 131),
    COLOR_161(31, 114, 160),
    COLOR_162(36, 133, 186),
    COLOR_163(19, 70, 98),
    COLOR_164(37, 82, 72),
    COLOR_165(45, 100, 88),
    COLOR_166(53, 117, 103),
    COLOR_167(28, 61, 54),
    COLOR_168(55, 54, 112),
    COLOR_169(67, 66, 138),
    COLOR_170(78, 77, 160),
    COLOR_171(41, 40, 84),
    COLOR_172(24, 28, 40),
    COLOR_173(30, 35, 49),
    COLOR_174(35, 41, 57),
    COLOR_175(18, 21, 30),
    COLOR_176(69, 75, 95),
    COLOR_177(84, 92, 116),
    COLOR_178(98, 107, 135),
    COLOR_179(51, 56, 71),
    COLOR_180(64, 64, 61),
    COLOR_181(79, 79, 75),
    COLOR_182(92, 92, 87),
    COLOR_183(48, 48, 46),
    COLOR_184(62, 51, 86),
    COLOR_185(75, 62, 105),
    COLOR_186(88, 73, 122),
    COLOR_187(46, 38, 64),
    COLOR_188(64, 43, 53),
    COLOR_189(79, 53, 65),
    COLOR_190(92, 62, 76),
    COLOR_191(48, 32, 40),
    COLOR_192(24, 35, 53),
    COLOR_193(30, 43, 65),
    COLOR_194(35, 50, 76),
    COLOR_195(18, 26, 40),
    COLOR_196(29, 57, 53),
    COLOR_197(36, 70, 65),
    COLOR_198(42, 82, 76),
    COLOR_199(22, 43, 40),
    COLOR_200(32, 42, 100),
    COLOR_201(39, 51, 122),
    COLOR_202(46, 60, 142),
    COLOR_203(24, 31, 75),
    COLOR_204(11, 15, 26),
    COLOR_205(13, 18, 31),
    COLOR_206(16, 22, 37),
    COLOR_207(8, 11, 19),
    COLOR_208(34, 33, 133),
    COLOR_209(42, 41, 163),
    COLOR_210(49, 48, 189),
    COLOR_211(25, 25, 100),
    COLOR_212(68, 44, 104),
    COLOR_213(83, 54, 127),
    COLOR_214(97, 63, 148),
    COLOR_215(51, 33, 78),
    COLOR_216(20, 17, 64),
    COLOR_217(25, 21, 79),
    COLOR_218(29, 25, 92),
    COLOR_219(15, 13, 48),
    COLOR_220(94, 88, 15),
    COLOR_221(115, 108, 18),
    COLOR_222(134, 126, 22),
    COLOR_223(70, 66, 11),
    COLOR_224(98, 100, 40),
    COLOR_225(120, 122, 50),
    COLOR_226(140, 142, 58),
    COLOR_227(74, 75, 30),
    COLOR_228(43, 31, 60),
    COLOR_229(53, 37, 74),
    COLOR_230(62, 44, 86),
    COLOR_231(32, 23, 45),
    COLOR_232(93, 127, 14),
    COLOR_233(114, 155, 17),
    COLOR_234(133, 180, 20),
    COLOR_235(70, 95, 10),
    COLOR_236(70, 70, 70),
    COLOR_237(86, 86, 86),
    COLOR_238(100, 100, 100),
    COLOR_239(52, 52, 52),
    COLOR_240(103, 123, 152),
    COLOR_241(126, 150, 186),
    COLOR_242(147, 175, 216),
    COLOR_243(77, 92, 114),
    COLOR_244(105, 117, 89),
    COLOR_245(129, 144, 109),
    COLOR_246(150, 167, 127),
    COLOR_247(79, 88, 67);

    private static final ColorPalette[] VALUES = values();

    private final int value;

    ColorPalette(int red, int green, int blue) {
        int alpha = 255;

        if (red == -1 && green == -1 && blue == -1) {
            alpha = 0; // transparent
        }

        this.value = ((alpha & 0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                (blue & 0xFF);
    }

    public static int count() {
        return values().length;
    }

    public int getARGB() {
        return value;
    }

    public static int toABGR(int argb) {
        int result = argb & 0xFF00FF00;

        result |= (argb << 16) & 0x00FF0000; // B to R
        result |= (argb >>> 16) & 0xFF; // R to B

        return result & 0xFFFFFFFF;

    }

    public int getABGR() {
        return toABGR(this.value);
    }

    public static ColorPalette fromId(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : COLOR_0;
    }

    public Color getColor() {
        return new Color(this.value, true);
    }
}
