package com.example.booksriver.theme

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.example.booksriver.data.Singleton

class AllColors {
    companion object {
        val NUM_COLORS = 19

        val IDX_50 = 0
        val IDX_100 = 1
        val IDX_200 = 2
        val IDX_300 = 3
        val IDX_400 = 4
        val IDX_500 = 5
        val IDX_600 = 6
        val IDX_700 = 7
        val IDX_800 = 8
        val IDX_900 = 9

        val WHITE = -1
        val BLACK = -16777216

        val RED = intArrayOf(
            -0x1412,
            -0x322e,
            -0x106566,
            -0x1a8c8d,
            -0x10acb0,
            -0xbbcca,
            -0x1ac6cb,
            -0x2cd0d1,
            -0x39d7d8,
            -0x48e3e4
        )
        val DEEP_PURPLE = intArrayOf(
            -0x12180a,
            -0x2e3b17,
            -0x4c6225,
            -0x6a8a33,
            -0x81a83e,
            -0x98c549,
            -0xa1ca4f,
            -0xaed258,
            -0xbad860,
            -0xcee46e
        )
        val LIGHT_BLUE = intArrayOf(
            -0x1e0a02,
            -0x4c1a04,
            -0x7e2b06,
            -0xb03c09,
            -0xd6490a,
            -0xfc560c,
            -0xfc641b,
            -0xfd772f,
            -0xfd8843,
            -0xfea865
        )
        val GREEN = intArrayOf(
            -0x170a17,
            -0x371937,
            -0x5a2959,
            -0x7e387c,
            -0x994496,
            -0xb350b0,
            -0xbc5fb9,
            -0xc771c4,
            -0xd182ce,
            -0xe4a1e0
        )
        val YELLOW = intArrayOf(
            -0x219,
            -0x63c,
            -0xa63,
            -0xe8a,
            -0x11a8,
            -0x14c5,
            -0x227cb,
            -0x43fd3,
            -0x657db,
            -0xa80e9
        )
        val DEEP_ORANGE = intArrayOf(
            -0x41619,
            -0x3344,
            -0x546f,
            -0x759b,
            -0x8fbd,
            -0xa8de,
            -0xbaee2,
            -0x19b5e7,
            -0x27bceb,
            -0x40c9f4
        )
        val BLUE_GREY = intArrayOf(
            -0x13100f,
            -0x302724,
            -0x4f413b,
            -0x6f5b52,
            -0x876f64,
            -0x9f8275,
            -0xab9186,
            -0xbaa59c,
            -0xc8b8b1,
            -0xd9cdc8
        )
        val PINK = intArrayOf(
            -0x31b14,
            -0x74430,
            -0xb704f,
            -0xf9d6e,
            -0x13bf86,
            -0x16e19d,
            -0x27e4a0,
            -0x3de7a5,
            -0x52eba9,
            -0x77f1b1
        )
        val INDIGO = intArrayOf(
            -0x17150a,
            -0x3a3517,
            -0x605726,
            -0x867935,
            -0xa39440,
            -0xc0ae4b,
            -0xc6b655,
            -0xcfc061,
            -0xd7ca6d,
            -0xe5dc82
        )
        val CYAN = intArrayOf(
            -0x1f0806,
            -0x4d140e,
            -0x7f2116,
            -0xb22f1f,
            -0xd93926,
            -0xff432c,
            -0xff533f,
            -0xff6859,
            -0xff7c71,
            -0xff9f9c
        )
        val LIGHT_GREEN = intArrayOf(
            -0xe0717,
            -0x231238,
            -0x3a1e5b,
            -0x512a7f,
            -0x63339b,
            -0x743cb6,
            -0x834cbe,
            -0x9760c8,
            -0xaa74d1,
            -0xcc96e2
        )
        val AMBER = intArrayOf(
            -0x71f,
            -0x134d,
            -0x1f7e,
            -0x2ab1,
            -0x35d8,
            -0x3ef9,
            -0x4d00,
            -0x6000,
            -0x7100,
            -0x9100
        )
        val BROWN = intArrayOf(
            -0x101417,
            -0x283338,
            -0x43555c,
            -0x5e7781,
            -0x72919d,
            -0x86aab8,
            -0x92b3bf,
            -0xa2bfc9,
            -0xb1cbd2,
            -0xc1d8dd
        )
        val PURPLE = intArrayOf(
            -0xc1a0b,
            -0x1e4119,
            -0x316c28,
            -0x459738,
            -0x54b844,
            -0x63d850,
            -0x71db56,
            -0x84e05e,
            -0x95e466,
            -0xb5eb74
        )
        val BLUE = intArrayOf(
            -0x1c0d03,
            -0x442105,
            -0x6f3507,
            -0x9b4a0a,
            -0xbd5a0b,
            -0xde690d,
            -0xe1771b,
            -0xe6892e,
            -0xea9a40,
            -0xf2b85f
        )
        val TEAL = intArrayOf(
            -0x1f0d0f,
            -0x4d2025,
            -0x7f343c,
            -0xb24954,
            -0xd95966,
            -0xff6978,
            -0xff7685,
            -0xff8695,
            -0xff96a4,
            -0xffb2c0
        )
        val LIME = intArrayOf(
            -0x60419,
            -0xf0b3d,
            -0x191164,
            -0x23188b,
            -0x2b1ea9,
            -0x3223c7,
            -0x3f35cd,
            -0x504bd5,
            -0x6162dc,
            -0x7d88e9
        )
        val ORANGE = intArrayOf(
            -0xc20,
            -0x1f4e,
            -0x3380,
            -0x48b3,
            -0x58da,
            -0x6800,
            -0x47400,
            -0xa8400,
            -0x109400,
            -0x19af00
        )
        val GREY = intArrayOf(
            -0x50506,
            -0xa0a0b,
            -0x111112,
            -0x1f1f20,
            -0x424243,
            -0x616162,
            -0x8a8a8b,
            -0x9e9e9f,
            -0xbdbdbe,
            -0xdededf
        )

        val SHADE_50 = intArrayOf(
            RED[IDX_50],
            DEEP_PURPLE[IDX_50],
            LIGHT_BLUE[IDX_50],
            GREEN[IDX_50],
            YELLOW[IDX_50],
            DEEP_ORANGE[IDX_50],
            BLUE_GREY[IDX_50],
            PINK[IDX_50],
            INDIGO[IDX_50],
            CYAN[IDX_50],
            LIGHT_GREEN[IDX_50],
            AMBER[IDX_50],
            BROWN[IDX_50],
            PURPLE[IDX_50], BLUE[IDX_50], TEAL[IDX_50], LIME[IDX_50], ORANGE[IDX_50], GREY[IDX_50]
        )
        val SHADE_100 = intArrayOf(
            RED[IDX_100],
            DEEP_PURPLE[IDX_100],
            LIGHT_BLUE[IDX_100],
            GREEN[IDX_100],
            YELLOW[IDX_100],
            DEEP_ORANGE[IDX_100],
            BLUE_GREY[IDX_100],
            PINK[IDX_100],
            INDIGO[IDX_100],
            CYAN[IDX_100],
            LIGHT_GREEN[IDX_100],
            AMBER[IDX_100],
            BROWN[IDX_100],
            PURPLE[IDX_100],
            BLUE[IDX_100], TEAL[IDX_100], LIME[IDX_100], ORANGE[IDX_100], GREY[IDX_100]
        )
        val SHADE_200 = intArrayOf(
            RED[IDX_200],
            DEEP_PURPLE[IDX_200],
            LIGHT_BLUE[IDX_200],
            GREEN[IDX_200],
            YELLOW[IDX_200],
            DEEP_ORANGE[IDX_200],
            BLUE_GREY[IDX_200],
            PINK[IDX_200],
            INDIGO[IDX_200],
            CYAN[IDX_200],
            LIGHT_GREEN[IDX_200],
            AMBER[IDX_200],
            BROWN[IDX_200],
            PURPLE[IDX_200],
            BLUE[IDX_200], TEAL[IDX_200], LIME[IDX_200], ORANGE[IDX_200], GREY[IDX_200]
        )
        val SHADE_300 = intArrayOf(
            RED[IDX_300],
            DEEP_PURPLE[IDX_300],
            LIGHT_BLUE[IDX_300],
            GREEN[IDX_300],
            YELLOW[IDX_300],
            DEEP_ORANGE[IDX_300],
            BLUE_GREY[IDX_300],
            PINK[IDX_300],
            INDIGO[IDX_300],
            CYAN[IDX_300],
            LIGHT_GREEN[IDX_300],
            AMBER[IDX_300],
            BROWN[IDX_300],
            PURPLE[IDX_300],
            BLUE[IDX_300], TEAL[IDX_300], LIME[IDX_300], ORANGE[IDX_300], GREY[IDX_300]
        )
        val SHADE_400 = intArrayOf(
            RED[IDX_400],
            DEEP_PURPLE[IDX_400],
            LIGHT_BLUE[IDX_400],
            GREEN[IDX_400],
            YELLOW[IDX_400],
            DEEP_ORANGE[IDX_400],
            BLUE_GREY[IDX_400],
            PINK[IDX_400],
            INDIGO[IDX_400],
            CYAN[IDX_400],
            LIGHT_GREEN[IDX_400],
            AMBER[IDX_400],
            BROWN[IDX_400],
            PURPLE[IDX_400],
            BLUE[IDX_400], TEAL[IDX_400], LIME[IDX_400], ORANGE[IDX_400], GREY[IDX_400]
        )
        val SHADE_500 = intArrayOf(
            RED[IDX_500],
            DEEP_PURPLE[IDX_500],
            LIGHT_BLUE[IDX_500],
            GREEN[IDX_500],
            YELLOW[IDX_500],
            DEEP_ORANGE[IDX_500],
            BLUE_GREY[IDX_500],
            PINK[IDX_500],
            INDIGO[IDX_500],
            CYAN[IDX_500],
            LIGHT_GREEN[IDX_500],
            AMBER[IDX_500],
            BROWN[IDX_500],
            PURPLE[IDX_500],
            BLUE[IDX_500], TEAL[IDX_500], LIME[IDX_500], ORANGE[IDX_500], GREY[IDX_500]
        )
        val SHADE_600 = intArrayOf(
            RED[IDX_600],
            DEEP_PURPLE[IDX_600],
            LIGHT_BLUE[IDX_600],
            GREEN[IDX_600],
            YELLOW[IDX_600],
            DEEP_ORANGE[IDX_600],
            BLUE_GREY[IDX_600],
            PINK[IDX_600],
            INDIGO[IDX_600],
            CYAN[IDX_600],
            LIGHT_GREEN[IDX_600],
            AMBER[IDX_600],
            BROWN[IDX_600],
            PURPLE[IDX_600],
            BLUE[IDX_600], TEAL[IDX_600], LIME[IDX_600], ORANGE[IDX_600], GREY[IDX_600]
        )
        val SHADE_700 = intArrayOf(
            RED[IDX_700],
            DEEP_PURPLE[IDX_700],
            LIGHT_BLUE[IDX_700],
            GREEN[IDX_700],
            YELLOW[IDX_700],
            DEEP_ORANGE[IDX_700],
            BLUE_GREY[IDX_700],
            PINK[IDX_700],
            INDIGO[IDX_700],
            CYAN[IDX_700],
            LIGHT_GREEN[IDX_700],
            AMBER[IDX_700],
            BROWN[IDX_700],
            PURPLE[IDX_700],
            BLUE[IDX_700], TEAL[IDX_700], LIME[IDX_700], ORANGE[IDX_700], GREY[IDX_700]
        )
        val SHADE_800 = intArrayOf(
            RED[IDX_800],
            DEEP_PURPLE[IDX_800],
            LIGHT_BLUE[IDX_800],
            GREEN[IDX_800],
            YELLOW[IDX_800],
            DEEP_ORANGE[IDX_800],
            BLUE_GREY[IDX_800],
            PINK[IDX_800],
            INDIGO[IDX_800],
            CYAN[IDX_800],
            LIGHT_GREEN[IDX_800],
            AMBER[IDX_800],
            BROWN[IDX_800],
            PURPLE[IDX_800],
            BLUE[IDX_800], TEAL[IDX_800], LIME[IDX_800], ORANGE[IDX_800], GREY[IDX_800]
        )
        val SHADE_900 = intArrayOf(
            RED[IDX_900],
            DEEP_PURPLE[IDX_900],
            LIGHT_BLUE[IDX_900],
            GREEN[IDX_900],
            YELLOW[IDX_900],
            DEEP_ORANGE[IDX_900],
            BLUE_GREY[IDX_900],
            PINK[IDX_900],
            INDIGO[IDX_900],
            CYAN[IDX_900],
            LIGHT_GREEN[IDX_900],
            AMBER[IDX_900],
            BROWN[IDX_900],
            PURPLE[IDX_900],
            BLUE[IDX_900], TEAL[IDX_900], LIME[IDX_900], ORANGE[IDX_900], GREY[IDX_900]
        )

        val ALL = intArrayOf(
            *(SHADE_100),
            *(SHADE_200),
            *(SHADE_300),
            *(SHADE_400),
            *(SHADE_500),
            *(SHADE_600),
            *(SHADE_700),
            *(SHADE_800),
            *(SHADE_900)
        )

        val ON_DARK_BACKGROUND_HIGH_CONTRAST = intArrayOf(
            -12846,
            -3029783,
            -4987396,
            -3610935,
            -1596,
            -13124,
            -3155748,
            -476208,
            -3814679,
            -5051406,
            -2298424,
            -4941,
            -2634552,
            -1982745,
            -4464901,
            -5054501,
            -985917,
            -8014,
            -657931,
            -1074534,
            -5005861,
            -8268550,
            -5908825,
            -2659,
            -21615,
            -5194043,
            -749647,
            -6313766,
            -8331542,
            -3808859,
            -8062,
            -4412764,
            -3238952,
            -7288071,
            -8336444,
            -1642852,
            -13184,
            -1118482,
            -1739917,
            -11549705,
            -8271996,
            -3722,
            -30107,
            -7297874,
            -1023342,
            -11677471,
            -5319295,
            -10929,
            -10177034,
            -11684180,
            -2300043,
            -18611,
            -2039584,
            -14043402,
            -10044566,
            -4520,
            -36797,
            -14235942,
            -6501275,
            -13784,
            -12409355,
            -14244198,
            -2825897,
            -22746,
            -4342339,
            -16537100,
            -11751600,
            -5317,
            -43230,
            -16728876,
            -7617718,
            -16121,
            -14575885,
            -3285959,
            -26624,
            -6381922,
            -16540699,
            -141259,
            -16732991,
            -8604862,
            -19712,
            -4142541,
            -291840,
            -278483,
            -9920712,
            -24576,
            -5262293,
            -689152,
            -415707,
            -28928,
            -6382300,
            -1086464,
            -688361,
            -37120
        )

        val ON_LIGHT_BACKGROUND_HIGH_CONTRAST = intArrayOf(
            -10011977,
            -12627531,
            -8825528,
            -6543440,
            -10603087,
            -13022805,
            -9614271,
            -7461718,
            -11457112,
            -12232092,
            -4056997,
            -13615201,
            -10665929,
            -8708190,
            -10395295,
            -3790808,
            -12245088,
            -13154481,
            -5434281,
            -14142061,
            -11652050,
            -9823334,
            -15374912,
            -16750244,
            -12434878,
            -4776932,
            -13558894,
            -16689253,
            -14983648,
            -4246004,
            -14273992,
            -7860657,
            -15064194,
            -16752540,
            -13407970,
            -12703965,
            -11922292,
            -15906911,
            -16757440,
            -14606047
        )

        fun getVisibleRandomColor(): Int {
            return if (Singleton.isDarkMode) ON_DARK_BACKGROUND_HIGH_CONTRAST.random() else ON_LIGHT_BACKGROUND_HIGH_CONTRAST.random()
        }

        fun getVisibleRandomColor(isDarkMode: Boolean): Int {
            return if (isDarkMode) ON_DARK_BACKGROUND_HIGH_CONTRAST.random() else ON_LIGHT_BACKGROUND_HIGH_CONTRAST.random()
        }

        fun getRandomColor() = ALL.random()

        fun generateColors(isDarkMode: Boolean) {
            val list = mutableListOf<Int>()
            ALL.forEach {
                if (it.canDisplayOnBackground(if (isDarkMode) BackgroundDark.toArgb() else BackgroundDark.toArgb())) {
                    list.add(it)
                }
            }
        }

        private fun Int.canDisplayOnBackground(@ColorInt background: Int): Boolean =
            ColorUtils.calculateContrast(this, background) > 5f

    }
}