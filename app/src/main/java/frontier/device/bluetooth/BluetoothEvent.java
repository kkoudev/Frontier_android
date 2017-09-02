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
package frontier.device.bluetooth;

import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;


/**
 * Bluetoothイベント列挙型。
 *
 * @author Kou
 *
 */
public enum BluetoothEvent {


    /**
     * デバイス : 発見
     */
    DEVICE_FOUND(BluetoothDevice.ACTION_FOUND),

    /**
     * デバイス : クラス変更
     */
    DEVICE_CLASS_CHANGED(BluetoothDevice.ACTION_CLASS_CHANGED),

    /**
     * デバイス : ACLリンク接続
     */
    DEVICE_ACL_CONNECTED(BluetoothDevice.ACTION_ACL_CONNECTED),

    /**
     * デバイス : ACLリンク切断要求
     */
    DEVICE_ACL_DISCONNECT_REQUESTED(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED),

    /**
     * デバイス : ACLリンク切断
     */
    DEVICE_ACL_DISCONNECTED(BluetoothDevice.ACTION_ACL_DISCONNECTED),

    /**
     * デバイス : 名称変更
     */
    DEVICE_NAME_CHANGED(BluetoothDevice.ACTION_NAME_CHANGED),

    /**
     * デバイス : ボンディング状態変更
     */
    DEVICE_BOND_STATE_CHAGED(BluetoothDevice.ACTION_BOND_STATE_CHANGED),



    /**
     * アダプタ : デバイス発見開始
     */
    ADAPTER_DISCOVERY_STARTED(BluetoothAdapter.ACTION_DISCOVERY_STARTED),

    /**
     * アダプタ : デバイス発見終了
     */
    ADAPTER_DISCOVERY_FINISHED(BluetoothAdapter.ACTION_DISCOVERY_FINISHED),

    /**
     * アダプタ : ローカル名変更
     */
    ADAPTER_LOCAL_NAME_CHANGED(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED),

    /**
     * アダプタ : 発見可能状態要求
     */
    ADAPTER_REQUEST_DISCOVERABLE(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE),

    /**
     * アダプタ : 有効化要求
     */
    ADAPTER_REQUEST_ENABLE(BluetoothAdapter.ACTION_REQUEST_ENABLE),

    /**
     * アダプタ : スキャンモード変更
     */
    ADAPTER_SCAN_MODE_CHANGED(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED),

    /**
     * アダプタ : Bluetooth状態変更
     */
    ADAPTER_STATE_CHANGED(BluetoothAdapter.ACTION_STATE_CHANGED),



    /**
     * インテント : デバイス発見完了
     */
    INTENT_DISCOVERY_COMPLETED(
            "android.bluetooth.intent.action.DISCOVERY_COMPLETED"
            ),

    /**
     * インテント : デバイス発見開始
     */
    INTENT_DISCOVERY_STARTED(
            "android.bluetooth.intent.action.DISCOVERY_STARTED"
            ),

    /**
     * インテント : Bluetooth状態変更
     */
    INTENT_BLUETOOTH_STATE_CHANGED(
            "android.bluetooth.intent.action.BLUETOOTH_STATE_CHANGED"
            ),

    /**
     * インテント : 名前変更
     */
    INTENT_NAME_CHANGED(
            "android.bluetooth.intent.action.NAME_CHANGED"
            ),

    /**
     * インテント : スキャンモード変更
     */
    INTENT_SCAN_MODE_CHANGED(
            "android.bluetooth.intent.action.SCAN_MODE_CHANGED"
            ),

    /**
     * インテント : ペアリングリクエスト
     */
    INTENT_PAIRING_REQUEST(
            "android.bluetooth.intent.action.PAIRING_REQUEST"
            ),

    /**
     * インテント : ペアリングキャンセル
     */
    INTENT_PAIRING_CANCEL(
            "android.bluetooth.intent.action.PAIRING_CANCEL"
            ),

    /**
     * インテント : リモートデバイス発見
     */
    INTENT_REMOTE_DEVICE_FOUND(
            "android.bluetooth.intent.action.REMOTE_DEVICE_FOUND"
            ),

    /**
     * インテント : リモートデバイス消失
     */
    INTENT_REMOTE_DEVICE_DISAPPEARED(
            "android.bluetooth.intent.action.REMOTE_DEVICE_DISAPPEARED"
            ),

    /**
     * インテント : リモートデバイスクラス更新
     */
    INTENT_REMOTE_DEVICE_CLASS_UPDATED(
            "android.bluetooth.intent.action.REMOTE_DEVICE_CLASS_UPDATED"
            ),

    /**
     * インテント : リモートデバイス接続
     */
    INTENT_REMOTE_DEVICE_CONNECTED(
            "android.bluetooth.intent.action.REMOTE_DEVICE_CONNECTED"
            ),

    /**
     * インテント : リモートデバイス切断要求
     */
    INTENT_REMOTE_DEVICE_DISCONNECT_REQUESTED(
            "android.bluetooth.intent.action.REMOTE_DEVICE_DISCONNECT_REQUESTED"
            ),

    /**
     * インテント : リモートデバイス切断
     */
    INTENT_REMOTE_DEVICE_DISCONNECTED(
            "android.bluetooth.intent.action.REMOTE_DEVICE_DISCONNECTED"
            ),

    /**
     * インテント : リモート名称更新
     */
    INTENT_REMOTE_NAME_UPDATED(
            "android.bluetooth.intent.action.REMOTE_NAME_UPDATED"
            ),

    /**
     * インテント : リモート名称失敗
     */
    INTENT_REMOTE_NAME_FAILED(
            "android.bluetooth.intent.action.REMOTE_NAME_FAILED"
            ),

    /**
     * インテント : ボンディング状態変更
     */
    INTENT_BOND_STATE_CHANGED(
            "android.bluetooth.intent.action.BOND_STATE_CHANGED"
            ),

    /**
     * インテント : ヘッドセット状態返納
     */
    INTENT_HEADSET_STATE_CHANGED(
            "android.bluetooth.intent.action.HEADSET_STATE_CHANGED"
            ),

    /**
     * インテント : ヘッドセットオーディオ状態変更
     */
    INTENT_HEADSET_AUDIO_STATE_CHANGED(
            "android.bluetooth.intent.action.HEADSET_ADUIO_STATE_CHANGED"
            );




    /**
     * Bluetoothイベント変換テーブル<br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>キー</td><td>String</td><td>インテントアクション</td>
     * </tr>
     * <tr>
     *   <td>値</td><td>BluetoothEvent</td><td>Bluetoothイベント種別</td>
     * </tr>
     * </table>
     */
    private static final Map<String, BluetoothEvent>    EVENT_TABLE =
        new HashMap<String, BluetoothEvent>();


    /**
     * インテントアクション
     */
    private final String    action;



    /**
     * イベントテーブルを初期化する
     */
    static {

        // 全イベント分処理をする
        for (final BluetoothEvent event : BluetoothEvent.values()) {

            // イベントテーブルへ追加する
            EVENT_TABLE.put(event.getAction(), event);

        }

    }


    /**
     * インテントアクションを設定してBluetoothイベントを初期化する。
     *
     * @param action インテントアクション
     */
    private BluetoothEvent(
            final String action
            ) {

        // インテントアクションを設定する
        this.action = action;

    }


    /**
     * インテントアクションを取得する。
     *
     * @return インテントアクション
     */
    public String getAction() {

        return action;

    }


    /**
     * 指定されたインテントアクションに対応するBluetoothイベントへ変換する。
     *
     * @param action インテントアクション
     * @return 指定されたインテントアクションに対応するBluetoothイベント
     */
    public static BluetoothEvent toEvent(
            final String    action
            ) {

        return EVENT_TABLE.get(action);

    }


}
