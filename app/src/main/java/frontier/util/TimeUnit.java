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
 * 時間単位を表す列挙型。<br>
 * <br>
 * Android標準は Java SE 5相当のため扱える単位が少ない。<br>
 * 本列挙型では Java SE 6相当の時間単位をサポートする。<br>
 *
 * @author Kou
 *
 */
public enum TimeUnit {


    /**
     * 時間単位 : ナノ秒
     */
    NANOSECONDS(0),

    /**
     * 時間単位 : マイクロ秒
     */
    MICROSECONDS(NANOSECONDS.timeUnit + 1),

    /**
     * 時間単位 : ミリ秒
     */
    MILLISECONDS(MICROSECONDS.timeUnit + 1),

    /**
     * 時間単位 : 秒
     */
    SECONDS(MILLISECONDS.timeUnit + 1),

    /**
     * 時間単位 : 分
     */
    MINUTES(SECONDS.timeUnit + 1),

    /**
     * 時間単位 : 時
     */
    HOURS(MINUTES.timeUnit + 1),

    /**
     * 時間単位 : 日
     */
    DAYS(HOURS.timeUnit + 1);




    /**
     * 変換規定値 : 0
     */
    private static final long C0 = 1L;

    /**
     * 変換規定値 : 1
     */
    private static final long C1 = C0 * 1000L;

    /**
     * 変換規定値 : 2
     */
    private static final long C2 = C1 * 1000L;

    /**
     * 変換規定値 : 3
     */
    private static final long C3 = C2 * 1000L;

    /**
     * 変換規定値 : 4
     */
    private static final long C4 = C3 * 60L;

    /**
     * 変換規定値 : 5
     */
    private static final long C5 = C4 * 60L;

    /**
     * 変換規定値 : 6
     */
    private static final long C6 = C5 * 24L;

    /**
     * 最大値
     */
    private static final long MAX = Long.MAX_VALUE;


    /**
     * 設定されている時間単位
     */
    private final int     timeUnit;



    /**
     * 時間単位を初期化する。
     *
     * @param typeUnit 時間単位種別
     */
    private TimeUnit(
            final int typeUnit
            ) {

        timeUnit = typeUnit;

    }



    /**
     * 指定期間をナノ秒へ変換する。
     *
     * @param duration ナノ秒へ変換する期間
     * @return ナノ秒へ変換された期間
     */
    public long toNanos(
            final long duration
            ) {

        // 単位がナノ秒の場合
        if (timeUnit == NANOSECONDS.timeUnit) {

            return duration;

        // 単位がマイクロ秒の場合
        } else if (timeUnit == MICROSECONDS.timeUnit) {

            return duration / (C1 / C0);

        // 単位がミリ秒の場合
        } else if (timeUnit == MILLISECONDS.timeUnit) {

            return duration / (C2 / C0);

        // 単位が秒の場合
        } else if (timeUnit == SECONDS.timeUnit) {

            return duration / (C3 / C0);

        // 単位が分の場合
        } else if (timeUnit == MINUTES.timeUnit) {

            return duration / (C4 / C0);

        // 単位が時の場合
        } else if (timeUnit == HOURS.timeUnit) {

            return duration / (C5 / C0);

        // 単位が日の場合
        } else if (timeUnit == DAYS.timeUnit) {

            return duration / (C6 / C0);

        // その他 (エラー)
        } else {

            throw new IllegalStateException();

        }

    }


    /**
     * 指定期間をマイクロ秒へ変換する。
     *
     * @param duration マイクロ秒へ変換する期間
     * @return マイクロ秒へ変換された期間
     */
    public long toMicro(
            final long duration
            ) {

        // 単位がナノ秒の場合
        if (timeUnit == NANOSECONDS.timeUnit) {

            return getSaturatedValue(duration, C1 / C0, MAX / (C1 / C0));

        // 単位がマイクロ秒の場合
        } else if (timeUnit == MICROSECONDS.timeUnit) {

            return duration;

        // 単位がミリ秒の場合
        } else if (timeUnit == MILLISECONDS.timeUnit) {

            return duration / (C2 / C1);

        // 単位が秒の場合
        } else if (timeUnit == SECONDS.timeUnit) {

            return duration / (C3 / C1);

        // 単位が分の場合
        } else if (timeUnit == MINUTES.timeUnit) {

            return duration / (C4 / C1);

        // 単位が時の場合
        } else if (timeUnit == HOURS.timeUnit) {

            return duration / (C5 / C1);

        // 単位が日の場合
        } else if (timeUnit == DAYS.timeUnit) {

            return duration / (C6 / C1);

        // その他 (エラー)
        } else {

            throw new IllegalStateException();

        }

    }


    /**
     * 指定期間をミリ秒へ変換する。
     *
     * @param duration ミリ秒へ変換する期間
     * @return ミリ秒へ変換された期間
     */
    public long toMillis(
            final long duration
            ) {

        // 単位がナノ秒の場合
        if (timeUnit == NANOSECONDS.timeUnit) {

            return getSaturatedValue(duration, C2 / C0, MAX / (C2 / C0));

        // 単位がマイクロ秒の場合
        } else if (timeUnit == MICROSECONDS.timeUnit) {

            return getSaturatedValue(duration, C2 / C1, MAX / (C2 / C1));

        // 単位がミリ秒の場合
        } else if (timeUnit == MILLISECONDS.timeUnit) {

            return duration;

        // 単位が秒の場合
        } else if (timeUnit == SECONDS.timeUnit) {

            return getSaturatedValue(duration, C3 / C2, MAX / (C3 / C2));

        // 単位が分の場合
        } else if (timeUnit == MINUTES.timeUnit) {

            return getSaturatedValue(duration, C4 / C2, MAX / (C4 / C2));

        // 単位が時の場合
        } else if (timeUnit == HOURS.timeUnit) {

            return getSaturatedValue(duration, C5 / C2, MAX / (C5 / C2));

        // 単位が日の場合
        } else if (timeUnit == DAYS.timeUnit) {

            return getSaturatedValue(duration, C6 / C2, MAX / (C6 / C2));

        // その他 (エラー)
        } else {

            throw new IllegalStateException();

        }

    }


    /**
     * 指定期間を秒へ変換する。
     *
     * @param duration 秒へ変換する期間
     * @return 秒へ変換された期間
     */
    public long toSeconds(
            final long duration
            ) {

        // 単位がナノ秒の場合
        if (timeUnit == NANOSECONDS.timeUnit) {

            return getSaturatedValue(duration, C3 / C0, MAX / (C3 / C0));

        // 単位がマイクロ秒の場合
        } else if (timeUnit == MICROSECONDS.timeUnit) {

            return getSaturatedValue(duration, C3 / C1, MAX / (C3 / C1));

        // 単位がミリ秒の場合
        } else if (timeUnit == MILLISECONDS.timeUnit) {

            return duration / (C3 / C2);

        // 単位が秒の場合
        } else if (timeUnit == SECONDS.timeUnit) {

            return duration;

        // 単位が分の場合
        } else if (timeUnit == MINUTES.timeUnit) {

            return getSaturatedValue(duration, C4 / C3, MAX / (C4 / C3));

        // 単位が時の場合
        } else if (timeUnit == HOURS.timeUnit) {

            return getSaturatedValue(duration, C5 / C3, MAX / (C5 / C3));

        // 単位が日の場合
        } else if (timeUnit == DAYS.timeUnit) {

            return getSaturatedValue(duration, C6 / C3, MAX / (C6 / C3));

        // その他 (エラー)
        } else {

            throw new IllegalStateException();

        }

    }


    /**
     * 指定期間を分へ変換する。
     *
     * @param duration 分へ変換する期間
     * @return 分へ変換された期間
     */
    public long toMinutes(
            final long duration
            ) {

        // 単位がナノ秒の場合
        if (timeUnit == NANOSECONDS.timeUnit) {

            return getSaturatedValue(duration, C4 / C0, MAX / (C4 / C0));

        // 単位がマイクロ秒の場合
        } else if (timeUnit == MICROSECONDS.timeUnit) {

            return getSaturatedValue(duration, C4 / C1, MAX / (C4 / C1));

        // 単位がミリ秒の場合
        } else if (timeUnit == MILLISECONDS.timeUnit) {

            return duration / (C4 / C2);

        // 単位が秒の場合
        } else if (timeUnit == SECONDS.timeUnit) {

            return duration / (C4 / C3);

        // 単位が分の場合
        } else if (timeUnit == MINUTES.timeUnit) {

            return duration;

        // 単位が時の場合
        } else if (timeUnit == HOURS.timeUnit) {

            return getSaturatedValue(duration, C5 / C4, MAX / (C5 / C4));

        // 単位が日の場合
        } else if (timeUnit == DAYS.timeUnit) {

            return getSaturatedValue(duration, C6 / C4, MAX / (C6 / C4));

        // その他 (エラー)
        } else {

            throw new IllegalStateException();

        }

    }


    /**
     * 指定期間を時間へ変換する。
     *
     * @param duration 時間へ変換する期間
     * @return 時間へ変換された期間
     */
    public long toHours(
            final long duration
            ) {

        // 単位がナノ秒の場合
        if (timeUnit == NANOSECONDS.timeUnit) {

            return getSaturatedValue(duration, C5 / C0, MAX / (C5 / C0));

        // 単位がマイクロ秒の場合
        } else if (timeUnit == MICROSECONDS.timeUnit) {

            return getSaturatedValue(duration, C5 / C1, MAX / (C5 / C1));

        // 単位がミリ秒の場合
        } else if (timeUnit == MILLISECONDS.timeUnit) {

            return duration / (C5 / C2);

        // 単位が秒の場合
        } else if (timeUnit == SECONDS.timeUnit) {

            return duration / (C5 / C3);

        // 単位が分の場合
        } else if (timeUnit == MINUTES.timeUnit) {

            return duration / (C5 / C4);

        // 単位が時の場合
        } else if (timeUnit == HOURS.timeUnit) {

            return duration;

        // 単位が日の場合
        } else if (timeUnit == DAYS.timeUnit) {

            return getSaturatedValue(duration, C6 / C5, MAX / (C6 / C5));

        // その他 (エラー)
        } else {

            throw new IllegalStateException();

        }

    }


    /**
     * 指定期間を日にちへ変換する。
     *
     * @param duration 日にちへ変換する期間
     * @return 日にちへ変換された期間
     */
    public long toDays(
            final long duration
            ) {

        // 単位がナノ秒の場合
        if (timeUnit == NANOSECONDS.timeUnit) {

            return getSaturatedValue(duration, C6 / C0, MAX / (C6 / C0));

        // 単位がマイクロ秒の場合
        } else if (timeUnit == MICROSECONDS.timeUnit) {

            return getSaturatedValue(duration, C6 / C1, MAX / (C6 / C1));

        // 単位がミリ秒の場合
        } else if (timeUnit == MILLISECONDS.timeUnit) {

            return duration / (C6 / C2);

        // 単位が秒の場合
        } else if (timeUnit == SECONDS.timeUnit) {

            return duration / (C6 / C3);

        // 単位が分の場合
        } else if (timeUnit == MINUTES.timeUnit) {

            return duration / (C6 / C4);

        // 単位が時の場合
        } else if (timeUnit == HOURS.timeUnit) {

            return duration / (C6 / C5);

        // 単位が日の場合
        } else if (timeUnit == DAYS.timeUnit) {

            return duration;

        // その他 (エラー)
        } else {

            throw new IllegalStateException();

        }

    }

    /**
     * 指定期間を指定単位へ変換する。
     *
     * @param duration  指定単位へ変換する期間
     * @param unit      変換する単位
     * @return 指定単位へ変換された期間
     * @throws IllegalArgumentException 単位が null の場合
     */
    public long convert(
            final long      duration,
            final TimeUnit  unit
            ) {

        // 単位が null の場合は例外
        if (unit == null) {

            throw new IllegalArgumentException();

        }


        // 単位がナノ秒の場合
        if (timeUnit == NANOSECONDS.timeUnit) {

            return unit.toNanos(duration);

        // 単位がマイクロ秒の場合
        } else if (timeUnit == MICROSECONDS.timeUnit) {

            return unit.toMicro(duration);

        // 単位がミリ秒の場合
        } else if (timeUnit == MILLISECONDS.timeUnit) {

            return unit.toMillis(duration);

        // 単位が秒の場合
        } else if (timeUnit == SECONDS.timeUnit) {

            return unit.toSeconds(duration);

        // 単位が分の場合
        } else if (timeUnit == MINUTES.timeUnit) {

            return unit.toMinutes(duration);

        // 単位が時の場合
        } else if (timeUnit == HOURS.timeUnit) {

            return unit.toHours(duration);

        // 単位が日の場合
        } else if (timeUnit == DAYS.timeUnit) {

            return unit.toDays(duration);

        // その他 (エラー)
        } else {

            throw new IllegalStateException();

        }

    }


    /**
     * 飽和処理した演算結果を取得する。
     *
     * @param duration  処理する期間
     * @param multiply  乗算する値
     * @param absLimit  上下限を表す絶対値
     * @return 飽和処理された演算結果
     */
    private static long getSaturatedValue(
            final long  duration,
            final long  multiply,
            final long  absLimit
            ) {

        // 上限値を上回る場合
        if (duration > absLimit) {

            // long最大値を設定
            return Long.MAX_VALUE;

        }

        // 下限値を下回る場合
        if (duration < -absLimit) {

            // long最小値を設定
            return Long.MIN_VALUE;

        }

        // 乗算した値を返す
        return duration * multiply;

    }


}
