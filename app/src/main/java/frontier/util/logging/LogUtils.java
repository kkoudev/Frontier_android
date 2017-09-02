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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import frontier.util.DateUtils;
import frontier.util.FileUtils;
import frontier.util.IOUtils;
import frontier.util.PreferencesUtils;


/**
 * ログ出力用ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class LogUtils {


    /**
     * プリファレンスキー : ログ設定ファイル名<br>
     * <br>
     * %s = クラス名<br>
     */
    private static final String                     PREF_KEY_CONFIG_FILE_NAME_FORMAT =
        "%s - PREF_KEY_CONFIG_FILE_NAME_FORMAT";

    /**
     * 文字コード
     */
    private static final String                     ENCODING  = "UTF-8";

    /**
     * 改行コード
     */
    private static final String                     LINE_FEED = "\n";

    /**
     * ネスト化診断コンテキストのスタック情報間の区切りトークン
     */
    private static final String                     TOKEN_NDC_SPLIT = " ";

    /**
     * 日付指定時のデフォルトフォーマット
     */
    private static final String                     DEFAULT_DATE_FORMAT =
        "yyyy-MM-dd' 'hh:mm:ss.SSS";

    /**
     * フラグ文字
     */
    private static final char[]                     PATTERN_FLAGS = {

        // 結果は左揃えになります。
        '-',

        // 結果は、変換に依存する代替フォームを使用する必要があります。
        '#',

        // 結果には、常に符号が含まれます。
        '+',

        // 結果の先頭には、正の値を示す空白が含まれます。
        ' ',

        // 結果にはゼロが追加されます。
        '0',

        // 結果には、ロケール固有のグループ化区切り文字が含まれます。
        ',',

        // 負の数値を括弧で囲みます。
        '(',

    };

    /**
     * パターン文字配列
     */
    private static final char[]                     PATTERN_CONVERSIONS = new char[] {

        // カテゴリ名
        'c',

        // 日付
        'd',

        // タグ名
        'g',

        // 改行コード
        'n',

        // ログメッセージ
        'm',

        // ログレベル名
        'p',

        // カレントスレッド名
        't',

        // NDCで保存した文字列
        'x',

        // MDCで保存した文字列
        'X',

    };

    /**
     * フラグ文字検索テーブル<br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>要素</td><td>Character</td><td>フラグ文字</td>
     * </tr>
     * </table>
     */
    private static final Set<Character>                     SEARCH_FLAGS =
        new HashSet<Character>();

    /**
     * パターン文字検索テーブル<br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>要素</td><td>Character</td><td>パターン文字</td>
     * </tr>
     * </table>
     */
    private static final Set<Character>                     SEARCH_PATTERN_CONVERSIONS =
        new HashSet<Character>();

    /**
     * 使用するログカテゴリのテーブル<br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>要素</td><td>LogCategory</td><td>ログカテゴリ</td>
     * </tr>
     * </table>
     */
    private static final List<LogCategory>                  LOG_CATEGORIES =
        new ArrayList<LogCategory>();

    /**
     * ログカテゴリ情報のロックオブジェクト
     */
    private static final Object                             LOG_CATEGORY_LOCK = new Object();

    /**
     * ネスト化診断コンテキスト情報一覧<br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>要素</td><td>ArrayList&lt;String&gt;</td><td>出力するネスト化診断コンテキスト情報</td>
     * </tr>
     * </table>
     */
    private static final ThreadLocal<ArrayList<String>>     LOG_NDC;

    /**
     * マップ化診断コンテキスト情報一覧<br>
     * <br>
     * <br>
     * <table border="1">
     * <tr>
     *   <td>項目</td><td>型</td><td>内容</td>
     * </tr>
     * <tr>
     *   <td>要素</td><td>Map&lt;String, String&gt;</td><td>出力するマップ化診断コンテキスト情報</td>
     * </tr>
     * </table>
     */
    private static final ThreadLocal<Map<String, String>>   LOG_MDC;

    /**
     * 使用するルートカテゴリ
     */
    private static LogCategory                              logCategoryRoot;



    /**
     * 各種テーブルを初期化する
     */
    static {

        // フラグ文字検索テーブルを初期化する
        for (final char flag : PATTERN_FLAGS) {

            // テーブルへ追加する
            SEARCH_FLAGS.add(flag);

        }

        // パターン文字検索テーブルを初期化する
        for (final char patternConversion : PATTERN_CONVERSIONS) {

            // テーブルへ追加する
            SEARCH_PATTERN_CONVERSIONS.add(patternConversion);

        }


        // ネスト化診断コンテキスト情報を作成する
        LOG_NDC = new ThreadLocal<ArrayList<String>>() {

            @Override
            protected ArrayList<String> initialValue() {

                // 初期値に配列リストを設定する
                return new ArrayList<String>();

            }

        };

        // マップ化診断コンテキスト情報を作成する
        LOG_MDC = new ThreadLocal<Map<String, String>>() {

            @Override
            protected Map<String, String> initialValue() {

                // 初期値にハッシュマップを設定する
                return new HashMap<String, String>();

            }

        };

    }


    /**
     * インスタンス生成防止。
     */
    private LogUtils() {

        // 処理なし

    }


    /**
     * ログ設定ファイルのパース処理を行う。
     *
     * @param parser ログ設定ファイルのパーサー
     * @throws XmlPullParserException   XMLパースエラー時
     * @throws IOException              入出力エラー時
     */
    private static void parseConfiguration(
            final XmlPullParser     parser
            ) throws IOException, XmlPullParserException {

        final Map<String, LogAppender>  readAppenders;              // 処理したアペンダ一覧
        boolean                         isEnded         = false;    // 読み込み終了したかどうか
        LogAppender                     procAppender    = null;     // 処理中アペンダデータ
        LogCategory                     procCategory    = null;     // 処理中カテゴリデータ
        LogConfigType                   configType      = null;     // 処理中のログ設定種別


        // ログカテゴリ情報をロックする
        synchronized (LOG_CATEGORY_LOCK) {

            // ログカテゴリ情報をクリアする
            LOG_CATEGORIES.clear();
            logCategoryRoot = null;

            // 処理したアペンダ一覧を初期化する
            readAppenders = new HashMap<String, LogAppender>();

            // 読み込み終了するまで繰り返し
            for (int eventType = parser.getEventType();
                 !isEnded;
                 eventType = parser.next()
                 ) {

                // イベント別処理
                switch (eventType) {

                // タグ開始
                case XmlPullParser.START_TAG:

                    // ログ設定種別を取得する
                    configType = LogConfigType.toConfigType(parser.getName());

                    // ログ設定種別が見つからなかった場合は例外
                    if (configType == null) {

                        throw new IllegalStateException(
                                "no supported tag. [name = " + parser.getName() + "]"
                                );

                    }

                    // ログ設定種類別処理
                    switch (configType) {

                    // アペンダ
                    case APPENDER:

                        // アペンダを作成する
                        procAppender = new LogAppender(
                                parser.getAttributeValue(null, LogConfigType.ATTR_NAME)
                                );
                        break;


                    // ログ出力先
                    case TARGET:

                        // アペンダが作成されていない場合は例外
                        if (procAppender == null) {

                            throw new IllegalStateException(
                                    "It doesn't exist in appender. [name = " + parser.getName() + "]"
                                    );

                        }


                        // ログ出力先を取得する
                        final String    targetType =
                            parser.getAttributeValue(null, LogConfigType.ATTR_TYPE);

                        // ログ出力先が標準出力の場合
                        if (LogConfigType.TARGET_STDOUT.equalsIgnoreCase(targetType)) {

                            // 標準出力の場合は null を設定する
                            procAppender.setTarget(null);

                        // ログ出力先がファイルの場合
                        } else if (LogConfigType.TARGET_FILE.equalsIgnoreCase(targetType)) {

                            // ファイルパスを取得する
                            final String    filePath =
                                Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/"
                                + parser.getAttributeValue(
                                    null,
                                    LogConfigType.ATTR_VALUE
                                    );

                            // ファイルが存在しない場合
                            if (!FileUtils.exists(filePath)) {

                                // ファイルを作成する
                                if (!FileUtils.createNew(filePath)) {

                                    // 作成に失敗した場合は例外
                                    throw new IllegalStateException("Failed to create a new log file.");

                                }

                            }

                            // ファイル出力ストリームを作成して設定する
                            procAppender.setTarget(
                                    new BufferedWriter(
                                            new OutputStreamWriter(
                                                    new FileOutputStream(filePath, true)
                                                    )
                                            )
                                    );

                        // その他 (エラー)
                        } else {

                            throw new IllegalStateException(
                                    "Illegal target type. [type = " + targetType + "]"
                                    );

                        }
                        break;


                    // ログ出力パターン
                    case PATTERN:

                        // アペンダが作成されていない場合は例外
                        if (procAppender == null) {

                            throw new IllegalStateException(
                                    "It doesn't exist in appender. [name = " + parser.getName() + "]"
                                    );

                        }

                        // 出力先情報をアペンダへ格納する
                        procAppender.setPattern(
                                parser.getAttributeValue(null, LogConfigType.ATTR_VALUE)
                                );
                        break;


                    // カテゴリ
                    case CATEGORY:

                        // 処理中カテゴリデータを作成する
                        procCategory = new LogCategory(
                                parser.getAttributeValue(null, LogConfigType.ATTR_NAME)
                                );
                        break;


                    // ルートカテゴリ
                    case CATEGORY_ROOT:

                        // 処理中カテゴリデータを作成する
                        procCategory = new LogCategory(null);
                        break;


                    // ログレベル
                    case PRIORITY:

                        // カテゴリが作成されていない場合は例外
                        if (procCategory == null) {

                            throw new IllegalStateException(
                                    "It doesn't exist in category. [name = " + parser.getName() + "]"
                                    );

                        }

                        // ログレベルを設定する
                        procCategory.setLogLevel(
                                LogLevel.toLogLevel(
                                        parser.getAttributeValue(null, LogConfigType.ATTR_VALUE)
                                        )
                                );
                        break;


                    // 参照アペンダ
                    case APPENDER_REF:

                        // カテゴリが作成されていない場合は例外
                        if (procCategory == null) {

                            throw new IllegalStateException(
                                    "It doesn't exist in category. [name = " + parser.getName() + "]"
                                    );

                        }

                        // アペンダを取得する
                        final String        refName     = parser.getAttributeValue(null, LogConfigType.ATTR_REF);
                        final LogAppender   refAppender = readAppenders.get(refName);

                        // アペンダが取得できなかった場合
                        if (refAppender == null) {

                            throw new IllegalStateException(
                                    "The appender doesn't exist. [name = " + refName + "]"
                                    );

                        }

                        // 参照アペンダを追加する
                        procCategory.addAppender(refAppender);
                        break;


                    // その他
                    default:

                        // 処理なし
                        break;

                    }
                    break;


                // タグ終了
                case XmlPullParser.END_TAG:

                    // ログ設定種別を取得する
                    configType = LogConfigType.toConfigType(parser.getName());

                    // ログ設定種別が見つからなかった場合は例外
                    if (configType == null) {

                        throw new IllegalStateException("no supported tag. [name = " + parser.getName() + "]");

                    }


                    // ログ設定種類別処理
                    switch (configType) {

                    // アペンダ
                    case APPENDER:

                        // 処理したアペンダテーブルへ追加する
                        readAppenders.put(procAppender.getName(), procAppender);

                        // 処理中アペンダをクリアする
                        procAppender = null;
                        break;


                    // カテゴリ
                    case CATEGORY:

                        // 作成したログカテゴリをキャッシュへ追加する
                        LOG_CATEGORIES.add(procCategory);

                        // 処理中カテゴリをクリアする
                        procCategory = null;
                        break;


                    // ルートカテゴリ
                    case CATEGORY_ROOT:

                        // 作成したログカテゴリをキャッシュへ設定する
                        logCategoryRoot = procCategory;

                        // 処理中カテゴリをクリアする
                        procCategory = null;
                        break;


                    // その他
                    default:

                        // 処理なし
                        break;

                    }

                    // ログ設定種別をクリアする
                    configType = null;
                    break;


                // ドキュメント終端
                case XmlPullParser.END_DOCUMENT:

                    // 読み込み終了を設定する
                    isEnded = true;
                    break;


                // その他
                default:

                    // 処理なし
                    break;

                }

            }

            // ログカテゴリとルートカテゴリがない場合は例外
            if (isEmptyConfiguration()) {

                throw new IllegalStateException(
                        "No categories. The category should be more than one."
                        );

            }

        }

    }


    /**
     * 設定ファイル名のプリファレンスキーを取得する。
     *
     * @return 設定ファイル名のプリファレンスキー
     */
    private static String getConfigFileNamePrefKey() {

        return String.format(PREF_KEY_CONFIG_FILE_NAME_FORMAT, LogUtils.class.getName());

    }


    /**
     * ログ設定を初期化する。
     *
     * @param context   利用するコンテキスト
     * @param fileName  設定ファイル名 (assetsに配置した設定ファイル)
     * @return 設定反映に成功した場合は true
     */
    public static boolean setConfiguration(
            final Context   context,
            final String    fileName
            ) {

        InputStream         in     = null;  // 入力ストリーム
        XmlPullParser       parser = null;  // XML読み込みパーサー


        try {

            // アセッツから設定内容を読み込む
            in = context.getAssets().open(fileName);

            // XML読み込みパーサーを作成する
            final XmlPullParserFactory  factory = XmlPullParserFactory.newInstance();

            // 名前空間を有効にする
            factory.setNamespaceAware(true);

            // パーサーを取得する
            parser = factory.newPullParser();
            parser.setInput(in, ENCODING);

            // パース処理を行う
            parseConfiguration(parser);

        } catch (final Throwable e) {

            e.printStackTrace();

            // 失敗
            return false;

        } finally {

            // ファイルを取得できた場合
            if (in != null) {

                // ファイルを閉じる
                IOUtils.closeQuietly(in);

                // 設定ファイル名をプリファレンスへ書きこむ
                PreferencesUtils.writeObject(
                        context,
                        getConfigFileNamePrefKey(),
                        fileName
                        );

            }

        }


        // 成功
        return true;

    }


    /**
     * ログ設定情報が空かどうかを取得する。
     *
     * @return ログ設定情報が空の場合は true
     */
    private static boolean isEmptyConfiguration() {

        return LOG_CATEGORIES.isEmpty() && (logCategoryRoot == null);

    }


    /**
     * 設定情報を復帰させる。
     *
     * @param context 利用するコンテキスト情報
     */
    private static void restoreConfiguration(
            final Context   context
            ) {

        // 保存されている設定ファイル名を取得する
        final String    fileName = PreferencesUtils.readObject(
                context,
                getConfigFileNamePrefKey(),
                String.class
                );

        // 設定ファイル名が復帰出来なかった場合は例外
        if (fileName == null) {

            throw new IllegalStateException(
                    "No setting the configuration of 'LogUtils' or failed to restore the configuration."
                    );

        }

        // 設定ファイルを再設定する
        setConfiguration(context, fileName);

    }


    /**
     * {@link LogLevel#VERBOSE} レベルでログを出力する。
     *
     * @param context   利用するコンテキスト情報
     * @param tag       タグ
     * @param message   メッセージ
     */
    public static void v(
            final Context   context,
            final Object    tag,
            final Object    message
            ) {

        log(context, LogLevel.VERBOSE, tag, message, null);

    }


    /**
     * {@link LogLevel#VERBOSE} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     */
    public static void v(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception
            ) {

        log(context, LogLevel.VERBOSE, tag, message, exception);

    }


    /**
     * {@link LogLevel#VERBOSE} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     * @param formatArgs    フォーマット文字列出力する場合のパラメータ
     */
    public static void v(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception,
            final Object... formatArgs
            ) {

        log(context, LogLevel.VERBOSE, tag, message, exception, formatArgs);

    }


    /**
     * {@link LogLevel#DEBUG} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     */
    public static void d(
            final Context   context,
            final Object    tag,
            final Object    message
            ) {

        log(context, LogLevel.DEBUG, tag, message, null);

    }


    /**
     * {@link LogLevel#DEBUG} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     */
    public static void d(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception
            ) {

        log(context, LogLevel.DEBUG, tag, message, exception);

    }


    /**
     * {@link LogLevel#DEBUG} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     * @param formatArgs    フォーマット文字列出力する場合のパラメータ
     */
    public static void d(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception,
            final Object... formatArgs
            ) {

        log(context, LogLevel.DEBUG, tag, message, exception, formatArgs);

    }


    /**
     * {@link LogLevel#INFO} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     */
    public static void i(
            final Context   context,
            final Object    tag,
            final Object    message
            ) {

        log(context, LogLevel.INFO, tag, message, null);

    }


    /**
     * {@link LogLevel#INFO} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     */
    public static void i(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception
            ) {

        log(context, LogLevel.INFO, tag, message, exception);

    }


    /**
     * {@link LogLevel#INFO} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     * @param formatArgs    フォーマット文字列出力する場合のパラメータ
     */
    public static void i(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception,
            final Object... formatArgs
            ) {

        log(context, LogLevel.INFO, tag, message, exception, formatArgs);

    }


    /**
     * {@link LogLevel#WARN} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     */
    public static void w(
            final Context   context,
            final Object    tag,
            final Object    message
            ) {

        log(context, LogLevel.WARN, tag, message, null);

    }


    /**
     * {@link LogLevel#WARN} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     */
    public static void w(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception
            ) {

        log(context, LogLevel.WARN, tag, message, exception);

    }


    /**
     * {@link LogLevel#WARN} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     * @param formatArgs    フォーマット文字列出力する場合のパラメータ
     */
    public static void w(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception,
            final Object... formatArgs
            ) {

        log(context, LogLevel.WARN, tag, message, exception, formatArgs);

    }


    /**
     * {@link LogLevel#ERROR} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     */
    public static void e(
            final Context   context,
            final Object    tag,
            final Object    message
            ) {

        log(context, LogLevel.ERROR, tag, message, null);

    }


    /**
     * {@link LogLevel#ERROR} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     */
    public static void e(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception
            ) {

        log(context, LogLevel.ERROR, tag, message, exception);

    }


    /**
     * {@link LogLevel#ERROR} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     * @param formatArgs    フォーマット文字列出力する場合のパラメータ
     */
    public static void e(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception,
            final Object... formatArgs
            ) {

        log(context, LogLevel.ERROR, tag, message, exception, formatArgs);

    }


    /**
     * {@link LogLevel#ASSERT} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     */
    public static void a(
            final Context   context,
            final Object    tag,
            final Object    message
            ) {

        log(context, LogLevel.ASSERT, tag, message, null);

    }


    /**
     * {@link LogLevel#ASSERT} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     */
    public static void a(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception
            ) {

        log(context, LogLevel.ASSERT, tag, message, exception);

    }


    /**
     * {@link LogLevel#ASSERT} レベルでログを出力する。
     *
     * @param context       利用するコンテキスト情報
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     * @param formatArgs    フォーマット文字列出力する場合のパラメータ
     */
    public static void a(
            final Context   context,
            final Object    tag,
            final Object    message,
            final Throwable exception,
            final Object... formatArgs
            ) {

        log(context, LogLevel.ASSERT, tag, message, exception, formatArgs);

    }


    /**
     * ネスト化診断コンテキスト値をスタックへ追加する。
     *
     * @param value 追加するネスト化診断コンテキスト値
     */
    public static void pushNDC(
            final Object    value
            ) {

        // スタックリストを取得する
        final ArrayList<String>     ndcStack = LOG_NDC.get();

        // スタックへ追加する
        ndcStack.add(String.valueOf(value));

    }


    /**
     * ネスト化診断コンテキスト値をスタックから取り出す。
     *
     */
    public static void popNDC() {

        // スタックリストを取得する
        final ArrayList<String>     ndcStack = LOG_NDC.get();

        // リストが 0 より大きい場合
        if (ndcStack.size() > 0) {

            // リストの末尾データを削除
            ndcStack.remove(ndcStack.size() - 1);

        }

        // リストが空の場合
        if (ndcStack.isEmpty()) {

            // 現在のスレッドからネスト化診断コンテキスト情報を削除する
            removeNDC();

        } else {

            // リストの要素数を最適化する
            ndcStack.trimToSize();

        }

    }


    /**
     * ネスト化診断コンテキスト値を削除する。<br>
     * <br>
     * カレントスレッドに対応したネスト化診断コンテキスト情報を削除する。
     */
    public static void removeNDC() {

        // 現在のスレッドからネスト化診断コンテキスト情報を削除する
        LOG_NDC.remove();

    }


    /**
     * マップ化診断コンテキスト値を設定する。
     *
     * @param mapName   マッピング名
     * @param mapValue  マッピング値
     */
    public static void putMDC(
            final String    mapName,
            final Object    mapValue
            ) {

        // マッピング名が null の場合は例外
        if (mapName == null) {

            throw new IllegalArgumentException();

        }


        // マッピングテーブルを取得する
        final Map<String, String>      mdcTable = LOG_MDC.get();

        // マッピングテーブルへ追加する
        mdcTable.put(mapName, String.valueOf(mapValue));

    }


    /**
     * マップ化診断コンテキスト値を削除する。
     *
     * @param mapName   削除するマッピング名
     */
    public static void removeMDC(
            final String    mapName
            ) {

        // マッピング名が null の場合は例外
        if (mapName == null) {

            throw new IllegalArgumentException();

        }


        // マッピングテーブルを取得する
        final Map<String, String>      mdcTable = LOG_MDC.get();

        // マッピングテーブルから削除する
        mdcTable.remove(mapName);

        // テーブルが空の場合
        if (mdcTable.isEmpty()) {

            // 現在のスレッドからマップ化診断コンテキスト情報を削除する
            LOG_MDC.remove();

        }

    }


    /**
     * カレントスレッドのネスト化診断コンテキスト値からログ出力文字列を作成する。
     *
     * @return ネスト化診断コンテキスト値のログ出力文字列
     */
    private static String getNDCString() {

        final StringBuilder     strBuf   = new StringBuilder();
        final ArrayList<String> ndcStack = LOG_NDC.get();

        // ネスト化診断コンテキスト情報がある場合
        if (!ndcStack.isEmpty()) {

            // スタックにあるネスト化診断コンテキスト情報分繰り返す
            for (final Iterator<String> i = ndcStack.iterator();;) {

                // スタックの値を追加する
                strBuf.append(i.next());

                // 次の要素がある場合
                if (i.hasNext()) {

                    // 区切り文字を追加する
                    strBuf.append(TOKEN_NDC_SPLIT);

                } else {

                    // ループ終了
                    break;

                }

            }

        }

        // 作成したログ出力文字列を返す
        return strBuf.toString();

    }


    /**
     * カレントスレッドの指定キーのマップ化診断コンテキスト値を取得する。
     *
     * @param key   マップ化診断コンテキスト値のキー
     * @return 対応するマップ化診断コンテキスト値
     */
    private static String getMDCString(
            final String    key
            ) {

        final Map<String, String>   mdcTable = LOG_MDC.get();
        final String                retValue = mdcTable.get(key);

        // 対応するマップ化診断コンテキスト値を返す
        return retValue == null ? "" : retValue;

    }


    /**
     * フォーマット変換文字位置に指定された値を追加する。
     *
     * @param strBuf            追加先文字列バッファ
     * @param conversionsChar   フォーマット変換文字
     * @param value             追加する値
     * @param width             指定文字幅
     * @param blankChar         幅より小さい場合、追加するブランク文字
     * @param alignLeft         左寄せするかどうか
     */
    private static void appendFormatValue(
            final StringBuilder strBuf,
            final char          conversionsChar,
            final String        value,
            final char          blankChar,
            final int           width,
            final boolean       alignLeft
            ) {

        final StringBuilder tempValueBuf = new StringBuilder(); // 作業用値バッファ
        int                 diffWidth;                          // 値幅の差分


        // 作業用バッファへ値を設定する
        tempValueBuf.append(value);

        // 実際の値幅と指定幅の差分を指定する
        diffWidth = width - tempValueBuf.length();


        // 左寄せの場合
        if (alignLeft) {

            // 値を付加する
            strBuf.append(tempValueBuf);

            // 差分を指定文字で埋める
            for (; diffWidth > 0; diffWidth--) {

                strBuf.append(blankChar);

            }

        // 右寄せの場合
        } else {

            // 差分を指定文字で埋める
            for (; diffWidth > 0; diffWidth--) {

                strBuf.append(blankChar);

            }

            // 値を付加する
            strBuf.append(tempValueBuf);

        }

    }


    /**
     * 指定されたアペンダのパターンからログメッセージを取得する。
     *
     * @param level     出力ログレベル
     * @param category  利用するカテゴリ
     * @param appender  利用するアペンダ
     * @param tag       出力するタグ名
     * @param message   出力するメッセージ
     * @return 指定されたアペンダのパターンからログメッセージ
     */
    private static String getPatternMessage(
            final LogLevel      level,
            final LogCategory   category,
            final LogAppender   appender,
            final String        tag,
            final String        message
            ) {

        final char              conversionToken     = '%';                      // パターン文字トークン
        final char              argumentLeftToken   = '{';                      // 引数囲い左トークン
        final char              argumentRightToken  = '}';                      // 引数囲い右トークン
        final StringBuilder     retBuf              = new StringBuilder();      // 返却文字列バッファ
        final String            pattern             = appender.getPattern();    // パターン文字列
        int                     nowPercentIndex     = -1;                       // パーセントの位置
        int                     nowSearchIndex      = 0;                        // 現在の検索位置


        // パターン文字列を検索する
        while (true) {

            // パーセント位置を検索する
            nowPercentIndex = appender.getPattern().indexOf(
                    conversionToken,
                    nowSearchIndex
                    );

            // パーセントが見つからない場合
            if (nowPercentIndex == -1) {

                // フォーマット変換文字から最後までを追加する
                retBuf.append(pattern.substring(
                        nowSearchIndex,
                        pattern.length())
                        );

                // ループ終了
                break;

            }


            // %位置が先頭より先にある場合
            if (nowPercentIndex > 0) {

                // % 位置までの文字列を設定する
                retBuf.append(pattern.substring(
                        nowSearchIndex,
                        nowPercentIndex)
                        );

            }


            final int   argumentLeftTokenIndex;             // 引数指定左囲いトークン位置
            final int   argumentRightTokenIndex;            // 引数指定右囲いトークン位置
            String      argumentValue           = null;     // 引数文字列
            int         conversionCharIndex     = -1;       // 作業用フォーマット変換文字位置
            int         flagsCharIndex          = -1;       // フラグ文字の位置
            int         stringWidth             = -1;       // 幅指定がある場合、その数値
            int         workIndex;                          // 作業用現在位置


            // 作業位置を設定する
            workIndex = nowPercentIndex + 1;

            // フォーマット変換文字の位置を取得する
            for (int i = workIndex; i < pattern.length(); i++) {

                // フォーマット変換文字があった場合
                if (SEARCH_PATTERN_CONVERSIONS.contains(pattern.charAt(i))) {

                    // フォーマット変換文字の位置を取得する
                    conversionCharIndex = i;

                    // ループ終了
                    break;

                }

            }

            // フォーマット変換文字がない場合
            if (conversionCharIndex == -1) {

                // フォーマット変換文字から最後までを追加する
                retBuf.append(pattern.substring(
                        nowSearchIndex,
                        pattern.length())
                        );

                // ループ終了
                break;

            }

            // フラグ指定があるか検索する
            for (int i = workIndex; i < conversionCharIndex; i++) {

                final char  tempChar = pattern.charAt(i);


                // フラグ文字があった場合
                if (SEARCH_FLAGS.contains(tempChar)) {

                    // 0 のフラグ文字の場合で、前の文字が数字の場合
                    if ((tempChar == '0') && Character.isDigit(pattern.charAt(i - 1))) {

                        // フラグ文字ではなくて幅指定数値なので次の文字へ
                        continue;

                    }

                    // 既に取得済みの場合は例外
                    if (flagsCharIndex != -1) {

                        throw new IllegalArgumentException("There are multiply flags.");

                    }

                    // フラグ指定文字位置を取得する
                    flagsCharIndex = i;

                    // 作業位置を更新する
                    workIndex = flagsCharIndex + 1;

                }

            }

            // 引数指定囲い左トークンがフォーマット文字の次に位置にあるかどうかを検索する
            if ((conversionCharIndex + 1 < pattern.length())
                && (pattern.charAt(conversionCharIndex + 1) == argumentLeftToken)
                ) {

                // 左トークン位置を設定する
                argumentLeftTokenIndex = conversionCharIndex + 1;

                // 引数指定囲い右トークンを検索する
                argumentRightTokenIndex = pattern.indexOf(argumentRightToken, argumentLeftTokenIndex + 1);

                // 引数指定囲い右トークンがある場合
                if (argumentLeftTokenIndex < argumentRightTokenIndex) {

                    // 引数の内容を取得する
                    argumentValue = pattern.substring(
                            argumentLeftTokenIndex + 1,
                            argumentRightTokenIndex
                            );

                    // 現在検索位置を設定する
                    nowSearchIndex = argumentRightTokenIndex + 1;

                } else {

                    // 現在検索位置を設定する
                    nowSearchIndex = argumentLeftTokenIndex + 1;

                }

            } else {

                // 現在検索位置を設定する
                nowSearchIndex = conversionCharIndex + 1;

            }



            // 幅指定がある場合
            if (workIndex != conversionCharIndex) {

                // 幅指定が正しいかどうか検索する
                for (int i = workIndex; i < conversionCharIndex; i++) {

                    final char  tempChar = pattern.charAt(i);

                    // 数字以外の文字だった場合は例外
                    if (!Character.isDigit(tempChar)) {

                        throw new IllegalArgumentException("No supported a precision value.");

                    }

                }


                // 幅数値を取得する
                stringWidth = Integer.parseInt(pattern.substring(workIndex, conversionCharIndex));

            }


            // フォーマット文字を取得する
            final char  formatConversionChar = pattern.charAt(conversionCharIndex);

            // 各種パラメータを定義する
            final char      appendChar      = ' ';      // 幅より小さい場合に埋める文字
            final String    valueStr;                   // 値の文字列表記
            final String    formatStr;                  // フォーマット文字列
            char            flagsChar       = 0;        // フラグ文字
            boolean         alignLeft       = false;    // 左寄せするかどうか


            // フォーマット文字別処理
            switch (formatConversionChar) {

            // カテゴリ名
            case 'c':

                // カテゴリ名を設定する
                valueStr = category.getName();
                break;


            // 日付
            case 'd':

                // 引数指定がある場合
                if (argumentValue != null) {

                    // 半角スペースをシングルクォーテーションで囲むように変換して設定する
                    formatStr = argumentValue.replace(" ", "' '");

                } else {

                    // デフォルトフォーマットを設定する
                    formatStr = DEFAULT_DATE_FORMAT;

                }


                // 指定パターンの文字列へ変換する
                valueStr = DateUtils.format(
                        formatStr,
                        System.currentTimeMillis()
                        );

                // 変換に失敗した場合
                if (valueStr == null) {

                    throw new IllegalStateException(
                            "Illegal date pattern format. [pattern = " + argumentValue + "]"
                            );

                }
                break;


            // タグ名
            case 'g':

                // タグ名を設定する
                valueStr = tag;
                break;


            // 改行コード
            case 'n':

                // 改行コードを設定する
                valueStr = LINE_FEED;
                break;


            // ログメッセージ
            case 'm':

                // ログメッセージを設定する
                valueStr = message;
                break;


            // ログレベル名
            case 'p':

                // ログレベル名を設定する
                valueStr = level.getName();
                break;


            // カレントスレッド名
            case 't':

                // カレントスレッド名を追加する
                valueStr = Thread.currentThread().getName();
                break;


            // NDCでPUSHした文字列
            case 'x':

                // NDCログ文字列を取得する
                valueStr = getNDCString();
                break;


            // MDCで保存された key の値
            case 'X':

                // MDCのキーが指定されていない場合
                if (argumentValue == null) {

                    throw new IllegalStateException("The key value of MDC is not specified.");

                }

                // MDCログ文字列を取得する
                valueStr = getMDCString(argumentValue);
                break;


            // その他 (エラー)
            default:

                throw new IllegalStateException(
                        "No supported conversion char. [char = " + formatConversionChar + "]"
                        );

            }


            // フラグ文字を取得する
            if (flagsCharIndex != -1) {

                flagsChar = pattern.charAt(flagsCharIndex);

            }

            // フラグ文字別処理
            switch (flagsChar) {

            // 左寄せ
            case '-':

                // 左寄せを有効にする
                alignLeft = true;
                break;


            // 不正フラグ
            case '0':

                throw new IllegalArgumentException(
                        "変換文字に適合しないフラグが指定されています [Conversion = "
                        + formatConversionChar
                        + ", Flags = 0]"
                        );

            // その他
            default:

                break;

            }

            // 値を追加する
            appendFormatValue(
                    retBuf,
                    formatConversionChar,
                    valueStr,
                    appendChar,
                    stringWidth,
                    alignLeft
                    );

        }


        // 作成した文字列を返却する
        return retBuf.toString();

    }


    /**
     * 指定ログレベル、タグ、メッセージ、例外をログに出力する。
     *
     * @param context       利用するコンテキスト
     * @param level         ログレベル
     * @param tag           タグ
     * @param message       メッセージ
     * @param exception     例外
     * @param formatArgs    フォーマット文字列出力する場合のパラメータ
     * @throws IllegalArgumentException ログレベルが null の場合
     */
    public static void log(
            final Context   context,
            final LogLevel  level,
            final Object    tag,
            final Object    message,
            final Throwable exception,
            final Object... formatArgs
            ) {

        // 利用するコンテキスト情報またはログレベルが指定されていない場合は例外
        if ((context == null) || (level == null)) {

            throw new IllegalArgumentException();

        }

        // タグとメッセージを文字列へ変換する
        final String    strTag = String.valueOf(tag);

        // カテゴリ情報が空の場合
        if (isEmptyConfiguration()) {

            // 設定情報を復帰させる
            restoreConfiguration(context);

        }


        final String    strMessage  = String.valueOf(message);  // 文字列メッセージ
        final String    localMessage;                           // ログメッセージ

        // 例外がない場合
        if (exception == null) {

            // フォーマット文字列の場合
            if ((formatArgs != null) && (formatArgs.length > 0)) {

                // フォーマット文字列として設定する
                localMessage = String.format(strMessage, formatArgs);

            } else {

                // メッセージをそのまま設定する
                localMessage = strMessage;

            }

        } else {

            // フォーマット文字列の場合
            if ((formatArgs != null) && (formatArgs.length > 0)) {

                // メッセージにスタックトレースを付加する
                localMessage = String.format(
                        strMessage + LINE_FEED + Log.getStackTraceString(exception),
                        formatArgs
                        );

            } else {

                // メッセージにスタックトレースを付加する
                localMessage = strMessage + LINE_FEED + Log.getStackTraceString(exception);

            }

        }


        LogCategory   category = null;   // 利用するログカテゴリ

        // カテゴリ情報でロックする
        synchronized (LOG_CATEGORY_LOCK) {

            // 一致するカテゴリを検索する
            for (final Iterator<LogCategory> i = LOG_CATEGORIES.iterator(); i.hasNext();) {

                // カテゴリを取得する
                final LogCategory   localCategory = i.next();

                // カテゴリ名が前方一致する場合
                if (strTag.startsWith(localCategory.getName())) {

                    // 使用するログカテゴリを取得する
                    category = localCategory;

                    // ループ中断
                    break;

                }

            }

            // ログカテゴリが取得できなかった場合
            if (category == null) {

                // ルートカテゴリを利用する
                category = logCategoryRoot;

                // ルートカテゴリがない場合
                if (category == null) {

                    throw new IllegalStateException(
                            "No root category. Please append a 'category-root' element."
                            );

                }

            }

            // 指定ログレベルが無効の場合
            if ((category.getLogLevel() != null)
                && (level.getType() < category.getLogLevel().getType())
                ) {

                // 何もしない
                return;

            }


            try {

                // 参照アペンダ分処理をする
                for (final Iterator<LogAppender> i = category.iteratorAppenders(); i.hasNext();) {

                    final LogAppender       appender = i.next();    // 利用するアペンダ
                    final BufferedWriter    target;                 // 出力先ストリーム
                    final String            outMessage;             // 出力するログメッセージ


                    // 出力先ストリームを取得する
                    target = appender.getTarget();

                    // 出力するログメッセージを作成する
                    outMessage = getPatternMessage(
                            level,
                            category,
                            appender,
                            strTag,
                            localMessage
                            );

                    // 出力先がない場合
                    if (target == null) {

                        // ログを出力する
                        Log.println(
                                level.getType(),
                                strTag,
                                outMessage
                                );

                    } else {

                        // 指定されている出力先へログを出力する
                        target.write(
                                outMessage + LINE_FEED
                                );
                        target.flush();

                    }

                }

            } catch (final IOException e) {

                e.printStackTrace();

            }

        }


    }




    /**
     * ログ設定種別。
     *
     * @author Kou
     *
     */
    private static enum LogConfigType {


        /**
         * 設定ルート
         */
        CONFIGRATION("configuration"),

        /**
         * アペンダ
         */
        APPENDER("appender"),

        /**
         * ログ出力先
         */
        TARGET("target"),

        /**
         * 出力パターン
         */
        PATTERN("pattern"),

        /**
         * カテゴリ
         */
        CATEGORY("category"),

        /**
         * ルートカテゴリ
         */
        CATEGORY_ROOT("category-root"),

        /**
         * ログレベル
         */
        PRIORITY("priority"),

        /**
         * 参照アペンダ
         */
        APPENDER_REF("appender-ref");


        /**
         * 種別参照テーブルを初期化する
         */
        static {

            // ログ種別テーブルを作成する
            final Map<String, LogConfigType>    typeTable = new HashMap<String, LogUtils.LogConfigType>();

            // 種別名をキーにして種別をテーブルへ追加する
            typeTable.put(CONFIGRATION.getName(),   CONFIGRATION);
            typeTable.put(APPENDER.getName(),       APPENDER);
            typeTable.put(TARGET.getName(),         TARGET);
            typeTable.put(PATTERN.getName(),        PATTERN);
            typeTable.put(CATEGORY.getName(),       CATEGORY);
            typeTable.put(CATEGORY_ROOT.getName(),  CATEGORY_ROOT);
            typeTable.put(PRIORITY.getName(),       PRIORITY);
            typeTable.put(APPENDER_REF.getName(),   APPENDER_REF);

            // 作成したログ種別テーブルを設定する
            LOG_CONFIG_TYPES = typeTable;

        }


        /**
         * 属性名 : 種類
         */
        public static final String          ATTR_TYPE   = "type";

        /**
         * 属性名 : 名前
         */
        public static final String          ATTR_NAME   = "name";

        /**
         * 属性名 : 値
         */
        public static final String          ATTR_VALUE  = "value";

        /**
         * 属性名 : 参照
         */
        public static final String          ATTR_REF    = "ref";


        /**
         * ログ出力先 : 標準出力
         */
        public static final String          TARGET_STDOUT   = "stdout";

        /**
         * ログ出力先 : SDファイル
         */
        public static final String          TARGET_FILE     = "file";



        /**
         * ログ設定種別参照テーブル<br>
         * <br>
         * <table border="1">
         * <tr>
         *   <td>項目</td><td>型</td><td>内容</td>
         * </tr>
         * <tr>
         *   <td>キー</td><td>String</td><td>ログ設定種別名</td>
         * </tr>
         * <tr>
         *   <td>値</td><td>LogConfigType</td><td>ログ設定種別</td>
         * </tr>
         * </table>
         */
        private static final Map<String, LogConfigType>     LOG_CONFIG_TYPES;

        /**
         * ログ設定種別名 (タグ名)
         */
        private final String        tagName;



        /**
         * ログ設定種別を初期化する。
         *
         * @param name  種別タグ名
         */
        private LogConfigType(
                final String    name
                ) {

            tagName = name;

        }


        /**
         * 種別名を取得する。
         *
         * @return 種別名
         */
        public String getName() {

            return tagName;

        }


        /**
         * 指定された種別名をログ設定種別へ変換する。
         *
         * @param name 変換する種別名
         * @return 変換したログ設定種別
         */
        public static LogConfigType toConfigType(
                final String    name
                ) {

            // 種別名が null の場合
            if (name == null) {

                // nullを返す
                return null;

            }

            // 指定した名前に対応するログ設定種別を返す
            return LOG_CONFIG_TYPES.get(name.toLowerCase());

        }


    }


    /**
     * ログアペンダ情報クラス。
     *
     * @author Kou
     *
     */
    private static final class LogAppender {


        /**
         * アペンダ名
         */
        private final String                appenderName;

        /**
         * ログ出力先
         */
        private BufferedWriter              appenderTarget;

        /**
         * 出力パターン
         */
        private String                      appenderPattern;



        /**
         * ログアペンダを初期化する。
         *
         * @param name ログアペンダ名
         */
        public LogAppender(
                final String        name
                ) {

            appenderName = name;

        }


        /**
         * ログアペンダ名を取得する。
         *
         * @return ログアペンダ名
         */
        public String getName() {

            return appenderName;

        }


        /**
         * ログ出力先を設定する。
         *
         * @param target    ログ出力先ライター
         */
        public void setTarget(
                final BufferedWriter    target
                ) {

            appenderTarget = target;

        }


        /**
         * ログ出力先を取得する。
         *
         * @return ログ出力先
         */
        public BufferedWriter getTarget() {

            return appenderTarget;

        }


        /**
         * ログ出力パターンを設定する。
         *
         * @param pattern ログ出力パターン
         */
        public void setPattern(
                final String    pattern
                ) {

            appenderPattern = pattern;

        }


        /**
         * ログ出力パターンを取得する。
         *
         * @return ログ出力パターン
         */
        public String getPattern() {

            return appenderPattern;

        }


    }


    /**
     * ログカテゴリ情報クラス。
     *
     * @author Kou
     *
     */
    private static final class LogCategory {


        /**
         * 参照アペンダリスト
         */
        private final List<LogAppender>     categoryAppenders = new CopyOnWriteArrayList<LogAppender>();

        /**
         * カテゴリ名
         */
        private final String                categoryName;

        /**
         * 出力ログレベル
         */
        private LogLevel                    categoryLevel;




        /**
         * ログカテゴリを初期化する。
         *
         * @param name  ログカテゴリ名
         */
        public LogCategory(
                final String    name
                ) {

            categoryName = name;

        }


        /**
         * ログカテゴリ名を取得する。
         *
         * @return ログカテゴリ名
         */
        public String getName() {

            return categoryName;

        }


        /**
         * ログレベルを設定する。
         *
         * @param level ログレベル
         */
        public void setLogLevel(
                final LogLevel  level
                ) {

            categoryLevel = level;

        }


        /**
         * ログレベルを取得する。
         *
         * @return ログレベル
         */
        public LogLevel getLogLevel() {

            return categoryLevel;

        }


        /**
         * 参照アペンダを追加する。
         *
         * @param appender 追加する参照アペンダ
         */
        public void addAppender(
                final LogAppender   appender
                ) {

            categoryAppenders.add(appender);

        }


        /**
         * 参照アペンダ一覧のイテレータを取得する。
         *
         * @return 参照アペンダ一覧のイテレータ
         */
        public Iterator<LogAppender> iteratorAppenders() {

            return categoryAppenders.iterator();

        }


    }


}
