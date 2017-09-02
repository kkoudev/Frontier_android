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



/**
 * バリデータクラス。<br>
 * <br>
 * 文字列や値のバリデーションチェックを行うユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class Validator {


    /**
     * インスタンス生成防止。
     */
    private Validator() {

        // 処理なし

    }



    /**
     * 指定された文字列が整数値かどうかをチェックする。
     *
     * @param str チェックする文字列
     * @return 指定された文字列が整数値の場合は true
     */
    public static boolean isDigit(
            final String    str
            ) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // 数値以外
            return false;

        }

        // 整数値かどうかをチェックして返す
        return isDigit(str.toCharArray());

    }


    /**
     * 指定された文字が整数値かどうかをチェックする。
     *
     * @param strChar チェックする文字
     * @return 指定された文字が整数値の場合は true
     */
    public static boolean isDigit(
            final char  strChar
            ) {

        // 数値かどうかを返す
        return Character.isDigit(strChar);

    }


    /**
     * 指定された文字列が整数値かどうかをチェックする。
     *
     * @param strChars チェックする文字列
     * @return 指定された文字列が整数値の場合は true
     */
    public static boolean isDigit(
            final char[]    strChars
            ) {

        // nullまたは空文字の場合
        if ((strChars == null) || (strChars.length == 0)) {

            // 数値以外
            return false;

        }

        // 全文字分処理をする
        for (int i = 0; i < strChars.length; i++) {

            // 数値以外の場合
            if (!isDigit(strChars[i])) {

                // 数値以外
                return false;

            }

        }

        // 数値
        return true;

    }


    /**
     * 指定された文字列が小数値かどうかをチェックする。
     *
     * @param str チェックする文字列
     * @return 指定された文字列が小数値の場合は true
     */
    public static boolean isDecimal(
            final String    str
            ) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // 小数値以外
            return false;

        }

        // 前後に空白が含まれている場合
        // (Javaの仕様で、Double.parseDouble, Float.parseFloatは前後の空白文字を許容するため、
        //  前後の空白文字についてはここでチェックする)
        if (Character.isWhitespace((str.charAt(0)))
            || Character.isWhitespace(str.charAt(str.length() - 1))
            ) {

            // 小数値以外
            return false;

        }


        try {

            // 小数値へ変換する
            Double.parseDouble(str);

        } catch (final NumberFormatException e) {

            // 小数値以外
            return false;

        }

        // 小数値
        return true;

    }


    /**
     * 指定された文字列が小数値かどうかをチェックする。
     *
     * @param strChars チェックする文字列
     * @return 指定された文字列が小数値の場合は true
     */
    public static boolean isDecimal(
            final char[]    strChars
            ) {

        // nullまたは空文字の場合
        if ((strChars == null) || (strChars.length == 0)) {

            // 小数値以外
            return false;

        }

        // 小数値かどうかをチェックして返す
        return isDecimal(new String(strChars));

    }


    /**
     * 指定された文字列が英小文字かどうかをチェックする。
     *
     * @param str チェックする文字列
     * @return 指定された文字列が英小文字の場合は true
     */
    public static boolean isLowerCase(
            final String    str
            ) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // 英小文字以外
            return false;

        }

        // 文字を取得する
        final char[]    strChars = str.toCharArray();

        // 全文字分処理をする
        for (int i = 0; i < strChars.length; i++) {

            // 英小文字以外の場合
            if (!Character.isLowerCase(strChars[i])) {

                // 英小文字以外
                return false;

            }

        }

        // 英小文字
        return true;

    }


    /**
     * 指定された文字列が英大文字かどうかをチェックする。
     *
     * @param str チェックする文字列
     * @return 指定された文字列が英大文字の場合は true
     */
    public static boolean isUpperCase(
            final String    str
            ) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // 英大文字以外
            return false;

        }

        // 英大文字かどうかをチェックして返す
        return isUpperCase(str);

    }


    /**
     * 指定された文字列が英大文字かどうかをチェックする。
     *
     * @param strChars チェックする文字列
     * @return 指定された文字列が英大文字の場合は true
     */
    public static boolean isUpperCase(
            final char[]    strChars
            ) {

        // nullまたは空文字の場合
        if ((strChars == null) || (strChars.length == 0)) {

            // 英大文字以外
            return false;

        }

        // 全文字分処理をする
        for (int i = 0; i < strChars.length; i++) {

            // 英大文字以外の場合
            if (!Character.isUpperCase(strChars[i])) {

                // 英大文字以外
                return false;

            }

        }

        // 英大文字
        return true;

    }


    /**
     * 指定された文字列がアルファベットかどうかをチェックする。
     *
     * @param str チェックする文字列
     * @return 指定された文字列がアルファベットの場合は true
     */
    public static boolean isAlpha(
            final String    str
            ) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // アルファベット以外
            return false;

        }

        // アルファベットかどうかをチェックして返す
        return isAlpha(str.toCharArray());

    }


    /**
     * 指定された文字がアルファベットかどうかをチェックする。
     *
     * @param strChar チェックする文字
     * @return 指定された文字がアルファベットの場合は true
     */
    public static boolean isAlpha(
            final char  strChar
            ) {

        // アルファベットかどうかを返す
        return (('A' <= strChar) && (strChar <= 'Z'))
                || (('a' <= strChar) && (strChar <= 'z'));

    }


    /**
     * 指定された文字列がアルファベットかどうかをチェックする。
     *
     * @param strChars チェックする文字列
     * @return 指定された文字列がアルファベットの場合は true
     */
    public static boolean isAlpha(
            final char[]    strChars
            ) {

        // nullまたは空文字の場合
        if ((strChars == null) || (strChars.length == 0)) {

            // アルファベット以外
            return false;

        }

        // 全文字分処理をする
        for (int i = 0; i < strChars.length; i++) {

            final char  tempChar = strChars[i];

            // アルファベット以外の場合
            if (!isAlpha(tempChar)) {

                // アルファベット以外
                return false;

            }

        }

        // アルファベット
        return true;

    }


    /**
     * 指定された文字列がアルファベットまたは数値かどうかをチェックする。
     *
     * @param str チェックする文字列
     * @return 指定された文字列がアルファベットまたは数値の場合は true
     */
    public static boolean isAlphaNum(
            final String    str
            ) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // アルファベットと数値以外
            return false;

        }

        // アルファベットまたは数値化どうかをチェックして返す
        return isAlphaNum(str.toCharArray());

    }


    /**
     * 指定された文字がアルファベットまたは数値かどうかをチェックする。
     *
     * @param strChar チェックする文字
     * @return 指定された文字がアルファベットまたは数値の場合は true
     */
    public static boolean isAlphaNum(
            final char      strChar
            ) {

        // アルファベットまたは数値かどうかを返す
        return (('A' <= strChar) && (strChar <= 'Z'))
                || (('a' <= strChar) && (strChar <= 'z'))
                || (('0' <= strChar) && (strChar <= '9'));

    }


    /**
     * 指定された文字列がアルファベットまたは数値かどうかをチェックする。
     *
     * @param strChars チェックする文字列
     * @return 指定された文字列がアルファベットまたは数値の場合は true
     */
    public static boolean isAlphaNum(
            final char[]    strChars
            ) {

        // nullまたは空文字の場合
        if ((strChars == null) || (strChars.length == 0)) {

            // アルファベットと数値以外
            return false;

        }

        // 全文字分処理をする
        for (int i = 0; i < strChars.length; i++) {

            final char  tempChar = strChars[i];

            // アルファベットと数値以外の場合
            if (!isAlphaNum(tempChar)) {

                // アルファベットと数値以外
                return false;

            }

        }

        // アルファベットと数値
        return true;

    }


    /**
     * 指定された文字列がJavaの基準に従った空白かどうかをチェックする。
     *
     * @param str チェックする文字列
     * @return 指定された文字列がJavaの基準に従った空白の場合は true
     */
    public static boolean isWhitespace(
            final String    str
            ) {

        // nullまたは空文字の場合
        if ((str == null) || (str.length() == 0)) {

            // 空白以外
            return false;

        }

        // 空白かどうかをチェックして返す
        return isWhitespace(str.toCharArray());

    }


    /**
     * 指定された文字がJavaの基準に従った空白かどうかをチェックする。
     *
     * @param strChar チェックする文字
     * @return 指定された文字がJavaの基準に従った空白の場合は true
     */
    public static boolean isWhitespace(
            final char      strChar
            ) {

        // 空白文字かどうかを返す
        return Character.isWhitespace(strChar);

    }


    /**
     * 指定された文字列がJavaの基準に従った空白かどうかをチェックする。
     *
     * @param strChars チェックする文字列
     * @return 指定された文字列がJavaの基準に従った空白の場合は true
     */
    public static boolean isWhitespace(
            final char[]    strChars
            ) {

        // nullまたは空文字の場合
        if ((strChars == null) || (strChars.length == 0)) {

            // 空白以外
            return false;

        }

        // 全文字分処理をする
        for (int i = 0; i < strChars.length; i++) {

            final char  tempChar = strChars[i];

            // 空白以外の場合
            if (!isWhitespace(tempChar)) {

                // 空白以外
                return false;

            }

        }

        // 空白
        return true;

    }


    /**
     * 指定された文字列が指定範囲内の値かどうかを取得する。
     *
     * @param baseValue         チェックする値
     * @param rangeLowValue     範囲の下限値
     * @param rangeHighValue    範囲の上限値
     * @param includeRangeLow   下限値を範囲に含むかどうか
     * @param includeRangeHigh  上限値を範囲に含むかどうか
     * @return 指定された値が範囲内にある場合は true。数値以外または範囲外の場合は false
     */
    public static boolean isRange(
            final String    baseValue,
            final int       rangeLowValue,
            final int       rangeHighValue,
            final boolean   includeRangeLow,
            final boolean   includeRangeHigh
            ) {

        // 有効な値以外の場合
        if (!isDecimal(baseValue)) {

            // 数値以外
            return false;

        }

        // 小数値に変換する
        final double    value = ConvertUtils.toDouble(baseValue);

        // 範囲内に値があるかどうか返す
        return (includeRangeLow ? (rangeLowValue <= value) : (rangeLowValue < value))
               && (includeRangeHigh ? (value <= rangeHighValue) : (value < rangeHighValue));

    }


    /**
     * 指定された文字列が指定範囲内の値かどうかを取得する。
     *
     * @param baseValue         チェックする値
     * @param rangeLowValue     範囲の下限値
     * @param rangeHighValue    範囲の上限値
     * @param includeRangeLow   下限値を範囲に含むかどうか
     * @param includeRangeHigh  上限値を範囲に含むかどうか
     * @return 指定された値が範囲内にある場合は true。数値以外または範囲外の場合は false
     */
    public static boolean isRange(
            final String    baseValue,
            final long      rangeLowValue,
            final long      rangeHighValue,
            final boolean   includeRangeLow,
            final boolean   includeRangeHigh
            ) {

        // 有効な値以外の場合
        if (!isDecimal(baseValue)) {

            // 数値以外
            return false;

        }

        // 小数値に変換する
        final double    value = ConvertUtils.toDouble(baseValue);

        // 範囲内に値があるかどうか返す
        return (includeRangeLow ? (rangeLowValue <= value) : (rangeLowValue < value))
               && (includeRangeHigh ? (value <= rangeHighValue) : (value < rangeHighValue));

    }


    /**
     * 指定された文字列が指定範囲内の値かどうかを取得する。
     *
     * @param baseValue         チェックする値
     * @param rangeLowValue     範囲の下限値
     * @param rangeHighValue    範囲の上限値
     * @param includeRangeLow   下限値を範囲に含むかどうか
     * @param includeRangeHigh  上限値を範囲に含むかどうか
     * @return 指定された値が範囲内にある場合は true。数値以外または範囲外の場合は false
     */
    public static boolean isRange(
            final String    baseValue,
            final float     rangeLowValue,
            final float     rangeHighValue,
            final boolean   includeRangeLow,
            final boolean   includeRangeHigh
            ) {

        // 有効な値以外の場合
        if (!isDecimal(baseValue)) {

            // 数値以外
            return false;

        }

        // 小数値に変換する
        final double    value = ConvertUtils.toDouble(baseValue);

        // 範囲内に値があるかどうか返す
        return (includeRangeLow ? (rangeLowValue <= value) : (rangeLowValue < value))
               && (includeRangeHigh ? (value <= rangeHighValue) : (value < rangeHighValue));

    }


    /**
     * 指定された文字列が指定範囲内の値かどうかを取得する。
     *
     * @param baseValue         チェックする値
     * @param rangeLowValue     範囲の下限値
     * @param rangeHighValue    範囲の上限値
     * @param includeRangeLow   下限値を範囲に含むかどうか
     * @param includeRangeHigh  上限値を範囲に含むかどうか
     * @return 指定された値が範囲内にある場合は true。数値以外または範囲外の場合は false
     */
    public static boolean isRange(
            final String    baseValue,
            final double    rangeLowValue,
            final double    rangeHighValue,
            final boolean   includeRangeLow,
            final boolean   includeRangeHigh
            ) {

        // 有効な値以外の場合
        if (!isDecimal(baseValue)) {

            // 数値以外
            return false;

        }

        // 小数値に変換する
        final double    value = ConvertUtils.toDouble(baseValue);

        // 範囲内に値があるかどうか返す
        return (includeRangeLow ? (rangeLowValue <= value) : (rangeLowValue < value))
               && (includeRangeHigh ? (value <= rangeHighValue) : (value < rangeHighValue));

    }


}
