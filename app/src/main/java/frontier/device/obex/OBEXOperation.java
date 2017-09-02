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

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import frontier.util.GeneralUtils;
import frontier.util.StringUtils;


/**
 * OBEXオペレーションデータクラス。
 *
 * @author Kou
 *
 */
public class OBEXOperation implements DataOutput, Delayed {


    /**
     * OBEXプロトコルバージョン
     */
    static final int        OBEX_PROTOCOL_VERSION   = 0x10;

    /**
     * OBEXフラグ
     */
    static final int        OBEX_FLAGS              = 0x00;

    /**
     * OBEX最大パケットサイズ
     */
    static final int        OBEX_MAX_PACKET_SIZE    = 0x2000;


    /**
     * オペレーションのヘッダ一覧
     */
    private final List<OBEXHeader>          obexHeaders = new ArrayList<OBEXHeader>();

    /**
     * オペレーションコード
     */
    private final OBEXOperationCode         obexCode;

    /**
     * バイトデータ
     */
    private final ByteArrayOutputStream     obexBuffer = new ByteArrayOutputStream();

    /**
     * データ書き込み先
     */
    private final DataOutputStream          obexWriter = new DataOutputStream(obexBuffer);

    /**
     * オペレーション処理元セッション
     */
    private OBEXSession                     obexSession;

    /**
     * オペレーションが属するスレッド
     */
    private Thread                          obexThread;

    /**
     * タイムアウト基本時間
     */
    private long                            obexTimeOutBase;




    /**
     * 指定されたオペレーションコードのオペレーションデータを作成する。
     *
     * @param code オペレーションコード
     */
    public OBEXOperation(
            final OBEXOperationCode     code
            ) {

        // 引数が null の場合は例外
        if (code == null) {

            throw new IllegalArgumentException();

        }

        // オペレーションコードを取得する
        obexCode = code;

    }


    /**
     * OBEXセッション情報を設定する。
     *
     * @param session OBEXセッション情報
     */
    void setSession(
            final OBEXSession   session
            ) {

        // セッションが null の場合
        if (session == null) {

            // 各種情報をクリアする
            obexThread      = null;     // 処理スレッド
            obexSession     = null;     // セッション
            obexTimeOutBase = 0;        // タイムアウト時間

        } else {

            // 各種情報を設定する
            obexThread      = Thread.currentThread();                               // 処理スレッド
            obexSession     = session;                                              // セッション
            obexTimeOutBase = System.currentTimeMillis() + session.getTimeout();    // タイムアウト時間

        }

    }


    /**
     * タイムアウト時間を現在時刻で更新する。
     *
     */
    void updateTimeout() {

        final OBEXSession   session = obexSession;

        // セッションが設定されていない場合は例外
        if (session == null) {

            throw new IllegalStateException();

        }

        // 現在時間で設定しなおす
        obexTimeOutBase = System.currentTimeMillis() + session.getTimeout();

    }


    /**
     * OBEXセッション情報を取得する。
     *
     * @return OBEXセッション情報
     */
    OBEXSession getSession() {

        return obexSession;

    }


    /**
     * OBEX処理スレッドを取得する。
     *
     * @return OBEX処理スレッド
     */
    Thread getThread() {

        return obexThread;

    }


    /**
     * OBEXオペレーションコードを取得する。
     *
     * @return OBEXオペレーションコード
     */
    public OBEXOperationCode getCode() {

        return obexCode;

    }


    /**
     * OBEXヘッダ情報を追加する。
     *
     * @param header    追加するOBEXヘッダ情報
     */
    public void addHeader(
            final OBEXHeader    header
            ) {

        // OBEXヘッダが null の場合は例外
        if (header == null) {

            throw new IllegalArgumentException();

        }

        // ヘッダを一覧へ追加する
        obexHeaders.add(header);

    }


    /**
     * 指定したヘッダコードのヘッダを取得する。
     *
     * @param code  取得するヘッダのコード
     * @return 指定したヘッダコードのヘッダ。見つからない場合は null
     */
    public OBEXHeader getHeader(
            final OBEXHeaderCode    code
            ) {

        // ヘッダコードが null の場合は例外
        if (code == null) {

            throw new IllegalArgumentException();

        }

        // 指定ヘッダコードのヘッダを探す
        for (final OBEXHeader header : obexHeaders) {

            // ヘッダコードが一致した場合
            if (code.equals(header.getCode())) {

                // ヘッダ情報を返す
                return header;

            }

        }


        // ヘッダがみつからなかったので null を返す
        return null;

    }


    /**
     * 指定されたヘッダコードのヘッダを削除する。
     *
     * @param code 削除するヘッダのコード
     * @return 削除に成功した場合は true
     */
    public boolean removeHeader(
            final OBEXHeaderCode    code
            ) {

        // ヘッダコードが null の場合は例外
        if (code == null) {

            throw new IllegalArgumentException();

        }


        boolean     retResult = false;  // 削除に成功したかどうか

        // 指定ヘッダコードのヘッダを探す
        for (final Iterator<OBEXHeader> i = obexHeaders.iterator(); i.hasNext();) {

            // ヘッダ情報を取得する
            final OBEXHeader    header = i.next();

            // ヘッダコードが一致した場合
            if (code.equals(header.getCode())) {

                // ヘッダ情報を削除する
                i.remove();

                // 削除に成功とする
                retResult = true;

            }

        }


        // 削除結果を返す
        return retResult;


    }


    /**
     * オペレーション全体のバイトデータを取得する。
     *
     * @return オペレーション全体のバイトデータ
     */
    byte[] toByteArray() {

        final ByteArrayOutputStream     headerBuf   = new ByteArrayOutputStream();      // ヘッダバッファリングストリーム
        final ByteArrayOutputStream     retBuf      = new ByteArrayOutputStream();      // 返却バッファリングストリーム
        final DataOutputStream          headerOut   = new DataOutputStream(headerBuf);  // ヘッダ書き込みストリーム
        final DataOutputStream          retOut      = new DataOutputStream(retBuf);     // 返却書き込みストリーム


        try {

            // ヘッダ情報分繰り返す
            for (final OBEXHeader header : obexHeaders) {

                // ヘッダのバイトデータを書き込む
                headerOut.write(header.toByteArray());

            }

            // ヘッダをフラッシュする
            headerOut.flush();

            // プレフィックスデータとヘッダデータを取得する
            final byte[]    prefixData = obexBuffer.toByteArray();
            final byte[]    headerData = headerBuf.toByteArray();


            // オペレーションコードを書きこむ
            retOut.write(obexCode.getCode());

            // オペレーション長を書きこむ
            retOut.writeShort(
                    (short)(prefixData.length
                            + headerData.length
                            + GeneralUtils.SIZE_BYTE
                            + GeneralUtils.SIZE_SHORT)
                    );

            // プレフィックスデータを書きこむ
            retOut.write(prefixData);

            // ヘッダデータを書き込む
            retOut.write(headerData);

            // 返却書き込みストリームをフラッシュする
            retOut.flush();


            // 返却バッファデータを返す
            return retBuf.toByteArray();

        } catch (final IOException e) {

            e.printStackTrace();

        }


        // エラー時は null を返す
        return null;

    }


    /**
     * 書き込み済みサイズを取得する。
     *
     * @return アクセス可能なサイズ
     * @throws IOException                      入出力エラー
     * @throws UnsupportedOperationException    メソッド未サポート
     */
    public long getSize() throws IOException {

        // サイズを取得する
        return obexBuffer.size();

    }


    /**
     * byte型データを書き込む。
     *
     * @param byteValue 書き込むbyte型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void write(
            final int byteValue
            ) throws IOException {

        obexWriter.write(byteValue);

    }


    /**
     * バイト配列データを書き込む。
     *
     * @param byteArray バイト配列データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void write(
            final byte[] byteArray
            ) throws IOException {

        obexWriter.write(byteArray);

    }


    /**
     * バイト配列データを書き込む。
     *
     * @param byteArray 書き込むバイト配列データ
     * @param offset    書き込み開始先オフセット
     * @param length    書き込みサイズ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void write(
            final byte[] byteArray,
            final int    offset,
            final int    length
            ) throws IOException {

        obexWriter.write(byteArray, offset, length);

    }


    /**
     * boolean型データを書き込む。
     *
     * @param boolValue 書き込むboolean型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeBoolean(
            final boolean   boolValue
            ) throws IOException {

        obexWriter.writeBoolean(boolValue);

    }


    /**
     * byte型データを書き込む。
     *
     * @param byteValue 書き込むbyte型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeByte(
            final int byteValue
            ) throws IOException {

        obexWriter.writeByte(byteValue);

    }


    /**
     * 文字列データを 1 文字 1 バイトサイズで書き込む。<br>
     * <br>
     * 指定された文字列と、その末尾に終端データ(1バイトの 0)が書き込まれる。<br>
     *
     * @param str 書き込む文字列データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeBytes(
            final String str
            ) throws IOException {

        // 文字列を書き込む
        obexWriter.writeBytes(str);

        // 末尾データを書きこむ
        obexWriter.writeByte(0);

    }


    /**
     * char型データを書き込む。
     *
     * @param charValue 書き込むchar型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeChar(
            final int charValue
            ) throws IOException {

        obexWriter.writeChar(charValue);

    }


    /**
     * 文字列データを 1 文字 2 バイトサイズで書き込む。
     *
     * @param str 書き込む文字列データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeChars(
            final String str
            ) throws IOException {

        // 文字列を書き込む
        obexWriter.writeChars(str);

        // 末尾データを書きこむ
        obexWriter.writeByte(0);

    }


    /**
     * double型データを書き込む。
     *
     * @param doubleValue 書き込むdouble型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeDouble(
            final double doubleValue
            ) throws IOException {

        obexWriter.writeDouble(doubleValue);

    }


    /**
     * float型データを書き込む。
     *
     * @param floatValue 書き込むfloat型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeFloat(
            final float floatValue
            ) throws IOException {

        obexWriter.writeFloat(floatValue);

    }


    /**
     * int型データを書き込む。
     *
     * @param intValue 書き込むint型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeInt(
            final int intValue
            ) throws IOException {

        obexWriter.writeInt(intValue);

    }


    /**
     * long型データを書き込む。
     *
     * @param longValue 書き込むlong型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeLong(
            final long longValue
            ) throws IOException {

        obexWriter.writeLong(longValue);

    }


    /**
     * short型データを書き込む。
     *
     * @param shortValue 書き込むshort型データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeShort(
            final int shortValue
            ) throws IOException {

        obexWriter.writeShort(shortValue);

    }


    /**
     * 修正UTF-8形式で文字列を書き込む。<br>
     * <br>
     * 指定された文字列と、その末尾に終端データ(1バイトの 0)が書き込まれる。<br>
     *
     * @param str 書き込む文字列データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeUTF(
            final String str
            ) throws IOException {

        // 文字列データをサイズ付きで書き込む
        obexWriter.writeUTF(str);

        // 末尾データを書きこむ
        obexWriter.writeByte(0);

    }


    /**
     * デフォルトエンコードで文字列を書き込む。<br>
     * <br>
     * 指定された文字列と、その末尾に終端データ(1バイトの 0)が書き込まれる。<br>
     *
     * @param str 書き込む文字列データ
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeString(
            final String    str
            ) throws IOException {

        writeString(str, StringUtils.DEFAULT_CHARSET);

    }


    /**
     * エンコードを指定して文字列を書き込む。<br>
     * <br>
     * 指定された文字列と、その末尾に終端データ(1バイトの 0)が書き込まれる。<br>
     *
     * @param str       書き込む文字列データ
     * @param charset   文字列のエンコード
     * @throws IOException 書き込み失敗時にスローされる
     */
    public void writeString(
            final String    str,
            final String    charset
            ) throws IOException {

        // 指定エンコードで文字列バイトデータを取得する
        final byte[]    strBytes = str.getBytes(charset);

        // バイトデータを書き込む
        obexWriter.write(strBytes);

        // 末尾データを書き込む
        obexWriter.writeByte(0);

    }


    /**
     * {@inheritDoc}
     */
    public int compareTo(
            final Delayed obj
            ) {

        // インスタンスが一致しない場合
        if (!(obj instanceof OBEXOperation)) {

            // -1を返す
            return -1;

        }

        // 差分を返す
        if (obexTimeOutBase < ((OBEXOperation)obj).obexTimeOutBase) {

            // -1を返す
            return -1;

        } else if (obexTimeOutBase > ((OBEXOperation)obj).obexTimeOutBase) {

            // 1を返す
            return 1;

        } else {

            // 等しい場合
            if (equals(obj)) {

                // 0 を返す
                return 0;

            } else {

                // -1を返す
                return 0;

            }

        }

    }


    /**
     * {@inheritDoc}
     */
    public long getDelay(
            final TimeUnit unit
            ) {

        return unit.convert(obexTimeOutBase - System.currentTimeMillis(), TimeUnit.MILLISECONDS);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final Object    obj
            ) {

        return super.equals(obj);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {

        return super.hashCode();

    }


}
