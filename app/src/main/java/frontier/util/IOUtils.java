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
package frontier.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 入出力操作を簡略化するユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class IOUtils {


    /**
     * 入出力バッファサイズ
     */
    private static final int        BUFFER_SIZE         = 8192;

    /**
     * 行区切り文字
     */
    private static final String     LINE_SEPARATOR      = System.getProperty("line.separator");



    /**
     * インスタンス生成防止。
     *
     */
    private IOUtils() {

        // 処理なし

    }


    /**
     * 指定されたクローズ可能インスタンスを無条件にクローズする。
     *
     * @param closable クローズ可能インスタンス
     */
    public static void closeQuietly(
            final Closeable     closable
            ) {

        // nullの場合
        if (closable == null) {

            // 処理なし
            return;

        }

        try {

            // 閉じる
            closable.close();

        } catch (final IOException e) {

            e.printStackTrace();

        }

    }



    /**
     * 指定した入力ストリームの内容を指定した出力ライターへコピーする。
     *
     * @param in        コピー元入力ストリーム
     * @param writer    コピー先出力ライター
     * @param closing   処理完了またはエラー発生時に入力リーダーと出力ライターをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean copy(
            final InputStream   in,
            final Writer        writer,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if ((in == null) || (writer == null)) {

            throw new IllegalArgumentException();

        }

        return copy(new InputStreamReader(in), writer, closing);

    }


    /**
     * 指定した入力ストリームの内容を指定した出力ライターへコピーする。
     *
     * @param in        コピー元入力ストリーム
     * @param writer    コピー先出力ライター
     * @param encoding  入力ストリームのエンコーディング
     * @param closing   処理完了またはエラー発生時に入力リーダーと出力ライターをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean copy(
            final InputStream   in,
            final Writer        writer,
            final String        encoding,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if ((in == null) || (writer == null)) {

            throw new IllegalArgumentException();

        }


        final InputStreamReader    reader;     // 読み込み元リーダー

        try {

            // リーダーを作成する
            reader = new InputStreamReader(in, encoding);

        } catch (final UnsupportedEncodingException e) {

            e.printStackTrace();

            // 失敗
            return false;

        }

        // コピー処理を実行する
        return copy(reader, writer, closing);

    }


    /**
     * 指定した入力リーダーの内容を指定した出力ストリームへコピーする。
     *
     * @param reader    コピー元入力リーダー
     * @param out       コピー先出力ストリーム
     * @param closing   処理完了またはエラー発生時に入力リーダーと出力ストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean copy(
            final Reader        reader,
            final OutputStream  out,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if ((reader == null) || (out == null)) {

            throw new IllegalArgumentException();

        }

        return copy(reader, new OutputStreamWriter(out), closing);

    }



    /**
     * 指定した入力リーダーの内容を指定した出力ストリームへコピーする。
     *
     * @param reader    コピー元入力リーダー
     * @param out       コピー先出力ストリーム
     * @param encoding  出力ストリームのエンコーディング
     * @param closing   処理完了またはエラー発生時に入力リーダーと出力ストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean copy(
            final Reader        reader,
            final OutputStream  out,
            final String        encoding,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if ((reader == null) || (out == null)) {

            throw new IllegalArgumentException();

        }

        final OutputStreamWriter    writer;     // 書き込み先ライター

        try {

            // ライターを作成する
            writer = new OutputStreamWriter(out, encoding);

        } catch (final UnsupportedEncodingException e) {

            e.printStackTrace();

            // 失敗
            return false;

        }

        // コピー処理を実行する
        return copy(reader, writer, closing);

    }


    /**
     * 指定した入力ストリームの内容を指定した出力ストリームへコピーする。
     *
     * @param in        コピー元入力ストリーム
     * @param out       コピー先出力ストリーム
     * @param closing   処理完了またはエラー発生時に入力ストリームと出力ストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean copy(
            final InputStream   in,
            final OutputStream  out,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if ((in == null) || (out == null)) {

            throw new IllegalArgumentException();

        }


        final byte[]            readBuffer = new byte[BUFFER_SIZE];     // 読み込みバッファ
        int                     readLength;                             // 読み込み長さ

        try {

            // 読み込みバッファへデータを読み込む
            readLength = in.read(readBuffer);

            // 読み込みバイト数がバッファ長と等しければ繰り返し
            while (readLength != -1) {

                // 読み込んだデータを書き込む
                out.write(readBuffer, 0, readLength);

                // 次のデータを読み込む
                readLength = in.read(readBuffer);

            }

            // バッファをフラッシュする
            out.flush();

        } catch (final IOException e) {

            e.printStackTrace();

            // 読み込みエラーが発生した場合は失敗
            return false;

        } finally {

            // 処理完了またはエラー発生時にクローズする場合
            if (closing) {

                // ストリームをクローズする
                closeQuietly(in);
                closeQuietly(out);

            }

        }


        // 処理成功
        return true;

    }



    /**
     * 指定した入力リーダーの内容を指定した出力ライターへコピーする。
     *
     * @param reader    コピー元入力リーダー
     * @param writer    コピー先出力ライター
     * @param closing   処理完了またはエラー発生時に入力リーダーまたは出力ライターをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean copy(
            final Reader        reader,
            final Writer        writer,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if ((reader == null) || (writer == null)) {

            throw new IllegalArgumentException();

        }


        final char[]            readBuffer = new char[BUFFER_SIZE];     // 読み込みバッファ
        int                     readLength;                             // 読み込み長さ

        try {

            // 読み込みバッファへデータを読み込む
            readLength = reader.read(readBuffer);

            // 読み込みバイト数がバッファ長と等しければ繰り返し
            while (readLength != -1) {

                // 読み込んだデータを書き込む
                writer.write(readBuffer, 0, readLength);

                // 次のデータを読み込む
                readLength = reader.read(readBuffer);

            }

            // バッファをフラッシュする
            writer.flush();

        } catch (final IOException e) {

            e.printStackTrace();

            // 読み込みエラーが発生した場合は失敗
            return false;

        } finally {

            // 処理完了またはエラー発生時にクローズする場合
            if (closing) {

                // リーダーとライターをクローズする
                closeQuietly(reader);
                closeQuietly(writer);

            }

        }


        // 処理成功
        return true;

    }


    /**
     * 入力ストリームをバイト配列へ変換する。
     *
     * @param in        変換するストリーム
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 変換後のバイト配列
     */
    public static byte[] toByteArray(
            final InputStream   in,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if (in == null) {

            throw new IllegalArgumentException();

        }


        final ByteArrayOutputStream   outBuf = new ByteArrayOutputStream();   // データ書き込み先バッファ

        // バイトバッファへコピーする
        if (!copy(in, outBuf, closing)) {

            // 失敗した場合は null を返す
            return null;

        }

        // 生成したバイトデータを返す
        return outBuf.toByteArray();

    }


    /**
     * 入力リーダーをバイト配列へ変換する。
     *
     * @param reader    変換する入力リーダー
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 変換後のバイト配列。失敗時は null。
     */
    public static byte[] toByteArray(
            final Reader        reader,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if (reader == null) {

            throw new IllegalArgumentException();

        }


        final ByteArrayOutputStream   outBuf = new ByteArrayOutputStream();   // データ書き込み先バッファ

        // バイトバッファへコピーする
        if (!copy(reader, outBuf, closing)) {

            // 失敗した場合は null を返す
            return null;

        }

        // 生成したバイトデータを返す
        return outBuf.toByteArray();

    }


    /**
     * エンコーディングを指定して入力リーダーをバイト配列へ変換する。
     *
     * @param reader    変換する入力リーダー
     * @param encoding  変換エンコーディング
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 変換後のバイト配列。失敗時は null。
     */
    public static byte[] toByteArray(
            final Reader        reader,
            final String        encoding,
            final boolean       closing
            ) {

        // ストリームが null の場合
        if (reader == null) {

            throw new IllegalArgumentException();

        }


        final ByteArrayOutputStream   outBuf = new ByteArrayOutputStream();   // データ書き込み先バッファ

        // バイトバッファへコピーする
        if (!copy(reader, outBuf, encoding, closing)) {

            // 失敗した場合は null を返す
            return null;

        }

        // 生成したバイトデータを返す
        return outBuf.toByteArray();

    }


    /**
     * 指定入力ストリームから行のリストを読み込む。
     *
     * @param in        読み込み元入力ストリーム
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 行のリスト
     */
    public static List<String> readLines(
            final InputStream   in,
            final boolean       closing
            ) {

        return readLines(new InputStreamReader(in), closing);

    }


    /**
     * エンコードを指定して指定入力ストリームから行のリストを読み込む。
     *
     * @param in        読み込み元入力ストリーム
     * @param encoding  変換エンコーディング
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 成功時は行のリスト。失敗時は null。
     */
    public static List<String> readLines(
            final InputStream   in,
            final String        encoding,
            final boolean       closing
            ) {

        final InputStreamReader    reader;     // 読み込み元リーダー

        try {

            // リーダーを作成する
            reader = new InputStreamReader(in, encoding);

        } catch (final UnsupportedEncodingException e) {

            // 失敗
            return null;

        }

        // 行を読み込んで返す
        return readLines(reader, closing);

    }


    /**
     * 指定リーダーから行のリストを読み込む。
     *
     * @param reader    読み込み元リーダー
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 成功時は行のリスト。失敗時は null。
     */
    public static List<String> readLines(
            final Reader        reader,
            final boolean       closing
            ) {

        final BufferedReader    readBuf  = new BufferedReader(reader);
        final List<String>      retLines = new ArrayList<String>();

        try {

            // 先頭行を読み込む
            String line = readBuf.readLine();

            // 行データーがある場合
            while (line != null) {

                // 返却リストへ追加する
                retLines.add(line);

                // 次の行を読み込む
                line = readBuf.readLine();

            }

        } catch (final IOException e) {

            e.printStackTrace();

            // 失敗
            return null;

        } finally {

            // クローズする場合
            if (closing) {

                // バッファをクローズする
                closeQuietly(readBuf);

            }

        }

        // 読み込んだ行リストを返却する
        return retLines;

    }


    /**
     * 指定されたリーダーのラインイテレーターを作成する。
     *
     * @param reader 読み込むリーダー
     * @return 指定されたリーダーのラインイテレーター
     */
    public static LineIterator lineIterator(
            final Reader    reader
            ) {

        return new LineIterator(reader);

    }


    /**
     * 指定された入力ストリームのラインイテレーターをデフォルトエンコーディングで作成する。
     *
     * @param in        読み込む入力ストリーム
     * @return 指定された入力ストリームのラインイテレーター
     */
    public static LineIterator lineIterator(
            final InputStream   in
            ) {

        return lineIterator(in, null);

    }


    /**
     * 指定された入力ストリームのラインイテレーターを指定エンコーディングで作成する。
     *
     * @param in        読み込む入力ストリーム
     * @param encoding  変換エンコーディング
     * @return 指定された入力ストリームのラインイテレーター
     */
    public static LineIterator lineIterator(
            final InputStream   in,
            final String        encoding
            ) {

        // エンコーディング指定がない場合
        if (encoding == null) {

            // デフォルトエンコーディングでリーダーを作成して返す
            return new LineIterator(new InputStreamReader(in));

        } else {

            try {

                // 指定エンコーディングでリーダーを作成する
                return new LineIterator(new InputStreamReader(in, encoding));

            } catch (final UnsupportedEncodingException e) {

                e.printStackTrace();

                // 失敗
                return null;

            }

        }

    }


    /**
     * 指定された文字列をデフォルトエンコーディングで入力ストリームへ変換する。
     *
     * @param data  変換する文字列
     * @return 変換された入力ストリーム
     */
    public static InputStream toInputStream(
            final String    data
            ) {

        return toInputStream(data, null);

    }


    /**
     * 指定された文字列を指定エンコーディングで入力ストリームへ変換する。
     *
     * @param data      変換する文字列
     * @param encoding  変換エンコーディング
     * @return 変換された入力ストリーム。変換失敗時は null。
     */
    public static InputStream toInputStream(
            final String    data,
            final String    encoding
            ) {

        // データが null の場合は例外
        if (data == null) {

            throw new IllegalArgumentException();

        }

        try {

            // 入力ストリームを作成して返す
            return new ByteArrayInputStream(
                    encoding == null ? data.getBytes() : data.getBytes(encoding)
                    );

        } catch (final UnsupportedEncodingException e) {

            e.printStackTrace();

            // 失敗
            return null;

        }

    }


    /**
     * 指定された読み込みリーダーをデフォルトエンコーディングで文字列へ変換する。
     *
     * @param reader    読み込むリーダー
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 指定された入力ストリームの文字列表現。失敗した場合は null
     */
    public static String toString(
            final Reader        reader,
            final boolean       closing
            ) {

        // nullの場合は例外
        if (reader == null) {

            throw new IllegalArgumentException();

        }

        // 文字列を作成して返す
        return new String(toByteArray(reader, closing));

    }


    /**
     * 指定された入力ストリームをデフォルトエンコーディングで文字列へ変換する。
     *
     * @param in        読み込む入力ストリーム
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 指定された入力ストリームの文字列表現。失敗した場合は null
     */
    public static String toString(
            final InputStream   in,
            final boolean       closing
            ) {

        // nullの場合は例外
        if (in == null) {

            throw new IllegalArgumentException();

        }

        // 文字列を作成して返す
        return new String(toByteArray(in, closing));

    }


    /**
     * 指定された読み込みリーダーを指定エンコーディングで文字列へ変換する。
     *
     * @param reader    読み込むリーダー
     * @param encoding  変換エンコーディング
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 指定された入力ストリームの文字列表現。失敗した場合は null
     */
    public static String toString(
            final Reader        reader,
            final String        encoding,
            final boolean       closing
            ) {

        // nullの場合は例外
        if (reader == null) {

            throw new IllegalArgumentException();

        }

        try {

            // 文字列を作成して返す
            return new String(toByteArray(reader, encoding, closing), encoding);

        } catch (final UnsupportedEncodingException e) {

            e.printStackTrace();

            // 失敗
            return null;

        }

    }


    /**
     * 指定された入力ストリームを指定エンコーディングで文字列へ変換する。
     *
     * @param in        読み込む入力ストリーム
     * @param encoding  変換エンコーディング
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 指定された入力ストリームの文字列表現。失敗した場合は null
     */
    public static String toString(
            final InputStream   in,
            final String        encoding,
            final boolean       closing
            ) {

        // nullの場合は例外
        if (in == null) {

            throw new IllegalArgumentException();

        }

        try {

            // 文字列を作成して返す
            return new String(toByteArray(in, closing), encoding);

        } catch (final UnsupportedEncodingException e) {

            e.printStackTrace();

            // 失敗
            return null;

        }

    }


    /**
     * 指定された入力ストリームのバイトサイズを取得する。
     *
     * @param in        サイズを取得する入力ストリーム
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 指定された入力ストリームのバイトサイズ。取得に失敗した場合は -1
     */
    public static long length(
            final InputStream   in,
            final boolean       closing
            ) {

        // nullの場合は例外
        if (in == null) {

            throw new IllegalArgumentException();

        }

        final byte[]    readBuffer = new byte[BUFFER_SIZE]; // 読み込みバッファ
        int             readLength;                         // 読み込みサイズ
        long            totalSize  = 0;                     // 読み込みサイズ合計

        try {

            // 読み込みバッファへデータを読み込む
            readLength = in.read(readBuffer);

            // 読み込みバイト数が -1 以外であれば繰り返し
            while (readLength != -1) {

                // 読み込みサイズを加算する
                totalSize += readLength;

                // 次のデータを読み込む
                readLength = in.read(readBuffer);

            }

        } catch (final IOException e) {

            e.printStackTrace();

            // 失敗
            return -1;

        } finally {

            // クローズする場合
            if (closing) {

                // 入力ストリームをクローズする
                closeQuietly(in);

            }

        }

        // 算出したサイズを返す
        return totalSize;

    }


    /**
     * 指定されたバイト配列を出力ストリームへ書き込む。
     *
     * @param data      書き込むバイトデータ
     * @param out       書き込み先出力ストリーム
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final byte[]        data,
            final OutputStream  out,
            final boolean       closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            try {

                // バイトデータを書き込む
                out.write(data);

            } catch (final IOException e) {

                e.printStackTrace();

                // 失敗
                return false;

            } finally {

                // クローズする場合
                if (closing) {

                    // 出力ストリームをクローズする
                    closeQuietly(out);

                }

            }

        }

        // 成功
        return true;

    }


    /**
     * 指定されたバイト配列をデフォルトエンコーディングでライターへ書き込む。
     *
     * @param data      書き込むバイトデータ
     * @param writer    書き込み先ライター
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final byte[]    data,
            final Writer    writer,
            final boolean   closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            try {

                // 文字列へ変換して書き込む
                writer.write(new String(data));

            } catch (final IOException e) {

                e.printStackTrace();

                // 失敗
                return false;

            } finally {

                // クローズする場合
                if (closing) {

                    // ライターをクローズする
                    closeQuietly(writer);

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定されたバイト配列を指定エンコーディングでライターへ書き込む。
     *
     * @param data      書き込むバイトデータ
     * @param writer    書き込み先ライター
     * @param encoding  書き込みエンコーディング
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final byte[]    data,
            final Writer    writer,
            final String    encoding,
            final boolean   closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            // エンコーディング指定がない場合
            if (encoding == null) {

                // デフォルトエンコーディングで書き込む
                return write(data, writer, closing);

            // エンコーディング指定がある場合
            } else {

                try {

                    // 指定されたエンコーディングで書き込む
                    writer.write(new String(data, encoding));

                } catch (final IOException e) {

                    e.printStackTrace();

                    // 失敗
                    return false;

                } finally {

                    // クローズする場合
                    if (closing) {

                        // ライターをクローズする
                        closeQuietly(writer);

                    }

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された文字配列をライターへ書き込む。
     *
     * @param data      書き込む文字配列
     * @param writer    書き込み先ライター
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final char[]    data,
            final Writer    writer,
            final boolean   closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            try {

                // 文字配列を書き込む
                writer.write(data);

            } catch (final IOException e) {

                e.printStackTrace();

                // 失敗
                return false;

            } finally {

                // クローズする場合
                if (closing) {

                    // ライターをクローズする
                    closeQuietly(writer);

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された文字配列をデフォルトエンコーディングで出力ストリームへ書き込む。
     *
     * @param data      書き込む文字配列
     * @param out       書き込み先出力ストリーム
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final char[]        data,
            final OutputStream  out,
            final boolean       closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            try {

                // 文字配列をデフォルトエンコーディングで書き込む
                out.write(new String(data).getBytes());

            } catch (final IOException e) {

                e.printStackTrace();

                // 失敗
                return false;

            } finally {

                // クローズする場合
                if (closing) {

                    // 出力ストリームをクローズする
                    closeQuietly(out);

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された文字配列を指定エンコーディングで出力ストリームへ書き込む。
     *
     * @param data      書き込む文字配列
     * @param out       書き込み先出力ストリーム
     * @param encoding  書き込みエンコーディング
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final char[]        data,
            final OutputStream  out,
            final String        encoding,
            final boolean       closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            // エンコーディング指定がない場合
            if (encoding == null) {

                // デフォルトエンコーディングで書き込む
                return write(data, out, closing);

            } else {

                try {

                    // 指定エンコーディングで文字配列を書き込む
                    out.write(new String(data).getBytes(encoding));

                } catch (final IOException e) {

                    e.printStackTrace();

                    // 失敗
                    return false;

                } finally {

                    // クローズする場合
                    if (closing) {

                        // 出力ストリームをクローズする
                        closeQuietly(out);

                    }

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された文字列をライターへ書き込む。
     *
     * @param data      書き込む文字列
     * @param writer    書き込み先ライター
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final String    data,
            final Writer    writer,
            final boolean   closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            try {

                // 文字列を書き込む
                writer.write(data);

            } catch (final IOException e) {

                e.printStackTrace();

                // 失敗
                return false;

            } finally {

                // クローズする場合
                if (closing) {

                    // ライターをクローズする
                    closeQuietly(writer);

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された文字列をデフォルトエンコーディングで出力ストリームへ書き込む。
     *
     * @param data      書き込む文字列
     * @param out       書き込み先出力ストリーム
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final String        data,
            final OutputStream  out,
            final boolean       closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            try {

                // デフォルトエンコーディングで文字列を書き込む
                out.write(data.getBytes());

            } catch (final IOException e) {

                e.printStackTrace();

                // 失敗
                return false;

            } finally {

                // クローズする場合
                if (closing) {

                    // 出力ストリームをクローズする
                    closeQuietly(out);

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された文字列を指定エンコーディングで出力ストリームへ書き込む。
     *
     * @param data      書き込む文字列
     * @param out       書き込み先出力ストリーム
     * @param encoding  書き込みエンコーディング
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final String        data,
            final OutputStream  out,
            final String        encoding,
            final boolean       closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            // エンコーディング指定がある場合
            if (encoding == null) {

                // デフォルトエンコーディングで書き込む
                return write(data, out, closing);

            } else {

                try {

                    // 指定エンコーディングで文字列を書き込む
                    out.write(data.getBytes(encoding));

                } catch (final IOException e) {

                    e.printStackTrace();

                    // 失敗
                    return false;

                } finally {

                    // クローズする場合
                    if (closing) {

                        // 出力ストリームをクローズする
                        closeQuietly(out);

                    }

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された可変文字列をライターへ書き込む。
     *
     * @param data      書き込む可変文字列
     * @param writer    書き込み先ライター
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final StringBuffer  data,
            final Writer        writer,
            final boolean       closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            try {

                // 文字列を書き込む
                writer.write(data.toString());

            } catch (final IOException e) {

                e.printStackTrace();

                // 失敗
                return false;

            } finally {

                // クローズする場合
                if (closing) {

                    // ライターをクローズする
                    closeQuietly(writer);

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された可変文字列をデフォルトエンコーディングで出力ストリームへ書き込む。
     *
     * @param data      書き込む可変文字列
     * @param out       書き込み先出力ストリーム
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final StringBuffer  data,
            final OutputStream  out,
            final boolean       closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            try {

                // デフォルトエンコーディングで文字列を書き込む
                out.write(data.toString().getBytes());

            } catch (final IOException e) {

                e.printStackTrace();

                // 失敗
                return false;

            } finally {

                // クローズする場合
                if (closing) {

                    // 出力ストリームをクローズする
                    closeQuietly(out);

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * 指定された可変文字列を指定エンコーディングで出力ストリームへ書き込む。
     *
     * @param data      書き込む可変文字列
     * @param out       書き込み先出力ストリーム
     * @param encoding  書き込みエンコーディング
     * @param closing   処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean write(
            final StringBuffer  data,
            final OutputStream  out,
            final String        encoding,
            final boolean       closing
            ) {

        // 書き込みデータがある場合
        if (data != null) {

            // エンコーディング指定がある場合
            if (encoding == null) {

                // デフォルトエンコーディングで書き込む
                return write(data, out, closing);

            } else {

                try {

                    // 指定エンコーディングで文字列を書き込む
                    out.write(data.toString().getBytes(encoding));

                } catch (final IOException e) {

                    e.printStackTrace();

                    // 失敗
                    return false;

                } finally {

                    // クローズする場合
                    if (closing) {

                        // 出力ストリームをクローズする
                        closeQuietly(out);

                    }

                }

            }

        }


        // 成功
        return true;

    }


    /**
     * リストに設定された複数行を指定された区切り文字を使用して
     * デフォルトエンコーディングで出力ストリームへ書き込む。
     *
     * @param lines         書き込む複数行
     * @param lineSeparator 書き込む複数行の行間区切り文字列
     * @param out           書き込み先出力ストリーム
     * @param closing       処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean writeLines(
            final List<String>  lines,
            final String        lineSeparator,
            final OutputStream  out,
            final boolean       closing
            ) {

        return writeLines(lines, lineSeparator, out, null, closing);

    }


    /**
     * リストに設定された複数行を指定された区切り文字を使用して
     * 指定エンコーディングで出力ストリームへ書き込む。
     *
     * @param lines         書き込む複数行
     * @param lineSeparator 書き込む複数行の行間区切り文字列
     * @param out           書き込み先出力ストリーム
     * @param encoding      書き込みエンコーディング
     * @param closing       処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean writeLines(
            final List<String>  lines,
            final String        lineSeparator,
            final OutputStream  out,
            final String        encoding,
            final boolean       closing
            ) {

        try {

            final OutputStreamWriter    writer = new OutputStreamWriter(out, encoding);

            // 複数行を書き込む
            return writeLines(lines, lineSeparator, writer, closing);

        } catch (final UnsupportedEncodingException e) {

            e.printStackTrace();

            // 失敗
            return false;

        } finally {

            // クローズする場合
            if (closing) {

                // 出力ストリームをクローズする
                closeQuietly(out);

            }

        }

    }


    /**
     * リストに設定された複数行を指定された区切り文字を使用して
     * 指定エンコーディングでライターへ書き込む。
     *
     * @param lines         書き込む複数行
     * @param lineSeparator 書き込む複数行の行間区切り文字列
     * @param writer        書き込み先ライター
     * @param closing       処理完了またはエラー発生時にストリームをクローズするかどうか
     * @return 処理が成功したかどうか
     */
    public static boolean writeLines(
            final List<String>  lines,
            final String        lineSeparator,
            final Writer        writer,
            final boolean       closing
            ) {

        // リストが null の場合は例外
        if (lines == null) {

            throw new IllegalArgumentException();

        }


        final String    localLineSplitToken;    // 使用する行区切り文字列

        // 区切り文字列が設定されていない場合
        if (lineSeparator == null) {

            // デフォルト区切り文字列を設定する
            localLineSplitToken = LINE_SEPARATOR;

        } else {

            // 指定された区切り文字列を設定する
            localLineSplitToken = lineSeparator;

        }


        try {

            // 複数行を書き込む
            for (final Iterator<String> it = lines.iterator(); it.hasNext();) {

                final String line = it.next();

                // 次の行がある場合
                if (line != null) {

                    // 次の行を書き込む
                    writer.write(line.toString());

                }

                // 行間区切り文字を書き込む
                writer.write(localLineSplitToken);

            }

        } catch (final IOException e) {

            e.printStackTrace();

            // 失敗
            return false;

        } finally {

            // クローズする場合
            if (closing) {

                // ライターをクローズする
                closeQuietly(writer);

            }

        }


        // 成功
        return true;

    }

}
