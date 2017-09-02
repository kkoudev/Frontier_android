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

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * ディスプレイ操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class DisplayUtils {



    /**
     * インスタンス生成防止。
     *
     */
    private DisplayUtils() {

        // 処理なし

    }



    /**
     * ディスプレイ情報を作成する。
     *
     * @param context 利用するコンテキスト情報
     * @return ディスプレイ情報
     */
    private static DisplayMetrics getDisplayMetrics(
            final Context   context
            ) {

        final WindowManager     manager = (WindowManager)context.getSystemService(
                                                Context.WINDOW_SERVICE);
        final DisplayMetrics    metrics = new DisplayMetrics();


        // ディスプレイメトリクス情報を取得する
        manager.getDefaultDisplay().getMetrics(metrics);

        // 取得したディスプレイメトリクス情報を返す
        return metrics;

    }


    /**
     * ディスプレイの幅を取得する。
     *
     * @param context   利用するコンテキスト情報
     * @return ディスプレイの幅
     */
    public static int getWidth(
            final Context   context
            ) {

        // コンテキストが null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }

        // ディスプレイの幅を返す
        return getDisplayMetrics(context).widthPixels;

    }


    /**
     * ディスプレイの高さを取得する。
     *
     * @param context   利用するコンテキスト情報
     * @return ディスプレイの高さ
     */
    public static int getHeight(
            final Context   context
            ) {

        // コンテキストが null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }

        // ディスプレイの幅を返す
        return getDisplayMetrics(context).heightPixels;

    }


    /**
     * 非デバイス依存ピクセル(dip)をデバイス依存ピクセル(px)へ変換する。
     *
     * @param context   利用するコンテキスト情報
     * @param dip       非デバイス依存ピクセル(dip)
     * @return デバイス依存ピクセル(px)
     */
    public static int toPixel(
            final Context   context,
            final float     dip
            ) {

        // コンテキストが null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }

        // デバイス依存ピクセル値へ変換する
        return (int)(dip * getDisplayMetrics(context).scaledDensity);

    }


    /**
     * デバイス依存ピクセル(px)を非デバイス依存ピクセル(dip)へ変換する。
     *
     * @param context   利用するコンテキスト情報
     * @param pixel     デバイス依存ピクセル(px)
     * @return 非デバイス依存ピクセル(dip)
     */
    public static float toDip(
            final Context   context,
            final float     pixel
            ) {

        // コンテキストが null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }

        // 非デバイス依存ピクセル値へ変換する
        return pixel / getDisplayMetrics(context).scaledDensity;

    }


}
