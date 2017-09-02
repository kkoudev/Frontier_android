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



/**
 * Base64エンコード・デコード操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class Base64 {


    /**
     * 標準Base64エンコードテーブル
     */
    private static final char[] ENCODE_TABLE_STANDARD = {

        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'

    };

    /**
     * URL利用可能エンコードテーブル
     */
    private static final char[] ENCODE_TABLE_URL_SAFE = {

        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'

    };

    /**
     * デコードテーブルサイズ
     */
    private static final int    DECODE_TABLE_SIZE       = 123;

    /**
     * デコードテーブル
     */
    private static final int[]  DECODE_TABLE            = new int[DECODE_TABLE_SIZE];

    /**
     * データ量に対するエンコードデータバッファサイズの倍数
     */
    private static final float  ENCODE_BUFFER_MULTIPLE  = 1.34f;

    /**
     * エンコードデータ量に対するデコードデータ量の倍数
     */
    private static final float  DECODE_BUFFER_MULTIPLE  = 0.75f;

    /**
     * Base64エンコードデータの1ブロック文字数
     */
    private static final int    ENCODE_BLOCK_CHAR_COUNT = 4;

    /**
     * 一度に処理するデータバイトサイズ<br>
     */
    private static final int    BLOCK_BYTE              = 3;

    /**
     * 1バイトのビット数
     */
    private static final int    BITS_PER_BYTE           = 8;

    /**
     * Base64処理ブロックのビットサイズ
     */
    private static final int    BLOCK_BIT               = 6;

    /**
     * byte型のint型変換用のマスク
     */
    private static final int    MASK_TO_INT             = 0xFF;

    /**
     * エンコードサイズ倍数に満たない場合に付加する文字
     */
    private static final char   PADDING_CHAR            = '=';

    /**
     * Base64処理ブロックマスク
     */
    private static final int    MASK_BLOCK              = (1 << BLOCK_BIT) - 1;

    /**
     * エンコード : ブロック 0 番目の右シフト値
     */
    private static final int    E_BLOCK_0_RIGHT_SHIFT   = BITS_PER_BYTE - BLOCK_BIT;

    /**
     * エンコード : ブロック 0 番目の左シフト値
     */
    private static final int    E_BLOCK_0_LEFT_SHIFT    = BLOCK_BIT - E_BLOCK_0_RIGHT_SHIFT;

    /**
     * エンコード : ブロック 1 番目の右シフト値
     */
    private static final int    E_BLOCK_1_RIGHT_SHIFT   = BITS_PER_BYTE - E_BLOCK_0_LEFT_SHIFT;

    /**
     * エンコード : ブロック 1 番目の左シフト値
     */
    private static final int    E_BLOCK_1_LEFT_SHIFT    = BLOCK_BIT - E_BLOCK_1_RIGHT_SHIFT;

    /**
     * エンコード : ブロック 2 番目の右シフト値
     */
    private static final int    E_BLOCK_2_RIGHT_SHIFT   = BITS_PER_BYTE - E_BLOCK_1_LEFT_SHIFT;

    /**
     * デコード : ブロック 0 番目の左シフト値
     */
    private static final int    D_BLOCK_0_LEFT_SHIFT    = E_BLOCK_0_RIGHT_SHIFT;

    /**
     * デコード : ブロック 1 番目の右シフト値
     */
    private static final int    D_BLOCK_1_RIGHT_SHIFT   = E_BLOCK_0_LEFT_SHIFT;

    /**
     * デコード : ブロック 1 番目の左シフト値
     */
    private static final int    D_BLOCK_1_LEFT_SHIFT    = E_BLOCK_1_RIGHT_SHIFT;

    /**
     * デコード : ブロック 2 番目の右シフト値
     */
    private static final int    D_BLOCK_2_RIGHT_SHIFT   = E_BLOCK_1_LEFT_SHIFT;

    /**
     * デコード : ブロック 2 番目の左シフト値
     */
    private static final int    D_BLOCK_2_LEFT_SHIFT    = E_BLOCK_2_RIGHT_SHIFT;




    /**
     * デコードテーブルを作成する
     */
    static {

        // 標準エンコードテーブルを処理する
        for (int i = 0; i < ENCODE_TABLE_STANDARD.length; i++) {

            // デコードテーブルに値を設定する
            DECODE_TABLE[ENCODE_TABLE_STANDARD[i]] = i;

        }

        // URL用エンコードテーブルを処理する
        for (int i = 0; i < ENCODE_TABLE_URL_SAFE.length; i++) {

            // デコードテーブルに値を設定する
            DECODE_TABLE[ENCODE_TABLE_URL_SAFE[i]] = i;

        }

    }



    /**
     * インスタンス生成防止。
     *
     */
    private Base64() {

        // 処理なし

    }


    /**
     * 指定されたデータをBase64エンコードする。
     *
     * @param data      エンコードするデータ
     * @return Base64エンコードした文字列データ
     */
    public static String encode(
            final byte[]    data
            ) {

        return encode(data, false);

    }


    /**
     * 指定されたデータをBase64エンコードする。
     *
     * @param data      エンコードするデータ
     * @param urlSafe   URL利用可能なBase64エンコードを行うかどうか
     * @return Base64エンコードした文字列データ
     */
    public static String encode(
            final byte[]    data,
            final boolean   urlSafe
            ) {

        // 引数が不正の場合は例外
        if (data == null) {

            throw new IllegalArgumentException();

        }


        final StringBuilder strBuf      = new StringBuilder(
                                            (int)(data.length * ENCODE_BUFFER_MULTIPLE));   // 返却文字列バッファ
        int                 indexBlock  = 0;                                                // 処理データインデックス

        // エンコードテーブルを取得する
        final char[]    encodeTable = (urlSafe ? ENCODE_TABLE_URL_SAFE : ENCODE_TABLE_STANDARD);


        // 最後のブロックを除いた全データを処理する
        while (indexBlock + BLOCK_BYTE < data.length) {

            final int   block0 = data[indexBlock++] & MASK_TO_INT;     // 0番目データ
            final int   block1 = data[indexBlock++] & MASK_TO_INT;     // 1番目データ
            final int   block2 = data[indexBlock++] & MASK_TO_INT;     // 2番目データ

            // Base64エンコードしながらデータを格納する
            strBuf.append(
                    encodeTable[(block0 >>> E_BLOCK_0_RIGHT_SHIFT) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[((block0 << E_BLOCK_0_LEFT_SHIFT)
                                 | (block1 >>> E_BLOCK_1_RIGHT_SHIFT)) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[((block1 << E_BLOCK_1_LEFT_SHIFT)
                                 | (block2 >>> E_BLOCK_2_RIGHT_SHIFT)) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[block2 & MASK_BLOCK]
                    );

        }


        // 余りブロック数を算出する
        final int   remainBlockCount = data.length - indexBlock;
        final int   remainBlock1     = 1;
        final int   remainBlock2     = 2;
        final int   remainBlock3     = 3;

        // 余りブロック別処理
        switch (remainBlockCount) {

        // 余りブロック数が 1 つの場合
        case remainBlock1:

            final int   block10 = data[indexBlock++] & MASK_TO_INT;     // 0番目データ

            // Base64エンコードしながらデータを格納する
            strBuf.append(
                    encodeTable[(block10 >>> E_BLOCK_0_RIGHT_SHIFT) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[(block10 << E_BLOCK_0_LEFT_SHIFT) & MASK_BLOCK]
                    );
            break;


        // 余りブロック数が 2 つの場合
        case remainBlock2:

            final int   block20 = data[indexBlock++] & MASK_TO_INT;     // 0番目データ
            final int   block21 = data[indexBlock++] & MASK_TO_INT;     // 1番目データ

            // Base64エンコードしながらデータを格納する
            strBuf.append(
                    encodeTable[(block20 >>> E_BLOCK_0_RIGHT_SHIFT) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[((block20 << E_BLOCK_0_LEFT_SHIFT)
                                 | (block21 >>> E_BLOCK_1_RIGHT_SHIFT)) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[(block21 << E_BLOCK_1_LEFT_SHIFT) & MASK_BLOCK]
                    );
            break;


        // 余りブロック数が 3 つの場合
        case remainBlock3:

            final int   block30 = data[indexBlock++] & MASK_TO_INT;     // 0番目データ
            final int   block31 = data[indexBlock++] & MASK_TO_INT;     // 1番目データ
            final int   block32 = data[indexBlock++] & MASK_TO_INT;     // 2番目データ

            // Base64エンコードしながらデータを格納する
            strBuf.append(
                    encodeTable[(block30 >>> E_BLOCK_0_RIGHT_SHIFT) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[((block30 << E_BLOCK_0_LEFT_SHIFT)
                                 | (block31 >>> E_BLOCK_1_RIGHT_SHIFT)) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[((block31 << E_BLOCK_1_LEFT_SHIFT)
                                 | (block32 >>> E_BLOCK_2_RIGHT_SHIFT)) & MASK_BLOCK]
                    );
            strBuf.append(
                    encodeTable[block32 & MASK_BLOCK]
                    );
            break;


        // その他
        default:

            // 処理なし
            break;

        }


        // パディングサイズを算出する
        int   padding = (((strBuf.length() + (ENCODE_BLOCK_CHAR_COUNT - 1))
                         / ENCODE_BLOCK_CHAR_COUNT) * ENCODE_BLOCK_CHAR_COUNT) - strBuf.length();

        // パディングサイズ分だけパディング文字を付加する
        while (padding > 0) {

            // パディング文字を追加する
            strBuf.append(PADDING_CHAR);

            // パディング数をデクリメントする
            padding--;

        }


        // 作成した文字列を返す
        return strBuf.toString();

    }


    /**
     * 指定されたBase64エンコードデータをバイトデータへデコードする。<br>
     *
     * @param encodeString  Base64エンコードデータ
     * @return デコードしたバイトデータ
     */
    public static byte[] decode(
            final String    encodeString
            ) {

        // 引数が不正の場合は例外
        if (encodeString == null) {

            throw new IllegalArgumentException();

        }


        final StringBuilder     strBuf          = new StringBuilder(encodeString);  // エンコード文字列バッファ
        int                     quadLength      = 0;                                // 改行文字を除いたデータ長
        int                     paddingLength   = 0;                                // パディングデータ長

        // データサイズを取得する
        for (int i = 0; i < strBuf.length(); i++) {

            final char  workChar = strBuf.charAt(i);

            // 改行文字またはパディング文字の場合
            if ((workChar == '\r') || (workChar == '\n')) {

                // 現在位置の文字を削除する
                strBuf.deleteCharAt(i);

            } else {

                // 改行文字を除いたデータ長をインクリメント
                quadLength++;

            }

        }

        // パディングデータ長を取得する
        for (int i = strBuf.length() - 1;
             (i >= strBuf.length() - ENCODE_BLOCK_CHAR_COUNT) && (i >= 0); i--
             ) {

            // パディング文字の場合
            if (strBuf.charAt(i) == PADDING_CHAR) {

                // パディング文字数をインクリメント
                paddingLength++;

            }

        }


        final byte[]    retBuffer;                          // 返却データバッファ
        final int       strLength   = strBuf.length();      // エンコード文字数
        int             indexChar   = 0;                    // エンコード文字インデックス
        int             indexBuffer = 0;                    // 処理バッファインデックス


        // 文字数が 1 ブロック文字数の倍数でない場合は例外
        if (strLength % ENCODE_BLOCK_CHAR_COUNT != 0) {

            throw new IllegalArgumentException("Illegal encoding parameter.");

        }


        // 返却データバッファを作成する
        retBuffer = new byte[(int)((quadLength - paddingLength) * DECODE_BUFFER_MULTIPLE)];

        // 1ブロックの文字数分ずつ処理をする
        while (indexChar + ENCODE_BLOCK_CHAR_COUNT < strLength) {

            // 文字データを処理ブロック文字数分取得する
            final int   codeBlock0 = strBuf.charAt(indexChar++);
            final int   codeBlock1 = strBuf.charAt(indexChar++);
            final int   codeBlock2 = strBuf.charAt(indexChar++);
            final int   codeBlock3 = strBuf.charAt(indexChar++);

            // データをデコードして追加する
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock0] << D_BLOCK_0_LEFT_SHIFT) & MASK_TO_INT)
                        | ((DECODE_TABLE[codeBlock1] >>> D_BLOCK_1_RIGHT_SHIFT) & MASK_TO_INT));
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock1] << D_BLOCK_1_LEFT_SHIFT) & MASK_TO_INT)
                        | ((DECODE_TABLE[codeBlock2] >>> D_BLOCK_2_RIGHT_SHIFT) & MASK_TO_INT));
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock2] << D_BLOCK_2_LEFT_SHIFT) & MASK_TO_INT)
                        | (DECODE_TABLE[codeBlock3] & MASK_TO_INT));

        }


        // 余りブロック数を算出する
        final int   remainBlockCount = strLength - indexChar - paddingLength;
        final int   remainBlock2     = 2;
        final int   remainBlock3     = 3;
        final int   remainBlock4     = 4;

        // 余りブロック別処理
        switch (remainBlockCount) {

        // 2ブロック残っている場合
        case remainBlock2:

            // 文字データを処理ブロック文字数分取得する
            final int   codeBlock20 = strBuf.charAt(indexChar++);
            final int   codeBlock21 = strBuf.charAt(indexChar++);

            // データをデコードして追加する
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock20] << D_BLOCK_0_LEFT_SHIFT) & MASK_TO_INT)
                        | ((DECODE_TABLE[codeBlock21] >>> D_BLOCK_1_RIGHT_SHIFT) & MASK_TO_INT));
            break;


        // 3ブロック残っている場合
        case remainBlock3:

            // 文字データを処理ブロック文字数分取得する
            final int   codeBlock30 = strBuf.charAt(indexChar++);
            final int   codeBlock31 = strBuf.charAt(indexChar++);
            final int   codeBlock32 = strBuf.charAt(indexChar++);

            // データをデコードして追加する
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock30] << D_BLOCK_0_LEFT_SHIFT) & MASK_TO_INT)
                        | ((DECODE_TABLE[codeBlock31] >>> D_BLOCK_1_RIGHT_SHIFT) & MASK_TO_INT));
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock31] << D_BLOCK_1_LEFT_SHIFT) & MASK_TO_INT)
                        | ((DECODE_TABLE[codeBlock32] >>> D_BLOCK_2_RIGHT_SHIFT) & MASK_TO_INT));
            break;


        // 4ブロック残っている場合
        case remainBlock4:

            // 文字データを処理ブロック文字数分取得する
            final int   codeBlock40 = strBuf.charAt(indexChar++);
            final int   codeBlock41 = strBuf.charAt(indexChar++);
            final int   codeBlock42 = strBuf.charAt(indexChar++);
            final int   codeBlock43 = strBuf.charAt(indexChar++);

            // データをデコードして追加する
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock40] << D_BLOCK_0_LEFT_SHIFT) & MASK_TO_INT)
                        | ((DECODE_TABLE[codeBlock41] >>> D_BLOCK_1_RIGHT_SHIFT) & MASK_TO_INT));
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock41] << D_BLOCK_1_LEFT_SHIFT) & MASK_TO_INT)
                        | ((DECODE_TABLE[codeBlock42] >>> D_BLOCK_2_RIGHT_SHIFT) & MASK_TO_INT));
            retBuffer[indexBuffer++] =
                (byte)(((DECODE_TABLE[codeBlock42] << D_BLOCK_2_LEFT_SHIFT) & MASK_TO_INT)
                        | (DECODE_TABLE[codeBlock43] & MASK_TO_INT));
            break;


        // その他
        default:

            // 処理なし
            break;

        }


        // デコードしたデータを返す
        return retBuffer;

    }


}
