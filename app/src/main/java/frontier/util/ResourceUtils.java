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

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;


/**
 * リソース操作用ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class ResourceUtils {


    /**
     * 描画オブジェクト種別名
     */
    private static final String     TYPE_DRAWABLE_NAME = "drawable";


    /**
     * インスタンス生成防止。
     */
    private ResourceUtils() {

        // 処理なし

    }


    /**
     * 指定したリソースファイル名(拡張子除く)のリソースIDを取得する。
     *
     * @param context   利用するコンテキスト情報
     * @param type      取得するリソース種別
     * @param fileName  取得するリソースファイル名
     * @return リソースID
     */
    private static int getResourceId(
            final Context   context,
            final String    type,
            final String    fileName
            ) {

        return context.getResources().getIdentifier(
                fileName,
                TYPE_DRAWABLE_NAME,
                context.getApplicationInfo().packageName
                );

    }


    /**
     * 指定したリソースファイルへアクセスするための入力ストリームを取得する。
     *
     * @param filePath リソースファイルパス
     * @return 指定したリソースファイルへアクセスするための入力ストリーム
     */
    private static InputStream openResourceAsStream(
            final String    filePath
            ) {

        // 読み込み用ストリームを取得する
        final InputStream     in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);

        // 取得出来なかった場合は例外
        if (in == null) {

            throw new IllegalStateException(
                    "The file is not found. [filePath = " + filePath + "]"
                    );

        }

        // 入力ストリームを返す
        return in;

    }


    /**
     * 指定されたリソースファイルのバイトデータを取得する。<br>
     * <br>
     * ここでいうリソースファイルとは、APKファイルにまとめられているファイルへのパスを指す。<br>
     * 参照しているJARライブラリのリソースファイルもこのAPKへまとめられるので、<br>
     * そのようなファイルを取得する場合に利用する。<br>
     *
     * @param filePath 取得するリソースファイルパス
     * @return 取得したリソースファイルバイトデータ
     * @throws IllegalArgumentException リソースファイルパスが null の場合
     * @throws IllegalStateException    ファイルが見つからない場合
     */
    public static byte[] getResource(
            final String    filePath
            ) {

        // ファイルパスが null の場合は例外
        if (filePath == null) {

            throw new IllegalArgumentException();

        }

        // バイトデータを返す
        return IOUtils.toByteArray(openResourceAsStream(filePath), true);

    }


    /**
     * 指定したリソースファイル名(拡張子除く)の描画オブジェクトを取得する。
     *
     * @param context   利用するコンテキスト情報
     * @param fileName  取得するリソースファイル名
     * @return 描画オブジェクト
     */
    public static Drawable getDrawable(
            final Context   context,
            final String    fileName
            ) {

        // コンテキストまたはファイル名が null の場合は例外
        if ((context == null) || (fileName == null)) {

            throw new IllegalArgumentException();

        }

        // 描画オブジェクトを返す
        return context.getResources().getDrawable(
                getResourceId(
                        context,
                        TYPE_DRAWABLE_NAME,
                        fileName)
                );

    }


    /**
     * 指定したリソースファイル名(拡張子除く)のビットマップオブジェクトを取得する。
     *
     * @param context   利用するコンテキスト情報
     * @param fileName  取得するリソースファイル名
     * @return ビットマップオブジェクト
     */
    public static Bitmap getBitmap(
            final Context   context,
            final String    fileName
            ) {

        // コンテキストまたはファイル名が null の場合は例外
        if ((context == null) || (fileName == null)) {

            throw new IllegalArgumentException();

        }

        // ビットマップオブジェクトを返す
        return BitmapFactory.decodeResource(
                context.getResources(),
                getResourceId(
                        context,
                        TYPE_DRAWABLE_NAME,
                        fileName)
                );

    }


    /**
     * 画像取得オプションを指定して
     * 指定したリソースファイル名(拡張子除く)のビットマップオブジェクトを取得する。
     *
     * @param context       利用するコンテキスト情報
     * @param fileName      取得するリソースファイル名
     * @param maxWidth      読み込み最大画像幅
     * @param maxHeight     読み込み最大画像高さ
     * @return ビットマップオブジェクト
     */
    public static Bitmap getBitmap(
            final Context   context,
            final String    fileName,
            final int       maxWidth,
            final int       maxHeight
            ) {

        // コンテキストまたはファイル名が null の場合は例外
        if ((context == null) || (fileName == null)) {

            throw new IllegalArgumentException();

        }


        final Options   optsInfo  = new Options();      // ファイルサイズ情報読み込み用
        final Options   optsImage = new Options();      // イメージ読み込み用
        final int       imageId;                        // イメージID


        // イメージIDを取得する
        imageId = context.getResources().getIdentifier(
                fileName,
                TYPE_DRAWABLE_NAME,
                context.getApplicationInfo().packageName
                );

        // ファイルサイズ情報のみ取得を指定する
        optsInfo.inJustDecodeBounds = true;

        // 読み込むファイル情報を取得する
        BitmapFactory.decodeResource(
                context.getResources(),
                imageId,
                optsInfo
                );

        // 取得したファイル情報から拡大縮小比率を算出する
        optsImage.inSampleSize = Math.max(
                optsInfo.outWidth  / maxWidth,
                optsInfo.outHeight / maxHeight
                );


        // ビットマップオブジェクトを返す
        return BitmapFactory.decodeResource(
                context.getResources(),
                imageId,
                optsImage
                );

    }


    /**
     * 画像取得オプションを指定して
     * 指定したリソースファイル名(拡張子除く)のビットマップオブジェクトを取得する。
     *
     * @param context   利用するコンテキスト情報
     * @param fileName  取得するリソースファイル名
     * @param opts      画像取得オプション
     * @return ビットマップオブジェクト
     */
    public static Bitmap getBitmap(
            final Context   context,
            final String    fileName,
            final Options   opts
            ) {

        // コンテキストまたはファイル名が null の場合は例外
        if ((context == null) || (fileName == null)) {

            throw new IllegalArgumentException();

        }


        // ビットマップオブジェクトを返す
        return BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier(
                        fileName,
                        TYPE_DRAWABLE_NAME,
                        context.getApplicationInfo().packageName),
                opts
                );

    }


    /**
     * 画像取得オプションを指定して
     * 指定したリソースファイルパスのビットマップオブジェクトを取得する。<br>
     *
     * @param filePath  取得するリソースファイルパス
     * @return ビットマップオブジェクト
     * @throws IllegalArgumentException リソースファイルパスが null の場合
     * @throws IllegalStateException    ファイルが見つからない場合
     */
    public static Bitmap getBitmap(
            final String    filePath
            ) {

        return getBitmap(filePath, null);

    }


    /**
     * 画像取得オプションを指定して
     * 指定したリソースファイルパスのビットマップオブジェクトを取得する。<br>
     *
     * @param filePath      取得するリソースファイルパス
     * @param maxWidth      読み込み最大画像幅
     * @param maxHeight     読み込み最大画像高さ
     * @return ビットマップオブジェクト
     * @throws IllegalArgumentException リソースファイルパスが null の場合
     * @throws IllegalStateException    ファイルが見つからない場合
     */
    public static Bitmap getBitmap(
            final String    filePath,
            final int       maxWidth,
            final int       maxHeight
            ) {

        // リソースファイルパスが null の場合は例外
        if (filePath == null) {

            throw new IllegalArgumentException();

        }


        InputStream     in = null;  // 読み込み用ストリーム

        try {

            final Options   optsInfo  = new Options();      // ファイルサイズ情報読み込み用
            final Options   optsImage = new Options();      // イメージ読み込み用


            // 読み込み用ストリームを取得する
            in = openResourceAsStream(filePath);

            // ファイルサイズ情報のみ取得を指定する
            optsInfo.inJustDecodeBounds = true;

            // 読み込むファイル情報を取得する
            BitmapFactory.decodeStream(
                    in,
                    null,
                    optsInfo
                    );

            // 取得したファイル情報から拡大縮小比率を算出する
            optsImage.inSampleSize = Math.max(
                    optsInfo.outWidth  / maxWidth,
                    optsInfo.outHeight / maxHeight
                    );

            // 現在のストリームを閉じて再度読み込む
            IOUtils.closeQuietly(in);
            in = openResourceAsStream(filePath);

            // ビットマップデータを取得する
            return BitmapFactory.decodeStream(in, null, optsImage);

        } finally {

            // ストリームを閉じる
            IOUtils.closeQuietly(in);

        }

    }


    /**
     * 画像取得オプションを指定して
     * 指定したリソースファイルパスのビットマップオブジェクトを取得する。<br>
     *
     * @param filePath  取得するリソースファイルパス
     * @param opts      画像取得オプション
     * @return ビットマップオブジェクト
     * @throws IllegalArgumentException リソースファイルパスが null の場合
     * @throws IllegalStateException    ファイルが見つからない場合
     */
    public static Bitmap getBitmap(
            final String    filePath,
            final Options   opts
            ) {

        // リソースファイルパスが null の場合は例外
        if (filePath == null) {

            throw new IllegalArgumentException();

        }


        InputStream     in = null;  // 読み込み用ストリーム

        try {

            // 読み込み用ストリームを取得する
            in = openResourceAsStream(filePath);

            // ビットマップデータを取得する
            return BitmapFactory.decodeStream(in, null, opts);

        } finally {

            // ストリームを閉じる
            IOUtils.closeQuietly(in);

        }

    }

}
