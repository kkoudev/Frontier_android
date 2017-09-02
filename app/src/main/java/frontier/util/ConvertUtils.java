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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.net.Uri;


/**
 * 型変換ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class ConvertUtils {


    /**
     * 型変換処理一覧<br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>キー</td><td>Class</td><td>変換型を表すクラスオブジェクト</td>
     * </tr>
     * <tr>
     *   <td>値</td><td>ConvertProcessable</td><td>変換処理を定義したハンドラ</td>
     * </tr>
     * </table>
     */
    private static final Map<Class<?>, TypeConvertiable>          TYPE_CONVERSIONS   =
        new HashMap<Class<?>, ConvertUtils.TypeConvertiable>();



    /**
     * 各種初期化処理を行う
     */
    static {

        //----------------//
        // 変換処理の定義 //
        //----------------//

        // String型の処理を定義する
        TYPE_CONVERSIONS.put(String.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をStringに変換して返す
                return ConvertUtils.<T>cast(
                        ConvertUtils.toString(value)
                        );

            }

        });

        // Integer型の処理を定義する
        TYPE_CONVERSIONS.put(Integer.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 変換種類別処理
                switch (convertType) {

                // 標準変換
                case GENERAL:

                    // 値をIntegerに変換して返す
                    return ConvertUtils.<T>cast(
                            value == null ? null : ConvertUtils.toInt(value)
                            );


                // その他エラー
                default:

                    throw new IllegalStateException();

                }

            }

        });

        // int型の処理を定義する
        TYPE_CONVERSIONS.put(int.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 変換種類別処理
                switch (convertType) {

                // 標準変換
                case GENERAL:

                    // 値をIntegerに変換して返す
                    return ConvertUtils.<T>cast(
                            ConvertUtils.toInt(value)
                            );


                // その他エラー
                default:

                    throw new IllegalStateException();

                }

            }

        });

        // Long型の処理を定義する
        TYPE_CONVERSIONS.put(Long.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をLongに変換して返す
                return ConvertUtils.<T>cast(
                        value == null ? null : ConvertUtils.toLong(value)
                        );

            }

        });

        // long型の処理を定義する
        TYPE_CONVERSIONS.put(long.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をLongに変換して返す
                return ConvertUtils.<T>cast(
                        ConvertUtils.toLong(value)
                        );

            }

        });

        // Boolean型の処理を定義する
        TYPE_CONVERSIONS.put(Boolean.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 変換種類別処理
                switch (convertType) {

                // 標準変換
                case GENERAL:

                    // 値をBooleanに変換して返す
                    return ConvertUtils.<T>cast(
                            value == null ? null : ConvertUtils.toBoolean(value)
                            );


                // その他エラー
                default:

                    throw new IllegalStateException();

                }

            }

        });

        // boolean型の処理を定義する
        TYPE_CONVERSIONS.put(boolean.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 変換種類別処理
                switch (convertType) {

                // 標準変換
                case GENERAL:

                    // 値をBooleanに変換して返す
                    return ConvertUtils.<T>cast(
                            ConvertUtils.toBoolean(value)
                            );


                // その他エラー
                default:

                    throw new IllegalStateException();

                }

            }

        });

        // Float型の処理を定義する
        TYPE_CONVERSIONS.put(Float.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をFloatに変換して返す
                return ConvertUtils.<T>cast(
                        value == null ? null : ConvertUtils.toFloat(value)
                        );

            }

        });

        // float型の処理を定義する
        TYPE_CONVERSIONS.put(float.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をFloatに変換して返す
                return ConvertUtils.<T>cast(
                        ConvertUtils.toFloat(value)
                        );

            }

        });

        // Double型の処理を定義する
        TYPE_CONVERSIONS.put(Double.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をDoubleに変換して返す
                return ConvertUtils.<T>cast(
                        value == null ? null : ConvertUtils.toDouble(value)
                        );

            }

        });

        // double型の処理を定義する
        TYPE_CONVERSIONS.put(double.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をDoubleに変換して返す
                return ConvertUtils.<T>cast(
                        ConvertUtils.toDouble(value)
                        );

            }

        });

        // Byte型の処理を定義する
        TYPE_CONVERSIONS.put(Byte.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をByteに変換して返す
                return ConvertUtils.<T>cast(
                        value == null ? null : ConvertUtils.toByte(value)
                        );

            }

        });

        // byte型の処理を定義する
        TYPE_CONVERSIONS.put(byte.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をByteに変換して返す
                return ConvertUtils.<T>cast(
                        ConvertUtils.toByte(value)
                        );

            }

        });

        // Short型の処理を定義する
        TYPE_CONVERSIONS.put(Short.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をShortに変換して返す
                return ConvertUtils.<T>cast(
                        value == null ? null : ConvertUtils.toShort(value)
                        );

            }

        });

        // short型の処理を定義する
        TYPE_CONVERSIONS.put(short.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をShortに変換して返す
                return ConvertUtils.<T>cast(
                        ConvertUtils.toShort(value)
                        );

            }

        });

        // Date型の変換処理を定義する
        TYPE_CONVERSIONS.put(Date.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 値をDateに変換して返す
                return ConvertUtils.<T>cast(
                        ConvertUtils.toDate(value)
                        );

            }

        });

        // java.sql.Date型の変換処理を設定する
        TYPE_CONVERSIONS.put(java.sql.Date.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 日付型に変換する
                final Date  date = ConvertUtils.toDate(value);

                // 値をjava.sql.Dateに変換して返す
                return ConvertUtils.<T>cast(
                        date == null ? null : new java.sql.Date(date.getTime())
                        );

            }

        });

        // Timestamp型の変換処理を設定する
        TYPE_CONVERSIONS.put(Timestamp.class, new TypeConvertiable() {

            @Override
            public <T> T convertType(
                    final DataConvertType   convertType,
                    final Class<T>          resultType,
                    final Object            value
                    ) {

                // 日付型に変換する
                final Date  date = ConvertUtils.toDate(value);

                // 値をTimestampに変換して返す
                return ConvertUtils.<T>cast(
                        date == null ? null : new Timestamp(date.getTime())
                        );

            }

        });

    }




    /**
     * インスタンス生成防止。
     *
     */
    private ConvertUtils() {

        // 処理なし

    }


    /**
     * 指定されたインスタンスを代入先の型へ自動キャストする。
     *
     * @param <T>   キャストするインスタンスの型
     * @param value キャストするインスタンス
     * @return キャストしたインスタンス
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(
            final Object    value
            ) {

        return (T)value;

    }


    /**
     * Object 型を Boolean 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、false を返却する。
     *
     * @param value 変換対象値
     * @return 変換された値
     */
    public static Boolean toBoolean(
            final Object    value
            ) {

        return toBoolean(value, false);

    }


    /**
     * Object 型を Boolean 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、指定したデフォルト値を返却する。
     *
     * @param value         変換対象値
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換された値
     */
    public static Boolean toBoolean(
            final Object    value,
            final Boolean   defaultValue
            ) {

        // nullの場合
        if (value == null) {

            // デフォルト値を返却
            return defaultValue;

        }

        // boolean値を返す
        return String.valueOf(Boolean.TRUE).toLowerCase().equals(String.valueOf(value).toLowerCase());

    }


    /**
     * Object 型を Byte 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、0 を返却する。
     *
     * @param value 変換対象値
     * @return 変換された値
     */
    public static Byte toByte(
            final Object    value
            ) {

        return toByte(value, (byte)0);

    }


    /**
     * Object 型を Byte 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、指定したデフォルト値を返却する。
     *
     * @param value         変換対象値
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換された値
     */
    public static Byte toByte(
            final Object    value,
            final Byte      defaultValue
            ) {

        // nullの場合
        if (value == null) {

            // デフォルト値を返却
            return defaultValue;

        }


        try {

            // byte値を返す
            return Byte.parseByte(String.valueOf(value));

        } catch (final NumberFormatException e) {

            // デフォルト値を返却
            return defaultValue;

        }

    }


    /**
     * Object 型を short 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、0 を返却する。
     *
     * @param value 変換対象値
     * @return 変換された値
     */
    public static Short toShort(
            final Object    value
            ) {

        return toShort(value, (short)0);

    }


    /**
     * Object 型を short 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、指定したデフォルト値を返却する。
     *
     * @param value         変換対象値
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換された値
     */
    public static Short toShort(
            final Object    value,
            final Short     defaultValue
            ) {

        // nullの場合
        if (value == null) {

            // デフォルト値を返却
            return defaultValue;

        }


        try {

            // short値を返す
            return Short.parseShort(String.valueOf(value));

        } catch (final NumberFormatException e) {

            // デフォルト値を返却
            return defaultValue;

        }

    }


    /**
     * Object 型を Integer 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、0 を返却する。
     *
     * @param value 変換対象値
     * @return 変換された値
     */
    public static Integer toInt(
            final Object    value
            ) {

        return toInt(value, 0);

    }


    /**
     * Object 型を Integer 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、指定したデフォルト値を返却する。
     *
     * @param value         変換対象値
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換された値
     */
    public static int toInt(
            final Object    value,
            final Integer   defaultValue
            ) {

        // nullの場合
        if (value == null) {

            // デフォルト値を返却
            return defaultValue;

        }


        try {

            // int値を返す
            return Integer.parseInt(String.valueOf(value));

        } catch (final NumberFormatException e) {

            // デフォルト値を返却
            return defaultValue;

        }

    }


    /**
     * Object 型を Long 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、0 を返却する。
     *
     * @param value 変換対象値
     * @return 変換された値
     */
    public static Long toLong(
            final Object    value
            ) {

        return toLong(value, 0L);

    }


    /**
     * Object 型を Long 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、指定したデフォルト値を返却する。
     *
     * @param value         変換対象値
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換された値
     */
    public static Long toLong(
            final Object    value,
            final Long      defaultValue
            ) {

        // nullの場合
        if (value == null) {

            // デフォルト値を返却
            return defaultValue;

        }


        try {

            // long値を返す
            return Long.parseLong(String.valueOf(value));

        } catch (final NumberFormatException e) {

            // デフォルト値を返却
            return defaultValue;

        }

    }


    /**
     * Object 型を Float 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、0.0f を返却する。
     *
     * @param value 変換対象値
     * @return 変換された値
     */
    public static Float toFloat(
            final Object    value
            ) {

        return toFloat(value, 0.0f);

    }


    /**
     * Object 型を Float 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、指定したデフォルト値を返却する。
     *
     * @param value         変換対象値
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換された値
     */
    public static Float toFloat(
            final Object    value,
            final Float     defaultValue
            ) {

        // nullの場合
        if (value == null) {

            // デフォルト値を返却
            return defaultValue;

        }


        try {

            // float値を返す
            return Float.parseFloat(String.valueOf(value));

        } catch (final NumberFormatException e) {

            // デフォルト値を返却
            return defaultValue;

        }

    }


    /**
     * Object 型を Double 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、0.0 を返却する。
     *
     * @param value 変換対象値
     * @return 変換された値
     */
    public static Double toDouble(
            final Object    value
            ) {

        return toDouble(value, 0.0);

    }


    /**
     * Object 型を double 型へ変換する。<br>
     * <br>
     * 変換対象値が変換できなかった場合は、指定したデフォルト値を返却する。
     *
     * @param value         変換対象値
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換された値
     */
    public static Double toDouble(
            final Object    value,
            final Double    defaultValue
            ) {

        // nullの場合
        if (value == null) {

            // デフォルト値を返却
            return defaultValue;

        }


        try {

            // double値を返す
            return Double.parseDouble(String.valueOf(value));

        } catch (final NumberFormatException e) {

            // デフォルト値を返却
            return defaultValue;

        }

    }


    /**
     * Object型を文字列へ変換する。<br>
     * <br>
     * {@link String#valueOf(Object)}を利用した場合、
     * nullを変換すると、nullという文字列として返されてしまうため
     * 本メソッドでは、意図的に null 値を変換する場合は null を返す。
     *
     * @param value 変換する値
     * @return 変換後の文字列
     */
    public static String toString(
            final Object    value
            ) {

        return value == null ? null : String.valueOf(value);

    }


    /**
     * Object型を日付型へ変換する。<br>
     *
     * @param value 変換する値
     * @return 変換後の日付型。変換に失敗した場合は null
     */
    public static Date toDate(
            final Object    value
            ) {

        final String    strValue = String.valueOf(value);   // 値の文字列形式
        Date            date     = null;                    // 変換した日付

        // 整数値の場合
        if (Validator.isDigit(strValue)) {

            // Long型として日付データを作成する
            date = new Date(ConvertUtils.toLong(strValue));

        } else {

            // 日付変換パターン分処理をする
            for (final DateFormatType type : DateFormatType.values()) {

                try {

                    // 日付に変換する
                    date = new SimpleDateFormat(type.getFormat()).parse(strValue);

                    // 日付変換に成功
                    break;

                } catch (final ParseException e) {

                    // 次のフォーマットへ
                    continue;

                }

            }

        }

        // 日付が取得できた場合
        if (date != null) {

            // 変換した日付を返す
            return date;

        } else {

            // nullを返す
            return null;

        }

    }


    /**
     * 指定されたオブジェクト値を指定型へ変換する。
     *
     * @param <T>           変換型
     * @param convertType   変換種別
     * @param resultType    変換後の型
     * @param value         変換する値
     * @return 変換した値
     */
    public static <T> T toType(
            final DataConvertType   convertType,
            final Class<T>          resultType,
            final Object            value
            ) {

        // 指定型の変換処理を取得する
        final TypeConvertiable   processable = TYPE_CONVERSIONS.get(resultType);

        // 変換処理がない場合
        if (processable == null) {

            // nullを返す
            return null;

        }

        // 値を変換して返す
        return processable.convertType(convertType, resultType, value);

    }


    /**
     * 日付を指定されたフォーマットの文字列へ変換する。
     *
     * @param type  変換する日付フォーマット種別
     * @param date  変換する日付
     * @return 変換後の文字列
     */
    public static String formatDate(
            final DateFormatType    type,
            final Date              date
            ) {

        // 引数が null の場合は例外
        if ((type == null) || (date == null)) {

            throw new IllegalArgumentException();

        }

        // 日付を指定種別に変換する
        return new SimpleDateFormat(type.getFormat()).format(date);

    }


    /**
     * 日付を指定されたフォーマットの文字列へ変換する。
     *
     * @param type  変換する日付フォーマット種別
     * @param date  変換する日付
     * @return 変換後の文字列
     */
    public static String formatDate(
            final DateFormatType    type,
            final long              date
            ) {

        return formatDate(type, new Date(date));

    }


    /**
     * 指定されたクラスが変換可能型であるかどうかを取得する。<br>
     * <br>
     * {@link #toType(DataConvertType, Class, Object)}メソッドで変換をサポートしている型かどうかを返す。<br>
     *
     * @param typeClass 変換可能型かどうかを調べるクラス
     * @return 変換可能型の場合は true
     */
    public static boolean canConvertType(
            final Class<?>  typeClass
            ) {

        // nullの場合は例外
        if (typeClass == null) {

            throw new IllegalArgumentException();

        }

        // 変換可能かどうかを返す
        return TYPE_CONVERSIONS.containsKey(typeClass);

    }


    /**
     * Boolean型配列をboolean型配列へ変換する。
     *
     * @param values        変換するBoolean型配列
     * @return 変換したboolean型配列
     */
    public static boolean[] toPrimitiveArray(
            final Boolean[]       values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, false);

    }


    /**
     * Boolean型配列をboolean型配列へ変換する。
     *
     * @param values        変換するBoolean型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したint型配列
     */
    public static boolean[] toPrimitiveArray(
            final Boolean[]       values,
            final boolean         defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new boolean[0];

        }


        final boolean[]     retValues = new boolean[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final Boolean   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value.booleanValue());

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * Byte型配列をbyte型配列へ変換する。
     *
     * @param values        変換するByte型配列
     * @return 変換したbyte型配列
     */
    public static byte[] toPrimitiveArray(
            final Byte[]       values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, (byte)0);

    }


    /**
     * Byte型配列をbyte型配列へ変換する。
     *
     * @param values        変換するByte型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したbyte型配列
     */
    public static byte[] toPrimitiveArray(
            final Byte[]       values,
            final byte         defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new byte[0];

        }


        final byte[]     retValues = new byte[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final Byte   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value.byteValue());

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * Character型配列をchar型配列へ変換する。
     *
     * @param values        変換するCharacter型配列
     * @return 変換したchar型配列
     */
    public static char[] toPrimitiveArray(
            final Character[]       values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, (char)0);

    }


    /**
     * Character型配列をchar型配列へ変換する。
     *
     * @param values        変換するCharacter型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したchar型配列
     */
    public static char[] toPrimitiveArray(
            final Character[]       values,
            final char         defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new char[0];

        }


        final char[]     retValues = new char[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final Character   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value.charValue());

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * Short型配列をshort型配列へ変換する。
     *
     * @param values        変換するShort型配列
     * @return 変換したshort型配列
     */
    public static short[] toPrimitiveArray(
            final Short[]       values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, (short)0);

    }


    /**
     * Short型配列をshort型配列へ変換する。
     *
     * @param values        変換するShort型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したshort型配列
     */
    public static short[] toPrimitiveArray(
            final Short[]       values,
            final short         defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new short[0];

        }


        final short[]     retValues = new short[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final Short   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value.shortValue());

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * Integer型配列をint型配列へ変換する。
     *
     * @param values        変換するInteger型配列
     * @return 変換したint型配列
     */
    public static int[] toPrimitiveArray(
            final Integer[]     values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, 0);

    }


    /**
     * Integer型配列をint型配列へ変換する。
     *
     * @param values        変換するInteger型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したint型配列
     */
    public static int[] toPrimitiveArray(
            final Integer[]     values,
            final int           defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new int[0];

        }


        final int[]     retValues = new int[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final Integer   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value.intValue());

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * Long型配列をlong型配列へ変換する。
     *
     * @param values        変換するLong型配列
     * @return 変換したint型配列
     */
    public static long[] toPrimitiveArray(
            final Long[]        values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, 0L);

    }


    /**
     * Long型配列をlong型配列へ変換する。
     *
     * @param values        変換するLong型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したint型配列
     */
    public static long[] toPrimitiveArray(
            final Long[]        values,
            final long          defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new long[0];

        }


        final long[]     retValues = new long[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final Long   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value.longValue());

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * Float型配列をfloat型配列へ変換する。
     *
     * @param values        変換するFloat型配列
     * @return 変換したint型配列
     */
    public static float[] toPrimitiveArray(
            final Float[]        values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, 0.0f);

    }


    /**
     * Float型配列をfloat型配列へ変換する。
     *
     * @param values        変換するFloat型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したint型配列
     */
    public static float[] toPrimitiveArray(
            final Float[]        values,
            final float          defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new float[0];

        }


        final float[]     retValues = new float[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final Float   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value.floatValue());

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * Double型配列をdouble型配列へ変換する。
     *
     * @param values        変換するDouble型配列
     * @return 変換したint型配列
     */
    public static double[] toPrimitiveArray(
            final Double[]        values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, 0.0d);

    }


    /**
     * Double型配列をdouble型配列へ変換する。
     *
     * @param values        変換するDouble型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したint型配列
     */
    public static double[] toPrimitiveArray(
            final Double[]        values,
            final double          defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new double[0];

        }


        final double[]     retValues = new double[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final Double   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value.doubleValue());

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * String型配列をString型配列へ変換する。
     *
     * @param values        変換するString型配列
     * @return 変換したint型配列
     */
    public static String[] toPrimitiveArray(
            final String[]        values
            ) {

        // 変換した値を返却する
        return toPrimitiveArray(values, null);

    }


    /**
     * String型配列をString型配列へ変換する。
     *
     * @param values        変換するString型配列
     * @param defaultValue  変換対象値が変換できなかった場合に返却するデフォルト値
     * @return 変換したint型配列
     */
    public static String[] toPrimitiveArray(
            final String[]        values,
            final String          defaultValue
            ) {

        // 引数が null の場合
        if (values == null) {

            // nullを返却する
            return null;

        }

        // 引数が空の配列の場合
        if (values.length == 0) {

            // 空の配列を返却する
            return new String[0];

        }


        final String[]     retValues = new String[values.length];

        // 値を変換する
        for (int i = 0; i < retValues.length; i++) {

            final String   value = values[i];

            // プリミティブへ変換する
            retValues[i] = (value == null ? defaultValue : value);

        }


        // 変換した値を返却する
        return retValues;

    }


    /**
     * 配列データをリストへ変換する。
     *
     * @param <T> 配列データ型
     * @param values リストへ変換する配列データ
     * @return 変換したリスト
     */
    public static <T> List<T> toList(
            final T...  values
            ) {

        // nullの場合は例外
        if (values == null) {

            throw new IllegalArgumentException();

        }


        final List<T>   retList = new ArrayList<T>();

        // データ分処理をする
        for (final T value : values) {

            // リストへ値を追加する
            retList.add(value);

        }

        // 作成したリストを返却する
        return retList;

    }


    /**
     * 配列データをセットへ変換する。
     *
     * @param <T> 配列データ型
     * @param values セットへ変換する配列データ
     * @return 変換したセットコレクション
     */
    public static <T> Set<T> toSet(
            final T...  values
            ) {

        // nullの場合は例外
        if (values == null) {

            throw new IllegalArgumentException();

        }


        final Set<T>   retSet = new HashSet<T>();

        // データ分処理をする
        for (final T value : values) {

            // セットへ値を追加する
            retSet.add(value);

        }

        // 作成したセットを返却する
        return retSet;

    }


    /**
     * ファイルをAndroid URIへ変換する。
     *
     * @param file 変換するファイル
     * @return 変換したURI。失敗時は null
     * @throws IllegalArgumentException 変換するファイルが null の場合
     */
    public static Uri toUri(
            final File  file
            ) {

        // 引数が null の場合は例外
        if (file == null) {

            throw new IllegalArgumentException();

        }

        // 変換結果を返す
        return Uri.fromFile(file);

    }


    /**
     * Android URIをファイルへ変換する。
     *
     * @param uri 変換するURI
     * @return 変換したファイル。失敗時は null
     * @throws IllegalArgumentException 変換するURIが null の場合
     */
    public static File toFile(
            final Uri   uri
            ) {

        // 引数が null の場合は例外
        if (uri == null) {

            throw new IllegalArgumentException();

        }


        try {

            // ファイルへ変換する
            return new File(new URI(uri.toString()));

        } catch (final URISyntaxException e) {

            // 変換失敗
            return null;

        }

    }


    /**
     * バイト配列を16進数文字列へ変換する。
     *
     * @param byteArray バイト配列
     * @return 変換した16進数文字列
     */
    public static String toHex(
            final byte[]    byteArray
            ) {

        final int           toInt    = 0xFF;    // int型変換用
        final int           hexValue = 16;      // 16進数
        final StringBuffer  sb       = new StringBuffer(byteArray.length * 2);

        // バイト配列の各要素ごとに処理をする
        for (int i = 0; i < byteArray.length; i++) {

            final int     num = byteArray[i] & toInt;

            // 16より小さい場合
            if (num < hexValue) {

                // 0を付加する
                sb.append('0');

            }

            // 16進数へ変換する
            sb.append(Integer.toHexString(num));

        }


        // 変換後の文字列を返す
        return sb.toString();

    }


    /**
     * 16進数文字列をバイト配列へ変換する。
     *
     * @param hexStr 16進数文字列
     * @return 変換したバイト配列
     */
    public static byte[] toByte(
            final String    hexStr
            ) {

        final int   hexValue = 16;      // 16進数
        String      tempHexStr;
        int         length;
        byte[]      retBuffer;


        // 長さが奇数の場合、先頭に 0 を付加する
        if ((hexStr.length() & 1) != 0) {

            tempHexStr = '0' + hexStr;

        } else {

            tempHexStr = hexStr;

        }

        // 長さとバッファを作成
        length    = tempHexStr.length() >> 1;
        retBuffer = new byte[length];


        // 16進数へ変換する
        for (int i = 0; i < length; i++) {

            final int     index = i * 2;

            retBuffer[i] = (byte)Integer.parseInt(
                    hexStr.substring(index, index + 2),
                    hexValue);

        }

        // 作成したバイト配列を返す
        return retBuffer;

    }



    /**
     * データ変換種別列挙型。
     *
     * @author Kou
     *
     */
    public static enum DataConvertType {

        /**
         * 標準変換
         */
        GENERAL,

    }


    /**
     * 日付フォーマット種別列挙型。
     *
     * @author Kou
     *
     */
    public static enum DateFormatType {


        /**
         * ANSI C形式<br>
         * (EEE MMM d HH:mm:ss yyyy)<br>
         */
        ANSI_C("EEE MMM d HH:mm:ss yyyy"),

        /**
         * RFC1036形式<br>
         * (EEEE, dd-MMM-yy HH:mm:ss zzz)<br>
         */
        RFC1036("EEEE, dd-MMM-yy HH:mm:ss zzz"),

        /**
         * RFC1123形式<br>
         * (EEE, dd MMM yyyy HH:mm:ss zzz)<br>
         */
        RFC1123("EEE, dd MMM yyyy HH:mm:ss zzz"),

        /**
         * SQL99形式<br>
         * (yyyy-MM-dd' 'HH:mm:ss.SSS)<br>
         */
        SQL99("yyyy-MM-dd' 'HH:mm:ss.SSS"),

        /**
         * ISO8601形式 (年月日と時間)<br>
         * (yyyy-MM-dd'T'HH:mm:ss.SSS)<br>
         */
        ISO8601_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss.SSS"),

        /**
         * ISO8601形式 (年月日のみ)<br>
         * (yyyy-MM-dd)<br>
         */
        ISO8601_DATE("yyyy-MM-dd"),

        /**
         * ISO8601形式 (時間のみ)<br>
         * (HH:mm:ss)<br>
         */
        ISO8601_TIME("HH:mm:ss");



        /**
         * 日付フォーマット文字列
         */
        private final String    format;


        /**
         * 日付フォーマット種別を初期化する。
         *
         * @param pattern    フォーマットパターン文字列
         */
        private DateFormatType(
                final String    pattern
                ) {

            format = pattern;

        }


        /**
         * 日付フォーマット文字列を取得する。
         *
         * @return 日付フォーマット文字列
         */
        String getFormat() {

            return format;

        }

    }


    /**
     * 型変換処理定義用インターフェース。
     *
     * @author Kou
     *
     */
    private interface TypeConvertiable {


        /**
         * 型変換処理を実行する。
         *
         * @param <T>           変換型
         * @param convertType   変換種別
         * @param resultType    変換後の型
         * @param value         変換する値
         * @return 変換した値
         */
        <T> T convertType(
                final DataConvertType   convertType,
                final Class<T>          resultType,
                final Object            value
                );


    }

}
