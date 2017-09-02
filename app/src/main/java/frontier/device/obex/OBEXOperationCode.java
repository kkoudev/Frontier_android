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


/**
 * OBEXオペレーションコード列挙型。<br>
 * <br>
 * OBEXの仕様では、最後に送信するオペレーションコードの最上位ビットを 1 にする決まりがある。<br>
 * そのため、単体で送信するオペレーションは全て 0x80 以降の値となっている。<br>
 *
 * @author Kou
 *
 */
public enum OBEXOperationCode {


    /**
     * 接続
     */
    CONNECT(0x80),

    /**
     * 切断
     */
    DISCONNECT(0x81),

    /**
     * アイドル
     */
    IDLE(0x01),

    /**
     * オブジェクトデータ送信
     */
    PUT(0x02),

    /**
     * 最後のオブジェクトデータ送信
     */
    FINAL_PUT(0x82),

    /**
     * オブジェクトデータ取得
     */
    GET(0x83),

    /**
     * 受信側のカレントパス設定
     */
    SET_PATH(0x85),

    /**
     * セッション利用
     */
    SESSION(0x87),

    /**
     * 中断
     */
    ABORT(0xFF);



    /**
     * オペレーションコード値
     */
    private final byte      code;


    /**
     * OBEXオペレーションコードを初期化する。
     *
     * @param code 設定するオペレーションコード値
     */
    private OBEXOperationCode(
            final int  code
            ) {

        this.code = (byte)code;

    }


    /**
     * オペレーションコードを取得する。
     *
     * @return オペレーションコード
     */
    public byte getCode() {

        return code;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        return String.format("0x%x", code);

    }


}
