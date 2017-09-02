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

import android.bluetooth.BluetoothAdapter;


/**
 * デバイス情報アクセス用ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class DeviceUtils {


    /**
     * インスタンス生成防止。
     *
     */
    private DeviceUtils() {

        // 処理なし

    }


    /**
     * Bluetoothを端末がサポートしているかどうかを取得する。
     *
     * @return Bluetoothを端末がサポートしている場合は true
     */
    public static boolean isSupportedBluetooth() {

        return BluetoothAdapter.getDefaultAdapter() != null;

    }


    /**
     * Bluetoothが有効化されているかどうかを取得する。
     *
     * @return Bluetoothが有効化されている場合は true
     */
    public static boolean isEnabledBluetooth() {

        // アダプタを取得する
        final BluetoothAdapter  adapter = BluetoothAdapter.getDefaultAdapter();

        // アダプタが取得できなかった場合
        if (adapter == null) {

            // 失敗
            return false;

        }

        // Bluetoothが有効化されているかどうかを返す
        return adapter.isEnabled();

    }


}
