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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;



/**
 * マニフェスト情報アクセス操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class ManifestUtils {


    /**
     * インスタンス生成防止。
     *
     */
    private ManifestUtils() {

        // 処理なし

    }


    /**
     * マニフェストに記載されたバージョンコードを取得する。
     *
     * @param context   利用するコンテキスト
     * @return マニフェストに記載されたバージョンコード
     */
    public static int getVersionCode(
            final Context   context
            ) {

        // 引数が null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }


        try {

            // バージョンコードを取得して返す
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA
                    ).versionCode;

        } catch (final NameNotFoundException e) {

            throw new IllegalStateException(e);

        }

    }


    /**
     * マニフェストに記載されたバージョン名を取得する。
     *
     * @param context   利用するコンテキスト
     * @return マニフェストに記載されたバージョン名
     */
    public static String getVersionName(
            final Context   context
            ) {

        // 引数が null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }


        try {

            // バージョン名を取得して返す
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA
                    ).versionName;

        } catch (final NameNotFoundException e) {

            throw new IllegalStateException(e);

        }

    }


    /**
     * デバッグモードかどうかを取得する。
     *
     * @param context   利用するコンテキスト
     * @return デバッグモードの場合は true
     */
    public static boolean isDebug(
            final Context   context
            ) {

        // 引数が null の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }


        try {

            // バージョン名を取得して返す
            return (context.getPackageManager().getApplicationInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA
                    ).flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;

        } catch (final NameNotFoundException e) {

            throw new IllegalStateException(e);

        }

    }


}
