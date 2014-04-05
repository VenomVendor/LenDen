/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author		:	VenomVendor
 * Dated		:	5 Apr, 2014 3:30:55 AM
 * Project		:	LenDen-Android
 * Client		:	LenDen
 * Contact		:	info@VenomVendor.com
 * URL			:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)	:	2014-Present, VenomVendor.
 * License		:	This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *					License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *					Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.aUI;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import vee.HexWhale.LenDen.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class CustomFontTextView.
 */
public class CustomFontTextView extends TextView {

    /** The tag. */
    String tag = "CUSTOMFONTTEXTVIEW";

    /** The m typefaces. */
    private static Map<String, Typeface> mTypefaces;

    public CustomFontTextView(final Context context) {
        this(context, null);
    }

    public CustomFontTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFontTextView(final Context context, final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);

        if (mTypefaces == null) {
            mTypefaces = new HashMap<String, Typeface>();
        }
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);

        if (array != null) {
            final String typefaceAssetPath = array.getString(R.styleable.CustomFontTextView_veeTypeface);

            if (typefaceAssetPath != null) {
                Typeface typeface = null;

                if (mTypefaces.containsKey(typefaceAssetPath)) {
                    typeface = mTypefaces.get(typefaceAssetPath);
                } else {
                    final AssetManager assets = context.getAssets();

                    typeface = Typeface.createFromAsset(assets, typefaceAssetPath);
                    mTypefaces.put(typefaceAssetPath, typeface);
                }
                setTypeface(typeface);
            }
            array.recycle();
        }
    }

    /*
     * (non-Javadoc)
     * @see android.widget.TextView#setTypeface(android.graphics.Typeface)
     */
    @Override
    public void setTypeface(Typeface typeface) {
        super.setTypeface(typeface);
    }

}
