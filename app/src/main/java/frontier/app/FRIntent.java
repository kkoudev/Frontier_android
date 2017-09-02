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
package frontier.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import frontier.util.GeneralUtils;


/**
 * フレームワーク拡張インテント。
 *
 * @author Kou
 *
 */
public class FRIntent extends Intent {


    /**
     * 拡張インテントを作成する。
     *
     */
    public FRIntent() {

        super();

    }


    /**
     * 指定されたインテントで拡張インテントを作成する。
     *
     * @param intent インテント
     */
    public FRIntent(
            final Intent intent
            ) {

        super(intent);

    }


    /**
     * 指定したアクションで拡張インテントを作成する。
     *
     * @param action アクション文字列
     */
    public FRIntent(
            final String action
            ) {

        super(action);

    }


    /**
     * 指定したアクションとコンテンツURIで拡張インテントを作成する。
     *
     * @param action    アクション文字列
     * @param uri       コンテンツURI
     */
    public FRIntent(
            final String    action,
            final Uri       uri
            ) {

        super(action, uri);

    }


    /**
     * 指定された現在パッケージのコンテキストと遷移先クラスで拡張インテントを作成する。
     *
     * @param packageContext    現在パッケージのコンテキスト
     * @param cls               遷移先クラス
     */
    public FRIntent(
            final Context   packageContext,
            final Class<?>  cls
            ) {

        super(packageContext, cls);

    }


    /**
     * 指定されたアクション、コンテンツURI、現在パッケージコンテキスト、遷移先クラスで
     * 拡張インテントを作成する。
     *
     * @param action            アクション文字列
     * @param uri               コンテンツURI
     * @param packageContext    現在パッケージコンテキスト
     * @param cls               遷移先クラス
     */
    public FRIntent(
            final String    action,
            final Uri       uri,
            final Context   packageContext,
            final Class<?>  cls
            ) {

        super(action, uri, packageContext, cls);

    }


    /**
     * 指定されたパラメータ一覧を設定した拡張インテントを作成する。
     *
     * @param params    設定するパラメータ一覧
     * @return 指定されたパラメータ一覧を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final FRNameValuePair... params
            ) {

        return createIntent(null, null, null, null, params);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass
            ) {

        return createIntent(context, destClass, null, null, (FRNameValuePair[])null);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param flag          Intentフラグ情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final int                           flag
            ) {

        return createIntent(context, destClass, null, flag, (FRNameValuePair[])null);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param flags         Intentフラグ情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final int[]                         flags
            ) {

        return createIntent(context, destClass, null, flags, (FRNameValuePair[])null);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param flag          Intentフラグ情報
     * @param params        設定するパラメータ一覧情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final int                           flag,
            final FRNameValuePair...            params
            ) {

        return createIntent(context, destClass, null, new int[] {flag}, params);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param params        設定するパラメータ一覧情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final FRNameValuePair...            params
            ) {

        return createIntent(context, destClass, null, null, params);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param flags         Intentフラグ情報
     * @param params        設定するパラメータ一覧情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return createIntent(context, destClass, null, flags, params);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param action        Intentアクション情報
     * @param flag          Intentフラグ情報
     * @param params        設定するパラメータ一覧情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final String                        action,
            final int                           flag,
            final FRNameValuePair...            params
            ) {

        return createIntent(context, destClass, action, new int[] {flag}, params);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param action        Intentアクション情報
     * @param flags         Intentフラグ情報
     * @param params        設定するパラメータ一覧情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final String                        action,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return createIntent(context, destClass, action, null, (String[])null, null, flags, params);

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param action        Intentアクション情報
     * @param type          Intentタイプ情報
     * @param data          Intentデータ情報
     * @param flags         Intentフラグ情報
     * @param params        設定するパラメータ一覧情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final String                        action,
            final String                        type,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return createIntent(
                context,
                destClass,
                action,
                type,
                (String[])null,
                data,
                flags,
                params
                );

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param action        Intentアクション情報
     * @param type          Intentタイプ情報
     * @param category      Intentカテゴリ情報
     * @param data          Intentデータ情報
     * @param flags         Intentフラグ情報
     * @param params        設定するパラメータ一覧情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final String                        action,
            final String                        type,
            final String                        category,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return createIntent(
                context,
                destClass,
                action,
                type,
                new String[] {category},
                data,
                flags,
                params
                );

    }


    /**
     * 指定された各種情報を設定した拡張インテントを作成する。
     *
     * @param context       利用するコンテキスト情報
     * @param destClass     遷移先クラス
     * @param action        Intentアクション情報
     * @param type          Intentタイプ情報
     * @param categories    Intentカテゴリ情報
     * @param data          Intentデータ情報
     * @param flags         Intentフラグ情報
     * @param params        設定するパラメータ一覧情報
     * @return 指定された各種情報を設定した拡張インテント
     */
    public static FRIntent createIntent(
            final Context                       context,
            final Class<?>                      destClass,
            final String                        action,
            final String                        type,
            final String[]                      categories,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        // 拡張インテントを作成する
        final FRIntent    intent = (((context == null) || (destClass == null))
                                    ? new FRIntent()
                                    : new FRIntent(context, destClass));

        // アクション情報がある場合
        if (action != null) {

            // アクション情報を設定する
            intent.setAction(action);

        }

        // タイプ情報がある場合
        if (type != null) {

            // タイプ情報を設定する
            intent.setType(type);

        }

        // カテゴリ情報がある場合
        if (categories != null) {

            // 全カテゴリ情報を追加する
            for (final String category : categories) {

                // カテゴリ情報を設定する
                intent.addCategory(category);

            }

        }

        // データ情報がある場合
        if (data != null) {

            // データ情報を設定する
            intent.setData(data);

        }

        // フラグ情報がある場合
        if (flags != null) {

            // 全フラグ情報を追加する
            for (final int flag : flags) {

                // フラグ情報を追加する
                intent.addFlags(flag);

            }

        }

        // 設定パラメータが有効である場合
        if (isValidParams(params)) {

            // パラメータをインテントに設定する
            putParams(intent, params);

        }


        // 作成したインテントを返す
        return intent;

    }


    /**
     * 指定されたインテントにパラメータ配列内容を設定する。
     *
     * @param intent    パラメータ設定先インテント
     * @param params    設定するパラメータ一覧
     */
    private static void putParams(
            final Intent            intent,
            final FRNameValuePair[] params
            ) {

        // パラメータの型別処理
        for (final FRNameValuePair param : params) {

            // キーと値を取得する
            final String    key   = param.getName();
            final Object    value = param.getValue();

            // 値を追加する
            if (!GeneralUtils.putObjectIntent(intent, key, value)) {

                // 追加失敗時は例外
                throw new IllegalArgumentException(
                        "No support class type. [class = " + value.getClass() + "]"
                        );

            }

        }

    }


    /**
     * パラメータ配列に利用可能な要素があるかどうかを取得する。
     *
     * @param params パラメータ配列
     * @return パラメータ配列に利用可能な要素がある場合は true
     */
    private static boolean isValidParams(
            final FRNameValuePair[] params
            ) {

        // パラメータ配列が null の場合
        if (params == null) {

            // パラメータ無効
            return false;

        }

        // 全パラメータを検索する
        for (final FRNameValuePair param : params) {

            // パラメータが null 以外の場合
            if (param != null) {

                // パラメータ有効
                return true;

            }

        }


        // パラメータ無効
        return false;

    }


    /**
     * 指定した名称の値をboolean型で取得する。
     *
     * @param name 取得する値名
     * @return 取得した値
     */
    public boolean getBooleanExtra(
            final String    name
            ) {

        return getBooleanExtra(name, false);

    }


    /**
     * 指定した名称の値をbyte型で取得する。
     *
     * @param name 取得する値名
     * @return 取得した値
     */
    public byte getByteExtra(
            final String    name
            ) {

        return getByteExtra(name, (byte)0);

    }


    /**
     * 指定した名称の値をchar型で取得する。
     *
     * @param name 取得する値名
     * @return 取得した値
     */
    public char getCharExtra(
            final String    name
            ) {

        return getCharExtra(name, (char)0);

    }


    /**
     * 指定した名称の値をshort型で取得する。
     *
     * @param name 取得する値名
     * @return 取得した値
     */
    public short getShortExtra(
            final String    name
            ) {

        return getShortExtra(name, (short)0);

    }


    /**
     * 指定した名称の値をint型で取得する。
     *
     * @param name 取得する値名
     * @return 取得した値
     */
    public int getIntExtra(
            final String    name
            ) {

        return getIntExtra(name, 0);

    }


    /**
     * 指定した名称の値をlong型で取得する。
     *
     * @param name 取得する値名
     * @return 取得した値
     */
    public long getLongExtra(
            final String    name
            ) {

        return getLongExtra(name, 0);

    }


    /**
     * 指定した名称の値をfloat型で取得する。
     *
     * @param name 取得する値名
     * @return 取得した値
     */
    public float getFloatExtra(
            final String    name
            ) {

        return getFloatExtra(name, 0.0f);

    }


    /**
     * 指定した名称の値をdouble型で取得する。
     *
     * @param name 取得する値名
     * @return 取得した値
     */
    public double getDoubleExtra(
            final String    name
            ) {

        return getDoubleExtra(name, 0.0d);

    }



}
