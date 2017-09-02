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

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import frontier.device.bluetooth.BluetoothEvent;
import frontier.device.bluetooth.BluetoothInfo;
import frontier.util.DeviceUtils;
import frontier.util.GeneralUtils;
import frontier.util.ReflectUtils;
import frontier.util.StringUtils;




/**
 * 各抽象アクティビティの共通処理を定義したユーティリティークラス。<br>
 * <br>
 * フレームワーク内でのみアクセス可能とする。
 *
 * @author Kou
 *
 */
final class FRActivityUtils {


    /**
     * Bluetoothリクエストコード : Bluetooth有効化
     */
    private static final int            REQUEST_BLUETOOTH_ENABLED       = 0x80000000;

    /**
     * Bluetoothリクエストコード : Bluetooth発見可能状態
     */
    private static final int            REQUEST_BLUETOOTH_DISCOVERABLE  = REQUEST_BLUETOOTH_ENABLED + 1;


    /**
     * フィールドキー名フォーマットのクラス名とフィールド名の区切りトークン
     */
    private static final String         FIELD_TOKEN         = ",";

    /**
     * フィールドキー名フォーマット<br>
     * <br>
     * 1$ = クラス名<br>
     * 2$ = フィールド名<br>
     */
    private static final String         FIELD_KEY_FORMAT    =
        "%1$s" + FIELD_TOKEN + "%2$s";



    /**
     * インスタンス生成防止。
     *
     */
    private FRActivityUtils() {

        // 処理なし

    }


    /**
     * スクリーンを起動する。<br>
     * <br>
     * スリープ状態の場合、スクリーンを起動させる。<br>
     *
     * @param activity アクティビティのインスタンス
     * @throws IllegalArgumentException アクティビティのインスタンスが null の場合
     */
    public static void launchScreen(
            final Activity  activity
            ) {

        // アクティビティのインスタンスが null の場合は例外
        if (activity == null) {

            throw new IllegalArgumentException();

        }

        // スクリーン起動のためのフラグを追加する
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                );

    }


    /**
     * アクティビティのハンドラを取得する。
     *
     * @param activity アクティビティのインスタンス
     * @return アクティビティのハンドラ
     * @throws IllegalArgumentException アクティビティのインスタンスが null の場合
     */
    public static Handler getHandler(
            final Activity  activity
            ) {

        // アクティビティのインスタンスが null の場合は例外
        if (activity == null) {

            throw new IllegalArgumentException();

        }

        // ハンドラを作成して返す
        return new Handler(activity.getMainLooper());

    }


    /**
     * 拡張インテントを取得する。
     *
     * @param activity アクティビティのインスタンス
     * @return 設定されている拡張インテント
     * @throws IllegalArgumentException アクティビティのインスタンスが null の場合
     */
    public static FRIntent getIntentExtra(
            final Activity  activity
            ) {

        // アクティビティのインスタンスが null の場合は例外
        if (activity == null) {

            throw new IllegalArgumentException();

        }


        // インテントを取得する
        Intent    intent = activity.getIntent();

        // インテントが拡張インテントの場合
        if (intent instanceof FRIntent) {

            // そのまま返す
            return (FRIntent)intent;

        }

        // 拡張インテントにラップする
        intent = new FRIntent(intent);

        // 拡張インテントを設定する
        activity.setIntent(intent);

        // 作成した拡張インテントを返却する
        return (FRIntent)intent;

    }


    /**
     * アクティビティの作成の初期処理を行う。<br>
     * <br>
     * アクティビティ作成時における初期化処理を行う。<br>
     *
     * @param activity              アクティビティのインスタンス
     * @param savedInstanceState    {@link #onSaveInstanceState(Bundle)} メソッドで保存されたインスタンスデータ
     */
    public static void onCreate(
            final Activity  activity,
            final Bundle    savedInstanceState
            ) {

        // アクティビティのインスタンスがフレームワーク定義のインスタンスかどうかをチェックする
        checkActivityInstance(activity);

        // 例外ハンドラを取得する
        final UncaughtExceptionHandler  exceptionHandler =
            ((FRActivityProcessable)activity).getUncaughtExceptionHandler();

        // null以外の場合
        if (exceptionHandler != null) {

            // 例外ハンドラを設定する
            setDefaultUncaughtExceptionHandler(exceptionHandler);

        }

    }


    /**
     * アクティビティのレジューム処理を行う。<br>
     * <br>
     * アクティビティが他アクティビティ起動中状態の場合は、他アクティビティ起動中状態を解除する。
     *
     * @param activity  アクティビティのインスタンス
     */
    public static void onResume(
            final Activity  activity
            ) {

        // アクティビティ状態を取得する
        final FRActivityStatus  status = getActivityStatus(activity);

        // 他アクティビティ起動中状態を解除する
        status.setStarting(false);

    }


    /**
     * アクティビティ破棄処理を実行する。<br>
     * <br>
     * Bluetoothレシーバーが登録されている場合、その登録を解除する。
     *
     * @param activity  アクティビティのインスタンス
      */
    public static void onDestroy(
            final Activity  activity
            ) {

        // アクティビティ状態を取得する
        final FRActivityStatus  status = getActivityStatus(activity);

        // Bluetoothレシーバーがある場合
        if (status.getBluetoothReceiver() != null) {

            // レシーバーの登録を解除する
            activity.unregisterReceiver(status.getBluetoothReceiver());

        }

    }


    /**
     * デフォルトの例外ハンドラを設定する。<br>
     * <br>
     * 本メソッドで例外ハンドラを設定することにより、<br>
     * 自前の例外処理とは別に<br>
     * 例外発生時のアプリケーション強制終了ダイアログを表示することが可能になる。<br>
     * <br>
     * Thread#setDefaultUncaughtExceptionHandler で直接設定すると、<br>
     * 強制終了ダイアログが表示できなくなり、アプリもフリーズするため<br>
     * 本メソッドで例外ハンドラを設定することを推奨する。<br>
     *
     * @param handler   設定する例外ハンドラ
     */
    private static void setDefaultUncaughtExceptionHandler(
            final UncaughtExceptionHandler handler
            ) {

        // 例外ハンドラが null の場合は例外
        if (handler == null) {

            throw new IllegalArgumentException();

        }


        // デフォルトの例外ハンドラを取得する
        final UncaughtExceptionHandler          defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 例外ハンドラが内部処理例外ハンドラの場合
        if (defaultHandler instanceof FRUncatchExceptionHandlerInner) {

            // 内部ハンドラへキャストする
            final FRUncatchExceptionHandlerInner innerHandler =
                (FRUncatchExceptionHandlerInner)defaultHandler;

            // 例外ハンドラを再設定する
            Thread.setDefaultUncaughtExceptionHandler(
                    new FRUncatchExceptionHandlerInner(innerHandler.getDefaultHandler()) {

                @Override
                public void uncaughtException(
                        final Thread    thread,
                        final Throwable throwable
                        ) {

                    // 指定された例外ハンドラを実行する
                    handler.uncaughtException(thread, throwable);

                    // デフォルト例外ハンドラが設定されている場合
                    if (getDefaultHandler() != null) {

                        // デフォルト例外ハンドラを実行する
                        getDefaultHandler().uncaughtException(thread, throwable);

                    }

                }

            });

        } else {

            // 例外ハンドラを設定する
            Thread.setDefaultUncaughtExceptionHandler(
                    new FRUncatchExceptionHandlerInner(defaultHandler) {

                @Override
                public void uncaughtException(
                        final Thread    thread,
                        final Throwable throwable
                        ) {

                    // 指定された例外ハンドラを実行する
                    handler.uncaughtException(thread, throwable);

                    // デフォルト例外ハンドラが設定されている場合
                    if (getDefaultHandler() != null) {

                        // デフォルト例外ハンドラを実行する
                        getDefaultHandler().uncaughtException(thread, throwable);

                    }

                }

            });

        }


    }


    /**
     * メンバ変数の自動保存処理を行う。<br>
     * <br>
     * 本メソッドは、端末がメモリ不足などの理由でアプリをメモリ上に常駐できなくなった場合に実行される。<br>
     * 本来の Activity の仕様では各アクティビティごとにメンバ変数を保存する必要があるが、<br>
     * これは非常な手間が掛かり、かつ修正時における不具合の原因となりうる。<br>
     * <br>
     * そこで本クラスはアクティビティに定義されているメンバ変数を自動的に保存する。br>
     * 自動保存できるメンバ変数は、プリミティブ型とそのラッパーと配列など<br>
     * Bundleクラスへの追加をサポートしている型変数のみとなる。<br>
     *
     * @param activity アクティビティのインスタンス
     * @param outState 自動保存するメンバ変数の保存先 Bundle データ
     * @throws IllegalArgumentException アクティビティまたは Bundle データが null の場合は例外
     */
    public static void onSaveInstanceState(
            final Activity  activity,
            final Bundle    outState
            ) {

        // 引数が null の場合は例外
        if ((activity == null) || (outState == null)) {

            throw new IllegalArgumentException();

        }


        try {

            // 自クラスを含んだ全スーパークラス情報を取得する
            final List<Class<?>> classes = ReflectUtils.getSuperClasses(
                    activity.getClass(),
                    Activity.class,
                    true,
                    true
                    );

            // 自クラスを含んだ全スーパークラス情報分繰り返す
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

                    // パラメータを追加する
                    GeneralUtils.putObjectBundle(
                            outState,
                            String.format(
                                    FIELD_KEY_FORMAT,
                                    nowClass.getName(),
                                    field.getName()
                                    ),
                            field.get(activity)
                            );

                }

            }

        } catch (final Throwable e) {

            e.printStackTrace();

        }

    }


    /**
     * メンバ変数の自動復帰処理を行う。<br>
     * <br>
     * {@link #onSaveInstanceState(Bundle)} メソッドで自動保存されたメンバ変数の<br>
     * 自動復帰処理を行う。<br>
     *
     * @param activity              アクティビティのインスタンス
     * @param savedInstanceState    自動保存されたメンバ変数を格納した Bundle データ
     * @throws IllegalArgumentException アクティビティまたは Bundle データが null の場合は例外
     */
    public static void onRestoreInstanceState(
            final Activity  activity,
            final Bundle    savedInstanceState
            ) {

        // 引数が null の場合は例外
        if ((activity == null) || (savedInstanceState == null)) {

            throw new IllegalArgumentException();

        }


        try {

            final int   indexClassName  = 0;    // クラス名インデックス
            final int   indexFieldName  = 1;    // フィールド名インデックス
            final int   indexCount      = 2;    // インデックス数

            // 全キー名分処理をする
            for (final String key : savedInstanceState.keySet()) {

                // キー名を分割する
                final String[]  names     = StringUtils.split(key, FIELD_TOKEN);

                // キー名の要素がインデックス数と異なる場合
                if (names.length != indexCount) {

                    // 次の要素へ
                    continue;

                }

                // クラス名とフィールド名を取得する
                final String    className = names[indexClassName];
                final String    fieldName = names[indexFieldName];

                // 値を設定する
                ReflectUtils.setInstanceFieldValue(
                        ReflectUtils.getClassObject(className),
                        activity,
                        fieldName,
                        savedInstanceState.get(key)
                        );

            }

        } catch (final Throwable e) {

            e.printStackTrace();

        }

    }



    /**
     * 指定されたアクティビティがフレームワーク定義のインスタンスかどうかを取得する。
     *
     * @param activity チェックするアクティビティのインスタンス
     * @throws IllegalArgumentException 指定されたアクティビティがフレームワーク定義のインスタンス以外の場合
     */
    public static void checkActivityInstance(
            final Activity  activity
            ) {

        // アクティビティがフレームワーク定義のアクティビティ以外の場合は例外
        if (!(activity instanceof FRActivityProcessable)) {

            throw new IllegalArgumentException(
                    "The activity instance is not subclass of the frontier framework activity."
                    );

        }

    }


    /**
     * 指定されたアクティビティ状態を取得する。
     *
     * @param activity チェックするアクティビティのインスタンス
     * @return アクティビティ状態
     * @throws IllegalArgumentException 指定されたアクティビティがフレームワーク定義のインスタンス以外の場合
     * @throws IllegalStateException    アクティビティ状態が取得できなかった場合
     */
    private static FRActivityStatus getActivityStatus(
            final Activity  activity
            ) {

        // アクティビティのインスタンスがフレームワーク定義のインスタンスかどうかをチェックする
        checkActivityInstance(activity);

        // アクティビティ状態を取得する
        final FRActivityStatus  status = ((FRActivityProcessable)activity).getStatus();

        // アクティビティ状態が null の場合
        if (status == null) {

            throw new IllegalStateException();

        }

        // 取得したステータスを返す
        return status;

    }


    /**
     * Bluetoothレシーバーを設定する。
     *
     * @param activity  アクティビティのインスタンス
     * @param actions   登録するアクション一覧
     */
    private static void setBluetoothReceiver(
            final Activity  activity,
            final String... actions
            ) {

        final FRActivityStatus  status      = getActivityStatus(activity);      // アクティビティ状態
        final BluetoothReceiver oldReceiver = status.getBluetoothReceiver();    // Bluetoothレシーバー
        final IntentFilter      filter      = new IntentFilter();               // Intentフィルター

        // アクション分繰り返す
        for (final String action : actions) {

            // フィルタへアクションを登録する
            filter.addAction(action);

        }

        // 既存レシーバーがある場合
        if (oldReceiver != null) {

            // 登録を解除する
            activity.unregisterReceiver(oldReceiver);

        }

        // Bluetoothレシーバーを作成する
        final BluetoothReceiver newReceiver = new BluetoothReceiver();

        // アクティビティ状態へ設定する
        status.setBluetoothReceiver(newReceiver);

        // レシーバーを登録する
        activity.registerReceiver(newReceiver, filter);

    }


    /**
     * Bluetooth有効化要求を開始する。<br>
     * <br>
     * {@link FRActivity#onBluetoothResult(int, int, int)} へ指定したリクエストコードで結果が返却される。<br>
     *
     * @param activity  アクティビティのインスタンス
     * @return 有効化設定起動に成功した場合 または 既にBluetoothが有効の場合は true
     */
    public static boolean requestBluetoothEnabled(
            final Activity  activity
            ) {

        // Bluetoothアダプタを取得する
        final BluetoothAdapter  adapter = BluetoothAdapter.getDefaultAdapter();

        // アダプタがない場合
        if (adapter == null) {

            // 有効化設定起動失敗
            return false;

        }

        // Bluetoothが既に有効の場合
        if (adapter.isEnabled()) {

            // 有効化成功とする
            return true;

        }


        // Bluetoothレシーバーを設定する
        setBluetoothReceiver(
                activity,
                BluetoothAdapter.ACTION_STATE_CHANGED
                );

        // Bluetooth設定の起動
        return startActivityForResult(
                activity,
                null,
                REQUEST_BLUETOOTH_ENABLED,
                BluetoothAdapter.ACTION_REQUEST_ENABLE,
                null,
                null,
                null,
                null
                );

    }


    /**
     * Bluetoothデバイス発見可能状態変更を要求する。<br>
     * <br>
     * {@link FRActivity#onBluetoothDiscoveried(int, int, int)} へ指定したリクエストコードで結果が返却される。<br>
     * <br>
     * 発見可能時間はデフォルトの 120 秒で行われる。<br>
     *
     * @param activity  アクティビティのインスタンス
     * @return 有効化設定起動に成功した場合 または 既にBluetoothが有効の場合は true
     */
    public static boolean requestBluetoothDiscoverable(
            final Activity  activity
            ) {

        // Bluetooth非サポートの場合
        if (!DeviceUtils.isSupportedBluetooth()) {

            // 失敗
            return false;

        }


        // Bluetoothレシーバーを設定する
        setBluetoothReceiver(
                activity,
                BluetoothAdapter.ACTION_SCAN_MODE_CHANGED
                );

        // Bluetooth発見可能状態変更要求開始
        return startActivityForResult(
                activity,
                null,
                REQUEST_BLUETOOTH_DISCOVERABLE,
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE,
                null,
                null,
                null,
                null
                );

    }


    /**
     * Bluetoothデバイス発見可能状態変更を要求する。<br>
     * <br>
     * {@link FRActivity#onBluetoothDiscoveried(int, int, int)} へ指定したリクエストコードで結果が返却される。<br>
     *
     * @param activity      アクティビティのインスタンス
     * @param duration      発見可能時間 (0 - 300)
     * @return 有効化設定起動に成功した場合 または 既にBluetoothが有効の場合は true
     */
    public static boolean requestBluetoothDiscoverable(
            final Activity  activity,
            final int       duration
            ) {

        // Bluetooth非サポートの場合
        if (!DeviceUtils.isSupportedBluetooth()) {

            // 失敗
            return false;

        }


        // Bluetoothレシーバーを設定する
        setBluetoothReceiver(
                activity,
                BluetoothAdapter.ACTION_SCAN_MODE_CHANGED
                );

        // Bluetooth発見可能状態変更要求開始
        return startActivityForResult(
                activity,
                null,
                REQUEST_BLUETOOTH_DISCOVERABLE,
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE,
                null,
                null,
                null,
                null,
                new FRNameValuePair(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                        duration
                        )
                );

    }


    /**
     * Bluetoothデバイスを有効にする。
     *
     * @param activity  アクティビティのインスタンス
     * @return Bluetoothデバイスの有効化に成功した場合は true
     */
    public static boolean enableBluetooth(
            final Activity  activity
            ) {

        // アダプタを取得する
        final BluetoothAdapter  adapter = BluetoothAdapter.getDefaultAdapter();

        // アダプタが取得できなかった場合
        if (adapter == null) {

            // 失敗
            return false;

        }


        // 既にBluetoothが有効の場合
        if (adapter.isEnabled()) {

            // 有効を返す
            return true;

        }

        // Bluetoothレシーバーを設定する
        setBluetoothReceiver(
                activity,
                BluetoothAdapter.ACTION_STATE_CHANGED
                );

        // Bluetoothデバイスを有効化する
        return adapter.enable();

    }


    /**
     * Bluetoothデバイスを無効にする。
     *
     * @param activity  アクティビティのインスタンス
     * @return Bluetoothデバイスの無効化に成功した場合は true
     */
    public static boolean disableBluetooth(
            final Activity  activity
            ) {

        // アダプタを取得する
        final BluetoothAdapter  adapter = BluetoothAdapter.getDefaultAdapter();

        // アダプタが取得できなかった場合
        if (adapter == null) {

            // 失敗
            return false;

        }


        // 既にBluetoothが無効の場合
        if (!adapter.isEnabled()) {

            // 成功
            return true;

        }

        // Bluetoothレシーバーを設定する
        setBluetoothReceiver(
                activity,
                BluetoothAdapter.ACTION_STATE_CHANGED
                );

        // Bluetoothデバイスを無効化する
        return adapter.disable();

    }


    /**
     * Bluetoothデバイス発見を開始する。
     *
     * @param activity  アクティビティのインスタンス
     * @return Bluetoothデバイス発見の開始に成功した場合は true
     */
    public static boolean startBluetoothDiscovery(
            final Activity  activity
            ) {

        // Bluetooth非サポートの場合
        if (!DeviceUtils.isSupportedBluetooth()) {

            // 失敗
            return false;

        }

        // Bluetoothレシーバーを設定する
        setBluetoothReceiver(
                activity,
                BluetoothDevice.ACTION_FOUND,
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                );

        // Bluetoothデバイス発見を開始する
        return BluetoothAdapter.getDefaultAdapter().startDiscovery();

    }


    /**
     * Bluetoothデバイス発見をキャンセルする。
     *
     * @return Bluetoothデバイス発見のキャンセルに成功した場合は true
     */
    public static boolean cancelBluetoothDiscovery() {

        // アダプタを取得する
        final BluetoothAdapter  adapter = BluetoothAdapter.getDefaultAdapter();

        // アダプタが取得できなかった場合
        if (adapter == null) {

            // 失敗
            return false;

        }

        // Bluetoothデバイスが発見中以外の場合
        if (!adapter.isDiscovering()) {

            // キャンセル成功を返す
            return true;

        }

        // Bluetoothデバイス発見をキャンセルする
        return adapter.cancelDiscovery();

    }


    /**
     * 指定されたIntentの情報に基づいた画面の起動準備を行う。<br>
     *
     * @param activity  アクティビティのインスタンス
     * @throws IllegalArgumentException 指定されたアクティビティがフレームワーク定義のインスタンス以外の場合
     * @throws IllegalStateException    アクティビティ状態が取得できなかった場合
     */
    public static void prepareStartingActivity(
            final Activity  activity
            ) {

        // アクティビティ状態を取得する
        final FRActivityStatus  status = getActivityStatus(activity);

        // 他アクティビティ起動中にする
        status.setStarting(true);

    }


    /**
     * 遷移先アクティビティに対する各種情報を設定し、
     * パラメータを渡して画面遷移を行う。
     *
     * @param activity      遷移元アクティビティのインスタンス
     * @param destClass     遷移先アクティビティのクラス
     * @param action        遷移先アクティビティへ設定するIntentアクション情報
     * @param type          遷移先アクティビティへ設定するIntentタイプ情報
     * @param categories    遷移先アクティビティへ設定するIntentカテゴリ情報
     * @param data          遷移先アクティビティへ設定するIntentデータ情報
     * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
     * @param params        遷移先に渡すパラメータ
     * @return 画面遷移に成功したかどうか
     * @throws IllegalArgumentException 指定されたアクティビティがフレームワーク定義のインスタンス以外の場合
     * @throws IllegalStateException    アクティビティ状態が取得できなかった場合
     */
    public static boolean startActivity(
            final Activity                      activity,
            final Class<? extends Activity>     destClass,
            final String                        action,
            final String                        type,
            final String[]                      categories,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        // アクティビティ状態を取得する
        final FRActivityStatus      status = getActivityStatus(activity);

        // 他アクティビティ起動中の場合
        if (status.isStarting()) {

            // 処理失敗
            return false;

        }


        try {

            // 他アクティビティ起動中にする
            status.setStarting(true);

            // 画面遷移する
            activity.startActivity(
                    FRIntent.createIntent(activity, destClass, action, type, categories, data, flags, params)
                    );

            // 画面遷移成功
            return true;

        } catch (final Throwable e) {

            // 画面遷移失敗
            return false;

        }

    }


    /**
     * 遷移先アクティビティに対する各種情報を設定し、
     * パラメータを渡して画面遷移を行った後に現在のアクティビティを終了する。
     *
     * @param activity      遷移元アクティビティのインスタンス
     * @param destClass     遷移先アクティビティのクラス
     * @param action        遷移先アクティビティへ設定するIntentアクション情報
     * @param type          遷移先アクティビティへ設定するIntentタイプ情報
     * @param categories    遷移先アクティビティへ設定するIntentカテゴリ情報
     * @param data          遷移先アクティビティへ設定するIntentデータ情報
     * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
     * @param params        遷移先に渡すパラメータ
     * @return 画面遷移に成功したかどうか
     */
    public static boolean startActivityAndFinish(
            final Activity                      activity,
            final Class<? extends Activity>     destClass,
            final String                        action,
            final String                        type,
            final String[]                      categories,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        // 画面遷移を行う
        final boolean   result = startActivity(activity, destClass, action, type, categories, data, flags, params);

        // 画面遷移が成功した場合
        if (result) {

            // アクティビティを終了する
            activity.finish();

        }

        // 結果を返す
        return result;

    }


    /**
     * 遷移先アクティビティへリクエストコードと
     * 遷移先アクティビティに対する各種情報を設定し、
     * パラメータを渡して画面遷移を行う。
     *
     * @param activity      遷移元アクティビティのインスタンス
     * @param destClass     遷移先アクティビティのクラス
     * @param requestCode   リクエストコード
     * @param action        遷移先アクティビティへ設定するIntentアクション情報
     * @param type          遷移先アクティビティへ設定するIntentタイプ情報
     * @param categories    遷移先アクティビティへ設定するIntentカテゴリ情報
     * @param data          遷移先アクティビティへ設定するIntentデータ情報
     * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
     * @param params        遷移先に渡すパラメータ
     * @return 画面遷移に成功したかどうか
     * @throws IllegalArgumentException 指定されたアクティビティがフレームワーク定義のインスタンス以外の場合
     * @throws IllegalStateException    アクティビティ状態が取得できなかった場合
     */
    public static boolean startActivityForResult(
            final Activity                      activity,
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final String                        action,
            final String                        type,
            final String[]                      categories,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        // アクティビティ状態を取得する
        final FRActivityStatus      status = getActivityStatus(activity);

        // 他アクティビティ起動中の場合
        if (status.isStarting()) {

            // 処理失敗
            return false;

        }


        // 他アクティビティ起動中の場合
        if (status.isStarting()) {

            // 処理失敗
            return false;

        }


        try {

            // 他アクティビティ起動中にする
            status.setStarting(true);

            // 画面遷移する
            activity.startActivityForResult(
                    FRIntent.createIntent(activity, destClass, action, type, categories, data, flags, params),
                    requestCode
                    );

            // 画面遷移成功
            return true;

        } catch (final Throwable e) {

            // 画面遷移失敗
            return false;

        }

    }


    /**
     * 指定した結果コードとインテントを呼び出し元へ返却してアクティビティを終了する。
     *
     * @param activity      アクティビティのインスタンス
     * @param resultCode    返却する結果コード
     * @param data          返却するインテント
     */
    public static void setResultAndFinish(
            final Activity  activity,
            final int       resultCode,
            final Intent    data
            ) {

        // アクティビティが null の場合は例外
        if (activity == null) {

            throw new IllegalArgumentException();

        }


        // 結果コードとインテントを設定する
        activity.setResult(resultCode, data);

        // アクティビティを終了する
        activity.finish();

    }


    /**
     * 指定した結果コードとパラメータを呼び出し元へ返却してアクティビティを終了する。
     *
     * @param activity      アクティビティのインスタンス
     * @param resultCode    返却する結果コード
     * @param params        返却するパラメータ
     */
    public static void setResultAndFinish(
            final Activity              activity,
            final int                   resultCode,
            final FRNameValuePair...    params
            ) {

        // アクティビティが null の場合は例外
        if (activity == null) {

            throw new IllegalArgumentException();

        }


        // 結果コードとパラメータを設定する
        activity.setResult(resultCode, FRIntent.createIntent(params));

        // アクティビティを終了する
        activity.finish();

    }


    /**
     * Bluetoothレシーバークラス。
     *
     * @author Kou
     *
     */
    static final class BluetoothReceiver extends BroadcastReceiver {


        /**
         * {@inheritDoc}
         */
        @Override
        public void onReceive(
                final Context   context,
                final Intent    intent
                ) {

            final BluetoothInfo     info   = new BluetoothInfo();                           // Bluetooth情報
            final BluetoothEvent    event  = BluetoothEvent.toEvent(intent.getAction());    // Bluetoothイベント


            // イベント変換出来なかった場合
            if (event == null) {

                // 変換できなかったアクションをログに出力
                Log.w(
                        getClass().getName(),
                        "No supported bluetooth action. [action = " + intent.getAction() + "]"
                        );

                // 処理なし
                return;

            }


            // Bluetoothイベント別処理
            switch (event) {

            // Bluetooth状態変更
            case ADAPTER_STATE_CHANGED:
            case INTENT_BLUETOOTH_STATE_CHANGED:

                // Bluetooth状態情報を設定する
                info.setState(
                        intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                        );
                info.setPreviousState(
                        intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, 0)
                        );
                break;


            // スキャンモード変更
            case ADAPTER_SCAN_MODE_CHANGED:
            case INTENT_SCAN_MODE_CHANGED:

                // スキャンモード情報を設定する
                info.setScanMode(
                        intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0)
                        );
                info.setPreviousScanMode(
                        intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, 0)
                        );
                break;


            // デバイス発見
            case DEVICE_FOUND:

                // Bluetoothデバイス情報とクラス情報を設定する
                info.setBluetoothDevice(
                        (BluetoothDevice)intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        );
                info.setBluetoothClass(
                        (BluetoothClass)intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS)
                        );
                break;


            // その他
            default:

                // 処理なし
                break;

            }


            // コンテキストがフレームワーク定義アクティビティの場合
            if (context instanceof FRActivityProcessable) {

                // Bluetooth結果処理を実行する
                ((FRActivityProcessable)context).onBluetoothResult(event, info);

            }

        }


    }




    /**
     * フレームワーク用内部処理例外ハンドラ抽象クラス。
     *
     * @author Kou
     *
     */
    private abstract static class FRUncatchExceptionHandlerInner implements UncaughtExceptionHandler {


        /**
         * デフォルト例外ハンドラ
         */
        private final UncaughtExceptionHandler      defaultHandler;


        /**
         * デフォルト例外ハンドラを指定して内部例外ハンドラを初期化する。
         *
         * @param handler デフォルト例外ハンドラ
         */
        public FRUncatchExceptionHandlerInner(
                final UncaughtExceptionHandler  handler
                ) {

            defaultHandler = handler;

        }


        /**
         * デフォルト例外ハンドラを取得する。
         *
         * @return デフォルト例外ハンドラ
         */
        public UncaughtExceptionHandler getDefaultHandler() {

            return defaultHandler;

        }


    }




    /**
     * フレームワーク定義アクティビティの共通動作定義のためのインターフェース。
     *
     * @author Kou
     *
     */
    interface FRActivityProcessable {


        /**
         * スクリーンを起動する。<br>
         * <br>
         * スリープ状態の場合、スクリーンを起動させる。<br>
         */
        void launchScreen();


        /**
         * アクティビティのハンドラを取得する。
         *
         * @return アクティビティのハンドラ
         */
        Handler getHandler();


        /**
         * 拡張インテントを取得する。
         *
         * @return 設定されている拡張インテント
         */
        FRIntent getIntentExtra();


        /**
         * デフォルト例外ハンドラを設定する。<br>
         * <br>
         * 本メソッドをサブクラスでオーバーライドし、使用する例外ハンドラを返却することで、<br>
         * 例外ハンドラを onCreate のタイミングで自動的に設定することが可能になる。<br>
         *
         * @return 設定するデフォルト例外ハンドラ
         */
        UncaughtExceptionHandler getUncaughtExceptionHandler();


        /**
         * Bluetooth結果処理を定義する。<br>
         * <br>
         * サブクラスでオーバーライドして処理を定義する。<br>
         *
         * @param event Bluetooth結果イベント
         * @param info  通知されたBluetooth結果イベント情報
         */
        void onBluetoothResult(
                final BluetoothEvent    event,
                final BluetoothInfo     info
                );


        /**
         * アクティビティ状態を取得する。
         *
         * @return アクティビティ状態
         */
        FRActivityStatus getStatus();


        /**
         * Bluetooth有効化要求を開始する。<br>
         * <br>
         * {@link #onBluetoothResult(BluetoothEvent, BluetoothInfo)} へ指定したリクエストコードで結果が返却される。<br>
         *
         * @return 有効化設定起動に成功した場合 または 既にBluetoothが有効の場合は true
         */
        boolean requestBluetoothEnabled();


        /**
         * Bluetoothデバイス発見可能状態変更を要求する。<br>
         * <br>
         * {@link #onBluetoothResult(BluetoothEvent, BluetoothInfo)} へ指定したリクエストコードで結果が返却される。<br>
         * <br>
         * 発見可能時間はデフォルトの 120 秒で行われる。<br>
         *
         * @return 有効化設定起動に成功した場合 または 既にBluetoothが有効の場合は true
         */
        boolean requestBluetoothDiscoverable();


        /**
         * Bluetoothデバイス発見可能状態変更を要求する。<br>
         * <br>
         * {@link #onBluetoothResult(BluetoothEvent, BluetoothInfo)} へ指定したリクエストコードで結果が返却される。<br>
         *
         * @param duration      発見可能時間 (0 - 300)
         * @return 有効化設定起動に成功した場合 または 既にBluetoothが有効の場合は true
         */
        boolean requestBluetoothDiscoverable(
                final int       duration
                );


        /**
         * Bluetoothデバイスを有効にする。
         *
         * @return Bluetoothデバイスの有効化に成功した場合は true
         */
        boolean enableBluetooth();


        /**
         * Bluetoothデバイスを無効にする。
         *
         * @return Bluetoothデバイスの無効化に成功した場合は true
         */
        boolean disableBluetooth();


        /**
         * Bluetoothデバイス発見を開始する。
         *
         * @return Bluetoothデバイス発見の開始に成功した場合は true
         */
        boolean startBluetoothDiscovery();


        /**
         * Bluetoothデバイス発見をキャンセルする。
         *
         * @return Bluetoothデバイス発見のキャンセルに成功した場合は true
         */
        boolean cancelBluetoothDiscovery();


        /**
         * 指定されたIntentの情報に基づいた画面を起動する。<br>
         *
         * @param intent 開始する画面情報を格納したIntent
         */
        void startActivity(
                final Intent intent
                );


        /**
         * 指定されたIntentの情報に基づいた画面を起動して結果待ち状態になる。
         *
         * @param intent        開始する画面情報を格納したIntent
         * @param requestCode   結果情報を判別するためのリクエストコード
         */
        void startActivityForResult(
                final Intent    intent,
                final int       requestCode
                );


        /**
         * 起動が必要であれば指定されたIntentの情報に基づいた画面を起動する。<br>
         * <br>
         * 本メソッドで起動した画面からの戻り値を onActivityResult メソッドで<br>
         * 受け取ることが可能である。<br>
         *
         * @param intent        開始する画面情報を格納したIntent
         * @param requestCode   onActivityResult メソッドで戻り値を判断するために利用するリクエストコード
         * @return 起動に成功した場合は true
         */
        boolean startActivityIfNeeded(
                final Intent    intent,
                final int       requestCode
                );


        /**
         * 指定されたIntentの画面情報に基づいた画面に置き換えて起動する。
         *
         * @param intent 開始する画面情報を格納したIntent
         * @return 起動に成功した場合は true
         */
        boolean startNextMatchingActivity(
                final Intent intent
                );


        /**
         * 子関係にあるアクティビティから指定されたIntentの画面情報に基づいた画面を起動する。<br>
         * <br>
         * 子アクティビティが startActivity, startActivityForResult を実行した場合に起動される。
         *
         * @param child         子アクティビティ
         * @param intent        開始する画面情報を格納したIntent
         * @param requestCode   onActivityResult メソッドで戻り値を判断するために利用するリクエストコード
         */
        void startActivityFromChild(
                final Activity  child,
                final Intent    intent,
                final int       requestCode
                );


        /**
         * 指定されたレシーバにブロードキャストを通知する。
         *
         * @param destClass 通知先レシーバ。不要な場合は null
         * @param flags     レシーバへ設定するIntentフラグ情報
         * @param params    レシーバへ渡すパラメータ
         * @return ブロードキャスト通知に成功した場合は true
         */
        boolean sendBroadcast(
                final Class<? extends BroadcastReceiver>    destClass,
                final int[]                                 flags,
                final FRNameValuePair...                    params
                );


        /**
         * 指定されたサービスを開始する。
         *
         * @param destClass 開始するサービス
         * @return 既に指定サービスが起動されている場合はそのサービスのコンポーネント名
         */
        ComponentName startService(
                final Class<? extends Service>  destClass
                );


        /**
         * 指定されたサービスを開始する。
         *
         * @param destClass 開始するサービス
         * @param flags     開始サービスへ設定するIntentフラグ情報
         * @param params    開始サービスに渡すパラメータ
         * @return 既に指定サービスが起動されている場合はそのサービスのコンポーネント名
         */
        ComponentName startService(
                final Class<? extends Service>  destClass,
                final int[]                     flags,
                final FRNameValuePair...        params
                );


        /**
         * 指定されたサービスを停止する。
         *
         * @param destClass 停止するサービス
         * @return サービスの停止が成功した場合は true
         */
        boolean stopService(
                final Class<? extends Service>  destClass
                );


        /**
         * 現在のアクティビティをスタックへ追加して画面遷移を行う。
         *
         * @param destClass 遷移先アクティビティ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>   destClass
                );


        /**
         * 遷移先アクティビティに対するフラグ情報を設定して画面遷移を行う。
         *
         * @param destClass 遷移先アクティビティ
         * @param flag      遷移先アクティビティへ設定するIntentフラグ情報
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final int                           flag
                );


        /**
         * 遷移先アクティビティに対するフラグ情報を設定して画面遷移を行う。
         *
         * @param destClass 遷移先アクティビティ
         * @param flags     遷移先アクティビティへ設定するIntentフラグ情報
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final int[]                         flags
                );


        /**
         * 遷移先アクティビティにパラメータを渡して画面遷移を行う。
         *
         * @param destClass 遷移先アクティビティ
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対するフラグ情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass 遷移先アクティビティ
         * @param flag      遷移先アクティビティへ設定するIntentフラグ情報
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final int                           flag,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対するフラグ情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass 遷移先アクティビティ
         * @param flags     遷移先アクティビティへ設定するIntentフラグ情報
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対するアクション情報とフラグ情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass 遷移先アクティビティ
         * @param action    遷移先アクティビティへ設定するIntentアクション情報
         * @param flag      遷移先アクティビティへ設定するIntentフラグ情報
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final int                           flag,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対するアクション情報とフラグ情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass 遷移先アクティビティ
         * @param action    遷移先アクティビティへ設定するIntentアクション情報
         * @param flags     遷移先アクティビティへ設定するIntentフラグ情報
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final String                        type,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param category      遷移先アクティビティへ設定するIntentカテゴリ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final String                        type,
                final String                        category,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param categories    遷移先アクティビティへ設定するIntentカテゴリ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivity(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final String                        type,
                final String[]                      categories,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティをスタックへ追加して画面遷移を行い、
         * 現在のアクティビティを終了する。
         *
         * @param destClass 遷移先アクティビティ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>   destClass
                );


        /**
         * 遷移先アクティビティに対するフラグ情報を設定して画面遷移を行い、
         * 現在のアクティビティを終了する。
         *
         * @param destClass 遷移先アクティビティ
         * @param flag      遷移先アクティビティへ設定するIntentフラグ情報
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final int                           flag
                );


        /**
         * 遷移先アクティビティに対するフラグ情報を設定して画面遷移を行い、
         * 現在のアクティビティを終了する。
         *
         * @param destClass 遷移先アクティビティ
         * @param flags     遷移先アクティビティへ設定するIntentフラグ情報
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final int[]                         flags
                );


        /**
         * 遷移先アクティビティにパラメータを渡して画面遷移を行い、
         * 現在のアクティビティを終了する。
         *
         * @param destClass 遷移先アクティビティ
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対するフラグ情報を設定し、
         * パラメータを渡して画面遷移を行った後に現在のアクティビティを終了する。
         *
         * @param destClass 遷移先アクティビティ
         * @param flag     遷移先アクティビティへ設定するIntentフラグ情報
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final int                           flag,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対するフラグ情報を設定し、
         * パラメータを渡して画面遷移を行った後に現在のアクティビティを終了する。
         *
         * @param destClass 遷移先アクティビティ
         * @param flags     遷移先アクティビティへ設定するIntentフラグ情報
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対するアクション情報とフラグ情報を設定し、
         * パラメータを渡して画面遷移を行った後に現在のアクティビティを終了する。
         *
         * @param destClass 遷移先アクティビティ
         * @param action    遷移先アクティビティへ設定するIntentアクション情報
         * @param flag      遷移先アクティビティへ設定するIntentフラグ情報
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final int                           flag,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対するアクション情報とフラグ情報を設定し、
         * パラメータを渡して画面遷移を行った後に現在のアクティビティを終了する。
         *
         * @param destClass 遷移先アクティビティ
         * @param action    遷移先アクティビティへ設定するIntentアクション情報
         * @param flags     遷移先アクティビティへ設定するIntentフラグ情報
         * @param params    遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行った後に現在のアクティビティを終了する。
         *
         * @param destClass     遷移先アクティビティ
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final String                        type,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行った後に現在のアクティビティを終了する。
         *
         * @param destClass     遷移先アクティビティ
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param category      遷移先アクティビティへ設定するIntentカテゴリ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final String                        type,
                final String                        category,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行った後に現在のアクティビティを終了する。
         *
         * @param destClass     遷移先アクティビティ
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param categories    遷移先アクティビティへ設定するIntentカテゴリ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityAndFinish(
                final Class<? extends Activity>     destClass,
                final String                        action,
                final String                        type,
                final String[]                      categories,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティへリクエストコードを設定して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対するフラグ情報を設定して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param flag          遷移先アクティビティへ設定するIntentフラグ情報
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final int                           flag
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対するフラグ情報を設定して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final int[]                         flags
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対するフラグ情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final int                           flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対するフラグ情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対するアクション情報を設定し、画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final String                        action
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対するアクション情報とフラグ情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param flag          遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final String                        action,
                final int                           flag,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対するアクション情報とフラグ情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final String                        action,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final String                        action,
                final String                        type,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param category      遷移先アクティビティへ設定するIntentカテゴリ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final String                        action,
                final String                        type,
                final String                        category,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 遷移先アクティビティへリクエストコードと
         * 遷移先アクティビティに対する各種情報を設定し、
         * パラメータを渡して画面遷移を行う。
         *
         * @param destClass     遷移先アクティビティ
         * @param requestCode   リクエストコード
         * @param action        遷移先アクティビティへ設定するIntentアクション情報
         * @param type          遷移先アクティビティへ設定するIntentタイプ情報
         * @param categories    遷移先アクティビティへ設定するIntentカテゴリ情報
         * @param data          遷移先アクティビティへ設定するIntentデータ情報
         * @param flags         遷移先アクティビティへ設定するIntentフラグ情報
         * @param params        遷移先に渡すパラメータ
         * @return 画面遷移に成功したかどうか
         */
        boolean startActivityForResult(
                final Class<? extends Activity>     destClass,
                final int                           requestCode,
                final String                        action,
                final String                        type,
                final String[]                      categories,
                final Uri                           data,
                final int[]                         flags,
                final FRNameValuePair...            params
                );


        /**
         * 指定した結果コードを呼び出し元へ返却してアクティビティを終了する。
         *
         * @param resultCode    返却する結果コード
         */
        void setResultAndFinish(
                final int       resultCode
                );


        /**
         * 指定した結果コードとインテントを呼び出し元へ返却してアクティビティを終了する。
         *
         * @param resultCode    返却する結果コード
         * @param data          返却するインテント
         */
        void setResultAndFinish(
                final int       resultCode,
                final Intent    data
                );


        /**
         * 指定した結果コードとパラメータを呼び出し元へ返却してアクティビティを終了する。
         *
         * @param resultCode    返却する結果コード
         * @param params        返却するパラメータ
         */
        void setResultAndFinish(
                final int                   resultCode,
                final FRNameValuePair...    params
                );


    }


}
