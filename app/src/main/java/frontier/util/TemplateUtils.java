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

import frontier.app.FRNameValuePair;

import android.content.Context;


/**
 * テンプレートファイル操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class TemplateUtils {


    /**
     * テンプレート指定の左トークン
     */
    private static final String     TEMPLATE_LEFT_TOKEN     = "{{";

    /**
     * テンプレート指定の右トークン
     */
    private static final String     TEMPLATE_RIGHT_TOKEN    = "}}";



    /**
     * インスタンス生成防止。
     *
     */
    private TemplateUtils() {

        // 処理なし

    }


    /**
     * 指定されたアセット内テンプレートファイルを置換して文字列として取得する。<br>
     * <br>
     * 指定されたファイル内にある以下の文字列パターンを置換する。<br>
     * <br>
     * {{key}}<br>
     * <br>
     * keyは名称値の名前である。<br>
     * 上記を名称値の値で置換する。<br>
     *
     * @param context       利用するコンテキスト
     * @param assetFilePath アセット内テンプレートファイルパス
     * @param params        置換用名称値パラメータ
     * @return 置換後のテンプレート文字列。失敗時は null
     * @throws IllegalArgumentException ファイルパスが null または ファイルが存在しない場合
     * @throws IllegalStateException    内部エラーが発生した場合
     */
    public static String getTemplate(
            final Context               context,
            final String                assetFilePath,
            final FRNameValuePair...    params
            ) {

        // ファイルパスが null または存在しない場合は例外
        if ((assetFilePath == null) || !AssetUtils.exists(context, assetFilePath)) {

            throw new IllegalArgumentException();

        }


        try {

            // 指定ファイルを開いて文字列へ変換する
            final StringBuilder     strBuf = new StringBuilder(
                    IOUtils.toString(AssetUtils.open(context, assetFilePath), true)
                    );

            // パラメータ指定がある場合
            if (params != null) {

                // パラメータ分繰り返す
                for (final FRNameValuePair param : params) {

                    // 指定パラメータを置換する
                    StringUtils.replace(
                            strBuf,
                            TEMPLATE_LEFT_TOKEN + param.getName() + TEMPLATE_RIGHT_TOKEN,
                            String.valueOf(param.getValue())
                            );

                }

            }

            // 置換後のテンプレート文字列を返す
            return strBuf.toString();

        } catch (final IOException e) {

            throw new IllegalStateException(e);

        }


    }

}
