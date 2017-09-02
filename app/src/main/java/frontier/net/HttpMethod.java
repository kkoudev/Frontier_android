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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;


/**
 * 通信メソッド生成ファクトリークラス。
 *
 * @author Kou
 *
 */
public final class HttpMethod {


    /**
     * ヘッダ : エンコーディング形式
     */
    public static final String          HEADER_CONTENT_ENCODING             = "Content-Encoding";

    /**
     * ヘッダ : コンテンツ形式
     */
    public static final String          HEADER_CONTENT_TYPE                 = "Content-Type";

    /**
     * ヘッダ : 日付
     */
    public static final String          HEADER_DATE                         = "Date";

    /**
     * ヘッダ : 有効期限
     */
    public static final String          HEADER_EXPIRES                      = "Expires";

    /**
     * ヘッダ : 更新時刻
     */
    public static final String          HEADER_LAST_MODIFIED                = "Last-Modified";

    /**
     * ヘッダ : コンテンツ長
     */
    public static final String          HEADER_CONTENT_LENGTH               = "Content-Length";

    /**
     * ヘッダ : 持続的接続
     */
    public static final String          HEADER_CONNECTION                   = "Connection";

    /**
     * ヘッダ : 転送符号化形式
     */
    public static final String          HEADER_TRANSFER_ENCODING            = "Transfer-Encoding";

    /**
     * ヘッダ : 期待値
     */
    public static final String          HEADER_EXPECT_DIRECTIVE             = "Expect";

    /**
     * ヘッダ : ホスト
     */
    public static final String          HEADER_TARGET_HOST                  = "Host";

    /**
     * ヘッダ : ユーザエージェント
     */
    public static final String          HEADER_USER_AGENT                   = "User-Agent";

    /**
     * ヘッダ : サーバ
     */
    public static final String          HEADER_SERVER                       = "Server";



    /**
     * ポート番号 : HTTP
     */
    public static final int             PORT_HTTP   = 80;

    /**
     * ポート番号 : HTTPS
     */
    public static final int             PORT_HTTPS  = 443;



    /**
     * デフォルトクライアント
     */
    private static final HttpClient     DEFAULT_CLIENT;


    /**
     * デフォルト通信クライアントを作成する
     */
    static {

        final SchemeRegistry    registry = new SchemeRegistry();
        final HttpParams        params   = new BasicHttpParams();

        // スキーマを設定する
        registry.register(
                new Scheme(
                        HttpHost.DEFAULT_SCHEME_NAME,
                        PlainSocketFactory.getSocketFactory(),
                        PORT_HTTP
                        )
                );

        // プロトコル情報を設定する
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);    // バージョン
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);       // 文字コード

        // HTTPクライアントを作成する
        DEFAULT_CLIENT = new DefaultHttpClient(
                new ThreadSafeClientConnManager(
                        params,
                        registry
                        ),
                params
                );

    }



    /**
     * インスタンス生成防止。
     *
     */
    private HttpMethod() {

        // 処理なし

    }


    /**
     * HTTP通信を行う。
     *
     * @param client            使用するHTTPクライアント
     * @param method            通信メソッド
     * @param queryParams       URIに付加するクエリパラメータ
     * @param requestHeaders    リクエストヘッダ情報
     * @param listener          通信完了通知先リスナー
     * @throws IllegalArgumentException HTTPクライアントが null の場合
     * @throws IllegalArgumentException URIが null または長さ 0 の場合
     */
    private static void doMethod(
            final HttpClient        client,
            final HttpRequestBase   method,
            final NameValuePair[]   queryParams,
            final NameValuePair[]   requestHeaders,
            final HttpListener      listener
            ) {

        // HTTPクライアントが null の場合は例外
        if (client == null) {

            throw new IllegalArgumentException();

        }


        // リクエストヘッダ情報がある場合
        if (requestHeaders != null) {

            // リクエストヘッダ分処理をする
            for (final NameValuePair requestHeader : requestHeaders) {

                // ヘッダを追加する
                method.addHeader(
                        requestHeader.getName(),
                        requestHeader.getValue()
                        );

            }

        }


        // 通信を非同期で開始する
        new AsyncTask<Void, Void, HttpResult>() {

            @Override
            protected HttpResult doInBackground(
                    final Void... params) {

                Header[]    responseHeaders     = null;
                int         statusCode          = HttpStatus.SC_BAD_REQUEST;
                byte[]      responseData        = null;


                try {

                    // 通信を開始する
                    final HttpResponse  response    = client.execute(method);
                    final StatusLine    statusLine  = response.getStatusLine();

                    // ヘッダを取得する
                    responseHeaders = response.getAllHeaders();

                    // ステータスコードを取得する
                    statusCode = statusLine.getStatusCode();

                    // レスポンスデータを取得する
                    responseData = EntityUtils.toByteArray(response.getEntity());

                } catch (final Throwable e) {

                    e.printStackTrace();

                } finally {

                    // 通信を切断する
                    method.abort();

                }


                // イメージファイルをSDへ書き込む
                return new HttpResult(
                        responseHeaders,
                        statusCode,
                        responseData
                        );

            }


            @Override
            protected void onPostExecute(
                    final HttpResult result
                    ) {

                // 通信完了通知先リスナーがある場合
                if (listener != null) {

                    // 通信完了処理を実行する
                    listener.connectFinished(result);

                }

            }

        }
        .execute((Void[])null);

    }


    /**
     * デフォルトHTTPクライアントでHTTP GET通信を行う。
     *
     * @param uri               通信先URI
     * @param queryParams       URIに付加するクエリパラメータ
     * @param requestHeaders    リクエストヘッダ情報
     * @param listener          通信完了通知先リスナー
     * @throws IllegalArgumentException HTTPクライアントが null の場合
     * @throws IllegalArgumentException URIが null または長さ 0 の場合
     */
    public static void doGet(
            final String            uri,
            final NameValuePair[]   queryParams,
            final NameValuePair[]   requestHeaders,
            final HttpListener      listener
            ) {

        doGet(DEFAULT_CLIENT, uri, queryParams, requestHeaders, listener);

    }


    /**
     * HTTPクライアントを指定してHTTP GET通信を行う。
     *
     * @param client            使用するHTTPクライアント
     * @param uri               通信先URI
     * @param queryParams       URIに付加するクエリパラメータ
     * @param requestHeaders    リクエストヘッダ情報
     * @param listener          通信完了通知先リスナー
     * @throws IllegalArgumentException HTTPクライアントが null の場合
     * @throws IllegalArgumentException URIが null または長さ 0 の場合
     */
    public static void doGet(
            final HttpClient        client,
            final String            uri,
            final NameValuePair[]   queryParams,
            final NameValuePair[]   requestHeaders,
            final HttpListener      listener
            ) {

        // URI が null の場合は例外
        if ((uri == null) || (uri.length() == 0)) {

            throw new IllegalArgumentException();

        }

        // GETメソッド通信を行う
        doMethod(
                client,
                new HttpGet(HttpUtils.createURI(uri, queryParams)),
                queryParams,
                requestHeaders,
                listener
                );

    }


    /**
     * デフォルトHTTPクライアントでHTTP POST通信を行う。
     *
     * @param uri               通信先URI
     * @param queryParams       URIに付加するクエリパラメータ
     * @param requestHeaders    リクエストヘッダ情報
     * @param requestEntity     リクエストエンティティ情報
     * @param listener          通信完了通知先リスナー
     * @throws IllegalArgumentException HTTPクライアントが null の場合
     * @throws IllegalArgumentException URIが null または長さ 0 の場合
     */
    public static void doPost(
            final String            uri,
            final NameValuePair[]   queryParams,
            final NameValuePair[]   requestHeaders,
            final HttpEntity        requestEntity,
            final HttpListener      listener
            ) {

        doPost(DEFAULT_CLIENT, uri, queryParams, requestHeaders, requestEntity, listener);

    }


    /**
     * HTTPクライアントを指定してHTTP POST通信を行う。
     *
     * @param client            使用するHTTPクライアント
     * @param uri               通信先URI
     * @param queryParams       URIに付加するクエリパラメータ
     * @param requestHeaders    リクエストヘッダ情報
     * @param requestEntity     リクエストエンティティ情報
     * @param listener          通信完了通知先リスナー
     * @throws IllegalArgumentException HTTPクライアントが null の場合
     * @throws IllegalArgumentException URIが null または長さ 0 の場合
     */
    public static void doPost(
            final HttpClient        client,
            final String            uri,
            final NameValuePair[]   queryParams,
            final NameValuePair[]   requestHeaders,
            final HttpEntity        requestEntity,
            final HttpListener      listener
            ) {

        // URI が null の場合は例外
        if ((uri == null) || (uri.length() == 0)) {

            throw new IllegalArgumentException();

        }

        final HttpPost      httpPost = new HttpPost(HttpUtils.createURI(uri, queryParams));

        // リクエストエンティティ情報がある場合
        if (requestEntity != null) {

            // エンティティ情報を設定する
            httpPost.setEntity(requestEntity);

        }

        // POSTメソッド通信を行う
        doMethod(
                client,
                httpPost,
                queryParams,
                requestHeaders,
                listener
                );

    }


}
