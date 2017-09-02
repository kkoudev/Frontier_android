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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


/**
 * 日付操作ユーティリティクラス。
 *
 * @author Kou
 *
 */
public final class DateUtils {



    /**
     * 日付操作種別 : 年
     */
    public static final int                 YEAR                = Calendar.YEAR;

    /**
     * 日付操作種別 : 月
     */
    public static final int                 MONTH               = Calendar.MONTH;

    /**
     * 日付操作種別 : 日
     */
    public static final int                 DATE                = Calendar.DATE;

    /**
     * 日付操作種別 : 曜日
     */
    public static final int                 DAY_OF_WEEK         = Calendar.DAY_OF_WEEK;

    /**
     * 日付操作種別 : 時 (12時間制)
     */
    public static final int                 HOUR                = Calendar.HOUR;

    /**
     * 日付操作種別 : 時 (24時間制)
     */
    public static final int                 HOUR_OF_DAY         = Calendar.HOUR_OF_DAY;

    /**
     * 日付操作種別 : 分
     */
    public static final int                 MINUTE              = Calendar.MINUTE;

    /**
     * 日付操作種別 : 秒
     */
    public static final int                 SECOND              = Calendar.SECOND;

    /**
     * 日付操作種別 : ミリ秒
     */
    public static final int                 MILLISECOND         = Calendar.MILLISECOND;




    /**
     * 曜日 : 日曜日
     */
    public static final int                 SUNDAY              = Calendar.SUNDAY;

    /**
     * 曜日 : 月曜日
     */
    public static final int                 MONDAY              = Calendar.MONDAY;

    /**
     * 曜日 : 火曜日
     */
    public static final int                 TUESDAY             = Calendar.TUESDAY;

    /**
     * 曜日 : 水曜日
     */
    public static final int                 WEDNESDAY           = Calendar.WEDNESDAY;

    /**
     * 曜日 : 木曜日
     */
    public static final int                 THURSDAY            = Calendar.THURSDAY;

    /**
     * 曜日 : 金曜日
     */
    public static final int                 FRIDAY              = Calendar.FRIDAY;

    /**
     * 曜日 : 土曜日
     */
    public static final int                 SATURDAY            = Calendar.SATURDAY;




    /**
     * 1秒におけるミリ秒数
     */
    public static final long                MILLIS_PER_SECOND   = 1000L;

    /**
     * 1分におけるミリ秒数
     */
    public static final long                MILLIS_PER_MINUTE   = 60000L;

    /**
     * 1時間におけるミリ秒数
     */
    public static final long                MILLIS_PER_HOUR     = 3600000L;

    /**
     * 1日におけるミリ秒数
     */
    public static final long                MILLIS_PER_DAY      = 86400000L;

    /**
     * 1週間におけるミリ秒数
     */
    public static final long                MILLIS_PER_WEEK     = 604800000L;

    /**
     * 1年におけるミリ秒数
     */
    public static final long                MILLIS_PER_YEAR     = 31449600000L;

    /**
     * 1年における月数
     */
    public static final int                 MONTHS_PER_YEAR     = 12;

    /**
     * 1週間における日数
     */
    public static final int                 DAYS_PER_WEEK       = 7;




    /**
     * 週の開始曜日
     */
    private static int                      dateStartDayOfWeek  = SUNDAY;




    /**
     * インスタンス生成防止。
     *
     */
    private DateUtils() {

        // 処理なし

    }


    /**
     * 指定日付に対して指定種別の値を加算する。
     *
     * @param date  加算先日付データ
     * @param type  加算する種別
     * @param value 加算する値
     * @return 加算後の日付データ
     */
    public static Date addDate(
            final Date  date,
            final int   type,
            final int   value
            ) {

        // 引数が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }


        final Calendar    cal = Calendar.getInstance();   // 日付操作用インスタンス

        // 日付データを設定する
        cal.setTime(date);

        // 指定種別で値を加算する
        cal.set(type, cal.get(type) + value);

        // 日付データを返す
        return cal.getTime();

    }


    /**
     * 指定日付に対して指定種別の値を加算する。
     *
     * @param date  加算先日付データ
     * @param type  加算する種別
     * @param value 加算する値
     * @return 加算後の日付データ
     */
    public static long addDate(
            final long  date,
            final int   type,
            final int   value
            ) {

        return addDate(new Date(date), type, value).getTime();

    }


    /**
     * 指定日付の指定種別の値を取得する。
     *
     * @param type  取得種別
     * @param date  取得先日付データ
     * @return 指定日付の指定種別の値
     */
    public static int getDate(
            final int   type,
            final Date  date
            ) {

        // 引数が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }


        final Calendar    cal = Calendar.getInstance();   // 日付操作用インスタンス

        // 日付データを設定する
        cal.setTime(date);

        // 指定種別の値を返す
        return cal.get(type);

    }


    /**
     * 指定日付の指定種別の値を取得する。
     *
     * @param date  取得先日付データ
     * @param type  取得種別
     * @return 指定日付の指定種別の値
     */
    public static int getDate(
            final int   type,
            final long  date
            ) {

        return getDate(type, new Date(date));

    }


    /**
     * 指定年月の日数を取得する。
     *
     * @param year  指定する年
     * @param month 指定する月 (1 - 12)
     * @return 指定された年月の日数
     */
    public static int getDateCount(
            final int   year,
            final int   month
            ) {

        final Calendar    cal = Calendar.getInstance(); // 日付操作用インスンタンス

        // 指定された年月を設定する
        // (月は日付日数取得用に次月を指定するためそのまま指定する)
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // 指定年月の日数を返す
        return cal.get(Calendar.DATE);

    }


    /**
     * 指定された日付が等しいかどうかを比較する。<br>
     * <br>
     * 指定された日付データの日付部分のみを比較して<br>
     * 一致するかどうかを返す。<br>
     *
     * @param date1     比較する日付1
     * @param date2     比較する日付2
     * @return 指定された日付が等しい場合は true
     */
    public static boolean equalsDate(
            final long  date1,
            final long  date2
            ) {

        return equalsDate(new Date(date1), new Date(date2));

    }


    /**
     * 指定された日付が等しいかどうかを比較する。<br>
     * <br>
     * 指定された日付データの日付部分のみを比較して<br>
     * 一致するかどうかを返す。<br>
     *
     * @param date1     比較する日付1
     * @param date2     比較する日付2
     * @return 指定された日付が等しい場合は true
     */
    public static boolean equalsDate(
            final Date  date1,
            final Date  date2
            ) {

        // 引数が null の場合は例外
        if ((date1 == null) || (date2 == null)) {

            throw new IllegalArgumentException();

        }

        // 比較用に日付を作成する
        final Calendar  localDate1 = Calendar.getInstance();
        final Calendar  localDate2 = Calendar.getInstance();

        // 日付を設定する
        localDate1.setTime(date1);
        localDate2.setTime(date2);

        // 時間をクリアする
        clearTime(localDate1);
        clearTime(localDate2);

        // 等しいかどうか比較する
        return localDate1.getTimeInMillis() == localDate2.getTimeInMillis();

    }


    /**
     * 週の開始曜日を設定する。<br>
     * <br>
     * デフォルトは ISO8601 に準じて月曜日を開始曜日としている。<br>
     *
     * @param startDayOfWeek 設定する週の開始曜日
     */
    public static void setFirstDayOfWeek(
            final int   startDayOfWeek
            ) {

        // 曜日種別が不正の場合は例外
        if ((startDayOfWeek < SUNDAY) || (SATURDAY < startDayOfWeek)) {

            throw new IllegalArgumentException("曜日種別が不正です [type = " + startDayOfWeek + "]");

        }

        // 指定された曜日を保持する
        dateStartDayOfWeek = startDayOfWeek;

    }


    /**
     * カレンダーに設定された時間をクリアする。
     *
     * @param cal 時間をクリアするカレンダー
     */
    private static void clearTime(
            final Calendar  cal
            ) {

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

    }


    /**
     * 指定日を含む週の開始日を取得する。
     *
     * @param date      日付
     * @return 指定日を含む週の開始日
     */
    public static Date getFirstDateOfWeek(
            final Date  date
            ) {

        // 日付が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }


        final int       startWeek   = dateStartDayOfWeek;            // 週の開始曜日
        final Calendar  retCal      = Calendar.getInstance();   // 返却日付


        // 日付を設定する
        retCal.setTime(date);

        // 開始曜日と指定日の曜日の差分を日数に加算する
        retCal.set(
                Calendar.DATE,
                retCal.get(Calendar.DATE)
                + (startWeek - retCal.get(Calendar.DAY_OF_WEEK))
                );

        // 指定日付よりも大きい場合
        if (retCal.getTime().getTime() > date.getTime()) {

            // 一週間分の日数を減算する
            retCal.set(Calendar.DATE, retCal.get(Calendar.DATE) - DAYS_PER_WEEK);

        }


        // 時間をクリアする
        clearTime(retCal);


        // 指定日を含む週の終了日を返却する
        return retCal.getTime();

    }


    /**
     * 指定日を含む週の開始日を取得する。
     *
     * @param date      日付
     * @return 指定日を含む週の開始日
     */
    public static long getFirstDateOfWeek(
            final long  date
            ) {

        return getFirstDateOfWeek(new Date(date)).getTime();

    }


    /**
     * 指定日を含む週の終了日を取得する。
     *
     * @param date      日付
     * @return 指定日を含む週の終了日
     */
    public static Date getLastDateOfWeek(
            final Date  date
            ) {

        // 日付が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }


        final int       endWeek;                            // 週の終了曜日
        final Calendar  retCal = Calendar.getInstance();    // 返却日付


        // 週の終了曜日を算出する
        endWeek = ((dateStartDayOfWeek + DAYS_PER_WEEK - 1) % DAYS_PER_WEEK);

        // 日付を設定する
        retCal.setTime(date);

        // 終了曜日と指定日の曜日の差分を日数に加算する
        retCal.set(
                Calendar.DATE,
                retCal.get(Calendar.DATE)
                + (endWeek - retCal.get(Calendar.DAY_OF_WEEK))
                );

        // 指定日付よりも小さい場合
        if (retCal.getTime().getTime() < date.getTime()) {

            // 一週間分の日数を加算する
            retCal.set(Calendar.DATE, retCal.get(Calendar.DATE) + DAYS_PER_WEEK);

        }


        // 時間をクリアする
        clearTime(retCal);


        // 指定日を含む週の終了日を返却する
        return retCal.getTime();

    }


    /**
     * 指定日を含む週の終了日を取得する。
     *
     * @param date      日付
     * @return 指定日を含む週の終了日
     */
    public static long getLastDateOfWeek(
            final long  date
            ) {

        return getLastDateOfWeek(new Date(date)).getTime();

    }


    /**
     * 指定日を含む月の開始日を取得する。
     *
     * @param date      日付
     * @return 指定日を含む月の開始日
     */
    public static Date getFirstDateOfMonth(
            final Date  date
            ) {

        // 日付が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }


        final Calendar  retCal = Calendar.getInstance();    // 返却日付

        // 日付を設定する
        retCal.setTime(date);

        // 月の開始日に設定する
        retCal.set(Calendar.DATE, 1);

        // 時間をクリアする
        clearTime(retCal);


        // 日付を返却する
        return retCal.getTime();

    }


    /**
     * 指定日を含む月の開始日を取得する。
     *
     * @param date      日付
     * @return 指定日を含む月の開始日
     */
    public static long getFirstDateOfMonth(
            final long  date
            ) {

        return getFirstDateOfMonth(new Date(date)).getTime();

    }


    /**
     * 指定日を含む月の終了日を取得する。
     *
     * @param date      日付
     * @return 指定日を含む月の終了日
     */
    public static Date getLastDateOfMonth(
            final Date  date
            ) {

        // 日付が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }


        final Calendar  retCal = Calendar.getInstance();    // 返却日付

        // 日付を設定する
        retCal.setTime(date);

        // 月の終了日に設定する
        retCal.set(Calendar.MONTH, retCal.get(Calendar.MONTH) + 1);
        retCal.set(Calendar.DATE,  0);

        // 時間をクリアする
        clearTime(retCal);


        // 日付を返却する
        return retCal.getTime();

    }


    /**
     * 指定日を含む月の終了日を取得する。
     *
     * @param date      日付
     * @return 指定日を含む月の終了日
     */
    public static long getLastDateOfMonth(
            final long  date
            ) {

        return getLastDateOfMonth(new Date(date)).getTime();

    }


    /**
     * 指定された日付操作種別に対する2つの日付の差分値を取得する。<br>
     *
     * @param type          差分対象日付操作種別
     * @param baseDate      比較元の日付
     * @param targetDate    比較対象日付
     * @return 指定された日付操作種別に対する2つの日付の差分値。(baseDate - targetDate)を返す。
     */
    public static int compare(
            final int   type,
            final Date  baseDate,
            final Date  targetDate
            ) {

        // nullなら例外
        if ((baseDate == null) || (targetDate == null)) {

            throw new IllegalArgumentException();

        }


        final Calendar  baseCal;        // 比較元日付
        final Calendar  targetCal;      // 比較対象日付
        final long      divideValue;    // 除算する値


        // 種類別処理
        switch (type) {

        // 年
        case YEAR:

            // 比較元日付と比較対象日付を作成する
            baseCal     = Calendar.getInstance();
            targetCal   = Calendar.getInstance();
            baseCal.setTime(baseDate);
            targetCal.setTime(targetDate);

            // 年の差分を返す
            return baseCal.get(Calendar.YEAR) - targetCal.get(Calendar.YEAR);


        // 月
        case MONTH:

            // 比較元日付と比較対象日付を作成する
            baseCal     = Calendar.getInstance();
            targetCal   = Calendar.getInstance();
            baseCal.setTime(baseDate);
            targetCal.setTime(targetDate);

            // 月の差分を返す
            return (baseCal.get(Calendar.YEAR) * MONTHS_PER_YEAR + (baseCal.get(Calendar.MONTH) + 1))
                   - (targetCal.get(Calendar.YEAR) * MONTHS_PER_YEAR + (targetCal.get(Calendar.MONTH) + 1));


        // 日
        case DATE:

            // 日の算出用除算値を設定する
            divideValue = MILLIS_PER_DAY;
            break;


        // 時
        case HOUR:
        case HOUR_OF_DAY:

            // 時の算出用除算値を設定する
            divideValue = MILLIS_PER_HOUR;
            break;


        // 分
        case MINUTE:

            // 分の算出用除算値を設定する
            divideValue = MILLIS_PER_MINUTE;
            break;


        // 秒
        case SECOND:

            // 秒の算出用除算値を設定する
            divideValue = MILLIS_PER_SECOND;
            break;


        // ミリ秒
        case MILLISECOND:

            // ミリ秒の算出用除算値を設定する
            divideValue = 1L;
            break;


        // その他 (エラー)
        default:

            throw new IllegalArgumentException("指定された日付操作種別には対応していません [type = " + type + "]");

        }


        // 差分結果を返す
        return (int)((baseDate.getTime() - targetDate.getTime()) / divideValue);

    }


    /**
     * 指定された日付操作種別に対する2つの日付の差分値を取得する。<br>
     *
     * @param type          差分対象日付操作種別
     * @param baseDate      比較元の日付
     * @param targetDate    比較対象日付
     * @return 指定された日付操作種別に対する2つの日付の差分値。(baseDate - targetDate)を返す。
     */
    public static int compare(
            final int   type,
            final long  baseDate,
            final long  targetDate
            ) {

        return compare(type, new Date(baseDate), new Date(targetDate));

    }


    /**
     * 指定された日付の時間部分をクリアして取得する。
     *
     * @param date          クリアする日付
     * @return 指定された日付の時間部分をクリアした日付
     */
    public static Date getClearTimeDate(
            final Date      date
            ) {

        final Calendar  cal = Calendar.getInstance();   // 作業用カレンダー

        // 日付を設定する
        cal.setTime(date);

        // 時間部分をクリアする
        clearTime(cal);

        // 日付を返す
        return cal.getTime();

    }


    /**
     * 指定されたカレンダーをクリアする。
     *
     * @param cal           クリアするカレンダー
     * @param ignoreTypes   クリアしない日付項目
     */
    public static void clearCalendar(
            final Calendar  cal,
            final int...    ignoreTypes
            ) {

        // 引数が null の場合は例外
        if (cal == null) {

            throw new IllegalArgumentException();

        }


        final Set<Integer>      typesTable = new HashSet<Integer>();

        // 非クリア種別が指定されている場合
        if (ignoreTypes != null) {

            // 非クリア種別分繰り返す
            for (final int ignoreType : ignoreTypes) {

                // テーブルへ追加する
                typesTable.add(ignoreType);

            }

        }


        // クリア値を設定する
        final int   clearYear           = 1970;     // 年
        final int   clearMonth          = 0;        // 月
        final int   clearDate           = 1;        // 日
        final int   clearHour           = 0;        // 時
        final int   clearMinute         = 0;        // 分
        final int   clearSecond         = 0;        // 秒
        final int   clearMillisecond    = 0;        // ミリ秒


        // 年が非クリア対象でない場合
        if (!typesTable.contains(Calendar.YEAR)) {

            // 年をクリアする
            cal.set(Calendar.YEAR, clearYear);

        }

        // 月が非クリア対象でない場合
        if (!typesTable.contains(Calendar.MONTH)) {

            // 月をクリアする
            cal.set(Calendar.MONTH, clearMonth);

        }

        // 日が非クリア対象でない場合
        if (!typesTable.contains(Calendar.DATE)) {

            // 日をクリアする
            cal.set(Calendar.DATE, clearDate);

        }

        // 時間が非クリア対象でない場合
        if (!typesTable.contains(Calendar.HOUR_OF_DAY)) {

            // 時間をクリアする
            cal.set(Calendar.HOUR_OF_DAY, clearHour);

        }

        // 分が非クリア対象でない場合
        if (!typesTable.contains(Calendar.MINUTE)) {

            // 分をクリアする
            cal.set(Calendar.MINUTE, clearMinute);

        }

        // 秒が非クリア対象でない場合
        if (!typesTable.contains(Calendar.SECOND)) {

            // 秒をクリアする
            cal.set(Calendar.SECOND, clearSecond);

        }

        // ミリ秒が非クリア対象でない場合
        if (!typesTable.contains(Calendar.MILLISECOND)) {

            // ミリ秒をクリアする
            cal.set(Calendar.MILLISECOND, clearMillisecond);

        }

    }


    /**
     * 指定フォーマットで指定日付データを表現した文字列を取得する。<br>
     * <br>
     * ロケールはデフォルトロケールが使用される。<br>
     *
     * @param format    フォーマット文字列
     * @param date      日時
     * @return 指定フォーマットで指定日付データを表現した文字列
     */
    public static String format(
            final String    format,
            final Date      date
            ) {

        // 引数が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }

        // 指定フォーマットの日付文字列を返す
        return format(format, date.getTime());

    }


    /**
     * 指定フォーマットで指定日付データを表現した文字列を取得する。<br>
     * <br>
     * ロケールはデフォルトロケールが使用される。<br>
     *
     * @param format    フォーマット文字列
     * @param date      日時
     * @return 指定フォーマットで指定日付データを表現した文字列
     */
    public static String format(
            final String    format,
            final long      date
            ) {

        // 指定フォーマットの日付文字列を返す
        return format(format, date, Locale.getDefault());

    }


    /**
     * 指定フォーマット、ロケールで指定日付データを表現した文字列を取得する。
     *
     * @param format    フォーマット文字列
     * @param date      日時
     * @param locale    ロケール
     * @return 指定フォーマットで指定日付データを表現した文字列
     */
    public static String format(
            final String    format,
            final Date      date,
            final Locale    locale
            ) {

        // 引数が null の場合は例外
        if (date == null) {

            throw new IllegalArgumentException();

        }

        // 指定フォーマットの日付文字列を返す
        return format(format, date.getTime(), locale);

    }


    /**
     * 指定フォーマットで指定日付データを表現した文字列を取得する。
     *
     * @param format    フォーマット文字列
     * @param time      日時
     * @param locale    ロケール
     * @return 指定フォーマットで指定日付データを表現した文字列
     */
    public static String format(
            final String    format,
            final long      time,
            final Locale    locale
            ) {

        // 引数が null の場合は例外
        if ((format == null) || (locale == null)) {

            throw new IllegalArgumentException();

        }

        try {

            // 指定フォーマットの日付文字列を返す
            return new SimpleDateFormat(format, locale).format(new Date(time));

        } catch (final IllegalArgumentException e) {

            // パターンが不正の場合は変換失敗
            return null;

        }

    }


    /**
     * 指定フォーマットで指定テキストをパースした日付を取得する。
     *
     * @param format    フォーマット文字列
     * @param text      パースする文字列
     * @return パースした日付。失敗時は null
     */
    public static Date parse(
            final String    format,
            final String    text
            ) {

        // 引数が null の場合は例外
        if ((format == null) || (text == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // パースした日付を返す
            return new SimpleDateFormat(format).parse(text);

        } catch (final ParseException e) {

            // 失敗
            return null;

        }

    }


}
