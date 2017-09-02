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
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * 行読み込みイテレーター。<br>
 * <br>
 * 本イテレーターを使用することで、<br>
 * テキストデータから複数行を読み込む場合の処理が容易になる。<br>
 * 使用例としては以下のようになる。<br>
 * <pre>
 * LineIterator it = IOUtils.lineIterator(reader);
 *
 * try {
 *
 *     while (it.hasNext()) {
 *         String line = it.nextLine();
 *     }
 *
 * } finally {
 *
 *     LineIterator.closeQuietly(iterator);
 *
 * }
 * </pre>
 *
 * @author Kou
 *
 */
public class LineIterator implements Iterator<String> {


    /**
     * 元となるバッファリーダー
     */
    private final BufferedReader        lineReader;

    /**
     * 次の行のキャッシュデータ
     */
    private String                      lineNextCache;

    /**
     * 最後まで読み込み完了しているかどうか
     */
    private boolean                     lineFinished;

    /**
     * 読み込んだ行数
     */
    private int                         lineReadCount;



    /**
     * ラインイテレーターを初期化する。
     *
     * @param reader 元となるデータのあるリーダー
     */
    public LineIterator(
            final Reader    reader
            ) {

        // nullの場合は例外
        if (reader == null) {

            throw new IllegalArgumentException();

        }

        // 指定されたリーダーがバッファリーダーの場合
        if (reader instanceof BufferedReader) {

            // そのまま設定する
            lineReader = (BufferedReader)reader;

        } else {

            // 新規バッファリーダーを作成して設定する
            lineReader = new BufferedReader(reader);

        }

    }


    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {

        // キャッシュがある場合
        if (lineNextCache != null) {

            // 次の要素がみつかった
            return true;

        // 最後まで読み込み完了している場合
        } else if (lineFinished) {

            // 次の要素がみつからなかった
            return false;

        } else {

            try {

                // 行検索ループ
                while (true) {

                    // 次の行を読み込む
                    final String  line = lineReader.readLine();

                    // 次の行がない場合
                    if (line == null) {

                        // 最後まで読み込み完了したことにする
                        lineFinished = true;

                        // 次の要素がみつからなかった
                        return false;

                    } else {

                        // 読み込んだ行をキャッシュへ設定する
                        lineNextCache = line;

                        // 次の要素がみつかった
                        return true;

                    }

                }

            } catch (final IOException e) {

                // イテレータを閉じる
                close();

                // 例外をスローする
                throw new IllegalStateException(e.toString());

            }


        }


    }


    /**
     * 次の行文字列を取得する。<br>
     * <br>
     * @return 次の行文字列
     */
    public String next() {

        // 次の要素がない場合は例外
        if (!hasNext()) {

            throw new NoSuchElementException();

        }

        // キャッシュの行を取得する
        final String    retLine = lineNextCache;

        // キャッシュをクリアする
        lineNextCache = null;

        // 読み込んだ行数 + 1
        lineReadCount++;

        // 取得した行を返す
        return retLine;

    }


    /**
     * 次の行のインデックスを取得する。<br>
     * <br>
     * @return 次の行のインデックス
     */
    public int nextIndex() {

        // 読み込んだ行数を返す
        return lineReadCount;

    }


    /**
     * 現在の要素を削除する。<br>
     * <br>
     * LineIterator における削除は未サポートとなる。<br>
     */
    public void remove() {

        // 削除は未サポート
        throw new UnsupportedOperationException();

    }


    /**
     * ラインイテレーターをクローズする。<br>
     * <br>
     * 参照しているリーダーをクローズする。<br>
     */
    public void close() {

        // 最後まで読み込み完了したことにする
        lineFinished = true;

        // バッファリーダーを閉じる
        IOUtils.closeQuietly(lineReader);

        // キャッシュをクリアする
        lineNextCache = null;

    }

}
