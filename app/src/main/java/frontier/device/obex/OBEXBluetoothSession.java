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
package frontier.device.obex;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.bluetooth.BluetoothSocket;
import frontier.util.IOUtils;


/**
 * Bluetooth用OBEX通信セッション。
 *
 * @author Kou
 *
 */
public class OBEXBluetoothSession extends OBEXSession {


    /**
     * 元となるBluetoothソケット
     */
    private final BluetoothSocket         obexSocket;



    /**
     * Bluetooth用OBEX通信セッションを初期化する。
     *
     * @param socket 元となるBluetoothソケット
     */
    OBEXBluetoothSession(
            final BluetoothSocket   socket
            ) {

        obexSocket = socket;

    }


    /**
     * {@inheritDoc}
     */
    public void close() {

        // ソケットを閉じる
        IOUtils.closeQuietly(obexSocket);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean prepareConnect() {

        try {

            // 接続を開始する
            obexSocket.connect();

            // 接続準備成功
            return true;

        } catch (final IOException e) {

            e.printStackTrace();

            // 接続準備失敗
            return false;

        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected DataInputStream getInputStream() throws IOException {

        return new DataInputStream(obexSocket.getInputStream());

    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected DataOutputStream getOutputStream() throws IOException {

        return new DataOutputStream(obexSocket.getOutputStream());

    }


}
