/*
 * Copyright (C) 2017 kkoudev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package frontier.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import frontier.util.ViewUtils;


/**
 * 重ね表示のテキストビュー。
 *
 * @author Kou
 *
 */
public class MultiTextView extends TextView {


    /**
     * テキストスタイル : 通常
     */
    public static final int     STYLE_NORMAL    = 0;

    /**
     * テキストスタイル : 太字
     */
    public static final int     STYLE_BOLD      = STYLE_NORMAL + 1;

    /**
     * テキストスタイル : 縁取り
     */
    public static final int     STYLE_EDGE      = STYLE_BOLD + 1;

    /**
     * テキストスタイル数
     */
    public static final int     STYLE_COUNT     = STYLE_EDGE + 1;



    /**
     * 属性 : テキストスタイル
     */
    private static final String ATTR_MULTI_TEXT_STYLE   = "multiTextStyle";

    /**
     * テキストスタイル
     */
    private int                 textStyle               = STYLE_NORMAL;

    /**
     * 文字間の距離
     */
    private float               textDistance    = 1.0f;

    /**
     * 追加テキスト色
     */
    private int                 textExtensionColor;




    /**
     * カレンダービューを作成する。
     *
     * @param context   利用するコンテキスト
     */
    public MultiTextView(
            final Context       context
            ) {

        super(context);

        // 初期化を行う
        initParams(null);

    }


    /**
     * カレンダービューを作成する。
     *
     * @param context   利用するコンテキスト
     * @param attrs     属性情報
     */
    public MultiTextView(
            final Context       context,
            final AttributeSet  attrs
            ) {

        super(context, attrs);

        // 初期化を行う
        initParams(attrs);

    }


    /**
     * カレンダービューを作成する。
     *
     * @param context   利用するコンテキスト
     * @param attrs     属性情報
     * @param defStyle  画面方向
     */
    public MultiTextView(
            final Context       context,
            final AttributeSet  attrs,
            final int           defStyle
            ) {

        super(context, attrs, defStyle);

        // 初期化を行う
        initParams(attrs);

    }


    /**
     * 初期化処理を行う。
     *
     * @param attrs 属性情報
     */
    private void initParams(
            final AttributeSet  attrs
            ) {

        // 属性情報がある場合
        if (attrs != null) {

            // スタイル情報を更新する
            setMultiTextStyle(attrs.getAttributeIntValue(
                    null,
                    ATTR_MULTI_TEXT_STYLE,
                    STYLE_NORMAL)
                    );

        }

    }


    /**
     * マルチテキストスタイルを設定する。
     *
     * @param style 設定するマルチテキストスタイル
     */
    public void setMultiTextStyle(
            final int   style
            ) {

        // 不正なスタイルの場合は例外
        if ((style < 0) || (STYLE_COUNT <= style)) {

            throw new IllegalArgumentException();

        }

        // テキストスタイルを設定する
        textStyle = style;

    }


    /**
     * 文字間の距離を設定する。
     *
     * @param distance 設定する文字間の距離
     */
    public void setMultiTextDistance(
            final float distance
            ) {

        textDistance = distance;

    }


    /**
     * 追加テキスト色を設定する。
     *
     * @param color 追加テキスト色
     */
    public void setExtensionTextColor(
            final int   color
            ) {

        textExtensionColor = color;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(
            final Canvas canvas
            ) {

        final ColorStateList    colorList;                              // 現在のテキスト色一覧
        final float             distance       = textDistance;          // 文字間の距離
        final int               extensionColor = textExtensionColor;    // 追加テキスト色


        // 再描画状態を無効にする
        ViewUtils.setInvalidateEnabled(this, false);

        // テキストスタイル別処理
        switch (textStyle) {

        // 通常
        case STYLE_NORMAL:

            // テキストを描画する
            super.onDraw(canvas);
            break;


        // 太字
        case STYLE_BOLD:

            // テキストを描画する
            super.onDraw(canvas);

            // 横に距離分ずらして描画する
            canvas.translate(distance, 0);
            super.onDraw(canvas);
            break;


        // 縁取り
        case STYLE_EDGE:

            // 現在のテキスト色一覧を取得する
            colorList = getTextColors();

            // 縁色を設定する
            setTextColor(extensionColor);


            // 左に距離分ずらして描画する
            canvas.translate(-distance, 0);
            super.onDraw(canvas);

            // 上に距離分ずらして描画する
            canvas.translate(0, -distance);
            super.onDraw(canvas);

            // 右に距離分ピクセルずらして描画する
            canvas.translate(distance, 0);
            super.onDraw(canvas);

            // 下に距離分ピクセルずらして描画する
            canvas.translate(0, distance);
            super.onDraw(canvas);

            // 色を元に戻す
            setTextColor(colorList);

            // 位置を元に戻す
            canvas.translate(0, 0);

            // テキストを描画する
            super.onDraw(canvas);
            break;


        // その他 (エラー)
        default:

            throw new IllegalStateException();

        }

        // 再描画状態を有効にする
        ViewUtils.setInvalidateEnabled(this, true);

    }


}
