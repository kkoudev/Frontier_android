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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import frontier.util.StringUtils;


/**
 * 通信ユーティリティクラス。
 *
 * @author Kou
 *
 */
public final class HttpUtils {


    /**
     * クエリパラメータの項目名と値の区切り文字
     */
    private static final String         TOKEN_QUERY_VALUE   = "=";

    /**
     * クエリパラメータの連結文字
     */
    private static final String         TOKEN_QUERY_PARAMS  = "&";

    /**
     * URLとクエリ文字列の連結文字
     */
    private static final String         TOKEN_URL_QUERY     = "?";



    /**
     * インスタンス生成防止。
     *
     */
    private HttpUtils() {

        // 処理なし

    }


    /**
     * URI文字列を作成する。
     *
     * @param baseURI       ベースとなるURI文字列
     * @param queryParams   URIへ付加するクエリパラメータ。不要な場合は null を指定する
     * @return 作成したURI文字列
     */
    public static String createURI(
            final String                baseURI,
            final NameValuePair[]       queryParams
            ) {

        // nullの場合は例外
        if (baseURI == null) {

            throw new IllegalArgumentException();

        }

        // クエリパラメータが不要な場合
        if (queryParams == null) {

            // そのまま返却する
            return baseURI;

        }

        // クエリパラメータを付加して返却する
        return baseURI + TOKEN_URL_QUERY + createQueryString(queryParams);

    }


    /**
     * URIからクエリ文字列を取得する。
     *
     * @param uri 解析するURI
     * @return クエリ文字列
     */
    public static String getQueryStringFromURI(
            final String    uri
            ) {

        // nullの場合は例外
        if (uri == null) {

            throw new IllegalArgumentException();

        }


        // クエリ文字列トークンを検索する
        final int   index = uri.indexOf(TOKEN_URL_QUERY);

        // クエリ文字列が含まれている場合
        if ((0 <= index) && (index < uri.length() - 1)) {

            // クエリ文字列を返す
            return uri.substring(index + 1);

        }

        // 空文字を返す
        return "";

    }


    /**
     * クエリ文字列を除いたURIを取得する。
     *
     * @param uri 解析するURI
     * @return クエリ文字列を除いたURI
     */
    public static String getIgnoreQueryStringURI(
            final String    uri
            ) {

        // nullの場合は例外
        if (uri == null) {

            throw new IllegalArgumentException();

        }


        // クエリ文字列トークンを検索する
        final int   index = uri.indexOf(TOKEN_URL_QUERY);

        // クエリ文字列が含まれている場合
        if (index != -1) {

            // クエリ文字列除いたURIを返す
            return uri.substring(0, index);

        }

        // そのままURIを返す
        return uri;

    }


    /**
     * クエリ文字列を作成する。
     *
     * @param queryParams  クエリ文字列として作成するパラメータ項目値の配列
     * @return 作成したクエリ文字列
     */
    public static String createQueryString(
            final NameValuePair[]   queryParams
            ) {

        // nullの場合は例外
        if (queryParams == null) {

            throw new IllegalArgumentException();

        }


        // 結合文字列を作成する
        final StringBuffer    urlBuf = new StringBuffer();

        // 値と項目のペアを結合する
        for (int i = 0; i < queryParams.length; i++) {

            final NameValuePair      nameValue = queryParams[i];

            // 文字列表現を取得し、追加する
            urlBuf.append(URLEncoder.encode(nameValue.getName()));
            urlBuf.append(TOKEN_QUERY_VALUE);
            urlBuf.append(URLEncoder.encode(nameValue.getValue()));

            // 最後の要素以外の場合
            if (i < queryParams.length - 1) {

                // 連結文字を付加
                urlBuf.append(TOKEN_QUERY_PARAMS);

            }

        }

        // 結合結果を返す
        return urlBuf.toString();

    }


    /**
     * クエリ文字列からクエリパラメータを取得する。
     *
     * @param queryString パラメータを取得するクエリ文字列
     * @return 取得したクエリパラメータ
     */
    public static List<NameValuePair> getQueryParams(
            final String    queryString
            ) {

        // nullの場合は例外
        if (queryString == null) {

            throw new IllegalArgumentException();

        }


        final String[]              params      = StringUtils.split(queryString, TOKEN_QUERY_PARAMS, true); // パラメータ一覧
        final List<NameValuePair>   retParams   = new ArrayList<NameValuePair>();                           // 返却パラメータ一覧
        final int                   indexName   = 0;                                                        // 名前のインデックス
        final int                   indexValue  = 1;                                                        // 値のインデックス
        final int                   indexCount  = 2;                                                        // インデックス数


        // クエリ文字列からパラメータを作成する
        for (int i = 0; i < params.length; i++) {

            final String[]  nameValue = StringUtils.split(params[i], TOKEN_QUERY_VALUE);

            // インデックス数が一致しない場合
            if (nameValue.length != indexCount) {

                // 次のパラメータへ
                continue;

            }

            // パラメータを作成して代入する
            retParams.add(
                    new BasicNameValuePair(
                            nameValue[indexName],
                            nameValue[indexValue])
                    );

        }

        // 作成したパラメータを返却する
        return retParams;

    }


    /**
     * NameValuePairリストから指定項目名の値を取得する。
     *
     * @param params    検索先パラメータ一覧
     * @param name      項目名
     * @return 指定項目の値
     */
    public static NameValuePair getParameter(
            final List<NameValuePair>       params,
            final String                    name
            ) {

        // パラメータまたは項目名が null の場合は例外
        if ((params == null) || (name == null)) {

            throw new IllegalArgumentException();

        }

        // 指定項目名を検索
        for (final NameValuePair param : params) {

            // 見つかったらその値を返す
            if (param.getName().equals(name)) {

                // ヘッダを返す
                return param;

            }

        }

        // nullを返す
        return null;

    }


    /**
     * Headerリストから指定項目名の値を取得する。
     *
     * @param headers    検索先パラメータ一覧
     * @param name      項目名
     * @return 指定項目の値
     */
    public static Header getHeader(
            final List<Header>  headers,
            final String        name
            ) {

        // パラメータまたは項目名が null の場合は例外
        if ((headers == null) || (name == null)) {

            throw new IllegalArgumentException();

        }

        // 指定項目名を検索
        for (final Header header : headers) {

            // 見つかったらその値を返す
            if (header.getName().equals(name)) {

                // ヘッダを返す
                return header;

            }

        }

        // nullを返す
        return null;

    }


    /**
     * ヘッダの名称と値をマップテーブルに変換する。
     *
     * @param headers   変換するヘッダ一覧
     * @return 変換したマップテーブル
     */
    public static Map<String, String> createHeaderTable(
            final Header[]      headers
            ) {

        // ヘッダ一覧が null の場合は例外
        if (headers == null) {

            throw new IllegalArgumentException();

        }

        // 返却テーブルを作成する
        final Map<String, String>   retTable = new HashMap<String, String>();

        // ヘッダ分繰り返す
        for (final Header header : headers) {

            // 返却テーブルへ追加する
            retTable.put(header.getName(), header.getValue());

        }

        // 作成したテーブルを返却する
        return retTable;

    }


}
