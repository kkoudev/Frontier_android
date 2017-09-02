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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * ビュー操作用ユーティリティクラス。
 *
 * @author nagaoka-koichi
 *
 */
public final class ViewUtils {


    /**
     * レイアウト : コンテンツサイズ
     */
    public static final int         WRAP_CONTENT    = LayoutParams.WRAP_CONTENT;

    /**
     * レイアウト : 親サイズ
     */
    public static final int         FILL_PARENT     = LayoutParams.FILL_PARENT;




    /**
     * インスタンス生成防止。
     */
    private ViewUtils() {

        // 処理なし

    }


    /**
     * レイアウトインフレーターを取得する。
     *
     * @param context   利用するコンテキスト
     * @return レイアウトインフレーター
     */
    public static LayoutInflater getLayoutInflater(
            final Context   context
            ) {

        // nullの場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }

        // レイアウトインフレーターのインスタンスを返す
        return (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    /**
     * レイアウトパラメータを作成する。
     *
     * @param width     レイアウト幅
     * @param height    レイアウト高さ
     * @param margin    マージン(px)
     * @return 作成したレイアウトパラメータ
     */
    public static ViewGroup.MarginLayoutParams createLayoutParams(
            final int   width,
            final int   height,
            final int   margin
            ) {

        // パラメータを作成する
        final ViewGroup.MarginLayoutParams param = new ViewGroup.MarginLayoutParams(
                width,
                height
                );

        // マージンを設定する
        param.setMargins(margin, margin, margin, margin);

        // 作成したパラメータを返す
        return param;

    }


    /**
     * 指定されたビューグループへビューを追加する。
     *
     * @param viewGroup     追加先ビューグループ
     * @param view          追加するビュー
     */
    public static void addView(
            final ViewGroup     viewGroup,
            final View          view
            ) {

        addView(
                viewGroup,
                view,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                0,
                0,
                0,
                0,
                0.0f
                );

    }


    /**
     * 指定されたビューグループへビューを追加する。
     *
     * @param viewGroup     追加先ビューグループ
     * @param view          追加するビュー
     * @param layoutWidth   レイアウトの幅
     * @param layoutHeight  レイアウトの高さ
     */
    public static void addView(
            final ViewGroup     viewGroup,
            final View          view,
            final int           layoutWidth,
            final int           layoutHeight
            ) {

        addView(
                viewGroup,
                view,
                layoutWidth,
                layoutHeight,
                0,
                0,
                0,
                0,
                0.0f
                );

    }


    /**
     * 指定されたビューグループへビューを追加する。
     *
     * @param viewGroup     追加先ビューグループ
     * @param view          追加するビュー
     * @param layoutWidth   レイアウトの幅
     * @param layoutHeight  レイアウトの高さ
     * @param weight        重み
     */
    public static void addView(
            final ViewGroup     viewGroup,
            final View          view,
            final int           layoutWidth,
            final int           layoutHeight,
            final float         weight
            ) {

        addView(
                viewGroup,
                view,
                layoutWidth,
                layoutHeight,
                0,
                0,
                0,
                0,
                weight
                );

    }


    /**
     * 指定されたビューグループへビューを追加する。
     *
     * @param viewGroup     追加先ビューグループ
     * @param view          追加するビュー
     * @param leftMargin    左マージン (px)
     * @param topMargin     上マージン (px)
     * @param rightMargin   右マージン (px)
     * @param bottomMargin  下マージン (px)
     */
    public static void addView(
            final ViewGroup     viewGroup,
            final View          view,
            final int           leftMargin,
            final int           topMargin,
            final int           rightMargin,
            final int           bottomMargin
            ) {

        addView(
                viewGroup,
                view,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin,
                0.0f
                );

    }


    /**
     * 指定されたビューグループへビューを追加する。
     *
     * @param viewGroup     追加先ビューグループ
     * @param view          追加するビュー
     * @param layoutWidth   レイアウトの幅
     * @param layoutHeight  レイアウトの高さ
     * @param margin        上下左右マージン
     */
    public static void addView(
            final ViewGroup     viewGroup,
            final View          view,
            final int           layoutWidth,
            final int           layoutHeight,
            final int           margin
            ) {

        addView(
                viewGroup,
                view,
                layoutWidth,
                layoutHeight,
                0.0f,
                margin
                );

    }


    /**
     * 指定されたビューグループへビューを追加する。
     *
     * @param viewGroup     追加先ビューグループ
     * @param view          追加するビュー
     * @param layoutWidth   レイアウトの幅
     * @param layoutHeight  レイアウトの高さ
     * @param margin        上下左右マージン
     * @param weight        重み
     */
    public static void addView(
            final ViewGroup     viewGroup,
            final View          view,
            final int           layoutWidth,
            final int           layoutHeight,
            final float         weight,
            final int           margin
            ) {

        addView(
                viewGroup,
                view,
                layoutWidth,
                layoutHeight,
                margin,
                margin,
                margin,
                margin,
                weight
                );

    }


    /**
     * 指定されたビューグループへビューを追加する。
     *
     * @param viewGroup     追加先ビューグループ
     * @param view          追加するビュー
     * @param layoutWidth   レイアウトの幅
     * @param layoutHeight  レイアウトの高さ
     * @param leftMargin    左マージン (px)
     * @param topMargin     上マージン (px)
     * @param rightMargin   右マージン (px)
     * @param bottomMargin  下マージン (px)
     */
    public static void addView(
            final ViewGroup     viewGroup,
            final View          view,
            final int           layoutWidth,
            final int           layoutHeight,
            final int           leftMargin,
            final int           topMargin,
            final int           rightMargin,
            final int           bottomMargin
            ) {

        addView(
                viewGroup,
                view,
                layoutWidth,
                layoutHeight,
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin,
                0.0f
                );

    }


    /**
     * 指定されたビューグループへビューを追加する。
     *
     * @param viewGroup     追加先ビューグループ
     * @param view          追加するビュー
     * @param layoutWidth   レイアウトの幅
     * @param layoutHeight  レイアウトの高さ
     * @param leftMargin    左マージン (px)
     * @param topMargin     上マージン (px)
     * @param rightMargin   右マージン (px)
     * @param bottomMargin  下マージン (px)
     * @param weight        重み
     */
    public static void addView(
            final ViewGroup     viewGroup,
            final View          view,
            final int           layoutWidth,
            final int           layoutHeight,
            final int           leftMargin,
            final int           topMargin,
            final int           rightMargin,
            final int           bottomMargin,
            final float         weight
            ) {

        // 引数が不正の場合は例外
        if ((viewGroup == null) || (view == null)) {

            throw new IllegalArgumentException();

        }

        // レイアウトを作成する
        final LinearLayout.LayoutParams  layoutParams =
            new LinearLayout.LayoutParams(layoutWidth, layoutHeight);

        // マージンを設定する
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        // 重みを設定する
        layoutParams.weight = weight;

        // ビューを追加する
        viewGroup.addView(view, layoutParams);

    }


    /**
     * 相対レイアウトにビューを追加する。
     *
     * @param layout                追加先相対レイアウト
     * @param view                  追加するビュー
     * @param layoutWidth           レイアウト幅
     * @param layoutHeight          レイアウト高さ
     * @param alignWithParent       親をアンカーにするかどうか
     * @param relativeRuleParams    追加ルールパラメータ
     */
    public static void addView(
            final RelativeLayout        layout,
            final View                  view,
            final int                   layoutWidth,
            final int                   layoutHeight,
            final boolean               alignWithParent,
            final RelativeRuleParams... relativeRuleParams
            ) {

        // 相対レイアウトパラメータを作成する
        final RelativeLayout.LayoutParams   params = new RelativeLayout.LayoutParams(
                layoutWidth,
                layoutHeight
                );

        // 親アンカー可否を設定する
        params.alignWithParent = alignWithParent;

        // ルール指定がある場合
        if (relativeRuleParams != null) {

            // 全ルールパラメータ文処理をする
            for (final RelativeRuleParams ruleParams : relativeRuleParams) {

                // アンカーがない場合
                if (ruleParams.getAnchor() == null) {

                    // ビューを追加する
                    params.addRule(ruleParams.getRule());

                } else {

                    // ビューを追加する
                    params.addRule(ruleParams.getRule(), ruleParams.getAnchor());

                }

            }

        }

        // ビューを追加する
        layout.addView(view, params);

    }


    /**
     * 指定されたアクティビティへビューを追加する。
     *
     * @param activity      追加先アクティビティ
     * @param view          追加するビュー
     */
    public static void addView(
            final Activity      activity,
            final View          view
            ) {

        addView(
                activity,
                view,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                0,
                0,
                0,
                0
                );

    }


    /**
     * 指定されたアクティビティへビューを追加する。
     *
     * @param activity      追加先アクティビティ
     * @param view          追加するビュー
     * @param layoutWidth   レイアウトの幅
     * @param layoutHeight  レイアウトの高さ
     */
    public static void addView(
            final Activity      activity,
            final View          view,
            final int           layoutWidth,
            final int           layoutHeight
            ) {

        addView(
                activity,
                view,
                layoutWidth,
                layoutHeight,
                0,
                0,
                0,
                0
                );

    }


    /**
     * 指定されたアクティビティへビューを追加する。
     *
     * @param activity      追加先アクティビティ
     * @param view          追加するビュー
     * @param leftMargin    左マージン (px)
     * @param topMargin     上マージン (px)
     * @param rightMargin   右マージン (px)
     * @param bottomMargin  下マージン (px)
     */
    public static void addView(
            final Activity      activity,
            final View          view,
            final int           leftMargin,
            final int           topMargin,
            final int           rightMargin,
            final int           bottomMargin
            ) {

        addView(
                activity,
                view,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin
                );

    }


    /**
     * 指定されたアクティビティへビューを追加する。
     *
     * @param activity      追加先アクティビティ
     * @param view          追加するビュー
     * @param layoutWidth   レイアウトの幅
     * @param layoutHeight  レイアウトの高さ
     * @param leftMargin    左マージン (px)
     * @param topMargin     上マージン (px)
     * @param rightMargin   右マージン (px)
     * @param bottomMargin  下マージン (px)
     */
    public static void addView(
            final Activity      activity,
            final View          view,
            final int           layoutWidth,
            final int           layoutHeight,
            final int           leftMargin,
            final int           topMargin,
            final int           rightMargin,
            final int           bottomMargin
            ) {

        // 引数が不正の場合は例外
        if ((activity == null) || (view == null)) {

            throw new IllegalArgumentException();

        }


        // レイアウトを作成する
        final ViewGroup.MarginLayoutParams   layoutParams =
            new ViewGroup.MarginLayoutParams(layoutWidth, layoutHeight);

        // マージンを設定する
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        // ビューを追加する
        activity.addContentView(view, layoutParams);

    }


    /**
     * 指定されたレイアウトでスピナーを作成する。
     *
     * @param <T>               設定項目データの型
     * @param spinner           スピナーを配置するコンテキスト
     * @param prompt            プロンプトメッセージ
     * @param layoutItemId      項目レイアウトID
     * @param layoutDropDownId  ドロップダウン項目レイアウトID
     * @param items             設定項目データ一覧
     */
    public static <T> void setSpinnerAdapter(
            final Spinner   spinner,
            final String    prompt,
            final int       layoutItemId,
            final int       layoutDropDownId,
            final List<T>   items
            ) {

        // 引数が不正の場合は例外
        if (spinner == null) {

            throw new IllegalArgumentException();

        }

        // アダプターを作成する
        final ArrayAdapter<T>     adapter =
            items == null
            ? new ArrayAdapter<T>(spinner.getContext(), layoutItemId)
            : new ArrayAdapter<T>(spinner.getContext(), layoutItemId, items);

        // ドロップダウン項目レイアウトを設定する
        adapter.setDropDownViewResource(layoutDropDownId);

        // スピナーに設定する
        spinner.setAdapter(adapter);

        // プロンプトメッセージを設定する
        spinner.setPrompt(prompt);

    }


    /**
     * 指定パスが有効な場合、そのビットマップを取得し、<br>
     * 無効である場合は無効画像を取得する。<br>
     *
     * @param imagePath         取得するイメージパス
     * @param defaultImage      イメージがない場合に取得するデフォルト画像
     * @return 取得したビットマップイメージ
     */
    public static Bitmap getBitmap(
            final String        imagePath,
            final Bitmap        defaultImage
            ) {

        // 画像が存在する場合
        if ((imagePath != null)
            && FileUtils.exists(imagePath)
            ) {

            // 画像を返す
            return ImageUtils.createBitmap(imagePath);

        } else {

            // デフォルト画像を返す
            return defaultImage;

        }
    }


    /**
     * 指定されたイメージビューに指定パスの画像を設定する。
     *
     * @param imageView         イメージビュー
     * @param imagePath         設定するイメージパス
     * @param noImage           イメージがない場合に設定する画像
     */
    public static void setImage(
            final ImageView     imageView,
            final String        imagePath,
            final Drawable      noImage
            ) {

        // 引数が null の場合は例外
        if (imageView == null) {

            throw new IllegalArgumentException();

        }

        // メモ画像が存在する場合
        if ((imagePath != null)
            && FileUtils.exists(imagePath)
            ) {

            // メモ画像を設定する
            imageView.setImageBitmap(
                    ImageUtils.createBitmap(imagePath)
                    );

        } else {

            // イメージがない場合に設定する画像を設定する
            imageView.setImageDrawable(noImage);

        }

    }


    /**
     * 指定されたイメージビューに指定パスの画像を設定する。
     *
     * @param imageView         イメージビュー
     * @param imagePath         設定するイメージパス
     * @param imageMaxWidth     読み込むイメージの最大幅
     * @param imageMaxHeight    読み込むイメージの最大高さ
     * @param noImage           イメージがない場合に設定する画像
     */
    public static void setImage(
            final ImageView     imageView,
            final String        imagePath,
            final int           imageMaxWidth,
            final int           imageMaxHeight,
            final Drawable      noImage
            ) {

        // 引数が null の場合は例外
        if (imageView == null) {

            throw new IllegalArgumentException();

        }

        // メモ画像が存在する場合
        if ((imagePath != null)
            && FileUtils.exists(imagePath)
            ) {

            // メモ画像を設定する
            imageView.setImageBitmap(
                    ImageUtils.createBitmap(
                            imagePath,
                            imageMaxWidth,
                            imageMaxHeight
                            )
                    );

        } else {

            // イメージがない場合に設定する画像を設定する
            imageView.setImageDrawable(noImage);

        }

    }


    /**
     * 指定されたイメージビューに画像を非同期で設定する。
     *
     * @param imageView         イメージビュー
     * @param prepareBitmap     準備中画像
     * @param settingImagePath  実際に設定する画像パス
     */
    public static void setAsyncImage(
            final ImageView     imageView,
            final Bitmap        prepareBitmap,
            final String        settingImagePath
            ) {

        // イメージビューが null の場合は例外
        if (imageView == null) {

            throw new IllegalArgumentException();

        }

        // イメージビューに準備中画像を設定する
        imageView.setImageBitmap(prepareBitmap);

        // 非同期タスクを実行する
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(
                    final Void... params
                    ) {

                // ビットマップを作成して返す
                return ResourceUtils.getBitmap(
                        imageView.getContext(),
                        settingImagePath
                        );

            }


            @Override
            protected void onPostExecute(final Bitmap result) {

                // イメージビューに画像を設定する
                imageView.setImageBitmap(result);

            }

        }
        .execute((Void[])null);

    }


    /**
     * 状態一覧描画オブジェクトを作成する。
     *
     * @param params    設定する状態一覧パラメータ
     * @return 作成した状態一覧描画オブジェクト
     */
    public static StateListDrawable createStateListDrawable(
            final StateListParams...    params
            ) {

        // パラメータが null の場合
        if (params == null) {

            throw new IllegalArgumentException();

        }

        // 状態一覧描画オブジェクトを作成する
        final StateListDrawable     stateListDrawable = new StateListDrawable();

        // 状態一覧パラメータ分処理をする
        for (final StateListParams param : params) {

            // 状態一覧描画オブジェクトへ追加する
            stateListDrawable.addState(
                    param.getStates(),
                    param.getDrawable()
                    );

        }

        // 作成した状態一覧描画オブジェクトを返す
        return stateListDrawable;

    }


    /**
     * 状態一覧描画オブジェクトをビューへ設定する。
     *
     * @param view      状態一覧描画オブジェクト設定先ビュー
     * @param params    設定する状態一覧パラメータ
     */
    public static void setStateListDrawable(
            final View                  view,
            final StateListParams...    params
            ) {

        // パラメータが null の場合
        if ((view == null) || (params == null)) {

            throw new IllegalArgumentException();

        }

        // 作成した状態一覧描画オブジェクトをビューの背景へ設定する
        view.setBackgroundDrawable(createStateListDrawable(params));

    }


    /**
     * 指定されたビューの再描画状態を設定する。
     *
     * @param view      ビュー
     * @param enable    再描画を有効にする場合は true。無効にする場合は false
     */
    public static void setInvalidateEnabled(
            final View      view,
            final boolean   enable
            ) {

        // 引数が不正の場合は例外
        if (view == null) {

            throw new IllegalArgumentException();

        }


        try {

            // 現在のフラグ値を取得する
            final int   flags = ReflectUtils.getInstanceField(
                    View.class,
                    view,
                    "mPrivateFlags"
                    ).getInt(view);

            // DRAWNを取得する
            final int   drawn = ReflectUtils.getClassField(
                    View.class,
                    "DRAWN"
                    ).getInt(null);

            // 再描画状態を変更する
            ReflectUtils.setInstanceFieldValue(
                    View.class,
                    view,
                    "mPrivateFlags",
                    (enable ? flags | drawn : flags & ~drawn)
                    );

        } catch (final Throwable e) {

            e.printStackTrace();

        }

    }


    /**
     * テキストビューの内容が変更されたときのイベント処理を設定する。
     *
     * @param textView              イベント設定先テキストビュー
     * @param changeEventListener   テキスト変更時に実行するイベント (null不可)
     * @param noChangeEventListener テキスト非変更時に実行するイベント (null可)
     */
    public static void setChangeTextEventListener(
            final TextView  textView,
            final Runnable  changeEventListener,
            final Runnable  noChangeEventListener
            ) {

        // 引数が null の場合は例外
        if ((textView == null) || (changeEventListener == null)) {

            throw new IllegalArgumentException();

        }

        // テキスト変更イベントを追加する
        textView.addTextChangedListener(new TextWatcher() {


            /**
             * 変更前のテキスト内容
             */
            private final String    beforeText = textView.getText().toString();



            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count
                    ) {

                // 処理なし

            }


            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after
                    ) {

                // 処理なし

            }


            public void afterTextChanged(
                    final Editable s
                    ) {

                // 変更前の内容と異なる場合
                if (!s.toString().equals(beforeText)) {

                    // イベント処理を実行する
                    changeEventListener.run();

                } else {

                    // 非変更時イベントが定義されている場合
                    if (noChangeEventListener != null) {

                        // 非変更時イベントを実行する
                        noChangeEventListener.run();

                    }

                }

            }

        });

    }


    /**
     * エディットテキストに指定された文言のエラーポップアップを表示する。
     *
     * @param editText      エディットテキスト
     * @param errorMessage  表示するエラーメッセージ
     */
    public static void setEditTextError(
            final EditText  editText,
            final String    errorMessage
            ) {

        // 引数が不正の場合は例外
        if (editText == null) {

            throw new IllegalArgumentException();

        }

        // エラーメッセージを設定する
        editText.setError(errorMessage);

        // メッセージが null の場合
        if (errorMessage == null) {

            // 処理終了
            return;

        }

        // エディットテキストへフォーカス要求を行う
        editText.requestFocus();

    }


    /**
     * 相対レイアウトパラメータ。
     *
     * @author Kou
     *
     */
    public static class RelativeRuleParams {


        /**
         * レイアウトルール
         */
        private final Integer       rule;

        /**
         * レイアウトアンカー
         */
        private final Integer       anchor;




        /**
         * 相対レイアウトパラメータを初期化する。
         *
         * @param argRule   レイアウトルール
         */
        public RelativeRuleParams(
                final Integer   argRule
                ) {

            this(argRule, null);

        }


        /**
         * 相対レイアウトパラメータを初期化する。
         *
         * @param argRule   レイアウトルール
         * @param argAnchor レイアウトアンカー
         */
        public RelativeRuleParams(
                final Integer   argRule,
                final Integer   argAnchor
                ) {

            rule    = argRule;
            anchor  = argAnchor;

        }


        /**
         * レイアウトルールを取得する。
         *
         * @return レイアウトルール
         */
        final Integer getRule() {

            return rule;

        }


        /**
         * レイアウトアンカーを取得する。
         *
         * @return レイアウトアンカー
         */
        final Integer getAnchor() {

            return anchor;

        }

    }


    /**
     * 状態一覧パラメータ。
     *
     * @author Kou
     *
     */
    public static class StateListParams {


        /**
         * 状態値の一覧
         */
        private final int[]     states;

        /**
         * 設定する描画オブジェクト
         */
        private final Drawable  drawable;



        /**
         * 状態一覧パラメータを初期化する。
         *
         * @param argDrawable   設定する描画オブジェクト
         * @param argStates     状態値の一覧
         */
        public StateListParams(
                final Drawable  argDrawable,
                final int...    argStates
                ) {

            // 描画パラメータが null の場合は例外
            if (argDrawable == null) {

                throw new IllegalArgumentException();

            }

            // 各種パラメータを設定する
            states   = argStates.clone();
            drawable = argDrawable;

        }


        /**
         * 状態値一覧を取得する。
         *
         * @return 状態値一覧
         */
        final int[] getStates() {

            return states == null ? new int[]{} : states;

        }


        /**
         * 設定する描画オブジェクトを取得する。
         *
         * @return 設定する描画オブジェクト
         */
        final Drawable getDrawable() {

            return drawable;

        }


    }

}
