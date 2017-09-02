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

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * 文字列操作を簡略化するユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class StringUtils {


    /**
     * 文字コード : Shift_JIS
     */
    public static final String          SHIFT_JIS       = "Shift_JIS";

    /**
     * 文字コード : UTF-8
     */
    public static final String          UTF_8           = "UTF-8";

    /**
     * 文字コード : UTF-16
     */
    public static final String          UTF_16          = "UTF-16";

    /**
     * 文字コード : US-ASCII
     */
    public static final String          US_ASCII        = "US-ASCII";

    /**
     * 文字コード : ASCII
     */
    public static final String          ASCII           = "ASCII";

    /**
     * 文字コード : ISO-8859-1
     */
    public static final String          ISO_8859_1      = "ISO-8859-1";

    /**
     * デフォルトエンコーディング
     */
    public static final String          DEFAULT_CHARSET = UTF_8;



    /**
     * クエリパラメータの項目名と値の区切り文字
     */
    private static final String     TOKEN_QUERY_VALUE           = "=";

    /**
     * クエリパラメータの連結文字
     */
    private static final String     TOKEN_QUERY_PARAMS          = "&";

    /**
     * URLとクエリ文字列の連結文字
     */
    private static final String     TOKEN_URL_QUERY             = "?";

    /**
     * 指数表記文字
     */
    private static final char       TOKEN_EXPONENT              = 'E';

    /**
     * 小数点文字
     */
    private static final char       TOKEN_PERIOD                = '.';



    /**
     * インスタンス生成防止。
     *
     */
    private StringUtils() {

        // 処理なし

    }



    /**
     * 指定した文字列の改行文字を削除する。
     *
     * @param str 文字列
     * @return 改行文字を削除した文字列
     */
    public static String deleteLineFeeds(
            final String    str
            ) {

        // 引数が不正の場合は例外
        if (str == null) {

            throw new IllegalArgumentException();

        }

        // 改行文字を削除して返す
        return str.replace("\r", "").replace("\n", "");

    }


    /**
     * 先頭文字を大文字に変換した文字列を取得する。
     *
     * @param str 元となる文字列
     * @return 先頭文字を大文字に変換した文字列
     */
    public static String toUpperCaseFront(
            final String    str
            ) {

        // 引数が不正の場合は例外
        if (str == null) {

            throw new IllegalArgumentException();

        }

        // 文字列長が 1 より大きい場合
        if (str.length() > 1) {

            final char[]    strChars = str.toCharArray();

            // 先頭を大文字にする
            strChars[0] = Character.toUpperCase(strChars[0]);

            // 変換した文字列を作成して返す
            return new String(strChars);

        } else if (str.length() == 1) {

            // 文字列を大文字にして返す
            return str.toUpperCase();

        } else {

            // そのまま返す
            return str;

        }

    }


    /**
     * 先頭文字を小文字に変換した文字列を取得する。
     *
     * @param str 元となる文字列
     * @return 先頭文字を小文字に変換した文字列
     */
    public static String toLowerCaseFront(
            final String    str
            ) {

        // 引数が不正の場合は例外
        if (str == null) {

            throw new IllegalArgumentException();

        }

        // 文字列長が 1 より大きい場合
        if (str.length() > 1) {

            final char[]    strChars = str.toCharArray();

            // 先頭を小文字にする
            strChars[0] = Character.toLowerCase(strChars[0]);

            // 変換した文字列を作成して返す
            return new String(strChars);

        } else if (str.length() == 1) {

            // 文字列を小文字にして返す
            return str.toLowerCase();

        } else {

            // そのまま返す
            return str;

        }

    }


    /**
     * 一致する文字列があるかどうかを検索する。
     *
     * @param sourceStr     検索元文字列
     * @param patternStr    検索するパターン文字列
     * @return 検索文字列が見つかった位置。見つからなかった場合は -1
     */
    public static int indexOf(
            final String    sourceStr,
            final String    patternStr
            ) {

        // 検索元文字列とパターン文字列が null の場合は例外
        if ((sourceStr == null) || (patternStr == null)) {

            throw new IllegalArgumentException();

        }

        // 文字列検索を行う
        return indexOf(
                sourceStr.toCharArray(),
                0,
                sourceStr.length(),
                patternStr.toCharArray(),
                0,
                patternStr.length(),
                0
                );

    }


    /**
     * 一致する文字列があるかどうかを検索する。
     *
     * @param sourceStr     検索元文字列
     * @param patternStr    検索するパターン文字列
     * @param fromIndex     検索開始位置
     * @return 検索文字列が見つかった位置。見つからなかった場合は -1
     */
    public static int indexOf(
            final String    sourceStr,
            final String    patternStr,
            final int       fromIndex
            ) {

        // 検索元文字列とパターン文字列が null の場合は例外
        if ((sourceStr == null) || (patternStr == null)) {

            throw new IllegalArgumentException();

        }

        // 文字列検索を行う
        return indexOf(
                sourceStr.toCharArray(),
                0,
                sourceStr.length(),
                patternStr.toCharArray(),
                0,
                patternStr.length(),
                fromIndex
                );

    }


    /**
     * 一致する文字列があるかどうかを検索する。
     *
     * @param sourceStr     検索元文字列
     * @param sourceOffset  検索元文字列先頭オフセット位置
     * @param sourceLength  検索元文字列長
     * @param patternStr    検索するパターン文字列
     * @param patternOffset 検索するパターン文字列先頭オフセット位置
     * @param patternLength 検索するパターン文字列長
     * @param fromIndex     検索開始位置
     * @return 検索文字列が見つかった位置。見つからなかった場合は -1
     */
    public static int indexOf(
            final String    sourceStr,
            final int       sourceOffset,
            final int       sourceLength,
            final String    patternStr,
            final int       patternOffset,
            final int       patternLength,
            final int       fromIndex
            ) {

        // 検索元文字列とパターン文字列が null の場合は例外
        if ((sourceStr == null) || (patternStr == null)) {

            throw new IllegalArgumentException();

        }

        // 文字列検索を行う
        return indexOf(
                sourceStr.toCharArray(),
                sourceOffset,
                sourceLength,
                patternStr.toCharArray(),
                patternOffset,
                patternLength,
                0
                );


    }


    /**
     * 一致する文字列があるかどうかを検索する。
     *
     * @param sourceStr                 検索元文字列
     * @param sourceOffset              検索元文字列先頭オフセット位置
     * @param sourceLength              検索元文字列長
     * @param patternStr                検索するパターン文字列
     * @param patternOffset             検索するパターン文字列先頭オフセット位置
     * @param patternLength             検索するパターン文字列長
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @param fromIndex                 検索開始位置
     * @return 検索文字列が見つかった位置。見つからなかった場合は -1
     */
    public static int indexOf(
            final String    sourceStr,
            final int       sourceOffset,
            final int       sourceLength,
            final String    patternStr,
            final int       patternOffset,
            final int       patternLength,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd,
            final int       fromIndex
            ) {

        // 検索元文字列とパターン文字列が null の場合は例外
        if ((sourceStr == null) || (patternStr == null)) {

            throw new IllegalArgumentException();

        }

        // 文字列検索を行う
        return indexOf(
                sourceStr.toCharArray(),
                sourceOffset,
                sourceLength,
                patternStr.toCharArray(),
                patternOffset,
                patternLength,
                ignoreSurroundTokenBegin == null ? null : ignoreSurroundTokenBegin.toCharArray(),
                ignoreSurroundTokenEnd == null ? null : ignoreSurroundTokenEnd.toCharArray(),
                fromIndex
                );

    }


    /**
     * 一致する文字列があるかどうかを検索する。
     *
     * @param sourceChars   検索元文字列
     * @param patternChars  検索するパターン文字列
     * @return 検索文字列が見つかった位置。見つからなかった場合は -1
     */
    public static int indexOf(
            final char[]    sourceChars,
            final char[]    patternChars
            ) {

        // 検索元文字列とパターン文字列が null の場合は例外
        if ((sourceChars == null) || (patternChars == null)) {

            throw new IllegalArgumentException();

        }

        // 文字列検索を行う
        return indexOf(
                sourceChars,
                0,
                sourceChars.length,
                patternChars,
                0,
                patternChars.length,
                0
                );

    }


    /**
     * 一致する文字列があるかどうかを検索する。
     *
     * @param sourceChars   検索元文字列
     * @param patternChars  検索するパターン文字列
     * @param fromIndex     検索開始位置
     * @return 検索文字列が見つかった位置。見つからなかった場合は -1
     */
    public static int indexOf(
            final char[]    sourceChars,
            final char[]    patternChars,
            final int       fromIndex
            ) {

        // 検索元文字列とパターン文字列が null の場合は例外
        if ((sourceChars == null) || (patternChars == null)) {

            throw new IllegalArgumentException();

        }

        // 文字列検索を行う
        return indexOf(
                sourceChars,
                0,
                sourceChars.length,
                patternChars,
                0,
                patternChars.length,
                fromIndex
                );

    }


    /**
     * 一致する文字列があるかどうかを検索する。
     *
     * @param sourceChars   検索元文字列
     * @param sourceOffset  検索元文字列先頭オフセット位置
     * @param sourceLength  検索元文字列長
     * @param patternChars  検索するパターン文字列
     * @param patternOffset 検索するパターン文字列先頭オフセット位置
     * @param patternLength 検索するパターン文字列長
     * @param fromIndex     検索開始位置
     * @return 検索文字列が見つかった位置。見つからなかった場合は -1
     */
    public static int indexOf(
            final char[]    sourceChars,
            final int       sourceOffset,
            final int       sourceLength,
            final char[]    patternChars,
            final int       patternOffset,
            final int       patternLength,
            final int       fromIndex
            ) {

        // 文字列検索を行う
        return indexOf(
                sourceChars,
                0,
                sourceChars.length,
                patternChars,
                0,
                patternChars.length,
                null,
                null,
                fromIndex
                );

    }


    /**
     * 指定された文字列とその開始位置を比較し、一致するかどうかを取得する。
     *
     * @param sourceChars   検索元文字列
     * @param searchIndex   検索開始位置
     * @param patternChars  一致判定する文字列
     * @return 一致した場合は true
     * @see #indexOf(char[], int, int, char[], int, int, char[], char[], int)
     */
    private static boolean equalsChars(
            final char[]    sourceChars,
            final int       searchIndex,
            final char[]    patternChars
            ) {

        final int   patternCount     = patternChars.length;     // パターン文字数
        int         matchCount       = 0;                       // 一致した文字数

        // 一致した文字数がパターン文字数より小さい場合
        // かつ現在位置の文字とパターン文字が等しい場合
        while ((matchCount < patternCount)
               && (sourceChars[searchIndex + matchCount] == patternChars[matchCount])
               ) {

            // 一致した文字数をインクリメント
            matchCount++;

        }

        // 一致した文字数とパターン文字数が等しい場合は一致とする
        return (matchCount == patternCount);

    }


    /**
     * 一致する文字列があるかどうかを検索する。
     *
     * @param sourceChars               検索元文字列
     * @param sourceOffset              検索元文字列先頭オフセット位置
     * @param sourceLength              検索元文字列長
     * @param patternChars              検索するパターン文字列
     * @param patternOffset             検索するパターン文字列先頭オフセット位置
     * @param patternLength             検索するパターン文字列長
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @param fromIndex                 検索開始位置
     * @return 検索文字列が見つかった位置。見つからなかった場合は -1
     */
    public static int indexOf(
            final char[]    sourceChars,
            final int       sourceOffset,
            final int       sourceLength,
            final char[]    patternChars,
            final int       patternOffset,
            final int       patternLength,
            final char[]    ignoreSurroundTokenBegin,
            final char[]    ignoreSurroundTokenEnd,
            final int       fromIndex
            ) {

        // 検索元文字列、検索する文字列が null の場合
        // またはオフセット位置が負の場合は例外
        if ((sourceChars == null) || (patternChars == null)
            || (sourceOffset < 0) || (patternOffset < 0)
            || (fromIndex < 0)
            ) {

            throw new IllegalArgumentException();

        }

        // 検索開始位置が検索元文字列長以上の場合
        if (fromIndex >= sourceLength) {

            // パターン文字列長が 0 の場合は検索元文字列長を返し、
            // それ以外の場合は見つからないので -1 を返す
            return patternLength == 0 ? sourceLength : -1;

        }


        final int   sourceSearchCount   = sourceLength - patternLength + 1;     // 検索元文字列検索数
        final int   patternSearchCount  = patternLength - patternOffset;        // 検索パターン文字列検索数
        final char  firstChar           = patternChars[patternOffset];          // 検索先頭文字
        int         searchIndex         = sourceOffset + fromIndex;             // 検索位置
        int         matchCount;                                                 // 一致した文字数


        // 分割対象外文字列を囲む開始・終了トークンが指定されていない場合
        if ((ignoreSurroundTokenBegin == null) && (ignoreSurroundTokenEnd == null)) {

            // 文字列を先頭から検索する
            for (; searchIndex < sourceSearchCount; searchIndex++) {

                // 先頭文字が見つかるまで現在位置を移動する
                while ((searchIndex < sourceSearchCount) && (firstChar != sourceChars[searchIndex])) {

                    // 検索位置を移動する
                    searchIndex++;

                }

                // 検索位置が検索元文字列検索数以上の場合
                if (searchIndex >= sourceSearchCount) {

                    // 見つからなかった
                    return -1;

                }

                // 一致した文字数を初期化する
                matchCount = 0;

                // 一致した文字数がパターン文字列検索数より小さい場合
                // かつ先頭の文字と等しい場合は繰り返し
                while ((matchCount < patternSearchCount)
                       && (sourceChars[searchIndex + matchCount] == patternChars[patternOffset + matchCount])
                       ) {

                    // 一致した文字数をインクリメント
                    matchCount++;

                }

                // 一致した文字数と検索パターン文字列長が等しい場合
                if (matchCount == patternSearchCount) {

                    // 見つかった位置を返す
                    return searchIndex;

                }

            }

        } else {

            // 分割対象外囲いトークンのどちらか一方が指定されていない場合
            if ((ignoreSurroundTokenBegin == null)
                || (ignoreSurroundTokenBegin.length == 0)
                || (ignoreSurroundTokenEnd == null)
                || (ignoreSurroundTokenEnd.length == 0)
                ) {

                throw new IllegalArgumentException();

            }

            final char  ignoreSurroundBeginTokenFirstChar   = ignoreSurroundTokenBegin[0];  // 分割対象外囲い開始トークンの先頭文字
            final char  ignoreSurroundEndTokenFirstChar     = ignoreSurroundTokenEnd[0];    // 分割対象外囲い終了トークンの先頭文字
            boolean     ignoreSurroundEnabled               = false;                        // 分割対象外ループ中かどうか


            // 文字列を先頭から検索する
            for (; searchIndex < sourceSearchCount; searchIndex++) {

                // 先頭文字検索ループ
                for (; searchIndex < sourceSearchCount;) {

                    final char  sourceChar = sourceChars[searchIndex];      // 検索対象文字

                    // 分割対象外囲い開始トークン開始文字の場合
                    // かつ分割対象外ループ中でない場合
                    if ((ignoreSurroundBeginTokenFirstChar == sourceChar)
                        && !ignoreSurroundEnabled
                        ) {

                        // 分割対象外囲い開始トークンの場合
                        if (equalsChars(sourceChars, searchIndex, ignoreSurroundTokenBegin)) {

                            // 分割対象外ループ状態を有効にする
                            ignoreSurroundEnabled = true;

                            // 現在位置に分割対象外囲い開始トークン文字数を加算する
                            searchIndex += ignoreSurroundTokenBegin.length;

                            // 次の文字へ
                            continue;

                        }

                    // 分割対象外ループ中の場合
                    // かつ分割対象外囲い終了トークン開始文字の場合
                    } else if (ignoreSurroundEnabled
                               && (ignoreSurroundEndTokenFirstChar == sourceChar)
                               ) {

                        // 分割対象外囲い終了トークンの場合
                        if (equalsChars(sourceChars, searchIndex, ignoreSurroundTokenEnd)) {

                            // 分割対象外ループ状態を無効する
                            ignoreSurroundEnabled = false;

                            // 現在位置に分割対象外囲い終了トークン文字数を加算する
                            searchIndex += ignoreSurroundTokenEnd.length;

                            // 次の文字へ
                            continue;

                        }

                    // その他
                    } else {

                        // 処理なし

                    }

                    // 先頭文字が見つかった場合
                    if (firstChar == sourceChar) {

                        // 分割対象外ループ状態が無効の場合
                        if (!ignoreSurroundEnabled) {

                            // ループ終了
                            break;

                        }

                    }

                    // 次の文字へ
                    searchIndex++;

                }

                // 文字列終端に達している場合
                if (searchIndex >= sourceSearchCount) {

                    // 見つからなかった
                    return -1;

                }


                // 一致した文字数を初期化する
                matchCount = 0;


                // 一致した文字数がパターン文字列検索数より小さい場合
                while (matchCount < patternSearchCount) {

                    final char  sourceChar = sourceChars[searchIndex + matchCount];     // 検索対象文字

                    // 分割対象外囲い開始トークン開始文字の場合
                    if (sourceChar == ignoreSurroundBeginTokenFirstChar) {

                        // 分割対象外囲い開始トークンの場合
                        if (equalsChars(sourceChars, searchIndex + matchCount, patternChars)) {

                            // 分割対象外ループ状態を有効にする
                            ignoreSurroundEnabled = true;

                            // 現在位置に分割対象外囲い開始トークン文字数を加算する
                            searchIndex += ignoreSurroundTokenBegin.length;

                        }

                        // ループ終了
                        break;

                    // 検索対象文字と現在位置のパターン文字が等しい場合
                    } else if (sourceChar == patternChars[patternOffset + matchCount]) {

                        // 一致した文字数をインクリメント
                        matchCount++;

                    // その他
                    } else {

                        // ループ終了
                        break;

                    }

                }

                // 一致した文字数と検索パターン文字列長が等しい場合
                if (matchCount == patternSearchCount) {

                    // 見つかった位置を返す
                    return searchIndex;

                }

            }

        }


        // 見つからなかった
        return -1;

    }


    /**
     * 指定された文字列の配列を指定されたトークンで結合した文字列を取得する。
     *
     * @param strs      文字列配列
     * @param token     文字列連結トークン
     * @return 指定された文字列の配列を指定されたトークンで結合した文字列
     * @throws IllegalArgumentException 文字列配列または文字列連結トークンが null の場合
     */
    public static String join(
            final String[]  strs,
            final String    token
            ) {

        // 引数が不正の場合は例外
        if ((strs == null) || (token == null)) {

            throw new IllegalArgumentException();

        }


        final StringBuilder strBuf = new StringBuilder();    // 作業用文字列バッファ

        // 文字列配列分処理をする
        for (int i = 0; i < strs.length; i++) {

            // 文字列を追加する
            strBuf.append(strs[i]);

            // 最後の文字列以外の場合
            if (i != strs.length - 1) {

                // 文字列連結トークンを追加する
                strBuf.append(token);

            }

        }


        // 作成した文字列を返す
        return strBuf.toString();

    }


    /**
     * 指定された文字列の指定位置をリストへ追加する。
     *
     * @param list          追加先リスト
     * @param str           追加文字列
     * @param beginIndex    文字列の追加開始位置
     * @param endIndex      文字列の追加終了位置
     * @param ignoreEmpty   空要素を無視するかどうか
     */
    private static void addString(
            final ArrayList<String> list,
            final String            str,
            final int               beginIndex,
            final int               endIndex,
            final boolean           ignoreEmpty
            ) {

        // 空要素を無視する場合
        if (ignoreEmpty) {

            // 空以外の場合
            if (beginIndex != endIndex) {

                // 分割して追加する
                list.add(str.substring(beginIndex, endIndex));

            }

        } else {

            // 分割して追加する
            list.add(str.substring(beginIndex, endIndex));

        }

    }


    /**
     * 区切り文字で文字列を分割する。<br>
     * <br>
     * 空要素は無視せず、分割最大数で分割を行う。<br>
     *
     * @param str       分割する文字列
     * @param token     区切り文字列
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列または区切り文字列が null の場合
     */
    public static String[] split(
            final String    str,
            final String    token
            ) {

        return split(str, token, false, null, null, Integer.MAX_VALUE);

    }


    /**
     * 区切り文字で文字列を分割する。<br>
     * <br>
     * 空要素を無視するかどうかを指定し、分割最大数で分割を行う。<br>
     *
     * @param str           分割する文字列
     * @param token         区切り文字列
     * @param ignoreEmpty   空要素を無視するかどうか
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列または区切り文字列が null の場合
     */
    public static String[] split(
            final String    str,
            final String    token,
            final boolean   ignoreEmpty
            ) {

        return split(str, token, ignoreEmpty, null, null, Integer.MAX_VALUE);

    }


    /**
     * 区切り文字で文字列を分割する。
     * <br>
     * 空要素は無視せず、指定された分割数で分割を行う。<br>
     *
     * @param str       分割する文字列
     * @param token     区切り文字列
     * @param limit     分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列または区切り文字列が null の場合
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     */
    public static String[] split(
            final String    str,
            final String    token,
            final int       limit
            ) {

        return split(str, token, false, null, null, limit);

    }


    /**
     * 区切り文字で文字列を分割する。<br>
     * <br>
     * 空要素は無視するかどうかを指定し、指定された分割数で分割を行う。<br>
     *
     * @param str           分割する文字列
     * @param token         区切り文字列
     * @param ignoreEmpty   空要素を無視するかどうか
     * @param limit         分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列または区切り文字列が null の場合
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     */
    public static String[] split(
            final String    str,
            final String    token,
            final boolean   ignoreEmpty,
            final int       limit
            ) {

        return split(str, token, ignoreEmpty, null, null, limit);

    }


    /**
     * 区切り文字で文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * 指定されたトークンに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * <br>
     * 空要素は無視せず、分割最大数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param token                     区切り文字列
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列、区切り文字列、分割対象外文字列囲いトークンのいずれかが null の場合
     */
    public static String[] split(
            final String    str,
            final String    token,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd
            ) {

        return split(str, token, false, ignoreSurroundTokenBegin, ignoreSurroundTokenEnd, Integer.MAX_VALUE);

    }


    /**
     * 区切り文字で文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * 指定されたトークンに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * <br>
     * 空要素を無視するかどうかを指定し、分割最大数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param token                     区切り文字列
     * @param ignoreEmpty               空要素を無視するかどうか
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列、区切り文字列、分割対象外文字列囲いトークンのいずれかが null の場合
     */
    public static String[] split(
            final String    str,
            final String    token,
            final boolean   ignoreEmpty,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd
            ) {

        return split(str, token, ignoreEmpty, ignoreSurroundTokenBegin, ignoreSurroundTokenEnd, Integer.MAX_VALUE);

    }


    /**
     * 区切り文字で文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * 指定されたトークンに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * <br>
     * 空要素は無視せず、指定された分割数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param token                     区切り文字列
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @param limit                     分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列、区切り文字列、分割対象外文字列囲いトークンのいずれかが null の場合
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     */
    public static String[] split(
            final String    str,
            final String    token,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd,
            final int       limit
            ) {

        return split(str, token, false, ignoreSurroundTokenBegin, ignoreSurroundTokenEnd, limit);

    }


    /**
     * 区切り文字で文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * 指定されたトークンに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * <br>
     * 空要素は無視するかどうかを指定し、指定された分割数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param token                     区切り文字列
     * @param ignoreEmpty               空要素を無視するかどうか
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @param limit                     分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列、区切り文字列のいずれかが null の場合
     * @throws IllegalArgumentException 分割対象外文字列を
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     * @throws IllegalArgumentException 分割対象外文字列を囲む開始・終了トークンのいずれか指定されていて、一方が null または 空文字の場合
     */
    public static String[] split(
            final String    str,
            final String    token,
            final boolean   ignoreEmpty,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd,
            final int       limit
            ) {

        // 分割文字列、区切り文字列が null、または分割最大数が 0 以下の場合は例外
        if ((str == null)
            || (token == null)
            || (limit <= 0)
            ) {

            throw new IllegalArgumentException();

        }


        final ArrayList<String> divStrings  = new ArrayList<String>();  // 分割用リスト
        final int               tokenLength = token.length();           // 分割トークン長
        int                     fromIndexLast;                          // 前回の分割トークン位置
        int                     fromIndex;                              // 現在の分割トークン位置


        // 分割対象外文字列囲いトークンが指定されていない場合
        if (((ignoreSurroundTokenBegin == null) || (ignoreSurroundTokenBegin.length() == 0))
            && ((ignoreSurroundTokenEnd == null) || (ignoreSurroundTokenEnd.length() == 0))
            ) {

            // 前回の分割トークン位置を初期化する
            fromIndexLast = -tokenLength;

            // 文字列分割ループ
            while (true) {

                // 文字列を検索する
                fromIndex = str.indexOf(token, fromIndexLast + tokenLength);

                // 区切り文字が見つからなかった場合
                // または「分割最大数 - 1」まで分割数が達している場合
                if ((fromIndex == -1) || (divStrings.size() == limit - 1)) {

                    // 残りの要素をリストへ追加
                    addString(
                            divStrings,
                            str,
                            fromIndexLast + tokenLength,
                            str.length(),
                            ignoreEmpty
                            );

                    // 分割終了
                    break;

                }

                // 分割して追加する
                addString(
                        divStrings,
                        str,
                        fromIndexLast + tokenLength,
                        fromIndex,
                        ignoreEmpty
                        );


                // 前回のインデックスとして更新
                fromIndexLast = fromIndex;

            }

        } else {

            // 分割対象外囲いトークンのどちらか一方が指定されていない場合
            if ((ignoreSurroundTokenBegin == null)
                || (ignoreSurroundTokenBegin.length() == 0)
                || (ignoreSurroundTokenEnd == null)
                || (ignoreSurroundTokenEnd.length() == 0)
                ) {

                throw new IllegalArgumentException();

            }


            int         surroundStartIndex;                     // 分割対象外文字列囲みトークン開始位置
            int         surroundEndIndex;                       // 分割対象外文字列囲みトークン終了位置
            boolean     endedSearchSurroundToken = false;       // 分割対象外文字列囲みトークン検索終了状態


            // トークン長を取得する
            final int   ignoreSurroundTokenBeginLength = ignoreSurroundTokenBegin.length(); // 分割対象外囲い開始トークン長
            final int   ignoreSurroundTokenEndLength   = ignoreSurroundTokenEnd.length();   // 分割対象外囲い終了トークン長

            // トークン位置を初期化する
            fromIndexLast       = -tokenLength;
            surroundStartIndex  = -ignoreSurroundTokenBeginLength;
            surroundEndIndex    = -ignoreSurroundTokenBeginLength;

            // 文字列分割ループ
            while (true) {

                // 文字列を検索する
                fromIndex = str.indexOf(token, fromIndexLast + tokenLength);

                // 区切り文字位置が見つかった場合
                // かつ分割対象外文字列囲みトークン検索が終了していない場合
                if (!endedSearchSurroundToken) {

                    // 分割対象外文字列囲い開始トークンで検索する
                    surroundStartIndex  = str.indexOf(ignoreSurroundTokenBegin,
                                                      surroundStartIndex + ignoreSurroundTokenBeginLength);

                    // 分割対象外文字列囲い開始トークンが見つからない場合
                    if (surroundStartIndex == -1) {

                        // 分割対象外文字列囲みトークン検索を終了する
                        endedSearchSurroundToken = true;

                    } else {

                        // 見つかった分割対象外囲い開始トークン数
                        int     searchIndex                   = surroundStartIndex + ignoreSurroundTokenBeginLength;
                        int     ignoreSurroundTokenBeginCount = 1;

                        // 分割対象外文字列囲い終了トークン位置を検索する
                        while (searchIndex < str.length()) {

                            // 開始トークンの場合
                            if (str.startsWith(ignoreSurroundTokenBegin, searchIndex)) {

                                // 分割対象外囲い開始トークン数をインクリメント
                                ignoreSurroundTokenBeginCount++;

                                // 開始トークン分次へ
                                searchIndex += ignoreSurroundTokenBeginLength;

                            // 終了トークンの場合
                            } else if (str.startsWith(ignoreSurroundTokenEnd, searchIndex)) {

                                // 分割対象外囲い開始トークン数をデクリメント
                                ignoreSurroundTokenBeginCount--;

                                // 分割対象外囲い開始トークン数が 0 になった場合
                                if (ignoreSurroundTokenBeginCount == 0) {

                                    // 現在位置を分割対象外囲い終了トークン位置とする
                                    surroundEndIndex = searchIndex;

                                    // ループ終了
                                    break;

                                }

                                // 終了トークン分次へ
                                searchIndex += ignoreSurroundTokenEndLength;

                            } else {

                                // 一文字分次の位置へ
                                searchIndex++;

                            }

                        }


                        // 区切り文字位置が分割対象外文字列囲みトークン内だった場合
                        while ((fromIndex != -1)
                               && (surroundStartIndex <= fromIndex)
                               && (fromIndex < surroundEndIndex + ignoreSurroundTokenEndLength)
                               ) {

                            // 次の区切り文字位置を検索する
                            fromIndex = str.indexOf(token, fromIndex + tokenLength);

                        }

                        // 分割対象外文字列囲みトークン位置を更新する
                        surroundStartIndex = surroundEndIndex;

                    }

                }


                // 区切り文字が見つからなかった場合
                // または「分割最大数 - 1」まで分割数が達している場合
                if ((fromIndex == -1) || (divStrings.size() == limit - 1)) {

                    // 残りの要素をリストへ追加
                    addString(
                            divStrings,
                            str,
                            fromIndexLast + tokenLength,
                            str.length(),
                            ignoreEmpty
                            );

                    // 分割終了
                    break;

                }


                // 分割して追加する
                addString(
                        divStrings,
                        str,
                        fromIndexLast + tokenLength,
                        fromIndex,
                        ignoreEmpty
                        );


                // 前回のインデックスとして更新
                fromIndexLast = fromIndex;

            }

        }


        // 作成した配列を返却する
        return divStrings.toArray(new String[divStrings.size()]);

    }


    /**
     * ホワイトスペースで文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * ホワイトスペースに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * ホワイトスペースとは、 {@link Validator#isWhitespace(char)} で true を返す文字のことである。<br>
     * <br>
     * 空要素は無視せず、分割最大数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     */
    public static String[] splitWhitespace(
            final String    str
            ) {

        return splitWhitespace(str, false, null, null, Integer.MAX_VALUE);

    }


    /**
     * ホワイトスペースで文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * ホワイトスペースに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * ホワイトスペースとは、 {@link Validator#isWhitespace(char)} で true を返す文字のことである。<br>
     * <br>
     * 空要素は無視せず、指定された分割数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param limit                     分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     */
    public static String[] splitWhitespace(
            final String    str,
            final int       limit
            ) {

        return splitWhitespace(str, false, null, null, limit);

    }


    /**
     * ホワイトスペースで文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * ホワイトスペースに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * ホワイトスペースとは、 {@link Validator#isWhitespace(char)} で true を返す文字のことである。<br>
     * <br>
     * 空要素を無視するかどうかを指定し、分割最大数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param ignoreEmpty               空要素を無視するかどうか
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     */
    public static String[] splitWhitespace(
            final String    str,
            final boolean   ignoreEmpty
            ) {

        return splitWhitespace(str, ignoreEmpty, null, null, Integer.MAX_VALUE);

    }


    /**
     * ホワイトスペースで文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * ホワイトスペースに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * ホワイトスペースとは、 {@link Validator#isWhitespace(char)} で true を返す文字のことである。<br>
     * <br>
     * 空要素は無視せず、分割最大数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     * @throws IllegalArgumentException 分割対象外文字列を囲む開始・終了トークンのいずれか指定されていて、一方が null または 空文字の場合
     */
    public static String[] splitWhitespace(
            final String    str,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd
            ) {

        return splitWhitespace(str, false, ignoreSurroundTokenBegin, ignoreSurroundTokenEnd, Integer.MAX_VALUE);

    }


    /**
     * ホワイトスペースで文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * ホワイトスペースに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * ホワイトスペースとは、 {@link Validator#isWhitespace(char)} で true を返す文字のことである。<br>
     * <br>
     * 空要素は無視せず、指定された分割数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @param limit                     分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     * @throws IllegalArgumentException 分割対象外文字列を囲む開始・終了トークンのいずれか指定されていて、一方が null または 空文字の場合
     */
    public static String[] splitWhitespace(
            final String    str,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd,
            final int       limit
            ) {

        return splitWhitespace(str, false, ignoreSurroundTokenBegin, ignoreSurroundTokenEnd, limit);

    }


    /**
     * ホワイトスペースで文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * ホワイトスペースに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * ホワイトスペースとは、 {@link Validator#isWhitespace(char)} で true を返す文字のことである。<br>
     * <br>
     * 空要素を無視するかどうかを指定し、分割最大数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param ignoreEmpty               空要素を無視するかどうか
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     * @throws IllegalArgumentException 分割対象外文字列を囲む開始・終了トークンのいずれか指定されていて、一方が null または 空文字の場合
     */
    public static String[] splitWhitespace(
            final String    str,
            final boolean   ignoreEmpty,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd
            ) {

        return splitWhitespace(str, ignoreEmpty, ignoreSurroundTokenBegin, ignoreSurroundTokenEnd, Integer.MAX_VALUE);

    }


    /**
     * ホワイトスペースで文字列を分割する。<br>
     * <br>
     * このメソッドは、<br>
     * ホワイトスペースに囲まれる文字列を区切り対象外として分割することが可能である。<br>
     * ホワイトスペースとは、 {@link Validator#isWhitespace(char)} で true を返す文字のことである。<br>
     * <br>
     * 空要素は無視するかどうかを指定し、指定された分割数で分割を行う。<br>
     *
     * @param str                       分割する文字列
     * @param ignoreEmpty               空要素を無視するかどうか
     * @param ignoreSurroundTokenBegin  分割対象外文字列を囲む開始トークン
     * @param ignoreSurroundTokenEnd    分割対象外文字列を囲む終了トークン
     * @param limit                     分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     * @throws IllegalArgumentException 分割対象外文字列を囲む開始・終了トークンのいずれか指定されていて、一方が null または 空文字の場合
     */
    public static String[] splitWhitespace(
            final String    str,
            final boolean   ignoreEmpty,
            final String    ignoreSurroundTokenBegin,
            final String    ignoreSurroundTokenEnd,
            final int       limit
            ) {

        // 分割文字列、分割対象外文字列囲いトークンが null、または分割最大数が 0 以下の場合は例外
        if ((str == null)
            || (limit <= 0)
            ) {

            throw new IllegalArgumentException();

        }


        final ArrayList<String> divStrings  = new ArrayList<String>();  // 分割用リスト
        int                     fromIndexLast;                          // 前回の分割トークン位置
        int                     fromIndex   = 0;                        // 現在の分割トークン位置


        // 分割対象外文字列囲いトークンが指定されていない場合
        if (((ignoreSurroundTokenBegin == null) || (ignoreSurroundTokenBegin.length() == 0))
            && ((ignoreSurroundTokenEnd == null) || (ignoreSurroundTokenEnd.length() == 0))
            ) {

            // 前回の分割トークン位置を初期化する
            fromIndexLast = -1;

            // 文字列分割ループ
            while (true) {

                // 現在位置が文字列長より小さい場合
                // かつホワイトスペース以外の場合は繰り返し
                while ((fromIndex < str.length()) && !Validator.isWhitespace(str.charAt(fromIndex))) {

                    // 次の文字へ
                    fromIndex++;

                }

                // 区切り文字が見つからなかった場合
                // または「分割最大数 - 1」まで分割数が達している場合
                if ((fromIndex == str.length()) || (divStrings.size() == limit - 1)) {

                    // 残りの要素をリストへ追加
                    addString(
                            divStrings,
                            str,
                            fromIndexLast + 1,
                            str.length(),
                            ignoreEmpty
                            );

                    // 分割終了
                    break;

                }

                // 分割して追加する
                addString(
                        divStrings,
                        str,
                        fromIndexLast + 1,
                        fromIndex,
                        ignoreEmpty
                        );


                // 前回のインデックスとして更新
                fromIndexLast = fromIndex;

                // 次の文字へ
                fromIndex++;

            }

        } else {

            // 分割対象外囲いトークンのどちらか一方が指定されていない場合
            if ((ignoreSurroundTokenBegin == null)
                || (ignoreSurroundTokenBegin.length() == 0)
                || (ignoreSurroundTokenEnd == null)
                || (ignoreSurroundTokenEnd.length() == 0)
                ) {

                throw new IllegalArgumentException();

            }


            int         surroundStartIndex;                     // 分割対象外文字列囲みトークン開始位置
            int         surroundEndIndex;                       // 分割対象外文字列囲みトークン終了位置
            boolean     endedSearchSurroundToken = false;       // 分割対象外文字列囲みトークン検索終了状態


            // トークン長を取得する
            final int   ignoreSurroundTokenBeginLength = ignoreSurroundTokenBegin.length(); // 分割対象外囲い開始トークン長
            final int   ignoreSurroundTokenEndLength   = ignoreSurroundTokenEnd.length();   // 分割対象外囲い終了トークン長

            // トークン位置を初期化する
            fromIndexLast       = -1;
            surroundStartIndex  = -ignoreSurroundTokenBeginLength;
            surroundEndIndex    = -ignoreSurroundTokenBeginLength;

            // 文字列分割ループ
            while (true) {

                // 現在位置が文字列長より小さい場合
                // かつホワイトスペース以外の場合は繰り返し
                while ((fromIndex < str.length()) && !Validator.isWhitespace(str.charAt(fromIndex))) {

                    // 次の文字へ
                    fromIndex++;

                }

                // 区切り文字位置が見つかった場合
                // かつ分割対象外文字列囲みトークン検索が終了していない場合
                if ((fromIndex < str.length()) && !endedSearchSurroundToken) {

                    // 分割対象外文字列囲い開始トークンで検索する
                    surroundStartIndex  = str.indexOf(ignoreSurroundTokenBegin,
                                                      surroundStartIndex + ignoreSurroundTokenBeginLength);

                    // 分割対象外文字列囲い開始トークンが見つからない場合
                    if (surroundStartIndex == -1) {

                        // 分割対象外文字列囲みトークン検索を終了する
                        endedSearchSurroundToken = true;

                    } else {

                        // 見つかった分割対象外囲い開始トークン数
                        int     searchIndex                   = surroundStartIndex + ignoreSurroundTokenBeginLength;
                        int     ignoreSurroundTokenBeginCount = 1;

                        // 分割対象外文字列囲い終了トークン位置を検索する
                        while (searchIndex < str.length()) {

                            // 開始トークンの場合
                            if (str.startsWith(ignoreSurroundTokenBegin, searchIndex)) {

                                // 分割対象外囲い開始トークン数をインクリメント
                                ignoreSurroundTokenBeginCount++;

                                // 開始トークン分次へ
                                searchIndex += ignoreSurroundTokenBeginLength;

                            // 終了トークンの場合
                            } else if (str.startsWith(ignoreSurroundTokenEnd, searchIndex)) {

                                // 分割対象外囲い開始トークン数をデクリメント
                                ignoreSurroundTokenBeginCount--;

                                // 分割対象外囲い開始トークン数が 0 になった場合
                                if (ignoreSurroundTokenBeginCount == 0) {

                                    // 現在位置を分割対象外囲い終了トークン位置とする
                                    surroundEndIndex = searchIndex;

                                    // ループ終了
                                    break;

                                }

                                // 終了トークン分次へ
                                searchIndex += ignoreSurroundTokenEndLength;

                            } else {

                                // 一文字分次の位置へ
                                searchIndex++;

                            }

                        }


                        // 区切り文字位置が分割対象外文字列囲みトークン内だった場合
                        while ((surroundStartIndex <= fromIndex)
                               && (fromIndex < surroundEndIndex + ignoreSurroundTokenEndLength)
                               ) {

                            // 現在位置の次の位置へ移動させる
                            fromIndex++;

                            // ホワイトスペースが見つかるまで検索する
                            while ((fromIndex < str.length()) && !Validator.isWhitespace(str.charAt(fromIndex))) {

                                // 次の文字へ
                                fromIndex++;

                            }

                        }

                        // 分割対象外文字列囲みトークン位置を更新する
                        surroundStartIndex = surroundEndIndex;

                    }

                }


                // 区切り文字が見つからなかった場合
                // または「分割最大数 - 1」まで分割数が達している場合
                if ((fromIndex == str.length()) || (divStrings.size() == limit - 1)) {

                    // 残りの要素をリストへ追加
                    addString(
                            divStrings,
                            str,
                            fromIndexLast + 1,
                            str.length(),
                            ignoreEmpty
                            );

                    // 分割終了
                    break;

                }


                // 分割して追加する
                addString(
                        divStrings,
                        str,
                        fromIndexLast + 1,
                        fromIndex,
                        ignoreEmpty
                        );


                // 前回のインデックスとして更新
                fromIndexLast = fromIndex;

                // 次の文字へ
                fromIndex++;

            }

        }


        // 作成した配列を返却する
        return divStrings.toArray(new String[divStrings.size()]);

    }


    /**
     * 指定した文字列に含まれた改行コードを元に分割する。<br>
     * <br>
     * 改行コード (CR, CRLF, LF) を元に分割を行う。<br>
     * 分割された各文字列からは改行コードは除去される。<br>
     * 改行コード形式を考慮せずに分割することが可能である。<br>
     * <br>
     * 空要素は無視せず、分割最大数で分割を行う。<br>
     *
     * @param str       分割する文字列
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     */
    public static String[] splitLines(
            final String    str
            ) {

        return splitLines(str, false);

    }


    /**
     * 指定した文字列に含まれた改行コードを元に分割する。<br>
     * <br>
     * 改行コード (CR, CRLF, LF) を元に分割を行う。<br>
     * 分割された各文字列からは改行コードは除去される。<br>
     * 改行コード形式を考慮せずに分割することが可能である。<br>
     * <br>
     * 空要素は無視するかどうかを指定し、分割最大数で分割を行う。<br>
     *
     * @param str           分割する文字列
     * @param ignoreEmpty   空要素を無視するかどうか
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     */
    public static String[] splitLines(
            final String    str,
            final boolean   ignoreEmpty
            ) {

        return splitLines(str, ignoreEmpty, Integer.MAX_VALUE);

    }


    /**
     * 指定した文字列に含まれた改行コードを元に分割する。<br>
     * <br>
     * 改行コード (\r または \n) を元に分割を行う。<br>
     * 分割された各文字列からは改行コードは除去される。<br>
     * 改行コード形式を考慮せずに分割することが可能である。<br>
     * <br>
     * 空要素は無視せず、指定された分割数で分割を行う。<br>
     *
     * @param str       分割する文字列
     * @param limit     分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     */
    public static String[] splitLines(
            final String    str,
            final int       limit
            ) {

        return splitLines(str, false, limit);

    }


    /**
     * 指定した文字列に含まれた改行コードを元に分割する。<br>
     * <br>
     * 改行コード (\r または \n) を元に分割を行う。<br>
     * 分割された各文字列からは改行コードは除去される。<br>
     * 改行コード形式を考慮せずに分割することが可能である。<br>
     * <br>
     * 空要素は無視するかどうかを指定し、指定された分割数で分割を行う。<br>
     *
     * @param str           分割する文字列
     * @param ignoreEmpty   空要素を無視するかどうか
     * @param limit         分割最大数
     * @return 分割した文字列
     * @throws IllegalArgumentException 分割する文字列が null の場合
     * @throws IllegalArgumentException 分割最大数が 0 以下の場合
     */
    public static String[] splitLines(
            final String    str,
            final boolean   ignoreEmpty,
            final int       limit
            ) {

        // 分割文字列が null、分割最大数が 0以下の場合は例外
        if ((str == null) || (limit <= 0)) {

            throw new IllegalArgumentException();

        }


        final ArrayList<String>     divStrings    = new ArrayList<String>();    // 分割用リスト
        int                         fromIndexLast = 0;                          // 前回の分割トークン位置
        int                         fromIndex;                                  // 現在の分割トークン位置
        int                         tokenLength;                                // トークン長


        // 文字列分割ループ
        for (fromIndex = 0; fromIndex < str.length();) {

            final char  tempChar = str.charAt(fromIndex);   // 現在位置の文字を取得する

            // 改行文字 (CR, LF) 以外の場合
            if ((tempChar != '\r') && (tempChar != '\n')) {

                // 現在位置をインクリメント
                fromIndex++;

                // 次の文字へ
                continue;

            }

            // 「分割最大数 - 1」まで分割数が達している場合
            if (divStrings.size() == limit - 1) {

                // 分割終了
                break;

            }

            // 最後の文字以外の場合
            // かつ現在位置が CR の場合
            // かつ次の位置が LF の場合
            if ((fromIndex != str.length() - 1)
                && (tempChar == '\r')
                && (str.charAt(fromIndex + 1) == '\n')
                ) {

                // トークン長を 2 文字分に設定する
                tokenLength = 2;

            } else {

                // トークン長を 1 文字分に設定する
                tokenLength = 1;

            }

            // 分割して追加する
            addString(
                    divStrings,
                    str,
                    fromIndexLast,
                    fromIndex,
                    ignoreEmpty
                    );


            // 前回のインデックスとして更新
            fromIndexLast = fromIndex + tokenLength;

            // トークン長分次の文字へ
            fromIndex += tokenLength;

        }

        // 前回分割トークン位置が文字列長より小さい場合
        if (fromIndexLast < str.length()) {

            // 残りの要素をリストへ追加
            addString(
                    divStrings,
                    str,
                    fromIndexLast,
                    str.length(),
                    ignoreEmpty
                    );

        }


        // 作成した文字列配列を返却する
        return divStrings.toArray(new String[divStrings.size()]);

    }


    /**
     * クエリー文字列のパラメータ名と値を格納したテーブルを取得する。
     *
     * @param queryString クエリー文字列
     * @return クエリー文字列のパラメータと値を格納したテーブル
     * @throws IllegalArgumentException クエリー文字列が null の場合
     */
    public static Map<String, String> splitQuery(
            final String    queryString
            ) {

        final String[]      params;     // 分割したパラメータ


        // パラメータが null の場合は例外
        if (queryString == null) {

            throw new IllegalArgumentException();

        }


        // パラメータを分割する
        params = split(
                queryString.substring(queryString.indexOf(TOKEN_URL_QUERY) + 1),
                TOKEN_QUERY_PARAMS
                );

        // 返却するパラメータテーブルを作成する
        final Map<String, String>      retTable = new HashMap<String, String>();

        // 分割したパラメータをテーブルへ格納する
        for (int i = 0; i < params.length; i++) {

            // パラメータ名と値に分割する
            final String[]  itemNameValue = split(
                    params[i],
                    TOKEN_QUERY_VALUE
                    );

            // 分割したパラメータ名と値をURLデコードしてテーブルへ設定する
            retTable.put(
                    URLDecoder.decode(itemNameValue[0]),
                    URLDecoder.decode(itemNameValue[1])
                    );

        }

        // 作成したテーブルを返す
        return retTable;

    }


    /**
     * 基となる文字列に含まれる指定文字列を全て置換する。
     *
     * @param srcStr    検索基文字列
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @return 置換後の文字列
     * @throws IllegalArgumentException 検索基文字列、検索する文字列、置換する文字列のいずれかが null の場合
     */
    public static String replace(
            final String    srcStr,
            final String    oldStr,
            final String    newStr
            ) {

        // 検索基文字列が null の場合は例外
        if (srcStr == null) {

            throw new IllegalArgumentException();

        }

        // 置換後の文字列を返す
        return replace(srcStr, oldStr, newStr, 0, srcStr.length(), Integer.MAX_VALUE);

    }


    /**
     * 基となる文字列に含まれる指定文字列を全て置換する。
     *
     * @param srcStr    検索基文字列
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @param offset    検索開始位置
     * @param length    検索長
     * @return 置換後の文字列
     * @throws IllegalArgumentException 検索基文字列、検索する文字列、置換する文字列のいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置と検索長が 0 より小さい場合
     */
    public static String replace(
            final String    srcStr,
            final String    oldStr,
            final String    newStr,
            final int       offset,
            final int       length
            ) {

        // 検索基文字列が null の場合は例外
        if (srcStr == null) {

            throw new IllegalArgumentException();

        }

        // 置換後の文字列を返す
        return replace(srcStr, oldStr, newStr, offset, length, Integer.MAX_VALUE);

    }


    /**
     * 基となる文字列に含まれる指定文字列を全て置換する。
     *
     * @param srcStr    検索基文字列
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @param offset    検索開始位置
     * @param length    検索長
     * @param count     置換個数
     * @return 置換後の文字列
     * @throws IllegalArgumentException 検索基文字列、検索する文字列、置換する文字列のいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置と検索長と置換個数が 0 より小さい場合
     */
    public static String replace(
            final String    srcStr,
            final String    oldStr,
            final String    newStr,
            final int       offset,
            final int       length,
            final int       count
            ) {

        // いずれかが null の場合、または検索開始位置と検索長と置換個数が 0 より小さい場合は例外
        if ((srcStr == null)
            || (oldStr == null)
            || (newStr == null)
            || (offset < 0)
            || (length < 0)
            || (count < 0)
            ) {

            throw new IllegalArgumentException();

        }


        final StringBuilder retStr = new StringBuilder();   // 作業用バッファ
        int                 oldIndex;                       // 前回検索文字列位置
        int                 newIndex;                       // 現在検索文字列位置
        int                 replaceCount = 0;               // 置換済み個数


        // 検索文字列長を取得する
        final int   oldStrLength = oldStr.length();

        // 検索終端位置を算出する
        final int   lastIndex = offset + length;

        // 前回検索文字列位置を初期化する
        oldIndex = -oldStrLength;

        // 指定した文字列を検索する
        newIndex = srcStr.indexOf(oldStr, offset);

        // 見つかった場合
        // かつ検索長より見つかった位置が小さい場合
        // かつ置換済み個数が置換する個数より小さい場合
        while ((newIndex != -1) && (newIndex < lastIndex) && (replaceCount < count)) {

            // 指定位置までの文字列をバッファへ代入する
            retStr.append(srcStr.substring(oldIndex + oldStrLength, newIndex));

            // 置換する文字列をバッファへ追加する
            retStr.append(newStr);

            // 次の文字列を検索する
            oldIndex = newIndex;
            newIndex = srcStr.indexOf(oldStr, oldIndex + oldStrLength);

            // 置換済み個数をインクリメント
            replaceCount++;

        }

        // 残りの文字列がある場合
        if (oldIndex + oldStrLength < srcStr.length()) {

            // 残りの文字列を追加する
            retStr.append(srcStr.substring(oldIndex + oldStrLength, srcStr.length()));

        }


        // 置換後の文字列を返す
        return retStr.toString();

    }


    /**
     * 基となる文字列バッファに含まれる指定文字列を全て置換する。
     *
     * @param srcStr    検索基文字列バッファ
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @throws IllegalArgumentException 検索基文字列バッファ、検索する文字列、置換する文字列のいずれかが null の場合
     */
    public static void replace(
            final StringBuilder srcStr,
            final String        oldStr,
            final String        newStr
            ) {

        // 検索基文字列が null の場合は例外
        if (srcStr == null) {

            throw new IllegalArgumentException();

        }

        // 文字列バッファを置換する
        replace(srcStr, oldStr, newStr, 0, srcStr.length(), Integer.MAX_VALUE);

    }


    /**
     * 基となる文字列バッファに含まれる指定文字列を全て置換する。
     *
     * @param srcStr    検索基文字列バッファ
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @param offset    検索開始位置
     * @param length    検索長
     * @throws IllegalArgumentException 検索基文字列バッファ、検索する文字列、置換する文字列のいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置と検索長が 0 より小さい場合
     */
    public static void replace(
            final StringBuilder srcStr,
            final String        oldStr,
            final String        newStr,
            final int           offset,
            final int           length
            ) {

        // 検索基文字列が null の場合は例外
        if (srcStr == null) {

            throw new IllegalArgumentException();

        }

        // 文字列バッファを置換する
        replace(srcStr, oldStr, newStr, offset, length, Integer.MAX_VALUE);

    }


    /**
     * 基となる文字列バッファに含まれる指定文字列を全て置換する。
     *
     * @param srcStr    検索基文字列バッファ
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @param offset    検索開始位置
     * @param length    検索長
     * @param count     置換個数
     * @throws IllegalArgumentException 検索基文字列バッファ、検索する文字列、置換する文字列のいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置と検索長と置換個数が 0 より小さい場合
     */
    public static void replace(
            final StringBuilder srcStr,
            final String        oldStr,
            final String        newStr,
            final int           offset,
            final int           length,
            final int           count
            ) {

        // いずれかが null の場合、または検索開始位置と検索長と置換個数が 0 より小さい場合は例外
        if ((srcStr == null)
            || (oldStr == null)
            || (newStr == null)
            || (offset < 0)
            || (length < 0)
            || (count < 0)
            ) {

            throw new IllegalArgumentException();

        }


        int     oldIndex;           // 前回検索文字列位置
        int     newIndex;           // 現在検索文字列位置
        int     replaceCount = 0;   // 置換済み個数


        // 検索文字列長を取得する
        final int   oldStrLength = oldStr.length();

        // 検索終端位置を算出する
        final int   lastIndex = offset + length;

        // 指定した文字列を検索する
        newIndex = srcStr.indexOf(oldStr, offset);

        // 見つかった場合
        // かつ検索長より見つかった位置が小さい場合
        // かつ置換済み個数が置換する個数より小さい場合
        while ((newIndex != -1) && (newIndex < lastIndex) && (replaceCount < count)) {

            // 指定位置までの文字列をバッファへ代入する
            srcStr.replace(newIndex, newIndex + oldStrLength, newStr);

            // 次の文字列を検索する
            oldIndex = newIndex + newStr.length();
            newIndex = srcStr.indexOf(oldStr, oldIndex);

            // 置換済み個数をインクリメント
            replaceCount++;

        }

    }


    /**
     * 基となる文字列に含まれる最初に見つかった指定文字列を置換する。
     *
     * @param srcStr    検索基文字列
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @return 置換後の文字列
     * @throws IllegalArgumentException 検索基文字列、検索する文字列、置換する文字列のいずれかが null の場合
     */
    public static String replaceFirst(
            final String    srcStr,
            final String    oldStr,
            final String    newStr
            ) {

        // 検索基文字列が null の場合は例外
        if (srcStr == null) {

            throw new IllegalArgumentException();

        }

        // 置換後の文字列を返す
        return replace(srcStr, oldStr, newStr, 0, srcStr.length(), 1);

    }


    /**
     * 基となる文字列に含まれる最初に見つかった指定文字列を置換する。
     *
     * @param srcStr    検索基文字列
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @param offset    検索開始位置
     * @param length    検索長
     * @return 置換後の文字列
     * @throws IllegalArgumentException 検索基文字列、検索する文字列、置換する文字列のいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置と検索長が 0 より小さい場合
     */
    public static String replaceFirst(
            final String    srcStr,
            final String    oldStr,
            final String    newStr,
            final int       offset,
            final int       length
            ) {

        // 検索基文字列が null の場合は例外
        if (srcStr == null) {

            throw new IllegalArgumentException();

        }

        // 置換後の文字列を返す
        return replace(srcStr, oldStr, newStr, offset, length, 1);

    }


    /**
     * 基となる文字列バッファに含まれる最初に見つかった指定文字列を置換する。
     *
     * @param srcStr    検索基文字列バッファ
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @throws IllegalArgumentException 検索基文字列バッファ、検索する文字列、置換する文字列のいずれかが null の場合
     */
    public static void replaceFirst(
            final StringBuilder srcStr,
            final String        oldStr,
            final String        newStr
            ) {

        // 検索基文字列が null の場合は例外
        if (srcStr == null) {

            throw new IllegalArgumentException();

        }

        // 文字列バッファを置換する
        replace(srcStr, oldStr, newStr, 0, srcStr.length(), 1);

    }


    /**
     * 基となる文字列バッファに含まれる最初に見つかった指定文字列を置換する。
     *
     * @param srcStr    検索基文字列バッファ
     * @param oldStr    検索する文字列
     * @param newStr    置換する文字列
     * @param offset    検索開始位置
     * @param length    検索長
     * @throws IllegalArgumentException 検索基文字列バッファ、検索する文字列、置換する文字列のいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置と検索長が 0 より小さい場合
     */
    public static void replaceFirst(
            final StringBuilder srcStr,
            final String        oldStr,
            final String        newStr,
            final int           offset,
            final int           length
            ) {

        // 検索基文字列が null の場合は例外
        if (srcStr == null) {

            throw new IllegalArgumentException();

        }

        // 文字列バッファを置換する
        replace(srcStr, oldStr, newStr, offset, length, 1);

    }


    /**
     * float値を指数表記しないで文字列へ変換する。<br>
     * <br>
     * 変換後の値が整数の場合は整数表記の文字列として返される。<br>
     *
     * @param value 変換する小数値
     * @return 指数表記されていない変換後の文字列。
     */
    public static String toDecimalString(
            final float     value
            ) {

        return toDecimalString(String.valueOf(value), false);

    }


    /**
     * double値を指数表記しないで文字列へ変換する。<br>
     * <br>
     * 変換後の値が整数の場合は整数表記の文字列として返される。<br>
     *
     * @param value 変換する小数値
     * @return 指数表記されていない変換後の文字列。
     */
    public static String toDecimalString(
            final double     value
            ) {

        return toDecimalString(String.valueOf(value), false);

    }


    /**
     * float値を指数表記しないで文字列へ変換する。<br>
     * <br>
     * 変換後の値が整数の場合でも小数表記するかどうかを指定できる。<br>
     *
     * @param value     変換する小数値
     * @param isDecimal 変換後の値が整数の場合でも小数表記する場合は true
     * @return 指数表記されていない変換後の文字列。
     */
    public static String toDecimalString(
            final float     value,
            final boolean   isDecimal
            ) {

        return toDecimalString(String.valueOf(value), isDecimal);

    }


    /**
     * double値を指数表記しないで文字列へ変換する。<br>
     * <br>
     * 変換後の値が整数の場合でも小数表記するかどうかを指定できる。<br>
     *
     * @param value     変換する小数値
     * @param isDecimal 変換後の値が整数の場合でも小数表記する場合は true
     * @return 指数表記されていない変換後の文字列。
     */
    public static String toDecimalString(
            final double    value,
            final boolean   isDecimal
            ) {

        return toDecimalString(String.valueOf(value), isDecimal);

    }


    /**
     * 数値文字列を指数表記しないで文字列へ変換する。<br>
     * <br>
     * 変換後の値が整数の場合でも小数表記するかどうかを指定できる。<br>
     *
     * @param value     変換する小数値の文字列
     * @param isDecimal 変換後の値が整数の場合でも小数表記する場合は true
     * @return 指数表記されていない変換後の文字列。
     * @throws IllegalArgumentException 変換する小数値の文字列に数値文字列以外を指定した場合
     */
    private static String toDecimalString(
            final String    value,
            final boolean   isDecimal
            ) {

        // 数値以外の場合は例外
        if (!Validator.isDecimal(value)) {

            throw new IllegalArgumentException();

        }


        final String    tempStr         = value.toUpperCase();              // 小数値を文字列へ変換する
        final int       periodIndex     = tempStr.indexOf(TOKEN_PERIOD);    // 小数点位置を取得する
        final int       exponentIndex   = tempStr.indexOf(TOKEN_EXPONENT);  // 指数トークン位置を検索する


        // 小数以外の場合
        // または指数表記されていない場合
        if ((periodIndex == -1)
            || (exponentIndex == -1)
            ) {

            // そのまま返す
            return value;

        }


        // 乗数より前の数値文字列を小数点を除去して取得する
        final String    frontExponent =
            tempStr.substring(0, periodIndex)
            + tempStr.substring(periodIndex + 1, exponentIndex);

        // 乗数を取得する
        final int       exponentValue = Integer.parseInt(
                tempStr.substring(exponentIndex + 1));


        final StringBuilder strBuf = new StringBuilder();   // 文字列バッファ
        final int           paddingCount;                   // 0 パディング個数


        // 乗数が負の値の場合
        if (exponentValue < 0) {

            // 挿入位置を算出する
            final int   insertIndex = periodIndex + exponentValue;

            // 挿入位置が正の値の場合
            if (insertIndex > 0) {

                // 乗数より前の文字列を追加する
                strBuf.append(frontExponent);

                // 指定位置へ小数点を挿入する
                strBuf.insert(insertIndex, TOKEN_PERIOD);

            } else {

                // 先頭文字を追加する
                strBuf.append('0');
                strBuf.append(TOKEN_PERIOD);

                // 0パディング数を算出する
                paddingCount = -(periodIndex + exponentValue);

                // 乗数分だけ 0 を挿入する
                for (int i = 0; i < paddingCount; i++) {

                    // 0 を追加する
                    strBuf.append('0');

                }

                // 乗数より前の文字列を追加する
                strBuf.append(frontExponent);

            }

            // 0 が末尾にある場合は全て除去する
            while (strBuf.charAt(strBuf.length() - 1) == '0') {

                // 末尾を除去する
                strBuf.deleteCharAt(strBuf.length() - 1);

            }

            // 末尾がピリオドの場合
            if (strBuf.charAt(strBuf.length() - 1) == TOKEN_PERIOD) {

                // 0 を追加する
                strBuf.append('0');

            }

        } else {

            // 乗数より前の文字列を追加する
            strBuf.append(frontExponent);


            // 挿入位置を算出する
            final int   insertIndex = periodIndex + exponentValue;

            // 挿入位置が文字列長より小さい場合
            if (insertIndex < frontExponent.length()) {

                // 指定位置へ小数点を挿入する
                strBuf.insert(insertIndex, TOKEN_PERIOD);

                // 0 が末尾に続いている場合は全て除去する
                while (strBuf.charAt(strBuf.length() - 1) == '0') {

                    // 末尾を除去する
                    strBuf.deleteCharAt(strBuf.length() - 1);

                }

                // 末尾がピリオドの場合
                if (strBuf.charAt(strBuf.length() - 1) == TOKEN_PERIOD) {

                    // 0 を追加する
                    strBuf.append('0');

                }

            // 挿入位置が文字列長と等しい場合
            } else if (insertIndex == frontExponent.length()) {

                // 小数表記する場合
                if (isDecimal) {

                    // ピリオド + 0を追加する
                    strBuf.append(TOKEN_PERIOD);
                    strBuf.append('0');

                }

            } else {

                // 0 パディング数を算出する
                paddingCount = insertIndex - frontExponent.length();

                // 乗数分だけ 0 を挿入する
                for (int i = 0; i < paddingCount; i++) {

                    // 0 を追加する
                    strBuf.append('0');

                }

                // 小数表記する場合
                if (isDecimal) {

                    // ピリオド + 0を追加する
                    strBuf.append(TOKEN_PERIOD);
                    strBuf.append('0');

                }

            }

        }


        // 変換した文字列を返却する
        return strBuf.toString();

    }


    /**
     * 開始トークンと終了トークンの範囲内にある文字列を取得する。<br>
     * <br>
     * 指定された取り出し元文字列内から<br>
     * 指定した開始トークンと終了トークンの範囲にある文字列を抽出する。<br>
     *
     * @param str           取り出し元文字列
     * @param tokenBegin    開始トークン
     * @param tokenEnd      終了トークン
     * @return 開始トークンと終了トークンの範囲にある文字列。見つからない場合は空文字
     * @throws IllegalArgumentException 取り出し元文字列、開始トークン、終了トークンのいずれかが null の場合
     */
    public static String extractTokenRange(
            final String    str,
            final String    tokenBegin,
            final String    tokenEnd
            ) {

        // 取り出し元文字列が null の場合は例外
        if (str == null) {

            throw new IllegalArgumentException();

        }

        // 取り出し元文字列全体を検索する
        return extractTokenRange(str, tokenBegin, tokenEnd, 0, str.length());

    }


    /**
     * 開始トークンと終了トークンの範囲内にある文字列を取得する。<br>
     * <br>
     * 指定された取り出し元文字列内から<br>
     * 指定した開始トークンと終了トークンの範囲にある文字列を抽出する。<br>
     *
     * @param str           取り出し元文字列
     * @param tokenBegin    開始トークン
     * @param tokenEnd      終了トークン
     * @param offset        検索開始位置
     * @param length        検索長
     * @return 開始トークンと終了トークンの範囲にある文字列。見つからない場合は空文字
     * @throws IllegalArgumentException 取り出し元文字列、開始トークン、終了トークンのいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置または検索長が 0 より小さい場合
     */
    public static String extractTokenRange(
            final String    str,
            final String    tokenBegin,
            final String    tokenEnd,
            final int       offset,
            final int       length
            ) {

        // 引数が null の場合は例外
        if ((str == null)
            || (tokenBegin == null)
            || (tokenEnd == null)
            || (offset < 0)
            || (length < 0)
            ) {

            throw new IllegalArgumentException();

        }

        // 開始トークンと終了トークンの範囲内文字列を取得して返す
        return new String(extractTokenRange(
                str.toCharArray(),
                tokenBegin.toCharArray(),
                tokenEnd.toCharArray(),
                offset,
                length)
                );

    }


    /**
     * 開始トークンと終了トークンの範囲内にある文字列を取得する。<br>
     * <br>
     * 指定された取り出し元文字列内から<br>
     * 指定した開始トークンと終了トークンの範囲にある文字列を抽出する。<br>
     *
     * @param sourceChars   取り出し元文字列
     * @param tokenBegin    開始トークン
     * @param tokenEnd      終了トークン
     * @param offset        検索開始位置
     * @param length        検索長
     * @return 開始トークンと終了トークンの範囲にある文字列。見つからない場合は空文字
     * @throws IllegalArgumentException 取り出し元文字列、開始トークン、終了トークンのいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置または検索長が 0 より小さい場合
     */
    public static char[] extractTokenRange(
            final char[]    sourceChars,
            final char[]    tokenBegin,
            final char[]    tokenEnd,
            final int       offset,
            final int       length
            ) {

        // 開始トークンと終了トークンの間の文字列を 1 回分だけ取り出す
        final String[]  retStrs = extractMultiTokenRange(sourceChars, tokenBegin, tokenEnd, offset, length, 1);

        // 取り出した文字列がある場合
        if (retStrs.length > 0) {

            // 文字列を返却する
            return retStrs[0].toCharArray();

        } else {

            // 空文字を返却する
            return new char[0];

        }

    }


    /**
     * 開始トークンと終了トークンの範囲内にある文字列一覧を取得する。<br>
     * <br>
     * 指定された取り出し元文字列内から<br>
     * 指定した開始トークンと終了トークンの範囲にある文字列を抽出する。<br>
     * 開始トークンと終了トークンの対応が複数存在する場合は、<br>
     * 開始トークンと終了トークンの範囲内にある文字列を複数返却する。<br>
     *
     * @param str           取り出し元文字列
     * @param tokenBegin    開始トークン
     * @param tokenEnd      終了トークン
     * @return 開始トークンと終了トークンの範囲内にある文字列
     * @throws IllegalArgumentException 取り出し元文字列、開始トークン、終了トークンのいずれかが null の場合
     */
    public static String[] extractMultiTokenRange(
            final String    str,
            final String    tokenBegin,
            final String    tokenEnd
            ) {

        // 取り出し元文字列が null の場合は例外
        if (str == null) {

            throw new IllegalArgumentException();

        }

        // 取り出し元文字列全体を検索する
        return extractMultiTokenRange(str, tokenBegin, tokenEnd, 0, str.length(), Integer.MAX_VALUE);

    }


    /**
     * 開始トークンと終了トークンの範囲内にある文字列一覧を取得する。<br>
     * <br>
     * 指定された取り出し元文字列内から<br>
     * 指定した開始トークンと終了トークンの範囲にある文字列を抽出する。<br>
     * 開始トークンと終了トークンの対応が複数存在する場合は、<br>
     * 開始トークンと終了トークンの範囲内にある文字列を複数返却する。<br>
     *
     * @param str           取り出し元文字列
     * @param tokenBegin    開始トークン
     * @param tokenEnd      終了トークン
     * @param offset        検索開始位置
     * @param length        検索長
     * @param count         取り出し最大数
     * @return 開始トークンと終了トークンの範囲内にある文字列
     * @throws IllegalArgumentException 取り出し元文字列、開始トークン、終了トークンのいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置または検索長が 0 より小さい場合
     * @throws IllegalArgumentException 取り出し最大数が 0 以下の場合
     */
    public static String[] extractMultiTokenRange(
            final String    str,
            final String    tokenBegin,
            final String    tokenEnd,
            final int       offset,
            final int       length,
            final int       count
            ) {

        // 引数が不正の場合は例外
        if ((str == null)
            || (tokenBegin == null)
            || (tokenEnd == null)
            || (offset < 0)
            || (length < 0)
            || (count <= 0)
            ) {

            throw new IllegalArgumentException();

        }

        // 開始トークンと終了トークンの範囲内にある文字列一覧を取得して返す
        return extractMultiTokenRange(
                str.toCharArray(),
                tokenBegin.toCharArray(),
                tokenEnd.toCharArray(),
                offset,
                length,
                count
                );

    }


    /**
     * 開始トークンと終了トークンの範囲内にある文字列一覧を取得する。<br>
     * <br>
     * 指定された取り出し元文字列内から<br>
     * 指定した開始トークンと終了トークンの範囲にある文字列を抽出する。<br>
     * 開始トークンと終了トークンの対応が複数存在する場合は、<br>
     * 開始トークンと終了トークンの範囲内にある文字列を複数返却する。<br>
     *
     * @param sourceChars   取り出し元文字列
     * @param tokenBegin    開始トークン
     * @param tokenEnd      終了トークン
     * @param offset        検索開始位置
     * @param length        検索長
     * @param count         取り出し最大数
     * @return 開始トークンと終了トークンの範囲内にある文字列
     * @throws IllegalArgumentException 取り出し元文字列、開始トークン、終了トークンのいずれかが null の場合
     * @throws IllegalArgumentException 検索開始位置または検索長が 0 より小さい場合
     * @throws IllegalArgumentException 取り出し最大数が 0 以下の場合
     */
    public static String[] extractMultiTokenRange(
            final char[]    sourceChars,
            final char[]    tokenBegin,
            final char[]    tokenEnd,
            final int       offset,
            final int       length,
            final int       count
            ) {

        // 引数が不正の場合は例外
        if ((sourceChars == null)
            || (tokenBegin == null)
            || (tokenEnd == null)
            || (offset < 0)
            || (length < 0)
            || (count <= 0)
            ) {

            throw new IllegalArgumentException();

        }


        final ArrayList<String> retRangeStrings = new ArrayList<String>();  // 返却する範囲内文字列一覧
        int                     indexBeginToken = 0;                        // 開始トークン位置
        int                     indexEndToken   = -1;                       // 終了トークン位置


        // 開始トークンと終了トークンに囲まれた文字列を検索する
        for (int i = 0; i < count; i++) {

            // 開始トークン位置を検索する
            indexBeginToken = indexOf(
                                sourceChars,
                                offset,
                                length,
                                tokenBegin,
                                0,
                                tokenBegin.length,
                                indexBeginToken
                                );

            // 開始トークンが見つからない場合
            if (indexBeginToken == -1) {

                // ループ終了
                break;

            }

            // 終了トークン位置を検索する
            indexEndToken   = indexOf(
                                sourceChars,
                                offset,
                                length,
                                tokenEnd,
                                0,
                                tokenEnd.length,
                                indexBeginToken + tokenBegin.length
                                );

            // 終了トークンが見つかった場合
            if (indexEndToken != -1) {

                final int       indexExtractBegin   = indexBeginToken + tokenBegin.length;          // 取り出し開始位置
                final char[]    retChars            = new char[indexEndToken - indexExtractBegin];  // 取り出し文字列コピー先配列

                // 開始トークンと終了トークンの間の文字列をコピーする
                System.arraycopy(sourceChars, indexExtractBegin, retChars, 0, retChars.length);

                // 文字列を作成して返却一覧へ追加する
                retRangeStrings.add(new String(retChars));

                // 開始トークン位置を更新する
                indexBeginToken = indexEndToken + tokenEnd.length;

            } else {

                // ループ終了
                break;

            }

        }


        // 範囲内文字列一覧を返却する
        return retRangeStrings.toArray(new String[retRangeStrings.size()]);

    }


    /**
     * 指定ファイルパスからカレントディレクトリまでのパスを取得する。
     *
     * @param path  ファイルパス
     * @return カレントディレクトリまでのパス
     * @throws IllegalArgumentException ファイルパスが null の場合
     */
    public static String getCurrentDirectory(
            final String    path
            ) {

        // nullの場合は例外
        if (path == null) {

            throw new IllegalArgumentException();

        }


        // 後ろから順に検索する
        for (int i = path.length() - 1; i >= 0; i--) {

            // \ か / がある場合
            if ((path.charAt(i) == '\\') || (path.charAt(i) == '/')) {

                // 先頭から現在位置までの文字列を返す
                return path.substring(0, i);

            }

        }

        // 見つからない場合は空文字を返す
        return "";

    }

}
