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

import android.app.AlarmManager;
import android.content.Context;
import android.net.Uri;
import frontier.app.FRIntent;
import frontier.app.FRNameValuePair;
import frontier.app.FRPendingIntent;


/**
 * アラーム機能ユーティリティクラス。<br>
 * <br>
 * アラーム機能を簡潔に実現するためのユーティリティークラス。<br>
 *
 * @author Kou
 *
 */
public final class AlarmUtils {


    /**
     * アラーム種別 : 時刻指定 (スリープ時も起動)
     */
    public static final int RTC_WAKEUP                  =
        AlarmManager.RTC_WAKEUP;

    /**
     * アラーム種別 : 時刻指定 (スリープ時は起動しない)
     */
    public static final int RTC                         =
        AlarmManager.RTC;

    /**
     * アラーム種別 : 起動からの時間指定 (スリープ時も起動)
     */
    public static final int ELAPSED_REALTIME_WAKEUP     =
        AlarmManager.ELAPSED_REALTIME_WAKEUP;

    /**
     * アラーム種別 : 起動からの時間指定 (スリープ時は起動しない)
     */
    public static final int ELAPSED_REALTIME            =
        AlarmManager.ELAPSED_REALTIME;

    /**
     * 不正確タイマのインターバル時間 : 15分
     */
    public static final long INTERVAL_FIFTEEN_MINUTES   =
        AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    /**
     * 不正確タイマのインターバル時間 : 30分
     */
    public static final long INTERVAL_HALF_HOUR         =
        AlarmManager.INTERVAL_HALF_HOUR;

    /**
     * 不正確タイマのインターバル時間 : 1時間
     */
    public static final long INTERVAL_HOUR              =
        AlarmManager.INTERVAL_HOUR;

    /**
     * 不正確タイマのインターバル時間 : 12時間
     */
    public static final long INTERVAL_HALF_DAY          =
        AlarmManager.INTERVAL_HALF_DAY;

    /**
     * 不正確タイマのインターバル時間 : 24時間
     */
    public static final long INTERVAL_DAY               =
        AlarmManager.INTERVAL_DAY;




    /**
     * インスタンス生成防止。
     *
     */
    private AlarmUtils() {

        // 処理なし

    }


    /**
     * タイムゾーンを設定する。<br>
     * <br>
     * 本メソッドを利用する場合は<br>
     * AndroidManifestに対して以下のパーミッションを追加する必要がある。<br>
     * <br>
     * <pre>
     * android.permission.SET_TIME_ZONE
     * </pre>
     *
     * @param context   利用するコンテキスト
     * @param timeZone  設定するタイムゾーン
     */
    public static void setTimeZone(
            final Context   context,
            final String    timeZone
            ) {

        // タイムゾーンが null の場合は例外
        if (timeZone == null) {

            throw new IllegalArgumentException();

        }

        // タイムゾーンを変更する
        getAlarmManager(context).setTimeZone(timeZone);

    }


    /**
     * コンテキストからアラームマネージャーを取得する。
     *
     * @param context 利用するコンテキスト
     * @return アラームマネージャー
     */
    private static AlarmManager getAlarmManager(
            final Context   context
            ) {

        return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

    }


    /**
     * 対象ペンディング種別のアラームをキャンセルする。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destParams        起動インテントパラメータ
     */
    public static void cancel(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final Class<?>                      destClass,
            final String                        destAction,
            final FRNameValuePair...            destParams
            ) {

        cancel(
                pendingType,
                context,
                destClass,
                destAction,
                null,
                null,
                null,
                destParams
                );

    }


    /**
     * 対象ペンディング種別のアラームをキャンセルする。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destFlag          起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void cancel(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final Class<?>                      destClass,
            final String                        destAction,
            final int                           destFlag,
            final FRNameValuePair...            destParams
            ) {

        cancel(
                pendingType,
                context,
                destClass,
                destAction,
                null,
                null,
                new int[] {destFlag},
                destParams
                );

    }


    /**
     * 対象ペンディング種別のアラームをキャンセルする。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destFlags         起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void cancel(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final Class<?>                      destClass,
            final String                        destAction,
            final int[]                         destFlags,
            final FRNameValuePair...            destParams
            ) {

        cancel(
                pendingType,
                context,
                destClass,
                destAction,
                null,
                null,
                destFlags,
                destParams
                );

    }


    /**
     * アクティビティ対象アラームをキャンセルする。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destType          起動インテントタイプ
     * @param destData          起動インテントデータ
     * @param destFlags         起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void cancel(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final Class<?>                      destClass,
            final String                        destAction,
            final String                        destType,
            final Uri                           destData,
            final int[]                         destFlags,
            final FRNameValuePair...            destParams
            ) {

        // 引数が不正の場合は例外
        if ((pendingType == null) || (context == null) || (destClass == null)) {

            throw new IllegalArgumentException();

        }

        // アラームをキャンセルする
        getAlarmManager(context).cancel(
                FRPendingIntent.createPendingIntent(
                        context,
                        pendingType,
                        FRIntent.createIntent(
                                context,
                                destClass,
                                destAction,
                                destType,
                                destData,
                                destFlags,
                                destParams
                                )
                        )
                );

    }


    /**
     * ワンショットアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destParams        起動インテントパラメータ
     */
    public static void set(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final Class<?>                      destClass,
            final String                        destAction,
            final FRNameValuePair...            destParams
            ) {

        set(
                pendingType,
                context,
                type,
                triggerAtTime,
                destClass,
                destAction,
                null,
                null,
                null,
                destParams
                );

    }


    /**
     * ワンショットアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destFlag          起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void set(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final Class<?>                      destClass,
            final String                        destAction,
            final int                           destFlag,
            final FRNameValuePair...            destParams
            ) {

        set(
                pendingType,
                context,
                type,
                triggerAtTime,
                destClass,
                destAction,
                null,
                null,
                new int[] {destFlag},
                destParams
                );

    }


    /**
     * ワンショットアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destFlags         起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void set(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final Class<?>                      destClass,
            final String                        destAction,
            final int[]                         destFlags,
            final FRNameValuePair...            destParams
            ) {

        set(
                pendingType,
                context,
                type,
                triggerAtTime,
                destClass,
                destAction,
                null,
                null,
                destFlags,
                destParams
                );

    }


    /**
     * ワンショットアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destType          起動インテントタイプ
     * @param destData          起動インテントデータ
     * @param destFlags         起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void set(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final Class<?>                      destClass,
            final String                        destAction,
            final String                        destType,
            final Uri                           destData,
            final int[]                         destFlags,
            final FRNameValuePair...            destParams
            ) {

        // 引数が不正の場合は例外
        if ((pendingType == null) || (context == null) || (destClass == null)) {

            throw new IllegalArgumentException();

        }

        // ワンショットアラームを設定する
        getAlarmManager(context).set(
                type,
                triggerAtTime,
                FRPendingIntent.createPendingIntent(
                        context,
                        pendingType,
                        FRIntent.createIntent(
                                context,
                                destClass,
                                destAction,
                                destType,
                                destData,
                                destFlags,
                                destParams
                                )
                        )
                );

    }


    /**
     * 繰り返しアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param interval          アラームの間隔時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destParams        起動インテントパラメータ
     */
    public static void setRepeating(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final long                          interval,
            final Class<?>                      destClass,
            final String                        destAction,
            final FRNameValuePair...            destParams
            ) {

        setRepeating(
                pendingType,
                context,
                type,
                triggerAtTime,
                interval,
                destClass,
                destAction,
                null,
                null,
                null,
                destParams
                );

    }


    /**
     * 繰り返しアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param interval          アラームの間隔時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destFlag          起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void setRepeating(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final long                          interval,
            final Class<?>                      destClass,
            final String                        destAction,
            final int                           destFlag,
            final FRNameValuePair...            destParams
            ) {

        setRepeating(
                pendingType,
                context,
                type,
                triggerAtTime,
                interval,
                destClass,
                destAction,
                null,
                null,
                new int[] {destFlag},
                destParams
                );

    }


    /**
     * 繰り返しアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param interval          アラームの間隔時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destFlags         起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void setRepeating(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final long                          interval,
            final Class<?>                      destClass,
            final String                        destAction,
            final int[]                         destFlags,
            final FRNameValuePair...            destParams
            ) {

        setRepeating(
                pendingType,
                context,
                type,
                triggerAtTime,
                interval,
                destClass,
                destAction,
                null,
                null,
                destFlags,
                destParams
                );

    }


    /**
     * 繰り返しアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param interval          アラームの間隔時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destType          起動インテントタイプ
     * @param destData          起動インテントデータ
     * @param destFlags         起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void setRepeating(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final long                          interval,
            final Class<?>                      destClass,
            final String                        destAction,
            final String                        destType,
            final Uri                           destData,
            final int[]                         destFlags,
            final FRNameValuePair...            destParams
            ) {

        // 引数が不正の場合は例外
        if ((pendingType == null) || (context == null) || (destClass == null)) {

            throw new IllegalArgumentException();

        }

        // 繰り返しアラームを設定する
        getAlarmManager(context).setRepeating(
                type,
                triggerAtTime,
                interval,
                FRPendingIntent.createPendingIntent(
                        context,
                        pendingType,
                        FRIntent.createIntent(
                                context,
                                destClass,
                                destAction,
                                destType,
                                destData,
                                destFlags,
                                destParams
                                )
                        )
                );

    }


    /**
     * 不正確繰り返しアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param interval          アラームの間隔時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destParams        起動インテントパラメータ
     */
    public static void setInexactRepeatingActivity(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final long                          interval,
            final Class<?>                      destClass,
            final String                        destAction,
            final FRNameValuePair...            destParams
            ) {

        setInexactRepeatingActivity(
                pendingType,
                context,
                type,
                triggerAtTime,
                interval,
                destClass,
                destAction,
                null,
                null,
                null,
                destParams
                );

    }


    /**
     * 不正確繰り返しアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param interval          アラームの間隔時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destFlag          起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void setInexactRepeatingActivity(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final long                          interval,
            final Class<?>                      destClass,
            final String                        destAction,
            final int                           destFlag,
            final FRNameValuePair...            destParams
            ) {

        setInexactRepeatingActivity(
                pendingType,
                context,
                type,
                triggerAtTime,
                interval,
                destClass,
                destAction,
                null,
                null,
                new int[] {destFlag},
                destParams
                );

    }


    /**
     * 不正確繰り返しアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param interval          アラームの間隔時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destFlags         起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void setInexactRepeatingActivity(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final long                          interval,
            final Class<?>                      destClass,
            final String                        destAction,
            final int[]                         destFlags,
            final FRNameValuePair...            destParams
            ) {

        setInexactRepeatingActivity(
                pendingType,
                context,
                type,
                triggerAtTime,
                interval,
                destClass,
                destAction,
                null,
                null,
                destFlags,
                destParams
                );

    }


    /**
     * 不正確繰り返しアラームを対象ペンディング種別に対して設定する。
     *
     * @param pendingType       対象ペンディング種別
     * @param context           利用するコンテキスト
     * @param type              アラームの時間種別
     * @param triggerAtTime     アラームの基準時間
     * @param interval          アラームの間隔時間
     * @param destClass         起動するアクティビティ
     * @param destAction        起動インテントアクション
     * @param destType          起動インテントタイプ
     * @param destData          起動インテントデータ
     * @param destFlags         起動インテントフラグ
     * @param destParams        起動インテントパラメータ
     */
    public static void setInexactRepeatingActivity(
            final FRPendingIntent.PendingType   pendingType,
            final Context                       context,
            final int                           type,
            final long                          triggerAtTime,
            final long                          interval,
            final Class<?>                      destClass,
            final String                        destAction,
            final String                        destType,
            final Uri                           destData,
            final int[]                         destFlags,
            final FRNameValuePair...            destParams
            ) {

        // 引数が不正の場合は例外
        if ((pendingType == null) || (context == null) || (destClass == null)) {

            throw new IllegalArgumentException();

        }

        // 不正確繰り返しアラームを設定する
        getAlarmManager(context).setInexactRepeating(
                type,
                triggerAtTime,
                interval,
                FRPendingIntent.createPendingIntent(
                        context,
                        pendingType,
                        FRIntent.createIntent(
                                context,
                                destClass,
                                destAction,
                                destType,
                                destData,
                                destFlags,
                                destParams
                                )
                        )
                );

    }

}
