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

import frontier.util.GeneralUtils;



/**
 * OBEXレスポンスデータ。
 *
 * @author Kou
 *
 */
public class OBEXResponse {


    /**
     * レスポンス結果ブロックサイズ
     */
    static final int                SIZE_RESPONSE_BLOCK_RESULT  =
        GeneralUtils.SIZE_BYTE + GeneralUtils.SIZE_SHORT;

    /**
     * レスポンスコード
     */
    private final OBEXResponseCode      resCode;

    /**
     * 返却データ
     */
    private final byte[]                resContent;



    /**
     * レスポンスデータを作成する。
     *
     * @param code      レスポンスコード
     * @param content   レスポンスコンテントデータ
     */
    OBEXResponse(
            final OBEXResponseCode  code,
            final byte[]            content
            ) {

        resCode    = code;
        resContent = content == null ? new byte[0] : content;

    }


    /**
     * レスポンスコードを取得する。
     *
     * @return レスポンスコード
     */
    public OBEXResponseCode getCode() {

        return resCode;

    }


    /**
     * レスポンスコンテントデータを取得する。
     *
     * @return レスポンスコンテントデータ
     */
    public byte[] getContent() {

        return resContent.clone();

    }


}
