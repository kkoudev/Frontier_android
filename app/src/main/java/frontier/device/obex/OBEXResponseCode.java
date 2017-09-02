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

import java.util.HashMap;
import java.util.Map;


/**
 * OBEXレスポンスコード列挙型。<br>
 * <br>
 * OBEXのレスポンスコードは HTTP のステータスコードと同等の意味を持つ。<br>
 * コード値については 1 byteに収まるように上位桁を[0xA0 = 200, 0xB0 = 300, 0xC0 = 400 ...]と表現し、<br>
 * 下位4bitのデータについては、ステータスコードと同様の値が振られている。<br>
 *
 * @author Kou
 *
 */
public enum OBEXResponseCode {


    /**
     * 情報提供 (100)
     */
    CONTINUE(0x90),

    /**
     * OKです (200)
     */
    OK(0xA0),

    /**
     * 作成されました (201)
     */
    CREATED(0xA1),

    /**
     * 許可されました (202)
     */
    ACCEPTED(0xA2),

    /**
     * 不当な情報です (203)
     */
    NON_AUTH_INF(0xA3),

    /**
     * コンテンツがありません (204)
     */
    NO_CONTENT(0xA4),

    /**
     * コンテンツをリセットします (205)
     */
    RESET_CONTENT(0xA5),

    /**
     * 部分的なコンテンツです (206)
     */
    PARTIAL_CONTENT(0xA6),

    /**
     * 複数選択されています (300)
     */
    MULTIPLE_CHOICES(0xB0),

    /**
     * 永続的に移動されました (301)
     */
    MOVED_PERMANENTLY(0xB1),

    /**
     * 一時的に切り替えます (302)
     */
    MOVED_TEMPORARILY(0xB2),

    /**
     * 他を参照してください (303)
     */
    SEE_OTHER(0xB3),

    /**
     * 修正されませんでした (304)
     */
    NOT_MODIFIED(0xB4),

    /**
     * プロキシを使用してください (305)
     */
    USE_PROXY(0xB5),

    /**
     * 不当な要求です (400)
     */
    BAD_REQUEST(0xC0),

    /**
     * 認証されませんでした (401)
     */
    UNAUTHORIZED(0xC1),

    /**
     * 支払いが必要です (402)
     */
    PAYMENT_REQUIRED(0xC2),

    /**
     * 禁止されています (403)
     */
    FORBIDDEN(0xC3),

    /**
     * 見つかりませんでした (404)
     */
    NOT_FOUND(0xC4),

    /**
     * メソッドは許可されません (405)
     */
    METHOD_NOT_ALLOWED(0xC5),

    /**
     * 許容されません (406)
     */
    NOT_ACEPTABLE(0xC6),

    /**
     * プロキシの認証が必要です (407)
     */
    PROXY_AUTH_REQUIRED(0xC7),

    /**
     * 要求が時間切れです (408)
     */
    REQUEST_TIMEOUT(0xC8),

    /**
     * 重複しています (409)
     */
    CONFLICT(0xC9),

    /**
     * 移動しました (410)
     */
    GONE(0xCA),

    /**
     * 長さが必要です (411)
     */
    LENGTH_REQUIRED(0xCB),

    /**
     * 前提条件が正しくありません (412)
     */
    PRECON_FAILED(0xCC),

    /**
     * 要求エンティティが長すぎます (413)
     */
    ENTITY_TOO_LARGE(0xCD),

    /**
     * 要求 URL が長すぎます (414)
     */
    REQUEST_URL_TOO_LARGE(0xCE),

    /**
     * サポートされないメディアタイプです (415)
     */
    UNSUPPORTED_MEDIA_TYPE(0xCF),

    /**
     * 内部サーバエラーです (500)
     */
    INTERNAL_SERVER_ERROR(0xD0),

    /**
     * 実装されていません (501)
     */
    NOT_IMPLEMENTED(0xD1),

    /**
     * 誤ったゲートウェイです (502)
     */
    BAD_GATEWAY(0xD2),

    /**
     * サービスが利用できません (503)
     */
    SERVICE_UNAVAILABLE(0xD3),

    /**
     * ゲートウェイが時間切れです (504)
     */
    GATEWAY_TIMEOUT(0xD4),

    /**
     * HTTP バージョンがサポートされていません (505)
     */
    UNSUPPORTED_VERSION(0xD5),

    /**
     * データベースフル (OBEX独自コード)
     */
    DATABASE_FULL(0xE0),

    /**
     * データベースロック (OBEX独自コード)
     */
    DATABASE_LOCKED(0xE1);



    /**
     * レスポンスコード変換テーブル<br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>キー</td><td>Byte</td><td>OBEXレスポンスコード値</td>
     * </tr>
     * <tr>
     *   <td>値</td><td>OBEXResponseCode</td><td>OBEXレスポンスコード種別</td>
     * </tr>
     * </table>
     */
    private static final Map<Byte, OBEXResponseCode>    CODE_TABLE =
        new HashMap<Byte, OBEXResponseCode>();


    /**
     * レスポンスコード値
     */
    private final byte      code;



    /**
     * 変換テーブルを初期化する
     */
    static {

        // 全レスポンスコード分処理をする
        for (final OBEXResponseCode code : OBEXResponseCode.values()) {

            // イベントテーブルへ追加する
            CODE_TABLE.put(code.getCode(), code);

        }

    }



    /**
     * OBEXレスポンスコードを初期化する。
     *
     * @param code 設定するレスポンスコード値
     */
    private OBEXResponseCode(
            final int  code
            ) {

        this.code = (byte)code;

    }


    /**
     * レスポンスコードを取得する。
     *
     * @return レスポンスコード
     */
    public byte getCode() {

        return code;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        return String.format("0x%x", code);

    }


    /**
     * 指定された値をレスポンスコード種別へ変換する。
     *
     * @param oneByte 変換する値
     * @return 指定された値に対応するレスポンスコード種別。対応するデータがない場合は null
     */
    public static OBEXResponseCode toType(
            final int  oneByte
            ) {

        // 対応するレスポンスコードを返す
        return CODE_TABLE.get((byte)oneByte);

    }


}
