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

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import frontier.app.FRActivityUtils.FRActivityProcessable;
import frontier.device.bluetooth.BluetoothEvent;
import frontier.device.bluetooth.BluetoothInfo;


/**
 * 機能拡張アクティビティのスーパークラス。
 *
 * @author Kou
 *
 */
public abstract class FRActivity extends Activity implements FRActivityProcessable {


    /**
     * アクティビティ状態
     */
    private final FRActivityStatus  activityStatus = new FRActivityStatus();



    /**
     * 各種クラスをロードする
     */
    static {

        // システムで利用するクラスをロードする
        FRClassInitializer.loadClasses();

    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void launchScreen() {

        FRActivityUtils.launchScreen(this);

    }


    /**
     * アクティビティのハンドラを取得する。
     *
     * @return アクティビティのハンドラ
     */
    @Override
    public Handler getHandler() {

        return FRActivityUtils.getHandler(this);

    }


    /**
     * 拡張インテントを取得する。
     *
     * @return 設定されている拡張インテント
     */
    @Override
    public FRIntent getIntentExtra() {

        return FRActivityUtils.getIntentExtra(this);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public FRActivityStatus getStatus() {

        return activityStatus;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {

        // 設定なし
        return null;

    }


    /**
     * アクティビティの作成の初期処理を行う。<br>
     * <br>
     * アクティビティ作成時における初期化処理を行う。<br>
     *
     * @param savedInstanceState {@link #onSaveInstanceState(Bundle)} メソッドで保存されたインスタンスデータ
     */
    @Override
    protected void onCreate(
            final Bundle savedInstanceState
            ) {

        // スーパークラスの処理を実行する
        super.onCreate(savedInstanceState);

        // アクティビティ作成処理を実行する
        FRActivityUtils.onCreate(this, savedInstanceState);

    }


    /**
     * アクティビティのレジューム処理を行う。<br>
     * <br>
     * 本メソッドをオーバーライドすることで、<br>
     * アクティビティがアクティブ状態になったときに実行される処理を定義することが可能である。<br>
     * その場合、必ずスーパークラスの処理を実行すること。<br>
     * (スーパークラスをコールしない場合、例外が発生する)<br>
     */
    @Override
    protected void onResume() {

        super.onResume();

        // アクティビティレジューム処理を実行する
        FRActivityUtils.onResume(this);

    }


    /**
     * アクティビティ破棄処理を実行する。<br>
     * <br>
     * 本メソッドをオーバーライドすることで、<br>
     * アクティビティが破棄されるときに実行される処理を定義することが可能である。<br>
     * その場合、必ずスーパークラスの処理を実行すること。<br>
     * (スーパークラスをコールしない場合、例外が発生する)<br>
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();

        // アクティビティ破棄処理を実行する
        FRActivityUtils.onDestroy(this);

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
     * @param outState 自動保存するメンバ変数の保存先 Bundle データ
     */
    @Override
    protected void onSaveInstanceState(
            final Bundle outState
            ) {

        // アクティビティのデータを保存する
        FRActivityUtils.onSaveInstanceState(this, outState);

        // スーパークラスの処理を実行する
        super.onSaveInstanceState(outState);

    }


    /**
     * メンバ変数の自動復帰処理を行う。<br>
     * <br>
     * {@link #onSaveInstanceState(Bundle)} メソッドで自動保存されたメンバ変数の<br>
     * 自動復帰処理を行う。<br>
     *
     * @param savedInstanceState 自動保存されたメンバ変数を格納した Bundle データ
     */
    @Override
    protected void onRestoreInstanceState(
            final Bundle savedInstanceState
            ) {

        // アクティビティのデータを復帰する
        FRActivityUtils.onRestoreInstanceState(this, savedInstanceState);

        // スーパークラスの処理を実行する
        super.onRestoreInstanceState(savedInstanceState);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onBluetoothResult(
            final BluetoothEvent    event,
            final BluetoothInfo     info
            ) {

        // 処理なし

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requestBluetoothEnabled() {

        return FRActivityUtils.requestBluetoothEnabled(this);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requestBluetoothDiscoverable() {

        return FRActivityUtils.requestBluetoothDiscoverable(this);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean requestBluetoothDiscoverable(
            final int   duration
            ) {

        return FRActivityUtils.requestBluetoothDiscoverable(this, duration);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean enableBluetooth() {

        return FRActivityUtils.enableBluetooth(this);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean disableBluetooth() {

        return FRActivityUtils.disableBluetooth(this);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startBluetoothDiscovery() {

        return FRActivityUtils.startBluetoothDiscovery(this);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cancelBluetoothDiscovery() {

        return FRActivityUtils.cancelBluetoothDiscovery();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startActivity(
            final Intent intent
            ) {

        // アクティビティ起動準備を行う
        FRActivityUtils.prepareStartingActivity(this);

        // アクティビティを起動する
        super.startActivity(intent);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startActivityForResult(
            final Intent    intent,
            final int       requestCode
            ) {

        // アクティビティ起動準備を行う
        FRActivityUtils.prepareStartingActivity(this);

        // アクティビティを起動する
        super.startActivityForResult(intent, requestCode);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityIfNeeded(
            final Intent    intent,
            final int       requestCode
            ) {

        // アクティビティ起動準備を行う
        FRActivityUtils.prepareStartingActivity(this);

        // アクティビティを起動する
        return super.startActivityIfNeeded(intent, requestCode);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startNextMatchingActivity(
            final Intent intent
            ) {

        // アクティビティ起動準備を行う
        FRActivityUtils.prepareStartingActivity(this);

        // アクティビティを起動する
        return super.startNextMatchingActivity(intent);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startActivityFromChild(
            final Activity  child,
            final Intent    intent,
            final int       requestCode
            ) {

        // アクティビティ起動準備を行う
        FRActivityUtils.prepareStartingActivity(this);

        // アクティビティを起動する
        super.startActivityFromChild(child, intent, requestCode);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sendBroadcast(
            final Class<? extends BroadcastReceiver>    destClass,
            final int[]                                 flags,
            final FRNameValuePair...                    params
            ) {

        // アクティビティのインスタンスがフレームワーク定義のインスタンスかどうかをチェックする
        FRActivityUtils.checkActivityInstance(this);

        try {

            // ブロードキャストレシーバを起動する
            super.sendBroadcast(
                    FRIntent.createIntent(this, destClass, null, flags, params)
                    );

            // 起動成功
            return true;

        } catch (final Throwable e) {

            e.printStackTrace();

            // 起動失敗
            return false;

        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ComponentName startService(
            final Class<? extends Service>  destClass
            ) {

        return startService(destClass, null, (FRNameValuePair[])null);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ComponentName startService(
            final Class<? extends Service>  destClass,
            final int[]                     flags,
            final FRNameValuePair...        params
            ) {

        // 指定されたサービスを起動する
        return super.startService(
                FRIntent.createIntent(this, destClass, params)
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopService(
            final Class<? extends Service>  destClass
            ) {

        // アクティビティのインスタンスがフレームワーク定義のインスタンスかどうかをチェックする
        FRActivityUtils.checkActivityInstance(this);

        // サービスを停止する
        return super.stopService(FRIntent.createIntent(this, destClass));

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>   destClass
            ) {

        return startActivity(destClass, (int[])null);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final int                           flag
            ) {

        return startActivity(destClass, new int[] {flag});

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final int[]                         flags
            ) {

        return startActivity(destClass, flags, (FRNameValuePair[])null);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final FRNameValuePair...            params
            ) {

        return startActivity(destClass, null, params);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final int                           flag,
            final FRNameValuePair...            params
            ) {

        return startActivity(destClass, new int[] {flag}, params);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivity(
                destClass,
                null,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final int                           flag,
            final FRNameValuePair...            params
            ) {

        return startActivity(
                destClass,
                action,
                new int[] {flag},
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivity(destClass, action, null, (String[])null, null, flags, params);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final String                        type,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivity(
                destClass,
                action,
                type,
                (String[])null,
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final String                        type,
            final String                        category,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivity(
                destClass,
                action,
                type,
                new String[] {category},
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivity(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final String                        type,
            final String[]                      categories,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return FRActivityUtils.startActivity(
                this,
                destClass,
                action,
                type,
                categories,
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>   destClass
            ) {

        return startActivity(destClass, (int[])null);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final int                           flag
            ) {

        return startActivityAndFinish(destClass, new int[] {flag});

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final int[]                         flags
            ) {

        return startActivityAndFinish(destClass, flags, (FRNameValuePair[])null);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final FRNameValuePair...            params
            ) {

        return startActivityAndFinish(destClass, null, params);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final int                           flag,
            final FRNameValuePair...            params
            ) {

        return startActivityAndFinish(destClass, new int[] {flag}, params);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivityAndFinish(
                destClass,
                null,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final int                           flag,
            final FRNameValuePair...            params
            ) {

        return startActivityAndFinish(
                destClass,
                action,
                new int[] {flag},
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivityAndFinish(destClass, action, null, (String[])null, null, flags, params);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final String                        type,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivityAndFinish(
                destClass,
                action,
                type,
                (String[])null,
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final String                        type,
            final String                        category,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivityAndFinish(
                destClass,
                action,
                type,
                new String[] {category},
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityAndFinish(
            final Class<? extends Activity>     destClass,
            final String                        action,
            final String                        type,
            final String[]                      categories,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return FRActivityUtils.startActivityAndFinish(
                this,
                destClass,
                action,
                type,
                categories,
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode
            ) {

        return startActivityForResult(destClass, requestCode, (int[])null);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final int                           flag
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                new int[] {flag},
                (FRNameValuePair[])null
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final int[]                         flags
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                flags,
                (FRNameValuePair[])null
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final FRNameValuePair...            params
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                null,
                null,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final int                           flags,
            final FRNameValuePair...            params
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                null,
                new int[] {flags},
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                null,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final String                        action
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                action,
                (int[])null,
                (FRNameValuePair[])null
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final String                        action,
            final int                           flag,
            final FRNameValuePair...            params
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                action,
                new int[] {flag},
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final String                        action,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                action,
                null,
                (String[])null,
                null,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final String                        action,
            final String                        type,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                action,
                type,
                (String[])null,
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final String                        action,
            final String                        type,
            final String                        category,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return startActivityForResult(
                destClass,
                requestCode,
                action,
                type,
                new String[] {category},
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startActivityForResult(
            final Class<? extends Activity>     destClass,
            final int                           requestCode,
            final String                        action,
            final String                        type,
            final String[]                      categories,
            final Uri                           data,
            final int[]                         flags,
            final FRNameValuePair...            params
            ) {

        return FRActivityUtils.startActivityForResult(
                this,
                destClass,
                requestCode,
                action,
                type,
                categories,
                data,
                flags,
                params
                );

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setResultAndFinish(
            final int       resultCode
            ) {

        setResultAndFinish(resultCode, (Intent)null);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setResultAndFinish(
            final int       resultCode,
            final Intent    data
            ) {

        FRActivityUtils.setResultAndFinish(this, resultCode, data);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setResultAndFinish(
            final int                   resultCode,
            final FRNameValuePair...    params
            ) {

        FRActivityUtils.setResultAndFinish(this, resultCode, params);

    }


}
