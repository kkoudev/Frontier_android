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

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;


/**
 * アセット操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class AssetUtils {


    /**
     * アクセスモード : ランダムアクセス
     */
    public static final int     ACCESS_RANDOM       = AssetManager.ACCESS_RANDOM;

    /**
     * アクセスモード : ストリーミング
     */
    public static final int     ACCESS_STREAMING    = AssetManager.ACCESS_STREAMING;

    /**
     * アクセスモード : バッファ
     */
    public static final int     ACCESS_BUFFER       = AssetManager.ACCESS_BUFFER;




    /**
     * インスタンス生成防止。
     *
     */
    private AssetUtils() {

        // 処理なし

    }


    /**
     * アセットマネージャーを取得する。
     *
     * @param context   利用するコンテキスト
     * @return アセットマネージャー
     */
    private static AssetManager getAssets(
            final Context   context
            ) {

        // nullの場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }

        // アセットマネージャーを返す
        return context.getAssets();

    }


    /**
     * 指定パスのファイルがアセット内に存在するかどうかを調べる。
     *
     * @param context   利用するコンテキスト
     * @param filePath  アセット内ファイルパス
     * @return 指定パスのファイルがアセット内に存在する場合は true
     */
    public static boolean exists(
            final Context   context,
            final String    filePath
            ) {

        // ファイルパスが不正の場合は例外
        if ((filePath == null) || (filePath.length() == 0)) {

            throw new IllegalArgumentException();

        }


        InputStream     in = null;  // 入力ストリーム

        try {

            // ファイルを開く
            in = getAssets(context).open(filePath);

            // ファイルが存在した
            return true;

        } catch (final IOException e) {

            // ファイルが存在しない
            return false;

        } finally {

            // 入力ストリームを閉じる
            IOUtils.closeQuietly(in);

        }

    }


    /**
     * 指定アセット内ファイルを開く。
     *
     * @param context   利用するコンテキスト
     * @param filePath  開くファイルパス
     * @return 指定ファイルの入力ストリーム
     * @throws IOException  ファイルオープンエラー発生時
     */
    public static InputStream open(
            final Context   context,
            final String    filePath
            ) throws IOException {

        // 引数が不正の場合は例外
        if ((context == null) || (filePath == null)) {

            throw new IllegalArgumentException();

        }

        // ファイルを開いて返す
        return getAssets(context).open(filePath, AssetManager.ACCESS_STREAMING);

    }


    /**
     * 指定アセット内ファイルを開く。
     *
     * @param context       利用するコンテキスト
     * @param filePath      開くファイルパス
     * @param accessMode    アクセスモード
     * @return 指定ファイルの入力ストリーム
     * @throws IOException  ファイルオープンエラー発生時
     */
    public static InputStream open(
            final Context   context,
            final String    filePath,
            final int       accessMode
            ) throws IOException {

        // 引数が不正の場合は例外
        if ((context == null) || (filePath == null)) {

            throw new IllegalArgumentException();

        }

        // ファイルを開いて返す
        return getAssets(context).open(filePath, accessMode);

    }

}
