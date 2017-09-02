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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * ファイル操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class FileUtils {



    /**
     * インスタンス生成防止。
     *
     */
    private FileUtils() {

        // 処理なし

    }



    /**
     * 指定パスのファイルを作成する。
     *
     * @param filePath  作成するファイルパス
     * @return 作成に成功した場合は true
     */
    public static boolean createNew(
            final String    filePath
            ) {

        // ファイルパスが null の場合は例外
        if (filePath == null) {

            throw new IllegalArgumentException();

        }


        try {

            // ファイルを作成する
            final File  file   = new File(filePath);
            final File  parent = file.getParentFile();

            // 親ディレクトリが存在しない場合
            if (!parent.exists()) {

                // 親ディレクトリを作成する
                if (!parent.mkdirs()) {

                    // 作成失敗
                    return false;

                }

            }

            // ファイルを作成する
            return file.createNewFile();

        } catch (final IOException e) {

            // 作成失敗
            return false;

        }

    }


    /**
     * 指定パスのファイルが存在するかどうかを取得する。
     *
     * @param filePath 存在するかどうかを調べるファイルパス
     * @return 指定パスのファイルが存在する場合は true
     */
    public static boolean exists(
            final String    filePath
            ) {

        // ファイルパスが null の場合は例外
        if (filePath == null) {

            throw new IllegalArgumentException();

        }

        // ファイルが存在するかどうかを返す
        return new File(filePath).exists();

    }


    /**
     * 指定パスのファイルを無条件に削除する。
     *
     * @param filePath 削除するファイルパス
     * @return 削除に成功した場合は true
     */
    public static boolean deleteQuiently(
            final String    filePath
            ) {

        // ファイルパスが null の場合
        if (filePath == null) {

            // 削除失敗
            return false;

        }

        // ファイルを削除する
        return new File(filePath).delete();

    }


    /**
     * 指定ファイルを無条件に削除する。
     *
     * @param file 削除するファイル
     * @return 削除に成功した場合は true
     */
    public static boolean deleteQuiently(
            final File  file
            ) {

        // ファイルが null の場合
        if (file == null) {

            // 削除失敗
            return false;

        }

        // ファイルを削除する
        return file.delete();

    }


    /**
     * 指定されたファイルをリネームする。
     *
     * @param srcPath   リネーム元ファイルパス
     * @param destPath  リネーム後ファイルパス
     * @return リネームに成功した場合は true
     * @throws IllegalArgumentException ファイルが null の場合
     */
    public static boolean rename(
            final String    srcPath,
            final String    destPath
            ) {

        return rename(new File(srcPath), new File(destPath));

    }


    /**
     * 指定されたファイルをリネームする。
     *
     * @param srcFile   リネーム元ファイル
     * @param destFile  リネーム後ファイル
     * @return リネームに成功した場合は true
     * @throws IllegalArgumentException ファイルが null の場合
     */
    public static boolean rename(
            final File  srcFile,
            final File  destFile
            ) {

        // 引数が不正の場合は例外
        if ((srcFile == null) || (destFile == null)) {

            throw new IllegalArgumentException();

        }

        // リネームする
        return srcFile.renameTo(destFile);

    }


    /**
     * 指定ファイルパスへバイトデータを書き込む。
     *
     * @param filePath  書き込み先ファイル名
     * @param data      書き込むバイトデータ
     * @return 書き込みに成功した場合は true
     */
    public static boolean write(
            final String    filePath,
            final byte[]    data
            ) {

        // ファイルパスが null の場合は例外
        if (filePath == null) {

            throw new IllegalArgumentException();

        }

        // 書き込み処理を実行する
        return write(new File(filePath), data);

    }


    /**
     * 指定ファイルパスへバイトデータを書き込む。
     *
     * @param file      書き込み先ファイル
     * @param data      書き込むバイトデータ
     * @return 書き込みに成功した場合は true
     */
    public static boolean write(
            final File      file,
            final byte[]    data
            ) {

        // 引数が null の場合は例外
        if ((file == null) || (data == null)) {

            throw new IllegalArgumentException();

        }


        OutputStream    out = null;


        try {

            // ファイル書き込みストリームを作成する
            out = new FileOutputStream(file.getAbsoluteFile());

            // バイトデータを書き込む
            out.write(data);

            // 書き込み成功
            return true;

        } catch (final IOException e) {

            e.printStackTrace();

            // 書き込み失敗
            return false;

        } finally {

            // ファイルを閉じる
            IOUtils.closeQuietly(out);

        }

    }


}
