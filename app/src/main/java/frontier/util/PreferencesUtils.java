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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import frontier.app.FRNameValuePair;
import frontier.util.ConvertUtils.DataConvertType;


/**
 * プリファレンス操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class PreferencesUtils {


    /**
     * フィールドキー名フォーマットのクラス名とフィールド名の区切りトークン
     */
    private static final String     FIELD_TOKEN         = ",";

    /**
     * フィールドキー名フォーマット<br>
     * <br>
     * 1$ = クラス名<br>
     * 2$ = フィールド名<br>
     */
    private static final String     FIELD_KEY_FORMAT    =
        "%1$s" + FIELD_TOKEN + "%2$s";



    /**
     * インスタンス生成防止。
     *
     */
    private PreferencesUtils() {

        // 処理なし

    }


    /**
     * 指定されたプリファレンス識別キーのプリファレンスを取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @return 指定されたプリファレンス識別キーのプリファレンス
     * @throws IllegalArgumentException 利用するコンテキストまたはプリファレンス識別キーが null の場合
     */
    private static SharedPreferences getPreference(
            final Context   context,
            final String    key
            ) {

        // 引数が null の場合は例外
        if ((context == null) || (key == null)) {

            throw new IllegalArgumentException();

        }

        // 指定識別キーのプリファレンスを返す
        return context.getSharedPreferences(
                key,
                Context.MODE_PRIVATE
                );

    }


    /**
     * 指定されたオブジェクト値をプリファレンスへ書きこむ。<br>
     *
     * @param <T>   書きこむ値の型
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param value     書き込み値
     * @return 書き込みに成功した場合は true
     * @throws IllegalArgumentException 利用するコンテキスト、プリファレンス識別キー、書き込み値のいずれかが null の場合
     */
    public static <T> boolean writeObject(
            final Context   context,
            final String    key,
            final T         value
            ) {

        // 引数が null の場合は例外
        if ((context == null) || (key == null) || (value == null)) {

            throw new IllegalArgumentException();

        }

        // プリファレンスを取得する
        final SharedPreferences         pref = getPreference(context, key);

        // 書き込み用エディタを取得する
        final SharedPreferences.Editor  edit = pref.edit();

        // 変換可能型の場合
        if (ConvertUtils.canConvertType(value.getClass())) {

            // 文字列へ変換して書きこむ
            edit.putString(key, String.valueOf(value));

            // 変更を反映する
            return edit.commit();

        }


        try {

            // 書きこむ値クラスを含んだ全スーパークラス情報を取得する
            final List<Class<?>> classes = ReflectUtils.getSuperClasses(
                    value.getClass(),
                    Object.class,
                    true,
                    false
                    );

            // 書きこむ値クラスを含んだ全スーパークラス情報分繰り返す
            for (final Class<?> nowClass : classes) {

                // 全フィールドを取得する
                final List<Field>   fields = ReflectUtils.getClassInstanceFields(nowClass);

                // 全フィールド分処理をする
                for (final Field field : fields) {

                    // フィールドが final の場合
                    if (Modifier.isFinal(field.getModifiers())) {

                        // 次のフィールドへ
                        continue;

                    }

                    // フィールドの値を取得する
                    final Object    valueObj = field.get(value);

                    // 書きこむ値が null の場合
                    if (valueObj == null) {

                        // 次のフィールドへ
                        continue;

                    }


                    // パラメータを追加する
                    edit.putString(
                            String.format(
                                    FIELD_KEY_FORMAT,
                                    nowClass.getName(),
                                    field.getName()
                                    ),
                            String.valueOf(valueObj)
                            );

                }

            }

            // 変更を反映する
            return edit.commit();

        } catch (final Throwable e) {

            e.printStackTrace();

            // 書き込み失敗
            return false;

        }

    }


    /**
     * 指定されたクラスの値をプリファレンスから読み込む。<br>
     *
     * @param <T>   読み込む値の型
     * @param context       利用するコンテキスト
     * @param key           プリファレンス識別キー
     * @param valueClass    読み込む値のクラス型
     * @return 読み込んだ値のインスタンス
     * @throws IllegalArgumentException 利用するコンテキスト、プリファレンス識別キー、読み込む値のクラス型のいずれかが null の場合
     */
    public static <T> T readObject(
            final Context   context,
            final String    key,
            final Class<T>  valueClass
            ) {

        // nullの場合は例外
        if ((context == null) || (valueClass == null)) {

            throw new IllegalArgumentException();

        }

        try {

            // プリファレンスを取得する
            final SharedPreferences     pref = context.getSharedPreferences(
                                                key,
                                                Context.MODE_PRIVATE
                                                );
            // 変換可能型の場合
            if (ConvertUtils.canConvertType(valueClass)) {

                // 変換して返す
                return ConvertUtils.toType(DataConvertType.GENERAL, valueClass, pref.getString(key, null));

            }

            // 返却値のインスタンスを生成する
            final T     retValue = ReflectUtils.newInstance(valueClass);

            // 読み込む値クラスを含んだ全スーパークラス情報を取得する
            final List<Class<?>> classes = ReflectUtils.getSuperClasses(
                    valueClass,
                    Object.class,
                    true,
                    false
                    );

            // 読み込む値クラスを含んだ全スーパークラス情報分繰り返す
            for (final Class<?> nowClass : classes) {

                // 全フィールドを取得する
                final List<Field>   fields = ReflectUtils.getClassInstanceFields(nowClass);

                // 全フィールド分処理をする
                for (final Field field : fields) {

                    // フィールドが final の場合
                    if (Modifier.isFinal(field.getModifiers())) {

                        // 次のフィールドへ
                        continue;

                    }


                    // パラメータを取得する
                    final String    value = pref.getString(
                                                String.format(
                                                        FIELD_KEY_FORMAT,
                                                        nowClass.getName(),
                                                        field.getName()
                                                        ),
                                                null
                                                );

                    // 値が見つからなかった場合
                    if (value == null) {

                        // 次のフィールドへ
                        continue;

                    }

                    // 変換した値を取得する
                    final Object    convertValue = ConvertUtils.toType(
                            DataConvertType.GENERAL,
                            field.getType(),
                            value
                            );

                    // 変換に成功した場合
                    if (convertValue != null) {

                        // 値を設定する
                        ReflectUtils.setInstanceFieldValue(
                                retValue,
                                field.getName(),
                                convertValue
                                );

                    }

                }

            }

            // 読み込んだ値を返却する
            return retValue;

        } catch (final Throwable e) {

            e.printStackTrace();

            // 読み込み失敗
            return null;

        }

    }


    /**
     * 指定された名称値をプリファレンスへ書きこむ。<br>
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param params    書き込むパラメータ一覧
     * @return 書き込みに成功した場合は true
     * @throws IllegalArgumentException 利用するコンテキスト、プリファレンス識別キーのいずれかが null の場合
     * @throws IllegalArgumentException 書きこむパラメータ一覧が null または 0 の場合
     */
    public static boolean writeParameters(
            final Context               context,
            final String                key,
            final FRNameValuePair...    params
            ) {

        // 引数が null の場合は例外
        if ((context == null)
            || (key == null)
            || (params == null)
            || (params.length == 0)
            ) {

            throw new IllegalArgumentException();

        }

        // プリファレンスを取得する
        final SharedPreferences         pref = getPreference(context, key);

        // 書き込み用エディタを取得する
        final SharedPreferences.Editor  edit = pref.edit();

        // パラメータ個数分繰り返す
        for (final FRNameValuePair param : params) {

            // パラメータを書き込む
            edit.putString(
                    param.getName(),
                    String.valueOf(param.getValue())
                    );

        }


        // 書き込みを反映する
        return edit.commit();

    }

    /**
     * 指定プリファレンスの値を削除する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @return 削除に成功した場合は true
     * @throws IllegalArgumentException 利用するコンテキスト、プリファレンス識別キーのいずれかが null の場合
     */
    public static boolean remove(
            final Context   context,
            final String    key
            ) {

        // 引数が null の場合は例外
        if ((context == null) || (key == null)) {

            throw new IllegalArgumentException();

        }

        // プリファレンスを取得する
        final SharedPreferences         pref = context.getSharedPreferences(
                                                key,
                                                Context.MODE_PRIVATE
                                                );

        // 書き込み用エディタを取得する
        final SharedPreferences.Editor  edit = pref.edit();

        // 値をクリアする
        edit.clear();

        // 変更を反映する
        return edit.commit();

    }


    /**
     * 指定プリファレンスが存在するかどうかを取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @return 指定プリファレンスが存在する場合は true
     */
    public static boolean exists(
            final Context   context,
            final String    key
            ) {

        return !getPreference(context, key).getAll().isEmpty();

    }


    /**
     * 指定プリファレンスの値が存在するかどうかを取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定プリファレンスの値が存在する場合は true
     */
    public static boolean exists(
            final Context   context,
            final String    key,
            final String    name
            ) {

        // プリファレンスを取得する
        final SharedPreferences         pref = getPreference(context, key);

        // 指定名称の値が存在するかどうかを返す
        return pref.contains(name);

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を String 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static String getString(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return getPreference(context, key).getString(name, null);

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を Integer 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static Integer getInt(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return ConvertUtils.toInt(
                getPreference(context, key).getString(name, null),
                null
                );

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を Long 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static Long getLong(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return ConvertUtils.toLong(
                getPreference(context, key).getString(name, null),
                null
                );

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を Boolean 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static Boolean getBoolean(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return ConvertUtils.toBoolean(
                getPreference(context, key).getString(name, null),
                null
                );

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を Float 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static Float getFloat(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return ConvertUtils.toFloat(
                getPreference(context, key).getString(name, null),
                null
                );

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を Double 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static Double getDouble(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return ConvertUtils.toDouble(
                getPreference(context, key).getString(name, null),
                null
                );

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を Byte 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static Byte getByte(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return ConvertUtils.toByte(
                getPreference(context, key).getString(name, null),
                null
                );

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を Short 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static Short getShort(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return ConvertUtils.toShort(
                getPreference(context, key).getString(name, null),
                null
                );

    }


    /**
     * 指定したプリファレンス識別キーの指定名称の値を Date 形式で取得する。
     *
     * @param context   利用するコンテキスト
     * @param key       プリファレンス識別キー
     * @param name      プリファレンス値名称
     * @return 指定したプリファレンス識別キーの指定名称の値
     */
    public static Date getDate(
            final Context   context,
            final String    key,
            final String    name
            ) {

        return ConvertUtils.toDate(
                getPreference(context, key).getString(name, null)
                );

    }

}
