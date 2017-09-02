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
package frontier.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;


/**
 * グラフィック操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class GraphicUtils {



    /**
     * インスタンス生成防止。
     *
     */
    private GraphicUtils() {

        // 処理なし

    }


    /**
     * フォント幅を取得する。
     *
     * @param text      幅を取得する文字列
     * @param textSize  文字サイズ (sp)
     * @return 指定文字列の幅
     */
    public static float getFontWidth(
            final String        text,
            final float         textSize
            ) {

        final Paint     paint = new Paint();

        // テキストサイズを設定する
        paint.setTextSize(textSize);

        // テキストサイズを返す
        return paint.measureText(text);

    }


    /**
     * フォント高さを取得する。
     *
     * @param paint 描画情報
     * @return フォント高さ
     */
    public static float getFontHeight(
            final Paint     paint
            ) {

        // 描画情報が null の場合は例外
        if (paint == null) {

            throw new IllegalArgumentException();

        }

        // フォント高さを返す
        return getFontHeight(paint.getFontMetrics());

    }


    /**
     * フォント高さを取得する。
     *
     * @param metrics フォントメトリクス
     * @return フォント高さ
     */
    public static float getFontHeight(
            final FontMetrics   metrics
            ) {

        // 引数が null の場合は例外
        if (metrics == null) {

            throw new IllegalArgumentException();

        }

        // フォント高さを返す
        return metrics.bottom - metrics.top;

    }


    /**
     * フォントの左上座標を基準とした描画Y座標を取得する。
     *
     * @param metrics   フォントメトリクス
     * @param value     変換する描画Y座標
     * @return 変換した描画Y座標
     */
    public static float getFontDrawY(
            final FontMetrics   metrics,
            final float         value
            ) {

        // 引数が null の場合は例外
        if (metrics == null) {

            throw new IllegalArgumentException();

        }

        // 描画Y座標を返す
        return value + metrics.descent - metrics.ascent;

    }


    /**
     * キャンバスに指定テキストを描画する。<br>
     * 描画座標はベースラインではなく、画像と同じくXYを基準とした座標を指定する。
     *
     * @param canvas    描画先キャンバス
     * @param text      描画テキスト
     * @param xpos      描画X座標
     * @param ypos      描画Y座標
     * @param paint     描画情報
     * @throws IllegalArgumentException 描画先キャンバスまたは描画情報が null の場合
     */
    public static void drawText(
            final Canvas    canvas,
            final String    text,
            final float     xpos,
            final float     ypos,
            final Paint     paint
            ) {

        // null の場合は例外
        if ((canvas == null) || (paint == null)) {

            throw new IllegalArgumentException();

        }

        // テキストを描画する
        canvas.drawText(text, xpos, getFontDrawY(paint.getFontMetrics(), ypos), paint);

    }


}
