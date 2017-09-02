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
package frontier.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import frontier.util.ConvertUtils;
import frontier.util.GeneralUtils;


/**
 * 配列項目データアダプタクラス。
 *
 * @author Kou
 * @param <T> パラメータデータクラス
 */
public class ArrayItemAdapter<T> extends BaseAdapter {


    /**
     * 項目データ一覧
     */
    private final List<ItemViewInfo<T>>         adapterViews    = new ArrayList<ItemViewInfo<T>>();

    /**
     * ビューインスタンスごとの対応インデックス
     */
    private final Map<View, ItemViewInfo<T>>    adapterIndexes  = new HashMap<View, ItemViewInfo<T>>();

    /**
     * デフォルトパディング情報
     */
    private final Rect                          adapterPadding  = new Rect();

    /**
     * 項目ビュー作成リスナー
     */
    private final OnGetViewListener<T>          adapterGetViewListener;



    /**
     * 配列項目アダプタを初期化する。
     *
     * @param listener 項目ビュー作成リスナー
     */
    public ArrayItemAdapter(
            final OnGetViewListener<T>  listener
            ) {

        this(0, listener);

    }


    /**
     * 配列項目アダプタを初期化する。
     *
     * @param padding   項目のパディング
     * @param listener  項目ビュー作成リスナー
     */
    public ArrayItemAdapter(
            final int                       padding,
            final OnGetViewListener<T>      listener
            ) {

        this(padding, padding, padding, padding, listener);

    }


    /**
     * 配列項目アダプタを初期化する。
     *
     * @param leftPadding   項目の左パディング
     * @param topPadding    項目の上パディング
     * @param rightPadding  項目の右パディング
     * @param bottomPadding 項目の下パディング
     * @param listener      項目ビュー作成リスナー
     */
    public ArrayItemAdapter(
            final int                       leftPadding,
            final int                       topPadding,
            final int                       rightPadding,
            final int                       bottomPadding,
            final OnGetViewListener<T>      listener
            ) {

        this(leftPadding, topPadding, rightPadding, bottomPadding, (T[])null, listener);

    }


    /**
     * 指定された項目配列でアダプタを初期化する。
     *
     * @param padding   項目のパディング
     * @param items     設定する項目配列
     * @param listener  項目ビュー作成リスナー
     */
    public ArrayItemAdapter(
            final int                       padding,
            final T[]                       items,
            final OnGetViewListener<T>      listener
            ) {

        this(padding, padding, padding, padding, items, listener);

    }


    /**
     * 指定された項目配列でアダプタを初期化する。
     *
     * @param leftPadding   項目の左パディング
     * @param topPadding    項目の上パディング
     * @param rightPadding  項目の右パディング
     * @param bottomPadding 項目の下パディング
     * @param items         設定する項目配列
     * @param listener      項目ビュー作成リスナー
     */
    public ArrayItemAdapter(
            final int                       leftPadding,
            final int                       topPadding,
            final int                       rightPadding,
            final int                       bottomPadding,
            final T[]                       items,
            final OnGetViewListener<T>      listener
            ) {

        // リスナーを確保する
        adapterGetViewListener = listener;

        // デフォルトパディング情報を確保する
        adapterPadding.set(leftPadding, topPadding, rightPadding, bottomPadding);

        // 項目一覧が指定された場合
        if (items != null) {

            // 全項目を処理する
            for (final T view : items) {

                // 追加する
                addItem(view, leftPadding, topPadding, rightPadding, bottomPadding);

            }

        }

    }


    /**
     * 項目を追加する。
     *
     * @param item      追加する項目
     */
    public void addItem(
            final T  item
            ) {

        // nullの場合は例外
        if (item == null) {

            throw new IllegalArgumentException();

        }

        // 項目を一覧へ追加する
        adapterViews.add(
                new ItemViewInfo<T>(
                        item,
                        null
                        )
                );

    }


    /**
     * 項目を追加する。
     *
     * @param view      追加する項目
     * @param padding   項目の上下左右パディング
     */
    public void addItem(
            final T     view,
            final int   padding
            ) {

        addItem(view, padding, padding, padding, padding);

    }


    /**
     * 項目を追加する。
     *
     * @param item          追加する項目
     * @param leftPadding   項目の左パディング
     * @param topPadding    項目の上パディング
     * @param rightPadding  項目の右パディング
     * @param bottomPadding 項目の下パディング
     */
    public void addItem(
            final T     item,
            final int   leftPadding,
            final int   topPadding,
            final int   rightPadding,
            final int   bottomPadding
            ) {

        // nullの場合は例外
        if (item == null) {

            throw new IllegalArgumentException();

        }

        // 項目を一覧へ追加する
        adapterViews.add(
                new ItemViewInfo<T>(
                        item,
                        new Rect(
                            leftPadding,
                            topPadding,
                            rightPadding,
                            bottomPadding
                            )
                        )
                );

    }


    /**
     * 指定項目を削除する。
     *
     * @param item 削除する項目
     */
    public void removeItem(
            final T     item
            ) {

        // nullの場合は例外
        if (item == null) {

            throw new IllegalArgumentException();

        }

        // 指定した項目を削除する
        adapterViews.remove(new ItemViewInfo<T>(item, null));

    }


    /**
     * 指定位置のファイルを削除する。
     *
     * @param index 削除ファイル
     */
    public void removeItem(
            final int   index
            ) {

        adapterViews.remove(index);

    }


    /**
     * 全項目をクリアする。
     *
     */
    public void clear() {

        adapterViews.clear();

    }


    /**
     * 全項目のバンドルパラメータをクリアする。
     *
     */
    public void clearAllItemBundles() {

        // 項目情報分繰り返す
        for (final ItemViewInfo<T> item : adapterViews) {

            // バンドル情報をクリアする
            item.getBundle().clear();

        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {

        return adapterViews.size();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(
            final int index
            ) {

        return adapterViews.get(index).getItem();

    }


    /**
     * 指定位置の項目データを取得する。<br>
     * <br>
     * 返却値は {@link #getItem(int)} と同じである。<br>
     * {@link #getItem(int)} との違いは、型変換に自動対応しているかどうかである。<br>
     *
     * @param index 取得する項目位置
     * @return 指定位置の項目データ
     */
    public T getTypeItem(
            final int index
            ) {

        return adapterViews.get(index).getItem();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(
            final int index
            ) {

        return index;

    }


    /**
     * 指定項目のバンドルデータを取得する。
     *
     * @param index 取得するバンドルデータの項目位置
     * @return 指定項目のバンドルデータ
     */
    public Bundle getItemBundle(
            final int   index
            ) {

        return adapterViews.get(index).getBundle();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(
            final int       i,
            final View      view,
            final ViewGroup viewgroup
            ) {

        // リスナーが設定されている場合
        if (adapterGetViewListener != null) {

            final ItemViewInfo<T>   item    = adapterViews.get(i);  // 項目情報
            final Rect              padding = item.getPadding();    // パディング情報
            final View              retView;                        // 返却するビュー

            // ビューがキャッシュされていない場合
            if (view == null) {

                // 新しいビューを作成する
                retView = adapterGetViewListener.createItemView(
                        this,
                        i,
                        item.getItem()
                        );

            } else {

                // データ保存項目の場合
                if (view instanceof ItemViewSavable) {

                    final ItemViewSavable   savable     = (ItemViewSavable)view;
                    final ItemViewInfo<T>   targetItem  = adapterIndexes.get(view);

                    // 対応項目情報がある場合
                    if (targetItem != null) {

                        // データ保存処理を実行する
                        savable.onSaveInstanceState(
                                targetItem.getBundle()
                                );

                    }

                    // データ復帰処理を実行する
                    savable.onRestoreInstanceState(
                            item.getBundle()
                            );

                }

                // キャッシュされているビューを更新する
                adapterGetViewListener.updateItemView(
                        this,
                        view,
                        i,
                        item.getItem()
                        );

                // 更新したデータを設定する
                retView = view;

            }


            // パディング情報がある場合
            if (padding != null) {

                // パディングを設定する
                retView.setPadding(
                        padding.left,
                        padding.top,
                        padding.right,
                        padding.bottom
                        );

            } else {

                // デフォルトパディングを設定する
                retView.setPadding(
                        adapterPadding.left,
                        adapterPadding.top,
                        adapterPadding.right,
                        adapterPadding.bottom
                        );

            }


            // 対応項目情報を保存する
            adapterIndexes.put(retView, item);

            // 作成したビューを返す
            return retView;

        }

        // 引数のビューをそのまま返す
        return view;

    }



    /**
     * ビュー取得時の処理を定義するインターフェース。
     *
     * @author Kou
     *
     * @param <T> 項目データのクラス
     */
    public interface OnGetViewListener<T> {


        /**
         * 指定された項目情報データから項目ビューを作成する。
         *
         * @param adapter   アダプター情報
         * @param position  項目データ位置
         * @param item      項目情報データ
         * @return 作成した項目ビュー
         */
        View createItemView(
                final ArrayItemAdapter<T>   adapter,
                final int                   position,
                final T                     item
                );

        /**
         * 指定された項目ビューを更新する。
         *
         * @param adapter   アダプター情報
         * @param view      項目ビューのインスタンス
         * @param position  項目データ位置
         * @param item      項目情報データ
         */
        void updateItemView(
                final ArrayItemAdapter<T>   adapter,
                final View                  view,
                final int                   position,
                final T                     item
                );


    }



    /**
     * 項目ビューのデータ保存可能インターフェース。
     *
     * @author Kou
     *
     */
    public interface ItemViewSavable {


        /**
         * データ保存処理を行う。
         *
         * @param outState データ保存先バンドル
         */
        void onSaveInstanceState(
                final Bundle outState
                );


        /**
         * データ復帰処理を行う。
         *
         * @param savedInstanceState 保存したデータが格納されたバンドル
         */
        void onRestoreInstanceState(
                final Bundle savedInstanceState
                );


    }


    /**
     * 項目情報データ。
     *
     * @author Kou
     *
     */
    private static class ItemViewInfo<T> {


        /**
         * 項目データ
         */
        private final T         itemData;

        /**
         * 項目パディングデータ
         */
        private final Rect      itemPadding;

        /**
         * 項目別保存データ
         */
        private final Bundle    itemBundle = new Bundle();




        /**
         * 項目情報データを初期化する。
         *
         * @param data      項目データ
         * @param padding   項目パディング
         */
        public ItemViewInfo(
                final T     data,
                final Rect  padding
                ) {

            // データを設定する
            itemData = data;

            // パディングを設定する
            itemPadding = padding == null ? null : new Rect(padding);

        }


        /**
         * 項目データを取得する。
         *
         * @return 項目データ
         */
        public T getItem() {

            return itemData;

        }


        /**
         * 項目パディング情報を取得する。
         *
         * @return 項目パディング情報
         */
        public Rect getPadding() {

            return itemPadding;

        }


        /**
         * 項目別保存データを取得する。
         *
         * @return 項目別保存データ
         */
        public Bundle getBundle() {

            return itemBundle;

        }


        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(
                final Object    obj
                ) {

            // インスタンスが一致する場合
            if (obj instanceof ItemViewInfo) {

                final ItemViewInfo<T>   target = ConvertUtils.cast(obj);

                // 一致するかどうかを返す
                return GeneralUtils.equalsObject(itemData, target.itemData);

            }

            // 一致しない
            return false;

        }


        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {

            return itemData == null ? super.hashCode() : itemData.hashCode();

        }

    }

}
