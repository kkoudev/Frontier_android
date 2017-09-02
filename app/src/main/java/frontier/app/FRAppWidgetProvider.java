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
package frontier.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;


/**
 * ウィジェットのベースクラス。
 *
 * @author Kou
 *
 */
public abstract class FRAppWidgetProvider extends AppWidgetProvider {



    /**
     * ウィジェット更新用のリモートビューを取得する。
     *
     * @param context   利用するコンテキスト
     * @param layoutId  利用するレイアウトID
     * @return ウィジェット更新用のリモートビュー
     * @throws IllegalArgumentException 利用するコンテキストが null の場合
     */
    public static RemoteViews getRemoteViews(
            final Context   context,
            final int       layoutId
            ) {

        // 引数が不正の場合は例外
        if (context == null) {

            throw new IllegalArgumentException();

        }

        // リモートビューを作成して返す
        return new RemoteViews(context.getPackageName(), layoutId);

    }


    /**
     * ウィジェットの表示を更新する。
     *
     * @param context       利用するコンテキスト
     * @param remoteView    反映するリモートビュー
     * @throws IllegalArgumentException 利用するコンテキスト、リモートビュー、ウィジェットが null の場合
     */
    public void update(
            final Context       context,
            final RemoteViews   remoteView
            ) {

        update(context, remoteView, getClass());

    }


    /**
     * 指定されたウィジェットの表示を更新する。
     *
     * @param context       利用するコンテキスト
     * @param remoteView    反映するリモートビュー
     * @param widget        更新するウィジェット
     * @throws IllegalArgumentException 利用するコンテキスト、リモートビュー、ウィジェットが null の場合
     */
    public static void update(
            final Context                               context,
            final RemoteViews                           remoteView,
            final Class<? extends AppWidgetProvider>    widget
            ) {

        // 引数が不正の場合は例外
        if ((context == null) || (remoteView == null) || (widget == null)) {

            throw new IllegalArgumentException();

        }

        // ウィジェットの表示を更新する
        AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, widget),
                remoteView
                );

    }


}
