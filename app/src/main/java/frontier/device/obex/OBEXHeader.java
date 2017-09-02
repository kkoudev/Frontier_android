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
import java.util.HashSet;
import java.util.Set;

import frontier.util.GeneralUtils;
import frontier.util.StringUtils;


/**
 * OBEXヘッダ情報クラス。
 *
 * @author Kou
 *
 */
public class OBEXHeader implements DataOutput {


    /**
     * OBEX MIME種別 : フォルダ一覧
     */
    public static final String      TYPE_MIME_FOLDER_LISTING    = "x-obex/folder-listing";

    /**
     * OBEX MIME種別 : vCalendar
     */
    public static final String      TYPE_MIME_VCALENDAR         = "text/x-vcalendar";

    /**
     * OBEX MIME種別 : vCard
     */
    public static final String      TYPE_MIME_VCARD             = "text/x-vcard";

    /**
     * OBEX MIME種別 : vMsg
     */
    public static final String      TYPE_MIME_VMSG              = "text/x-vmsg";

    /**
     * OBEX MIME種別 : UPF
     */
    public static final String      TYPE_MIME_UPF               = "image/x-UPF";




    /**
     * ヘッダ長データ不要コード判別テーブル
     */
    private static final Set<OBEXHeaderCode>    NO_HEADER_HENGTH_CODES =
        new HashSet<OBEXHeaderCode>();

    /**
     * バイトデータ
     */
    private final ByteArrayOutputStream     obexBuffer = new ByteArrayOutputStream();

    /**
     * データ書き込み先
     */
    private final DataOutputStream          obexWriter = new DataOutputStream(obexBuffer);

    /**
     * ヘッダコード
     */
    private final OBEXHeaderCode            obexCode;



    /**
     * ヘッダ長データ不要コード判別テーブルを初期化する
     */
    static {

        // ヘッダ長が不要なヘッダコードを設定する
        NO_HEADER_HENGTH_CODES.add(OBEXHeaderCode.LENGTH);
        NO_HEADER_HENGTH_CODES.add(OBEXHeaderCode.SESSION_ID);

    }



    /**
     * 指定した種別のOBEXヘッダ情報を作成する。
     *
     * @param code OBEXヘッダコード
     */
    public OBEXHeader(
            final OBEXHeaderCode    code
            ) {

        obexCode = code;

    }


    /**
     * OBEXヘッダコードを取得する。
     *
     * @return OBEXヘッダコード
     */
    public OBEXHeaderCode getCode() {

        return obexCode;

    }


    /**
     * バッファリング中のバイトデータを取得する。<br>
     *
     * @return バッファリング中のバイトデータ
     * @throws IOException  入出力エラー
     */
    byte[] toByteArray() throws IOException {

        final ByteArrayOutputStream     retBuf  = new ByteArrayOutputStream();  // 返却バッファ
        final DataOutputStream          dataOut = new DataOutputStream(retBuf); // データ書き込み先


        // バッファをフラッシュする
        obexWriter.flush();

        // 書き込みデータを取得する
        final byte[]    writeData = obexBuffer.toByteArray();

        // ヘッダコードを書き込む
        dataOut.write(obexCode.getCode());

        // ヘッダ長を書きこむコードの場合
        if (!NO_HEADER_HENGTH_CODES.contains(obexCode)) {

            // ヘッダ長を書きこむ
            dataOut.writeShort(
                    (short)(writeData.length + GeneralUtils.SIZE_BYTE + GeneralUtils.SIZE_SHORT)
                    );

        }

        // ヘッダデータを書き込む
        dataOut.write(writeData);

        // バッファをフラッシュする
        dataOut.flush();


        // ヘッダバイトデータを返却する
        return retBuf.toByteArray();

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


}
