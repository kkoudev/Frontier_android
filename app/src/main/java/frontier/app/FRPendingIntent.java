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

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import frontier.util.ReflectException;
import frontier.util.ReflectUtils;



/**
 * フレームワーク拡張ペンディングインテントクラス。
 *
 * @author Kou
 *
 */
public final class FRPendingIntent {



    /**
     * インスタンス生成防止。
     *
     */
    private FRPendingIntent() {

        // 処理なし

    }


    /**
     * ペンディングインテントを作成する。
     *
     * @param context       利用するコンテキスト
     * @param pendingType   ペンディングインテント種別
     * @param intent        ペンディングするインテント
     * @return 作成したペンディングインテント
     */
    public static PendingIntent createPendingIntent(
            final Context       context,
            final PendingType   pendingType,
            final Intent        intent
            ) {

        // 引数が不正の場合
        if ((context == null) || (pendingType == null) || (intent == null)) {

            throw new IllegalArgumentException();

        }


        final PendingIntent     pendingIntent;  // 返却するペンディングインテント


        try {

            // アラーム起動用ペンディングインテントを取得する
            pendingIntent = (PendingIntent)ReflectUtils.invokeStaticPublicMethod(
                    PendingIntent.class.getName(),
                    pendingType.getName(),
                    new Class<?>[] {

                            Context.class,
                            int.class,
                            Intent.class,
                            int.class

                    },
                    new Object[] {

                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT

                    }
                    );

        } catch (final ReflectException e) {

            throw new IllegalStateException(e);

        }


        // 作成したペンディングインテントを返す
        return pendingIntent;

    }




    /**
     * ペンディングインテント種別。
     *
     * @author Kou
     *
     */
    public static enum PendingType {


        /**
         * アクティビティ対象
         */
        ACTIVITY("getActivity"),

        /**
         * ブロードキャストレシーバ対象
         */
        BROADCAST("getBroadcast"),

        /**
         * サービス対象
         */
        SERVICE("getService");



        /**
         * 種別名
         */
        private final String    typeName;


        /**
         * ペンディング種別を初期化する。
         *
         * @param name 種別名
         */
        private PendingType(
                final String    name
                ) {

            typeName = name;

        }


        /**
         * ペンディング種別名を取得する。
         *
         * @return ペンディング種別名
         */
        public String getName() {

            return typeName;

        }

    }

}
