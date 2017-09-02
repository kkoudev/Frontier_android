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

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;


/**
 * 通知操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class NotificationUtils {


    /**
     * インスタンス生成防止。
     */
    private NotificationUtils() {

        // 処理なし

    }


    /**
     * コンテキストから通知マネージャーを取得する。
     *
     * @param context 利用するコンテキスト
     * @return 通知マネージャー
     */
    private static NotificationManager getNotificationManager(
            final Context   context
            ) {

        return (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

    }


    /**
     * 指定された通知データをシステムへ通知する。
     *
     * @param context       利用するコンテキスト
     * @param notifyId      通知ID
     * @param notification  通知データ
     */
    public static void notify(
            final Context       context,
            final int           notifyId,
            final Notification  notification
            ) {

        // 引数が不正の場合は例外
        if ((context == null) || (notification == null)) {

            throw new IllegalArgumentException();

        }

        // 指定された通知データを通知する
        getNotificationManager(context).notify(notifyId, notification);

    }



}
