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

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;


/**
 * Bluetoothイベント情報。
 *
 * @author Kou
 *
 */
public class BluetoothInfo {


    /**
     * 変更後状態
     */
    private int                 state;

    /**
     * 変更前状態
     */
    private int                 previousState;

    /**
     * 変更後スキャンモード
     */
    private int                 scanMode;

    /**
     * 変更前スキャンモード
     */
    private int                 previousScanMode;

    /**
     * デバイス情報
     */
    private BluetoothDevice     device;

    /**
     * Bluetoothクラス情報
     */
    private BluetoothClass      bluetoothClass;




    /**
     * 変更後状態を取得する。
     *
     * @return 変更後状態
     */
    public int getState() {

        return state;

    }


    /**
     * 変更後状態を設定する。
     *
     * @param state 変更後状態
     */
    public void setState(final int state) {

        this.state = state;

    }


    /**
     * 変更前状態を取得する。
     *
     * @return 変更前状態
     */
    public int getPreviousState() {

        return previousState;

    }


    /**
     * 変更前状態を設定する。
     *
     * @param previousState 変更前状態
     */
    public void setPreviousState(final int previousState) {

        this.previousState = previousState;

    }


    /**
     * 変更後スキャンモードを取得する。
     *
     * @return 変更後スキャンモード
     */
    public int getScanMode() {

        return scanMode;

    }


    /**
     * 変更後スキャンモードを設定する。
     *
     * @param scanMode 変更後スキャンモード
     */
    public void setScanMode(final int scanMode) {

        this.scanMode = scanMode;

    }


    /**
     * 変更前スキャンモードを取得する。
     *
     * @return 変更前スキャンモード
     */
    public int getPreviousScanMode() {

        return previousScanMode;

    }


    /**
     * 変更前スキャンモードを設定する。
     *
     * @param previousScanMode 変更前スキャンモード
     */
    public void setPreviousScanMode(final int previousScanMode) {

        this.previousScanMode = previousScanMode;

    }


    /**
     * デバイス情報を取得する。
     *
     * @return デバイス情報
     */
    public BluetoothDevice getBluetoothDevice() {

        return device;

    }


    /**
     * デバイス情報を設定する。
     *
     * @param device デバイス情報
     */
    public void setBluetoothDevice(final BluetoothDevice device) {

        this.device = device;

    }


    /**
     * Bluetoothクラス情報を取得する。
     *
     * @return Bluetoothクラス情報
     */
    public BluetoothClass getBluetoothClass() {

        return bluetoothClass;

    }


    /**
     * Bluetoothクラス情報を設定する。
     *
     * @param bluetoothClass Bluetoothクラス情報
     */
    public void setBluetoothClass(final BluetoothClass bluetoothClass) {

        this.bluetoothClass = bluetoothClass;

    }


}
