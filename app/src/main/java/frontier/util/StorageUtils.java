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
import java.util.List;

import android.os.Environment;


/**
 * ストレージ操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class StorageUtils {



    /**
     * インスタンス生成防止。
     *
     */
    private StorageUtils() {

        // 処理なし

    }


    /**
     * SDカードが存在するかどうかをチェックする。
     *
     * @return SDカードが存在する場合は true
     */
    public static boolean existsExternalStorage() {

        // 読み込みまたは書き込みが可能であればSDカードが存在していることとする
        return canWriteExternalStorage() || canReadExternalStorage();

    }


    /**
     * SDカードへ書き込み可能かどうかをチェックする。
     *
     * @return SDカードへ書き込み可能な場合は true
     */
    public static boolean canWriteExternalStorage() {

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    }


    /**
     * SDカードから読み込み可能かどうかをチェックする。
     *
     * @return SDカードから読み込み可能な場合は true
     */
    public static boolean canReadExternalStorage() {

        final String    state = Environment.getExternalStorageState();

        // 読み込み可能かどうかを返す
        return Environment.MEDIA_MOUNTED.equals(state)
               || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);

    }


    /**
     * 指定された相対パスを付加したSDファイルパスを取得する。
     *
     * @param pathes 結合するパスの一覧
     * @return 指定された相対パスを付加したSDファイルパス
     */
    public static String getExternalStoragePath(
            final String... pathes
            ) {

        // 引数が null の場合は例外
        if (pathes == null) {

            throw new IllegalArgumentException();

        }

        // パス一覧を作成する
        final List<String>  workPathes = ConvertUtils.toList(pathes);

        // 先頭にルートディレクトリを追加する
        workPathes.add(0, Environment.getExternalStorageDirectory().getAbsolutePath());

        // ファイルパスを作成して返す
        return StringUtils.join(workPathes.toArray(new String[]{}), File.separator);

    }


    /**
     * 指定されたSDルートからのファイルorディレクトリパスが存在するかどうかを取得する。
     *
     * @param path SDルートからの相対ファイルorディレクトリパス
     * @return 指定されたSDルートからのファイルorディレクトリパスが存在する場合は true
     */
    public static boolean existsExternalStoragePath(
            final String path
            ) {

        // 引数が null の場合は例外
        if (path == null) {

            throw new IllegalArgumentException();

        }

        // 指定パスが存在するかどうかを返す
        return new File(path).exists();

    }


    /**
     * 指定されたSDルートからのディレクトリを新規作成する。
     *
     * @param dirPathes 作成するSDルートからのディレクトリパス一覧
     * @return 作成に成功した場合は true
     */
    public static boolean createExternalStorageDirectory(
            final String... dirPathes
            ) {

        // SDが存在しない場合
        if (!existsExternalStorage()) {

            // 失敗
            return false;

        }


        // 指定パスでファイルオブジェクトを作成する
        final File  dir = new File(
                getExternalStoragePath(dirPathes)
                );

        // 既にディレクトリが存在している場合
        if (dir.exists()) {

            // 成功を返す
            return true;

        }

        // ディレクトリを作成する
        return dir.mkdirs();

    }


    /**
     * 指定されたSDルートからのディレクトリパスのディレクトリを削除する。
     *
     * @param dirPathes 削除するSDルートからのディレクトリパス一覧
     * @return 削除に成功した場合は true
     */
    public static boolean removeExternalStorageDirectory(
            final String...   dirPathes
            ) {

        // SDが存在しない場合
        if (!existsExternalStorage()) {

            // 失敗
            return false;

        }


        // 指定パスでファイルオブジェクトを作成する
        final File  dir = new File(
                getExternalStoragePath(dirPathes)
                );

        // 既にディレクトリが存在していない場合
        if (!dir.exists()) {

            // 成功を返す
            return true;

        }

        // ディレクトリを削除する
        return dir.delete();

    }


}
