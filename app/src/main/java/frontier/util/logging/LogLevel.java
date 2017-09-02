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
package frontier.util.logging;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;


/**
 * ログレベルを定義した列挙型。
 *
 * @author Kou
 *
 */
public enum LogLevel {


    /**
     * ログレベル : VERBOSE<br>
     * <br>
     * トレース目的で利用するログに設定するレベル
     */
    VERBOSE("VERBOSE", Log.VERBOSE),

    /**
     * ログレベル : DEBUG<br>
     * <br>
     * 一般的なレベル。デバッグのためのパラメータ確認などのログに設定するレベル
     */
    DEBUG("DEBUG", Log.DEBUG),

    /**
     * ログレベル : INFO<br>
     * <br>
     * 運用での確認のために出力する必要のあるログに設定するレベル
     */
    INFO("INFO", Log.INFO),

    /**
     * ログレベル : WARN<br>
     * <br>
     * 無視しても問題ないが、何か問題が発生した場合に出力するログに設定するレベル
     */
    WARN("WARN", Log.WARN),

    /**
     * ログレベル : ERROR<br>
     * <br>
     * 明らかなエラーが発生した場合に出力するログに設定するレベル
     */
    ERROR("ERROR", Log.ERROR),

    /**
     * ログレベル : ASSERT<br>
     * <br>
     * アプリが続行不能となるような致命的なエラーが発生した場合に出力するログに設定するレベル
     */
    ASSERT("ASSERT", Log.ASSERT);



    /**
     * ログレベル変換テーブルを初期化する
     */
    static {

        // ログレベル変換テーブルを作成する
        final Map<String, LogLevel> levelTable = new HashMap<String, LogLevel>();

        // ログレベル名をキーにしてログレベルをテーブルへ追加する
        levelTable.put(VERBOSE.getName(), VERBOSE);
        levelTable.put(DEBUG.getName(), DEBUG);
        levelTable.put(INFO.getName(), INFO);
        levelTable.put(WARN.getName(), WARN);
        levelTable.put(ERROR.getName(), ERROR);
        levelTable.put(ASSERT.getName(), ASSERT);

        // 作成したログレベル変換テーブルを設定する
        LOG_LEVELS = levelTable;

    }


    /**
     * ログレベル変換テーブル<br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>キー</td><td>String</td><td>ログレベル名</td>
     * </tr>
     * <tr>
     *   <td>値</td><td>LogLevel</td><td>ログレベル</td>
     * </tr>
     * </table>
     */
    private static final Map<String, LogLevel>     LOG_LEVELS;

    /**
     * ログレベル名
     */
    private final String    logLevelName;

    /**
     * ログレベル種別値
     */
    private final int       logLevelType;



    /**
     * ログレベルを初期化する。
     *
     * @param name  ログレベル名
     * @param type  ログレベル種別値
     */
    private LogLevel(
            final String    name,
            final int       type
            ) {

        logLevelName = name;
        logLevelType = type;

    }


    /**
     * ログレベル名を取得する。
     *
     * @return ログレベル名
     */
    final String getName() {

        return logLevelName;

    }


    /**
     * ログレベル種別値を取得する。
     *
     * @return ログレベル種別値
     */
    final int getType() {

        return logLevelType;

    }


    /**
     * 指定名称をログレベルを変換する。
     *
     * @param name  変換する名前
     * @return 変換したログレベル
     */
    public static LogLevel toLogLevel(
            final String    name
            ) {

        // 名前が null の場合
        if (name == null) {

            // nullを返す
            return null;

        }

        // 指定した名前に対応する種別を返す
        return LOG_LEVELS.get(name.toUpperCase());

    }


}
