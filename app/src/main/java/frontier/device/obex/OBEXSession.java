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

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import frontier.util.IOUtils;


/**
 * OBEX通信セッションクラス。
 *
 * @author Kou
 *
 */
public abstract class OBEXSession implements Closeable {


    /**
     * データ受信リトライ間隔時間 (ms)
     */
    static final int                INTERVAL_RETRY_MILLIS       = 50;

    /**
     * デフォルトセッションタイムアウト時間 (ms)
     */
    private static final long       OBEX_DEFAULT_TIME_OUT       = 300000;

    /**
     * OBEX通信管理スレッド数
     */
    private static final int        OBEX_THREAD_COUNT           = 10;


    /**
     * OBEX通信管理スレッドエグゼキュータ
     */
    private static final ExecutorService    OBEX_EXECUTOR;

    /**
     * OBEXタイムアウト管理スレッド
     */
    private static final OBEXTimeoutThread  OBEX_TIMEOUT_THREAD;

    /**
     * OBEXリスナー
     */
    private OBEXListener                    obexListener;

    /**
     * セッションタイムアウト時間 (ms)
     */
    private long                            obexTimeout = OBEX_DEFAULT_TIME_OUT;

    /**
     * 接続済みかどうか
     */
    private final AtomicBoolean             obexConnected = new AtomicBoolean();



    /**
     * 各種初期化処理を実行する
     */
    static {

        // OBEXエグゼキュータとタイムアウト管理スレッドを作成する
        OBEX_EXECUTOR       = Executors.newFixedThreadPool(OBEX_THREAD_COUNT);
        OBEX_TIMEOUT_THREAD = new OBEXTimeoutThread();

        // OBEXタイムアウト管理スレッドを起動する
        OBEX_TIMEOUT_THREAD.start();

    }



    /**
     * 接続前準備処理を定義する。<br>
     *
     * @return 接続前準備処理に成功した場合は true
     */
    protected abstract boolean prepareConnect();



    /**
     * OBEX受信ストリームを取得する。
     *
     * @return OBEX受信ストリーム
     * @throws IOException 入出力エラー時
     */
    protected abstract DataInputStream getInputStream() throws IOException;


    /**
     * OBEX送信ストリームを取得する。
     *
     * @return OBEX送信ストリーム
     * @throws IOException 入出力エラー時
     */
    protected abstract DataOutputStream getOutputStream() throws IOException;



    /**
     * セッション情報を破棄する。
     *
     */
    private void disposeSession() {

        try {

            // セッションの入出力ストリームを閉じる
            IOUtils.closeQuietly(getInputStream());
            IOUtils.closeQuietly(getOutputStream());

        } catch (final IOException e) {

            e.printStackTrace();

        }

        // セッションをクローズする
        IOUtils.closeQuietly(this);

        // 接続状態をクリアする
        obexConnected.set(false);

    }


    /**
     * OBEXリスナーを設定する。
     *
     * @param listener OBEXリスナー
     */
    public void setListener(
            final OBEXListener  listener
            ) {

        obexListener = listener;

    }


    /**
     * 接続を開始する。
     *
     * @return 接続開始に成功した場合は true
     */
    public boolean connect() {

        try {

            // 接続オペレーションを作成する
            final OBEXOperation operation       = new OBEXOperation(OBEXOperationCode.CONNECT);

            // 各種情報を書き込む
            operation.writeByte(OBEXOperation.OBEX_PROTOCOL_VERSION);   // プロトコルバージョン
            operation.writeByte(OBEXOperation.OBEX_FLAGS);              // フラグ
            operation.writeShort(OBEXOperation.OBEX_MAX_PACKET_SIZE);   // 最大パケットサイズ

            // 接続オペレーションを送信する
            processPost(operation, new OBEXInnerListener() {

                @Override
                public void responseObex(
                        final OBEXOperationCode     opecode,
                        final OBEXResponse          response
                        ) {

                    final OBEXListener  listener = obexListener;    // 通信リスナー

                    // 正常終了の場合
                    if (response.getCode() == OBEXResponseCode.OK) {

                        // 接続済みに設定する
                        obexConnected.set(true);

                    }

                    // リスナーが存在する場合
                    if (listener != null) {

                        // 接続レスポンス処理を実行する
                        listener.responseObexOperation(
                                OBEXSession.this,
                                OBEXOperationCode.CONNECT,
                                response
                                );

                        // オペレーションを送信する
                        postOperation(listener.getRequestObexOperation());

                    }

                }

            });

            // 接続成功
            return true;

        } catch (final IOException e) {

            e.printStackTrace();

            // 接続失敗
            return false;

        }

    }


    /**
     * OBEX接続中断要求を送信する。
     *
     */
    public void abort() {

        // 中断オペレーションを送信する
        processPost(
                new OBEXOperation(OBEXOperationCode.ABORT),
                new OBEXInnerListener() {

                    @Override
                    public void responseObex(
                            final OBEXOperationCode opecode,
                            final OBEXResponse      response
                            ) {

                        final OBEXListener  listener = obexListener;    // 通信リスナー

                        // リスナーが存在する場合
                        if (listener != null) {

                            // 接続中断レスポンス処理を実行する
                            listener.responseObexOperation(
                                    OBEXSession.this,
                                    OBEXOperationCode.ABORT,
                                    response
                                    );

                        }

                        // セッションクローズ処理を実行する
                        IOUtils.closeQuietly(OBEXSession.this);

                    }

                }
                );

    }


    /**
     * OBEX接続切断要求を送信する。
     *
     */
    public void disconnect() {

        // 切断オペレーションを送信する
        processPost(
                new OBEXOperation(OBEXOperationCode.DISCONNECT),
                new OBEXInnerListener() {

                    @Override
                    public void responseObex(
                            final OBEXOperationCode opecode,
                            final OBEXResponse      response
                            ) {

                        final OBEXListener  listener = obexListener;    // 通信リスナー

                        // リスナーが存在する場合
                        if (listener != null) {

                            // 接続切断レスポンス処理を実行する
                            listener.responseObexOperation(
                                    OBEXSession.this,
                                    OBEXOperationCode.DISCONNECT,
                                    response
                                    );

                        }

                        // セッション情報を破棄する
                        disposeSession();

                    }

                }
                );

    }


    /**
     * セッションタイムアウト時間(ms)を設定する。
     *
     * @param timeout セッションタイムアウト時間(ms)
     * @throws IllegalArgumentException 指定されたタイムアウト時間が 0 以下の場合
     */
    public void setTimeout(
            final long  timeout
            ) {

        // 0以下の場合は例外
        if (timeout <= 0) {

            throw new IllegalArgumentException();

        }

        // セッションタイムアウト時間を設定する
        obexTimeout = timeout;

    }


    /**
     * セッションタイムアウト時間(ms)を取得する。
     *
     * @return セッションタイムアウト時間(ms)
     */
    public long getTimeout() {

        return obexTimeout;

    }


    /**
     * 指定オペレーションを開始する。
     *
     * @param operation 開始するオペレーション
     */
    private void beginOperation(
            final OBEXOperation operation
            ) {

        // セッション情報を設定する
        operation.setSession(this);

        // タイムアウト管理へ追加する
        OBEX_TIMEOUT_THREAD.putOperation(operation);

    }


    /**
     * オペレーション状態を更新する。
     *
     * @param operation 更新するオペレーション
     */
    private void updateOperation(
            final OBEXOperation operation
            ) {

        // タイムアウト時間を再設定する
        operation.updateTimeout();

    }


    /**
     * 指定オペレーションを終了する。
     *
     * @param operation 終了するオペレーション
     */
    private void endOperation(
            final OBEXOperation operation
            ) {

        // セッション情報を空にする
        operation.setSession(null);

        // タイムアウト管理から削除する
        OBEX_TIMEOUT_THREAD.removeOperation(operation);

    }


    /**
     * オペレーション送信処理を実行する。
     *
     * @param operation     送信オペレーション一覧
     * @param listener      レスポンス処理リスナー
     */
    private void processPost(
            final OBEXOperation         operation,
            final OBEXInnerListener     listener
            ) {

        processPost(new OBEXOperation[] {operation}, listener);

    }


    /**
     * オペレーション送信処理を実行する。
     *
     * @param operations    送信オペレーション一覧
     * @param listener      レスポンス処理リスナー
     */
    private void processPost(
            final OBEXOperation[]       operations,
            final OBEXInnerListener     listener
            ) {

        // 送信処理を定義する
        final Runnable      threadProcess = new Runnable() {

            @Override
            public void run() {

                byte[]              resultBlock;        // 結果ブロック読み込みバッファ
                int                 responseSize;       // レスポンス全サイズ
                OBEXResponseCode    responseCode;       // レスポンスコード
                byte[]              responseContent;    // レスポンスコンテントデータ
                int                 readResult;         // 読み込み結果
                DataInputStream     in  = null;         // データ受信ストリーム
                DataOutputStream    out = null;         // データ送信ストリーム


                // レスポンスコードとコンテントデータを初期化する
                responseCode    = OBEXResponseCode.BAD_REQUEST;
                responseContent = null;


                // 接続していない場合
                // かつ接続準備処理を行い、準備処理が失敗した場合
                if (!obexConnected.get() && !prepareConnect()) {

                    // タイムアウトとしてレスポンスを返す
                    listener.responseObex(
                            null,
                            new OBEXResponse(
                                    OBEXResponseCode.REQUEST_TIMEOUT,
                                    responseContent
                                    )
                            );

                    // セッション情報を破棄する
                    disposeSession();

                    // 接続開始失敗
                    return;

                }

                // オペレーション分繰り返す
                for (final OBEXOperation operation : operations) {

                    try {

                        // オペレーションを開始する
                        beginOperation(operation);


                        // データ送受信ストリームを取得する
                        in  = getInputStream();
                        out = getOutputStream();

                        // オペレーションを送信する
                        out.write(operation.toByteArray());

                        // 少し待つ
                        Thread.sleep(INTERVAL_RETRY_MILLIS);

                        // 結果ブロック読み込みバッファを作成する
                        resultBlock = new byte[OBEXResponse.SIZE_RESPONSE_BLOCK_RESULT];

                        // レスポンスデータを読み込む
                        readResult = in.read(resultBlock);

                        // オペレーション状態を更新する
                        updateOperation(operation);

                        // 返却データがない場合
                        while (readResult == -1) {

                            // 少し待つ
                            Thread.sleep(INTERVAL_RETRY_MILLIS);

                            // データを読み込む
                            readResult = in.read(resultBlock);

                            // オペレーション状態を更新する
                            updateOperation(operation);

                        }


                        // 結果ブロック読み込みストリームを作成する
                        final DataInputStream       resultBlockIn =
                            new DataInputStream(new ByteArrayInputStream(resultBlock));

                        // レスポンスコードを取得する
                        responseCode = OBEXResponseCode.toType(resultBlockIn.readByte());

                        // レスポンスデータサイズを取得する
                        responseSize = resultBlockIn.readShort() - OBEXResponse.SIZE_RESPONSE_BLOCK_RESULT;

                        // レスポンスデータサイズが 0 より大きい場合
                        if (responseSize > 0) {

                            // コンテントデータ読み込みバッファを作成する
                            responseContent = new byte[responseSize];

                            // コンテントデータを読み込む
                            readResult = in.read(responseContent);

                            // オペレーション状態を更新する
                            updateOperation(operation);

                            // 返却データがない場合
                            while (readResult == -1) {

                                // 少し待つ
                                Thread.sleep(INTERVAL_RETRY_MILLIS);

                                // データを読み込む
                                readResult = in.read(responseContent);

                                // オペレーション状態を更新する
                                updateOperation(operation);

                            }

                        }

                        // 正常終了とする
                        responseCode = OBEXResponseCode.OK;

                    } catch (final Throwable e) {

                        e.printStackTrace();

                        // 例外情報が入出力例外の場合
                        if (e instanceof IOException) {

                            // 割り込みフラグが有効の場合
                            if (Thread.interrupted()) {

                                // レスポンスコードをタイムアウトに設定する
                                responseCode = OBEXResponseCode.REQUEST_TIMEOUT;

                            } else {

                                // レスポンスコードを不当な要求に設定する
                                responseCode = OBEXResponseCode.BAD_REQUEST;

                            }

                        } else {

                            // レスポンスコードを内部エラーに設定する
                            responseCode = OBEXResponseCode.INTERNAL_SERVER_ERROR;

                        }

                        // セッション情報を破棄する
                        disposeSession();

                        // 処理中断
                        return;

                    } finally {

                        // オペレーションを終了する
                        endOperation(operation);

                        // レスポンス処理を実行する
                        listener.responseObex(
                                operation.getCode(),
                                new OBEXResponse(
                                        responseCode,
                                        responseContent
                                        )
                                );

                    }

                }

            }

        };

        // 通信処理を実行する
        OBEX_EXECUTOR.execute(threadProcess);

    }


    /**
     * 指定されたオペレーションを送信する。<br>
     * <br>
     * レスポンスは指定されたリスナーへ返却される。<br>
     *
     * @param operation     送信するオペレーション
     * @throws IllegalArgumentException 送信するオペレーションが null の場合
     */
    void postOperation(
            final OBEXOperation     operation
            ) {

        // 送信するオペレーションが null の場合は例外
        if (operation == null) {

            throw new IllegalArgumentException();

        }

        // 送信処理を実行する
        postOperation(new OBEXOperation[] {operation});

    }


    /**
     * 指定されたオペレーションを送信する。<br>
     * <br>
     * レスポンスは指定されたリスナーへ返却される。<br>
     *
     * @param operations    送信するオペレーション一覧
     * @throws IllegalArgumentException 送信するオペレーション一覧が null の場合
     */
    void postOperation(
            final OBEXOperation[]   operations
            ) {

        // 送信するオペレーション一覧が null の場合は例外
        if (operations == null) {

            throw new IllegalArgumentException();

        }

        // 送信処理を実行する
        processPost(operations, new OBEXInnerListener() {

            @Override
            public void responseObex(
                    final OBEXOperationCode opecode,
                    final OBEXResponse      response
                    ) {

                final OBEXListener  listener = obexListener;    // 通信リスナー

                // リスナーが存在する場合
                if (listener != null) {

                    // オペレーションレスポンス処理を実行する
                    listener.responseObexOperation(
                            OBEXSession.this,
                            opecode,
                            response
                            );

                }

            }

        });

    }




    /**
     * OBEXレスポンス内部処理リスナー。
     *
     * @author Kou
     *
     */
    private interface OBEXInnerListener {


        /**
         * OBEXレスポンス処理を実行する。<br>
         * <br>
         * 処理したオペレーションごとにレスポンスを本メソッドで返却する。<br>
         * 対応オペレーションコードがない場合、<br>
         * オペレーション実行前にエラーが発生したこととなる。<br>
         *
         * @param opecode   対応オペレーションコード
         * @param response  レスポンスデータ
         */
        void responseObex(
                final OBEXOperationCode opecode,
                final OBEXResponse      response
                );


    }


    /**
     * OBEX通信タイムアウト管理スレッド。
     *
     * @author Kou
     *
     */
    private static final class OBEXTimeoutThread extends Thread {


        /**
         * タイムアウトオペレーションキュー
         */
        private final DelayQueue<OBEXOperation>      timeoutQueue =
            new DelayQueue<OBEXOperation>();



        /**
         * 処理中オペレーションを追加する。
         *
         * @param operation 追加するオペレーション
         */
        public void putOperation(
                final OBEXOperation operation
                ) {

            // nullの場合は例外
            if (operation == null) {

                throw new IllegalArgumentException();

            }

            // オペレーションを追加する
            timeoutQueue.put(operation);

        }


        /**
         * 処理中オペレーションを削除する。
         *
         * @param operation 削除するオペレーション
         * @return 削除に成功した場合は true
         */
        public boolean removeOperation(
                final OBEXOperation operation
                ) {

            // nullの場合は例外
            if (operation == null) {

                throw new IllegalArgumentException();

            }

            // 指定オペレーションを削除する
            return timeoutQueue.remove(operation);

        }


        /**
         * OBEX通信タイムアウト処理を実行する。
         *
         */
        @Override
        public void run() {

            try {

                // メインループ
                while (true) {

                    // オペレーションデータが取得できるまで待つ
                    final OBEXOperation     queueData = timeoutQueue.take();
                    final OBEXSession       session   = queueData.getSession();
                    final Thread            thread    = queueData.getThread();

                    // セッションまたは処理スレッドがない場合
                    if ((session == null) || (thread == null)) {

                        // 処理済みなので次へ
                        continue;

                    }

                    // 割り込みを発生させる
                    thread.interrupt();

                    // セッション情報を破棄する
                    session.disposeSession();

                }

            } catch (final Throwable e) {

                e.printStackTrace();

            }

        }



    }


}
