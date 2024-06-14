package com.kan.dev.st051_stickermaker.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.data.data.Companion.colorList
import com.kan.dev.st051_stickermaker.data.data.Companion.fontList
import com.kan.dev.st051_stickermaker.data.data.Companion.stickList
import com.kan.dev.st051_stickermaker.data.model.ColorModel
import com.kan.dev.st051_stickermaker.data.model.StickerModel
import com.kan.dev.st051_stickermaker.databinding.ActivityEditBinding
import com.kan.dev.st051_stickermaker.dialog.DialogEnterText
import com.kan.dev.st051_stickermaker.interfaces.IEditTextDialogListener
import com.kan.dev.st051_stickermaker.ui.adapter.ColorAdapter
import com.kan.dev.st051_stickermaker.ui.adapter.FontAdapter
import com.kan.dev.st051_stickermaker.ui.adapter.IColorClickListener
import com.kan.dev.st051_stickermaker.ui.adapter.IFontChangeListener
import com.kan.dev.st051_stickermaker.ui.adapter.IStickClickListener
import com.kan.dev.st051_stickermaker.ui.adapter.IStickerListListener
import com.kan.dev.st051_stickermaker.ui.adapter.IStickerTypeListener
import com.kan.dev.st051_stickermaker.ui.adapter.StickAdapter
import com.kan.dev.st051_stickermaker.ui.adapter.StickerListAdapter
import com.kan.dev.st051_stickermaker.ui.adapter.StickerTypeAdapter
import com.kan.dev.st051_stickermaker.utils.PUT_BITMAP
import com.kan.dev.st051_stickermaker.utils.adjustAlpha
import com.kan.dev.st051_stickermaker.utils.convertBitmap
import com.kan.dev.st051_stickermaker.utils.drawableToBitmap
import com.kan.dev.st051_stickermaker.utils.stickerview.CustomSticker
import com.kan.dev.st051_stickermaker.utils.stickerview.DrawableSticker
import com.kan.dev.st051_stickermaker.utils.stickerview.Sticker
import com.kan.dev.st051_stickermaker.viewModel.StickerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivity : BaseActivity<ActivityEditBinding>(), IEditTextDialogListener,
    IFontChangeListener,IColorClickListener,IStickClickListener,IStickerListListener {
    override fun setViewBinding(): ActivityEditBinding {
        return ActivityEditBinding.inflate(layoutInflater)
    }

    private val viewModel : StickerViewModel by viewModels()

    private lateinit var byteArray: ByteArray
    private lateinit var bitmap: Bitmap
    private lateinit var drawable: Drawable
    private lateinit var textSticker: DrawableSticker
    private var checkEdit = false
    private val dialogSticker: DialogEnterText by lazy {
        DialogEnterText(this, this)
    }
    private val adapter: FontAdapter by lazy {
        FontAdapter(this, this)
    }
    private val coloAdapter :ColorAdapter by lazy {
        ColorAdapter(this,this)
    }
    private val stickAdapter :StickAdapter by lazy {
        StickAdapter(this,this)
    }
    private lateinit var stickerTypeAdapter : StickerTypeAdapter
    private lateinit var stickerListAdapter: StickerListAdapter
    private lateinit var stickBitmap : Bitmap
    private var checkBold = false
    private var checkItalic = false
    private var checkStrikethrough = false
    private var checkUnderlined = false
    private lateinit var textColor : ColorModel
    private var adjustedColor = 0

    override fun initData() {
        byteArray = intent.getByteArrayExtra(PUT_BITMAP)!!
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        binding.apply {
            layoutText.visibility = View.INVISIBLE
            adapter.setItems(fontList)
            rcvLayoutFont.adapter = adapter
            rcvLayoutFont.layoutManager = GridLayoutManager(this@EditActivity, 3)
            layoutPaint.apply {
                coloAdapter.setItems(colorList)
                rcvPaint.adapter = coloAdapter
                rcvPaint.layoutManager = LinearLayoutManager(this@EditActivity, HORIZONTAL,false)
            }
            stickAdapter.setItems(stickList)
            rcvLayoutStick.adapter = stickAdapter
            rcvLayoutStick.layoutManager = GridLayoutManager(this@EditActivity,4)

        }
        binding.layoutSticker.apply {
            stickerTypeAdapter = StickerTypeAdapter(this@EditActivity,object : IStickerTypeListener{
                override fun clickSticker(listSticker : List<StickerModel>) {
                    stickerListAdapter = StickerListAdapter(this@EditActivity,this@EditActivity)
                    rcvSticker.adapter = stickerListAdapter
                    stickerListAdapter.setItems(listSticker)
                    rcvSticker.layoutManager = StaggeredGridLayoutManager(4, VERTICAL)
                }
            })

            viewModel.typeStickers.observe(this@EditActivity){
                stickerTypeAdapter.setItems(it)
            }
            rcvTypeSticker.adapter = stickerTypeAdapter
            rcvTypeSticker.layoutManager = LinearLayoutManager(this@EditActivity, HORIZONTAL,false)
        }
    }

    override fun initView() {
        binding.apply {
            image.setImageBitmap(bitmap)

            layoutPaint.seekBarColor.isEnabled = false
        }
    }


    override fun initListener() {
        binding.apply {
            icBack.setOnClickListener {
                finish()
            }
            Text.setOnClickListener {
                dialogSticker.show()
            }
            Sticker.setOnClickListener {
                layoutSticker.root.visibility = View.VISIBLE
                layoutText.visibility = View.INVISIBLE
            }
            Effect.setOnClickListener {
                drawView.toggleMode()
            }
            Border.setOnClickListener {

            }

            icTypeFace.setOnClickListener {
                setTextLayout(R.drawable.ic_type,R.drawable.ic_theme_un,R.drawable.ic_stick_un)
                layoutAlignText.visibility = View.VISIBLE
                layoutPaint.root.visibility = View.GONE
                rcvLayoutStick.visibility = View.GONE
            }
            icTheme.setOnClickListener {
                setTextLayout(R.drawable.ic_type_un,R.drawable.ic_theme,R.drawable.ic_stick_un)
                layoutAlignText.visibility = View.GONE
                layoutPaint.root.visibility = View.VISIBLE
                rcvLayoutStick.visibility = View.GONE
            }
            icStick.setOnClickListener {
                setTextLayout(R.drawable.ic_type_un,R.drawable.ic_theme_un,R.drawable.ic_stick)
                layoutAlignText.visibility = View.GONE
                layoutPaint.root.visibility = View.GONE
                rcvLayoutStick.visibility = View.VISIBLE
            }

            //Alignment
            icAlignLeft.setOnClickListener {
                setAlignment(
                    R.drawable.alignment_left,
                    R.drawable.alignment_center_un,
                    R.drawable.alignment_right_un
                )
                tvSticker.gravity = Gravity.START
                setTextSticker()
            }
            icAlignCenter.setOnClickListener {
                setAlignment(
                    R.drawable.alignment_left_un,
                    R.drawable.alignment_center,
                    R.drawable.alignment_right_un
                )
                tvSticker.gravity = Gravity.CENTER
                setTextSticker()
            }
            icAlignRight.setOnClickListener {
                setAlignment(
                    R.drawable.alignment_left_un,
                    R.drawable.alignment_center_un,
                    R.drawable.alignment_right
                )
                tvSticker.gravity = Gravity.END
                setTextSticker()
            }
            //Decoration
            icNomarl.setOnClickListener {
                setNormal()
            }
            icBold.setOnClickListener {
                setBold()
                setTextSticker()
            }
            icItalic.setOnClickListener {
                setItalic()
                setTextSticker()
            }
            icStrikethrough.setOnClickListener {
                setStrikethrough()
                setTextSticker()
            }
            icUnderlined.setOnClickListener {
                setUnderlined()
                setTextSticker()
            }
            icClose.setOnClickListener {
                layoutText.visibility = View.INVISIBLE
            }
            icDone.setOnClickListener {
                layoutText.visibility = View.INVISIBLE
            }
            icShadow1.setOnClickListener {
                tvSticker.setShadowLayer(0f, 0f, 0f, Color.BLACK)
                setTextSticker()
            }
            icShadow2.setOnClickListener {
                tvSticker.setShadowLayer(2f, 1f, 1f, Color.BLACK)
                setTextSticker()
            }
            icShadow3.setOnClickListener {
                tvSticker.setShadowLayer(5f, 3f, 3f, Color.BLACK)
                setTextSticker()
            }
            btnAlign.setOnClickListener {
                layoutAlign.visibility = View.VISIBLE
                rcvLayoutFont.visibility = View.GONE
                btnFont.setBackgroundColor(
                    ContextCompat.getColor(
                        this@EditActivity,
                        R.color.transfers
                    )
                )
                btnAlign.setBackgroundResource(R.drawable.bg_selector_font)
                btnAlign.setTextColor(ContextCompat.getColor(this@EditActivity, R.color.white))
                btnFont.setTextColor(ContextCompat.getColor(this@EditActivity, R.color.gray949494))
            }
            btnFont.setOnClickListener {
                layoutAlign.visibility = View.GONE
                rcvLayoutFont.visibility = View.VISIBLE
                btnAlign.setBackgroundColor(
                    ContextCompat.getColor(
                        this@EditActivity,
                        R.color.transfers
                    )
                )
                btnFont.setBackgroundResource(R.drawable.bg_selector_font)
                btnFont.setTextColor(ContextCompat.getColor(this@EditActivity, R.color.white))
                btnAlign.setTextColor(ContextCompat.getColor(this@EditActivity, R.color.gray949494))
            }

            stickerView.setOnStickerOperationListener(object : CustomSticker.OnStickerOperationListener {
                override fun onStickerAdded(sticker: Sticker) {

                }

                override fun onStickerClicked(sticker: Sticker) {
                    layoutText.visibility = View.VISIBLE
                }

                override fun onStickerDeleted(sticker: Sticker) {

                }

                override fun onStickerDragFinished(sticker: Sticker) {

                }

                override fun onStickerTouchedDown(sticker: Sticker) {

                }

                override fun onStickerZoomFinished(sticker: Sticker) {

                }

                override fun onStickerFlipped(sticker: Sticker) {

                }

                override fun onStickerDoubleTapped(sticker: Sticker) {

                }

                override fun onStickerEdit() {
                    checkEdit = true
                    dialogSticker.show()
                }

            })
            layoutPaint.apply {
                seekBarColor.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        adjustedColor = adjustAlpha(ContextCompat.getColor(this@EditActivity,textColor.color),progress/100f)
                        tvSticker.setTextColor(adjustedColor)
                        setTextSticker()
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })

            }
        }

        binding.layoutSticker.apply {
            icClose.setOnClickListener {
                root.visibility = View.GONE
            }
            icDone.setOnClickListener {
                root.visibility = View.GONE
            }
        }
    }
    private fun setNormal() {
        binding.apply {
            icNomarl.setImageResource(R.drawable.normal)
            checkBold = false
            checkItalic = false
            checkStrikethrough = false
            checkUnderlined = false

            icBold.setImageResource(R.drawable.bold_un)
            icItalic.setImageResource(R.drawable.italic_un)
            icStrikethrough.setImageResource(R.drawable.strikethrough_un)
            icUnderlined.setImageResource(R.drawable.underlined_un)

            tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            tvSticker.paintFlags =
                tvSticker.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv() and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            setTextSticker()
        }
    }
    private fun setBold() {
        binding.apply {
            checkBold = !checkBold
            if (checkBold) {
                icBold.setImageResource(R.drawable.bold)
                icNomarl.setImageResource(R.drawable.normal_un)
                if (checkItalic) {
                    tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
                } else {
                    tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            } else {
                icBold.setImageResource(R.drawable.bold_un)
                if (checkItalic) {
                    tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
                } else {
                    tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                }
            }
        }
    }
    private fun setItalic() {
        binding.apply {
            checkItalic = !checkItalic
            if (checkItalic) {
                icItalic.setImageResource(R.drawable.italic)
                icNomarl.setImageResource(R.drawable.normal_un)
                if (checkBold) {
                    tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
                } else {
                    tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
                }
            } else {
                icItalic.setImageResource(R.drawable.italic_un)
                if (checkBold) {
                    tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                } else {
                    tvSticker.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                }
            }
        }
    }
    private fun setStrikethrough() {
        binding.apply {
            checkStrikethrough = !checkStrikethrough
            if (checkStrikethrough) {
                icStrikethrough.setImageResource(R.drawable.strikethrough)
                icNomarl.setImageResource(R.drawable.normal_un)
                if (checkUnderlined) {
                    tvSticker.paintFlags =
                        tvSticker.paintFlags or Paint.UNDERLINE_TEXT_FLAG or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvSticker.paintFlags = tvSticker.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                icStrikethrough.setImageResource(R.drawable.strikethrough_un)
                if (checkUnderlined) {
                    tvSticker.paintFlags = tvSticker.paintFlags or Paint.UNDERLINE_TEXT_FLAG and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                } else {
                    tvSticker.paintFlags =
                        tvSticker.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
    }
    private fun setUnderlined() {
        binding.apply {
            checkUnderlined = !checkUnderlined
            if (checkUnderlined) {
                icUnderlined.setImageResource(R.drawable.underlined)
                icNomarl.setImageResource(R.drawable.normal_un)
                if (checkStrikethrough) {
                    tvSticker.paintFlags =
                        tvSticker.paintFlags or Paint.UNDERLINE_TEXT_FLAG or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvSticker.paintFlags = tvSticker.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                }
            } else {
                icUnderlined.setImageResource(R.drawable.underlined_un)
                if (checkStrikethrough) {
                    tvSticker.paintFlags = tvSticker.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvSticker.paintFlags = tvSticker.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
                }
            }
        }
    }
    override fun IClickDoneListener(str: String) {
        binding.apply {
            tvSticker.text = str
            tvSticker.post {
                layoutText.visibility = View.VISIBLE
                bitmap = convertBitmap(textStickers)
                drawable = BitmapDrawable(resources, bitmap)
                textSticker = DrawableSticker(drawable)
                if (checkEdit) {
                    stickerView.setSticker(textSticker)
                    checkEdit = false
                } else {
                    binding.stickerView.addSticker(textSticker)
                }
            }
        }
    }
    private fun setAlignment(img1: Int, img2: Int, img3: Int) {
        binding.apply {
            icAlignLeft.setImageResource(img1)
            icAlignCenter.setImageResource(img2)
            icAlignRight.setImageResource(img3)
        }
    }
    private fun setTextLayout(img1: Int,img2: Int,img3: Int){
        binding.apply {
            icTypeFace.setImageResource(img1)
            icTheme.setImageResource(img2)
            icStick.setImageResource(img3)
        }
    }
    private fun setTextSticker() {
        binding.apply {
            tvSticker.post {
                bitmap = convertBitmap(textStickers)
                drawable = BitmapDrawable(resources, bitmap)
                textSticker = DrawableSticker(drawable)
                stickerView.setSticker(textSticker)
            }
        }
    }
    override fun hideNavigation() {
        super.hideNavigation()
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
    override fun fontChangeListener(font: Int) {
        binding.tvSticker.typeface = ResourcesCompat.getFont(this, font)
        setTextSticker()
    }

    override fun onColorClicked(position: Int, item: ColorModel) {
        binding.apply {
            layoutPaint.seekBarColor.isEnabled = true
            layoutPaint.seekBarColor.progress = 100
            tvSticker.setTextColor(ContextCompat.getColor(this@EditActivity,item.color))
            setTextSticker()
        }
        textColor = item
    }

    override fun clickStick(position: Int, img: Int) {
        if (position > 0){
            stickBitmap = ContextCompat.getDrawable(this, img)!!.toBitmap()
        }else{

        }
    }
    override fun clickItemSticker(item: StickerModel) {

        Log.d("Kan","http://lvtglobal.site/" + item.link)

        Glide.with(this@EditActivity).asDrawable().load("http://lvtglobal.site" + item.link).into(object  : CustomTarget<Drawable>(){
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                binding.apply {
                    textSticker = DrawableSticker(resource)
                    stickerView.addSticker(textSticker)
                }
            }
            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })
    }
}