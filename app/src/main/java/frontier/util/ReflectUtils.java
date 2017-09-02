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

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import frontier.util.ConvertUtils.DataConvertType;


/**
 * リフレクション機能を提供するユーティリティクラス。<br>
 *
 * @author Kou
 *
 */
public final class ReflectUtils {


    /**
     * setterメソッドの接頭語
     */
    private static final String[]               PREFIX_SETTER = new String[] {

        "set"

    };

    /**
     * getterメソッドの接頭語
     */
    private static final String[]               PREFIX_GETTER = new String[] {

        "get",
        "is",

    };

    /**
     * 公開メソッドマップのキャッシュ
     */
    private static final Map<Class<?>, WeakReference<Map<String, Method>>>  CACHE_PUBLIC_METHOD_MAP =
        new WeakHashMap<Class<?>, WeakReference<Map<String, Method>>>();

    /**
     * 非公開メソッドマップのキャッシュ
     */
    private static final Map<Class<?>, WeakReference<Map<String, Method>>>  CACHE_DECLARED_METHOD_MAP =
        new WeakHashMap<Class<?>, WeakReference<Map<String, Method>>>();

    /**
     * 全メソッドマップのキャッシュ
     */
    private static final Map<Class<?>, WeakReference<Map<String, Method>>>  CACHE_METHOD_MAP =
        new WeakHashMap<Class<?>, WeakReference<Map<String, Method>>>();

    /**
     * 公開メソッドのキャッシュ
     */
    private static final Map<Class<?>, WeakReference<List<Method>>>         CACHE_PUBLIC_METHOD =
        new WeakHashMap<Class<?>, WeakReference<List<Method>>>();

    /**
     * 非公開メソッドのキャッシュ
     */
    private static final Map<Class<?>, WeakReference<List<Method>>>         CACHE_DECLARED_METHOD =
        new WeakHashMap<Class<?>, WeakReference<List<Method>>>();

    /**
     * 全メソッドのキャッシュ
     */
    private static final Map<Class<?>, WeakReference<List<Method>>>         CACHE_METHOD =
        new WeakHashMap<Class<?>, WeakReference<List<Method>>>();




    /**
     * インスタンス生成防止。
     *
     */
    private ReflectUtils() {

        // 処理なし

    }


    /**
     * 指定したパッケージを含むクラス名のクラスオブジェクトを取得する。
     *
     * @param name パッケージを含むクラス名
     * @return クラスオブジェクト
     * @throws ReflectException         指定された名称のクラスがない場合
     * @throws IllegalArgumentException クラス名が null の場合
     */
    public static Class<?> getClassObject(
            final String    name
            ) throws ReflectException {

        // nullの場合は例外
        if (name == null) {

            throw new IllegalArgumentException();

        }


        try {

            // 指定名称のクラスオブジェクトを返す
            return Class.forName(name);

        } catch (final ClassNotFoundException e) {

            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの全スーパークラスを取得する。<br>
     * <br>
     * 指定クラスの全スーパークラスを取得し、一覧として返却する。<br>
     * 最上位のスーパークラスであるObject型を除いた全スーパークラスが返される。<br>
     * <br>
     * また、返却一覧へ指定した検索開始・終了クラスの情報は含まれない。<br>
     *
     * @param clazz         クラス
     * @return 指定クラスの全スーパークラス
     * @throws IllegalArgumentException クラスが null の場合
     */
    public static List<Class<?>> getSuperClasses(
            final Class<?>  clazz
            ) {

        return getSuperClasses(clazz, null, false, false);

    }


    /**
     * 指定クラスの指定範囲内スーパークラスを取得する。<br>
     * <br>
     * 指定クラスの指定範囲内スーパークラスを取得し、一覧として返却する。<br>
     * 最上位のスーパークラスであるObject型を除いた全スーパークラスが返される。<br>
     * <br>
     * また、返却一覧へ指定した検索開始・終了クラスの情報は含まれない。<br>
     *
     * @param startClass    検索開始クラス
     * @param endClass      検索終了クラス
     * @return 指定クラスの全スーパークラス
     * @throws IllegalArgumentException 検索開始クラスが null の場合
     */
    public static List<Class<?>> getSuperClasses(
            final Class<?>  startClass,
            final Class<?>  endClass
            ) {

        return getSuperClasses(startClass, endClass, false, false);

    }


    /**
     * 指定クラスの全スーパークラスを取得する。<br>
     * <br>
     * 指定クラスの全スーパークラスを取得し、一覧として返却する。<br>
     * 最上位のスーパークラスであるObject型を除いた全スーパークラスが返される。<br>
     * <br>
     * また、返却一覧へ指定した検索開始・終了クラスの情報を含めるかどうかを設定できる。<br>
     *
     * @param startClass        検索開始クラス
     * @param endClass          検索終了クラス
     * @param includeStartClass 返却一覧へ検索開始クラスを含めるかどうか
     * @param includeEndClass   返却一覧へ検索終了クラスを含めるかどうか
     * @return 指定クラスの全スーパークラス
     * @throws IllegalArgumentException 検索開始クラスが null の場合
     */
    public static List<Class<?>> getSuperClasses(
            final Class<?>  startClass,
            final Class<?>  endClass,
            final boolean   includeStartClass,
            final boolean   includeEndClass
            ) {

        // 指定クラスが null の場合は例外
        if (startClass == null) {

            throw new IllegalArgumentException();

        }


        final List<Class<?>>    retClasses  = new ArrayList<Class<?>>();    // 返却クラス一覧
        Class<?>                nowClass;                                   // 現在のクラス


        // 返却一覧へ検索開始クラス情報を含める場合
        if (includeStartClass) {

            // 現在のクラスに指定された検索開始クラスを設定できる
            nowClass = startClass;

        } else {

            // 指定されたクラスのスーパークラスを設定する
            nowClass = startClass.getSuperclass();

        }


        // 検索終了クラスが指定されていない場合
        if (endClass == null) {

            // スーパークラスが null
            // または Object になるまで繰り返し
            while ((nowClass != null)
                   && !Object.class.equals(nowClass)
                   ) {

                // 一覧へ取得したスーパークラスを追加する
                retClasses.add(nowClass);

                // 現在のクラスのスーパークラスを取得する
                nowClass = nowClass.getSuperclass();

            }

        } else {

            // スーパークラスが null
            // または 検索終了クラス
            // または Object になるまで繰り返し
            while ((nowClass != null)
                   && !endClass.equals(nowClass)
                   && !Object.class.equals(nowClass)
                   ) {

                // 一覧へ取得したスーパークラスを追加する
                retClasses.add(nowClass);

                // 現在のクラスのスーパークラスを取得する
                nowClass = nowClass.getSuperclass();

            }

            // 返却一覧へ検索終了クラス情報を含める場合
            if (includeEndClass) {

                // 現在のクラスに指定された検索終了クラスを設定できる
                retClasses.add(nowClass);

            }

        }


        // 作成した一覧を返却する
        return retClasses;

    }


    /**
     * 指定インスタンスの指定フィールド(公開フィールド対象)を取得する。
     *
     * @param instance  インスタンス
     * @param name      取得するフィールド名
     * @return 指定インスタンスの指定フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getInstancePublicField(
            final Object    instance,
            final String    name
            ) throws ReflectException {

        return getInstancePublicField(null, instance, name);

    }


    /**
     * 指定インスタンスの指定フィールド(公開フィールド対象)を取得する。
     *
     * @param clazz     クラス
     * @param instance  インスタンス
     * @param name      取得するフィールド名
     * @return 指定インスタンスの指定フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getInstancePublicField(
            final Class<?>  clazz,
            final Object    instance,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }

        // インスタンスからクラスを取得する
        final Class<?>    localClass = clazz == null ? instance.getClass() : clazz;


        try {

            // 公開フィールドを返す
            return localClass.getField(name);

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }

    /**
     * 指定インスタンスの全公開フィールドを取得する。
     *
     * @param instance  インスタンス
     * @return 指定インスタンスのフィールド一覧
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static List<Field> getInstancePublicFields(
            final Object    instance
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (instance == null) {

            throw new IllegalArgumentException();

        }

        // インスタンスクラスのフィールド一覧を返す
        return getClassFields(instance.getClass());

    }


    /**
     * 指定インスタンスの指定フィールド(publicフィールド対象)を取得する。
     *
     * @param instance  インスタンス
     * @param name      取得するフィールド名
     * @return 指定インスタンスの指定フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getInstanceDeclaredField(
            final Object    instance,
            final String    name
            ) throws ReflectException {

        return getInstanceDeclaredField(null, instance, name);

    }


    /**
     * 指定インスタンスの指定フィールド(publicフィールド対象)を取得する。
     *
     * @param clazz     クラス
     * @param instance  インスタンス
     * @param name      取得するフィールド名
     * @return 指定インスタンスの指定フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getInstanceDeclaredField(
            final Class<?>  clazz,
            final Object    instance,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }

        // インスタンスからクラスを取得する
        final Class<?>    localClass = clazz == null ? instance.getClass() : clazz;


        try {

            // 非公開フィールドを取得する
            final Field     retField = localClass.getDeclaredField(name);

            // アクセス可能に設定する
            retField.setAccessible(true);

            // フィールドを返却する
            return retField;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定インスタンスの全非公開フィールドを取得する。
     *
     * @param instance  インスタンス
     * @return 指定インスタンスのフィールド一覧
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static List<Field> getInstanceDeclaredFields(
            final Object    instance
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (instance == null) {

            throw new IllegalArgumentException();

        }

        // インスタンスクラスのフィールド一覧を返す
        return getClassFields(instance.getClass());

    }


    /**
     * 指定インスタンスの指定フィールド(全フィールド対象)を取得する。
     *
     * @param instance  インスタンス
     * @param name      取得するフィールド名
     * @return 指定インスタンスの指定フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getInstanceField(
            final Object    instance,
            final String    name
            ) throws ReflectException {

        return getInstanceField(null, instance, name);

    }


    /**
     * 指定インスタンスの指定フィールド(全フィールド対象)を取得する。
     *
     * @param clazz     クラス
     * @param instance  インスタンス
     * @param name      取得するフィールド名
     * @return 指定インスタンスの指定フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getInstanceField(
            final Class<?>  clazz,
            final Object    instance,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        try {

            try {

                // 公開フィールドを返す
                return getInstancePublicField(clazz, instance, name);

            } catch (final Throwable e) {

                // 内部フィールドを返す
                return getInstanceDeclaredField(clazz, instance, name);

            }

        } catch (final ReflectException e) {

            // 失敗のため例外を返す
            throw e;

        }

    }


    /**
     * 指定インスタンスの全フィールドを取得する。
     *
     * @param instance  インスタンス
     * @return 指定インスタンスのフィールド一覧
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static List<Field> getInstanceFields(
            final Object    instance
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (instance == null) {

            throw new IllegalArgumentException();

        }

        // インスタンスクラスのフィールド一覧を返す
        return getClassFields(instance.getClass());

    }


    /**
     * 指定クラスの指定公開フィールドを取得する。
     *
     * @param clazz     クラス
     * @param name      フィールド名
     * @return 指定クラスの指定公開フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getClassPublicField(
            final Class<?>  clazz,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // 指定フィールドを返す
            return clazz.getField(name);

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの指定非公開フィールドを取得する。
     *
     * @param clazz     クラス
     * @param name      フィールド名
     * @return 指定クラスの指定非公開フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getClassDeclaredField(
            final Class<?>  clazz,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // 指定フィールドを取得する
            final Field     retField = clazz.getDeclaredField(name);

            // アクセス可能を設定する
            retField.setAccessible(true);

            // 取得したフィールドを返す
            return retField;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの指定フィールドを取得する。
     *
     * @param clazz     クラス
     * @param name      フィールド名
     * @return 指定クラスの指定フィールド
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static Field getClassField(
            final Class<?>  clazz,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        try {

            try {

                // 指定公開フィールドを返す
                return getClassPublicField(clazz, name);

            } catch (final Throwable e) {

                // 指定非公開フィールドを返す
                return getClassDeclaredField(clazz, name);

            }

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの全公開フィールドを取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのフィールド一覧
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static List<Field> getClassPublicFields(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            // 返却一覧を返す
            return Arrays.asList(clazz.getFields());

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの全非公開フィールドを取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのフィールド一覧
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static List<Field> getClassDeclaredFields(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            // 返却一覧を作成する
            final List<Field>   retFields = new ArrayList<Field>();

            // 非公開フィールド一覧を取得する
            final Field[]       fields = clazz.getDeclaredFields();

            // 全フィールド分処理をする
            for (final Field field : fields) {

                // アクセス可能に設定する
                field.setAccessible(true);

                // 返却一覧へ追加する
                retFields.add(field);

            }

            // 返却一覧を返す
            return retFields;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの全フィールドを取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのフィールド一覧
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static List<Field> getClassFields(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            // 公開フィールドと非公開フィールドを取得する
            final Field[] fields         = clazz.getFields();
            final Field[] declaredFields = clazz.getDeclaredFields();

            // 返却一覧を作成する
            final List<Field>   retFields = new ArrayList<Field>();

            // 公開フィールド分だけ処理をする
            for (final Field field : fields) {

                // フィールドを返却一覧へ追加する
                retFields.add(field);

            }

            // 非公開フィールド分だけ処理をする
            for (final Field field : declaredFields) {

                // アクセス可能に設定する
                field.setAccessible(true);

                // フィールドを返却一覧へ追加する
                retFields.add(field);

            }

            // 返却一覧を返す
            return retFields;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの全インスタンスフィールドを取得する。<br>
     * <br>
     * 指定クラスの非staticフィールドの一覧を取得する。<br>
     *
     * @param clazz  クラス
     * @return 指定クラスのフィールド一覧
     * @throws ReflectException フィールド取得失敗エラー時
     */
    public static List<Field> getClassInstanceFields(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            // 公開フィールドと非公開フィールドを取得する
            final Field[] fields         = clazz.getFields();
            final Field[] declaredFields = clazz.getDeclaredFields();

            // 返却一覧を作成する
            final List<Field>   retFields = new ArrayList<Field>();

            // 公開フィールド分だけ処理をする
            for (final Field field : fields) {

                // staticの場合
                if (Modifier.isStatic(field.getModifiers())) {

                    // 次のフィールドへ
                    continue;

                }

                // フィールドを返却一覧へ追加する
                retFields.add(field);

            }

            // 非公開フィールド分だけ処理をする
            for (final Field field : declaredFields) {

                // staticの場合
                if (Modifier.isStatic(field.getModifiers())) {

                    // 次のフィールドへ
                    continue;

                }

                // アクセス可能に設定する
                field.setAccessible(true);

                // フィールドを返却一覧へ追加する
                retFields.add(field);

            }

            // 返却一覧を返す
            return retFields;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定されたインスタンス公開フィールドへ値を設定する。
     *
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setInstancePublicFieldValue(
            final Object    instance,
            final String    name,
            final Object    value
            ) throws ReflectException {

        setInstancePublicFieldValue(null, instance, name, value);

    }


    /**
     * 指定されたインスタンス公開フィールドへ値を設定する。
     *
     * @param clazz     対象クラス
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setInstancePublicFieldValue(
            final Class<?>  clazz,
            final Object    instance,
            final String    name,
            final Object    value
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 対象クラスがない場合
            if (clazz == null) {

                // 指定インスタンスの公開フィールドを取得する
                field = getInstancePublicField(instance, name);

            } else {

                // 指定クラスの公開フィールドを取得する
                field = getClassPublicField(clazz, name);

            }

            // 値を設定する
            field.set(instance, value);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定されたインスタンス公開フィールドの値を取得する。
     *
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @return 指定されたインスタンス公開フィールドの値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Object getInstancePublicFieldValue(
            final Object    instance,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 指定インスタンスの公開フィールドを取得する
            field = getInstancePublicField(instance, name);

            // 値を返す
            return field.get(instance);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定されたインスタンス非公開フィールドへ値を設定する。
     *
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setInstanceDeclaredFieldValue(
            final Object    instance,
            final String    name,
            final Object    value
            ) throws ReflectException {

        setInstanceDeclaredFieldValue(null, instance, name, value);

    }


    /**
     * 指定されたインスタンス非公開フィールドへ値を設定する。
     *
     * @param clazz     対象クラス
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setInstanceDeclaredFieldValue(
            final Class<?>  clazz,
            final Object    instance,
            final String    name,
            final Object    value
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 対象クラスがない場合
            if (clazz == null) {

                // 指定インスタンスの非公開フィールドを取得する
                field = getInstanceDeclaredField(instance, name);

            } else {

                // 指定クラスの非公開フィールドを取得する
                field = getClassDeclaredField(clazz, name);

            }

            // 値を設定する
            field.set(instance, value);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定されたインスタンス非公開フィールドの値を取得する。
     *
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @return 指定されたインスタンス非公開フィールドの値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Object getInstanceDeclaredFieldValue(
            final Object    instance,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 指定インスタンスの非公開フィールドを取得する
            field = getInstanceDeclaredField(instance, name);

            // 値を返す
            return field.get(instance);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定されたインスタンスフィールドへ値を設定する。
     *
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(
            final Object    instance,
            final String    name,
            final Object    value
            ) throws ReflectException {

        setInstanceFieldValue(null, instance, name, value);

    }


    /**
     * 指定されたインスタンスフィールドへ値を設定する。
     *
     * @param clazz     対象クラス
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setInstanceFieldValue(
            final Class<?>  clazz,
            final Object    instance,
            final String    name,
            final Object    value
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 対象クラスがない場合
            if (clazz == null) {

                try {

                    // 指定インスタンスの公開フィールドを取得する
                    field = getInstancePublicField(instance, name);

                } catch (final Throwable e) {

                    // 指定インスタンスの非公開フィールドを取得する
                    field = getInstanceDeclaredField(instance, name);

                }

            } else {

                try {

                    // 指定クラスの公開フィールドを取得する
                    field = getClassPublicField(clazz, name);

                } catch (final Throwable e) {

                    // 指定クラスの非公開フィールドを取得する
                    field = getClassDeclaredField(clazz, name);

                }

            }

            // 値を設定する
            field.set(instance, value);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定されたインスタンスフィールドの値を取得する。
     *
     * @param instance  対象インスタンス
     * @param name      フィールド名
     * @return 指定されたインスタンスフィールドの値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Object getInstanceFieldValue(
            final Object    instance,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            try {

                // 指定インスタンスの公開フィールドを取得する
                field = getInstancePublicField(instance, name);

            } catch (final Throwable e) {

                // 指定インスタンスの非公開フィールドを取得する
                field = getInstanceDeclaredField(instance, name);

            }

            // 値を返す
            return field.get(instance);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定された static 公開フィールドへ値を設定する。
     *
     * @param clazz     対象クラス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setStaticPublicFieldValue(
            final Class<?>  clazz,
            final String    name,
            final Object    value
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 指定staticの公開フィールドを取得する
            field = getClassPublicField(clazz, name);

            // 値を設定する
            field.set(null, value);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定された static 公開フィールドの値を取得する。
     *
     * @param clazz     対象クラス
     * @param name      フィールド名
     * @return 指定された static 公開フィールドの値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Object getStaticPublicFieldValue(
            final Class<?>  clazz,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 指定staticの公開フィールドを取得する
            field = getClassPublicField(clazz, name);

            // 値を返す
            return field.get(null);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定された static 非公開フィールドへ値を設定する。
     *
     * @param clazz     対象クラス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setStaticDeclaredFieldValue(
            final Class<?>  clazz,
            final String    name,
            final Object    value
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 指定staticの非公開フィールドを取得する
            field = getClassDeclaredField(clazz, name);

            // 値を設定する
            field.set(null, value);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定された static 非公開フィールドの値を取得する。
     *
     * @param clazz     対象クラス
     * @param name      フィールド名
     * @return 指定された static 非公開フィールドの値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Object getStaticDeclaredFieldValue(
            final Class<?>  clazz,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            // 指定staticの非公開フィールドを取得する
            field = getClassDeclaredField(clazz, name);

            // 値を返す
            return field.get(null);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定された static フィールドへ値を設定する。
     *
     * @param clazz     対象クラス
     * @param name      フィールド名
     * @param value     設定する値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static void setStaticFieldValue(
            final Class<?>  clazz,
            final String    name,
            final Object    value
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            try {

                // 指定staticの公開フィールドを取得する
                field = getClassPublicField(clazz, name);

            } catch (final Throwable e) {

                // 指定staticの非公開フィールドを取得する
                field = getClassDeclaredField(clazz, name);

            }

            // 値を設定する
            field.set(null, value);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定されたstaticフィールドの値を取得する。
     *
     * @param clazz     対象クラス
     * @param name      フィールド名
     * @return 指定された static フィールドの値
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Object getStaticFieldValue(
            final Class<?>  clazz,
            final String    name
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((clazz == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        Field     field = null;     // 取得したフィールド

        try {

            try {

                // 指定staticの公開フィールドを取得する
                field = getClassPublicField(clazz, name);

            } catch (final Throwable e) {

                // 指定staticの非公開フィールドを取得する
                field = getClassDeclaredField(clazz, name);

            }

            // 値を返す
            return field.get(null);

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);
        }

    }


    /**
     * 指定インスタンスの指定メソッド(公開メソッド対象)を取得する。
     *
     * @param instance          インスタンス
     * @param name              取得するメソッド名
     * @param parameterTypes    取得するメソッドのパラメータ一覧
     * @return 指定インスタンスの指定メソッド
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Method getPublicMethod(
            final Object        instance,
            final String        name,
            final Class<?>...   parameterTypes
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }

        // インスタンスからクラスを取得する
        final Class<?>    clazz = instance.getClass();


        try {

            // 公開メソッドを返す
            return clazz.getMethod(name, parameterTypes);

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }



    /**
     * 指定インスタンスの全公開メソッドを取得する。
     *
     * @param instance  インスタンス
     * @return 指定インスタンスのメソッド一覧
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static List<Method> getPublicMethods(
            final Object    instance
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (instance == null) {

            throw new IllegalArgumentException();

        }

        // インスタンスクラスのメソッド一覧を返す
        return getMethods(instance.getClass());

    }


    /**
     * 指定クラスの全公開メソッドを取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのメソッド一覧
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static List<Method> getPublicMethods(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            final WeakReference<List<Method>>   cacheReference;     // キャッシュリファレンス
            List<Method>                        retMethods;         // 返却一覧

            // 公開メソッドキャッシュでロックする
            synchronized (CACHE_PUBLIC_METHOD) {

                // 公開メソッドリファレンスを取得する
                cacheReference = CACHE_PUBLIC_METHOD.get(clazz);

            }

            // メソッド一覧を取得する
            retMethods = cacheReference == null ? null : cacheReference.get();

            // メソッド一覧が取得できた場合
            if (retMethods != null) {

                // メソッド一覧を返却する
                return retMethods;

            }


            // メソッド一覧を取得する
            retMethods = Arrays.asList(clazz.getMethods());

            // 公開メソッドキャッシュでロックする
            synchronized (CACHE_PUBLIC_METHOD) {

                // 作成した一覧をキャッシュマップへ追加する
                CACHE_PUBLIC_METHOD.put(clazz, new WeakReference<List<Method>>(retMethods));

            }

            // 返却一覧を返す
            return retMethods;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの全公開メソッドをマップ形式で取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのメソッド名をキーにしたマップ
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Map<String, Method> getPublicMethodsForMap(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            final WeakReference<Map<String, Method>>    cacheReference; // キャッシュマップリファレンス
            Map<String, Method>                         retMap;         // 返却マップ

            // 公開メソッド一覧キャッシュマップでロックする
            synchronized (CACHE_PUBLIC_METHOD_MAP) {

                // メソッドマップリファレンスを取得する
                cacheReference = CACHE_PUBLIC_METHOD_MAP.get(clazz);

            }

            // メソッドマップを取得する
            retMap = cacheReference == null ? null : cacheReference.get();

            // メソッドマップがある場合
            if (retMap != null) {

                // マップ一覧を取得する
                return retMap;

            }


            // メソッド一覧を取得する
            final Method[]  methods = clazz.getMethods();

            // 返却マップを新規作成する
            retMap = new HashMap<String, Method>();

            // メソッド分処理をする
            for (final Method method : methods) {

                // メソッド名をキーにしてマップへ追加する
                retMap.put(method.getName(), method);

            }

            // 公開メソッド一覧キャッシュマップでロックする
            synchronized (CACHE_PUBLIC_METHOD_MAP) {

                // 作成したマップをキャッシュマップへ追加する
                CACHE_PUBLIC_METHOD_MAP.put(
                        clazz,
                        new WeakReference<Map<String, Method>>(retMap)
                        );

            }

            // 作成したマップを返却する
            return retMap;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定インスタンスの指定メソッド(publicメソッド対象)を取得する。
     *
     * @param instance          インスタンス
     * @param name              取得するメソッド名
     * @param parameterTypes    取得するメソッドのパラメータ一覧
     * @return 指定インスタンスの指定メソッド
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Method getDeclaredMethod(
            final Object        instance,
            final String        name,
            final Class<?>...   parameterTypes
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }

        // インスタンスからクラスを取得する
        final Class<?>    clazz = instance.getClass();


        try {

            // 非公開メソッドを取得する
            final Method     retMethod = clazz.getDeclaredMethod(name, parameterTypes);

            // アクセス可能に設定する
            retMethod.setAccessible(true);

            // メソッドを返却する
            return retMethod;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定インスタンスの全非公開メソッドを取得する。
     *
     * @param instance  インスタンス
     * @return 指定インスタンスのメソッド一覧
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static List<Method> getDeclaredMethods(
            final Object    instance
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (instance == null) {

            throw new IllegalArgumentException();

        }

        // インスタンスクラスのメソッド一覧を返す
        return getMethods(instance.getClass());

    }


    /**
     * 指定クラスの全非公開メソッドを取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのメソッド一覧
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static List<Method> getDeclaredMethods(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            final WeakReference<List<Method>>   cacheReference;     // キャッシュリファレンス
            List<Method>                        retMethods;         // 返却一覧

            // 非公開メソッドキャッシュでロックする
            synchronized (CACHE_DECLARED_METHOD) {

                // 非公開メソッドリファレンスを取得する
                cacheReference = CACHE_DECLARED_METHOD.get(clazz);

            }

            // メソッド一覧を取得する
            retMethods = cacheReference == null ? null : cacheReference.get();

            // メソッド一覧が取得できた場合
            if (retMethods != null) {

                // メソッド一覧を返却する
                return retMethods;

            }


            // メソッド一覧を取得する
            final Method[]       methods = clazz.getDeclaredMethods();


            // 返却一覧を作成する
            retMethods = new ArrayList<Method>();

            // 全メソッド分処理をする
            for (final Method method : methods) {

                // アクセス可能に設定する
                method.setAccessible(true);

                // 返却一覧へ追加する
                retMethods.add(method);

            }


            // 非公開メソッドキャッシュでロックする
            synchronized (CACHE_DECLARED_METHOD) {

                // 作成した一覧をキャッシュマップへ追加する
                CACHE_DECLARED_METHOD.put(clazz, new WeakReference<List<Method>>(retMethods));

            }

            // 返却一覧を返す
            return retMethods;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの全非公開メソッドをマップ形式で取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのメソッド名をキーにしたマップ
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Map<String, Method> getDeclaredMethodsForMap(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            final WeakReference<Map<String, Method>>    cacheReference; // キャッシュマップリファレンス
            Map<String, Method>                         retMap;         // 返却マップ

            // 非公開メソッド一覧キャッシュマップでロックする
            synchronized (CACHE_DECLARED_METHOD_MAP) {

                // メソッドマップリファレンスを取得する
                cacheReference = CACHE_DECLARED_METHOD_MAP.get(clazz);

            }

            // メソッドマップを取得する
            retMap = cacheReference == null ? null : cacheReference.get();

            // メソッドマップがある場合
            if (retMap != null) {

                // マップ一覧を取得する
                return retMap;

            }


            // メソッド一覧を取得する
            final Method[]  methods = clazz.getDeclaredMethods();

            // 返却マップを新規作成する
            retMap = new HashMap<String, Method>();

            // メソッド分処理をする
            for (final Method method : methods) {

                // メソッド名をキーにしてマップへ追加する
                retMap.put(method.getName(), method);

            }

            // 非公開メソッド一覧キャッシュマップでロックする
            synchronized (CACHE_DECLARED_METHOD_MAP) {

                // 作成したマップをキャッシュマップへ追加する
                CACHE_DECLARED_METHOD_MAP.put(
                        clazz,
                        new WeakReference<Map<String, Method>>(retMap)
                        );

            }

            // 作成したマップを返却する
            return retMap;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定インスタンスの指定メソッド(全メソッド対象)を取得する。
     *
     * @param instance          インスタンス
     * @param name              取得するメソッド名
     * @param parameterTypes    取得するメソッドのパラメータ一覧
     * @return 指定インスタンスの指定メソッド
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Method getMethod(
            final Object        instance,
            final String        name,
            final Class<?>...   parameterTypes
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (name == null)) {

            throw new IllegalArgumentException();

        }


        try {

            try {

                // 公開メソッドを返す
                return getPublicMethod(instance, name, parameterTypes);

            } catch (final Throwable e) {

                // 内部メソッドを返す
                return getDeclaredMethod(instance, name, parameterTypes);

            }

        } catch (final ReflectException e) {

            // 失敗のため例外を返す
            throw e;

        }

    }


    /**
     * 指定インスタンスの全メソッドを取得する。
     *
     * @param instance  インスタンス
     * @return 指定インスタンスのメソッド一覧
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static List<Method> getMethods(
            final Object    instance
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (instance == null) {

            throw new IllegalArgumentException();

        }

        // インスタンスクラスのメソッド一覧を返す
        return getMethods(instance.getClass());

    }


    /**
     * 指定クラスの全メソッドを取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのメソッド一覧
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static List<Method> getMethods(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            final WeakReference<List<Method>>   cacheReference;     // キャッシュリファレンス
            List<Method>                        retMethods;         // 返却一覧

            // 全メソッドキャッシュでロックする
            synchronized (CACHE_METHOD) {

                // 全メソッドリファレンスを取得する
                cacheReference = CACHE_METHOD.get(clazz);

            }

            // メソッド一覧を取得する
            retMethods = cacheReference == null ? null : cacheReference.get();

            // メソッド一覧が取得できた場合
            if (retMethods != null) {

                // メソッド一覧を返却する
                return retMethods;

            }


            // 公開メソッドと非公開メソッドを取得する
            final Method[] methods         = clazz.getMethods();
            final Method[] declaredMethods = clazz.getDeclaredMethods();

            // 返却一覧を作成する
            retMethods = new ArrayList<Method>();

            // 公開メソッド分だけ処理をする
            for (final Method method : methods) {

                // メソッドを返却一覧へ追加する
                retMethods.add(method);

            }

            // 非公開メソッド分だけ処理をする
            for (final Method method : declaredMethods) {

                // アクセス可能に設定する
                method.setAccessible(true);

                // メソッドを返却一覧へ追加する
                retMethods.add(method);

            }

            // 全メソッドキャッシュでロックする
            synchronized (CACHE_METHOD) {

                // 全メソッドリファレンスを取得する
                CACHE_METHOD.put(clazz, new WeakReference<List<Method>>(retMethods));

            }


            // 返却一覧を返す
            return retMethods;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの全メソッドをマップ形式で取得する。
     *
     * @param clazz  クラス
     * @return 指定クラスのメソッド名をキーにしたマップ
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static Map<String, Method> getMethodsForMap(
            final Class<?>  clazz
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            final WeakReference<Map<String, Method>>    cacheReference; // キャッシュマップリファレンス
            Map<String, Method>                         retMap;         // 返却マップ

            // 全メソッド一覧キャッシュマップでロックする
            synchronized (CACHE_METHOD_MAP) {

                // メソッドマップリファレンスを取得する
                cacheReference = CACHE_METHOD_MAP.get(clazz);

            }

            // メソッドマップを取得する
            retMap = cacheReference == null ? null : cacheReference.get();

            // メソッドマップがある場合
            if (retMap != null) {

                // マップ一覧を取得する
                return retMap;

            }


            // メソッド一覧を取得する
            final Method[]  methods         = clazz.getMethods();
            final Method[]  declaredMethods = clazz.getDeclaredMethods();

            // 返却マップを新規作成する
            retMap = new HashMap<String, Method>();

            // 公開メソッド分処理をする
            for (final Method method : methods) {

                // メソッド名をキーにしてマップへ追加する
                retMap.put(method.getName(), method);

            }

            // 非公開メソッド分処理をする
            for (final Method method : declaredMethods) {

                // アクセス可能に設定する
                method.setAccessible(true);

                // メソッド名をキーにしてマップへ追加する
                retMap.put(method.getName(), method);

            }

            // 全メソッド一覧キャッシュマップでロックする
            synchronized (CACHE_METHOD_MAP) {

                // 作成したマップをキャッシュマップへ追加する
                CACHE_METHOD_MAP.put(
                        clazz,
                        new WeakReference<Map<String, Method>>(retMap)
                        );

            }

            // 作成したマップを返却する
            return retMap;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスのgetterメソッドを取得する。
     *
     * @param clazz  メソッド取得元クラス
     * @return 指定クラスのgetterメソッド一覧
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static List<Method> getGetterMethods(
            final Class<?>  clazz
            ) throws ReflectException {


        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            final List<Method>  retMethods  = new ArrayList<Method>();  // 返却メソッド一覧
            final Method[]      methods     = clazz.getMethods();       // 指定クラスのメソッド一覧

            // 全メソッド分ループする
            for (final Method method : methods) {

                // getter接頭語分繰り返す
                for (final String prefix : PREFIX_GETTER) {

                    // getterメソッドの場合
                    if (method.getName().startsWith(prefix)) {

                        // 返却メソッド一覧へ追加する
                        retMethods.add(method);

                        // ループ終了
                        break;

                    }

                }

            }

            // 作成した返却メソッド一覧を返す
            return retMethods;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスのsetterメソッドを取得する。
     *
     * @param clazz  メソッド取得元クラス
     * @return 指定クラスのsetterメソッド一覧
     * @throws ReflectException メソッド取得失敗エラー時
     */
    public static List<Method> getSetterMethods(
            final Class<?>  clazz
            ) throws ReflectException {


        // 引数が不正の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }


        try {

            final List<Method>  retMethods  = new ArrayList<Method>();  // 返却メソッド一覧
            final List<Method>  methods     = getPublicMethods(clazz);  // 指定クラスのメソッド一覧

            // 全メソッド分ループする
            for (final Method method : methods) {

                // setter接頭語分繰り返す
                for (final String prefix : PREFIX_SETTER) {

                    // setterメソッドの場合
                    if (method.getName().startsWith(prefix)) {

                        // 返却メソッド一覧へ追加する
                        retMethods.add(method);

                        // ループ終了
                        break;

                    }

                }

            }

            // 作成した返却メソッド一覧を返す
            return retMethods;

        } catch (final Throwable e) {

            // 失敗のため例外を返す
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスのインスタンスを生成する。
     *
     * @param <T>   インスタンスを生成するクラス
     * @param clazz インスタンスを生成するクラス
     * @return 生成したインスタンス
     * @throws ReflectException インスタンス生成失敗時
     */
    public static <T> T newInstance(
            final Class<T>  clazz
            ) throws ReflectException {

        return newInstance(clazz, null, null);

    }


    /**
     * 指定クラスのインスタンスを生成する。
     *
     * @param <T>           インスタンス生成するクラス
     * @param clazz         インスタンス生成するクラス
     * @param argumentTypes コンストラクターの引数の型
     * @param arguments     コンストラクターの引数
     * @return 生成したインスタンス
     * @throws ReflectException インスタンス生成失敗時
     */
    public static <T> T newInstance(
            final Class<T>      clazz,
            final Class<?>[]    argumentTypes,
            final Object[]      arguments
            ) throws ReflectException {

        // 引数が null の場合は例外
        if (clazz == null) {

            throw new IllegalArgumentException();

        }

        try {

            // コンストラクターを取得する
            final Constructor<T>    constructor = clazz.getConstructor(argumentTypes);

            // アクセス可能に設定する
            constructor.setAccessible(true);

            // 指定パラメータでインスタンスを生成する
            return constructor.newInstance(arguments);

        } catch (final Throwable e) {

            // 例外をスローする
            throw new ReflectException(e);

        }

    }


    /**
     * 指定クラスの指定型のアノテーションを取得する。
     *
     * @param <T>               返却アノテーション型
     * @param sourceClass       元のクラス
     * @param annotationType    取得したいアノテーション型
     * @return 指定クラスの指定型のアノテーション。取得出来ない場合は null
     */
    public static <T> T getClassAnnotation(
            final Class<?>                      sourceClass,
            final Class<? extends Annotation>   annotationType
            ) {

        // 引数が不正の場合は例外
        if ((sourceClass == null) || (annotationType == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // 指定クラスの指定型のアノテーションを返す
            return ConvertUtils.<T>cast(
                    sourceClass.getAnnotation(annotationType)
                    );

        } catch (final Throwable e) {

            // 取得失敗
            return null;

        }

    }


    /**
     * 指定クラスの指定型のアノテーションをフィールドごとに取得する。
     *
     * @param sourceClass       元のクラス
     * @param annotationType    取得したいアノテーション型
     * @return フィールド名をキーにしたフィールドごとのアノテーションマップ
     * @throws ReflectException エラー発生時
     */
    public static Map<String, Annotation> getFieldAnnotations(
            final Class<?>                      sourceClass,
            final Class<? extends Annotation>   annotationType
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((sourceClass == null) || (annotationType == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // フィールド名ごとのアノテーション一覧を作成する
            final Map<String, Annotation>   retAnnotations = new HashMap<String, Annotation>();
            final List<Field>               fields         = getClassFields(sourceClass);

            // 全フィールド分処理をする
            for (final Field field : fields) {

                // フィールド名をキーにしてアノテーションを格納する
                retAnnotations.put(
                        field.getName(),
                        field.getAnnotation(annotationType)
                        );

            }

            // 作成したフィールド名ごとのアノテーション一覧を返す
            return retAnnotations;

        } catch (final ReflectException e) {

            throw e;

        } catch (final Throwable e) {

            throw new ReflectException(e);

        }

    }


    /**
     * JavaBeanの規定に則って作成されたクラスインスタンスの指定フィールドに値を設定する。
     *
     * @param <T>           JavaBeanクラス
     * @param convertType   データ変換種別
     * @param instance      インスタンス
     * @param fieldName     値を設定するフィールド名
     * @param value         設定する値
     * @throws ReflectException 設定失敗エラー時
     */
    public static <T> void setBeanValue(
            final DataConvertType   convertType,
            final T                 instance,
            final String            fieldName,
            final Object            value
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((convertType == null) || (instance == null) || (fieldName == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // メソッド一覧キャッシュマップを取得する
            final Map<String, Method>   methods = getPublicMethodsForMap(instance.getClass());
            Method                      method  = null;

            // setter接頭語分繰り返す
            for (final String prefix : PREFIX_SETTER) {

                // 指定フィールド名に対応するsetterメソッドを取得する
                method = methods.get(
                        prefix + StringUtils.toUpperCaseFront(fieldName)
                        );

                // 一致するフィールド名がある場合
                if (method != null) {

                    // ループ終了
                    break;

                }

            }

            // メソッドが取得出来なかった場合
            if (method == null) {

                // 処理なし
                return;

            }


            // 引数の型を取得する
            final Class<?>[]    parameterTypes = method.getParameterTypes();

            // パラメータがない場合
            if (parameterTypes.length == 0) {

                // パラメータなし例外
                throw new ReflectException("this method has not any arguments.");

            // パラメータが多すぎる場合
            } else if (parameterTypes.length > 1) {

                // パラメータ過多例外
                throw new ReflectException("this method has arguments too much.");

            // その他
            } else {

                // 処理なし

            }

            // 先頭パラメータを取得する
            final Class<?>      parameterType  = method.getParameterTypes()[0];
            final Class<?>      valueType      = value == null ? null : value.getClass();


            // 型が等しい場合
            if (parameterType.equals(valueType)) {

                // 値をそのまま設定する
                method.invoke(instance, new Object[] {value});

            // その他
            } else {

                // setter処理を実行する
                method.invoke(instance, new Object[] {
                        ConvertUtils.toType(convertType, parameterType, value)
                        });

            }

        } catch (final ReflectException e) {

            // そのままスローする
            throw e;

        } catch (final Throwable e) {

            // リフレクション例外としてスローする
            throw new ReflectException(e);

        }

    }


    /**
     * JavaBeanの規定に則って作成されたクラスインスタンスの指定フィールドの値を取得する。
     *
     * @param <T>       JavaBeanクラス
     * @param instance  インスタンス
     * @param fieldName 値を設定するフィールド名
     * @return 指定されたフィールドの値
     * @throws ReflectException 設定失敗エラー時
     */
    public static <T> Object getBeanValue(
            final T         instance,
            final String    fieldName
            ) throws ReflectException {

        // 引数が不正の場合は例外
        if ((instance == null) || (fieldName == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // メソッド一覧キャッシュマップを取得する
            final Map<String, Method>   methods = getPublicMethodsForMap(instance.getClass());
            Method                      method  = null;

            // getter接頭語分繰り返す
            for (final String prefix : PREFIX_GETTER) {

                // 指定フィールド名に対応するgetterメソッドを取得する
                method = methods.get(
                        prefix + StringUtils.toUpperCaseFront(fieldName)
                        );

                // 一致するフィールド名がある場合
                if (method != null) {

                    // ループ終了
                    break;

                }

            }

            // メソッドが取得出来なかった場合
            if (method == null) {

                // nullを返す
                return null;

            }


            // メソッド実行結果を返す
            return method.invoke(instance);

        } catch (final Throwable e) {

            throw new ReflectException(e);

        }

    }


    /**
     * JavaBeanの規定に則って作成されたクラスメソッド名を対応フィールド名へ変換する。
     *
     * @param beanMethodName JavaBeanクラスメソッド名 (getXXX, setXXX)
     * @return 対応フィールド名
     */
    public static String toBeanFieldName(
            final String    beanMethodName
            ) {

        // 引数が不正の場合は例外
        if (beanMethodName == null) {

            throw new IllegalArgumentException();

        }

        // getter接頭語分繰り返す
        for (final String prefix : PREFIX_GETTER) {

            // 接頭語が等しい場合
            if (beanMethodName.startsWith(prefix)) {

                // 接頭語を除去して返す
                return StringUtils.toLowerCaseFront(beanMethodName.substring(prefix.length()));

            }

        }

        // setter接頭語分繰り返す
        for (final String prefix : PREFIX_SETTER) {

            // 接頭語が等しい場合
            if (beanMethodName.startsWith(prefix)) {

                // 接頭語を除去して返す
                return StringUtils.toLowerCaseFront(beanMethodName.substring(prefix.length()));

            }

        }


        // そのまま返す
        return beanMethodName;

    }


    /**
     * 指定された引数なしインスタンス公開メソッドを実行する。
     *
     * @param instance          実行するクラスインスタンス
     * @param methodName        実行するメソッド名
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラスインスタンスまたはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeInstancePublicMethod(
            final Object        instance,
            final String        methodName
            ) throws ReflectException {

        return invokeInstancePublicMethod(instance, methodName, null, null);

    }


    /**
     * 指定された引数ありインスタンス公開メソッドを実行する。
     *
     * @param instance          実行するクラスインスタンス
     * @param methodName        実行するメソッド名
     * @param arguments         実行するメソッドパラメータ
     * @param argumentTypes     実行するメソッドパラメータ型
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラスインスタンスまたはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeInstancePublicMethod(
            final Object        instance,
            final String        methodName,
            final Class<?>[]    argumentTypes,
            final Object[]      arguments
            ) throws ReflectException {

        // クラスインスタンスまたはメソッド名が null の場合は例外
        if ((instance == null) || (methodName == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // 実行するメソッドを取得する
            final Method    invokeMethod = instance.getClass().getMethod(
                    methodName,
                    argumentTypes
                    );

            // メソッドを実行する
            return invokeMethod.invoke(instance, arguments);

        } catch (final Throwable e) {

            throw new ReflectException(e);

        }

    }


    /**
     * 指定された引数なしインスタンス非公開メソッドを実行する。
     *
     * @param instance          実行するクラスインスタンス
     * @param methodName        実行するメソッド名
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラスインスタンスまたはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeInstanceDeclaredMethod(
            final Object        instance,
            final String        methodName
            ) throws ReflectException {

        return invokeInstanceDeclaredMethod(instance, methodName, null, null);

    }


    /**
     * 指定された引数ありインスタンス非公開メソッドを実行する。
     *
     * @param instance          実行するクラスインスタンス
     * @param methodName        実行するメソッド名
     * @param arguments         実行するメソッドパラメータ
     * @param argumentTypes     実行するメソッドパラメータ型
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラスインスタンスまたはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeInstanceDeclaredMethod(
            final Object        instance,
            final String        methodName,
            final Class<?>[]    argumentTypes,
            final Object[]      arguments
            ) throws ReflectException {

        // クラスインスタンスまたはメソッド名が null の場合は例外
        if ((instance == null) || (methodName == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // 実行するメソッドを取得する
            final Method    invokeMethod = instance.getClass().getDeclaredMethod(
                    methodName,
                    argumentTypes
                    );

            // アクセス許可を設定する
            invokeMethod.setAccessible(true);

            // メソッドを実行する
            return invokeMethod.invoke(instance, arguments);

        } catch (final Throwable e) {

            throw new ReflectException(e);

        }

    }


    /**
     * 指定された引数なしインスタンスメソッド(全メソッド対象)を実行する。
     *
     * @param instance          実行するクラスインスタンス
     * @param methodName        実行するメソッド名
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラスインスタンスまたはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeInstanceMethod(
            final Object        instance,
            final String        methodName
            ) throws ReflectException {

        return invokeInstanceMethod(instance, methodName, null, null);

    }


    /**
     * 指定された引数ありインスタンスメソッド(全メソッド対象)を実行する。
     *
     * @param instance          実行するクラスインスタンス
     * @param methodName        実行するメソッド名
     * @param arguments         実行するメソッドパラメータ
     * @param argumentTypes     実行するメソッドパラメータ型
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラスインスタンスまたはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeInstanceMethod(
            final Object        instance,
            final String        methodName,
            final Class<?>[]    argumentTypes,
            final Object[]      arguments
            ) throws ReflectException {

        // クラスインスタンスまたはメソッド名が null の場合は例外
        if ((instance == null) || (methodName == null)) {

            throw new IllegalArgumentException();

        }


        try {

            try {

                // 公開メソッドを実行する
                return invokeInstancePublicMethod(instance, methodName, argumentTypes, arguments);

            } catch (final Throwable e) {

                // 非公開メソッドを実行する
                return invokeInstanceDeclaredMethod(instance, methodName, argumentTypes, arguments);

            }

        } catch (final Throwable e) {

            throw new ReflectException(e);

        }

    }


    /**
     * 指定された引数なし static 公開メソッドを実行する。
     *
     * @param className         実行するクラス名
     * @param methodName        実行するメソッド名
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラス名またはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeStaticPublicMethod(
            final String        className,
            final String        methodName
            ) throws ReflectException {

        return invokeStaticPublicMethod(className, methodName, null, null);

    }


    /**
     * 指定された引数あり static 公開メソッドを実行する。
     *
     * @param className         実行するクラス名
     * @param methodName        実行するメソッド名
     * @param arguments         実行するメソッドパラメータ
     * @param argumentTypes     実行するメソッドパラメータ型
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラス名またはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeStaticPublicMethod(
            final String        className,
            final String        methodName,
            final Class<?>[]    argumentTypes,
            final Object[]      arguments
            ) throws ReflectException {

        // クラス名またはメソッド名が null の場合
        if ((className == null) || (methodName == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // 実行するクラスオブジェクトを取得する
            final Class<?>  invokeClass = Class.forName(className);

            // 実行するメソッドを取得する
            final Method    invokeMethod = invokeClass.getMethod(methodName, argumentTypes);

            // メソッドを実行する
            return invokeMethod.invoke(null, arguments);

        } catch (final Throwable e) {

            throw new ReflectException(e);

        }

    }


    /**
     * 指定された引数なし static 非公開メソッドを実行する。
     *
     * @param className         実行するクラス名
     * @param methodName        実行するメソッド名
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラス名またはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeStaticDeclaredMethod(
            final String        className,
            final String        methodName
            ) throws ReflectException {

        return invokeStaticDeclaredMethod(className, methodName, null, null);

    }


    /**
     * 指定された引数あり static 非公開メソッドを実行する。
     *
     * @param className         実行するクラス名
     * @param methodName        実行するメソッド名
     * @param arguments         実行するメソッドパラメータ
     * @param argumentTypes     実行するメソッドパラメータ型
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラス名またはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeStaticDeclaredMethod(
            final String        className,
            final String        methodName,
            final Class<?>[]    argumentTypes,
            final Object[]      arguments
            ) throws ReflectException {

        // クラス名またはメソッド名が null の場合
        if ((className == null) || (methodName == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // 実行するクラスオブジェクトを取得する
            final Class<?>  invokeClass = Class.forName(className);

            // 実行するメソッドを取得する
            final Method    invokeMethod = invokeClass.getDeclaredMethod(methodName, argumentTypes);

            // アクセス許可を設定する
            invokeMethod.setAccessible(true);

            // メソッドを実行する
            return invokeMethod.invoke(null, arguments);

        } catch (final Throwable e) {

            throw new ReflectException(e);

        }

    }


    /**
     * 指定された引数なし static メソッド(全メソッド対象)を実行する。
     *
     * @param className         実行するクラス名
     * @param methodName        実行するメソッド名
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラス名またはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeStaticMethod(
            final String        className,
            final String        methodName
            ) throws ReflectException {

        return invokeStaticMethod(className, methodName, null, null);

    }


    /**
     * 指定された引数あり static メソッド(全メソッド対象)を実行する。
     *
     * @param className         実行するクラス名
     * @param methodName        実行するメソッド名
     * @param arguments         実行するメソッドパラメータ
     * @param argumentTypes     実行するメソッドパラメータ型
     * @return メソッドの戻り値
     * @throws IllegalArgumentException クラス名またはメソッド名が null の場合
     * @throws ReflectException         メソッド実行エラー時
     */
    public static Object invokeStaticMethod(
            final String        className,
            final String        methodName,
            final Class<?>[]    argumentTypes,
            final Object[]      arguments
            ) throws ReflectException {

        // クラス名またはメソッド名が null の場合
        if ((className == null) || (methodName == null)) {

            throw new IllegalArgumentException();

        }


        try {

            try {

                // 公開メソッドを実行する
                return invokeStaticPublicMethod(className, methodName, argumentTypes, arguments);

            } catch (final Throwable e) {

                // 非公開メソッドを実行する
                return invokeStaticDeclaredMethod(className, methodName, argumentTypes, arguments);

            }

        } catch (final ReflectException e) {

            throw e;

        }

    }


}
