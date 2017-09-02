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
package frontier.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;


/**
 * HTTP通信結果データ。
 *
 * @author Kou
 *
 */
public class HttpResult {


    /**
     * レスポンスヘッダ一覧
     */
    private final List<Header>          resultHeaders;

    /**
     * ステータスコード
     */
    private final int                   resultStatusCode;

    /**
     * レスポンスボディデータ
     */
    private final byte[]                resultBodyData;



    /**
     * HTTP通信結果データを初期化する。
     *
     * @param headers       レスポンスヘッダ
     * @param statusCode    ステータスコード
     * @param bodyData      レスポンスボディデータ
     */
    HttpResult(
            final Header[]  headers,
            final int       statusCode,
            final byte[]    bodyData
            ) {

        resultHeaders       = headers == null ? new ArrayList<Header>() : Arrays.asList(headers);
        resultStatusCode    = statusCode;
        resultBodyData      = bodyData;

    }


    /**
     * 指定されたレスポンスヘッダ名に対応するレスポンスヘッダを取得する。
     *
     * @param name  レスポンスヘッダ名
     * @return レスポンスヘッダ
     */
    public Header getHeader(
            final String    name
            ) {

        return HttpUtils.getHeader(resultHeaders, name);

    }


    /**
     * 全レスポンスヘッダを取得する。
     *
     * @return 全レスポンスヘッダ
     */
    public Header[] getAllHeaders() {

        return resultHeaders.toArray(new Header[]{});

    }


    /**
     * ステータスコードを取得する。
     *
     * @return ステータスコード
     */
    public int getStatusCode() {

        return resultStatusCode;

    }


    /**
     * レスポンスボディデータを取得する。
     *
     * @return レスポンスボディデータ
     */
    public byte[] getBodyData() {

        return resultBodyData == null ? null : resultBodyData.clone();

    }


}
