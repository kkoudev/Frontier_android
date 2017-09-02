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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;



/**
 * ダイアログユーティリティクラス。
 *
 * @author nagaoka-koichi
 *
 */
public final class DialogUtils {


    /**
     * ダイアログ種別 : OKダイアログ
     */
    public static final int         DIALOG_OK           = 0;

    /**
     * ダイアログ種別 : YES, NOダイアログ
     */
    public static final int         DIALOG_YES_NO       = DIALOG_OK + 1;

    /**
     * ダイアログ種別 : OK, CANCELダイアログ
     */
    public static final int         DIALOG_OK_CANCEL    = DIALOG_YES_NO + 1;

    /**
     * ダイアログ種別数
     */
    public static final int         DIALOG_TYPE_COUNT   = DIALOG_OK_CANCEL + 1;


    /**
     * ボタン種別 : OK
     */
    public static final int         BUTTON_OK           = 0;

    /**
     * ボタン種別 : YES
     */
    public static final int         BUTTON_YES          = BUTTON_OK + 1;

    /**
     * ボタン種別 : NO
     */
    public static final int         BUTTON_NO           = BUTTON_YES + 1;

    /**
     * ボタン種別 : CANCEL
     */
    public static final int         BUTTON_CANCEL       = BUTTON_NO + 1;

    /**
     * ボタン種別数
     */
    public static final int         BUTTON_COUNT        = BUTTON_CANCEL + 1;



    /**
     * デフォルトボタン文言 : OK
     */
    private static final String     DEFAULT_WORD_OK     = "OK";

    /**
     * デフォルトボタン文言 : YES
     */
    private static final String     DEFAULT_WORD_YES    = "YES";

    /**
     * デフォルトボタン文言 : NO
     */
    private static final String     DEFAULT_WORD_NO     = "NO";

    /**
     * デフォルトボタン文言 : CANCEL
     */
    private static final String     DEFAULT_WORD_CANCEL = "CANCEL";

    /**
     * ボタン文言
     */
    private static final String[]   BUTTON_TEXTS    = new String[BUTTON_COUNT];




    /**
     * 各種パラメータを初期化する
     */
    static {

        // デフォルト文言を設定する
        // TODO : 今後は外部ファイルから読み込めるようにする
        BUTTON_TEXTS[BUTTON_OK]     = DEFAULT_WORD_OK;
        BUTTON_TEXTS[BUTTON_YES]    = DEFAULT_WORD_YES;
        BUTTON_TEXTS[BUTTON_NO]     = DEFAULT_WORD_NO;
        BUTTON_TEXTS[BUTTON_CANCEL] = DEFAULT_WORD_CANCEL;

    }


    /**
     * インスタンス生成防止。
     *
     */
    private DialogUtils() {

        // 処理なし

    }


    /**
     * 指定されたビルダーでアラートダイアログを表示する。
     *
     * @param activity  表示元アクティビティ
     * @param builder   アラートダイアログビルダー
     * @param view      ダイアログビュー
     */
    private static void showDialog(
            final Activity              activity,
            final AlertDialog.Builder   builder,
            final DialogView            view
            ) {

        // UIスレッドで実行するように設定する
        activity.runOnUiThread(new Runnable() {

            public void run() {

                // ダイアログを作成する
                final AlertDialog    dialog = builder.create();

                // ダイアログビューがある場合
                if (view != null) {

                    // マージンがある場合
                    if (view.getMargin() != null) {

                        // マージン付きでビューを設定する
                        dialog.setView(
                                view.getView(),
                                view.getMargin().left,
                                view.getMargin().top,
                                view.getMargin().right,
                                view.getMargin().bottom
                                );

                    } else {

                        // デフォルトマージンでビューを設定する
                        dialog.setView(view.getView());

                    }

                }

                // ダイアログを表示する
                dialog.show();

            }

        });

    }


    /**
     * 指定された種別のボタンに文言を設定する。
     *
     * @param buttonType    ボタン種別
     * @param text          ボタンに設定するテキスト
     */
    public static void setDialogButtonText(
            final int       buttonType,
            final String    text
            ) {

        // 種別が不正の場合は例外
        if ((buttonType < 0) || (BUTTON_COUNT <= buttonType)) {

            throw new IllegalArgumentException();

        }

        // 指定種別のボタンにテキストを設定する
        BUTTON_TEXTS[buttonType] = text;

    }


    /**
     * ダイアログを表示する。
     *
     * @param activity          表示元アクティビティ
     * @param type              ダイアログ種別
     * @param title             タイトル
     * @param message           メッセージ
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showDialog(
            final Activity          activity,
            final int               type,
            final String            title,
            final String            message
            ) {

        showDialog(activity, type, null, null, title, message, null, null, null);

    }


    /**
     * ダイアログを表示する。
     *
     * @param activity          表示元アクティビティ
     * @param icon              アイコン
     * @param type              ダイアログ種別
     * @param title             タイトル
     * @param message           メッセージ
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showDialog(
            final Activity          activity,
            final int               type,
            final Drawable          icon,
            final String            title,
            final String            message
            ) {

        showDialog(activity, type, icon, null, title, message, null, null, null);

    }


    /**
     * ダイアログを表示する。
     *
     * @param activity          表示元アクティビティ
     * @param type              ダイアログ種別
     * @param title             タイトル
     * @param message           メッセージ
     * @param positiveListener  ダイアログのOK, YESを押下したときに実行されるリスナー
     * @param negativeListener  ダイアログのNOを押下したときに実行されるリスナー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showDialog(
            final Activity          activity,
            final int               type,
            final String            title,
            final String            message,
            final OnClickListener   positiveListener,
            final OnClickListener   negativeListener
            ) {

        showDialog(activity, type, null, null, title, message, positiveListener, negativeListener, null);

    }


    /**
     * ダイアログを表示する。
     *
     * @param activity          表示元アクティビティ
     * @param type              ダイアログ種別
     * @param icon              アイコン
     * @param view              ダイアログビュー
     * @param title             タイトル
     * @param message           メッセージ
     * @param positiveListener  ダイアログのOK, YESを押下したときに実行されるリスナー
     * @param negativeListener  ダイアログのNOを押下したときに実行されるリスナー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showDialog(
            final Activity          activity,
            final int               type,
            final Drawable          icon,
            final DialogView        view,
            final String            title,
            final String            message,
            final OnClickListener   positiveListener,
            final OnClickListener   negativeListener
            ) {

        showDialog(activity, type, icon, view, title, message, positiveListener, negativeListener, null);

    }


    /**
     * ダイアログを表示する。
     *
     * @param activity          表示元アクティビティ
     * @param type              ダイアログ種別
     * @param title             タイトル
     * @param message           メッセージ
     * @param positiveListener  ダイアログのOK, YESを押下したときに実行されるリスナー
     * @param negativeListener  ダイアログのNOを押下したときに実行されるリスナー
     * @param backListener      ダイアログでBACKボタンを押下したときに実行されるリスナー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showDialog(
            final Activity          activity,
            final int               type,
            final String            title,
            final String            message,
            final OnClickListener   positiveListener,
            final OnClickListener   negativeListener,
            final OnCancelListener  backListener
            ) {

        showDialog(activity, type, null, null, title, message, positiveListener, negativeListener, null);

    }


    /**
     * ダイアログを表示する。
     *
     * @param activity          表示元アクティビティ
     * @param type              ダイアログ種別
     * @param icon              アイコン
     * @param view              ダイアログビュー
     * @param title             タイトル
     * @param message           メッセージ
     * @param positiveListener  ダイアログのOK, YESを押下したときに実行されるリスナー
     * @param negativeListener  ダイアログのNOを押下したときに実行されるリスナー
     * @param backListener      ダイアログでBACKボタンを押下したときに実行されるリスナー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showDialog(
            final Activity          activity,
            final int               type,
            final Drawable          icon,
            final DialogView        view,
            final String            title,
            final String            message,
            final OnClickListener   positiveListener,
            final OnClickListener   negativeListener,
            final OnCancelListener  backListener
            ) {

        // ダイアログを表示する
        showDialog(
                activity,
                createDialogBuilder(
                        activity,
                        type,
                        icon,
                        title,
                        message,
                        positiveListener,
                        negativeListener,
                        backListener
                        ),
                view
                );

    }


    /**
     * アラートダイアログビルダーを作成する。
     *
     * @param activity          表示元アクティビティ
     * @param type              ダイアログ種別
     * @param icon              アイコン
     * @param title             タイトル
     * @param message           メッセージ
     * @param positiveListener  ダイアログのOK, YESを押下したときに実行されるリスナー
     * @param negativeListener  ダイアログのNOを押下したときに実行されるリスナー
     * @param backListener      ダイアログでBACKボタンを押下したときに実行されるリスナー
     * @return 作成したアラートダイアログビルダー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static AlertDialog.Builder createDialogBuilder(
            final Activity          activity,
            final int               type,
            final Drawable          icon,
            final String            title,
            final String            message,
            final OnClickListener   positiveListener,
            final OnClickListener   negativeListener,
            final OnCancelListener  backListener
            ) {


        // アクティビティが null の場合
        if (activity == null) {

            throw new IllegalArgumentException();

        }

        // アラートダイアログビルダーを作成する
        final AlertDialog.Builder   builder = new AlertDialog.Builder(activity);

        // 各種パラメータを設定する
        builder.setIcon(icon);              // アイコン
        builder.setTitle(title);            // タイトル
        builder.setMessage(message);        // メッセージ

        // キャンセルリスナーがある場合
        if (backListener != null) {

            // キャンセルを有効にする
            builder.setCancelable(true);

            // キャンセルリスナーを設定する
            builder.setOnCancelListener(backListener);

        } else {

            // キャンセルを無効にする
            builder.setCancelable(false);

        }


        // ダイアログ種類別処理
        switch (type) {

        // OKダイアログ
        case DIALOG_OK:

            // OKボタンを作成する
            builder.setPositiveButton(BUTTON_TEXTS[BUTTON_OK], positiveListener);
            break;


        // YES, NOダイアログ
        case DIALOG_YES_NO:

            // YES, NOボタンを作成して設定する
            builder.setPositiveButton(BUTTON_TEXTS[BUTTON_YES], positiveListener);
            builder.setNegativeButton(BUTTON_TEXTS[BUTTON_NO], negativeListener);
            break;


        // OK, CANCELダイアログ
        case DIALOG_OK_CANCEL:

            // OK, CANCELボタンを作成して設定する
            builder.setPositiveButton(BUTTON_TEXTS[BUTTON_OK], positiveListener);
            builder.setNegativeButton(BUTTON_TEXTS[BUTTON_CANCEL], negativeListener);
            break;


        // 不正な種別
        default:

            throw new IllegalArgumentException("illegal type [type = " + type + "]");

        }

        // 作成したアラートダイアログビルダーを返す
        return builder;

    }


    /**
     * 項目選択ダイアログを表示する。
     *
     * @param activity      表示元アクティビティ
     * @param title         タイトル
     * @param items         表示する項目一覧
     * @param listener      項目選択時に実行されるリスナー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showItemDialog(
            final Activity          activity,
            final String            title,
            final String[]          items,
            final OnClickListener   listener
            ) {

        showItemDialog(activity, null, null, title, items, listener);

    }


    /**
     * 項目選択ダイアログを表示する。
     *
     * @param activity      表示元アクティビティ
     * @param icon          アイコン
     * @param view          ダイアログビュー
     * @param title         タイトル
     * @param items         表示する項目一覧
     * @param listener      項目選択時に実行されるリスナー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showItemDialog(
            final Activity          activity,
            final Drawable          icon,
            final DialogView        view,
            final String            title,
            final String[]          items,
            final OnClickListener   listener
            ) {

        // アクティビティが null の場合
        if (activity == null) {

            throw new IllegalArgumentException();

        }

        // アラートダイアログビルダーを作成する
        final AlertDialog.Builder   builder = new AlertDialog.Builder(activity);

        // タイトルと項目データを設定する
        builder.setIcon(icon);
        builder.setTitle(title == null ? "" : title);
        builder.setItems(items,  listener);

        // ダイアログを表示する
        showDialog(activity, builder, view);

    }


    /**
     * 排他項目選択ダイアログを表示する。
     *
     * @param activity      表示元アクティビティ
     * @param title         タイトル
     * @param items         表示する項目一覧
     * @param checkedItem   表示開始時に選択している項目のインデックス
     * @param listener      項目選択時に実行されるリスナー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showSingleChoiceItemDialog(
            final Activity          activity,
            final String            title,
            final String[]          items,
            final int               checkedItem,
            final OnClickListener   listener
            ) {

        showSingleChoiceItemDialog(activity, null, null, title, items, checkedItem, listener);

    }


    /**
     * 排他項目選択ダイアログを表示する。
     *
     * @param activity      表示元アクティビティ
     * @param icon          アイコン
     * @param view          ダイアログビュー
     * @param title         タイトル
     * @param items         表示する項目一覧
     * @param checkedItem   表示開始時に選択している項目のインデックス
     * @param listener      項目選択時に実行されるリスナー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static void showSingleChoiceItemDialog(
            final Activity          activity,
            final Drawable          icon,
            final DialogView        view,
            final String            title,
            final String[]          items,
            final int               checkedItem,
            final OnClickListener   listener
            ) {

        // アクティビティが null の場合
        if (activity == null) {

            throw new IllegalArgumentException();

        }

        // アラートダイアログビルダーを作成する
        final AlertDialog.Builder   builder = new AlertDialog.Builder(activity);

        // アイコンとタイトルと項目データを設定する
        builder.setIcon(icon);
        builder.setTitle(title == null ? "" : title);
        builder.setSingleChoiceItems(items, checkedItem, new OnClickListener() {

            /**
             * {@inheritDoc}
             * <br>
             * Handlerを介すことで dialog を閉じた場合でも<br>
             * 選択項目の描画反映まで見えるようにする。<br>
             */
            public void onClick(
                    final DialogInterface   dialog,
                    final int               which
                    ) {

                // ハンドラを作成する
                final Handler   handler = new Handler(activity.getMainLooper());

                // ハンドラへ処理を実行させる
                handler.post(new Runnable() {

                    public void run() {

                        // リスナーを実行する
                        listener.onClick(dialog, which);

                    }

                });

            }

        });

        // ダイアログを表示する
        showDialog(activity, builder, view);

    }


    /**
     * プログレスダイアログを表示する。
     *
     * @param activity      表示元アクティビティ
     * @param title         タイトル
     * @param message       メッセージ
     * @return 作成したプログレスダイアログ
     */
    public static ProgressDialog showProgressDialog(
            final Activity      activity,
            final String        title,
            final String        message
            ) {

        return showProgressDialog(activity, null, null, title, message);

    }


    /**
     * プログレスダイアログを表示する。
     *
     * @param activity      表示元アクティビティ
     * @param icon          アイコン
     * @param view          ダイアログビュー
     * @param title         タイトル
     * @param message       メッセージ
     * @return 作成したプログレスダイアログ
     */
    public static ProgressDialog showProgressDialog(
            final Activity      activity,
            final Drawable      icon,
            final DialogView    view,
            final String        title,
            final String        message
            ) {

        // プログレスダイアログを作成する
        final ProgressDialog    dialog = new ProgressDialog(activity);

        // 各種パラメータを設定する
        dialog.setIcon(icon);           // アイコン
        dialog.setTitle(title);         // タイトル
        dialog.setMessage(message);     // メッセージ

        // ダイアログビューがある場合
        if (view != null) {

            // マージンがある場合
            if (view.getMargin() != null) {

                // マージン付きでビューを設定する
                dialog.setView(
                        view.getView(),
                        view.getMargin().left,
                        view.getMargin().top,
                        view.getMargin().right,
                        view.getMargin().bottom
                        );

            } else {

                // デフォルトマージンでビューを設定する
                dialog.setView(view.getView());

            }

        }

        // 作成したダイアログを返却する
        return dialog;

    }




    /**
     * ダイアログに設定するビューを表すクラス。
     *
     * @author Kou
     *
     */
    public static class DialogView {


        /**
         * 元となるビュー
         */
        private final View      viewBase;

        /**
         * ビューのマージン
         */
        private final Rect      viewMargin;



        /**
         * ダイアログ表示ビューを作成する。
         *
         * @param view ビューのインスタンス
         */
        public DialogView(
                final View      view
                ) {

            viewBase    = view;
            viewMargin  = null;

        }


        /**
         * ダイアログ表示ビューを作成する。
         *
         * @param view          ビューのインスタンス
         * @param leftMargin    ビューの左マージン
         * @param topMargin     ビューの上マージン
         * @param rightMargin   ビューの右マージン
         * @param bottomMargin  ビューの下マージン
         */
        public DialogView(
                final View      view,
                final int       leftMargin,
                final int       topMargin,
                final int       rightMargin,
                final int       bottomMargin
                ) {

            viewBase    = view;
            viewMargin  = new Rect(leftMargin, topMargin, rightMargin, bottomMargin);

        }


        /**
         * 元となるビューを取得する。
         *
         * @return 元となるビュー
         */
        View getView() {

            return viewBase;

        }


        /**
         * ビューのマージン情報を取得する。
         *
         * @return ビューのマージン情報
         */
        Rect getMargin() {

            return viewMargin;

        }

    }

}
