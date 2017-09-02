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

import java.io.Serializable;

import frontier.app.FRActivityUtils.BluetoothReceiver;



/**
 * 起動中のアクティビティ状態。
 *
 * @author Kou
 *
 */
public class FRActivityStatus implements Serializable {


    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1535850575639030425L;


    /**
     * 他アクティビティ起動中状態
     */
    private boolean                         activityStarting;

    /**
     * Bluetoothレシーバー
     */
    private transient BluetoothReceiver     activityBluetoothReceiver;




    /**
     * 他アクティビティ起動中状態を設定する。
     *
     * @param starting 他アクティビティ起動中の場合は true
     */
    void setStarting(
            final boolean   starting
            ) {

        activityStarting = starting;

    }


    /**
     * 他アクティビティ起動中状態を取得する。
     *
     * @return 他アクティビティ起動中状態
     */
    public boolean isStarting() {

        return activityStarting;

    }


    /**
     * Bluetoothレシーバーを設定する。
     *
     * @param receiver 設定するBluetoothレシーバー
     */
    void setBluetoothReceiver(
            final BluetoothReceiver receiver
            ) {

        activityBluetoothReceiver = receiver;

    }


    /**
     * Bluetoothレシーバーを取得する。
     *
     * @return Bluetoothレシーバー
     */
    BluetoothReceiver getBluetoothReceiver() {

        return activityBluetoothReceiver;

    }


}
