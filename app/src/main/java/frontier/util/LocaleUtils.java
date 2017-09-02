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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * ロケール用ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class LocaleUtils {


    /**
     * ロケールテーブル
     */
    private static final Map<String, Locale>    LOCALES = new HashMap<String, Locale>();


    /**
     * ロケールテーブルを初期化する
     */
    static {

        // 有効なロケールを取得する
        final Locale[]  locales = Locale.getAvailableLocales();

        // 全ロケール分処理をする
        for (final Locale locale : locales) {

            // ロケールをテーブルへ追加する
            LOCALES.put(locale.toString(), locale);

        }

    }



    /**
     * インスタンス生成防止。
     *
     */
    private LocaleUtils() {

        // 処理なし

    }


    /**
     * 指定された名称のロケールを取得する。
     *
     * @param name  ロケール名
     * @return 指定された名称のロケール。存在しない場合は null
     */
    public static Locale getLocale(
            final String    name
            ) {

        return LOCALES.get(name);

    }


}
