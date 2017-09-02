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
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * ビュー配列アダプタクラス。
 *
 * @author Kou
 *
 */
public class ArrayViewAdapter extends BaseAdapter {


    /**
     * ビュー一覧
     */
    private final List<View>        adapterViews = new ArrayList<View>();



    /**
     * 配列ビューアダプタを初期化する。
     *
     */
    public ArrayViewAdapter() {

        this(0);

    }


    /**
     * 配列ビューアダプタを初期化する。
     *
     * @param padding 項目のパディング
     */
    public ArrayViewAdapter(
            final int   padding
            ) {

        this(padding, padding, padding, padding);

    }


    /**
     * 配列ビューアダプタを初期化する。
     *
     * @param leftPadding   項目の左パディング
     * @param topPadding    項目の上パディング
     * @param rightPadding  項目の右パディング
     * @param bottomPadding 項目の下パディング
     */
    public ArrayViewAdapter(
            final int   leftPadding,
            final int   topPadding,
            final int   rightPadding,
            final int   bottomPadding
            ) {

        this(leftPadding, topPadding, rightPadding, bottomPadding, (View[])null);

    }


    /**
     * 指定されたビュー配列でアダプタを初期化する。
     *
     * @param padding   項目のパディング
     * @param views     設定するビュー配列
     */
    public ArrayViewAdapter(
            final int           padding,
            final View...       views
            ) {

        this(padding, padding, padding, padding, views);

    }


    /**
     * 指定されたビュー配列でアダプタを初期化する。
     *
     * @param leftPadding   項目の左パディング
     * @param topPadding    項目の上パディング
     * @param rightPadding  項目の右パディング
     * @param bottomPadding 項目の下パディング
     * @param views         設定するビュー配列
     */
    public ArrayViewAdapter(
            final int           leftPadding,
            final int           topPadding,
            final int           rightPadding,
            final int           bottomPadding,
            final View...       views
            ) {

        // ビュー一覧が指定された場合
        if (views != null) {

            // 全ビューを処理する
            for (final View view : views) {

                // 追加する
                addItem(view, leftPadding, topPadding, rightPadding, bottomPadding);

            }

        }

    }


    /**
     * 項目を追加する。
     *
     * @param view      追加する項目
     */
    public void addItem(
            final View  view
            ) {

        addItem(view, 0);

    }


    /**
     * 項目を追加する。
     *
     * @param view      追加する項目
     * @param padding   項目の上下左右パディング
     */
    public void addItem(
            final View  view,
            final int   padding
            ) {

        addItem(view, padding, padding, padding, padding);

    }


    /**
     * 項目を追加する。
     *
     * @param view          追加する項目
     * @param leftPadding   項目の左パディング
     * @param topPadding    項目の上パディング
     * @param rightPadding  項目の右パディング
     * @param bottomPadding 項目の下パディング
     */
    public void addItem(
            final View  view,
            final int   leftPadding,
            final int   topPadding,
            final int   rightPadding,
            final int   bottomPadding
            ) {

        // nullの場合は例外
        if (view == null) {

            throw new IllegalArgumentException();

        }

        // パディングを設定する
        view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        // ビューを一覧へ追加する
        adapterViews.add(view);

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
     * {@inheritDoc}
     */
    public int getCount() {

        return adapterViews.size();

    }


    /**
     * {@inheritDoc}
     */
    public Object getItem(
            final int i
            ) {

        return adapterViews.get(i);

    }


    /**
     * {@inheritDoc}
     */
    public long getItemId(
            final int i
            ) {

        return i;

    }


    /**
     * {@inheritDoc}
     */
    public View getView(
            final int       i,
            final View      view,
            final ViewGroup viewgroup
            ) {

        // パラメータ設定したレイアウトを返す
        return (View)getItem(i);

    }

}
