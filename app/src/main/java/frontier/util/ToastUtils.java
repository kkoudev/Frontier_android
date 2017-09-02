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
import android.os.Handler;
import android.widget.Toast;


/**
 * トースト操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class ToastUtils {


    /**
     * トースト表示時間 : 短い
     */
    public static final int     SHORT   = Toast.LENGTH_SHORT;

    /**
     * トースト表示時間 : 長い
     */
    public static final int     LONG    = Toast.LENGTH_LONG;




    /**
     * インスタンス生成防止。
     *
     */
    private ToastUtils() {

        // 処理なし

    }


    /**
     * 指定された文言を表示する。
     *
     * @param context   利用するコンテキスト
     * @param text      表示する文字列
     * @param duration  表示間隔時間
     */
    public static void show(
            final Context       context,
            final CharSequence  text,
            final int           duration
            ) {

        // コンテキストが null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }


        // ハンドラを作成する
        final Handler   handler = new Handler(context.getMainLooper());

        // UIスレッド内で表示する
        handler.post(new Runnable() {

            public void run() {

                // トーストを作成して表示する
                Toast.makeText(context, text, duration).show();

            }

        });

    }


    /**
     * 指定されたリソースIDの文言を表示する。
     *
     * @param context   利用するコンテキスト
     * @param resId     文字列リソースID
     * @param duration  表示間隔時間
     */
    public static void show(
            final Context       context,
            final int           resId,
            final int           duration
            ) {

        // コンテキストが null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }


        // ハンドラを作成する
        final Handler   handler = new Handler(context.getMainLooper());

        // UIスレッド内で表示する
        handler.post(new Runnable() {

            public void run() {

                // トーストを作成して表示する
                Toast.makeText(context, resId, duration).show();

            }

        });

    }

}
