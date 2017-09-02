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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * イメージ操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class ImageUtils {



    /**
     * インスタンス生成防止。
     *
     */
    private ImageUtils() {

        // 処理なし

    }


    /**
     * 指定されたビットマップをバイト配列へ変換する。
     *
     * @param bitmap        変換するビットマップ
     * @param format        変換フォーマット
     * @param quality       画像品質 (0 - 100)
     * @return 指定されたビットマップのバイト配列
     */
    public static byte[] toByteArray(
            final Bitmap            bitmap,
            final CompressFormat    format,
            final int               quality
            ) {

        // 引数が不正の場合は例外
        if ((bitmap == null) || (format == null)) {

            throw new IllegalArgumentException();

        }


        // 変換用バイトストリームを作成する
        final ByteArrayOutputStream     bOut = new ByteArrayOutputStream();

        // ビットマップを指定フォーマット画像へ変換する
        bitmap.compress(format, quality, bOut);


        // バイトデータを返す
        return bOut.toByteArray();

    }


    /**
     * 指定されたファイルパスの画像を取得する。
     *
     * @param filePath      画像ファイルパス
     * @return 読み込んだ画像ファイル情報。失敗した場合は null
     * @throws IllegalArgumentException 画像ファイルパスが null の場合
     */
    public static Bitmap createBitmap(
            final String    filePath
            ) {

        // ファイルパスが null の場合
        if (filePath == null) {

            throw new IllegalArgumentException();

        }

        // 指定ファイルが存在しない場合
        if (!FileUtils.exists(filePath)) {

            // 読み込み失敗
            return null;

        }


        // 指定ファイルパスの画像を返却する
        return BitmapFactory.decodeFile(filePath);

    }


    /**
     * 指定されたファイルパスの画像を指定サイズに収まるように取得する。
     *
     * @param filePath      画像ファイルパス
     * @param maxWidth      読み込み最大画像幅
     * @param maxHeight     読み込み最大画像高さ
     * @return 読み込んだ画像ファイル情報。失敗した場合は null
     * @throws IllegalArgumentException 画像ファイルパスが null の場合
     * @throws IllegalArgumentException 画像最大幅または画像最大高さが 0 以下の場合
     */
    public static Bitmap createBitmap(
            final String    filePath,
            final int       maxWidth,
            final int       maxHeight
            ) {

        // ファイルパスが null の場合
        // または指定ファイルサイズが不正の場合は例外
        if ((filePath == null) || (maxWidth <= 0) || (maxHeight <= 0)) {

            throw new IllegalArgumentException();

        }

        // 指定ファイルが存在しない場合
        if (!FileUtils.exists(filePath)) {

            // 読み込み失敗
            return null;

        }


        final Options   optsInfo  = new Options();      // ファイルサイズ情報読み込み用
        final Options   optsImage = new Options();      // イメージ読み込み用

        // ファイルサイズ情報のみ取得を指定する
        optsInfo.inJustDecodeBounds = true;

        // 読み込むファイル情報を取得する
        BitmapFactory.decodeFile(filePath, optsInfo);

        // 取得したファイル情報から拡大縮小比率を算出する
        optsImage.inSampleSize = Math.max(
                optsInfo.outWidth  / maxWidth,
                optsInfo.outHeight / maxHeight
                );


        // 縮小した画像を返却する
        return BitmapFactory.decodeFile(filePath, optsImage);

    }


    /**
     * 指定された入力ストリームの画像を指定サイズに収まるように取得する。
     *
     * @param in            画像の入力ストリーム
     * @param maxWidth      読み込み最大画像幅
     * @param maxHeight     読み込み最大画像高さ
     * @param closing       ストリームを自動的に閉じるかどうか
     * @return 読み込んだ画像ファイル情報。失敗した場合は null
     * @throws IllegalArgumentException 画像の入力ストリームが null の場合
     * @throws IllegalArgumentException 画像最大幅または画像最大高さが 0 以下の場合
     */
    public static Options createBitmapOptions(
            final InputStream   in,
            final int           maxWidth,
            final int           maxHeight,
            final boolean       closing
            ) {

        // 画像の入力ストリームが null の場合
        // または指定ファイルサイズが不正の場合は例外
        if ((in == null) || (maxWidth <= 0) || (maxHeight <= 0)) {

            throw new IllegalArgumentException();

        }


        final Options   optsInfo  = new Options();      // ファイルサイズ情報読み込み用
        final Options   optsImage = new Options();      // イメージ読み込み用

        // ファイルサイズ情報のみ取得を指定する
        optsInfo.inJustDecodeBounds = true;

        // 読み込むファイル情報を取得する
        BitmapFactory.decodeStream(in, null, optsInfo);

        // 取得したファイル情報から拡大縮小比率を算出する
        optsImage.inSampleSize = Math.max(
                optsInfo.outWidth  / maxWidth,
                optsInfo.outHeight / maxHeight
                );

        // ストリームを自動的に閉じる場合
        if (closing) {

            // ストリームを閉じる
            IOUtils.closeQuietly(in);

        }

        // 作成したオプションを返す
        return optsImage;

    }


    /**
     * 指定された入力ストリームの画像を指定サイズに収まるように取得する。
     *
     * @param in            画像の入力ストリーム
     * @param opts          画像読み込みオプション
     * @param closing       ストリームを自動的に閉じるかどうか
     * @return 読み込んだ画像ファイル情報。失敗した場合は null
     * @throws IllegalArgumentException 画像の入力ストリームが null の場合
     * @throws IllegalArgumentException 画像最大幅または画像最大高さが 0 以下の場合
     */
    public static Bitmap createBitmap(
            final InputStream   in,
            final Options       opts,
            final boolean       closing
            ) {

        // 画像の入力ストリームが null の場合
        // または指定オプションが null の場合は例外
        if ((in == null) || (opts == null)) {

            throw new IllegalArgumentException();

        }


        // 縮小した画像を取得する
        final Bitmap    retBitmap = BitmapFactory.decodeStream(in, null, opts);

        // ストリームを自動的に閉じる場合
        if (closing) {

            // ストリームを閉じる
            IOUtils.closeQuietly(in);

        }

        // 作成した画像を返却する
        return retBitmap;

    }


    /**
     * 指定されたビットマップを拡大縮小変換する。
     *
     * @param bitmap        変換するビットマップ
     * @param scaledWidth   拡大縮小幅
     * @param scaledHeight  拡大縮小高さ
     * @return 変換後のビットマップ
     */
    public static Bitmap createBitmap(
            final Bitmap        bitmap,
            final int           scaledWidth,
            final int           scaledHeight
            ) {

        // 拡大縮小したビットマップを返す
        return Bitmap.createScaledBitmap(
                bitmap,
                scaledWidth,
                scaledHeight,
                true
                );

    }


    /**
     * 指定された画像ファイルパスをアフィン変換する。
     *
     * @param filePath      変換する画像ファイルパス
     * @param rotateDegree  回転角度
     * @param scaledWidth   拡大縮小幅
     * @param scaledHeight  拡大縮小高さ
     * @return 変換後のビットマップ
     */
    public static Bitmap createBitmap(
            final String        filePath,
            final float         rotateDegree,
            final int           scaledWidth,
            final int           scaledHeight
            ) {

        // 引数が不正の場合は例外
        if (filePath == null) {

            throw new IllegalArgumentException();

        }

        final Matrix    matrix = new Matrix();  // 回転用マトリックス
        Bitmap          localBitmap;            // 使用するビットマップ


        // 拡大縮小してビットマップを作成する
        localBitmap = createBitmap(filePath, scaledWidth, scaledHeight);

        // 回転角度を設定する
        matrix.postRotate(rotateDegree);

        // 画像を回転させる
        localBitmap = Bitmap.createBitmap(
                localBitmap,
                0,
                0,
                localBitmap.getWidth(),
                localBitmap.getHeight(),
                matrix,
                true
                );


        // 変換したビットマップを返す
        return localBitmap;

    }


    /**
     * 指定されたビットマップをアフィン変換する。
     *
     * @param bitmap        変換するビットマップ
     * @param rotateDegree  回転角度
     * @param scaledWidth   拡大縮小幅
     * @param scaledHeight  拡大縮小高さ
     * @return 変換後のビットマップ
     */
    public static Bitmap createBitmap(
            final Bitmap        bitmap,
            final float         rotateDegree,
            final int           scaledWidth,
            final int           scaledHeight
            ) {

        // 引数が不正の場合は例外
        if (bitmap == null) {

            throw new IllegalArgumentException();

        }

        final Matrix    matrix = new Matrix();  // 回転用マトリックス
        Bitmap          localBitmap;            // 使用するビットマップ


        // 拡大縮小してビットマップを作成する
        localBitmap = Bitmap.createScaledBitmap(
                bitmap,
                scaledWidth,
                scaledHeight,
                true
                );

        // 回転角度を設定する
        matrix.postRotate(rotateDegree);

        // 画像を回転させる
        localBitmap = Bitmap.createBitmap(
                localBitmap,
                0,
                0,
                localBitmap.getWidth(),
                localBitmap.getHeight(),
                matrix,
                true
                );


        // 変換したビットマップを返す
        return localBitmap;

    }


    /**
     * 指定ビットマップを指定カラーのビットマップへ変換する。
     *
     * @param bitmap    変換するビットマップ
     * @param config    変換カラー
     * @return 変換後のビットマップ
     */
    public static Bitmap toBitmap(
            final Bitmap    bitmap,
            final Config    config
            ) {

        // 引数が null の場合は例外
        if ((bitmap == null) || (config == null)) {

            throw new IllegalArgumentException();

        }

        // 変換したビットマップを返す
        return toBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), config);

    }


    /**
     * 指定ビットマップを指定カラー・サイズのビットマップへ変換する。
     *
     * @param bitmap    変換するビットマップ
     * @param width     変換幅
     * @param height    変換高さ
     * @param config    変換カラー
     * @return 変換後のビットマップ
     */
    public static Bitmap toBitmap(
            final Bitmap    bitmap,
            final int       width,
            final int       height,
            final Config    config
            ) {

        // 引数が null の場合は例外
        if ((bitmap == null) || (config == null)) {

            throw new IllegalArgumentException();

        }


        // ビットマップ編集用キャンバスとペイントを作成する
        final Bitmap        retBitmap = Bitmap.createBitmap(
                                                width,
                                                height,
                                                config);                // 書き込み先ビットマップ
        final Canvas        canvas    = new Canvas(retBitmap);          // 編集用キャンバス
        final Paint         paint     = new Paint();                    // 使用するペイント


        // 指定画像情報を描画する
        canvas.drawBitmap(
                bitmap,
                new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(0, 0, width, height),
                paint
                );


        // 編集したビットマップを返却する
        return retBitmap;

    }


}
