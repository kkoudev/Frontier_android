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

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;


/**
 * 汎用ユーティリティークラス。<br>
 * <br>
 * プログラムで汎用的に利用されるユーティリティーメソッドを定義したクラス。
 *
 * @author Kou
 *
 */
public final class GeneralUtils {



    /**
     * 型サイズ : boolean
     */
    public static final int         SIZE_BOOLEAN    = 1;

    /**
     * 型サイズ : byte
     */
    public static final int         SIZE_BYTE       = 1;

    /**
     * 型サイズ : char
     */
    public static final int         SIZE_CHAR       = 2;

    /**
     * 型サイズ : short
     */
    public static final int         SIZE_SHORT      = 2;

    /**
     * 型サイズ : int
     */
    public static final int         SIZE_INT        = 4;

    /**
     * 型サイズ : long
     */
    public static final int         SIZE_LONG       = 8;

    /**
     * 型サイズ : float
     */
    public static final int         SIZE_FLOAT      = SIZE_INT;

    /**
     * 型サイズ : double
     */
    public static final int         SIZE_DOUBLE     = SIZE_LONG;




    /**
     * float型の有効桁数
     */
    private static final float      EPSILON_FLOAT   = 1.0e-7f;

    /**
     * double型の有効桁数
     */
    private static final double     EPSILON_DOBULE  = 1.0e-16;




    /**
     * インスタンス生成防止。
     *
     */
    private GeneralUtils() {

        // 処理なし

    }


    /**
     * オブジェクト一致判定を行う。<br>
     * <br>
     * オブジェクトが null のときも考慮した一致判定を行う。
     *
     * @param obj1  比較オブジェクト1
     * @param obj2  比較オブジェクト2
     * @return true : 一致　false 不一致
     */
    public static boolean equalsObject(
            final Object obj1,
            final Object obj2
            ) {

        return obj1 == null ? obj2 == null : obj1.equals(obj2);

    }


    /**
     * float型の値の一致判定を行う。
     *
     * @param value1    比較するfloat値1
     * @param value2    比較するfloat値2
     * @return true : 一致　false 不一致
     */
    public static boolean equalsFloat(
            final float value1,
            final float value2
            ) {

        // 大きい方の値を取得する
        final float     max     = Math.max(value1, value2);

        // 有効桁数と比較する値を取得する
        final float     value   = max == 0.0f ? 0.0f : Math.abs((value1 - value2) / max);

        // 一致するかどうかを返す
        return value < EPSILON_FLOAT;

    }


    /**
     * double型の値の一致判定を行う。
     *
     * @param value1    比較するdouble値1
     * @param value2    比較するdouble値2
     * @return true : 一致　false 不一致
     */
    public static boolean equalsDouble(
            final double value1,
            final double value2
            ) {

        // 大きい方の値を取得する
        final double    max     = Math.max(value1, value2);

        // 有効桁数と比較する値を取得する
        final double    value   = max == 0.0f ? 0.0f : Math.abs((value1 - value2) / max);

        // 一致するかどうかを返す
        return value < EPSILON_DOBULE;

    }


    /**
     * 指定されたObject型データを指定されたインテントへ追加する。<br>
     * <br>
     * 指定された値の型を判定し、<br>
     * 適切な型であれば指定されたバンドルへ値が追加される。<br>
     * Bundleでサポートされていない型の場合は値は追加されない。<br>
     *
     * @param intent    追加先インテント
     * @param key       追加する値名称
     * @param value     追加する値
     * @return 値の追加に成功した場合は true
     * @throws IllegalArgumentException インテントまたは値名称が null の場合
     */
    public static boolean putObjectIntent(
            final Intent    intent,
            final String    key,
            final Object    value
            ) {

        // インテントまたは値名称が null の場合
        if ((intent == null) || (key == null)) {

            throw new IllegalArgumentException();

        }


        // Serializable型の場合
        if (value instanceof Serializable) {

            // Serializable型として追加する
            intent.putExtra(
                    key,
                    (Serializable)value
                    );

        // Bundle型の場合
        } else if (value instanceof Bundle) {

            // Bundle型として追加する
            intent.putExtra(key, (Bundle)value);

        // Parcelable型の場合
        } else if (value instanceof Parcelable) {

            // Parcelable型として追加する
            intent.putExtra(
                    key,
                    (Parcelable)value
                    );

        // Parcelable[]型の場合
        } else if (value instanceof Parcelable[]) {

            // Parcelable[]型として追加する
            intent.putExtra(
                    key,
                    (Parcelable[])value
                    );

        // その他
        } else {

            // 追加失敗
            return false;

        }


        // 追加成功
        return true;

    }


    /**
     * 指定されたObject型データを指定されたバンドルへ追加する。<br>
     * <br>
     * 指定された値の型を判定し、<br>
     * 適切な型であれば指定されたバンドルへ値が追加される。<br>
     * Bundleでサポートされていない型の場合は値は追加されない。<br>
     *
     * @param bundle    追加先バンドル
     * @param key       追加する値名称
     * @param value     追加する値
     * @return 値の追加に成功した場合は true
     * @throws IllegalArgumentException バンドルまたは値名称が null の場合
     */
    public static boolean putObjectBundle(
            final Bundle    bundle,
            final String    key,
            final Object    value
            ) {

        // バンドルまたは値名称が null の場合
        if ((bundle == null) || (key == null)) {

            throw new IllegalArgumentException();

        }


        // Serializable型の場合
        if (value instanceof Serializable) {

            // Serializable型として追加する
            bundle.putSerializable(
                    key,
                    (Serializable)value
                    );

        // Bundle型の場合
        } else if (value instanceof Bundle) {

            // Bundle型として追加する
            bundle.putBundle(
                    key,
                    (Bundle)value
                    );

        // Parcelable型の場合
        } else if (value instanceof Parcelable) {

            // Parcelable型として追加する
            bundle.putParcelable(
                    key,
                    (Parcelable)value
                    );

        // Parcelable[]型の場合
        } else if (value instanceof Parcelable[]) {

            // Parcelable[]型として追加する
            bundle.putParcelableArray(
                    key,
                    (Parcelable[])value
                    );

        // その他
        } else {

            // 追加失敗
            return false;

        }


        // 追加成功
        return true;

    }


}
