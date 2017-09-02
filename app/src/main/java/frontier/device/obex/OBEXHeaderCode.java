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
 * OBEXヘッダ種別列挙型。<br>
 *
 * @author Kou
 *
 */
public enum OBEXHeaderCode {


    /**
     * オブジェクト数
     */
    COUNT(0xC0),

    /**
     * オブジェクト名 (ファイル名)
     */
    NAME(0x01),

    /**
     * オブジェクト種別
     */
    TYPE(0x42),

    /**
     * オブジェクトバイト長
     */
    LENGTH(0xC3),

    /**
     * ISO 8601形式の日時
     */
    TIME(0x44),

    /**
     * 4桁形式の日時
     */
    TIME4(0xC4),

    /**
     * オブジェクト説明
     */
    DESCRIPTION(0x05),

    /**
     * ターゲットサービス名
     */
    TARGET(0x46),

    /**
     * HTTP 1.x ヘッダ
     */
    HTTP(0x47),

    /**
     * オブジェクトチャンクボディデータ
     */
    BODY(0x48),

    /**
     * オブジェクト終端ボディデータ
     */
    END_OF_BODY(0x49),

    /**
     * ピアトーキングアプリケーションで利用されていた識別子
     */
    WHO(0x4A),

    /**
     * 接続セッションID
     */
    SESSION_ID(0xCB),

    /**
     * アプリケーション定義パラメータ
     */
    APP_PARAMETERS(0x4C),

    /**
     * 認証チャレンジコード
     */
    AUTH_CHALLENGE(0x4D),

    /**
     * 認証レスポンスコード
     */
    AUTH_RESPONSE(0x4E),

    /**
     * 作成オブジェクトID指定
     */
    CREATOR_ID(0xCF),

    /**
     * WAN UUID
     */
    WAN_UUID(0x50),

    /**
     * オブジェクトのクラス
     */
    OBJECT_CLASS(0x51),

    /**
     * セッションパラメータ
     */
    SESSION_PARAM(0x52),

    /**
     * 連続するセッション数
     */
    SESSION_SEQUENCE_COUNT(0x93);



    /**
     * ヘッダコード値
     */
    private final byte      code;


    /**
     * OBEXヘッダコードを初期化する。
     *
     * @param code 設定するヘッダコード値
     */
    private OBEXHeaderCode(
            final int  code
            ) {

        this.code = (byte)code;

    }


    /**
     * ヘッダコードを取得する。
     *
     * @return ヘッダコード
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
