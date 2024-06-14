package com.kan.dev.st051_stickermaker.data

import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.data.model.ColorModel
import com.kan.dev.st051_stickermaker.data.model.FontModel
import com.kan.dev.st051_stickermaker.data.model.HomeModel
import com.kan.dev.st051_stickermaker.data.model.IntroModel
import com.kan.dev.st051_stickermaker.data.model.LanguageModel
import com.kan.dev.st051_stickermaker.data.model.SettingModel
import com.kan.dev.st051_stickermaker.data.model.StickModel

class data {
    companion object{
        val languageList = arrayListOf<LanguageModel>(
        LanguageModel(R.drawable.english,"English","en",false),
        LanguageModel(R.drawable.spanish, "Spanish","es",false),
        LanguageModel(R.drawable.french,"French","fr",false),
        LanguageModel(R.drawable.hindi,"Hindi","hi",false),
        LanguageModel(R.drawable.portugeese,"Portuguese","pt",false),
        LanguageModel(R.drawable.germen,"German","de",false),
        LanguageModel(R.drawable.indo,"Indonesian","io",false)
        )

        val tutorialList = arrayListOf(
            IntroModel(R.drawable.intro_1),
            IntroModel(R.drawable.intro_2),
            IntroModel(R.drawable.intro_3),
        )
        val homeList = arrayListOf(
            HomeModel(R.string.Regular,R.string.StickerWithPhoto,R.color.bgHome1,R.color.textColor1,R.drawable.sticker_photo),
            HomeModel(R.string.StickerWithText,R.string.CreateTextSticker,R.color.bgHome2,R.color.textColor2,R.drawable.sticker_text),
            HomeModel(R.string.Animated1,R.string.CreateAnimationsImage,R.color.bgHome3,R.color.textColor3,R.drawable.animation_image),
            HomeModel(R.string.Animated2,R.string.CreateAnimationsVideo,R.color.bgHome4,R.color.textColor4,R.drawable.animation_video),
        )

        val settingList = arrayListOf<SettingModel>(
            SettingModel(R.drawable.ic_lang,R.string.Language),
            SettingModel(R.drawable.rate,R.string.RateUs),
            SettingModel(R.drawable.share,R.string.ShareApp),
            SettingModel(R.drawable.policy,R.string.PrivacyPolicy)
        )

        val fontList = arrayListOf(
            FontModel(R.font.inter,"Inter"),
            FontModel(R.font.aclonica,"Aclonica"),
            FontModel(R.font.acme,"Acme"),
            FontModel(R.font.akronim,"Akronim"),
            FontModel(R.font.akaya_telivigala,"Akaya"),
            FontModel(R.font.alef,"Alef"),
            FontModel(R.font.alex_brush,"Alex Brush"),
            FontModel(R.font.alfa_slab,"Alfa Slab"),
            FontModel(R.font.allan,"Allan"),
            FontModel(R.font.amarante,"Amarante"),
            FontModel(R.font.amatic_sc,"Amatic SC"),
            FontModel(R.font.amita,"Amita"),
            FontModel(R.font.architects,"Architects"),
            FontModel(R.font.baloo,"Baloo"),
            FontModel(R.font.bonbon,"Bonbon"),
            FontModel(R.font.bruno_ace,"Bruno Ace"),
            FontModel(R.font.cherry_bomb,"Cherry Bomb"),
            FontModel(R.font.comforter,"Comforter"),
            FontModel(R.font.eagle_lake,"Eagle Lake"),
            FontModel(R.font.ewert,"Ewert"),
            FontModel(R.font.flavors,"Flavors"),
            FontModel(R.font.galada,"Galada"),
            FontModel(R.font.give_you_glory,"Give You"),
            FontModel(R.font.hachi_maru,"Hachi Maru"),
        )

        val colorList = arrayListOf(
            ColorModel(R.color.transfers),
            ColorModel(R.color.color_1),
            ColorModel(R.color.color_2),
            ColorModel(R.color.color_3),
            ColorModel(R.color.color_4),
            ColorModel(R.color.color_5),
            ColorModel(R.color.color_6),
            ColorModel(R.color.color_7),
            ColorModel(R.color.color_8),
            ColorModel(R.color.color_9),
            ColorModel(R.color.color_10),
            ColorModel(R.color.color_11),
            ColorModel(R.color.color_12),
            ColorModel(R.color.color_13),
            ColorModel(R.color.color_14),
            ColorModel(R.color.color_15),
            ColorModel(R.color.color_16),
            ColorModel(R.color.color_17),
            ColorModel(R.color.color_18),
            ColorModel(R.color.color_19),
            ColorModel(R.color.color_20),
            ColorModel(R.color.color_21),
            ColorModel(R.color.color_22),
            ColorModel(R.color.color_23),
            ColorModel(R.color.color_24),
            ColorModel(R.color.color_25),
            ColorModel(R.color.color_26),
            ColorModel(R.color.color_27),
            ColorModel(R.color.color_28),
        )

        val stickList = arrayListOf(
            StickModel(R.drawable.stick_0),
            StickModel(R.drawable.stick_1),
            StickModel(R.drawable.stick_2),
            StickModel(R.drawable.stick_3),
            StickModel(R.drawable.stick_4),
            StickModel(R.drawable.stick_5),
            StickModel(R.drawable.stick_6),
            StickModel(R.drawable.stick_7),
            StickModel(R.drawable.stick_8),
            StickModel(R.drawable.stick_9),
            StickModel(R.drawable.stick_10),
            StickModel(R.drawable.stick_11),
            StickModel(R.drawable.stick_12),
            StickModel(R.drawable.stick_13),
            StickModel(R.drawable.stick_14),
            StickModel(R.drawable.stick_15),
            StickModel(R.drawable.stick_16),
        )
    }
}